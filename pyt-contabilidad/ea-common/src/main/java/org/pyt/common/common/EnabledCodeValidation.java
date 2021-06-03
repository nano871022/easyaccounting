package org.pyt.common.common;

import java.util.HashMap;
import java.util.Map;

/**
 * Esta clase contrendra el coigo que sera inabilitado para segmentar
 * desarrollos, por si en produccion o en algun momento el codigo agregado sale
 * mal este se podra desactivar.
 * 
 * @author Alejandro Parra
 * @since 05/07/2019
 */
public final class EnabledCodeValidation {
	private static EnabledCodeValidation instance;
	private Map<String, Boolean> mapEnabledCode;

	private EnabledCodeValidation() {
		mapEnabledCode = new HashMap<String, Boolean>();
	}
	
	public static EnabledCodeValidation getInstance() {
		if(instance == null) {
			instance = new EnabledCodeValidation();
		}
		return instance;
	}

	public final boolean validation(String nameEnabledCode) {
		if (!mapEnabledCode.containsKey(nameEnabledCode)) {
			disabledCode(nameEnabledCode);
			return false;
		}
		return mapEnabledCode.get(nameEnabledCode);
	}

	public final void enableCode(String nameEnabledCode) {
		mapEnabledCode.put(nameEnabledCode, true);
	}

	public final void disabledCode(String nameEnabledCode) {
		mapEnabledCode.put(nameEnabledCode, false);
	}

	public final void removeCode(String nameEnabledCode) {
		if (mapEnabledCode.containsKey(nameEnabledCode)) {
			mapEnabledCode.remove(nameEnabledCode);
		}
	}
}
