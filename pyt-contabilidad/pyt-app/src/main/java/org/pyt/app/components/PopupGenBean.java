package org.pyt.app.components;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.pyt.common.common.ADto;
import org.pyt.common.common.UtilControlFieldFX;
import org.pyt.common.common.ValidateValues;
import org.pyt.common.constants.IconFontConstant;
import org.pyt.common.constants.LanguageConstant;
import org.pyt.common.constants.StylesPrincipalConstant;
import org.pyt.common.exceptions.ReflectionException;
import org.pyt.common.exceptions.validates.ValidateValueException;

import com.pyt.service.pojo.GenericPOJO;

import co.com.arquitectura.annotation.proccessor.FXMLFile;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
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
public class PopupGenBean<T extends ADto> extends GenericInterfacesReflection<T> {

	private T filter;
	private DataTableFXML<T, T> table;

	private Map<String, GenericPOJO<T>> filtros;
	private Map<String, GenericPOJO<T>> columnas;

	private HBox paginador;
	private GridPane gridFilter;
	private TableView<T> tabla;

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
	public void initialize() {
		loadTable();
		try {
			configFilters();
			configColumnas();
			panel.setTop(gridFilter);
			panel.setCenter(tabla);
			panel.setAlignment(tabla, Pos.CENTER);
			panel.setBottom(paginador);
			filter = assingValuesParameterized(getInstaceOfGenericADto());
		} catch (Exception e) {
			error(e);
		}
	}

	/**
	 * Se encarga de configurar el mapa de filtros y agregar los campos de filtros a
	 * la pantalla
	 * 
	 * @throws {@link IllegalAccessException}
	 */
	private final void configFilters() throws IllegalAccessException {
		filtros = getMapFieldsByObject(filter, GenericPOJO.Type.FILTER);
		final var indices = new Index();
		var util = new UtilControlFieldFX();
		filtros.forEach((key, value) -> {
			Label label = new Label(i18n().valueBundle(LanguageConstant.GENERIC_FILTER_LBL
					+ getClassParameterized().getSimpleName() + "." + value.getNameShow()));
			gridFilter.add(label, indices.columnIndex, indices.rowIndex);

			var input = util.getFieldByField(value.getField());
			if (value.getWidth() > 0) {
				input.prefWidth(value.getWidth());
			}
			util.inputListenerToAssingValue(input, (obj) -> assingsValueToField(value.getField().getName(), obj));
			gridFilter.add(input, indices.columnIndex + 1, indices.rowIndex);
			indices.columnIndex = indices.columnIndex == 4 ? 0 : indices.columnIndex + 2;
			indices.rowIndex = indices.columnIndex == 0 ? indices.rowIndex + 1 : indices.rowIndex;
		});
		gridFilter.getStyleClass().add(StylesPrincipalConstant.CONST_GRID_STANDARD);
		gridFilter.add(util.buttonGenericWithEventClicked(() -> table.search(),
				i18n().valueBundle(LanguageConstant.GENERIC_FILTER_BTN_SEARCH), IconFontConstant.CONST_FONT_SEARCH), 0,
				indices.rowIndex + 1);
		gridFilter.add(util.buttonGenericWithEventClicked(() -> cleanFilter(),
				i18n().valueBundle(LanguageConstant.GENERIC_FILTER_BTN_CLEAN), IconFontConstant.CONST_FONT_REMOVE), 1,
				indices.rowIndex + 1);

	}

	private final <O extends Object> void assingsValueToField(String nameField, O value) {
		try {
			if (value == null) {
				logger.warn(i18n().valueBundle(String.format(LanguageConstant.LANGUAGE_FIELD_ASSIGN_EMPTY, nameField,
						getInstaceOfGenericADto().getClass().getSimpleName())));
			} else {
				filter.set(nameField, value);
			}
		} catch (ReflectionException | InvocationTargetException | IllegalAccessException | InstantiationException
				| NoSuchMethodException e) {
			logger().logger(e);
		}
	}

	protected class Index {
		Integer columnIndex = 0;
		Integer rowIndex = 0;
	}

	private final void cleanFilter() {
		try {
			var util = new UtilControlFieldFX();
			filter = assingValuesParameterized(getInstaceOfGenericADto());
			gridFilter.getChildren().forEach(child -> util.cleanValueByFieldFX(child));
		} catch (InvocationTargetException | IllegalAccessException | InstantiationException
				| NoSuchMethodException e) {
			logger().logger(e);
		}

	}

	/**
	 * Se encarga de ralizar la configuracion del mapa de columnas y agregar las
	 * columnas a la tabla indicada
	 * 
	 * @throws {@link IllegalAccessException}
	 */
	private final void configColumnas() throws IllegalAccessException {
		columnas = getMapFieldsByObject(filter, GenericPOJO.Type.COLUMN);
		var validateValues = new ValidateValues();
		columnas.forEach((key, value) -> {
			var tableColumn = new TableColumn<T, String>(i18n().valueBundle(LanguageConstant.GENERIC_FORM_COLUMN
					+ getClassParameterized().getSimpleName() + "." + value.getNameShow()));
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
		table = new DataTableFXML<T, T>(paginador, tabla, false) {

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
		filter = assingValuesParameterized(filter);
		var cantidad = table.getTotalRows(validFilter());
		if (cantidad == 1) {
			this.caller(caller, table.getList(validFilter(), 0, 1).get(0));
			this.closeWindow();
		} else if (cantidad == 0) {
			this.closeWindow();
			throw new Exception(String.format(i18n().valueBundle(LanguageConstant.GENERIC_NO_ROWS_inputText),
					i18n().valueBundle(LanguageConstant.GENERIC_DOT + getClassParameterized().getSimpleName())));
		} else {
			showWindow();
		}
	}

	@SuppressWarnings("unchecked")
	private void selectedRow(MouseEvent event) {
		try {
			if (event.getClickCount() > 0 && table.isSelected()) {
				this.caller(caller, table.getSelectedRow());
				this.closeWindow();
			}
		} catch (Exception e) {
			error(e);
		}
	}

}