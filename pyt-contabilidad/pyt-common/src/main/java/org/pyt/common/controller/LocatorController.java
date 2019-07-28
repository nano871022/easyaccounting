package org.pyt.common.controller;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.pyt.common.common.LoadAppFxml;
import org.pyt.common.common.Log;
import org.pyt.common.constants.ReflectionConstants;
import org.pyt.common.interfaces.IBean;
import org.pyt.common.reflection.ReflectionUtils;

import co.com.arquitectura.annotation.proccessor.FXMLFile;

/**
 * Se encarga de localizar el controlador
 * 
 * @author Alejandro Parra
 * @since 22/10/2018
 */
public class LocatorController {
	private static LocatorController locator;
	public final static String REGEX = "[#]{1}[{]{1}[a-zA-Z0-9]+[.]{1}[a-zA-Z0-9]+[}]{1}";
	public final static String PACKAGE_CONTROLLER_PRE = "org.pyt.app.beans.controllers.";
	public final static String PACKAGE_CONTROLLER_LAST = "Controller";
	@SuppressWarnings("rawtypes")
	private Class clase;
	private Log logger = Log.Log(this.getClass());

	private LocatorController() {

	}

	/**
	 * Se encarga de obtener el controlador
	 * 
	 * @param nombre
	 * @return
	 */
	@SuppressWarnings({ "unchecked", "static-access", "rawtypes" })
	private final <T extends FXMLFileController> T getController(String nombre) throws Exception {
		String clase = PACKAGE_CONTROLLER_PRE + nombre + PACKAGE_CONTROLLER_LAST;
		Class clases = null;
//			logger.info("Clase a cargar :: "+clase);
		if (this.clase == null) {
			clases = Class.forName(clase);
		} else {
			clases = this.clase.forName(clase);
		}
		T instancia = (T) clases.getDeclaredConstructor().newInstance();
		return instancia;
	}

	/**
	 * se ecnarga de realizar un caller
	 * 
	 * @param caller {@link String}
	 * @param valor  {@link Object}
	 * @return {@link Object}
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public <T extends Object, S extends Object, L extends FXMLFileController, I extends IBean> T call(String caller,
			S valor) {
		try {
//			logger.info("Buscando "+caller);
			I bean = null;
			Pattern patron = Pattern.compile(REGEX);
			Matcher cruce = patron.matcher(caller);
			if (cruce.matches()) {
				caller = caller.replace("#{", "");
				caller = caller.replaceAll("}", "");
				String[] split = caller.split("\\.");
				L controller = getController(split[0]);
				if (controller != null) {

					bean = (I) controller.getFXMLFile(controller.getController());
					if (bean == null) {
						bean = (I) LoadAppFxml.BeanFxml(controller.getController());
					}
					if (bean != null) {
						var claseBean = (Class<I>) bean.getClass();
						if (StringUtils.isNotBlank(split[1])) {
							if (valor != null) {
								var setName = ReflectionUtils.instanciar().getNameMethod(ReflectionConstants.SET,
										split[1]);
								var metodo = claseBean.getDeclaredMethod(setName, valor.getClass());
								metodo.invoke(bean, valor);
							} else {
								var getName = ReflectionUtils.instanciar().getNameMethod(ReflectionConstants.GET,
										split[1]);
								var metodo = claseBean.getDeclaredMethod(getName);
								return (T) metodo.invoke(bean);
							}
						}
					}
				}
			} else {
				logger.error("Se encontro problema con " + caller + " ya que no cruza con " + REGEX);
			}
		} catch (Exception e) {
			logger.logger(e);
		}
		return null;
	}

	/**
	 * Se encarga de obtener el controlador buscado
	 * 
	 * @param controller {@link Class}
	 * @return {@link Class}
	 * @throws {@link Exception}
	 */
	@SuppressWarnings({ "rawtypes" })
	private final <T extends IBean, L extends FXMLFileController> L locator(Class<T> controller) throws Exception {
		FXMLFile fxml = controller.getAnnotation(FXMLFile.class);
		try {
			if (StringUtils.isNotBlank(fxml.name()))
				return getController(fxml.name());
			else
				throw new Exception("Buscar otro");
		} catch (Exception e) {
			try {
				return getController(controller.getSimpleName());
			} catch (ClassNotFoundException e1) {
				throw new Exception("No se encontro el controlador " + controller.getSimpleName() + " solicitado.");
			}
		}
	}

	/**
	 * Se encarga de poner el controlador sobre la clase que carga los controladores
	 * para ser usados
	 * 
	 * @param controller {@link Class}
	 * @throws {@link Exception}
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public final <T extends IBean, L extends FXMLFileController> void putLoadInController(T control) throws Exception {
		L instancia = locator(control.getClass());
		if (instancia != null) {
			instancia.put(control);
		}
	}

	/**
	 * Se encarga de remover el controladore cargado de la clase que lo carga
	 * 
	 * @param controller {@link class}
	 * @throws {@link Exception}
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public final <T extends IBean, L extends FXMLFileController> void removeLoadInController(T controller)
			throws Exception {
		L instancia = locator(controller.getClass());
		if (instancia != null) {
			instancia.destroy(instancia.getController());
		}
	}

	/**
	 * Se encarag de obtener una instancia
	 * 
	 * @return {@link LocatorController}
	 */
	public final static LocatorController getInstance() {
		if (locator == null) {
			locator = new LocatorController();
		}
		return locator;
	}

	/**
	 * Configura la clase a usar para realzar el forname para que no se haga desde
	 * common
	 * 
	 * @param clase {@link Class} para realizar el forname
	 * @return {@link LocatorController} locator siempre retorna el mosmo
	 */
	@SuppressWarnings("rawtypes")
	public final LocatorController setClass(Class clase) {
		this.clase = clase;
		return locator;
	}

	public final static void main(String... strings) {
		String caller = "#{bean.metodo}";
		Pattern patron = Pattern.compile(REGEX);
		Matcher cruce = patron.matcher(caller);
		Boolean re = cruce.matches();
		System.out.println(caller + " " + REGEX + " " + re);
	}
}
