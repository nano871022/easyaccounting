package co.com.japl.ea.beans.abstracts;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.collections4.MultiValuedMap;
import org.pyt.common.annotations.Inject;
import org.pyt.common.constants.StylesPrincipalConstant;

import co.com.japl.ea.common.abstracts.ADto;
import co.com.japl.ea.dto.interfaces.IConfigGenericFieldSvc;
import co.com.japl.ea.dto.interfaces.IQuerysPopup;
import co.com.japl.ea.dto.system.ConfigGenericFieldDTO;
import co.com.japl.ea.utls.DataTableFXMLUtil;
import javafx.scene.control.TableView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;

/**
 * Clase encargada de tener codio generico para poder implementar las paginas de
 * creacion dinamica segun el tipo de la clase indicado
 * 
 * @author Alejandro Parra
 * @since 09/04/2019
 */
public abstract class AGenericInterfacesReflectionBean<T extends ADto> extends AGenericToBean<T> {

	@Inject(resource = "com.pyt.service.implement.QuerysPopupSvc")
	protected IQuerysPopup querys;
	@Inject(resource = "com.pyt.service.implement.ConfigGenericFieldSvc")
	protected IConfigGenericFieldSvc configGenericFieldsSvc;
	protected DataTableFXMLUtil<T, T> table;
	protected HBox paginador;
	protected TableView<T> tabla;
	protected GridPane gridFilter;
	protected List<ConfigGenericFieldDTO> listColumns;
	protected List<ConfigGenericFieldDTO> listFilters;
	protected MultiValuedMap<String, Object> mapToChoiceBox;

	/**
	 * Se debe suministrar la clase con la cual se indica el generico
	 * 
	 * @param clazz {@link Class}
	 * @throws {@link Exception}
	 */
	@SuppressWarnings("unused")
	public AGenericInterfacesReflectionBean(Class<T> clazz) throws Exception {
		super();
		paginador = new HBox();
		paginador.getStyleClass().add(StylesPrincipalConstant.CONST_HBOX_PAGINATOR_CUSTOM);
		gridFilter = new GridPane();
		tabla = new TableView<T>();
		tabla.getStyleClass().add(StylesPrincipalConstant.CONST_TABLE_CUSTOM);
		setClazz(clazz);
		instaceOfGenericDTOAll(this.getClass());
	}

	@SuppressWarnings("unused")
	protected void loadTable() {
		table = new DataTableFXMLUtil<T, T>(paginador, tabla, false) {

			@Override
			public List<T> getList(T filter, Integer page, Integer rows) {
				List<T> list = new ArrayList<T>();
				try {
					list = querys.list(filter, page, rows, getUsuario());
				} catch (Exception e) {
					error(e);
				}
				return list;
			}

			@Override
			public Integer getTotalRows(T filter) {
				Integer cantidad = 0;
				try {
					cantidad = querys.records(filter, getUsuario());
				} catch (Exception e) {
					error(e);
				}
				return cantidad;
			}

			@Override
			public T getFilter() {
				return applyFilterToDataTableSearch();
			}

		};
	}

	public abstract T applyFilterToDataTableSearch();

	/**
	 * Se encarga de recorrer todos los campos de la aplicacion y aquellos que sean
	 * del generico de la clase les crea una instancia en la cosntruccion de la
	 * clase
	 * 
	 * @throws {@link Exception}
	 */
	private final void instaceOfGenericDTOAll(Class clazz) throws Exception {
		if (clazz.equals(Object.class))
			return;
		Field[] fields = clazz.getDeclaredFields();
		Arrays.asList(fields).stream().filter(field -> field.getType() == ADto.class)
				.forEach(field -> configFieldValue(field));
		instaceOfGenericDTOAll(clazz.getSuperclass());
	}

	/**
	 * Se encargad e agregar el valor en el campo y que se obliga a poder ingresarle
	 * informacion directamente
	 * 
	 * @param field {@link Field}
	 */
	private final void configFieldValue(Field field) {
		try {
			if (!field.canAccess(this)) {
				field.trySetAccessible();
			}
			var instance = clazz.getDeclaredConstructor().newInstance();
			field.set(this, instance);
		} catch (Exception exception) {
			logger().logger(exception);
		}
	}

	@SuppressWarnings("incomplete-switch")
	@Override
	public List<ConfigGenericFieldDTO> getListGenericsFields(TypeGeneric typeGeneric) {
		switch (typeGeneric) {
		case FILTER:
			return listFilters;
		case COLUMN:
			return listColumns;
		}
		return null;
	}

	@Override
	public GridPane getGridPane(TypeGeneric typeGeneric) {
		return gridFilter;
	}

	@Override
	public TableView<T> getTableView() {
		return tabla;
	}

	@Override
	public DataTableFXMLUtil<T, T> getTable() {
		return table;
	}

	@Override
	public MultiValuedMap<String, Object> getMapListToChoiceBox() {
		return mapToChoiceBox;
	}

}
