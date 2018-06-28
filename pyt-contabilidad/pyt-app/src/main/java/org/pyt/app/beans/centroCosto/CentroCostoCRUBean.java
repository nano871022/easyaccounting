package org.pyt.app.beans.centroCosto;

import org.apache.commons.lang3.StringUtils;
import org.pyt.common.annotations.FXMLFile;
import org.pyt.common.annotations.Inject;
import org.pyt.common.common.ABean;
import org.pyt.common.common.SelectList;
import org.pyt.common.constants.ParametroConstants;
import org.pyt.common.exceptions.CentroCostosException;

import com.pyt.service.dto.CentroCostoDTO;
import com.pyt.service.interfaces.ICentroCostosSvc;

import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;

/**
 * Se encarga de procesar la pantalla de creacion y actualizacion de una empresa
 * 
 * @author Alejandro Parra
 * @since 2018-05-22
 */
@FXMLFile(path = "view/centroCosto", file = "centroCosto.fxml")
public class CentroCostoCRUBean extends ABean<CentroCostoDTO> {
	@Inject(resource = "com.pyt.service.implement.CentroCostoSvc")
	private ICentroCostosSvc centroCostoSvc;
	@FXML
	private Label codigo;
	@FXML
	private TextField nombre;
	@FXML
	private TextField descripcion;
	@FXML
	private TextField orden;
	@FXML
	private ChoiceBox<String> estado;
	@FXML
	private Label titulo;
	@FXML
	private BorderPane pane;

	@FXML
	public void initialize() {
		NombreVentana = "Agregando Nuevo Centro de Costos";
		titulo.setText(NombreVentana);
		registro = new CentroCostoDTO();
		SelectList.put(estado, ParametroConstants.mapa_estados_parametros);
		estado.getSelectionModel().selectFirst();
	}

	/**
	 * Pasa los campos de la pantalla el objeto dto
	 */
	private void load() {
		if (registro == null) {
			registro = new CentroCostoDTO();
		}
		registro.setCodigo(codigo.getText());
		registro.setNombre(nombre.getText());
		registro.setDescripcion(descripcion.getText());
		registro.setOrden(Integer.valueOf(orden.getText()));
		registro.setEstado((String) SelectList.get(estado, ParametroConstants.mapa_estados_parametros));
	}

	private void loadFxml() {
		if (registro == null)
			return;
		codigo.setText(registro.getCodigo());
		nombre.setText(registro.getNombre());
		descripcion.setText(registro.getDescripcion());
		orden.setText(String.valueOf(registro.getOrden()));
		SelectList.put(estado, ParametroConstants.mapa_estados_parametros);
		SelectList.selectItem(estado, ParametroConstants.mapa_estados_parametros, registro.getEstado());
	}

	public void load(CentroCostoDTO dto) {
		if (dto != null && dto.getCodigo() != null) {
			registro = dto;
			loadFxml();
			titulo.setText("Modificando Centro de Costo");
		} else {
			error("EL centro de costo es invalido para editar.");
			cancel();
		}
	}

	/**
	 * Se encarga de validar los campos que se encuentren llenos
	 * 
	 * @return {@link Boolean}
	 */
	private Boolean valid() {
		Boolean valid = true;
		valid &= StringUtils.isNotBlank(registro.getNombre());
		valid &= StringUtils.isNotBlank(registro.getDescripcion());
		valid &= StringUtils.isNotBlank(registro.getEstado());
		valid &= (registro.getOrden()) != null;
		return valid;
	}

	public void add() {
		load();
		try {
			if (valid()) {
				if (StringUtils.isNotBlank(registro.getCodigo())) {
					centroCostoSvc.update(registro, userLogin);
					notificar("Se guardo el centro de costo correctamente.");
					cancel();
				} else {
					centroCostoSvc.insert(registro, userLogin);
					codigo.setText(registro.getCodigo());
					notificar("Se agrego el centro de costo correctamente.");
					cancel();
				}
			}
		} catch (CentroCostosException e) {
			error(e);
		}
	}

	public void cancel() {
		getController(CentroCostoBean.class);
	}

}
