package org.pyt.common.annotations;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * Se realiza el ingremenotoo de un valor segun los reistro que hay en la misma
 * tabla
 * 
 * @author Alejandro Parra 
 * @since 15/07/2018
 */
@Retention(RUNTIME)
@Target(FIELD)
public @interface Increment {

}
