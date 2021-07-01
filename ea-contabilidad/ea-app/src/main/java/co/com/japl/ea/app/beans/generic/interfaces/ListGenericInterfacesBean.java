package co.com.japl.ea.app.beans.generic.interfaces;

import java.util.List;

import org.apache.commons.collections4.MultiValuedMap;
import org.apache.commons.collections4.multimap.ArrayListValuedHashMap;
import org.apache.commons.lang3.StringUtils;
import org.controlsfx.glyphfont.FontAwesome.Glyph;
import org.pyt.common.annotations.Inject;
import org.pyt.common.constants.LanguageConstant;
import org.pyt.common.constants.PermissionConstants;
import org.pyt.common.constants.StylesPrincipalConstant;

import co.com.arquitectura.annotation.proccessor.FXMLFile;
import co.com.japl.ea.app.components.ConfirmPopupBean;
import co.com.japl.ea.beans.abstracts.AGenericInterfacesBean;
import co.com.japl.ea.common.button.apifluid.ButtonsImpl;
import co.com.japl.ea.dto.interfaces.IConfigGenericFieldSvc;
import co.com.japl.ea.dto.system.ConfigGenericFieldDTO;
import co.com.japl.ea.exceptions.ConfigGenericFieldException;
import co.com.japl.ea.utls.PermissionUtil;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;

@FXMLFile(file = "list_generic_interfaces.fxml", path = "/view/genericInterfaces", nombreVentana = "Lista Interfaces Genericas")
public class ListGenericInterfacesBean extends AGenericInterfacesBean<ConfigGenericFieldDTO> {
	@FXML
	private HBox paginador;
	@FXML
	private TableView<ConfigGenericFieldDTO> tableGeneric;
	@FXML
	private HBox filterGeneric;
	@FXML
	private Label lblTitle;
	private GridPane gridPane;

	private List<ConfigGenericFieldDTO> listFieldsToFilters;
	private List<ConfigGenericFieldDTO> listFieldsToColumns;
	@Inject(resource = "com.pyt.service.implement.ConfigGenericFieldSvc")
	private IConfigGenericFieldSvc configGenericFieldsSvc;
	private MultiValuedMap<String, Object> toChoiceBox;
	@FXML
	private HBox buttons;

	@FXML
	private void initialize() {
		gridPane = new GridPane();
		gridPane.setHgap(10);
		gridPane.setVgap(10);
		filtro = new ConfigGenericFieldDTO();
		filterGeneric.getChildren().addAll(gridPane);
		lblTitle.setText(i18n().valueBundle(LanguageConstant.GENERIC_LBL_LIST_GENERIC_INTERFACES).get());
		toChoiceBox = new ArrayListValuedHashMap<>();
		try {
			listFieldsToFilters = configGenericFieldsSvc.getFieldToFilters(this.getClass(),
					ConfigGenericFieldDTO.class);
			listFieldsToColumns = configGenericFieldsSvc.getFieldToColumns(this.getClass(),
					ConfigGenericFieldDTO.class);
		} catch (ConfigGenericFieldException e) {
			error(e);
		}
		loadDataModel(paginador, tableGeneric);
		load();
		visibleButtons();
		ButtonsImpl.Stream(HBox.class).setLayout(buttons).setName("fxml.btn.create").action(this::add).icon(Glyph.SAVE)
				.isVisible(save).setName("fxml.btn.copy").action(this::copy).icon(Glyph.SAVE).isVisible(save)
				.setName("fxml.btn.edit").action(this::set).icon(Glyph.EDIT).isVisible(edit).setName("fxml.btn.delete")
				.action(this::del).icon(Glyph.REMOVE).isVisible(delete).setName("fxml.btn.view").action(this::set)
				.icon(Glyph.FILE_TEXT).isVisible(view).build();
	}

	public final void add() {
		this.getController(GenericInterfacesBean.class).load();
	}

	public final void del() {
		try {
			controllerPopup(ConfirmPopupBean.class).load("#{ListGenericInterfacesBean.delete}",
					i18n().valueBundle(LanguageConstant.LANGUAGE_WARNING_DELETE_ROW));
		} catch (Exception e) {
			error(e);
		}
	}

