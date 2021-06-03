package co.com.japl.ea.dto.interfaces;

import co.com.japl.ea.common.abstracts.ADto;
import co.com.japl.ea.dto.system.UsuarioDTO;
import co.com.japl.ea.exceptions.CargueException;
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
	public <T extends ADto> FilePOJO cargue(String nameConfig,FilePOJO file,UsuarioDTO user)throws CargueException;
}
