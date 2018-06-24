package org.pyt.common.binario;

import org.pyt.common.common.ADto;

/**
 * Clase abstracta que se encarga de tener funcionalidades para usar el entre
 * escritura y lectura de archivos binarios
 * 
 * @author Alejandro Parra
 * @since 21-06-2018
 */
public abstract class ABin {
	/**
	 * Se encarga de obtener el nombe del archivo
	 * 
	 * @param dto
	 * @return
	 */
	protected final <T extends ADto> String getNameFile(Class<T> dto) {
		if (dto != null) {
			return String.format("%s.dat", dto.getSimpleName());
		}
		return null;
	}

}
