package org.pyt.common.annotation.generics;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * Se usa para indicar que un campo usara un valor
 * 
 * @author Alejandro Parra
 * @since 24/04/2019
 */
@Retention(RUNTIME)
@Target(FIELD)
@Repeatable(value = AssingValues.class)
public @interface AssingValue {
	public String nameField();

	public String value();
}
