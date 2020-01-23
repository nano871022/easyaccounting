package co.com.japl.ea.dto.system;

import org.pyt.common.abstracts.ADto;

public class HelpDTO extends ADto {

	private static final long serialVersionUID = 1L;
	private String title;
	private String body;
	private String classPathBean;
	private String iconCss;
	private String iconPath;
	private String imagePath;
	private String msgBottom;
	private String msgRigth;
	private String css;
	private Integer state;

	public Integer getState() {
		return this.state;
	}

	public void setState(Integer state) {
		this.state = state;
	}

	public String getTitle() {
		return this.title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getBody() {
		return this.body;
	}

	public void setBody(String body) {
		this.body = body;
	}

	public String getClassPathBean() {
		return this.classPathBean;
	}

	public void setClassPathBean(String classPathBean) {
		this.classPathBean = classPathBean;
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

	public String getImagePath() {
		return this.imagePath;
	}

	public void setImagePath(String imagePath) {
		this.imagePath = imagePath;
	}

	public String getMsgBottom() {
		return this.msgBottom;
	}

	public void setMsgBottom(String msgBottom) {
		this.msgBottom = msgBottom;
	}

	public String getMsgRigth() {
		return this.msgRigth;
	}

	public void setMsgRigth(String msgRigth) {
		this.msgRigth = msgRigth;
	}

	public String getCss() {
		return this.css;
	}

	public void setCss(String css) {
		this.css = css;
	}
}
