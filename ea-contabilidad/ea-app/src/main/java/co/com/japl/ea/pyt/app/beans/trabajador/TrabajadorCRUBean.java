package co.com.japl.ea.pyt.app.beans.trabajador;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.controlsfx.glyphfont.FontAwesome.Glyph;
import org.pyt.common.annotations.Inject;
import org.pyt.common.common.DtoUtils;
import org.pyt.common.common.ListUtils;
import org.pyt.common.common.SelectList;
import org.pyt.common.constants.ParametroConstants;
import org.pyt.common.constants.PermissionConstants;

import co.com.arquitectura.annotation.proccessor.FXMLFile;
import co.com.japl.ea.app.custom.PopupParametrizedControl;
import co.com.japl.ea.beans.abstracts.ABean;
import co.com.japl.ea.common.button.apifluid.ButtonsImpl;
import co.com.japl.ea.common.validates.ValidFields;
import co.com.japl.ea.dto.dto.CentroCostoDTO;
import co.com.japl.ea.dto.dto.ParametroDTO;
import co.com.japl.ea.dto.dto.PersonaDTO;
import co.com.japl.ea.dto.dto.TrabajadorDTO;
import co.com.japl.ea.dto.interfaces.ICentroCostosSvc;
import co.com.japl.ea.dto.interfaces.IEmpleadosSvc;
import co.com.japl.ea.dto.interfaces.IGenericServiceSvc;
import co.com.japl.ea.dto.interfaces.IParametrosSvc;
import co.com.japl.ea.exceptions.EmpleadoException;
import co.com.japl.ea.exceptions.GenericServiceException;
import co.com.japl.ea.exceptions.ParametroException;
import co.com.japl.ea.utls.PermissionUtil;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;

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
	private HBox buttons;

	@FXML
	public void initialize() {
		NombreVentana = i18n().get("fxml.title.add.new.employe");
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
			documento.textProperty().addListener(change -> validDocument());
			centroCostos.setPopupOpenAction(() -> popupOpenCentroCostos());
			centroCostos.setCleanValue(() -> {
				registro.setCentroCosto(null);
				centroCostos.setText(null);
			});
			visibleButtons();
			ButtonsImpl.Stream(HBox.class).setLayout(buttons).setName("fxml.btn.save").action(this::add)
					.icon(Glyph.SAVE).isVisible(save).setName("fxml.btn.edit").action(this::add).icon(Glyph.EDIT)
					.isVisible(edit).setName("fxml.btn.cancel").action(this::cancel).build();
		} catch (ParametroException e) {
			error(e);
		}
	}

	private void validDocument() {
		try {
			var persona = new PersonaDTO();
			persona.setDocumento(documento.getText());
			var list = empleadosSvc.getAllPersonas(persona);
			if (ListUtils.haveOneItem(list)) {
				registro.setPersona(list.get(0));
				var trabajador = new TrabajadorDTO();
				trabajador.setPersona(registro.getPersona());
				var listWorkers = empleadosSvc.getAllTrabajadores(trabajador);
				if (ListUtils.isNotBlank(listWorkers)) {
					registro = listWorkers.get(0);
					titulo.setText(i18n().get("mensaje.modifing.employe"));
				}
				loadFxml();
			} else if (ListUtils.haveMoreOneItem(list)) {
				alerta(i18n().valueBundle("err.worker.field.taxid.duplicate", documento.getText()));
			}
		} catch (EmpleadoException e) {
			logger().logger(e);
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

	private boolean valid() {
		var valid = true;
		valid &= ValidFields.valid(registro.getPersona().getNombre(), nombre, true, 1, 30,
				i18n().valueBundle("err.valid.worker.field.name.empty"));
		valid &= ValidFields.valid(registro.getPersona().getApellido(), apellido, true, 1, 30,
				i18n().valueBundle("err.valid.worker.field.lastname.empty"));
		valid &= ValidFields.valid(registro.getPersona().getDocumento(), documento, true, 1, 12,
				i18n().valueBundle("err.valid.worker.field.id.empty"));
		valid &= ValidFields.valid(registro.getCorreo(), email, true, 1, 100,
				i18n().valueBundle("err.valid.worker.field.email.empty"));
		valid &= ValidFields.valid(registro.getPersona().getTelefono(), telefono, true, 1, 15,
				i18n().valueBundle("err.valid.worker.field.telephone.empty"));
		valid &= ValidFields.valid(registro.getPersona().getDireccion(), direccion, true, 1, 30,
				i18n().valueBundle("err.valid.worker.field.address.empty"));
		valid &= ValidFields.valid(registro.getPersona().getFechaNacimiento(), fechaNacimiento, true, null, null,
				i18n().valueBundle("err.valid.worker.field.birthday.empty"));
		valid &= ValidFields.valid(registro.getFechaIngreso(), fechaIngreso, true, null, null,
				i18n().valueBundle("err.valid.worker.field.entrydate.empty"));
		valid &= ValidFields.valid(registro.getPersona().getTipoDocumento(), tipoDocumentos, true, null, null,
				i18n().valueBundle("err.valid.worker.field.idtype.empty"));
		valid &= ValidFields.valid(registro.getEstado(), estados, true, null, null,
				i18n().valueBundle("err.valid.worker.field.state.empty"));
		return valid;
	}

	public final void popupOpenTipoPago() {
		try {
			controllerGenPopup(ParametroDTO.class).setWidth(300)
					.addDefaultValuesToGenericParametrized(ParametroConstants.FIELD_NAME_GROUP,
							ParametroConstants.GRUPO_TIPO_PAGO)
					.addDefaultValuesToGenericParametrized(ParametroConstants.FIELD_NAME_STATE,
							ParametroConstants.COD_ESTADO_PARAMETRO_ACTIVO)
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
			controllerGenPopup(CentroCostoDTO.class).load("#{TrabajadorCRUBean.centroCosto}");
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
		if (DtoUtils.haveCode(dto)) {
			registro = dto;
			loadFxml();
			visibleButtons();
			titulo.setText(i18n().get("mensaje.modifing.employe"));
		} else {
			error(i18n().get("err.employe.cant.edit"));
			cancel();
		}
	}

	public void add() {
		load();
		try {
			if (valid()) {
				if (StringUtils.isNotBlank(registro.getCodigo())) {
					personaSvc.update(registro.getPersona(), getUsuario());
					empleadosSvc.update(registro, getUsuario());
					notificarI18n("mensaje.employe.have.been.updated.succesfull");
					cancel();
				} else {
					if (!DtoUtils.haveCode(registro.getPersona())) {
						registro.getPersona().setEmail(registro.getCorreo());
						personaSvc.insert(registro.getPersona(), getUsuario());
					}
					empleadosSvc.insert(registro, getUsuario());
					notificarI18n("mensaje.employe.have.been.inserted.succesfull");
					cancel();
				}
			}
		} catch (EmpleadoException | GenericServiceException e) {
			error(e);
		}
	}

	public void cancel() {
		getController(TrabajadorBean.class);
	}

	@Override
	protected void visibleButtons() {
		var save = PermissionUtil.INSTANCE().havePerm(PermissionConstants.CONST_PERM_CREATE, TrabajadorBean.class,
				getUsuario().getGrupoUser());
		var edit = PermissionUtil.INSTANCE().havePerm(PermissionConstants.CONST_PERM_UPDATE, TrabajadorBean.class,
				getUsuario().getGrupoUser());
		this.save.setValue(save);
		this.edit.setValue(edit);
	}

}
