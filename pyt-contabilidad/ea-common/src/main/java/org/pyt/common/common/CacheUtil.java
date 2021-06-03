package org.pyt.common.common;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.TreeMap;

/**
 * Usar para instancia {@link CachheUtil#INSTANCE}
 * @author Alejandro Parra
 * @since 02/02/2020
 */
public class CacheUtil {
	
	private Map<String,Boolean> cached;
	private static CacheUtil cache;
	private CacheUtil() {
		cached = new TreeMap<String, Boolean>();
	}
	
	public final static CacheUtil INSTANCE() {
		if(cache == null || cache.isRefresh("CacheUtil")) {
			cache = new CacheUtil();
			cache.load("CacheUtil");
		}
		return cache;
	}
	
	public final Boolean isRefresh(String name) {
		Boolean cached = false;
		if(this.cached.containsKey(name)) {
			 cached = Optional.ofNullable(this.cached.get(name)).orElse(false);
			 
		}
		return !cached;
	}
	
	public final void load(String name) {
		this.cached.put(name, true);
	}
	
	public final void unload(String name) {
		this.cached.put(name, false);
	}
	
	public final Map<String,Boolean> getAll(){
		var map = new HashMap<String,Boolean>();
		map.putAll(cached);
		return map;
	}
}
