package org.pyt.common.common;

import java.lang.reflect.Field;
import java.math.BigDecimal;

import org.pyt.common.abstracts.ADto;
import org.pyt.common.annotations.Operacion;
import org.pyt.common.annotations.Operaciones;
import org.pyt.common.reflection.ReflectionUtils;
import org.pyt.common.validates.ValidateValues;


public class AnnotationsOperacionUtil<F extends ADto> {
	private Field field;
	private F dto;
	private Operaciones operations;
	private Operacion operation;
	private ValidateValues validateValues = new ValidateValues();
	
	public AnnotationsOperacionUtil(F dto,Field field) {
		this.field = field;
		this.dto = dto;
		operationsByField();
	}
	
	private void operationsByField() {
		var value = field.getAnnotation(Operaciones.class);
		if(value != null && ListUtils.isNotBlank(value.value())) {
			operations = value;
		}else {
			var ope = field.getAnnotation(Operacion.class);
			if(ope != null) {
				operation = ope;
			}
		}
	}
	
	public void process() {
		if(operations != null && ListUtils.isNotBlank(operations.value())) {
			var list = operations.value();
			for(var value : list) {
				direction(value);
			}
		}else if(operation != null) {
			direction(operation);
		}
	}
	
	private void direction(Operacion operation) {
		switch(operation.operacion()) {
		case MULTIPLICAR:multiply(operation);break;
		case SUMA:add(operation);break;
		case DIVIDIR:divide(operation);break;
		case RESTAR:subtract(operation);break;
		case IGUAL:equal(operation);break;
	}
	}
	
	private void equal(Operacion value) {
		var campo1 = ReflectionUtils.getValueField(dto, value.valor1());
		ReflectionUtils.setValueField(dto, field.getName(), campo1);
	}
	
	private void divide(Operacion value) {
		var campo1 = ReflectionUtils.getValueField(dto, value.valor1());
		var campo2 = ReflectionUtils.getValueField(dto, value.valor2());
		BigDecimal result = new BigDecimal(0);
		if(campo1 != null && campo2 != null) {
			if(validateValues.isCast(campo1, BigDecimal.class)) {
				if(validateValues.isCast(campo2,BigDecimal.class)) {
					result = ((BigDecimal)campo1).divide((BigDecimal)campo2);
				}else {
					result = ((BigDecimal)campo1).divide(validateValues.cast(campo2, BigDecimal.class));
				}
			}else if(validateValues.isCast(campo2,BigDecimal.class)) {
				result = ((BigDecimal)campo2).divide(validateValues.cast(campo1, BigDecimal.class));
			}else {
				result = validateValues.cast(campo1, BigDecimal.class).divide(validateValues.cast(campo2, BigDecimal.class));
			}
		}
		ReflectionUtils.setValueField(dto, field.getName(), result);
	}
	
	private void subtract(Operacion value) {
		var campo1 = ReflectionUtils.getValueField(dto, value.valor1());
		var campo2 = ReflectionUtils.getValueField(dto, value.valor2());
		BigDecimal result = new BigDecimal(0);
		if(campo1 != null && campo2 != null) {
			if(validateValues.isCast(campo1, BigDecimal.class)) {
				if(validateValues.isCast(campo2,BigDecimal.class)) {
					result = ((BigDecimal)campo1).subtract((BigDecimal)campo2);
				}else {
					result = ((BigDecimal)campo1).subtract(validateValues.cast(campo2, BigDecimal.class));
				}
			}else if(validateValues.isCast(campo2,BigDecimal.class)) {
				result = ((BigDecimal)campo2).subtract(validateValues.cast(campo1, BigDecimal.class));
			}else {
				result = validateValues.cast(campo1, BigDecimal.class).subtract(validateValues.cast(campo2, BigDecimal.class));
			}
		}
		ReflectionUtils.setValueField(dto, field.getName(), result);
	}
	
	private void add(Operacion value) {
		var campo1 = ReflectionUtils.getValueField(dto, value.valor1());
		var campo2 = ReflectionUtils.getValueField(dto, value.valor2());
		BigDecimal result = new BigDecimal(0);
		if(campo1 != null && campo2 != null) {
			if(validateValues.isCast(campo1, BigDecimal.class)) {
				if(validateValues.isCast(campo2,BigDecimal.class)) {
					result = ((BigDecimal)campo1).add((BigDecimal)campo2);
				}else {
					result = ((BigDecimal)campo1).add(validateValues.cast(campo2, BigDecimal.class));
				}
			}else if(validateValues.isCast(campo2,BigDecimal.class)) {
				result = ((BigDecimal)campo2).add(validateValues.cast(campo1, BigDecimal.class));
			}else {
				result = validateValues.cast(campo1, BigDecimal.class).add(validateValues.cast(campo2, BigDecimal.class));
			}
		}else if(campo1 != null && campo2 == null) {
			var valueField = ReflectionUtils.getValueField(dto, field.getName());
			if(valueField != null && validateValues.isNumber(valueField.getClass())) {
				result = validateValues.cast(campo1, BigDecimal.class).add(validateValues.cast(valueField, BigDecimal.class));
			}
		}
		ReflectionUtils.setValueField(dto, field.getName(), result);
	}
	
	private void multiply(Operacion value) {
		var campo1 = ReflectionUtils.getValueField(dto, value.valor1());
		var campo2 = ReflectionUtils.getValueField(dto, value.valor2());
		BigDecimal result = new BigDecimal(0);
		if(campo1 != null && campo2 != null) {
			if(validateValues.isCast(campo1, BigDecimal.class)) {
				if(validateValues.isCast(campo2,BigDecimal.class)) {
					result = ((BigDecimal)campo1).multiply((BigDecimal)campo2);
				}else {
					result = ((BigDecimal)campo1).multiply(validateValues.cast(campo2, BigDecimal.class));
				}
			}else if(validateValues.isCast(campo2,BigDecimal.class)) {
				result = ((BigDecimal)campo2).multiply(validateValues.cast(campo1, BigDecimal.class));
			}else {
				result = validateValues.cast(campo1, BigDecimal.class).multiply(validateValues.cast(campo2, BigDecimal.class));
			}
		}
		ReflectionUtils.setValueField(dto, field.getName(), result);
	}
}
