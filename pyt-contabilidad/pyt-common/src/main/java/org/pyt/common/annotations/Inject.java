package org.pyt.common.annotations;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * Se encarga de realizar la injeccion de valores sobre el el objeto anotado,
 * por logica se obtiene los servicios simulados
 * 
 * @author Alejandro Parra
 * @since 2018-05-19
 * @version 1.1
 */
@Retention(RUNTIME)
@Target(FIELD)
public @interface Inject {
	/**
	 * import del paquete que contiene el objeto a injectar
	 * 
	 * @return {@link String} por defecto esta vacio ya que si no se especifica este
	 *         se forzara a instanciar el objeto que se indico en la anotacion.
	 */
	public String resource() default "";
	
}
