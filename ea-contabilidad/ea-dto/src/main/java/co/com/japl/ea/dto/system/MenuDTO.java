package co.com.japl.ea.dto.system;

import co.com.japl.ea.common.abstracts.ADto;

public class MenuDTO extends ADto {
	private static final long serialVersionUID = 1L;
	private String url;
	private String classPath;
	private String iconPath;
	private String iconCss;
	private String shortcut;
	private Integer state;

	public MenuDTO() {
	}

	public String getShortcut() {
		return shortcut;
	}


	public void setShortcut(String shortcut) {
		this.shortcut = shortcut;
	}


	public MenuDTO(String url, String value) {
		this.url = url;
		this.classPath = value;
		this.state = 1;
	}

	public String getUrl() {
		return this.url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getClassPath() {
		return this.classPath;
	}

	public void setClassPath(String classPath) {
		this.classPath = classPath;
	}

	public String getIconPath() {
		return this.iconPath;
	}

	public void setIconPath(String iconPath) {
		this.iconPath = iconPath;
	}

	public String getIconCss() {
		return this.iconCss;
	}

	public void setIconCss(String iconCss) {
		this.iconCss = iconCss;
	}

	public Integer getState() {
		return this.state;
	}

	public void setState(Integer state) {
		this.state = state;
	}

}
