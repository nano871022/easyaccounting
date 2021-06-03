package co.com.japl.ea.dto.pojo;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import co.com.japl.ea.common.abstracts.ADto;
import co.com.japl.ea.common.reflection.ReflectionUtils;
import co.com.japl.ea.dto.abstracts.Services;

/**
 * Se encargo de almacenar el grupo de procesos con parameteros
 * 
 * @author Alejandro Parra
 * @since 20/11/2018
 */
public class ProccessPOJO {
	private Object service;
	private String servicio;
	private Map<String, Object> parameters;
	private Method method;
	private List<String> errores;
	private Integer numLine;
	public ProccessPOJO() {
	}

	public ProccessPOJO copy() {
		ProccessPOJO pojo = new ProccessPOJO();
		pojo.setServicio(servicio);
		pojo.setService(service);
		for (String parameter : parameters.keySet()) {
			if (parameters.get(parameter) instanceof ADto) {
				try {
					pojo.add(ReflectionUtils.copy(parameters.get(parameter)));
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		pojo.setMethod(method);
		pojo.setNumLine(numLine);
		return pojo;
	}

	public void setNumLine(Integer num) {
		numLine = num;
	}

	public Integer getNumLine() {
		return numLine;
	}

	public final <T extends Exception> void addError(T error) {
		if (errores == null) {
			errores = new ArrayList<>();
		}
		if (error.getMessage() == null && error.getCause() == null) {
			Arrays.asList(error.getStackTrace()).stream()
					.filter(stackTrace -> stackTrace.getClass().getName().contains("pyt")
							|| stackTrace.getClass().getName().contains("ea")
							|| stackTrace.getClass().getName().contains("japl")
							|| stackTrace.getClass().getName().contains("co.com"))
					.forEach(stackTrace -> errores.add(stackTrace.getClass().getName() + "."
							+ stackTrace.getMethodName() + ":" + stackTrace.getLineNumber()));
		} else if(error.getMessage() == null && error.getCause() != null){
			errores.add(error.getCause().getMessage());
		}else {
			errores.add(error.getMessage());
		}
	}

	public void addError(String error) {
		if (errores == null) {
			errores = new ArrayList<>();
		}
		errores.add(error);
	}

	public List<String> getErrores() {
		return errores;
	}

	public final <T extends Object> void set(T parameter) {
		if (parameter != null) {
			parameters.put(parameter.getClass().getSimpleName(), parameter);
		}
	}

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

	public final String[] parameters() {
		return parameters.keySet().toArray(new String[parameters.keySet().size()]);
	}

	public final String getSimpleNameService() {
		return service.getClass().getSimpleName();
	}

	public final void setService(Object service) {
		if (service instanceof Services) {// si es un servicio configurado por la aplicacion se realiza una instancia
											// por medio de load anotado por postcontructor.
			((Services) service).load();
		}
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

	public final void setMethod(Method method) {
		this.method = method;
	}

	public final Method getMethod() {
		return this.method;
	}

	public final Map<String, Object> getParameters() {
		return parameters;
	}
}