package com.japl.ea.query.privates.jpa;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Se almacenan todos los parametros que se van a utilizar en la aplicacion y se
 * separan por grupos.
 * 
 * @author alejandro parra 
 * @since 06/05/2018
 */
@Entity(name="TBL_INVENTORY_PARAMETER")
@Table(name="TBL_INVENTORY_PARAMETER")
public class ParametroInventarioJPA extends ParametroJPA{

}
