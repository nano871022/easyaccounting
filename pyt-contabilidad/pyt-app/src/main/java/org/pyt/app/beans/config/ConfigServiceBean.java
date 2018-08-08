package org.pyt.app.beans.config;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.pyt.common.annotations.FXMLFile;
import org.pyt.common.common.ABean;
import org.pyt.common.common.SelectList;

import com.pyt.service.dto.AsociacionArchivoDTO;
import com.pyt.service.dto.MarcadorServicioDTO;
/**
 * Se encarga de controlar las asociacionesentra marcadores y servicios 
 * @author Alejandro Parra
 * @since 05-08-2018
 */
import com.pyt.service.dto.ServicioCampoBusquedaDTO;

import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Tab;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

@FXMLFile(path = "view/config/servicios", file = "ConfigService.fxml")
public class ConfigServiceBean extends ABean<AsociacionArchivoDTO> {
	@FXML
	private Tab tabServicios;
	@FXML
	private Tab tabMarcadores;
	@FXML
	private Tab tabAsociaciones;
	@FXML
	private Tab tabProbar;
	@FXML
	private TextField nameMarcador;
	@FXML
	private TextField nameFiler;
	@FXML
	private TableView<String> lstMarcadores;
	@FXML
	private TableView<ServicioCampoBusquedaDTO> lstServicioCampo;
	@FXML
	private ChoiceBox<String> lstServicios;
	@FXML
	private ChoiceBox<String> servicio;
	@FXML
	private ChoiceBox<String> campo;
	@FXML
	private ChoiceBox<String> marcador;
	@FXML
	private ChoiceBox<String> lstCampos;
	private List<String> marcadores;
	private List<String> servicios;
	private List<String> campos;
	private List<ServicioCampoBusquedaDTO> serviciosCampoBusqueda;
	private List<MarcadorServicioDTO> marcadoresServicios;
	private Integer posicion;
	private Integer max;
	public final static Integer TAB_MARCADOR = 1;
	public final static Integer TAB_SERVICIO_CAMPO = 2;
	public final static Integer TAB_ASOCIAR_MARCADOR = 3;
	public final static Integer TAB_PROBAR = 4;

	@FXML
	public void initialize() {
		marcadores = new ArrayList<String>();
		servicios = new ArrayList<String>();
		campos = new ArrayList<String>();
		serviciosCampoBusqueda = new ArrayList<ServicioCampoBusquedaDTO>();
		marcadoresServicios = new ArrayList<MarcadorServicioDTO>();
		posicion = TAB_MARCADOR;
		max = TAB_PROBAR;
	}

	private void tabView() {
		tabMarcadores.setDisable(posicion.compareTo(TAB_MARCADOR) == 0);
		tabServicios.setDisable(posicion.compareTo(TAB_SERVICIO_CAMPO) == 0);
		tabAsociaciones.setDisable(posicion.compareTo(TAB_ASOCIAR_MARCADOR) == 0);
		tabProbar.setDisable(posicion.compareTo(TAB_PROBAR) == 0);
	}

	private void addMarcador() {
		if (posicion.compareTo(TAB_MARCADOR) != 0)
			return;
		String marcador = nameMarcador.getText();
		if (StringUtils.isNotBlank(marcador)) {
			marcadores.add(marcador);
		}
	}

	private void delMarcador() {
		if (posicion.compareTo(TAB_MARCADOR) != 0)
			return;
		String marcador = lstMarcadores.getSelectionModel().getSelectedItem();
		if (StringUtils.isNotBlank(marcador)) {
			marcadores.remove(marcador);
		}
	}

	private void addServicioCampo() {
		if (posicion.compareTo(TAB_SERVICIO_CAMPO) != 0)
			return;

		String service = SelectList.get(servicio);
		String field = SelectList.get(campo);
		ServicioCampoBusquedaDTO serviceField = new ServicioCampoBusquedaDTO();
		serviceField.setCampo(field);
		serviceField.setServicio(service);
		serviciosCampoBusqueda.add(serviceField);
	}

	private void delServicioCampo() {
		if (posicion.compareTo(TAB_SERVICIO_CAMPO) != 0)
			return;
		String service = SelectList.get(servicio);

	}

	public void agregar() {

	}

	public void guardar() {

	}

	public void eliminar() {

	}

	public void anterior() {
		if (posicion > TAB_MARCADOR) {
			posicion--;
		}
		tabView();
	}

	public void siguiente() {
		if (posicion < max) {
			posicion++;
		}
		tabView();
	}

	public void cancelar() {
		posicion = TAB_MARCADOR;
		marcadores.clear();
		servicios.clear();
		campos.clear();
		serviciosCampoBusqueda.clear();
		marcadoresServicios.clear();
		tabView();
	}
}
