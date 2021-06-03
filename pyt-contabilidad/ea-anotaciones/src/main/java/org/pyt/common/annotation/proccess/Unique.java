package org.pyt.common.annotation.proccess;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;
/**
 * Esta anotacion se usa con {@link Valid}, indica que la consulta obtenida con valid debe solo contener un registro.
 * @author Alejandro Parra
 * @since 21/11/2018
 */
@Retention(RUNTIME)
@Target(FIELD)
public @interface Unique {

}

