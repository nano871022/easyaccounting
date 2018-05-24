package org.pyt.common.common;

import java.util.List;

import org.pyt.common.exceptions.LoadAppFxmlException;
import org.pyt.common.exceptions.ReflectionException;
import org.pyt.common.reflection.Reflection;

/**
 * bstracto bean para heredar todos los bean y tener objetos pre creados y
 * metodos que ayudan a trabajar con los controladores de fxml
 * 
 * @author Alejandro Parra
 * @since 2018-05-23
 * @param <T>
 */
public abstract class ABean<T extends ADto> extends Reflection {
	protected Integer page;
	protected Integer rows;
	protected Integer totalRows;
	protected List<T> lista;
	protected T filtro;
	protected T registro;
	protected UsuarioDTO userLogin;
	protected String NombreVentana;

	public ABean() {
		try {
			inject();
		} catch (ReflectionException e) {
			System.err.println(e);
		}
	}

	public <L extends ADto, S extends ABean<L>> S getController(Class<S> classOfBean) {
		try {
			return LoadAppFxml.BeanFxml(null,classOfBean);
		} catch (LoadAppFxmlException e) {
			Log.logger("El bean " + classOfBean.getName() + " no puede ser cargado.", e);
			error(e);
		}
		return null;
	}

	public void notificar(String msn) {
		System.out.println(msn);
	}

	@SuppressWarnings("hiding")
	public <T extends Exception> void error(T error) {
		Log.logger(error);
		System.err.println(error);
	}

	public void error(String msn) {
		Log.logger(msn);
		System.err.println(msn);
	}

	public Integer getPage() {
		return page;
	}

	public void setPage(Integer page) {
		this.page = page;
	}

	public void error(Throwable e) {

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

	public String getNombreVentana() {
		return NombreVentana;
	}

	public void setNombreVentana(String nombreVentana) {
		NombreVentana = nombreVentana;
	}

}
