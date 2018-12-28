package org.pyt.common.common;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.pyt.common.constants.AppConstants;
import org.pyt.common.constants.ConfigServiceConstant;
import org.pyt.common.constants.DtoConstants;
import org.pyt.common.exceptions.ReflectionException;
import org.pyt.common.exceptions.SearchServicesException;
import org.pyt.common.reflection.ReflectionUtils;

import co.com.arquitectura.librerias.implement.Services.ServicePOJO;
import co.com.arquitectura.librerias.implement.listProccess.AbstractListFromProccess;

/**
 * Se encarga de buscar un servicio en especifico en la lista de servicios y se
 * busca el servicio dentro del objeto indicado compararando los diferentes
 * metodos para obtener el unico que sea igual al que se indico de la lista de
 * servicios Campo excludeUsuarioDTo por defecto es TRUE y cuando se generen los
 * campos este no sera procesado si es {@link usuarioDTO}
 * 
 * @author Alejandro Parra
 * @sine 27/12/2018
 */
public class SearchService {
	private List<String> campos = new ArrayList<String>();
	private AbstractListFromProccess<ServicePOJO> listServices;
	private Boolean excludeUsuarioDTO;
	protected Log logger = Log.Log(this.getClass());

	@SuppressWarnings({ "unchecked", "rawtypes", "static-access" })
	private SearchService() throws SearchServicesException {
		campos = new ArrayList<String>();
		excludeUsuarioDTO = true;
		try {
			listServices = (AbstractListFromProccess) this.getClass().forName(AppConstants.PATH_LIST_SERVICE)
					.getConstructor().newInstance();
		} catch (InstantiationException e) {
			throw new SearchServicesException("Error en instanciación.", e);
		} catch (IllegalAccessException e) {
			throw new SearchServicesException("Acceso ilegal.", e);
		} catch (IllegalArgumentException e) {
			throw new SearchServicesException("Argumento ilegal.", e);
		} catch (InvocationTargetException e) {
			throw new SearchServicesException("Objetivo de invocación.", e);
		} catch (NoSuchMethodException e) {
			throw new SearchServicesException("No se encontro metodo.", e);
		} catch (SecurityException e) {
			throw new SearchServicesException("Error en seguridad.", e);
		} catch (ClassNotFoundException e) {
			throw new SearchServicesException("No se encontro clase.", e);
		}
	}

	public final static SearchService getInstance() throws SearchServicesException {
		return new SearchService();
	}

	/**
	 * Se encarga de obtener las instancias de los Strings los cuales son los
	 * parametros que se deben pasar a los servicos.
	 * 
	 * @param parameters
	 *            {@link String} array
	 * @return Array {@link Object}
	 * @throws {@link
	 *             SearchServicesException}
	 */
	@SuppressWarnings({ "unchecked" })
	private final <P extends Object> P[] stringToObjectParameters(String... parameters) throws SearchServicesException {
		if (parameters == null || parameters.length == 0) {
			throw new SearchServicesException("No se suministraron parametros.");
		}
		List<P> parametros = new ArrayList<P>();
		for (String parameter : parameters) {
			try {
				parametros.add((P) Class.forName(parameter).getConstructor().newInstance());
			} catch (InstantiationException e) {
				throw new SearchServicesException("Error en instanciación.", e);
			} catch (IllegalAccessException e) {
				throw new SearchServicesException("Acceso ilegal.", e);
			} catch (IllegalArgumentException e) {
				throw new SearchServicesException("Argumento ilegal.", e);
			} catch (InvocationTargetException e) {
				throw new SearchServicesException("Error en objetivo de invocación.", e);
			} catch (NoSuchMethodException e) {
				throw new SearchServicesException("No se encontro metodo.", e);
			} catch (SecurityException e) {
				throw new SearchServicesException("Error en seguridad.", e);
			} catch (ClassNotFoundException e) {
				throw new SearchServicesException("Clase no encontrada.", e);
			}
		}
		return (P[]) parametros.toArray();
	}

