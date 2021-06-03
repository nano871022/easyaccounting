package org.pyt.common.annotations;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * Se encarga de ejecutar un metodo despues de construir el objeto
 * 
 * @author Alejandro Parra
 * @since 22-06-2018
 */
@Retention(RUNTIME)
@Target(METHOD)
public @interface PostConstructor {

}
