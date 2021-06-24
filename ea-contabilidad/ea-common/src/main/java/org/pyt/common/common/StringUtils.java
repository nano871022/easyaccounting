package org.pyt.common.common;

import static org.apache.commons.lang3.StringUtils.isNotBlank;

public final class StringUtils {

	public static boolean contentEquals(String valor1, String valor2) {
		return isNotBlank(valor1) && isNotBlank(valor2) && valor1.contentEquals(valor2);
	}
	
}
