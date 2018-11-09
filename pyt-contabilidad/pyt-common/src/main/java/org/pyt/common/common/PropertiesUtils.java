package org.pyt.common.common;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Properties;

/**
 * Se encarga de cargar las propiedasd de un archivo properties y se pasan a la
 * aplicacion
 * 
 * @author ALejandro Parra
 * @since 2018-10-30
 */
public final class PropertiesUtils {
	private String pathProperties;
	private String nameProperties;
	private String folderSystem;
	private Properties properties;
	private final static String FILE_SEPARATOR = "file.separator";

	private PropertiesUtils() {
		folderSystem = System.getProperty(FILE_SEPARATOR);
		pathProperties = "./properties";
		nameProperties = "properties.properties";
	}
	/**
	 * Constructor encargado de cargar una instancia
	 * @return {@link PropertiesUtils}
	 */
	public final static PropertiesUtils getInstance() {
		return new PropertiesUtils();
	}
	/**
	 * Se encarga de carar el archivo leido de properties sobre el archivo de properties.
	 * @param fileInput {@link InputStream}
	 * @throws {@link Exception}
	 */
	private void load(InputStream fileInput) throws Exception{
		if(fileInput == null)throw new Exception("No se obtuvo la lectura del archivo");
		properties = new Properties();
		properties.load(fileInput);
	}
	
	/**
	 * Se encarga de cargar la configuracion de las propiedasd
	 * @return InputStram
	 * @throws {@link Exception}
	 */
	private final InputStream loadProperties() throws Exception {
		String namePath = pathProperties + folderSystem + nameProperties;
		File file = new File(namePath);
		if (file.exists()) {
			return new FileInputStream(file);
		} else {
			throw new Exception("No se encontro el archivo de properties " + namePath + " en " + pathProperties);
		}
	}
	/**
	 * Se encarga de cargar lass propiedades configuras
	 * @return
	 * @throws Exception
	 */
	public final PropertiesUtils load()throws Exception {
		InputStream reading = loadProperties();
		load(reading);
		return this;
	}
	
	public final PropertiesUtils setPathProperties(String pathProperties) {
		this.pathProperties = pathProperties;
		return this;
	}
	
	public final PropertiesUtils setNameProperties(String nameProperties) {
		this.nameProperties = nameProperties;
		return this;
	}
	
	public final Properties getProperties()throws Exception{
		return properties;
	}

}
