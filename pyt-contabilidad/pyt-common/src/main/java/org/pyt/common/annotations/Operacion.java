package org.pyt.common.annotations;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
/**
 * Se encarga de realizar la operacion sobre los campos indicados
 * @author Alejandro Parra
 * @since 16/07/2018
 */
@Retention(RUNTIME)
@Target(FIELD)
@Repeatable(value=Operaciones.class)
public @interface Operacion {
	/**
	 * Nombre de campo 1
	 * @return {@link String}
	 */
	public String valor1();
	/**
	 * Nombre del campo 2, puede no tener campo a usar
	 * @return {@link String}
	 */
	public String valor2() default "";
	/**
	 * Operacion a realizar ccon los dos campos
	 * @return {@link Operar}
	 */
	public Operar operacion();
}
