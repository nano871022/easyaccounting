package org.pyt.common.common;

import java.lang.reflect.Field;

import org.pyt.common.exceptions.ReflectionException;
import org.pyt.common.exceptions.validates.ValidateValueException;


/**
 * Se encarga de comparar dos objetos
 * 
 * @author Alejandro Parra
 * @since 22-06-2018
 */
public final class Compare<T extends ADto> {
	private T origen;
	private ValidateValues validate;

	public Compare(T obj) {
		origen = obj;
		validate = new ValidateValues();
	}

	/**
	 * Se encarga de comparar el objeto suministrado con el objeto creado
	 * 
	 * @param dto
	 *            {@link ADto} extends
	 * @return {@link Boolean}
	 */
	@SuppressWarnings("unchecked")
	public final <S, L extends Object> Boolean to(T dto) {
		if (dto == null) {
			return false;
		}
		if (origen == null) {
			return false;
		}
		Class<T> clase = (Class<T>) dto.getClass();
		Boolean valid = process(clase, dto);
		if (!valid)
			return false;
		return true;
	}

	/**
	 * Se encarga de relizar elk procesamiento de los campos obtenidos para
	 * verificar los datos
	 * 
	 * @param clase
	 */
	private final <S, L> Boolean process(Class<T> clase, T dto) {
		T obj;
		Field[] fields = clase.getDeclaredFields();
		for (Field field : fields) {
			String name = field.getName();
			try {
				S value = origen.get(name);
				L value2 = dto.get(name);
				if (value == null && value2 != null) {
					return false;
				}
				if (!validate.validate(value, value2)) {
					return false;
				}
			} catch (ReflectionException e) {
				return false;
			} catch (ValidateValueException e) {
				return false;
			}
		} // end for
		if (clase != ADto.class) {
			Boolean valid = process((Class<T>) clase.getSuperclass(), dto);
			if (!valid)
				return false;
		}else {
			return true;
		}
		return true;
	}// end process
}
