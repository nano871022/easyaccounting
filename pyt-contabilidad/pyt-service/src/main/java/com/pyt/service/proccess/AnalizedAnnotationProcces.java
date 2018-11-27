package com.pyt.service.proccess;

import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.pyt.common.annotation.proccess.IsNotBlank;
import org.pyt.common.annotation.proccess.Size;
import org.pyt.common.annotation.proccess.Unique;
import org.pyt.common.annotation.proccess.Valid;
import org.pyt.common.annotations.Inject;
import org.pyt.common.common.ADto;
import org.pyt.common.common.Log;
import org.pyt.common.common.ValidateValues;
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

	/**
	 * Se encarga derealizar todas las validaciones sobre los campoos del dto
	 * verificando si los campos con anotacion {@link IsNotBlank} el campo con la
	 * anotacion no debe encontrarse vacio o nulo
	 * 
	 * @param dto
	 *            {@link ADto} extends
	 * @return {@link ADto} extends
	 * @throws {@link
	 *             Exception}
	 */
	@SuppressWarnings("unchecked")
	public final <T extends ADto, V extends Object> Boolean isNotBlank(T dto) throws Exception {
		Boolean valid = true;
		Class<T> clazz = (Class<T>) dto.getClass();
		for (Field field : clazz.getDeclaredFields()) {
			if (field.isAnnotationPresent(IsNotBlank.class)) {
				if (field.trySetAccessible()) {
					V value = (V) field.get(dto);
					if (field.getType() == String.class) {
						if (value == null || StringUtils.isNotBlank(String.class.cast(value))) {
							valid &= false;
						}
					} else if (value == null) {
						valid &= false;
					}
				}
			}
		}
		return valid;
	}

	/**
	 * Se encarga de validar en el dto el campo fecha anotado con {@link DateTime}
	 * 
	 * @param dto
	 *            {@link ADto} extends
	 * @param fieldName
	 *            {@link String}
	 * @param value
	 *            {@link Object} extends
	 * @return {@link Object}
	 * @throws {@link
	 *             Exception}
	 */
	@SuppressWarnings({ "unchecked", "unused" })
	public final <T extends ADto, V extends Object> T DateTime(T dto, String fieldName, V value) throws Exception {
		Class<T> clazz = (Class<T>) dto.getClass();
		Field field = clazz.getDeclaredField(fieldName);
		if (field.isAnnotationPresent(org.pyt.common.annotation.proccess.DateTime.class)) {
			org.pyt.common.annotation.proccess.DateTime dt = field
					.getDeclaredAnnotation(org.pyt.common.annotation.proccess.DateTime.class);
			SimpleDateFormat sdf = new SimpleDateFormat(dt.format());
			ValidateValues vv = new ValidateValues();
			if (vv.isCast(sdf.parse((String) value), field.getType())) {
				if (field.trySetAccessible()) {
					field.set(dto, vv.cast(sdf.parse((String) value), field.getType()));
				}
			}
		}
		return dto;
	}
	/**
	 * Se encarga de validar si el campos de texto y numericos con anotacion {@link Size}, para verificar que cumpla las reglas indicadas
	 * @param dto {@link ADto}
	 * @param fieldName {@link String}
	 * @throws {@link Exception}
	 */
	@SuppressWarnings("unchecked")
	public final <T extends ADto> void size(T dto,String fieldName)throws Exception{
		Class<T> clazz = (Class<T>) dto.getClass();
		Field field = clazz.getDeclaredField(fieldName);
		if(field.isAnnotationPresent(Size.class)) {
			Size size = field.getDeclaredAnnotation(Size.class);
			ValidateValues vv = new ValidateValues();
			if(field.getType() == String.class) {
				String value = dto.get(fieldName);
				if(StringUtils.isNotBlank(value)) {
					if(value.length() >= size.max()) {
						throw new Exception("El campo tiene mas caracteres de lo permitido "+value.length()+":"+size.max());
					}
					if(value.length() <= size.min()) {
						throw new Exception("El campo tiene menos caracteres de lo permitido "+value.length()+":"+size.min());
					}
				}
			}
		}
	}
	
	/**
	 * Se encarga de valida el dto todass las anotaciones que tengan la anotacion
	 * Valid, con la cual se verifica el valor
	 * 
	 * @param dto
	 *            {@link ADto} extends
	 * @return {@link ADto} extends
	 * @throws {@link
	 *             Exception}
	 */
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
