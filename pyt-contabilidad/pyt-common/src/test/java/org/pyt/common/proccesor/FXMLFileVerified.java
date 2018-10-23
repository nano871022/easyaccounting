package org.pyt.common.proccesor;

import java.util.List;

import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;

import org.pyt.common.annotations.FXMLFile;

import co.com.arquitectura.proccessor.verifyAnotation.AbstractVerified;


public class FXMLFileVerified extends AbstractVerified<FXMLFile> {
	private Class<?> parent;
	private Element method;

	public FXMLFileVerified(Element method) throws IllegalArgumentException, Exception {
		 super((TypeElement)method,false);
		 this.method = method;
		 getAnnotation(analized());
	}

	@Override
	protected FXMLFile analized() throws Exception {
		FXMLFile service = method.getAnnotation(FXMLFile.class);
		if (service == null) {
			throw new Exception("La anotacion @FXMLFile service no existe en " + clase.getSimpleName() + ".");
		}
//		throw new Exception("Se encontro");
		return service;
	}

	@Override
	protected void getAnnotation(FXMLFile obj) throws Exception {
		id = obj.name();
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
