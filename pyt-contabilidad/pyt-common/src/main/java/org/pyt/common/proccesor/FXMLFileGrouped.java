package org.pyt.common.proccesor;

import java.io.Writer;
import java.lang.reflect.Modifier;

import javax.annotation.processing.Filer;
import javax.lang.model.element.PackageElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.ExecutableType;
import javax.lang.model.util.Elements;
import javax.tools.JavaFileObject;

import com.squareup.java.JavaWriter;

import co.com.arquitectura.constants.generics.GenericConstants;
import co.com.arquitectura.constants.proccessor.FileNameConstants;
import co.com.arquitectura.exceptions.proccess.IdAlreadyUsedException;
import co.com.arquitectura.proccessor.verifyAnotation.AbstractGrouped;

public class FXMLFileGrouped extends AbstractGrouped<FXMLFileVerified> {

	public FXMLFileGrouped(String canonicName) {
		super(canonicName, "FXMLFilePackage");
	}

	@Override
	public void add(FXMLFileVerified verified) throws IdAlreadyUsedException {
		FXMLFileVerified existente = items.get(verified.getId());
		if(existente != null)
			throw new IdAlreadyUsedException(existente.getSimpleNameClass(),existente.getId());
		items.put(verified.getId(), verified);
	}
	

	@Override
	public void generateSource(Elements elementUtils, Filer filer) throws Exception {
		TypeElement superClassName = elementUtils.getTypeElement(canonicName);
		String factoryClassName = superClassName.getSimpleName() +"" ;
		nameClass = factoryClassName;
		int lastDot = canonicName.lastIndexOf(".");
		String name = canonicName.substring(lastDot);
		name = name.replace(".", "");
		name = canonicName.replace(name, packagesSave+"."+FileNameConstants.SERVICE_NAME);
		createFile(name,filer,elementUtils,superClassName);
	}
	
	private final void createFile(String name,Filer filer,Elements elementUtils,TypeElement superClassName)throws Exception {
		JavaFileObject jfo = filer.createSourceFile(name);
		Writer writer = jfo.openWriter();
		JavaWriter jw = new JavaWriter(writer);
		// Write package
		PackageElement pkg = elementUtils.getPackageOf(superClassName);
		loadHeader(jw,pkg);
		for (FXMLFileVerified item : items.values()) {
			loadLine(item,jw);
		}
		jw.endMethod();
		jw.endType();
		jw.close();
	}
	
	private final void loadHeader(JavaWriter jw,PackageElement pkg)throws Exception{
		if (!pkg.isUnnamed()) {
			jw.emitPackage(pkg.getQualifiedName().toString()+"."+packagesSave);
			jw.emitEmptyLine();
		} else {
			jw.emitPackage("");
		}
		jw.emitImports(
				"org.pyt.common.proccesor.FXMLFilePOJO"
				,"org.pyt.common.annotations.FXMLFile"
				,"java.util.List"
				);
		jw.beginType(FileNameConstants.SERVICE_NAME, GenericConstants.CLASS, Modifier.PUBLIC );
		jw.emitEmptyLine();
		jw.beginMethod(GenericConstants.VOID, "load", Modifier.PUBLIC);
	}
	
	private final void loadLine(FXMLFileVerified item,JavaWriter jw)throws Exception{
		jw.emitStatement("lista.add("
				+ "new FXMLFilePOJO(\"%s\",\"%s\",%s.class))"
				, item.getMethod().getSimpleName().toString()
				, item.getId()
				, item.getClase().getQualifiedName().toString()
				);
		jw.emitEmptyLine();
	}

}
