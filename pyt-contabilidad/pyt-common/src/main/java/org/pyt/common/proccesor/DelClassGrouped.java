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

public class DelClassGrouped extends AbstractGrouped<DelClassVerified> {
	public final static String PATH = "dels";
	public final static String PACKAGE = "com.pyt.service.dto."+PATH;
	public final static String LAST_NAME = "DelDTO";
	public DelClassGrouped(String canonicName) {
		super(canonicName, PATH);
	}

	@Override
	public void add(DelClassVerified verified) throws IdAlreadyUsedException {
		DelClassVerified existente = items.get(verified.getId());
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
		String name = PACKAGE+canonicName.substring(lastDot).replace("DTO", "")+LAST_NAME;
		createFile(name, filer, elementUtils, superClassName);
	}

	private final void createFile(String name, Filer filer, Elements elementUtils, TypeElement superClassName)
			throws Exception {
		JavaFileObject jfo = filer.createSourceFile(name);
		Writer writer = jfo.openWriter();
		JavaWriter jw = new JavaWriter(writer);
		// Write package
		loadHeader(jw);
		jw.close();
	}

	private final <T extends Object> void loadHeader(JavaWriter jw) throws Exception {
		jw.emitPackage(PACKAGE);
		jw.emitEmptyLine();
		jw.emitImports(
				"java.time.LocalDateTime"
				,"org.pyt.common.interfaces.IDelClass"
				,canonicName
				);
		String[] split = canonicName.split("\\.");
		String nombre = split[split.length-1];
		jw.beginType(nombre.replaceAll("DTO", "") + LAST_NAME, GenericConstants.CLASS, Modifier.PUBLIC,
				nombre ,"IDelClass");
		jw.emitEmptyLine();
		jw.emitField("LocalDateTime", "fechaElimina", Modifier.PRIVATE);
		jw.emitField("String", "usuarioElimina", Modifier.PRIVATE);
		jw.beginMethod("LocalDateTime", "getFechaElimina", Modifier.PUBLIC);
		jw.emitStatement("return fechaElimina");
		jw.endMethod();
		jw.beginMethod("String", "getUsuarioELimina", Modifier.PUBLIC);
		jw.emitStatement("return usuarioElimina");
		jw.endMethod();
		jw.beginMethod(GenericConstants.VOID, "setUsuarioElimina", Modifier.PUBLIC,"String","usuario");
		jw.emitStatement("usuarioElimina = usuario");
		jw.endMethod();
		jw.beginMethod(GenericConstants.VOID, "setFechaElimina", Modifier.PUBLIC,"LocalDateTime","fecha");
		jw.emitStatement("this.fechaElimina = fecha");
		jw.endMethod();
		jw.endType();
	}

}
