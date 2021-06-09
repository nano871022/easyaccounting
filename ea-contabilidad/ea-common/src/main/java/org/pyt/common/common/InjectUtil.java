package org.pyt.common.common;

import java.util.ArrayList;
import java.util.List;

public class InjectUtil {
	public List<Class<?>> servicesImplements;
	private static InjectUtil instance;
	
	private InjectUtil() {
		this.servicesImplements = new ArrayList<>();
	}
	
	public static InjectUtil instance() {
		if(instance == null) {
			instance = new InjectUtil();
		}
		return instance;
	}
	
	public <T>void add(Class<T> clazz){
		this.servicesImplements.add(clazz);
	}
	
	public List<Class<?>> getList(){
		return this.servicesImplements;
	}
}
