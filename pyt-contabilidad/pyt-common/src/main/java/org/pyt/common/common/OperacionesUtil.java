package org.pyt.common.common;

import java.math.BigDecimal;
import java.util.Arrays;

public final class OperacionesUtil {
	private OperacionesUtil() {}
	
	public static BigDecimal impuesto(BigDecimal valor,Double porcentage) {
		if(porcentage > 1.0D) {
			porcentage = porcentage / 100;
		}
		return valor.multiply(new BigDecimal(porcentage));
	}
	
	public static Double plus(Double[] values) {
		if(values != null && values.length > 0) {
			return Arrays.asList(values).stream().reduce(0.0, Double::sum);
		}
		return 0.0;
	}
}
