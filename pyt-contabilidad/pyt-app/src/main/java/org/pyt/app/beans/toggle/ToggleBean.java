package org.pyt.app.beans.toggle;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.apache.commons.lang3.StringUtils;
import org.pyt.common.annotations.Inject;
import org.pyt.common.constants.AppConstants;
import org.pyt.common.exceptions.GenericServiceException;

import com.pyt.service.interfaces.IGenericServiceSvc;

import co.com.arquitectura.annotation.proccessor.FXMLFile;
import co.com.japl.ea.beans.abstracts.ABean;
import co.com.japl.ea.dto.system.ToggleDTO;
import co.com.japl.ea.utls.DataTableFXMLUtil;
import javafx.beans.property.SimpleObjectProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;

/**
 * Bean encargado de crear las empresas
 * 
 * @author alejandro parra
 * @since 07/05/2018
 */
@FXMLFile(path = "view/toggle", file = "toggle.fxml")
public class ToggleBean extends ABean<ToggleDTO> {
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
	private TableColumn<ToggleDTO, String> tcActived;
	@FXML
	private TableColumn<ToggleDTO, String> tcLastUpdated;
	@FXML
	private TableColumn<ToggleDTO, String> tcName;
	@FXML
	private GridPane gpRowSelected;
	@FXML
	private RadioButton rfSelected;
	private DataTableFXMLUtil<ToggleDTO, ToggleDTO> dt;
	public static final String CONST_TITTLE_NAME = "page.tittle.toggl";

	@FXML
	public void initialize() {
		NombreVentana = i18n().valueBundle(CONST_TITTLE_NAME).get();
		lblTittle.setText(NombreVentana);
		registro = new ToggleDTO();
		gpRowSelected.setVisible(false);
		tcName.setCellValueFactory(row -> new SimpleObjectProperty<>(
				Optional.ofNullable(row.getValue().getName()).orElse(i18n().valueBundle("err.not.found.value").get())));
		tcActived.setCellValueFactory(
				row -> new SimpleObjectProperty<String>(Optional.ofNullable(row.getValue().isActivado()).orElse(false)
						? i18n().valueBundle("message.value.active").get()
						: i18n().valueBundle("message.value.inactive").get()));
		tcLastUpdated.setCellValueFactory(row -> {
			var update = row.getValue().getFechaActualizacion();
			if (update != null) {
				return new SimpleObjectProperty<String>(
						new SimpleDateFormat(AppConstants.CONST_FORMAT_DATE_SHOW).format(update));
			}
			return null;
		});
		lazy();
	}

	/**
	 * encargada de crear el objeto que va controlar la tabla
	 */
	public void lazy() {
		dt = new DataTableFXMLUtil<ToggleDTO, ToggleDTO>(paginator, tabla) {

			@Override
			public List<ToggleDTO> getList(ToggleDTO filter, Integer page, Integer rows) {
				List<ToggleDTO> lista = new ArrayList<>();
				try {
					lista = toggleSvc.gets(filter, page, rows);
				} catch (GenericServiceException e) {
					error(e);
				}
				return lista;
			}

			@Override
			public Integer getTotalRows(ToggleDTO filter) {
				try {
					return toggleSvc.getTotalRows(filter);
				} catch (GenericServiceException e) {
					error(e);
				}
				return 0;
			}

			@Override
			public ToggleDTO getFilter() {
				var filtro = new ToggleDTO();
				if (StringUtils.isNotBlank(tfSearch.getText())) {
					filtro.setNombre(tfSearch.getText());
				}
				return filtro;
			}
		};
	}

	public void updateCacheSeleted() {
		var activado = this.rfSelected.isSelected();
		var dto = dt.getSelectedRow();
		dto.setActivado(activado);
		try {
			toggleSvc.update(dto, getUsuario());
		} catch (GenericServiceException e) {
			error(e);
		}
		cancel();
	}

	public void clickTable() {
		if (dt.isSelected()) {
			lblSelectedOption.setText(dt.getSelectedRow().getName());
			gpRowSelected.setVisible(true);
		} else {
			lblSelectedOption.setText(null);
			gpRowSelected.setVisible(false);
		}
	}

	public void cancel() {
		lblSelectedOption.setText(null);
		gpRowSelected.setVisible(false);
		search();
	}

	public void search() {
		dt.search();
	}

	public Boolean isSelected() {
		return dt.isSelected();
	}

	public DataTableFXMLUtil<ToggleDTO, ToggleDTO> getDt() {
		return dt;
	}
}
