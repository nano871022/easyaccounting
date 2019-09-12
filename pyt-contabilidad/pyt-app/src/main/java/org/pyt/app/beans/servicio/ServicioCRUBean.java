package org.pyt.app.beans.servicio;

import org.apache.commons.lang3.StringUtils;
import org.pyt.common.annotations.Inject;
import org.pyt.common.exceptions.ServiciosException;
import org.pyt.common.exceptions.validates.ValidateValueException;
import org.pyt.common.validates.ValidateValues;

import com.pyt.service.dto.ServicioDTO;
import com.pyt.service.interfaces.IServiciosSvc;

import co.com.arquitectura.annotation.proccessor.FXMLFile;
import co.com.japl.ea.beans.abstracts.ABean;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;

/**
 * Se encarga de procesar la pantalla de creacion y actualizacion de una empresa
 * 
 * @author Alejandro Parra
 * @since 2018-05-22
 */
@FXMLFile(path = "view/servicio", file = "servicio.fxml")
public class ServicioCRUBean extends ABean<ServicioDTO> {
	@Inject(resource = "com.pyt.service.implement.ServiciosSvc")
	private IServiciosSvc servicioSvc;
	@FXML
	private Label codigo;
	@FXML
	private TextField nombre;
	@FXML
	private TextField descripcion;
	@FXML
	private TextField valorManoObra;
	@FXML
	private Label titulo;
	@FXML
	private BorderPane pane;
	private ValidateValues vv;

	@FXML
	public void initialize() {
		NombreVentana = "Agregando Nuevo Servicio";
		titulo.setText(NombreVentana);
		registro = new ServicioDTO();
		vv = new ValidateValues();
	}

	/**
	 * Pasa los campos de la pantalla el objeto dto
	 */
	private void load() {
		if (registro == null) {
			registro = new ServicioDTO();
		}
		registro.setCodigo(codigo.getText());
		registro.setNombre(nombre.getText());
		registro.setDescripcion(descripcion.getText());
		try {
			registro.setValorManoObra(vv.cast(valorManoObra.getText(), Long.class));
		} catch (ValidateValueException e) {
			error(e);
		}
	}

	private void loadFxml() {
		if (registro == null)
			return;
		codigo.setText(registro.getCodigo());
		nombre.setText(registro.getNombre());
		descripcion.setText(registro.getDescripcion());
		if (registro.getValorManoObra() != null) {
			valorManoObra.setText(String.valueOf(registro.getValorManoObra()));
		}
	}

	public void load(ServicioDTO dto) {
		if (dto != null && dto.getCodigo() != null) {
			registro = dto;
			loadFxml();
			titulo.setText("Modificando Servicio");
		} else {
			error("EL servicio es invalido para editar.");
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
		valid &= registro.getValorManoObra() != null;
		return valid;
	}

	public void add() {
		load();
		try {
			if (valid()) {
				if (StringUtils.isNotBlank(registro.getCodigo())) {
					servicioSvc.update(registro, userLogin);
					notificar("Se guardo el servicio correctamente.");
					cancel();
				} else {
					servicioSvc.insert(registro, userLogin);
					codigo.setText(registro.getCodigo());
					notificar("Se agrego el servicio correctamente.");
					cancel();
				}
			}
		} catch (ServiciosException e) {
			error(e);
		}
	}

	public void cancel() {
		getController(ServicioBean.class);
	}

}
