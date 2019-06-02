package com.japl.ea.query.privates.jpa;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Almacena el codigo de un grupo de parametro a un mapa de grupo para tratar de
 * realizar los valores que sean lo mas parametrizable posible
 * 
 * @author Alejandro Parra
 * @since 30-06-2018
 */
@Entity(name="TBL_PARAMETER_GROUP_INVENTORY")
@Table(name="TBL_PARAMETER_GROUP_INVENTORY")
public class ParametroGrupoInventarioJPA extends ParametroGrupoJPA {
}
