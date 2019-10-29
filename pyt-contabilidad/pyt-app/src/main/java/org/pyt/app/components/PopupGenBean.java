package org.pyt.app.components;

import static co.com.japl.ea.interfaces.IGenericCommons.TypeGeneric.FILTER;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections4.MultiValuedMap;
import org.apache.commons.lang3.StringUtils;
import org.pyt.common.abstracts.ADto;
import org.pyt.common.annotations.Inject;
import org.pyt.common.constants.LanguageConstant;
import org.pyt.common.constants.StylesPrincipalConstant;
import org.pyt.common.exceptions.ReflectionException;
import org.pyt.common.exceptions.validates.ValidateValueException;
import org.pyt.common.validates.ValidateValues;

import com.pyt.service.interfaces.IGenericServiceSvc;
import com.pyt.service.pojo.GenericPOJO;

import co.com.arquitectura.annotation.proccessor.FXMLFile;
import co.com.japl.ea.beans.abstracts.AGenericInterfacesReflectionBean;
import co.com.japl.ea.dto.system.ConfigGenericFieldDTO;
import co.com.japl.ea.utls.DataTableFXMLUtil;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TableView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;

/**
 * Esta clase se encarga de configurar un popup de consultas con el dto
 * suministrado con el cual se realizan todas las consultas para obtener las
 * listas y filtrar datos y retornar un valor al componente quien lo solicito
 * 
 * @author Alejandro Parra
 *
 * @param <T> extends {@link ADto}
 */
@FXMLFile(path = "/", file = ".", nombreVentana = "PopupGenericBean")
public class PopupGenBean<T extends ADto> extends AGenericInterfacesReflectionBean<T> {

	private T filter;
	private DataTableFXMLUtil<T, T> table;

	private Map<String, GenericPOJO<T>> filtros;
	private Map<String, GenericPOJO<T>> columnas;

	@Inject(resource = "com.pyt.service.impls.GenericServiceSvc")
	private IGenericServiceSvc<ConfigGenericFieldDTO> configGenericFieldSvc;

	private HBox paginador;
	private GridPane gridFilter;
	private TableView<T> tabla;

	@SuppressWarnings("unused")
	public PopupGenBean(Class<T> clazz) throws Exception {
		super(clazz);
		paginador = new HBox();
		paginador.getStyleClass().add(StylesPrincipalConstant.CONST_HBOX_PAGINATOR_CUSTOM);
		gridFilter = new GridPane();
		tabla = new TableView<T>();
		tabla.getStyleClass().add(StylesPrincipalConstant.CONST_TABLE_CUSTOM);
		tittleWindowI18n = LanguageConstant.GENERIC_WINDOW_POPUP_TITTLE + clazz.getSimpleName();
		sizeWindow(750, 500);
	}

	@SuppressWarnings("static-access")
	@FXML
	@Override
	public void initialize() {
		loadTable();
		try {
			loadFields(FILTER);
			loadColumns();
			panel.setTop(gridFilter);
			panel.setCenter(tabla);
			panel.setAlignment(tabla, Pos.CENTER);
			panel.setBottom(paginador);
			// filter = assingValuesParameterized(getInstanceDto(FILTER));
		} catch (Exception e) {
			error(e);
		}
	}

	/**
	 * Se encarga de ralizar la configuracion del mapa de columnas y agregar las
	 * columnas a la tabla indicada
	 * 
	 * @throws {@link IllegalAccessException}
	 */
	private final void configColumnas() throws IllegalAccessException {
		// columnas = getConfigFields(filter, true, false);
		var validateValues = new ValidateValues();
		columnas.forEach((key, value) -> {
			var tableColumn = new TableColumn<T, String>(i18n().valueBundle(
					LanguageConstant.GENERIC_FORM_COLUMN + getClazz().getSimpleName() + "." + value.getNameShow()));
			if (value.getWidth() > 0) {
				tableColumn.setPrefWidth(value.getWidth());
			}
			tableColumn.setCellValueFactory((CellDataFeatures<T, String> param) -> {
				try {
					return new ReadOnlyStringWrapper(
							validateValues.cast(param.getValue().get(value.getField().getName()), String.class));
				} catch (ReflectionException | ValidateValueException e) {
					return null;
				}
			});
			tabla.getColumns().add(tableColumn);
		});
		tabla.setOnMouseClicked(event -> selectedRow(event));
	}

	private void loadTable() {
		table = new DataTableFXMLUtil<T, T>(paginador, tabla, false) {

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
				return validFilter();
			}

		};
	}

	public T validFilter() {
		filtros.forEach((key, value) -> {
			try {
				if (((Class<?>) filter.getType(value.getField().getName())) == String.class
						&& StringUtils.isNotBlank(filter.get(value.getField().getName()))) {
					filter.set(value.getField().getName(), "%" + filter.get(value.getField().getName()) + "%");
				}
			} catch (ReflectionException e) {
				logger().logger(e);
			}
		});
		return filter;
	}

	/**
	 * Se le indica en el momento de la carga a quien le debe retornar al
	 * seleccionar un registro
	 * 
	 * @param caller {@link String}
	 */
	@SuppressWarnings("unchecked")
	public final void load(String caller) throws Exception {
		this.caller = caller;
		// filter = assingValuesParameterized(filter);
		var cantidad = table.getTotalRows(validFilter());
		if (cantidad == 1) {
			this.caller(caller, table.getList(validFilter(), 0, 1).get(0));
			this.closeWindow();
		} else if (cantidad == 0) {
			this.closeWindow();
			throw new Exception(String.format(i18n().valueBundle(LanguageConstant.GENERIC_NO_ROWS_inputText),
					i18n().valueBundle(LanguageConstant.GENERIC_DOT + getClazz().getSimpleName())));
		} else {
			showWindow();
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public void selectedRow(MouseEvent event) {
		try {
			if (event.getClickCount() > 0 && table.isSelected()) {
				this.caller(caller, table.getSelectedRow());
				this.closeWindow();
			}
		} catch (Exception e) {
			error(e);
		}
	}

	public T getFilter() {
		return filter;
	}

	@Override
	public GridPane getGridPane(TypeGeneric typeGeneric) {
		return gridFilter;
	}

	public void setFilter(T filter) {
		this.filter = filter;
	}

	public void setFilters(Map<String, GenericPOJO<T>> filters) {
		filtros = filters;
	}

	public Map<String, GenericPOJO<T>> getFilters() {
		return filtros;
	}

	public IGenericServiceSvc<ConfigGenericFieldDTO> getServiceSvc() {
		return configGenericFieldSvc;
	}

	@Override
	public Integer getMaxColumns(TypeGeneric typeGeneric) {
		return 2;
	}

	@Override
	public List<ConfigGenericFieldDTO> getListGenericsFields(TypeGeneric typeGeneric) {
		return null;
	}

	@Override
	public MultiValuedMap<String, Object> getMapListToChoiceBox() {
		return null;
	}

	@Override
	public TableView<T> getTableView() {
		return tabla;
	}

}