package org.pyt.common.proccesor;

import java.io.Writer;
import java.lang.reflect.Modifier;

import javax.annotation.processing.Filer;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;
import javax.tools.JavaFileObject;

import com.squareup.java.JavaWriter;

import co.com.arquitectura.constants.generics.GenericConstants;
import co.com.arquitectura.exceptions.proccess.IdAlreadyUsedException;
import co.com.arquitectura.proccessor.verifyAnotation.AbstractGrouped;

public class FXMLFileGrouped extends AbstractGrouped<FXMLFileVerified> {

	public FXMLFileGrouped(String canonicName) {
		super(canonicName, "controllers");
	}

	@Override
	public void add(FXMLFileVerified verified) throws IdAlreadyUsedException {
		FXMLFileVerified existente = items.get(verified.getId());
		if (existente != null)
			throw new IdAlreadyUsedException(existente.getSimpleNameClass(), existente.getId());
		items.put(verified.getId(), verified);
	}

	@Override
	public void generateSource(Elements elementUtils, Filer filer) throws Exception {
		TypeElement superClassName = elementUtils.getTypeElement(canonicName);
		String factoryClassName = superClassName.getSimpleName() + "";
		nameClass = factoryClassName;
		int lastDot = canonicName.lastIndexOf(".");
		String name = "org.pyt.app.beans.controllers"+canonicName.substring(lastDot)+"Controller";
		createFile(name, filer, elementUtils, superClassName);
	}

	private final void createFile(String name, Filer filer, Elements elementUtils, TypeElement superClassName)
			throws Exception {
		JavaFileObject jfo = filer.createSourceFile(name);
		Writer writer = jfo.openWriter();
		JavaWriter jw = new JavaWriter(writer);
		// Write package
		loadHeader(jw);
		for (FXMLFileVerified item : items.values()) {
			loadLine(item, jw);
		}
		jw.endMethod();
		jw.endType();
		jw.close();
	}

	private final <T extends Object> void loadHeader(JavaWriter jw) throws Exception {
		jw.emitPackage("org.pyt.app.beans.controllers");
		jw.emitEmptyLine();
		jw.emitImports(
				"org.pyt.common.proccesor.FXMLFileController"
				,canonicName
				);
		String[] split = canonicName.split("\\.");
		String nombre = split[split.length-1];
		jw.beginType(nombre + "Controller", GenericConstants.CLASS, Modifier.PUBLIC,
				"FXMLFileController<" + nombre + ">");
		jw.emitEmptyLine();
		jw.beginMethod(GenericConstants.VOID, "load", Modifier.PUBLIC);
	}

	private final void loadLine(FXMLFileVerified item, JavaWriter jw) throws Exception {
		String[] split = item.getClase().getQualifiedName().toString().split("\\.");
		jw.emitStatement("controller = %s.class", split[split.length-1]);
		jw.emitEmptyLine();
	}

}
