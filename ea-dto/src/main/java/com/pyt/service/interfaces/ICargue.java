package com.pyt.service.interfaces;

import org.pyt.common.exceptions.CargueException;

import co.com.japl.ea.loader.pojo.FilePOJO;

/**
 * Se realiza de generar interfaz para realizar el cargue de la informacion
 * @author Alejandro Parra
 * @since 18/11/2018
 */
public interface ICargue {

	/**
	 * Se encarga de realizar el cargue de la informacion, recibe el nombr de la configuracion y el archivo cargado en un objeto especial
	 * @param nameConfig {@link String}
	 * @param file {@link FilePOJO}
	 * @return {@link FilePOJO}
	 * @throws {@link Exception}
	 */
	public FilePOJO cargue(String nameConfig,FilePOJO file)throws CargueException;
}