	/**
	 * Se encarga de obtener los campos del servicio indicado
	 * 
	 * @param service
	 *            {@link String} nombre del servicio
	 * @return {@link List} < {@link String} >
	 * @throws {@link
	 *             SearchServicesException}
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public final <T extends Object, P extends Object> List<String> getCampos(String service)
			throws SearchServicesException {
		P[] parametros = null;
		for (ServicePOJO servicio : listServices.getList()) {
			if ((servicio.getClasss().getSimpleName() + ConfigServiceConstant.SEP_2_DOT + servicio.getAlias())
					.contentEquals(service)) {
				try {
					parametros = stringToObjectParameters(servicio.getParameter());
					Method metodo = ReflectionUtils.instanciar().getMethod(servicio.getClasss(), servicio.getName(),
							parametros);
					Class retorno = metodo.getReturnType();

					if (retorno == List.class) {
						addFieldByType(metodo, retorno);
					} else {
						T obj = (T) retorno.getDeclaredConstructor().newInstance();
						addCampos(obj, retorno, retorno.getSimpleName());
					}
				} catch (ClassNotFoundException e) {
					throw new SearchServicesException("No se encontró clase.", e);
				} catch (NoSuchMethodException e) {
					throw new SearchServicesException("No se encontró método.", e);
				} catch (SecurityException e) {
					throw new SearchServicesException("Error en seguridad.", e);
				} catch (InstantiationException e) {
					throw new SearchServicesException("Error en instanciación.", e);
				} catch (IllegalAccessException e) {
					throw new SearchServicesException("Error en acceso ilegal", e);
				} catch (IllegalArgumentException e) {
					throw new SearchServicesException("Error en argumento ilegal.", e);
				} catch (InvocationTargetException e) {
					throw new SearchServicesException("Error en invocación.", e);
				} catch (ReflectionException e) {
					throw new SearchServicesException("Error en Reflection Utils.", e);
				}
				break;
			}
		} // end for
		return campos;
	}

	/**
	 * Obteine todos los campos a poner en la lsita desplegable despues de
	 * seleccionar el servicio
	 * 
	 * @param service
	 *            {@link ServicePOJO}
	 * @return {@link List} < {@link String} >
	 * @throws {@link
	 *             SearchServicesException}
	 */
	@SuppressWarnings("rawtypes")
	public final <P extends Object> List<String> putCamposServicios(ServicePOJO service)
			throws SearchServicesException {
		List<String> listaCampos = new ArrayList<String>();
		try {
			if(service == null)throw new SearchServicesException("No se suministro el ServicePOJO a procesar."); 
			P[] parameterss = stringToObjectParameters(service.getParameter());
			Method metodoUso;
			metodoUso = ReflectionUtils.instanciar().getMethod(service.getClasss(), service.getName(), parameterss);
			if (metodoUso != null) {
				Class[] parametros = metodoUso.getParameterTypes();
				Parameter[] parameters = metodoUso.getParameters();
				if (parametros != null && parametros.length > 0) {
					Arrays.asList(parametros).forEach(parametro -> addCampos(listaCampos, parametro));
				} else if (parameters != null && parameters.length > 0) {
					List<Class> lstParametros = new ArrayList<Class>();
					Arrays.asList(parameters).forEach(parameter -> {
						lstParametros.add(parameter.getType());
					});
					parametros = lstParametros.toArray(new Class[lstParametros.size()]);
					if (parametros != null && parametros.length > 0) {
						Arrays.asList(parametros).forEach(parametro -> addCampos(listaCampos, parametro));
					} else {
						throw new SearchServicesException("Imposible obtener los campos de entrada de los servicios.");
					}
				} else {
					logger.error("No se encontraron parametros para " + metodoUso);
				}
			}
		} catch (ReflectionException e) {
			throw new SearchServicesException("Se presento error con la reflección.", e);
		} catch (ClassNotFoundException e) {
			throw new SearchServicesException("No se encontro la clase.", e);
		}
		return listaCampos;
	}

