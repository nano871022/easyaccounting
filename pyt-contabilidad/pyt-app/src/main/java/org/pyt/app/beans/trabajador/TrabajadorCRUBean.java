package org.pyt.app.beans.trabajador;

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
import org.pyt.common.exceptions.GenericServiceException;
import org.pyt.common.exceptions.ParametroException;

import com.pyt.service.dto.CentroCostoDTO;
import com.pyt.service.dto.ParametroDTO;
import com.pyt.service.dto.PersonaDTO;
import com.pyt.service.dto.TrabajadorDTO;
import com.pyt.service.interfaces.ICentroCostosSvc;
import com.pyt.service.interfaces.IEmpleadosSvc;
import com.pyt.service.interfaces.IGenericServiceSvc;
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
@FXMLFile(path = "/view", file = "persona/trabajador.fxml")
public class TrabajadorCRUBean extends ABean<TrabajadorDTO> {
	@Inject(resource = "com.pyt.service.implement.ParametrosSvc")
	private IParametrosSvc parametroSvc;
	@Inject(resource = "com.pyt.service.implement.CentroCostoSvc")
	private ICentroCostosSvc centroCostoSvc;
	@Inject(resource = "com.pyt.service.implement.EmpleadosSvc")
	private IEmpleadosSvc empleadosSvc;
	@Inject(resource = "com.pyt.service.implement.GenericServiceSvc")
	private IGenericServiceSvc<PersonaDTO> personaSvc;
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
	private javafx.scene.control.ChoiceBox<String> tipoDocumentos;
	@FXML
	private PopupParametrizedControl tipoPagos;
	@FXML
	private PopupParametrizedControl centroCostos;
	@FXML
	private javafx.scene.control.ChoiceBox<String> estados;
	@FXML
	private javafx.scene.control.DatePicker fechaNacimiento;
	@FXML
	private javafx.scene.control.DatePicker fechaIngreso;
	@FXML
	private javafx.scene.control.DatePicker fechaRetiro;
	@FXML
	private Label titulo;
	@FXML
	private BorderPane pane;
	private List<ParametroDTO> lTipoDocumentos;
	private List<ParametroDTO> lEstados;
	public final static String FIELD_NAME = "nombre";
	public final static String FIELD_VALOR = "valor";

	@FXML
	public void initialize() {
		NombreVentana = "Agregando Nuevo Trabajador";
		titulo.setText(NombreVentana);
		registro = new TrabajadorDTO();
		registro.setPersona(new PersonaDTO());
		try {
			fechaNacimiento.setOnAction(event -> registro.getPersona().setFechaNacimiento(
					Date.from(fechaNacimiento.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant())));
			fechaIngreso.setOnAction(event -> registro.setFechaIngreso(
					Date.from(fechaIngreso.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant())));
			fechaRetiro.setOnAction(event -> registro.setFechaRetiro(
					Date.from(fechaRetiro.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant())));
			lEstados = parametroSvc.getAllParametros(new ParametroDTO(), ParametroConstants.GRUPO_ESTADO_EMPLEADO);
			lTipoDocumentos = parametroSvc.getAllParametros(new ParametroDTO(),
					ParametroConstants.GRUPO_TIPOS_DOCUMENTO_PERSONA);
			SelectList.put(tipoDocumentos, lTipoDocumentos, FIELD_NAME);
			SelectList.put(estados, lEstados, FIELD_NAME);
			tipoPagos.setPopupOpenAction(() -> popupOpenTipoPago());
			tipoPagos.setCleanValue(() -> {
				registro.setTipoPago(null);
				tipoPagos.setText(null);
			});
			centroCostos.setPopupOpenAction(() -> popupOpenCentroCostos());
			centroCostos.setCleanValue(() -> {
				registro.setCentroCosto(null);
				centroCostos.setText(null);
			});
		} catch (ParametroException e) {
			error(e);
		}
	}

