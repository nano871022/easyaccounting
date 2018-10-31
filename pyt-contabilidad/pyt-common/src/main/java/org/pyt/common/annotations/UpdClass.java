package org.pyt.common.annotations;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

@Retention(RUNTIME)
@Target(TYPE)
public @interface UpdClass {
	@SuppressWarnings("rawtypes")
	public Class clase() default Class.class;
	public String nombre() default "";
}
