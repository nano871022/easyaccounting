package org.pyt.common.annotation.proccess;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;
/**
 * Se encarga de validar en el campo anotado con el dto configurado y poner el valor resultado en el campo
 * @author Alejandro Parra
 * @since 21/11/2018 
 */
@Retention(RUNTIME)
@Target(FIELD)
public @interface Valid {
	/**
	 * Cuando se especifica este parametro indica que se va a realizar la busqueda sobre otro dto diferente al del anotado,
	 * por defecto el objeto anotado es Object.class si es este el sistema toma el objeto donde se encuentra la anotacion
	 * @return {@link Class}
	 */
	public Class<?> dto() default Object.class;
	/**
	 * En este parametro se usa para obtener el valor del campo indicado dentro de el dto configurado y ponerlo sobre el 
	 * campo anotado, si no se encuentra anotado usa el mismo campo el cual tiene anotado.
	 * @return {@link String}
	 */
	public String fieldOut() default "";
	/**
	 * En este parametro se usa para indicar en cual campo del dto configurado se pondra el valor que se encuentra en el
	 * campo que contiene la anotacion, si no usa este campo usa el mismo en donde se encuentra anotado.
	 * @return {@link String}
	 */
	public String fieldIn() default "";
	
}
