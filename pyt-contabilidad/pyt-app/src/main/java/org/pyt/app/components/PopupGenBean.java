package org.pyt.app.components;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.pyt.common.annotations.Inject;
import org.pyt.common.common.ABean;
import org.pyt.common.common.ADto;

import com.pyt.service.interfaces.IQuerysPopup;
import com.pyt.service.pojo.GenericPOJO;

import javafx.fxml.FXML;

/**
 * Esta clase se encarga de configurar un popup de consultas con el dto
 * suministrado con el cual se realizan todas las consultas para obtener las
 * listas y filtrar datos y retornar un valor al componente quien lo solicito
 * 
 * @author Alejandro Parra
 *
 * @param <T> extends {@link ADto}
 */
public class PopupGenBean<T extends ADto> extends GenericInterfacesReflection<T> {
	private static final String FILTER_NAME = "filter";
	private T filter;
	private List<T> l = new ArrayList<T>();
	private DataTableFXML<T, T> table;
	
	private Map<String, GenericPOJO> filtros;
	private Map<String, GenericPOJO> columnas;
	
	/**
	 * Se encarga de recibir la clase que implementa la paremitrizacion del objeto
	 * 
	 * @param clazz {@link Class}
	 * @return {@link PopupGenBean}
	 */
	public final GenericInterfacesReflection<T> dtoClass(Class<T> clazz) throws Exception{
		this.clazz = clazz;
		return this;
	}
	@FXML
	public void initialize() {
		loadTable();
		filter = instanceDto();
		filtros = new HashMap<String, GenericPOJO>();
		columnas = new HashMap<String, GenericPOJO>();
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
	 * Se enmcarga de obtener el nombre del campo a mostrar como filtro
	 * @param dto extends {@link ADto}
	 * @param field {@link Field}
	 * @return {@link String}
	 */
	private final <D extends ADto> String nameShow(D dto,Field field) {
		return field.getName();
	}
	/**
	 * Se encarga de retornar el valor o una instancia del valor del tipo del campo creado
	 * @param field
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	private final <O extends Object> O objectByField(Field field)throws Exception {
		if(field != null) {
			O out = (O) field.get(filtros);
			if(out != null) {
				return out;
			}else {
				return (O)field.getGenericType().getClass().getConstructor().newInstance();
			}
		}else {
			throw new Exception("El campo suministrado es nulo.");
		}
	}
	
	/**
	 * Se encarga de cargar todos los campos del dto y se ponen como filtros de la
	 * configuracion, segun el tipo de dato retorna un objeto que puede usar fx para
	 * poner un campo de informacion para recibir informacion
	 */
	private final void loadFiltros() throws Exception{
		Field[] fields = filtros.getClass().getDeclaredFields();
		for (Field field : fields) {
			if (!Modifier.isStatic(field.getModifiers()) && !Modifier.isAbstract(field.getModifiers())
					&& !Modifier.isFinal(field.getModifiers())) {
				filtros.put(field.getName(),
						new GenericPOJO<Object>(nameShow(filter,field), field, objectByField(field), GenericPOJO.Type.FILTER));
			}
		}
	}

}