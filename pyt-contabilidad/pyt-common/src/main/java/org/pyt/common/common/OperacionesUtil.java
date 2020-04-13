package org.pyt.common.common;

import java.math.BigDecimal;

public final class OperacionesUtil {
	private OperacionesUtil() {}
	
	public static BigDecimal impuesto(BigDecimal valor,Double porcentage) {
		if(porcentage > 1.0D) {
			porcentage = porcentage / 100;
		}
		return valor.multiply(new BigDecimal(porcentage));
	}
}
