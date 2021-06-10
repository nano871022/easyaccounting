package co.com.japl.ea.common.properties;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Properties;

public final class CachePropertiesPOM {
	private Map<String,String> properties;
	private List<String> loaders;
	private static CachePropertiesPOM cache;
	private CachePropertiesPOM() {
		properties = new HashMap<>();
		loaders = new ArrayList<>();
	}
	
	public static CachePropertiesPOM instance() {
		if(cache == null) {
			cache = new CachePropertiesPOM();
		}
		return cache;
	}

	public CachePropertiesPOM load(Class<?> clazzIntoModule,String fileName) {
		existFileProperties(clazzIntoModule, fileName)
		.filter(value->!value)
		.ifPresent(value->loadProperties(clazzIntoModule, fileName));
		return cache;
	}
	
	private Optional<Boolean> existFileProperties(Class<?> clazzIntoModule,String fileName){
		return Optional.ofNullable(loaders.stream()
					   .anyMatch(value->value.contentEquals(clazzIntoModule.getSimpleName()+":"+fileName)));
	}
	
	private void loadProperties(Class<?> clazzIntoModule,String fileName) {
		var inputStream = clazzIntoModule.getResourceAsStream(fileName);
		var properties = new Properties();
		try {
			properties.load(inputStream);
		}catch(Exception e) {
			System.err.println(e);
		}
		if(!properties.isEmpty()) {
			properties.keySet().forEach(value->this.properties.put((String)value, (String)properties.get(value)));
			loaders.add(clazzIntoModule.getSimpleName()+":"+fileName);
		}
	}
	
	public Optional<String> get(String key){
		if(properties.containsKey(key)) {
			return Optional.ofNullable(properties.get(key));
		}
		return Optional.empty();
	}
	
}
