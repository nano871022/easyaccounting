package com.pyt.service.pojo;

import java.lang.reflect.Field;

/**
 * Usado para poner columnas o campos para los bean que se crean dinamicos con
 * solo el dto que va a utilizar
 * 
 * @author Alejandro Parra
 * @since 29/01/2019
 */
public class GenericPOJO <O extends Object>{
	 public static enum Type {FILTER,COLUMN};
	 private String nameShow;
	 private Field field;
	 private O fieldUse;
	 private Type type;
	 private double width;
	 
	public GenericPOJO(String nameShow, Field field, O fieldUse, Type type,double width) {
		this.nameShow = nameShow;
		this.field = field;
		this.fieldUse = fieldUse;
		this.type = type;
		this.width = width;
	}
	public GenericPOJO(String nameShow, Field field, O fieldUse, Type type) {
		this.nameShow = nameShow;
		this.field = field;
		this.fieldUse = fieldUse;
		this.type = type;
	}
	public String getNameShow() {
		return nameShow;
	}
	public void setNameShow(String nameShow) {
		this.nameShow = nameShow;
	}
	public Field getField() {
		return field;
	}
	public void setField(Field field) {
		this.field = field;
	}
	public O getFieldUse() {
		return fieldUse;
	}
	public void setFieldUse(O fieldUse) {
		this.fieldUse = fieldUse;
	}
	public Type getType() {
		return type;
	}
	public void setType(Type type) {
		this.type = type;
	}
	public double getWidth() {
		return width;
	}
	public void setWidth(double width) {
		this.width = width;
	}
	
}