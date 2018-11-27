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
		this.errores = Arrays.asList(errores);
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
		this.errores = Arrays.asList(errores);
	}
	public void addErrores(String errores) {
		if(this.errores == null) {
			this.errores = new ArrayList<String>();
		}
		this.errores.add(errores);
	}
}
