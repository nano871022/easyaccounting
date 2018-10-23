package org.pyt.common.proccesor;

/**
 * Almacena la informacion de los serivicios
 * 
 * @author Alejandro Parra
 * @since 22/01/2018
 */
public class FXMLFilePOJO {
	private String name;
	private String alias;
	private Class<?> classs;

	public <M extends Object> FXMLFilePOJO(String name, String alias, Class<M> classs) {
		this.name = name;
		this.alias = alias;
		this.classs = classs;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAlias() {
		return alias;
	}

	public void setAlias(String alias) {
		this.alias = alias;
	}

	public Class<?> getClasss() {
		return classs;
	}

	public void setClasss(Class<?> classs) {
		this.classs = classs;
	}
}
