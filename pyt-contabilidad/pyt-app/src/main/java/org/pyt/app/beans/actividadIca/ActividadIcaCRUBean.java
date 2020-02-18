package org.pyt.app.beans.actividadIca;

import org.apache.commons.lang3.StringUtils;
import org.pyt.common.annotations.Inject;
import org.pyt.common.exceptions.ActividadIcaException;
import org.pyt.common.validates.ValidFields;

import com.pyt.service.dto.ActividadIcaDTO;
import com.pyt.service.interfaces.IActividadIcaSvc;

import co.com.arquitectura.annotation.proccessor.FXMLFile;
import co.com.japl.ea.beans.abstracts.ABean;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;

/**
 * Se encarga de procesar la pantalla de creacion y actualizacion de una
 * actividad ica
 * 
 * @author Alejandro Parra
 * @since 2018-05-22
 */
@FXMLFile(path = "view/actividadIca", file = "actividadIca.fxml")
public class ActividadIcaCRUBean extends ABean<ActividadIcaDTO> {
	@Inject(resource = "com.pyt.service.implement.ActividadIcaSvc")
	private IActividadIcaSvc actividadIcaSvc;
	@FXML
	private Label codigo;
	@FXML
	private TextField nombre;
	@FXML
	private TextField codigoIca;
	@FXML
	private TextField descripcion;
	@FXML
	private TextField tarifa;
	@FXML
	private TextField base;
	@FXML
	private Label titulo;
	@FXML
	private BorderPane pane;

	@FXML
	public void initialize() {
		NombreVentana = i18n().valueBundle("fxml.title.add.ica.activity").get();
		titulo.setText(NombreVentana);
		registro = new ActividadIcaDTO();
	}

	/**
	 * Pasa los campos de la pantalla el objeto dto
	 */
	private void load() {
		if (registro == null) {
			registro = new ActividadIcaDTO();
		}
		registro.setCodigo(codigo.getText());
		registro.setCodigoIca(codigoIca.getText());
		registro.setNombre(nombre.getText());
		registro.setDescripcion(descripcion.getText());
		registro.setBase(base.getText());
		registro.setTarifa(tarifa.getText());
	}

	private void loadFxml() {
		if (registro == null)
			return;
		codigo.setText(registro.getCodigo());
		codigoIca.setText(registro.getCodigoIca());
		nombre.setText(registro.getNombre());
		descripcion.setText(registro.getDescripcion());
		base.setText(registro.getBase());
		tarifa.setText(registro.getTarifa());
	}

	public void load(ActividadIcaDTO dto) {
		if (dto != null && dto.getCodigo() != null) {
			registro = dto;
			loadFxml();
			titulo.setText("Modificando Actividad ICa");
		} else {
			error("La actividad Ica es invalida para editar.");
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
		valid &= ValidFields.validI18n(registro.getNombre(), nombre, true, 1, 100,
				"err.valid.actividadica.field.name.empty");
		valid &= ValidFields.validI18n(registro.getDescripcion(), descripcion, true, 1, 100,
				"err.valid.actividadica.field.description.empty");
		valid &= ValidFields.validI18n(registro.getBase(), base, true, 1, 30,
				"err.valid.actividadica.field.base.empty");
		valid &= ValidFields.validI18n(registro.getTarifa(), tarifa, true, 1, 30,
				"err.valid.actividadica.field.rate.empty");
		valid &= ValidFields.validI18n(registro.getCodigoIca(), codigoIca, true, 1, 30,
				"err.valid.actividadica.field.codeica.empty");
		return valid;
	}

	public void add() {
		load();
		try {
			if (valid()) {
				if (StringUtils.isNotBlank(registro.getCodigo())) {
					actividadIcaSvc.update(registro, getUsuario());
					notificarI18n("mensaje.icaactivity,have.been.insert.succesfill");
					cancel();
				} else {
					actividadIcaSvc.insert(registro, getUsuario());
					codigo.setText(registro.getCodigo());
					notificarI18n("menaje.icaactivity.have.been.update.succesfull");
					cancel();
				}
			}
		} catch (ActividadIcaException e) {
			error(e);
		}
	}

	public void cancel() {
		getController(ActividadIcaBean.class);
	}

}
