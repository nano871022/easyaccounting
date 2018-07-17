package org.pyt.common.annotations;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;
/**
 * Esta anotacion indica que el campo no puede ser modificado con el formulario dinamico
 * @author Alejandro Parra
 * @since 16/07/2018
 */
@Retention(RUNTIME)
@Target(FIELD)
public @interface NoEdit {

}
