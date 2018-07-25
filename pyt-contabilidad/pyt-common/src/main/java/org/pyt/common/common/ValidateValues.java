package org.pyt.common.common;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

import org.apache.commons.lang3.StringUtils;
import org.pyt.common.exceptions.ValidateValueException;

/**
 * Se encarga de validar dos valores si son iguales o uno esta contenido en el
 * otro
 * 
 * @author Alejandro Parra
 * @since 22-06-2018
 */
public final class ValidateValues {
	/**
	 * Se encarga de validar dos valores que son de tipo Integer
	 * 
	 * @param value1
	 *            {@link Integer}
	 * @param value2
	 *            {@link Integer}
	 * @return {@link Boolean}
	 * @throws {@link
	 *             Exception}
	 */
	private final Boolean validate(Integer value1, Integer value2) throws Exception {
		return value1.compareTo(value2) == 0;
	}

	/**
	 * Se encarga de validar dos valores que son de tipo BigDecimal
	 * 
	 * @param value1
	 *            {@link BigDecimal}
	 * @param value2
	 *            {@link BigDecimal}
	 * @return {@link Boolean}
	 * @throws {@link
	 *             Exception}
	 */
	private final Boolean validate(BigDecimal value1, BigDecimal value2) throws Exception {
		return value1.compareTo(value2) == 0;
	}

	/**
	 * Se encarga de validar dos valores que son de tipo BigInteger
	 * 
	 * @param value1
	 *            {@link BigInteger}
	 * @param value2
	 *            {@link BigInteger}
	 * @return {@link Boolean}
	 * @throws {@link
	 *             Exception}
	 */
	private final Boolean validate(BigInteger value1, BigInteger value2) throws Exception {
		return value1.compareTo(value2) == 0;
	}

	/**
	 * Se encarga de validar dos valores que son de tipo Number
	 * 
	 * @param value1
	 *            {@link Number}
	 * @param value2
	 *            {@link Number}
	 * @return {@link Boolean}
	 * @throws {@link
	 *             Exception}
	 */
	private final Boolean validate(Number value1, Number value2) throws Exception {
		return value1.byteValue() == (value2).byteValue();
	}

	/**
	 * Se encarga de validar dos valores que son de tipo Long
	 * 
	 * @param value1
	 *            {@link Long}
	 * @param value2
	 *            {@link Long}
	 * @return {@link Boolean}
	 * @throws {@link
	 *             Exception}
	 */
	private final Boolean validate(Long value1, Long value2) throws Exception {
		return value1.compareTo(value2) == 0;
	}

	/**
	 * Se encarga de validar dos valores que son de tipo Double
	 * 
	 * @param value1
	 *            {@link Double}
	 * @param value2
	 *            {@link Double}
	 * @return {@link Boolean}
	 * @throws {@link
	 *             Exception}
	 */
	private final Boolean validate(Double value1, Double value2) throws Exception {
		return value1.compareTo(value2) == 0;
	}

	/**
	 * Se encarga de validar dos valores que son de tipo Short
	 * 
	 * @param value1
	 *            {@link Short}
	 * @param value2
	 *            {@link Short}
	 * @return {@link Boolean}
	 * @throws {@link
	 *             Exception}
	 */
	private final Boolean validate(Short value1, Short value2) throws Exception {
		return value1.compareTo(value2) == 0;
	}

	/**
	 * Se encarga de validar dos valores que son de tipo String
	 * 
	 * @param value1
	 *            {@link String}
	 * @param value2
	 *            {@link String}
	 * @return {@link Boolean}
	 * @throws {@link
	 *             Exception}
	 */
	private final Boolean validate(String value1, String value2) throws Exception {
		if (StringUtils.isNotBlank(value2)) {
			if (StringUtils.isBlank(value1))
				return false;
			if (StringUtils.isNotBlank(value1) && StringUtils.isNotBlank(value2)) {
				if (value2.contains("%")) {
					String[] split = value2.split("%");
					Boolean valid = true;
					for (String seg : split) {
						if (StringUtils.isNotBlank(seg)) {
							valid &= value1.contains(seg);
						}
					}
					return valid;
				} else {
					Boolean val = value1.contentEquals(value2);
					return val;
				}
			}
		}else if(StringUtils.isBlank(value2)) {
			return true;
		}
		return false;
	}

