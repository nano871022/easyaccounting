package co.com.japl.ea.common.properties;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.pyt.common.common.InjectUtil;

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
	
	@SuppressWarnings("unchecked")
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
			var response = getServicesImplements();
			if(response instanceof List<Class<?>>) {
				if(this.clazz == null) {
					this.clazz = new HashMap<>();
				}
				response.forEach(value->{
					this.clazz.put(value.getInterfaces()[0],value);
				});
			}
		}catch(Exception e) {
			
		}
	}
	
	@SuppressWarnings("unused")
	private List<Class<?>> getServicesImplements()throws Exception{
		Class<?> clazz = Class.forName("co.com.japl.ea.implement.inject.create.ServiceList");
		var list = InjectUtil.instance().getList();
		var clazzList = list.stream().map(value->{
			try {
				var method = value.getDeclaredMethod("list");
				var field = value.getDeclaredField("instance");
				field.setAccessible(true);
				var instance = field.get(null);
				return (Class<?>[])method.invoke(instance);
			}catch(Exception e) {
				System.err.println(e.getMessage());
			}
			return null;
		}).flatMap(value->Arrays.asList(value).stream())
				.collect(Collectors.toList());
		return clazzList;
	}
	
}
