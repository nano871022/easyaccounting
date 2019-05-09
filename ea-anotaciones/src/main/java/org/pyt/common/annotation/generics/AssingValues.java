package org.pyt.common.annotation.generics;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;
/**
 * Se eusa para construir anotaciones multiples sobre un solo campo
 * @author Alejandro Parra
 * @since 24/04/2019
 */
@Retention(RUNTIME)
@Target(FIELD)
public @interface AssingValues {
	public AssingValue[] value();
}
