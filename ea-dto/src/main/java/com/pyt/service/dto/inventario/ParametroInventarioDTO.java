package com.pyt.service.dto.inventario;

import org.pyt.common.abstracts.ADto;

import com.pyt.service.dto.ParametroDTO;
import com.pyt.service.dto.dels.ParametroDelDTO;
import com.pyt.service.dto.upds.ParametroUpdDTO;

import co.com.arquitectura.annotation.proccessor.DelClass;
import co.com.arquitectura.annotation.proccessor.UpdClass;

/**
 * Se almacenan todos los parametros que se van a utilizar en la aplicacion y se
 * separan por grupos.
 * 
 * @author alejandro parra 
 * @since 06/05/2018
 */
@DelClass(nombre="com.pyt.service.dto.dels.ParametroInventarioDelDTO")
@UpdClass(nombre="com.pyt.service.dto.upds.ParametroInventarioUpdDTO")
public class ParametroInventarioDTO extends ParametroDTO{
	private static final long serialVersionUID = -5396836082089633791L;

}
