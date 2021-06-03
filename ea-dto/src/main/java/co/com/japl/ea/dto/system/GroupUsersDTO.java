package co.com.japl.ea.dto.system;

import co.com.japl.ea.common.abstracts.ADto;

public class GroupUsersDTO extends ADto {
	private static final long serialVersionUID = 1L;
	private String name;
	private String description;
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

	public Integer getState() {
		return this.state;
	}

	public void setState(Integer state) {
		this.state = state;
	}
}
