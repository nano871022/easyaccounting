package org.pyt.app.beans.cache;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import org.apache.commons.lang3.StringUtils;
import org.pyt.common.common.CacheUtil;

import co.com.arquitectura.annotation.proccessor.FXMLFile;
import co.com.japl.ea.beans.abstracts.ABean;
import co.com.japl.ea.dto.system.CacheDTO;
import co.com.japl.ea.utls.DataTableFXMLUtil;
import javafx.beans.property.SimpleObjectProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
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
@FXMLFile(path = "view/cache", file = "cache.fxml")
public class CacheBean extends ABean<CacheDTO> {
	@FXML
	private javafx.scene.control.TableView<CacheDTO> tabla;
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
	private TableColumn<CacheDTO, String> tcState;
	@FXML
	private TableColumn<CacheDTO, String> tcName;
	@FXML
	private GridPane gpRowSelected;
	private DataTableFXMLUtil<CacheDTO, CacheDTO> dt;
	public static final String CONST_TITTLE_NAME = "page.tittle.cached";

	@FXML
	public void initialize() {
		NombreVentana = i18n().valueBundle(CONST_TITTLE_NAME).get();
		lblTittle.setText(NombreVentana);
		registro = new CacheDTO();
		gpRowSelected.setVisible(false);
		tcName.setCellValueFactory(row -> new SimpleObjectProperty<>(
				Optional.ofNullable(row.getValue().getName()).orElse(i18n().valueBundle("err.not.found.value").get())));
		tcState.setCellValueFactory(
				row -> new SimpleObjectProperty<>(Optional.ofNullable(row.getValue().isCached()).orElse(false)
						? i18n().valueBundle("message.value.active").get()
						: i18n().valueBundle("message.value.inactive").get()));
		lazy();
	}

	/**
	 * encargada de crear el objeto que va controlar la tabla
	 */
	public void lazy() {
		dt = new DataTableFXMLUtil<CacheDTO, CacheDTO>(paginator, tabla) {

			@Override
			public List<CacheDTO> getList(CacheDTO filter, Integer page, Integer rows) {
				List<CacheDTO> lista = new ArrayList<>();
				Optional.ofNullable(CacheUtil.INSTANCE().getAll()).orElse(new HashMap<String, Boolean>())
						.forEach((key, value) -> {
							if (key.contains(Optional.ofNullable(filter.getName()).orElse(""))) {
								lista.add(new CacheDTO(key, value));
							}
						});
				return lista.subList(page, rows);
			}

			@Override
			public Integer getTotalRows(CacheDTO filter) {
				return Optional.ofNullable(CacheUtil.INSTANCE().getAll()).orElse(new HashMap<String, Boolean>()).size();
			}

			@Override
			public CacheDTO getFilter() {
				CacheDTO filtro = new CacheDTO();
				if (StringUtils.isNotBlank(tfSearch.getText())) {
					filtro.setName(tfSearch.getText());
				}
				return filtro;
			}
		};
	}

	public void updateCacheSeleted() {
		dt.getSelectedRow().setCache(false);
		CacheUtil.INSTANCE().unload(dt.getSelectedRow().getName());
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

	public DataTableFXMLUtil<CacheDTO, CacheDTO> getDt() {
		return dt;
	}
}
