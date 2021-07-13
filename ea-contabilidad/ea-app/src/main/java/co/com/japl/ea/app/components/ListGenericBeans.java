package co.com.japl.ea.app.components;

import static org.pyt.common.constants.InsertResourceConstants.CONST_RESOURCE_IMPL_SVC_CONFIG_GENERIC_FIELD;

import java.util.List;

import org.controlsfx.glyphfont.FontAwesome.Glyph;
import org.pyt.common.annotations.Inject;
import org.pyt.common.common.Log;
import org.pyt.common.constants.PermissionConstants;

import co.com.japl.ea.beans.abstracts.AGenericInterfacesBean;
import co.com.japl.ea.common.abstracts.ADto;
import co.com.japl.ea.common.button.apifluid.ButtonsImpl;
import co.com.japl.ea.dto.interfaces.IConfigGenericFieldSvc;
import co.com.japl.ea.dto.system.ConfigGenericFieldDTO;
import co.com.japl.ea.utls.PermissionUtil;
import javafx.scene.Parent;
import javafx.scene.control.TableView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class ListGenericBeans<T extends ADto> extends AGenericInterfacesBean<T> {
	protected Log logger = Log.Log(this.getClass());
	private GridPane gridPane;
	private TableView<T> tableView;
	private Integer maxColumn;
	private VBox principal;
	private Class<T> clazz;
	private HBox paginator;
	private HBox buttons;
	private T filter;
	@Inject(resource = CONST_RESOURCE_IMPL_SVC_CONFIG_GENERIC_FIELD)
	private IConfigGenericFieldSvc configGenericSvc;
	private List<ConfigGenericFieldDTO> listFilters;
	private List<ConfigGenericFieldDTO> listColumns;

	public ListGenericBeans() {
		principal = new VBox();
		gridPane = new GridPane();
		tableView = new TableView<>();
		paginator = new HBox();
		buttons = new HBox();
	}

	public void initialize() throws Exception {
		this.registro = clazz.getConstructor().newInstance();
		this.filtro = clazz.getConstructor().newInstance();
		gridPane.setHgap(10);
		gridPane.setVgap(10);
		listFilters = configGenericSvc.getFieldToFilters(this.getClass(), clazz);
		listColumns = configGenericSvc.getFieldToColumns(this.getClass(), clazz);
		loadDataModel(paginator, tableView);
		loadFields(TypeGeneric.FILTER);
		loadColumns();
		visibleButtons();
		ButtonsImpl.Stream(HBox.class).setLayout(buttons).setName("fxml.btn.create").action(this::create)
				.icon(Glyph.SAVE).isVisible(save).setName("fxml.btn.edit").action(this::edit).icon(Glyph.EDIT)
				.isVisible(edit).setName("fxml.btn.delete").action(this::remove).icon(Glyph.REMOVE).isVisible(delete)
				.build();
	}

	public Parent load(Class<T> classDto) throws Exception {
		clazz = classDto;
		principal.getChildren().add(gridPane);
		principal.getChildren().add(tableView);
		principal.getChildren().add(paginator);
		principal.getChildren().add(buttons);
		return principal;
	}

	public void create() {

	}

	public void edit() {

	}

	public void remove() {
	}

	@Override
	public void selectedRow(MouseEvent eventHandler) {
		try {
			var dto = dataTable.getSelectedRow();
//			getController(GenerBean.class).load(dto);
		} catch (Exception e) {
			error(e);
		}
	}

	@Override
	public TableView<T> getTableView() {
		return tableView;
	}

	@Override
	public Integer getMaxColumns(TypeGeneric typeGeneric) {
		return 10;
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
	public Class<T> getClazz() {
		return clazz;
	}

	@Override
	public GridPane getGridPane(TypeGeneric typeGeneric) {
		return gridPane;
	}

	@Override
	public T getFilterToTable(T filter) {
		return filter;
	}

	@Override
	protected void visibleButtons() {
		var save = PermissionUtil.INSTANCE().havePerm(PermissionConstants.CONST_PERM_CREATE, ListGenericBeans.class,
				getUsuario().getGrupoUser());
		var edit = dataTable.isSelected() && PermissionUtil.INSTANCE().havePerm(PermissionConstants.CONST_PERM_UPDATE,
				ListGenericBeans.class, getUsuario().getGrupoUser());
		var delete = dataTable.isSelected() && PermissionUtil.INSTANCE().havePerm(PermissionConstants.CONST_PERM_DELETE,
				ListGenericBeans.class, getUsuario().getGrupoUser());
		var view = !save && !edit && PermissionUtil.INSTANCE().havePerm(PermissionConstants.CONST_PERM_READ,
				ListGenericBeans.class, getUsuario().getGrupoUser());
		this.save.setValue(save);
		this.edit.setValue(edit);
		this.delete.setValue(delete);
		this.view.setValue(view);
	}

}
