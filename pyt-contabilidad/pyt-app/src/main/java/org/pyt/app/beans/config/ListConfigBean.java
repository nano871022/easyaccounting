package org.pyt.app.beans.config;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.pyt.common.annotations.Inject;
import org.pyt.common.exceptions.LoadAppFxmlException;
import org.pyt.common.exceptions.MarcadorServicioException;

import com.pyt.service.dto.ConfiguracionDTO;
import com.pyt.service.interfaces.IConfigMarcadorServicio;

import co.com.arquitectura.annotation.proccessor.FXMLFile;
import co.com.japl.ea.beans.abstracts.ABean;
import co.com.japl.ea.utls.DataTableFXMLUtil;
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
	private TextField descripcion;
	@FXML
	private Button btnMod;
	@FXML
	private Button btnDel;
	@FXML
	private Button btnCargue;
	@FXML
	private Button btnReporte;
	@FXML
	private HBox paginador;
	private DataTableFXMLUtil<ConfiguracionDTO, ConfiguracionDTO> dt;

	@FXML
	public void initialize() {
		NombreVentana = "Lista de Configutaciones";
		registro = new ConfiguracionDTO();
		btnCargue.setVisible(false);
		btnReporte.setVisible(false);
		lazy();
	}

	/**
	 * encargada de crear el objeto que va controlar la tabla
	 */
	public void lazy() {
		dt = new DataTableFXMLUtil<ConfiguracionDTO, ConfiguracionDTO>(paginador, tabla) {
			@Override
			public List<ConfiguracionDTO> getList(ConfiguracionDTO filter, Integer page, Integer rows) {
				List<ConfiguracionDTO> lista = new ArrayList<ConfiguracionDTO>();
				try {
					lista = configMarcadorServicioSvc.getConfiguracion(filter, page - 1, rows);
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
					filtro.setConfiguracion("%" + configuracion.getText() + "%");
				}
				if (StringUtils.isNotBlank(descripcion.getText())) {
					filtro.setDescripcion("%" + descripcion.getText() + "%");
				}
				return filtro;
			}
		};
	}

	public void clickTable() {
		btnMod.setVisible(isSelected());
		btnDel.setVisible(isSelected());
		if (isSelected()) {
			if (dt.getSelectedRow().getReport()) {
				btnReporte.setVisible(true);
				btnCargue.setVisible(false);
			} else {
				btnCargue.setVisible(true);
				btnReporte.setVisible(false);
			}

		}
	}

	public void add() {
		getController(ConfigServiceBean.class).load();
	}

	public void search() {
		dt.search();
	}

	public final void limpiar() {
		descripcion.setText("");
		configuracion.setText("");
	}

	public void del() {
		try {
			registro = dt.getSelectedRow();
			if (registro != null) {
				configMarcadorServicioSvc.deleteConfiguracion(registro, getUsuario());
				notificar("Se ha eliminado la configuración.");
				dt.search();
			} else {
				notificar("No se ha seleccionado una registro.");
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

	public DataTableFXMLUtil<ConfiguracionDTO, ConfiguracionDTO> getDt() {
		return dt;
	}

	public void reporte() {
		try {
			controllerPopup(GenConfigBean.class).load(dt.getSelectedRow().getConfiguracion());
		} catch (LoadAppFxmlException e) {
			error(e);
		}
	}

	public void cargue() {
		try {
			controllerPopup(LoaderServiceBean.class).load(dt.getSelectedRow().getConfiguracion());
		} catch (LoadAppFxmlException e) {
			error(e);
		}

	}
}
