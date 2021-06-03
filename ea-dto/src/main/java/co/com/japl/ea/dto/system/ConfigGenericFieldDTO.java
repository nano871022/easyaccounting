package co.com.japl.ea.dto.system;

import org.pyt.common.annotation.generics.DefaultFieldToGeneric;
import org.pyt.common.annotation.generics.DefaultFieldToGeneric.Uses;

import co.com.japl.ea.common.abstracts.ADto;

public class ConfigGenericFieldDTO extends ADto {
	private static final long serialVersionUID = 1L;
	@DefaultFieldToGeneric(use = Uses.FILTER, simpleNameClazzBean = "ListGenericInterfacesBean")
	@DefaultFieldToGeneric(use = Uses.COLUMN, simpleNameClazzBean = "ListGenericInterfacesBean")
	private String name;
	@DefaultFieldToGeneric(use = Uses.FILTER, simpleNameClazzBean = "ListGenericInterfacesBean")
	@DefaultFieldToGeneric(use = Uses.COLUMN, simpleNameClazzBean = "ListGenericInterfacesBean")
	private String classPath;
	@DefaultFieldToGeneric(use = Uses.FILTER, simpleNameClazzBean = "ListGenericInterfacesBean")
	private String classPathBean;
	private String description;
	@DefaultFieldToGeneric(use = Uses.COLUMN, simpleNameClazzBean = "ListGenericInterfacesBean")
	private String alias;
	@DefaultFieldToGeneric(use = Uses.COLUMN, simpleNameClazzBean = "ListGenericInterfacesBean")
	@DefaultFieldToGeneric(use = Uses.FILTER, simpleNameClazzBean = "ListGenericInterfacesBean")
	private Boolean isFilter;
	@DefaultFieldToGeneric(use = Uses.COLUMN, simpleNameClazzBean = "ListGenericInterfacesBean")
	@DefaultFieldToGeneric(use = Uses.FILTER, simpleNameClazzBean = "ListGenericInterfacesBean")
	private Boolean isColumn;
	@DefaultFieldToGeneric(use = Uses.FILTER, simpleNameClazzBean = "ListGenericInterfacesBean")
	private Boolean isRequired;
	private Double width;
	private Integer orden;
	private String fieldShow;
	private String nameGroup;
	private String valueDefault;
	private Boolean isVisible;
	@DefaultFieldToGeneric(use = Uses.FILTER, simpleNameClazzBean = "ListGenericInterfacesBean")
	private Integer state;

	public Integer getOrden() {
		return this.orden;
	}

	public void setOrden(Integer order) {
		this.orden = order;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getClassPath() {
		return this.classPath;
	}

	public void setClassPath(String classPath) {
		this.classPath = classPath;
	}

	public String getClassPathBean() {
		return this.classPathBean;
	}

	public void setClassPathBean(String classPathBean) {
		this.classPathBean = classPathBean;
	}

	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getAlias() {
		return this.alias;
	}

	public void setAlias(String alias) {
		this.alias = alias;
	}

	public Boolean getIsFilter() {
		return this.isFilter;
	}

	public void setIsFilter(Boolean isFilter) {
		this.isFilter = isFilter;
	}

	public Boolean getIsColumn() {
		return this.isColumn;
	}

	public void setIsColumn(Boolean isColumn) {
		this.isColumn = isColumn;
	}

	public Boolean getIsRequired() {
		return this.isRequired;
	}

	public void setIsRequired(Boolean isRequired) {
		this.isRequired = isRequired;
	}

	public Double getWidth() {
		return this.width;
	}

	public void setWidth(Double width) {
		this.width = width;
	}

	public Integer getState() {
		return this.state;
	}

	public void setState(Integer state) {
		this.state = state;
	}

	public String getFieldShow() {
		return fieldShow;
	}

	public void setFieldShow(String fieldShow) {
		this.fieldShow = fieldShow;
	}

	public String getNameGroup() {
		return nameGroup;
	}

	public void setNameGroup(String group) {
		this.nameGroup = group;
	}

	public String getValueDefault() {
		return valueDefault;
	}

	public void setValueDefault(String valueDefault) {
		this.valueDefault = valueDefault;
	}

	public Boolean getIsVisible() {
		return isVisible;
	}

	public void setIsVisible(Boolean isVisible) {
		this.isVisible = isVisible;
	}
}
