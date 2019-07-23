package co.com.japl.ea.constants.utils;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.pyt.common.common.RefreshCodeValidation;
import org.pyt.common.constants.AppConstants;
import org.pyt.common.constants.EnviromentConstants;
import org.pyt.common.constants.RefreshCodeConstant;


public final class EnviromentProperties {

	private final static String CONST_BOOL_FALSE = "false";
	private static Map<String, Object> maps = new HashMap<String, Object>();

	public final static String getEnviromentProperty(String nameProperty) {
		return System.getenv(nameProperty);
	}
	
	private final static <T extends Object>Object searchEnviroment(String enviromentProperty,T emptyValue) {
		if(RefreshCodeValidation.getInstance().validate(RefreshCodeConstant.CONST_CLEAN_CACHE_ENV_PROP)) {
			maps.clear();
		}
		if (maps.containsKey(enviromentProperty)) {
			return (String) maps.get(enviromentProperty); 
		} else {	
			var value = getEnviromentProperty(enviromentProperty);
			if(StringUtils.isBlank(value)) {
				maps.put(enviromentProperty, emptyValue);
				return emptyValue;
			}
			maps.put(enviromentProperty, value);
			return value;
		}
	}

	public final static String getPath() {
		var value = searchEnviroment(EnviromentConstants.PROP_ENV_PATH, AppConstants.PROP_PATH);
		return (String) value;
	}

	public final static boolean getConsolePrint() {
		var value = searchEnviroment(EnviromentConstants.PROP_ENV_CONSOLE_PRINT, CONST_BOOL_FALSE);
		return (boolean)value;
	}
}
