package co.com.japl.ea.service.proccess;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Arrays;

import org.apache.commons.lang3.StringUtils;
import org.pyt.common.common.Log;
import org.pyt.common.constants.AppConstants;
import org.pyt.common.constants.ConfigServiceConstant;
import org.pyt.common.constants.DataPropertiesConstants;
import org.pyt.common.constants.PropertiesConstants;

import co.com.arquitectura.librerias.implement.Services.ServicePOJO;
import co.com.arquitectura.librerias.implement.listProccess.AbstractListFromProccess;
import co.com.japl.ea.common.abstracts.ADto;
import co.com.japl.ea.common.properties.PropertiesUtils;
import co.com.japl.ea.common.reflection.ReflectionUtils;
import co.com.japl.ea.exceptions.MarcadorServicioException;
import co.com.japl.ea.exceptions.ProccesConfigServiceException;
import co.com.japl.ea.exceptions.ReflectionException;

/**
 * Se encarga de realizar el procesamiento de los servicios
 * 
 * @author Alejandro Parra
 * @since 19/11/2018
 */
public final class ProccesConfigService {
	private Log logger = Log.Log(this.getClass());
	private String servicePackageList;
	
	public ProccesConfigService() {
		try {
		servicePackageList = PropertiesUtils.getInstance().setNameProperties(PropertiesConstants.PROP_DATA).load().getProperties().getProperty(DataPropertiesConstants.CONST_SERVICES_LIST);
		if(StringUtils.isBlank(servicePackageList)) {
			servicePackageList = servicePackageList;
		}
		}catch(Exception e) {
			logger.logger(e);
		}
	}
	/**
	 * Se encarga de obtener el objeto dentro de los parametros en el cual se encuentra el campo
	 * @param servicio {@link String}
	 * @param campo {@link String}
	 * @return {@link ADto} extends
	 * @throws {@link ProccesConfigServiceException}
	 */
	@SuppressWarnings({ "unchecked" })
	public <T extends Object, S extends ADto, C extends Object, I extends Object> S getDTOService(String servicio,
			String campo) throws ProccesConfigServiceException {
		try {
			String[] split = campo.split(ConfigServiceConstant.SEP_2_DOTS);
			Method method = getMetodo(servicio);
			if (method != null) {
				for (Parameter parametro : method.getParameters()) {
					if (parametro.getParameterizedType().getTypeName().contains(split[0])) {
						Class<C> clazz = (Class<C>) Class.forName(parametro.getParameterizedType().getTypeName());
						I instance = (I) clazz.getConstructor().newInstance();
						if (instance instanceof ADto) {
							S ret = (S) instance;
							return ret;
						}
						return (S) instance;
					}
				}
			}
		} catch (ClassNotFoundException e) {
			throw new ProccesConfigServiceException("No se encontro clase.",e);
		} catch (InvocationTargetException e) {
			throw new ProccesConfigServiceException("No se pudo realizar la invocacion.",e);
		} catch (IllegalAccessException e) {
			throw new ProccesConfigServiceException("Acceso ilegar.",e);
		} catch (InstantiationException e) {
			throw new ProccesConfigServiceException("No se logra realizar la instanciacion.",e);
		} catch (NoSuchMethodException e) {
			throw new ProccesConfigServiceException("No se encontro el metod solicitado.",e);
		}
		return null;
	}

	/**
	 * Se encargca de obtener el metod asociado al servicio con nombre indicado
	 * 
	 * @param servicio
	 *            {@link String}
	 * @return {@link Method}
	 * @throws {@link
	 *             ProccesConfigServiceException}
	 */
	@SuppressWarnings({ "unchecked", "rawtypes", "static-access" })
	public final Method getMetodo(String servicio) throws ProccesConfigServiceException {
		AbstractListFromProccess<ServicePOJO> listServices;
		try {
			listServices = (AbstractListFromProccess) this.getClass().forName(servicePackageList)
					.getConstructor().newInstance();
			for (ServicePOJO service : listServices.getList()) {
				if ((service.getClasss().getSimpleName() + ConfigServiceConstant.SEP_2_DOT + service.getAlias()).contains(servicio)) {
					return service.getClasss().getMethod(service.getName(), getParameters(service.getParameter()));
				}
			}
			return null;
		} catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException
				| NoSuchMethodException | SecurityException | ClassNotFoundException e) {
			throw new ProccesConfigServiceException("Problema en la obtencion del metodo de invocacion.", e);
		}
	}

	/**
	 * Se encarga de obtener una instancia del serivico solicitado
	 * 
	 * @param servicio
	 *            {@link String}
	 * @return {@link Object}
	 * @throws {@link
	 *             MarcadorServicioException}
	 */
	@SuppressWarnings({ "static-access", "unchecked", "rawtypes" })
	public final <T extends Object> T getService(String servicio) throws ProccesConfigServiceException {
		try {
			AbstractListFromProccess<ServicePOJO> listServices = (AbstractListFromProccess) this.getClass()
					.forName(servicePackageList).getConstructor().newInstance();
			for (ServicePOJO service : listServices.getList()) {
				if ((service.getClasss().getSimpleName() + ConfigServiceConstant.SEP_2_DOT + service.getAlias()).contains(servicio)) {
					return (T) service.getClasss().getConstructor().newInstance();
				}
			}
			return null;
		} catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException
				| NoSuchMethodException | SecurityException e) {
			throw new ProccesConfigServiceException("Problema en la obtencion del servicio.",e);
		} catch (ClassNotFoundException e) {
			throw new ProccesConfigServiceException("No se enconetro la clase.",e);
		}
	}

	/**
	 * Se encarga de invocar un servicio y obtener el resultado del mismo
	 * 
	 * @param service
	 *            {@link Object}
	 * @param metodo
	 *            {@link String}
	 * @param dto
	 *            {@link ADto}
	 * @return {@link Object}
	 * @throws {@link
	 *             MarcadorServicioException}
	 */
	@SuppressWarnings("unchecked")
	public <T extends Object, L extends Object, S extends ADto> T getResultService(L service, String metodo, S dto)
			throws MarcadorServicioException {
		T result = null;
		try {
			result = (T) ReflectionUtils.instanciar().invoke(service, metodo, dto);
		} catch (IllegalArgumentException e) {
			throw new MarcadorServicioException(e);
		} catch (ReflectionException e) {
			throw new MarcadorServicioException("Problema con la invocadion del servicio.", e);
		}
		return result;
	}

	/**
	 * Se encarga de obtener todas las clase apartir de los canonic name
	 * suministrado
	 * 
	 * @param parameters
	 *            {@link String} {@link Arrays}
	 * @return {@link Class} {@link Arrays}
	 * @throws {@link
	 *             ProccesConfigServiceException}
	 */
	@SuppressWarnings("rawtypes")
	private Class[] getParameters(String... parameters) throws ProccesConfigServiceException {
		Class[] clazz = null;
		try {
			if (parameters != null && parameters.length > 0) {
				clazz = new Class[parameters.length];
				int i = 0;
				for (String parameter : parameters) {
					clazz[i] = Class.forName(parameter);
					i++;
				}
			}
		} catch (ClassNotFoundException e) {
			throw new ProccesConfigServiceException("No se logro obtener la clase.", e);
		}
		return clazz;
	}
}
