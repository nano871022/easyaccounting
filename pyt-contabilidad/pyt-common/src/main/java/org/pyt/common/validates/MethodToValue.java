package org.pyt.common.validates;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class MethodToValue {

	private Method method;
	private Class typeIn;
	private Class typeOut;
	private Boolean isStatic;

	public MethodToValue(Method method, Class typeIn, Class typeOut, Boolean isStatic) {
		this.method = method;
		this.typeIn = typeIn;
		this.typeOut = typeOut;
		this.isStatic = isStatic;
	}

	public final Boolean isClassInOut(Class classIn, Class classOut) {
		return typeIn == classIn && typeOut == classOut;
	}

	@SuppressWarnings("unchecked")
	public final <T, S> T getValueInvoke(S value)
			throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		if (isStatic) {
			return (T) method.invoke(null, value);
		} else {
			return (T) method.invoke(value);
		}
	}

}
