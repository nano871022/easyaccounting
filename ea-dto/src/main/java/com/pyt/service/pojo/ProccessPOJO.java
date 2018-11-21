package com.pyt.service.pojo;

import java.util.HashMap;
import java.util.Map;
/**
 * Se encargo de almacenar el grupo de procesos con parameteros
 * @author Alejandro Parra
 * @since 20/11/2018
 */
public class ProccessPOJO {
	private Object service;
	private String servicio;
	private Map<String, Object> parameters;

	public final <T extends Object> void add(T parameter) {
		if (parameters == null) {
			parameters = new HashMap<String, Object>();
		}
		parameters.put(parameter.getClass().getSimpleName(), parameter);
	}

	@SuppressWarnings("unchecked")
	public final <T extends Object> T get(String parameter) {
		if (parameters == null)
			return null;
		return (T) parameters.get(parameter);
	}
	public final String getSimpleNameService() {
		return service.getClass().getSimpleName();
	}
	public final void setService(Object service) {
		this.service = service;
	}

	public final Object getService() {
		return this.service;
	}
	public final void setServicio(String servicio) {
		this.servicio = servicio;
	}
	public final String getServicio() {
		return this.servicio;
	}
}
