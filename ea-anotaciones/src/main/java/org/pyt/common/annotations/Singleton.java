package org.pyt.common.annotations;

import static java.lang.annotation.ElementType.TYPE;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <p>
 * Cuando un objeto es anotado, indica que cuando se solicite este objeto se
 * identificara y se retornara una unica instancia del objeto.
 * </p>
 * <p>
 * Esta anotacion se tiene en cuenta cuando se usa {@link Inject} .
 * </p>
 * <p>
 * Si se desea realizar la implementacion singleton por tener que inicializar el
 * objeto de forma especial se crea metodo llamado singleton en donde se realiza
 * la instanciacion especial.
 * </p>
 * 
 * @author Alejandro Parra
 * @since 2018-05-25
 *
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(TYPE)
public @interface Singleton {
}
