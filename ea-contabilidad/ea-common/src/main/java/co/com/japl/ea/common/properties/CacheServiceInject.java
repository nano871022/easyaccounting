package co.com.japl.ea.common.properties;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.apache.poi.ss.formula.functions.T;

public class CacheServiceInject {
	private static CacheServiceInject instance ;
	private Map<Class<?>,Class<?>> clazz;
	
	private CacheServiceInject() {}
	
	public static CacheServiceInject instance() {
		if(instance == null){
			instance = new CacheServiceInject();
			instance.load();
		}
		return instance;
	}
	
	public <T> Optional<T> implement(Class<?> interfaces){
		try {
			if(this.clazz.containsKey(interfaces)) {
				var clazz = this.clazz.get(interfaces);
				var instance = clazz.getConstructor().newInstance();
				return Optional.of((T)instance);
			}
		}catch(Exception e) {
			return Optional.empty();
		}
		return Optional.empty();
	}
	
	private void load() {
		try {
			Class<?> clazz = Class.forName("co.com.japl.ea.service.create.ServiceList");
			var serviceList = clazz.getConstructors()[0].newInstance();
			var response = clazz.getDeclaredMethods()[0].invoke(serviceList);
			if(response instanceof Class[]) {
				Arrays.asList((Class[])response).forEach(value->{
					if(this.clazz == null) {
						this.clazz = new HashMap<>();
					}
					this.clazz.put(value.getInterfaces()[0],value);
				});
			}
		}catch(Exception e) {
			
		}
	}
	
}
