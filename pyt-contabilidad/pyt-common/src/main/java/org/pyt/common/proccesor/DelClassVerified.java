package org.pyt.common.proccesor;

import java.util.List;

import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;

import org.apache.commons.lang3.StringUtils;
import org.pyt.common.annotations.DelClass;

import co.com.arquitectura.proccessor.verifyAnotation.AbstractVerified;


public class DelClassVerified extends AbstractVerified<DelClass> {
	private Class<?> parent;
	private Element method;

	public DelClassVerified(Element method) throws IllegalArgumentException, Exception {
		 super((TypeElement)method,false);
		 this.method = method;
		 getAnnotation(analized());
	}

	@Override
	protected DelClass analized() throws Exception {
		DelClass service = method.getAnnotation(DelClass.class);
		if (service == null) {
			throw new Exception("La anotacion @DelClass service no existe en " + clase.getSimpleName() + ".");
		}
		return service;
	}

	@Override
	protected void getAnnotation(DelClass obj) throws Exception {
		if(StringUtils.isBlank(obj.nombre()))return;
		id = obj.nombre();
		canonicClass = clase.toString();
		simpleNameClass = clase.getSimpleName().toString();
	}
	public Element getMethod() {
		return method;
	}

	public Class<?> getParent() {
		return parent;
	}

	@SuppressWarnings("unchecked")
	public <T extends Object> Class<T>[] getInterface() throws Exception {
		List<Class<T>> lista = superClass(parent);
		return (Class<T>[]) lista.toArray();
	}
}
