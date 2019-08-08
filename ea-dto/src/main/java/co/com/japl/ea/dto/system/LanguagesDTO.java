package co.com.japl.ea.dto.system;

import org.pyt.common.abstracts.ADto;

public class LanguagesDTO extends ADto {
	private static final long serialVersionUID = 1L;
	private String code;
	private String text;
	private String idiom;
	private String iconCss;
	private String iconPath;
	private String state;

	public String getCode() {
		return this.code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getText() {
		return this.text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getIdiom() {
		return this.idiom;
	}

	public void setIdiom(String idiom) {
		this.idiom = idiom;
	}

	public String getIconCss() {
		return this.iconCss;
	}

	public void setIconCss(String iconCss) {
		this.iconCss = iconCss;
	}

	public String getIconPath() {
		return this.iconPath;
	}

	public void setIconPath(String iconPath) {
		this.iconPath = iconPath;
	}

	public String getState() {
		return this.state;
	}

	public void setState(String state) {
		this.state = state;
	}
}
