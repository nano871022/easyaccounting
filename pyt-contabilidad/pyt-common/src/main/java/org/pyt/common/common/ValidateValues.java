package org.pyt.common.common;

import java.math.BigDecimal;
import java.math.BigInteger;

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
	 * @param value1 {@link Integer}
	 * @param value2 {@link Integer}
	 * @return {@link Boolean}
	 * @throws {@link Exception}
	 */
	private final Boolean validate(Integer value1, Integer value2)throws Exception{
		return value1.compareTo(value2)==0;
	}
	/**
	 * Se encarga de validar dos valores que son de tipo BigDecimal
	 * @param value1 {@link BigDecimal}
	 * @param value2 {@link BigDecimal}
	 * @return {@link Boolean}
	 * @throws {@link Exception}
	 */
	private final Boolean validate(BigDecimal value1, BigDecimal value2)throws Exception{
		return value1.compareTo(value2)==0;
	}
	/**
	 * Se encarga de validar dos valores que son de tipo BigInteger
	 * @param value1 {@link BigInteger}
	 * @param value2 {@link BigInteger}
	 * @return {@link Boolean}
	 * @throws {@link Exception}
	 */
	private final Boolean validate(BigInteger value1, BigInteger value2)throws Exception{
		return value1.compareTo(value2)==0;
	}
	/**
	 * Se encarga de validar dos valores que son de tipo Number
	 * @param value1 {@link Number}
	 * @param value2 {@link Number}
	 * @return {@link Boolean}
	 * @throws {@link Exception}
	 */
	private final Boolean validate(Number value1, Number value2)throws Exception{
		return value1.byteValue() == (value2).byteValue();
	}
	/**
	 * Se encarga de validar dos valores que son de tipo Long
	 * @param value1 {@link Long}
	 * @param value2 {@link Long}
	 * @return {@link Boolean}
	 * @throws {@link Exception}
	 */
	private final Boolean validate(Long value1, Long value2)throws Exception{
		return value1.compareTo(value2) == 0;
	}
	/**
	 * Se encarga de validar dos valores que son de tipo Double
	 * @param value1 {@link Double}
	 * @param value2 {@link Double}
	 * @return {@link Boolean}
	 * @throws {@link Exception}
	 */
	private final Boolean validate(Double value1, Double value2)throws Exception{
		return value1.compareTo(value2) == 0;
	}
	/**
	 * Se encarga de validar dos valores que son de tipo Short
	 * @param value1 {@link Short}
	 * @param value2 {@link Short}
	 * @return {@link Boolean}
	 * @throws {@link Exception}
	 */
	private final Boolean validate(Short value1, Short value2)throws Exception{
		return value1.compareTo(value2) == 0;
	}
	/**
	 * Se encarga de validar dos valores que son de tipo String
	 * @param value1 {@link String}
	 * @param value2 {@link String}
	 * @return {@link Boolean}
	 * @throws {@link Exception}
	 */
	private final Boolean validate(String value1, String value2)throws Exception{
		if(StringUtils.isNotBlank(value2)) {
			if(StringUtils.isBlank(value1)) return false;
			if(StringUtils.isNotBlank(value1) && StringUtils.isNotBlank(value2))
				return value1.contains(value2);
		}
		return true;
	}
	/**
	 * Se encarga de validar dos valores que son de tipo Object
	 * @param value1 {@link Object}
	 * @param value2 {@link Object}
	 * @return {@link Boolean}
	 * @throws {@link Exception}
	 */
	public final Boolean validate(Object value1, Object value2)throws ValidateValueException{
		try {
			if(value1 instanceof Integer && value2 instanceof Integer)       return validate((Integer)value1,(Integer)value2);
			if(value1 instanceof BigDecimal && value2 instanceof BigDecimal) return validate((BigDecimal)value1,(BigDecimal)value2);
			if(value1 instanceof BigInteger && value2 instanceof BigInteger) return validate((BigInteger)value1,(BigInteger)value2);
			if(value1 instanceof Long && value2 instanceof Long)      		 return validate((Long)value1,(Long)value2);
			if(value1 instanceof Double && value2 instanceof Double)  	 	 return validate((Double)value1,(Double)value2);
			if(value1 instanceof Short && value2 instanceof Short )   		 return validate((Short)value1,(Short)value2);
			if(value1 instanceof Number && value2 instanceof Number)  		 return validate((Number)value1,(Number)value2);
			if(value1 instanceof String && value2 instanceof String ) 		 return validate((String)value1,(String)value2);
			if(value1 != null && value2 != null )                            return value1.getClass().isInstance(value2.getClass());
			return true;
		}catch(Exception e) {
			throw new ValidateValueException("Se presento error en la validacion.",e);
		}
	}
	
}
