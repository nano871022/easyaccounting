package org.pyt.common.properties;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.pyt.common.common.CacheUtil;
import org.pyt.common.constants.RefreshCodeConstant;


public final class CacheInjects {
	private static CacheInjects cacheInjects;
	private Map<Class<?>, Class<?>> mapInjects;
	private Map<Class<?>, List<Method>> mapPostConstructor;

	private CacheInjects() {
		mapInjects = new HashMap<Class<?>, Class<?>>();
		mapPostConstructor = new HashMap<Class<?>, List<Method>>();
	}

	public static synchronized CacheInjects instance() {
		if (cacheInjects == null) {
			cacheInjects = new CacheInjects();
		}
		return cacheInjects;
	}

	@SuppressWarnings("unchecked")
	public synchronized final <T> T getInjectCache(Field field) throws InstantiationException, IllegalAccessException,
			IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException {
		cleanCache();
		if (mapInjects.containsKey(field.getType())) {
			var type = mapInjects.get(field.getType());
			var constructors = type.getDeclaredConstructors();
			if (constructors != null && constructors.length > 0) {
				var constructor = type.getDeclaredConstructor();
				var modifiers = constructor.getModifiers();
				if (!Modifier.isPrivate(modifiers)) {
					return (T) type.getConstructor().newInstance();
				}
			}
		}
		return null;
	}

	public synchronized final <T> void addInjectToCache(T obj, Field field) {
		if (!mapInjects.containsKey(field.getType())) {
			mapInjects.put(field.getType(), obj.getClass());
		}
	}

	private synchronized final void cleanCache() {
		if (CacheUtil.INSTANCE().isRefresh(RefreshCodeConstant.CONST_CLEAN_CACHE_INJECT_AND_POSTCONSTRUCTOR)) {
			mapPostConstructor.clear();
			mapInjects.clear();
			CacheUtil.INSTANCE().load(RefreshCodeConstant.CONST_CLEAN_CACHE_INJECT_AND_POSTCONSTRUCTOR);
		}
	}

	public synchronized final <T> Boolean invokeConstructorAnnotatedCache(T instance)
			throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		var result = false;
		cleanCache();
		if (mapPostConstructor.containsKey(instance.getClass())) {
			var methods = mapPostConstructor.get(instance.getClass());
			if (methods != null) {
				for (Method method : methods) {
					method.invoke(instance);
					result = true;
				}
			}
		}
		return result;
	}

	public synchronized final <T> void addConstructorAnnotatedToCache(T instance, Method method) {
		var list = mapPostConstructor.get(instance.getClass());
		if (list == null) {
			list = new ArrayList<Method>();
			mapPostConstructor.put(instance.getClass(), list);
		}
		if (!list.contains(method)) {
			list.add(method);
		}
	}

}
