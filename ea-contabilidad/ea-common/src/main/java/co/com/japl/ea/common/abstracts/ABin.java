package co.com.japl.ea.common.abstracts;

import org.pyt.common.constants.DataPropertiesConstants;

import co.com.japl.ea.common.properties.AppProperties;



/**
 * Clase abstracta que se encarga de tener funcionalidades para usar el entre
 * escritura y lectura de archivos binarios
 * 
 * @author Alejandro Parra
 * @since 21-06-2018
 */
public abstract class ABin {
	private String path_split = "/";

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

	private final String getPath() {
		return AppProperties.instance().get(DataPropertiesConstants.CONST_PATH_FILES);
	}

}
