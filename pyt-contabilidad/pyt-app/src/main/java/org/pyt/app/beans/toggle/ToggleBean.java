package org.pyt.app.beans.toggle;

import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.collections4.MultiValuedMap;
import org.apache.commons.collections4.multimap.ArrayListValuedHashMap;
import org.controlsfx.glyphfont.FontAwesome.Glyph;
import org.pyt.common.annotations.Inject;
import org.pyt.common.constants.PermissionConstants;
import org.pyt.common.exceptions.GenericServiceException;

import com.pyt.service.interfaces.IGenericServiceSvc;

import co.com.arquitectura.annotation.proccessor.FXMLFile;
import co.com.japl.ea.beans.abstracts.AGenericInterfacesBean;
import co.com.japl.ea.common.button.apifluid.ButtonsImpl;
import co.com.japl.ea.dto.system.ConfigGenericFieldDTO;
import co.com.japl.ea.dto.system.ToggleDTO;
import co.com.japl.ea.utls.PermissionUtil;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;

/**
 * Bean encargado de crear las empresas
 * 
 * @author alejandro parra
 * @since 07/05/2018
 */
@FXMLFile(path = "view/toggle", file = "toggle.fxml")
public class ToggleBean extends AGenericInterfacesBean<ToggleDTO> {
	@Inject
	private IGenericServiceSvc<ToggleDTO> toggleSvc;
	@FXML
	private javafx.scene.control.TableView<ToggleDTO> tabla;
	@FXML
	private TextField tfSearch;
	@FXML
	private Label lblTittle;
	@FXML
	private Label lblSelectedOption;
	@FXML
	private ToggleButton tbStatus;
	@FXML
	private HBox paginator;
	@FXML
	private GridPane gpRowSelected;
	@FXML
	private RadioButton rfSelected;
	public static final String CONST_TITTLE_NAME = "page.tittle.toggl";
	@FXML
	private HBox buttons;
	@FXML
	private GridPane filter;
	private MultiValuedMap<TypeGeneric, ConfigGenericFieldDTO> fieldsConfig;

	@FXML
	public void initialize() {
		NombreVentana = i18n().valueBundle(CONST_TITTLE_NAME).get();
		registro = new ToggleDTO();
		filtro = new ToggleDTO();
		gpRowSelected.setVisible(false);
		fieldsConfig = new ArrayListValuedHashMap<>();
		findFields(TypeGeneric.COLUMN, ToggleDTO.class, ToggleBean.class)
				.forEach(row -> fieldsConfig.put(TypeGeneric.COLUMN, row));
		findFields(TypeGeneric.FILTER, ToggleDTO.class, ToggleBean.class)
				.forEach(row -> fieldsConfig.put(TypeGeneric.FILTER, row));
		loadColumns();
		loadFields(TypeGeneric.FILTER);
		loadDataModel(paginator, tabla);
		visibleButtons();
		ButtonsImpl.Stream(HBox.class).setLayout(buttons).setName("fxml.btn.update").action(this::updateCacheSeleted)
				.icon(Glyph.SAVE).isVisible(save).setName("fxml.btn.cancel").action(this::cancel).build();
	}

	public void updateCacheSeleted() {
		var activado = this.rfSelected.isSelected();
		var dto = dataTable.getSelectedRow();
		dto.setActivado(activado);
		try {
			toggleSvc.update(dto, getUsuario());
		} catch (GenericServiceException e) {
			error(e);
		}
		cancel();
	}

	public void cancel() {
		lblSelectedOption.setText(null);
		gpRowSelected.setVisible(false);
	}

	@Override
	protected void visibleButtons() {
		var save = PermissionUtil.INSTANCE().havePerm(PermissionConstants.CONST_PERM_CREATE, ToggleBean.class,
				getUsuario().getGrupoUser());
		this.save.setValue(save);
	}

	@Override
	public void selectedRow(MouseEvent eventHandler) {
		gpRowSelected.setVisible(true);
		visibleButtons();
	}

	@Override
	public TableView<ToggleDTO> getTableView() {
		return tabla;
	}

	@Override
	public Integer getMaxColumns(TypeGeneric typeGeneric) {
		return 4;
	}

	@Override
	public List<ConfigGenericFieldDTO> getListGenericsFields(TypeGeneric typeGeneric) {
		return fieldsConfig.get(typeGeneric).stream().collect(Collectors.toList());
	}

	@Override
	public Class<ToggleDTO> getClazz() {
		return ToggleDTO.class;
	}

	@Override
	public GridPane getGridPane(TypeGeneric typeGeneric) {
		return filter;
	}

	@Override
	public ToggleDTO getFilterToTable(ToggleDTO filter) {
		return filter;
	}
}
