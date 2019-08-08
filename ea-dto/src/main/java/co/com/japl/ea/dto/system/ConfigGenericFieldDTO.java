package co.com.japl.ea.dto.system;

import org.pyt.common.abstracts.ADto;

public class ConfigGenericFieldDTO extends ADto {
	private static final long serialVersionUID = 1L;
	private String name;
	private String classPath;
	private String classPathBean;
	private String description;
	private String alias;
	private Boolean isFilter;
	private Boolean isColumn;
	private Boolean isRequired;
	private Double width;
	private Integer state;

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
}