	/**
	 * Se encarga de validar dos valores que son de tipo Object
	 * 
	 * @param value1
	 *            {@link Object}
	 * @param value2
	 *            {@link Object}
	 * @return {@link Boolean}
	 * @throws {@link
	 *             Exception}
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public final Boolean validate(Object value1, Object value2) throws ValidateValueException {
		try {
			if (value1 instanceof Integer && value2 instanceof Integer)
				return validate((Integer) value1, (Integer) value2);
			if (value1 instanceof BigDecimal && value2 instanceof BigDecimal)
				return validate((BigDecimal) value1, (BigDecimal) value2);
			if (value1 instanceof BigInteger && value2 instanceof BigInteger)
				return validate((BigInteger) value1, (BigInteger) value2);
			if (value1 instanceof Long && value2 instanceof Long)
				return validate((Long) value1, (Long) value2);
			if (value1 instanceof Double && value2 instanceof Double)
				return validate((Double) value1, (Double) value2);
			if (value1 instanceof Short && value2 instanceof Short)
				return validate((Short) value1, (Short) value2);
			if (value1 instanceof Number && value2 instanceof Number)
				return validate((Number) value1, (Number) value2);
			if (value1 instanceof String && value2 instanceof String)
				return validate((String) value1, (String) value2);
			if (value1 instanceof ADto && value2 instanceof ADto)
				if (value1.getClass() == value2.getClass())
					return validateDto(value1, value2);
			if (value1 != null && value2 != null) {
				try {
					Object o1 = ((Class) value1).getConstructor().newInstance();
					Object o2 = ((Class) value2).getConstructor().newInstance();
					return (o1.getClass() == o2.getClass());
				} catch (Exception e) {
				}
				return value1.getClass() == value2.getClass();
			}
			return true;
		} catch (Exception e) {
			throw new ValidateValueException("Se presento error en la validacion.", e);
		}
	}

	@SuppressWarnings({ "unchecked", "unused" })
	public final <T extends ADto> Boolean validateDto(Object obj, Object obj2) throws ValidateValueException {
		T dto = (T) obj;
		if (new Compare<T>((T) obj).to((T) obj2)) {
			return true;
		}
		return false;
	}

	/**
	 * Se encarga de realizar cast de un valor a otro valor
	 * 
	 * @param value
	 *            {@link Object} extended
	 * @param clase
	 *            {@link Class} extended
	 * @return {@link Object} extended
	 * @throws ValidateValueException
	 */
	@SuppressWarnings("unchecked")
	public final <T, S extends Object> T cast(S value, Class<T> clase) throws ValidateValueException {
		try {
			if (value == null)
				return null;
			if (clase == null)
				return null;
			if (clase == Date.class) {
				if (value.getClass() == LocalDate.class) {
					return (T) Date.from(((LocalDate) value).atStartOfDay(ZoneId.systemDefault()).toInstant());
				}
			}
			if(clase == LocalDate.class) {
				if(value.getClass() == Date.class) {
					return (T) LocalDate.ofInstant(((Date)value).toInstant(),ZoneId.systemDefault());
				}
			}
			if (clase == int.class) {
				clase = (Class<T>) Integer.class;
			}
			if (clase == short.class) {
				clase = (Class<T>) Short.class;
			}
			if (clase == double.class) {
				clase = (Class<T>) Double.class;
			}
			if (clase == long.class) {
				clase = (Class<T>) Long.class;
			}
			if (value.getClass() == String.class) {
				if (clase == Integer.class) {
					return (T) Integer.valueOf((String) value);
				}
				if (clase == Double.class) {
					return (T) Double.valueOf((String) value);
				}
				if (clase == Long.class) {
					return (T) Long.valueOf((String) value);
				}
				if (clase == Short.class) {
					return (T) Short.valueOf((String) value);
				}
			}
			if(value.getClass() == Integer.class) {
				if(clase == BigDecimal.class){
					return (T) new BigDecimal((int)value);
				}
			}
			if (value.getClass() == clase) {
				return (T) value;
			}

			Method[] metodos = clase.getDeclaredMethods();
			for (Method metodo : metodos) {
				if (Modifier.isStatic(metodo.getModifiers())) {
					if (metodo.getReturnType() == clase) {
						Class<?>[] clases = metodo.getParameterTypes();
						if (clases != null && clases.length == 1) {
							if (clases[0] == value.getClass()) {
								return (T) metodo.invoke(null, value);
							} else if (isCast(value, clases[0])) {
								T val = (T) cast(value, clases[0]);
								try {
									return (T) metodo.invoke(null, val);
								} catch (Exception e) {
								}
							}
						}
					}
				}
			}
			metodos = value.getClass().getMethods();
			for(Method metodo : metodos) {
				if(metodo.getParameterTypes().length == 0) {
					if(metodo.getReturnType() == clase) {
						return (T) metodo.invoke(value);
					}
				}
			}

		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			throw new ValidateValueException(e.getMessage(), e);
		}
		return null;
	}

