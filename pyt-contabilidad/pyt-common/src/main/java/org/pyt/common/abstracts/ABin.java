package org.pyt.common.abstracts;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

import org.apache.commons.lang3.StringUtils;
import org.pyt.common.common.Log;
import org.pyt.common.constants.DataPropertiesConstants;
import org.pyt.common.constants.PropertiesConstants;



/**
 * Clase abstracta que se encarga de tener funcionalidades para usar el entre
 * escritura y lectura de archivos binarios
 * 
 * @author Alejandro Parra
 * @since 21-06-2018
 */
public abstract class ABin {
	private String path_split = "/";
	private final static String default_file_data = "./systemFile";
	private Log logger = Log.Log(this.getClass());

	public ABin() {
		path_split = System.getProperty("file.separator");
	}
	/**
	 * Se encarga de obtener el nombe del archivo
	 * 
	 * @param dto
	 * @return
	 */
	protected final <T extends ADto> String getNameFile(Class<T> dto) {
		
		if (dto != null) {
			String name = dto.getSimpleName();
			name = name.replace("DTO", "");
			return String.format("%s%s%s.dat", getPath(),path_split, name);
		}
		return null;
	}

	/**
	 * Se encarga de obtener la ruta de almacenamiento de los archivos de la base de
	 * datos
	 * 
	 * @return {@link String} nombre directorio
	 */
	protected final String getPath() {
		String path = "";
		path = pathData();
		return path;
	}
	
	private final String pathData() {
		File properties = new File(PropertiesConstants.PROP_DATA);
		if(properties.exists()) {
			try {
				Properties prop = new Properties();
				FileInputStream fis = new FileInputStream(properties);
				if(fis != null) {
					prop.load(fis);
					String path = prop.getProperty(DataPropertiesConstants.CONST_PATH_FILES);
					if(StringUtils.isNotBlank(path)) {
						return path;
					}
				}
			} catch (FileNotFoundException e) {
				logger.logger(e);
			} catch (IOException e) {
				logger.logger(e);
			}
		}else {
			logger.error("No existe archivo de propiedades para datos.");
		}
		return default_file_data;
	}

}
