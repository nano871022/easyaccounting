package org.pyt.app.components;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.pyt.common.annotations.Inject;
import org.pyt.common.common.ABean;
import org.pyt.common.common.ADto;

import com.pyt.service.interfaces.IQuerysPopup;

/**
 * Esta clase se encarga de configurar un popup de consultas con el dto
 * suministrado con el cual se realizan todas las consultas para obtener las
 * listas y filtrar datos y retornar un valor al componente quien lo solicito
 * 
 * @author Alejandro Parra
 *
 * @param <T> extends {@link ADto}
 */
public class PopupGenBean<T extends ADto> extends ABean<T> {
	private static final String FILTER_NAME = "filter";
	private T filter;
	private List<T> l = new ArrayList<T>();
	private DataTableFXML<T, T> table;
	private Class<T> clazz;
	@Inject
	private IQuerysPopup querys;
	private Map<String,Object> filtros;
	private Map<String,Object> columnas;

	/**
	 * Se encarga de recibir la clase que implementa la paremitrizacion del objeto
	 * 
	 * @param clazz {@link Class}
	 * @return {@link PopupGenBean}
	 */
	public final PopupGenBean<T> dtoClass(Class<T> clazz) {
		this.clazz = clazz;
		return this;
	}

	public void initialize() {
		loadTable();
		filter = instanceDto();
	}

	private void loadTable() {
		table = new DataTableFXML<T, T>() {

			@Override
			public List<T> getList(T filter, Integer page, Integer rows) {
				List<T> list = new ArrayList<T>();
				try {
					list = querys.list(filter, page, rows, userLogin);
				} catch (Exception e) {
					error(e);
				}
				return list;
			}

			@Override
			public Integer getTotalRows(T filter) {
				Integer cantidad = 0;
				try {
					cantidad = querys.records(filter, userLogin);
				} catch (Exception e) {
					error(e);
				}
				return cantidad;
			}

			@Override
			public T getFilter() {
				T filter = instanceDto();
				return filter;
			}
		};
	}

	/**
	 * Se encarga de crear una instancia apartir del objeto configurado
	 * 
	 * @return T extends {@link ADto}
	 */
	private T instanceDto() {
		try {
			if (clazz == null)
				throw new Exception("El Dto que se implementa no fue suministrado (Usar metodo PopupGenBean dtoClass(Class)).");
			return clazz.getConstructor().newInstance();
		} catch (Exception e) {
			error(e);
		}
		return null;
	}

}