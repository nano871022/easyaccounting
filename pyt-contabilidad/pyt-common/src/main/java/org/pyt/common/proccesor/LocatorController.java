package org.pyt.common.proccesor;

import java.lang.reflect.Method;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.pyt.common.common.ABean;
import org.pyt.common.common.LoadAppFxml;
import org.pyt.common.common.Log;

/**
 * Se encarga de localizar el controlador
 * 
 * @author Alejandro Parra
 * @since 22/10/2018
 */
public class LocatorController {
	private static LocatorController locator;

	private LocatorController() {

	}
	/**
	 * Se encarga de obtener el controlador
	 * @param nombre
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private final <T extends FXMLFileController> T getController(String nombre) {
		try {
			String clase = "org.pyt.app.beans.controllers." + nombre + "Controller.class";
			Class clases = Class.forName(clase);
			T instancia = (T) clases.getDeclaredConstructor().newInstance();
			return instancia;
		} catch (Exception e) {
			Log.logger(e);
		}
		return null;
	}
	/**
	 * se ecnarga de realizar un caller
	 * @param caller {@link String}
	 * @param valor {@link Object}
	 * @return {@link Object}
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public <T extends Object, S extends Object, L extends FXMLFileController, I extends ABean> T call(String caller,
			S valor) {
		try {
			String regex = "#{w+\\.w+}";
			Pattern patron = Pattern.compile(regex);
			Matcher cruce = patron.matcher(caller);
			if (cruce.matches()) {
				caller = caller.replace("#{", "");
				caller = caller.replaceAll("}", "");
				String[] split = caller.split("\\.");
				L controller = getController(split[0]);
				if (controller != null) {
					I bean = (I) LoadAppFxml.BeanFxml(controller.getController());
					Class<I> claseBean = (Class<I>) bean.getClass();
					if(StringUtils.isNotBlank(split[1])) {
						if(valor != null) {
							String setName = "set" + split[1].substring(0, 1).toUpperCase() + split[1].substring(1);
							Method metodo = claseBean.getDeclaredMethod(setName, valor.getClass());
							metodo.invoke(bean, valor);
						}else {
							String getName = "get" + split[1].substring(0, 1).toUpperCase() + split[1].substring(1);
							Method metodo = claseBean.getDeclaredMethod(getName);
							return (T) metodo.invoke(bean);
						}
					}
				}
			}
		} catch (Exception e) {
			Log.logger(e);
		}
		return null;
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
}
