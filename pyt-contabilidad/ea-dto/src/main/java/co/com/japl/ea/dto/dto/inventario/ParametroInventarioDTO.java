package co.com.japl.ea.dto.dto.inventario;

import co.com.arquitectura.annotation.proccessor.DelClass;
import co.com.arquitectura.annotation.proccessor.UpdClass;
import co.com.japl.ea.dto.dto.ParametroDTO;

/**
 * Se almacenan todos los parametros que se van a utilizar en la aplicacion y se
 * separan por grupos.
 * 
 * @author alejandro parra 
 * @since 06/05/2018
 */
@DelClass(nombre="co.com.japl.ea.dto.dto.dels.ParametroInventarioDelDTO")
@UpdClass(nombre="co.com.japl.ea.dto.dto.upds.ParametroInventarioUpdDTO")
public class ParametroInventarioDTO extends ParametroDTO{
	private static final long serialVersionUID = -5396836082089633791L;

}
