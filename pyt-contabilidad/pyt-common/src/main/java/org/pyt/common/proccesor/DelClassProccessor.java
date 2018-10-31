package org.pyt.common.proccesor;

import java.io.IOException;
import java.security.Provider.Service;

import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.Modifier;

import org.apache.commons.lang3.StringUtils;
import org.pyt.common.annotations.DelClass;

import co.com.arquitectura.proccessor.abstracts.AbstractProccessorGeneric;

@SupportedSourceVersion(SourceVersion.RELEASE_8)
@SupportedAnnotationTypes("org.pyt.common.annotations.DelClass")
public class DelClassProccessor extends AbstractProccessorGeneric<DelClassVerified, DelClassGrouped, DelClass> {

	public DelClassProccessor() {
		super(DelClass.class);
	}

	@Override
	protected void proccess(RoundEnvironment roundEnv) throws Exception {
		try {
			// info(null,"procesando");
			if (groupClass != null && groupClass.values() != null) {
				// error(null,"Errores proceso ");
				for (DelClassGrouped services : groupClass.values()) {
					if (services != null && StringUtils.isNotBlank(services.getCanonicName())) {
						services.generateSource(processingEnv.getElementUtils(), processingEnv.getFiler());
					}
				}
			}
		} catch (IOException e) {
			error(null, e.getMessage());
		} catch (Exception e) {
			info(null, e.getMessage());
		}
		// info(null,"fin proccess");
	}

	@Override
	protected Boolean verifiedTypeElement(RoundEnvironment roundEnv) {
		DelClassVerified veryfied = null;
		for (Element clase : roundEnv.getElementsAnnotatedWith(annotation)) {
			if (clase.getKind() == ElementKind.CLASS) {
				info(null, "clase " + clase.getSimpleName());
				veryfied = proccessAnnotation(clase);
			} else {
				error(clase, " Solo se deben anotar clases con @" + annotation.getSimpleName());
			}
		}
		if (veryfied == null)
			return false;
		return true;
	}

	@Override
	protected DelClassVerified proccessAnnotation(Element clase) {
		info(clase, "proccessAnotation " + clase.getSimpleName() + " - " + clase.getEnclosingElement().toString()
				+ " - " + clase.getKind().toString() + " " + clase.asType());
		DelClassVerified services = null;
		try {
			services = new DelClassVerified(clase);
			info(clase, "clases obtenidas");
			if (!isValidClass(services)) {
				return null;
			}
			info(clase, "obteniendo grupo DelClass");
			DelClassGrouped group = groupClass.get(services.getCanonicClass());
			if (group == null) {
				info(clase, "Grupo encontrado");
				String qualifiedGroupName = services.getCanonicClass();
				group = new DelClassGrouped(qualifiedGroupName);
				groupClass.put(qualifiedGroupName, group);
			}
			group.add(services);
			return services;
		} catch (IllegalArgumentException e) {
			error(clase, "Error::" + e.getMessage());
		} catch (Exception e) {
			error(clase, "Problema:: " + e.getMessage());
		}
		return null;
	}

	@Override
	protected boolean isValidClass(DelClassVerified annotationClass) {
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
		if (clase.getModifiers().contains(Modifier.FINAL)) {
			error(clase, "La clase " + annotationClass.getClase().getSimpleName().toString() + " no debe ser final");
			return false;
		}
		return true;
	}

}
