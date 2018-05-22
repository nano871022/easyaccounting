package org.pyt.common.common;

import java.util.List;

import org.pyt.common.exceptions.ReflectionException;
import org.pyt.common.reflection.Reflection;

public abstract class ABean<T extends ADto> extends Reflection {
	protected Integer page;
	protected Integer rows;
	protected Integer totalRows;
	protected List<T> lista;
	protected T filtro;
	protected T registro;

	public ABean() {
		try {
			inject();
		} catch (ReflectionException e) {
			System.err.println(e);
		}
	}

	public Integer getPage() {
		return page;
	}

	public void setPage(Integer page) {
		this.page = page;
	}

	public void error(Throwable e) {

	}

	public void error(String mensaje) {

	}

	public void info(String mensaje) {

	}

	public Integer getRows() {
		return rows;
	}

	public void setRows(Integer rows) {
		this.rows = rows;
	}

	public List<T> getLista() {
		return lista;
	}

	public void setLista(List<T> lista) {
		this.lista = lista;
	}

	public T getFiltro() {
		return filtro;
	}

	public void setFiltro(T filtro) {
		this.filtro = filtro;
	}

	public T getRegistro() {
		return registro;
	}

	public void setRegistro(T registro) {
		this.registro = registro;
	}

	public Integer getTotalRows() {
		return totalRows;
	}

	public void setTotalRows(Integer totalRows) {
		this.totalRows = totalRows;
	}
}