	/**
	 * Se encarga de agregar los campos a la lsita e campos a usar
	 * 
	 * @param listaCampos
	 *            {@link List} of {@link String}
	 * @param parametro
	 *            {@link Class} clase en la cual se buscan los cmpos
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private final void addCampos(List<String> listaCampos, Class parametro) {
		if(excludeUsuarioDTO && parametro.isAssignableFrom(UsuarioDTO.class)) {
			return;
		}
		List<String> campos = allFields(parametro);
		campos.forEach(campo -> listaCampos.add(parametro.getSimpleName() + ConfigServiceConstant.SEP_2_DOTS + campo));
	}

	/**
	 * Se encarga de obtener tod slos nombre de los campos dentro de la clase
	 * suministrada
	 * 
	 * @param parametro
	 *            {@link Class}
	 * @return {@link List} of {@link String}
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private final <T extends Object> List<String> allFields(Class parametro) {
		List<String> list = new ArrayList<String>();
		T instance = null;
		try {
			instance = (T) parametro.getConstructor().newInstance();
			if (instance instanceof ADto) {
				ReflectionUtils.instanciar().getMethod(parametro, DtoConstants.GET_NAME_FIELDS);
				return ((ADto) instance).getNameFields();
			} else {
				list.add(parametro.getName());
				return list;
			}
		} catch (Exception e) {
			logger.logger("Se presento error en obtener todos los campos.", e);
		}
		return list;
	}

	/**
	 * Se encarga de agreagr los campos ssegun los datos suministrados
	 * 
	 * @param obj
	 *            {@link Object} extends instancia del objeto a obtener los campos
	 *            el campo debe conntener getNameFields
	 * @param clase
	 *            {@link Class} Clase que se usa para obtener el nombre del campo a
	 *            usar
	 * @param retornoSimpleName
	 *            {@link String} Nombe del campo al cual se retornara.
	 * @throws {@link
	 *             SearchServicesException}
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private final <T extends Object> void addCampos(T obj, Class clase, String retornoSimpleName)
			{
		try {
			if (obj instanceof ADto) {
				if( excludeUsuarioDTO && obj instanceof UsuarioDTO) {
					return;
				}
				List<String> campos;
				campos = (List<String>) ReflectionUtils.instanciar().invoke(obj, AppConstants.GET_NAME_FIELDS);
				for (String campo : campos) {
					this.campos.add(clase.getSimpleName() + ConfigServiceConstant.SEP_2_DOTS + campo);
				}
			} else {
				campos.add(retornoSimpleName);
			}
		} catch (ReflectionException e) {
			logger.logger("Problema en la utilidad de reflección.", e);
		}
	}

	/**
	 * Se encarga de agregar los campos segun el {@link Type} confiigurado en el
	 * retorno del metodo
	 * 
	 * @param metodo
	 *            {@link Method}
	 * @param retorno
	 *            {@link Class}
	 * @throws {@link
	 *             SearchServicesException}
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private <T extends Object> void addFieldByType(Method metodo, Class retorno) throws SearchServicesException {
		try {
			Type tipoRetorno = metodo.getGenericReturnType();
			if (tipoRetorno instanceof ParameterizedType) {
				ParameterizedType type = (ParameterizedType) tipoRetorno;
				Type[] typeArguments = type.getActualTypeArguments();
				for (Type typeArgument : typeArguments) {
					Class clase = (Class) typeArgument;
					T obj = (T) clase.getDeclaredConstructor().newInstance();
					addCampos(obj, clase, retorno.getSimpleName());
				}
			}
		} catch (IllegalAccessException e) {
			throw new SearchServicesException("Acceso Ilegal.", e);
		} catch (IllegalArgumentException e) {
			throw new SearchServicesException("Argumento Ilegal.", e);
		} catch (InvocationTargetException e) {
			throw new SearchServicesException("Error en objetivo de invocación.", e);
		} catch (NoSuchMethodException e) {
			throw new SearchServicesException("No se encontro metodo.", e);
		} catch (SecurityException e) {
			throw new SearchServicesException("Error en seguridad.", e);
		} catch (InstantiationException e) {
			throw new SearchServicesException("Error en instanciación.", e);
		}
	}

	public Boolean getExcludeUsuarioDTO() {
		return excludeUsuarioDTO;
	}

	public void setExcludeUsuarioDTO(Boolean excludeUsuarioDTO) {
		this.excludeUsuarioDTO = excludeUsuarioDTO;
	}

}
