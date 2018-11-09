package org.pyt.common.proccesor;

import java.util.List;

import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;

import org.apache.commons.lang3.StringUtils;
import org.pyt.common.annotations.UpdClass;

import co.com.arquitectura.proccessor.verifyAnotation.AbstractVerified;


public class UpdClassVerified extends AbstractVerified<UpdClass> {
	private Class<?> parent;
	private Element method;

	public UpdClassVerified(Element method) throws IllegalArgumentException, Exception {
		 super((TypeElement)method,false);
		 this.method = method;
		 getAnnotation(analized());
	}

	@Override
	protected UpdClass analized() throws Exception {
		UpdClass service = method.getAnnotation(UpdClass.class);
		if (service == null) {
			throw new Exception("La anotacion @UpdClass service no existe en " + clase.getSimpleName() + ".");
		}
		return service;
	}

	@Override
	protected void getAnnotation(UpdClass obj) throws Exception {
		if(StringUtils.isBlank(obj.nombre())) {
			return;
		}
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
