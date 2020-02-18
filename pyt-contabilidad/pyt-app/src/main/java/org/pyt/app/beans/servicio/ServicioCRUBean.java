package org.pyt.app.beans.servicio;

import org.apache.commons.lang3.StringUtils;
import org.pyt.common.annotations.Inject;
import org.pyt.common.exceptions.ServiciosException;
import org.pyt.common.exceptions.validates.ValidateValueException;
import org.pyt.common.validates.ValidFields;
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
		NombreVentana = i18n().get("fxml.title.add.new.service");
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
			titulo.setText(i18n().get("mensaje.modifig.service"));
		} else {
			error(i18n().get("err.service.cant.edit"));
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
		valid &= ValidFields.valid(registro.getNombre(), nombre, true, 1, 100,
				i18n().valueBundle("err.service.field.name.empty"));
		valid &= ValidFields.valid(registro.getDescripcion(), descripcion, true, 1, 100,
				i18n().valueBundle("err.service.field.description.empty"));
		valid &= ValidFields.valid(registro.getValorManoObra(), valorManoObra, true, null, null,
				i18n().valueBundle("err.service.field.value.empty"));
		return valid;
	}

	public void add() {
		load();
		try {
			if (valid()) {
				if (StringUtils.isNotBlank(registro.getCodigo())) {
					servicioSvc.update(registro, getUsuario());
					notificarI18n("mensaje.service.have.been.updated.succefull");
					cancel();
				} else {
					servicioSvc.insert(registro, getUsuario());
					codigo.setText(registro.getCodigo());
					notificarI18n("mensaje.service.have.been.inserted.succefull");
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
