package org.pyt.common.common;

import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;

public final class InstanceObject {

	@SuppressWarnings("unchecked")
	public static <T> T instance(Class<T> clazz) throws InstantiationException, IllegalAccessException,
			IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException {
		if(clazz == BigDecimal.class) {
			return (T) new BigDecimal(0);
		}
		return clazz.getDeclaredConstructor().newInstance();
	}

}
