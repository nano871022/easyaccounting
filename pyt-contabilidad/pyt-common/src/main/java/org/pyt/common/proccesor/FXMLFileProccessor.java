package org.pyt.common.proccesor;

import java.io.IOException;

import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.Modifier;

import org.pyt.common.annotations.FXMLFile;

import co.com.arquitectura.proccessor.abstracts.AbstractProccessorGeneric;

@SupportedSourceVersion(SourceVersion.RELEASE_8)
@SupportedAnnotationTypes("org.pyt.common.annotations.FXMLFile")
public class FXMLFileProccessor extends AbstractProccessorGeneric<FXMLFileVerified, FXMLFileGrouped, FXMLFile> {

	public FXMLFileProccessor() {
		super(FXMLFile.class);
	}

	@Override
	protected void proccess(RoundEnvironment roundEnv) throws Exception {
		try {info(null,"procesando");
			if (groupClass != null && groupClass.values() != null) {
				for (FXMLFileGrouped services : groupClass.values()) {
					services.generateSource(processingEnv.getElementUtils(), processingEnv.getFiler());
//					error(null,"grupos");
				}
			}
		} catch (IOException e) {
			error(null, e.getMessage());
		}
		info(null,"fin proccess");
	}

	@Override
	protected Boolean verifiedTypeElement(RoundEnvironment roundEnv) {
		FXMLFileVerified veryfied = null;
		for (Element clase : roundEnv.getElementsAnnotatedWith(annotation)) {
			if (clase.getKind() == ElementKind.CLASS ) {
				info(null,"clase "+clase.getSimpleName());
				veryfied = proccessAnnotation(clase);
			} else {
				error(clase, " Solo se deben anotar clases con @" + annotation.getSimpleName());
			}
		}
		if(veryfied == null) return false;
		return true;
	}

	@Override
	protected FXMLFileVerified proccessAnnotation(Element method) {
		info(method, "proccessAnotation "+method.getSimpleName()+" - "+method.getEnclosingElement().toString()+" - "+method.getKind().toString()+" "+method.asType());
		FXMLFileVerified services = null;
		try {
//			error(null,"error "+method.getEnclosingElement().toString());
			if(method.getEnclosingElement().toString().contains("dinamico")) {
				return null;
			}
			services = new FXMLFileVerified(method);
//			error(null,"fxmlfile");
			info(method, "clases obtenidas");
				if (!isValidClass(services)) {
					return null;
				}
				info(method, "obteniendo grupo fxmlfile");
				FXMLFileGrouped group = groupClass.get(services.getCanonicClass());
			if (group == null) {
				info(method, "Grupo encontrado");
				String qualifiedGroupName = services.getCanonicClass();
				group = new FXMLFileGrouped(qualifiedGroupName);
//				error(null,"grupo");
				groupClass.put(qualifiedGroupName, group);
//				error(null,"out");
			}
			group.add(services);
			return services;
		} catch (IllegalArgumentException e) {
			error(method, e.getMessage());
		} catch (Exception e) {
			error(method, "Problema:: " + e.getMessage());
		}
		return null;
	}

	@Override
	protected boolean isValidClass(FXMLFileVerified annotationClass) {
		Element clase = annotationClass.getClase();
		if (!clase.getModifiers().contains(Modifier.PUBLIC)) {
			error(clase, "La clase " + annotationClass.getClase().getSimpleName().toString() + " no es publico");
			return false;
		}
		if (clase.getModifiers().contains(Modifier.ABSTRACT)) {
			error(clase,
					"La clase " + annotationClass.getClase().getSimpleName().toString() + " no debe ser abstracto");
			return false;
		}
		if(clase.getModifiers().contains(Modifier.FINAL)) {
			error(clase,
					"La clase " + annotationClass.getClase().getSimpleName().toString() + " no debe ser final");
			return false;
		}
		return true;
	}

}
