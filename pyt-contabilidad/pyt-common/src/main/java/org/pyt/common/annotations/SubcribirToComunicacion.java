package org.pyt.common.annotations;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * Se encarga de realizar por medio de anotacion la subscripcion a comunicacion
 * con el comando indicado
 * 
 * @author Alejandro Parra
 * @since 2018-05-25
 */
@Retention(RUNTIME)
@Target(FIELD)
@Repeatable(SubcribcionesToComunicacion.class)
public @interface SubcribirToComunicacion {
	public String comando();
}
