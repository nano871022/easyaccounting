package com.pyt.service.proccess;

import java.lang.reflect.Field;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.pyt.common.annotation.proccess.Unique;
import org.pyt.common.annotation.proccess.Valid;
import org.pyt.common.annotations.Inject;
import org.pyt.common.common.ADto;
import org.pyt.common.common.Log;
import org.pyt.common.reflection.Reflection;

import com.pyt.query.interfaces.IQuerySvc;

/**
 * Se encarga de realizar el analisis del dto suministrado
 * 
 * @author Alejandro Parra
 * @since 21/11/2018
 */
public class AnalizedAnnotationProcces extends Reflection {
	@Inject(resource = "com.pyt.query.implement.QuerySvc")
	private IQuerySvc querySvc;
	private Log log = Log.Log(AnalizedAnnotationProcces.class);

	public AnalizedAnnotationProcces() {
		try {
			inject();
		} catch (Exception e) {
			log.logger(e);
		}
	}

	@SuppressWarnings({ "unchecked" })
	public final <T extends ADto, I extends ADto> T valid(T dto) throws Exception {
		I instance = null;
		String fieldIn = "";
		String fieldOut = "";
		String fieldName = "";
		Class<ADto> clazz = (Class<ADto>) dto.getClass();
		Field[] fields = clazz.getDeclaredFields();
		for (Field field : fields) {
			if (field.isAnnotationPresent(Valid.class)) {
				fieldName = "";
				fieldIn = "";
				fieldOut = "";
				fieldName = field.getName();
				Valid valid = field.getAnnotation(Valid.class);
				if (valid.dto() != Object.class) {
					instance = (I) valid.dto().getConstructor().newInstance();
				} else {
					instance = (I) dto.getClass().getConstructor().newInstance();
				}
				if (StringUtils.isNotBlank(valid.fieldIn())) {
					fieldIn = valid.fieldIn();
				} else {
					fieldIn = fieldName;
				}
				if (StringUtils.isNotBlank(valid.fieldOut())) {
					fieldOut = valid.fieldOut();
				} else {
					fieldOut = fieldName;
				}
				instance.set(fieldIn, dto.get(fieldName));
				List<I> list = querySvc.gets(instance);
				if (list == null || list.size() == 0)
					throw new Exception("No se encontro datos " + instance.getClass().getCanonicalName()
							+ " con el valor " + dto.get(field.getName()) + " suministrado.");
				if (field.isAnnotationPresent(Unique.class)) {
					if (list.size() == 1) {
						T result = (T) list.get(0);
						dto.set(fieldName, result.get(fieldOut));
					} else {
						throw new Exception(
								"Se encontro varios registros con datos " + instance.getClass().getCanonicalName()
										+ " con el valor " + dto.get(field.getName()) + " suministrado.");
					}
				} else {
					if (list.size() == 1) {
						T result = (T) list.get(0);
						dto.set(fieldName, result.get(fieldOut));
					} else {
						throw new Exception("No se ha implementado esta opcion para usar multiples valores.");
					}
				}
			}
		}
		return dto;
	}
}