	/**
	 * Se encarga de verificar si el valor no esta vacio y es del tipo de la clase
	 * indicado
	 * 
	 * @param value
	 *            {@link Object}
	 * @param clase
	 *            {@link Class}
	 * @return {@link Boolean}
	 */
	@SuppressWarnings("unchecked")
	public final <T, L extends Object> Boolean isCast(T value, Class<L> clase) {
		try {
			if (clase == int.class) {
				clase = (Class<L>) Integer.class;
			}
			if (clase == short.class) {
				clase = (Class<L>) Short.class;
			}
			if (clase == double.class) {
				clase = (Class<L>) Double.class;
			}
			if (clase == long.class) {
				clase = (Class<L>) Long.class;
			}
			if (clase == Integer.class) {
				if (value != null && value instanceof Integer) {
					return true;
				} else if (value.getClass() == String.class) {
					Integer.valueOf((String) value);
					return true;
				}
			}
			if (clase == BigDecimal.class) {
				if (value != null && value instanceof BigDecimal) {
					return true;
				}
			}
			if (clase == BigInteger.class) {
				if (value != null && value instanceof BigInteger) {
					return true;
				}
			}
			if (clase == Long.class) {
				if (value != null && value instanceof Long) {
					return true;
				} else if (value.getClass() == String.class) {
					Long.valueOf((String) value);
					return true;
				}
			}
			if (clase == Double.class) {
				if (value != null && value instanceof Double) {
					return true;
				} else if (value.getClass() == String.class) {
					Double.valueOf((String) value);
					return true;
				}
			}
			if (clase == Short.class) {
				if (value != null && value instanceof Short) {
					return true;
				} else if (value.getClass() == String.class) {
					Short.valueOf((String) value);
					return true;
				}
			}
			if (clase == Date.class) {
				if (value != null && value instanceof Date)
					return true;
				if (value != null && value instanceof LocalDate)
					return true;
			}
			if (clase == Number.class)
				if (value != null && value instanceof Number)
					return true;
			if (clase == String.class)
				if (value != null && value instanceof String && StringUtils.isNotBlank((String) value))
					return true;
			if (clase == ADto.class)
				if (value instanceof ADto)
					return true;
			return false;
		} catch (Exception e) {
			return false;
		}
	}

	@SuppressWarnings("unchecked")
	public final <S extends Object, T extends Object, P extends Object> S multiplicar(T value, P value2) {
		try {
			if(value == null) return null;
			if(value2 == null)return null;
			if(isCast(value,Long.class)) {
				if(isCast(value2,BigDecimal.class)) {
					return (S) cast(value2,BigDecimal.class).multiply(cast(value,BigDecimal.class));
				}
			}
		} catch (ValidateValueException e) {
			Log.logger(e);
		}
		return null;
	}
	
	@SuppressWarnings("unchecked")
	public final <S,T,P extends Object> S sumar(T value,P value2) {
		try {
			if(value == null) return null;
			if(value2 == null)return null;
			if(isCast(value,Long.class)) {
				if(isCast(value2,BigDecimal.class)) {
					return (S) cast(value2,BigDecimal.class).add(cast(value,BigDecimal.class));
				}
			}
			if(isCast(value,BigDecimal.class)) {
				if(isCast(value2,BigDecimal.class)) {
					return (S) cast(value2,BigDecimal.class).add(cast(value,BigDecimal.class));
				}
			}
		} catch (ValidateValueException e) {
			Log.logger(e);
		}
		return null;
	}
}