	/**
	 * Pasa los campos de la pantalla el objeto dto
	 */
	private void load() {
		if (registro == null) {
			registro = new TrabajadorDTO();
			registro.setPersona(new PersonaDTO());
		}
		registro.getPersona().setNombre(nombre.getText());
		registro.getPersona().setDocumento(documento.getText());
		registro.getPersona().setApellido(apellido.getText());
		registro.setCorreo(email.getText());
		registro.getPersona().setTelefono(telefono.getText());
		registro.getPersona().setDireccion(direccion.getText());
		if (fechaNacimiento.getValue() != null)
			registro.getPersona().setFechaNacimiento(
					Date.from(fechaNacimiento.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant()));
		if (fechaIngreso.getValue() != null)
			registro.setFechaIngreso(
					Date.from(fechaIngreso.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant()));
		if (fechaRetiro.getValue() != null)
			registro.setFechaRetiro(Date.from(fechaRetiro.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant()));
		registro.getPersona().setTipoDocumento(SelectList.get(tipoDocumentos, lTipoDocumentos, FIELD_NAME));
		var estado = SelectList.get(estados, lEstados, FIELD_NAME);
		registro.setEstado(estado);
	}

	public final void popupOpenTipoPago() {
		try {
			((PopupGenBean<ParametroDTO>) controllerPopup(new PopupGenBean<ParametroDTO>(ParametroDTO.class)
					.addDefaultValuesToGenericParametrized(ParametroConstants.FIELD_NAME_GROUP,
							parametroSvc.getIdByParametroGroup(ParametroConstants.GRUPO_TIPO_PAGO)))
									.addDefaultValuesToGenericParametrized(ParametroConstants.FIELD_NAME_STATE,
											ParametroConstants.COD_ESTADO_PARAMETRO_ACTIVO))
													.load("#{TrabajadorCRUBean.tipoPago}");
		} catch (Exception e) {
			error(e);
		}
	}

	public final void setTipoPago(ParametroDTO parametro) {
		registro.setTipoPago(parametro);
		tipoPagos.setText(parametro.getDescripcion());
	}

	public final void popupOpenCentroCostos() {
		try {
			controllerPopup(new PopupGenBean<CentroCostoDTO>(CentroCostoDTO.class))
					.load("#{TrabajadorCRUBean.centroCosto}");
		} catch (Exception e) {
			error(e);
		}
	}

	public final void setCentroCosto(CentroCostoDTO centroCosto) {
		registro.setCentroCosto(centroCosto);
		centroCostos.setText(centroCosto.getDescripcion());
	}

	private void loadFxml() {
		if (registro == null)
			return;
		documento.setText(registro.getPersona().getDocumento());
		nombre.setText(registro.getPersona().getNombre());
		apellido.setText(registro.getPersona().getNombre());
		direccion.setText(registro.getPersona().getDireccion());
		email.setText(registro.getCorreo());
		telefono.setText(registro.getPersona().getTelefono());
		if (registro.getPersona() != null && registro.getPersona().getFechaNacimiento() != null)
			fechaNacimiento.setValue(LocalDate.ofInstant(registro.getPersona().getFechaNacimiento().toInstant(),
					ZoneId.systemDefault()));
		if (registro.getFechaIngreso() != null)
			fechaIngreso.setValue(LocalDate.ofInstant(registro.getFechaIngreso().toInstant(), ZoneId.systemDefault()));
		if (registro.getFechaRetiro() != null)
			fechaRetiro.setValue(LocalDate.ofInstant(registro.getFechaRetiro().toInstant(), ZoneId.systemDefault()));
		SelectList.selectItem(tipoDocumentos, lTipoDocumentos, FIELD_NAME, registro.getPersona().getTipoDocumento(),
				FIELD_NAME);
		SelectList.selectItem(estados, lEstados, FIELD_NAME, registro.getEstado(), FIELD_NAME);
		tipoPagos.setText(registro.getTipoPago().getDescripcion());
		centroCostos.setText(registro.getCentroCosto().getDescripcion());
	}

	public void load(TrabajadorDTO dto) {
		if (dto != null && dto.getCodigo() != null) {
			registro = dto;
			loadFxml();
			titulo.setText("Modificando Trabajador");
		} else {
			error("El trabajador es invalido para editar.");
			cancel();
		}
	}

	public void add() {
		load();
		try {
			if (StringUtils.isNotBlank(registro.getCodigo())) {
				personaSvc.update(registro.getPersona(), userLogin);
				empleadosSvc.update(registro, userLogin);
				notificar("Se guardo la empresa correctamente.");
				cancel();
			} else {
				registro.getPersona().setEmail(registro.getCorreo());
				personaSvc.insert(registro.getPersona(), userLogin);
				empleadosSvc.insert(registro, userLogin);
				notificar("Se agrego la empresa correctamente.");
				cancel();
			}
		} catch (EmpleadoException | GenericServiceException e) {
			error(e);
		}
	}

	public void cancel() {
		getController(TrabajadorBean.class);
	}

}
