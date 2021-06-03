package com.japl.ea.query.privates.jpa;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import co.com.japl.ea.common.abstracts.ADto;

/**
 * Se encarga de controlar los campos que se muestran y se configuran por el
 * formulario dinamico
 * 
 * @author Alejandro Parra
 * @since  30-06-2018
 */
@Entity(name="TBL_DOCUMENTS")
@Table(name="TBL_DOCUMENTS")
public class DocumentosJPA extends AJPA {
	@Column(name="sfieldlable")
	private String fieldLabel;
	@Column(name="sfieldname")
	private String fieldName;
	@ManyToOne @JoinColumn(name="sdoctype")
	private ParametroJPA doctype;
	@Column(name="nedit")
	private Boolean edit;
	@Column(name="nnullable")
	private Boolean nullable;
	@Column(name="sobjectsearchdto")
	private Class objectSearchDto;
	@Column(name="sselectnamegroup")
	private String selectNameGroup;
	@Column(name="sputnameshow")
	private String putNameShow;
	@Column(name="sputnameassign")
	private String putNameAssign;
	@Column(name="sputfieldname")
	private Boolean putFieldName;
	@Column(name="scontrollerclass")
	private Class claseControlar;
	
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
	public ParametroJPA getDoctype() {
		return doctype;
	}
	public void setDoctype(ParametroJPA doctype) {
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
	
}
