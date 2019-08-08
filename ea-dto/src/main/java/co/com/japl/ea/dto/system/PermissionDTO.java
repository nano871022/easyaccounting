package co.com.japl.ea.dto.system;

import org.pyt.common.abstracts.ADto;

public class PermissionDTO extends ADto {
	private static final long serialVersionUID = 1L;
	private String name;
	private String description;
	private String action;
	private Integer state;

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getAction() {
		return this.action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public Integer getState() {
		return this.state;
	}

	public void setState(Integer state) {
		this.state = state;
	}
}
