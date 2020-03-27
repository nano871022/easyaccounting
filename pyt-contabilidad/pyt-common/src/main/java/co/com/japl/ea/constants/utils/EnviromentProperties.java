package co.com.japl.ea.constants.utils;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.apache.commons.lang3.StringUtils;
import org.pyt.common.common.CacheUtil;
import org.pyt.common.constants.EnviromentConstants;
import org.pyt.common.constants.RefreshCodeConstant;


public final class EnviromentProperties {

	private final static String CONST_BOOL_FALSE = "false";
	private final static String CONST_BOOL_TRUE = "true";
	private final static String CONST_PROP_FILE_SEPARATOR = "file.separator";
	private static Map<String, Object> maps = new HashMap<String, Object>();

	public final static String getEnviromentProperty(String nameProperty) {
		var property = Optional.ofNullable(System.getProperty(nameProperty));
		return  property.orElse(System.getenv(nameProperty));
	}
	
	private final static <T extends Object>Object searchEnviroment(String enviromentProperty,T emptyValue) {
		if (CacheUtil.INSTANCE().isRefresh(RefreshCodeConstant.CONST_CLEAN_CACHE_ENV_PROP)) {
			maps.clear();
			CacheUtil.INSTANCE().load(RefreshCodeConstant.CONST_CLEAN_CACHE_ENV_PROP);
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
		var pathDefault = "."+System.getProperty(CONST_PROP_FILE_SEPARATOR);
		var value = searchEnviroment(EnviromentConstants.PROP_ENV_PATH, pathDefault);
		return (String) value;
	}

	public final static boolean getConsolePrint() {
		var value = searchEnviroment(EnviromentConstants.PROP_ENV_CONSOLE_PRINT, CONST_BOOL_FALSE);
		if(value instanceof String) {
			var validate = ((String) value).equalsIgnoreCase(CONST_BOOL_TRUE);
			maps.put(EnviromentConstants.PROP_ENV_CONSOLE_PRINT, validate);
			return validate;
		}
		return (boolean) value;
	}
}
