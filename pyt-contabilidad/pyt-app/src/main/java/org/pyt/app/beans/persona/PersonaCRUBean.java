package org.pyt.app.beans.persona;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.ea.app.custom.PopupParametrizedControl;
import org.pyt.app.components.PopupGenBean;
import org.pyt.common.abstracts.ABean;
import org.pyt.common.annotations.Inject;
import org.pyt.common.common.SelectList;
import org.pyt.common.constants.ParametroConstants;
import org.pyt.common.exceptions.EmpleadoException;
import org.pyt.common.exceptions.ParametroException;

import com.pyt.service.dto.ParametroDTO;
import com.pyt.service.dto.PersonaDTO;
import com.pyt.service.interfaces.IEmpleadosSvc;
import com.pyt.service.interfaces.IParametrosSvc;

import co.com.arquitectura.annotation.proccessor.FXMLFile;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;

/**
 * Se encarga de procesar la pantalla de creacion y actualizacion de una empresa
 * 
 * @author Alejandro Parra
 * @since 2018-05-22
 */
@FXMLFile(path = "/view", file = "persona/personas.fxml")
public class PersonaCRUBean extends ABean<PersonaDTO> {
	@Inject(resource = "com.pyt.service.implement.ParametrosSvc")
	private IParametrosSvc parametroSvc;
	@Inject(resource = "com.pyt.service.implement.EmpleadosSvc")
	private IEmpleadosSvc empleadosSvc;
	@FXML
	private javafx.scene.control.TextField nombre;
	@FXML
	private javafx.scene.control.TextField apellido;
	@FXML
	private javafx.scene.control.TextField documento;
	@FXML
	private javafx.scene.control.TextField direccion;
	@FXML
	private javafx.scene.control.TextField email;
	@FXML
	private javafx.scene.control.TextField telefono;
	@FXML
	private javafx.scene.control.TextField numeroTarjetaProfesional;
	@FXML
	private javafx.scene.control.ChoiceBox<String> tipoDocumentos;
	@FXML
	private javafx.scene.control.DatePicker fechaNacimiento;
	@FXML
	private Label titulo;
	@FXML
	private BorderPane pane;
	@FXML
	private PopupParametrizedControl profesion;
	private List<ParametroDTO> lTipoDocumentos;
	public final static String FIELD_NAME = "nombre";
	public final static String FIELD_VALOR = "valor";

	@FXML
	public void initialize() {
		NombreVentana = "Agregando Nueva Persona";
		titulo.setText(NombreVentana);
		registro = new PersonaDTO();
		try {
			fechaNacimiento.setOnAction(event -> registro.setFechaNacimiento(
					Date.from(fechaNacimiento.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant())));
			lTipoDocumentos = parametroSvc.getAllParametros(new ParametroDTO(),
					ParametroConstants.GRUPO_TIPOS_DOCUMENTO_PERSONA);
			SelectList.put(tipoDocumentos, lTipoDocumentos, FIELD_NAME);
			profesion.setPopupOpenAction(() -> popupProfesion());
			profesion.setCleanValue(() -> {
				registro.setProfesion(null);
				profesion.setText(null);
				numeroTarjetaProfesional.setText(null);
				registro.setNumeroTarjetaProfesional(null);
				numeroTarjetaProfesional.setDisable(true);
			});
			numeroTarjetaProfesional.setDisable(true);
		} catch (ParametroException e) {
			error(e);
		}
	}

	/**
	 * Pasa los campos de la pantalla el objeto dto
	 */
	private void load() {
		if (registro == null) {
			registro = new PersonaDTO();
		}
		registro.setNombre(nombre.getText());
		registro.setDocumento(documento.getText());
		registro.setApellido(apellido.getText());
		registro.setEmail(email.getText());
		registro.setTelefono(telefono.getText());
		registro.setDireccion(direccion.getText());
		if (fechaNacimiento.getValue() != null)
			registro.setFechaNacimiento(
					Date.from(fechaNacimiento.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant()));
		registro.setTipoDocumento(SelectList.get(tipoDocumentos, lTipoDocumentos, FIELD_NAME).getCodigo());
	}

	private void loadFxml() {
		if (registro == null)
			return;
		documento.setText(registro.getDocumento());
		nombre.setText(registro.getNombre());
		apellido.setText(registro.getNombre());
		direccion.setText(registro.getDireccion());
		email.setText(registro.getEmail());
		telefono.setText(registro.getTelefono());
		if (registro != null && registro.getFechaNacimiento() != null)
			fechaNacimiento
					.setValue(LocalDate.ofInstant(registro.getFechaNacimiento().toInstant(), ZoneId.systemDefault()));
		SelectList.selectItem(tipoDocumentos, lTipoDocumentos, FIELD_NAME, registro.getTipoDocumento(), FIELD_VALOR);
	}

	public void load(PersonaDTO dto) {
		if (dto != null && dto.getCodigo() != null) {
			registro = dto;
			loadFxml();
			titulo.setText("Modificando Trabajador");
		} else {
			error("El persona es invalido para editar.");
			cancel();
		}
	}

	public void popupProfesion() {
		try {
			((PopupGenBean<ParametroDTO>) controllerPopup(new PopupGenBean<ParametroDTO>(ParametroDTO.class))
					.addDefaultValuesToGenericParametrized("estado", "A")
					.addDefaultValuesToGenericParametrized(ParametroConstants.FIELD_NAME_GROUP,
							parametroSvc.getIdByParametroGroup(ParametroConstants.GRUPO_PROFESIONES)))
									.load("#{PersonaCRUBean.profesion}");
		} catch (Exception e) {
			error(e);
		}
	}

	public void setProfesion(ParametroDTO profesion) {
		registro.setProfesion(profesion);
		this.profesion.setText(profesion.getNombre());
		numeroTarjetaProfesional.setDisable(false);
	}

	public void add() {
		load();
		try {
			if (StringUtils.isNotBlank(registro.getCodigo())) {
				empleadosSvc.update(registro, userLogin);
				notificar("Se guardo la persona correctamente.");
				cancel();
			} else {
				empleadosSvc.insert(registro, userLogin);
				notificar("Se agrego la persona correctamente.");
				cancel();
			}
		} catch (EmpleadoException e) {
			error(e);
		}
	}

	public void cancel() {
		getController(PersonaBean.class);
	}

}
