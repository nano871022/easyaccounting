package co.com.japl.ea.loader.pojo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ResultsPOJO {
	private List<String> errores;
	private Integer numLine;
	public ResultsPOJO() {
	}
	
	public ResultsPOJO(Integer numLine,String... errores) {
		this.numLine = numLine;
		setErrores(errores);
	}
	public Integer getNumLine() {
		return numLine;
	}
	public void setNumLine(Integer numLine) {
		this.numLine = numLine;
	}
	public List<String> getErrores() {
		return errores;
	}
	public void setErrores(String... errores) {
		if(errores.length == 0)return;
		this.errores = new ArrayList<String>();
		Arrays.asList(errores).forEach(e->this.errores.add(e));
	}
	public void addErrores(String errores) {
		if(this.errores == null) {
			this.errores = new ArrayList<String>();
		}
		this.errores.add(errores);
	}
	public void addAllErrores(String... errores) {
		if(errores.length == 0)return;
		if(this.errores == null) {
			setErrores(errores);
		}else
		Arrays.asList(errores).forEach(e->this.errores.add(e));
	}
}
