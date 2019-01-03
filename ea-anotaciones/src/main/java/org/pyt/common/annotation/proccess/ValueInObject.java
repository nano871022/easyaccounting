package org.pyt.common.annotation.proccess;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * Esta anotacion se usa para indicar que en un campo de tipo objeto en
 * especifico {@link ADto} se va a poner el valor directamente en el cargue a un
 * campo dentro de este objeto
 * 
 * @author Alejandro Parra
 * @since 03/01/2019
 */
@Retention(RUNTIME)
@Target(FIELD)
public @interface ValueInObject {
	/**
	 * En este campo se pone el nombre del campo en el objeto de destino.
	 * 
	 * @return {@link String}
	 */
	public String field();
}
