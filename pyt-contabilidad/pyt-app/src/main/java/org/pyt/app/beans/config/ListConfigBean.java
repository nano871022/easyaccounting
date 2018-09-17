package org.pyt.app.beans.config;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.pyt.app.components.DataTableFXML;
import org.pyt.common.annotations.FXMLFile;
import org.pyt.common.annotations.Inject;
import org.pyt.common.common.ABean;
import org.pyt.common.exceptions.MarcadorServicioException;

import com.pyt.service.dto.ConfiguracionDTO;
import com.pyt.service.interfaces.IConfigMarcadorServicio;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;

/**
 * Bean encargado de cargar la lista de las configuraciones
 * 
 * @author alejandro parra
 * @since 16/09/2018
 */
@FXMLFile(path = "view/config/servicios", file = "listConfigService.fxml")
public class ListConfigBean extends ABean<ConfiguracionDTO> {
	@Inject(resource = "com.pyt.service.implement.ConfigMarcadorServicioSvc")
	private IConfigMarcadorServicio configMarcadorServicioSvc;
	@FXML
	private TableView<ConfiguracionDTO> tabla;
	@FXML
	private TextField configuracion;
	@FXML
	private Button btnMod;
	@FXML
	private Button btnDel;
	@FXML
	private HBox paginador;
	private DataTableFXML<ConfiguracionDTO, ConfiguracionDTO> dt;

	@FXML
	public void initialize() {
		NombreVentana = "Lista de Configutaciones";
		registro = new ConfiguracionDTO();
		lazy();
	}

	/**
	 * encargada de crear el objeto que va controlar la tabla
	 */
	public void lazy() {
		dt = new DataTableFXML<ConfiguracionDTO, ConfiguracionDTO>(paginador, tabla) {
			@Override
			public List<ConfiguracionDTO> getList(ConfiguracionDTO filter, Integer page, Integer rows) {
				List<ConfiguracionDTO> lista = new ArrayList<ConfiguracionDTO>();
				try {
					lista = configMarcadorServicioSvc.getConfiguracion(filter, page-1, rows);
				} catch (MarcadorServicioException e) {
					error(e);
				}
				return lista;
			}

			@Override
			public Integer getTotalRows(ConfiguracionDTO filter) {
				Integer count = 0;
				try {
					count = configMarcadorServicioSvc.count(filter);  
				} catch (MarcadorServicioException e) {
					error(e);
				}
				return count;
			}

			@Override
			public ConfiguracionDTO getFilter() {
				ConfiguracionDTO filtro = new ConfiguracionDTO();
				if (StringUtils.isNotBlank(configuracion.getText())) {
					filtro.setConfiguracion(configuracion.getText());
				}
				return filtro;
			}
		};
	}

	public void clickTable() {
		btnMod.setVisible(isSelected());
		btnDel.setVisible(isSelected());
	}

	public void add() {
		getController(ConfigServiceBean.class);
	}

	public void search() {
		dt.search();
	}

	public void del() {
		try {
			registro = dt.getSelectedRow();
			if (registro != null) {
				configMarcadorServicioSvc.deleteConfiguracion(registro, userLogin);
				notificar("Se ha eliminado la configuración.");
				dt.search();
			} else {
				notificar("No se ha seleccionado una cuenta contable.");
			}
		} catch (MarcadorServicioException e) {
			error(e);
		}
	}

	public void set() {
		registro = dt.getSelectedRow();
		if (registro != null) {
			getController(ConfigServiceBean.class).load(registro);
		} else {
			notificar("No se ha seleccionado una configutación.");
		}
	}

	public Boolean isSelected() {
		return dt.isSelected();
	}

	public DataTableFXML<ConfiguracionDTO, ConfiguracionDTO> getDt() {
		return dt;
	}
}
