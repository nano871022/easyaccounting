package org.pyt.app.beans.config;

import java.util.List;

import org.apache.commons.collections4.MultiValuedMap;
import org.apache.commons.collections4.multimap.ArrayListValuedHashMap;
import org.controlsfx.glyphfont.FontAwesome.Glyph;
import org.pyt.common.annotations.Inject;
import org.pyt.common.constants.PermissionConstants;
import org.pyt.common.exceptions.LoadAppFxmlException;
import org.pyt.common.exceptions.MarcadorServicioException;

import com.pyt.service.dto.ConfiguracionDTO;
import com.pyt.service.interfaces.IConfigMarcadorServicio;

import co.com.arquitectura.annotation.proccessor.FXMLFile;
import co.com.japl.ea.beans.abstracts.AGenericInterfacesBean;
import co.com.japl.ea.common.button.apifluid.ButtonsImpl;
import co.com.japl.ea.dto.system.ConfigGenericFieldDTO;
import co.com.japl.ea.utls.PermissionUtil;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.fxml.FXML;
import javafx.scene.control.TableView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;

/**
 * Bean encargado de cargar la lista de las configuraciones
 * 
 * @author alejandro parra
 * @since 16/09/2018
 */
@FXMLFile(path = "view/config/servicios", file = "listConfigService.fxml")
public class ListConfigBean extends AGenericInterfacesBean<ConfiguracionDTO> {
	@Inject(resource = "com.pyt.service.implement.ConfigMarcadorServicioSvc")
	private IConfigMarcadorServicio configMarcadorServicioSvc;
	@FXML
	private TableView<ConfiguracionDTO> tabla;
	@FXML
	private HBox paginador;
	@FXML
	private FlowPane buttons;
	@FXML
	private GridPane filterGrid;
	private BooleanProperty cargue;
	private BooleanProperty report;
	private MultiValuedMap<TypeGeneric, ConfigGenericFieldDTO> config;

	@FXML
	public void initialize() {
		NombreVentana = i18n("fxml.title.list.configs");
		config = new ArrayListValuedHashMap<>();
		registro = new ConfiguracionDTO();
		save = new SimpleBooleanProperty();
		edit = new SimpleBooleanProperty();
		delete = new SimpleBooleanProperty();
		view = new SimpleBooleanProperty();
		cargue = new SimpleBooleanProperty();
		report = new SimpleBooleanProperty();
		filtro = new ConfiguracionDTO();
		findFields(TypeGeneric.FILTER, ConfiguracionDTO.class, this.getClass())
				.forEach(row -> config.put(TypeGeneric.FILTER, row));
		findFields(TypeGeneric.COLUMN, ConfiguracionDTO.class, this.getClass())
				.forEach(row -> config.put(TypeGeneric.COLUMN, row));
		loadDataModel(paginador, tabla);
		loadFields(TypeGeneric.FILTER);
		loadColumns();
		visibleButtons();
		ButtonsImpl.Stream(FlowPane.class).setLayout(buttons).setName("fxml.btn.new").action(this::add).icon(Glyph.SAVE)
				.isVisible(save).setName("fxml.btn.edit").action(this::set).icon(Glyph.SAVE).isVisible(edit)
				.setName("fxml.btn.delete").action(this::del).icon(Glyph.REMOVE).isVisible(delete)
				.setName("fxml.btn.view").action(this::set).icon(Glyph.FILE_TEXT).isVisible(view)
				.setName("fxml.btn.carge").action(this::cargue).icon(Glyph.FILE_ARCHIVE_ALT).isVisible(cargue)
				.setName("fxml.btn.report").action(this::reporte).icon(Glyph.FILE_PDF_ALT).isVisible(report).build();

	}

	public void add() {
		getController(ConfigServiceBean.class).load();
	}

	public void del() {
		try {
			registro = dataTable.getSelectedRow();
			if (registro != null) {
				configMarcadorServicioSvc.deleteConfiguracion(registro, getUsuario());
				notificar("Se ha eliminado la configuración.");
				dataTable.search();
			} else {
				notificar("No se ha seleccionado una registro.");
			}
		} catch (MarcadorServicioException e) {
			error(e);
		}
	}

	public void set() {
		registro = dataTable.getSelectedRow();
		if (registro != null) {
			getController(ConfigServiceBean.class).load(registro);
		} else {
			notificar("No se ha seleccionado una configutación.");
		}
	}

	public void reporte() {
		try {
			controllerPopup(GenConfigBean.class).load(dataTable.getSelectedRow().getConfiguracion());
		} catch (LoadAppFxmlException e) {
			error(e);
		}
	}

	public void cargue() {
		try {
			controllerPopup(LoaderServiceBean.class).load(dataTable.getSelectedRow().getConfiguracion());
		} catch (LoadAppFxmlException e) {
			error(e);
		}
	}

	protected void visibleButtons() {
		var save = PermissionUtil.INSTANCE().havePerm(PermissionConstants.CONST_PERM_CREATE, ListConfigBean.class,
				getUsuario().getGrupoUser());
		var edit = dataTable.isSelected() && PermissionUtil.INSTANCE().havePerm(PermissionConstants.CONST_PERM_UPDATE,
				ListConfigBean.class, getUsuario().getGrupoUser());
		var view = !save && PermissionUtil.INSTANCE().havePerm(PermissionConstants.CONST_PERM_READ,
				ListConfigBean.class, getUsuario().getGrupoUser());
		var delete = dataTable.isSelected() && PermissionUtil.INSTANCE().havePerm(PermissionConstants.CONST_PERM_DELETE,
				ListConfigBean.class, getUsuario().getGrupoUser());
		var report = dataTable.isSelected() ? dataTable.getSelectedRow().getReport() : false;
		var cargue = dataTable.isSelected() ? !dataTable.getSelectedRow().getReport() : false;
		this.save.setValue(save);
		this.delete.setValue(delete);
		this.edit.setValue(edit);
		this.view.setValue(view);
		this.cargue.setValue(cargue);
		this.report.setValue(report);
	}

	@Override
	public void selectedRow(MouseEvent eventHandler) {
		visibleButtons();
	}

	@Override
	public TableView<ConfiguracionDTO> getTableView() {
		return tabla;
	}

	@Override
	public Integer getMaxColumns(TypeGeneric typeGeneric) {
		return 4;
	}

	@Override
	public List<ConfigGenericFieldDTO> getListGenericsFields(TypeGeneric typeGeneric) {
		return (List<ConfigGenericFieldDTO>) config.get(typeGeneric);
	}

	@Override
	public Class<ConfiguracionDTO> getClazz() {
		return ConfiguracionDTO.class;
	}

	@Override
	public GridPane getGridPane(TypeGeneric typeGeneric) {
		return filterGrid;
	}

	@Override
	public ConfiguracionDTO getFilterToTable(ConfiguracionDTO filter) {
		return filter;
	}
}
