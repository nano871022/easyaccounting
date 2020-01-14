package com.pyt.service.dto;

import org.pyt.common.abstracts.ADto;

/**
 * Se encarga de controlar los campos que se muestran y se configuran por el
 * formulario dinamico
 * 
 * @author Alejandro Parra
 * @since 30-06-2018
 */

public class DocumentosDTO extends ADto {
	private static final long serialVersionUID = -8417260332275892148L;
	private String fieldLabel;
	private String fieldName;
	private ParametroDTO doctype;
	private Boolean edit;
	private Boolean nullable;
	@SuppressWarnings("rawtypes")
	private Class objectSearchDto;
	private String selectNameGroup;
	private String putNameShow;
	private String putNameAssign;
	private Boolean putFieldName;
	@SuppressWarnings("rawtypes")
	private Class claseControlar;
	private Boolean fieldFilter;
	private Boolean fieldColumn;
	private Boolean fieldHasDefaultValue;
	private Boolean fieldIsVisible;
	private String  fieldDefaultValue;

	public String getPutNameShow() {
		return putNameShow;
	}

	public void setPutNameShow(String putNameShow) {
		this.putNameShow = putNameShow;
	}

	public String getPutNameAssign() {
		return putNameAssign;
	}

	public void setPutNameAssign(String putNameAssign) {
		this.putNameAssign = putNameAssign;
	}

	@SuppressWarnings("rawtypes")
	public Class getClaseControlar() {
		return claseControlar;
	}

	@SuppressWarnings("rawtypes")
	public void setClaseControlar(Class claseControlar) {
		this.claseControlar = claseControlar;
	}

	@SuppressWarnings("unchecked")
	public <T extends ADto> Class<T> getObjectSearchDto() {
		return objectSearchDto;
	}

	public <T extends Object> void setObjectSearchDto(Class<T> class1) {
		this.objectSearchDto = class1;
	}

	public String getFieldLabel() {
		return fieldLabel;
	}

	public void setFieldLabel(String fieldLabel) {
		this.fieldLabel = fieldLabel;
	}

	public String getFieldName() {
		return fieldName;
	}

	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}

	public ParametroDTO getDoctype() {
		return doctype;
	}

	public void setDoctype(ParametroDTO doctype) {
		this.doctype = doctype;
	}

	public Boolean getEdit() {
		return edit;
	}

	public void setEdit(Boolean edit) {
		this.edit = edit;
	}

	public Boolean getNullable() {
		return nullable;
	}

	public void setNullable(Boolean nullable) {
		this.nullable = nullable;
	}

	public String getSelectNameGroup() {
		return selectNameGroup;
	}

	public void setSelectNameGroup(String selectNameGroup) {
		this.selectNameGroup = selectNameGroup;
	}

	public Boolean getPutFieldName() {
		return putFieldName;
	}

	public void setPutFieldName(Boolean putFieldName) {
		this.putFieldName = putFieldName;
	}

	public Boolean getFieldFilter() {
		return this.fieldFilter;
	}

	public void setFieldFilter(Boolean fieldFilter) {
		this.fieldFilter = fieldFilter;
	}

	public Boolean getFieldColumn() {
		return this.fieldColumn;
	}

	public void setFieldColumn(Boolean fieldColumn) {
		this.fieldColumn = fieldColumn;
	}

	public Boolean getFieldHasDefaultValue() {
		return fieldHasDefaultValue;
	}

	public void setFieldHasDefaultValue(Boolean fieldHasDefaultValue) {
		this.fieldHasDefaultValue = fieldHasDefaultValue;
	}

	public Boolean getFieldIsVisible() {
		return fieldIsVisible;
	}

	public void setFieldIsVisible(Boolean fieldIsVisible) {
		this.fieldIsVisible = fieldIsVisible;
	}

	public String getFieldDefaultValue() {
		return fieldDefaultValue;
	}

	public void setFieldDefaultValue(String fieldDefaultValue) {
		this.fieldDefaultValue = fieldDefaultValue;
	}
}