	public final void setDelete(Boolean del) {
		if (del) {
			dataTable.getSelectedRows().forEach(row -> {
				try {
					getServiceSvc().delete(row, getUsuario());
					notificar(i18n().valueBundle(LanguageConstant.LANGUAGE_SUCCESS_DELETE_CONFIG_ROW_CODE,
							row.getCodigo()));
				} catch (Exception e) {
					error(e);
				}
			});
		}
	}

	public final void set() {
		this.getController(GenericInterfacesBean.class).load(dataTable.getSelectedRow());
	}

	public final void clickTable() {
		visibleButtons();
	}

	public final void load() {
		try {
			loadFields(TypeGeneric.FILTER, StylesPrincipalConstant.CONST_GRID_STANDARD);
			loadColumns(StylesPrincipalConstant.CONST_TABLE_CUSTOM);
		} catch (Exception e) {
			error(e);
		}
	}

	public void copy() {
		try {
			var dto = dataTable.getSelectedRow();
			dto.setCodigo(null);
			this.getController(GenericInterfacesBean.class).load(dto);
		} catch (Exception e) {
			error(e);
		}
	}

	@Override
	public GridPane getGridPane(TypeGeneric typeGeneric) {
		return gridPane;
	}

	@Override
	public TableView<ConfigGenericFieldDTO> getTableView() {
		return tableGeneric;
	}

	@Override
	public Integer getMaxColumns(TypeGeneric typeGeneric) {
		return 2;
	}

	@Override
	public ConfigGenericFieldDTO getFilterToTable(ConfigGenericFieldDTO filter) {
		if (StringUtils.isNotBlank(filter.getAlias()) && !filter.getAlias().contains("%")) {
			filter.setAlias("%" + filter.getAlias() + "%");
		}
		if (StringUtils.isNotBlank(filter.getClassPath()) && !filter.getClassPath().contains("%")) {
			filter.setClassPath("%" + filter.getClassPath() + "%");
		}
		if (StringUtils.isNotBlank(filter.getClassPathBean()) && !filter.getClassPathBean().contains("%")) {
			filter.setClassPathBean("%" + filter.getClassPathBean() + "%");
		}
		if (StringUtils.isNotBlank(filter.getDescription()) && !filter.getDescription().contains("%")) {
			filter.setDescription("%" + filter.getDescription() + "%");
		}
		if (StringUtils.isNotBlank(filter.getName()) && !filter.getName().contains("%")) {
			filter.setName("%" + filter.getName() + "%");
		}
		return filter;
	}

	@Override
	public void selectedRow(MouseEvent eventHandler) {
		visibleButtons();
	}

	@Override
	public List<ConfigGenericFieldDTO> getListGenericsFields(TypeGeneric typeGeneric) {
		if (typeGeneric == null)
			throw new RuntimeException("Opcion no valida");
		if (typeGeneric == TypeGeneric.FILTER)
			return listFieldsToFilters;
		if (typeGeneric == TypeGeneric.COLUMN)
			return listFieldsToColumns;

		throw new RuntimeException("Opcion no valida");
	}

	@Override
	public MultiValuedMap<String, Object> getMapListToChoiceBox() {
		return toChoiceBox;
	}

	@Override
	public Class<ConfigGenericFieldDTO> getClazz() {
		return ConfigGenericFieldDTO.class;
	}

	protected void visibleButtons() {
		var save = PermissionUtil.INSTANCE().havePerm(PermissionConstants.CONST_PERM_CREATE,
				ListGenericInterfacesBean.class, getUsuario().getGrupoUser());
		var edit = dataTable.isSelected() && PermissionUtil.INSTANCE().havePerm(PermissionConstants.CONST_PERM_UPDATE,
				ListGenericInterfacesBean.class, getUsuario().getGrupoUser());
		var delete = dataTable.isSelected() && PermissionUtil.INSTANCE().havePerm(PermissionConstants.CONST_PERM_DELETE,
				ListGenericInterfacesBean.class, getUsuario().getGrupoUser());
		var view = !save && PermissionUtil.INSTANCE().havePerm(PermissionConstants.CONST_PERM_READ,
				ListGenericInterfacesBean.class, getUsuario().getGrupoUser());
		this.save.setValue(save);
		this.edit.setValue(edit);
		this.delete.setValue(delete);
		this.view.setValue(view);
	}
}
