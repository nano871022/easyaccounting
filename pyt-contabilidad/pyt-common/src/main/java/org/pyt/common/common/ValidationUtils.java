package org.pyt.common.common;

public class ValidationUtils {
	private ValidationUtils() {}
	
	public static boolean greaterZero(Integer value) {
		return value != null && value > 0;
	}
}
