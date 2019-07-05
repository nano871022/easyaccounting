package org.pyt.common.common;

import java.util.HashMap;
import java.util.Map;

/**
 * Esta clase es usada para indicar cuando un codigo se debe refrescar
 * las constantes que referencian este codigo se encuentra en el 
 * paquete de cosntantes con nombre {@link RefreshCodeConstants}
 * @author Alejandro Parra
 * @since 05/07/2019
 */
public final class RefreshCodeValidation {
	private static RefreshCodeValidation instance;
	private Map<String,Boolean> mapRefreshCode;
	private RefreshCodeValidation() {
		mapRefreshCode = new HashMap<String,Boolean>();
	}
	
	public final static RefreshCodeValidation getInstance() {
		if(instance == null) {
			instance = new RefreshCodeValidation();
		}
		return instance;
	}
	/**
	 * Se verifica si se encuetra una constante y la retorna y la inabilita
	 * @param constantRefreshCode {@link String}
	 * @return {@link Boolean}
	 */
	@SuppressWarnings("unlikely-arg-type")
	public boolean validate(String constantRefreshCode) {
		var contains = mapRefreshCode.containsValue(constantRefreshCode);
		if(contains) {
			mapRefreshCode.remove(constantRefreshCode);
			return true;
		}
		return false;
	}
	/**
	 * se usa para indicar que un codigo debe ser refrescado
	 * @param constantRefreshCode {@link String}
	 */
	public void activeRefreshCode(String constantRefreshCode) {
		mapRefreshCode.put(constantRefreshCode,true);
	}
}
