package org.pyt.common.controller;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.pyt.common.common.ABean;

import co.com.arquitectura.annotation.proccessor.FXMLFile;

/**
 * Se encarga de usar para generalizar el proceso de obtencion del controlador
 * fxmlfile
 * 
 * @author Alejandro Parra
 * @since 22/10/2018
 * @param <T>
 */
@SuppressWarnings("rawtypes")
public abstract class FXMLFileController<T extends ABean> {
	protected Class<T> controller;
	private static Map<String,Object> instancias;

	/**
	 * constructor
	 */
	public FXMLFileController() {
		load();
	}

	/**
	 * Se encara de cargar la contruccion del controlador
	 */
	public abstract void load();

	/**
	 * Retorana el controlador asociado
	 * 
	 * @return {@link Class}
	 */
	public final Class<T> getController() {
		return controller;
	}

	/**
	 * Se encarga devalidar si el alias suministrado es igual al configurado en la
	 * anotacion
	 * 
	 * @param alias
	 *            {@link String}
	 * @return {@link Class}
	 * @throws {@link
	 *             Exception}
	 */
	public final Class<T> valid(String alias) throws Exception {
		if (controller != null && StringUtils.isNotBlank(alias)) {
			FXMLFile fxml = controller.getDeclaredAnnotation(FXMLFile.class);
			if (fxml != null) {
				if (fxml.name().contains(alias)) {
					return controller;
				}
			}
		}
		return null;
	}
	/**
	 * Se encarga de agregar la instancia cargada para ser usada
	 * @param instance {@link ABean} extends
	 */
	@SuppressWarnings({ "hiding" })
	public final <T extends ABean> void put(T instance) {
		if(FXMLFileController.instancias == null) {
			FXMLFileController.instancias = new HashMap<String,Object>();
		}
		FXMLFileController.instancias.put(instance.getClass().getSimpleName(), instance );
	}
	/**
	 * Se encarga de limiar la instancia actual cargada
	 */
	@SuppressWarnings("hiding")
	public final <T extends ABean> void destroy(Class<T> instancia) {
		if(FXMLFileController.instancias != null) {
			FXMLFileController.instancias.remove(instancia.getSimpleName());
		}
	}
	/**
	 * Retorna la instalncia fxmlfile suministradda
	 * @return
	 */
	@SuppressWarnings({ "unchecked", "hiding" })
	public final <T extends ABean> T getFXMLFile(Class<T> instancia) {
		return (T) FXMLFileController.instancias.get(instancia.getSimpleName());
	}
}