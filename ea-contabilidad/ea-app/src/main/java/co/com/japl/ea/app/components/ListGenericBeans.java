package co.com.japl.ea.app.components;

import static org.apache.commons.lang3.StringUtils.isNotBlank;

import java.util.List;

import org.pyt.common.common.Log;
import org.pyt.common.constants.LanguageConstant;

import co.com.japl.ea.common.abstracts.ADto;
import co.com.japl.ea.dto.system.ConfigGenericFieldDTO;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.scene.control.TableView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;

public class ListGenericBeans<T extends ADto> extends AGenericsBeans<T, ListGenericBeans<T>> {
	protected Log logger = Log.Log(this.getClass());

	private TableView<T> tableView;
	private Integer maxColumn;
	private HBox paginator;

	private List<ConfigGenericFieldDTO> listFilters;
	private List<ConfigGenericFieldDTO> listColumns;

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public ListGenericBeans() {
		super((Class) ListGenericBeans.class);
		tableView = new TableView<>();
		paginator = new HBox();
		maxColumn = 10;
	}

	public void initializeMethod() throws Exception {
		this.filtro = clazz.getConstructor().newInstance();
		listFilters = configGenericSvc.getFieldToFilters(this.getClass(), clazz);
		listColumns = configGenericSvc.getFieldToColumns(this.getClass(), clazz);
		loadDataModel(paginator, tableView);
		loadFields(TypeGeneric.FILTER);
		loadColumns();
		loadNameTitle(clazz.getSimpleName() + ".bean.list.title");
	}

	@Override
	public void loadMethod() throws Exception {
		principal.getChildren().add(tableView);
		principal.getChildren().add(paginator);
	}

	@Override
	public void selectedRow(MouseEvent eventHandler) {
		visibleButtons();
	}

	@Override
	public TableView<T> getTableView() {
		return tableView;
	}

	@Override
	public Integer getMaxColumns(TypeGeneric typeGeneric) {
		return maxColumn;
	}

	@Override
	public List<ConfigGenericFieldDTO> getListGenericsFields(TypeGeneric typeGeneric) {
		switch (typeGeneric) {
		case FILTER:
			return listFilters;
		case COLUMN:
			return listColumns;
		default:
			break;
		}
		return null;
	}

	@Override
	public T getFilterToTable(T filter) {
		return filter;
	}

	@SuppressWarnings("unchecked")
	public void create() {
		try {
			getController(GenericBeans.class).load();
		} catch (Exception e) {
			logger().logger(e);
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	void update() {
		try {
			var registro = dataTable.getSelectedRow();
			if (registro != null && isNotBlank(registro.getCodigo())) {
				getController(GenericBeans.class).load(registro);
			}
		} catch (Exception e) {
			logger().logger(e);
		}
	}

	@Override
	void delete() {
		try {
			controllerPopup(ConfirmPopupBean.class).load("#{ListGenericBeans.remove}",
					i18n().valueBundle(LanguageConstant.LANGUAGE_WARNING_DELETE_ROW));
		} catch (Exception e) {
			error(e);
		}
	}

	public void setRemove(Boolean confirm) {
		if (confirm) {
			try {
				var registro = dataTable.getSelectedRow();
				if (registro != null && isNotBlank(registro.getCodigo())) {
					serviceSvc.delete(registro, getUsuario());
					alertaI18n(String.format(registro.getClass().getSimpleName() + ".with.code.was.removed",
							registro.getCodigo()));
					dataTable.search();
				}

			} catch (Exception e) {
				error(e);
			}
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	void copy() {
		var registro = dataTable.getSelectedRow();
		if (registro != null && isNotBlank(registro.getCodigo())) {
			registro.setCodigo(null);
			getController(GenericBeans.class).load(registro);
		}

	}

	@Override
	void upload() {
		// TODO Auto-generated method stub

	}

	@Override
	void pay() {
		// TODO Auto-generated method stub

	}

	@Override
	void print() {
		// TODO Auto-generated method stub

	}

	void cancel() {

	}

	void visibleButtonsMethod() {
		edit.setValue(edit.and(new SimpleBooleanProperty(dataTable.isSelected())).getValue());
		delete.setValue(delete.and(new SimpleBooleanProperty(dataTable.isSelected())).getValue());
	}
}
