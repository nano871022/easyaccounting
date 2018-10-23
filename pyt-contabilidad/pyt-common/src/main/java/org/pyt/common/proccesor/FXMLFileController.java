package org.pyt.common.proccesor;

import org.apache.commons.lang3.StringUtils;
import org.pyt.common.annotations.FXMLFile;

/**
 * Se encarga de usar para generalizar el proceso de obtencion del controlador fxmlfile
 * @author Alejandro Parra
 * @since 22/10/2018
 * @param <T>
 */
public abstract class FXMLFileController <T extends Object>{
	protected Class<T> controller;
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
	 * @return {@link Class}
	 */
	public final Class<T> getController() {
		return controller;
	}
	/**
	 * Se encarga devalidar si el alias suministrado es igual al configurado en la anotacion
	 * @param alias {@link String}
	 * @return {@link Class}
	 * @throws {@link Exception}
	 */
	public final Class<T> valid(String alias)throws Exception {
		if(controller != null && StringUtils.isNotBlank(alias)) {
			FXMLFile fxml = controller.getDeclaredAnnotation(FXMLFile.class);
			if(fxml != null) {
				if(fxml.name().contains(alias)) {
					return controller;
				}
			}
		}
		return null;
	}
}
