package org.pyt.common.annotation.generics;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * Anotacion usada para la configuracion de multiples anotaciones sobre campoas
 * 
 * @author Alejandro Parra
 * @since 16/04/2019
 */
@Retention(RUNTIME)
@Target(FIELD)
public @interface DefaultFieldToGenerics {
	/**
	 * Indica cuando se usan multiples anotaciones sobre un campo apra multiples
	 * usos sobre generics
	 * 
	 * @return {@link DefaultFieldToGeneric}
	 */
	public DefaultFieldToGeneric[] value();
}
