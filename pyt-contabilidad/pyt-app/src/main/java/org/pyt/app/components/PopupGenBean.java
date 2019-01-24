package org.pyt.app.components;

import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.ArrayList;
import java.util.List;

import org.pyt.common.common.ABean;
import org.pyt.common.common.ADto;

import com.pyt.service.dto.ConceptoDTO;
import com.sun.tools.javadoc.main.ParameterizedTypeImpl;

import javafx.fxml.FXML;

/**
 * Esta clase se encarga de configurar un popup de consultas con el dto
 * suministrado con el cual se realizan todas las consultas para obtener las
 * listas y filtrar datos y retornar un valor al componente quien lo solicito
 * 
 * @author Alejandro Parra
 *
 * @param <T>
 *            extends {@link ADto}
 */
public class PopupGenBean<T extends ADto> extends ABean<T> {
	private static final String FILTER_NAME = "filter";
	private T filter;
	private DataTableFXML<T, T> table;

	@FXML
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
					// TODO crear codigo de consulta
					// list = servicio.gets(filter,page,rows);
				} catch (Exception e) {
					error(e);
				}
				return list;
			}

			@Override
			public Integer getTotalRows(T filter) {
				Integer cantidad = 0;
				try {
					// TODO crear codigo para obtener la cantidad de registros
					// cantidad = servicio.rows(filter);
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
	@SuppressWarnings("unchecked")
	private T instanceDto() {
		try {
			Field field = this.getClass().getDeclaredField(FILTER_NAME);
			Type type = field.getGenericType();
			Class clases = field.getDeclaringClass();
			TypeVariable[] vars = this.getClass().getTypeParameters();
			TypeVariable[] varz = this.getClass().getSuperclass().getTypeParameters();
			Class<T> clazz = (Class<T>) field.getType();
			return (T) clazz.getConstructor().newInstance();
		} catch (Exception e) {
			error(e);
		}
		return null;
	}
	
	public static void main(String...strings) {
		PopupGenBean<ConceptoDTO> popup = new PopupGenBean<ConceptoDTO>();
		popup.initialize();
	}
}
