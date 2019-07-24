package org.pyt.app.beans.empresa;

import org.apache.commons.lang3.StringUtils;
import org.ea.app.custom.PopupParametrizedControl;
import org.pyt.app.components.PopupGenBean;
import org.pyt.common.abstracts.ABean;
import org.pyt.common.annotations.Inject;
import org.pyt.common.constants.AppConstants;
import org.pyt.common.constants.ParametroConstants;
import org.pyt.common.exceptions.EmpresasException;

import com.pyt.service.dto.EmpresaDTO;
import com.pyt.service.dto.ParametroDTO;
import com.pyt.service.dto.PersonaDTO;
import com.pyt.service.interfaces.IEmpresasSvc;
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
@FXMLFile(path = "/view", file = "empresa/empresa.fxml")
public class EmpresaCRUBean extends ABean<EmpresaDTO> {
	@Inject(resource = "com.pyt.service.implement.ParametrosSvc")
	private IParametrosSvc parametroSvc;
	@Inject(resource = "com.pyt.service.implement.EmpresaSvc")
	private IEmpresasSvc empresaSvc;
	@FXML
	private Label codigo;
	@FXML
	private javafx.scene.control.TextField nombre;
	@FXML
	private javafx.scene.control.TextField nit;
	@FXML
	private javafx.scene.control.TextField digito;
	@FXML
	private javafx.scene.control.TextField direccion;
	@FXML
	private javafx.scene.control.TextField email;
	@FXML
	private javafx.scene.control.TextField telefono;
	@FXML
	private javafx.scene.control.TextField pais;
	@FXML
	private PopupParametrizedControl representante;
	@FXML
	private PopupParametrizedControl contador;
	@FXML
	private javafx.scene.control.TextField nContador;
	@FXML
	private Label titulo;
	@FXML
	private BorderPane pane;
	@FXML
	private PopupParametrizedControl moneda;

	@FXML
	public void initialize() {
		NombreVentana = "Agregando Nueva Empresa";
		titulo.setText(NombreVentana);
		registro = new EmpresaDTO();
		pais.setText(AppConstants.DEFAULT_PAIS_COL);
		moneda.setPopupOpenAction(() -> popupMonedas());
		moneda.setCleanValue(() -> {
			registro.setMonedaDefecto(null);
			this.moneda.setText("");
		});
		representante.setPopupOpenAction(() -> popupRepresentante());
		representante.setCleanValue(() -> {
			registro.setRepresentante(null);
			this.representante.setText("");
		});
		contador.setPopupOpenAction(() -> popupContador());
		contador.setCleanValue(() -> {
			registro.setContador(null);
			this.contador.setText("");
		});
	}

	/**
	 * Pasa los campos de la pantalla el objeto dto
	 */
	private void load() {
		if (registro == null) {
			registro = new EmpresaDTO();
		}
		registro.setCodigo(codigo.getText());
		registro.setNombre(nombre.getText());
		registro.setNit(nit.getText());
		registro.setDigitoVerificacion(digito.getText());
		registro.setDireccion(direccion.getText());
		registro.setCorreoElectronico(email.getText());
		registro.setTelefono(telefono.getText());
		registro.setPais(pais.getText());
	}

	private void loadFxml() {
		if (registro == null)
			return;
		codigo.setText(registro.getCodigo());
		nombre.setText(registro.getNombre());
		nit.setText(registro.getNit());
		digito.setText(registro.getDigitoVerificacion());
		direccion.setText(registro.getDireccion());
		email.setText(registro.getCorreoElectronico());
		telefono.setText(registro.getTelefono());
		pais.setText(registro.getPais());
		this.moneda.setText(registro.getMonedaDefecto().getDescripcion());
		representante.setText(registro.getRepresentante().getNombres());
		contador.setText(registro.getContador().getNombres());
		nContador.setText(registro.getContador().getNumeroTarjetaProfesional());
	}

	public void load(EmpresaDTO dto) {
		if (dto != null && dto.getCodigo() != null) {
			registro = dto;
			loadFxml();
			titulo.setText("Modificando Empresa");
		} else {
			error("La empresa es invalida para editar.");
			cancel();
		}
	}

	public void add() {
		load();
		try {
			if (StringUtils.isNotBlank(registro.getCodigo())) {
				empresaSvc.update(registro, userLogin);
				notificar("Se guardo la empresa correctamente.");
				cancel();
			} else {
				empresaSvc.insert(registro, userLogin);
				codigo.setText(registro.getCodigo());
				notificar("Se agrego la emrpesa correctamente.");
				cancel();
			}
		} catch (EmpresasException e) {
			error(e);
		}
	}

	public void popupMonedas() {
		try {
			((PopupGenBean<ParametroDTO>) controllerPopup(new PopupGenBean<ParametroDTO>(ParametroDTO.class)
					.addDefaultValuesToGenericParametrized(ParametroConstants.FIELD_NAME_GROUP,
							parametroSvc.getIdByParametroGroup(ParametroConstants.GRUPO_MONEDA))).setWidth(350))
									.load("#{EmpresaCRUBean.moneda}");
		} catch (Exception e) {
			error(e);
		}
	}

	public void setMoneda(ParametroDTO moneda) {
		registro.setMonedaDefecto(moneda);
		this.moneda.setText(moneda.getDescripcion());
	}

	public void popupRepresentante() {
		try {
			((PopupGenBean<PersonaDTO>) controllerPopup(new PopupGenBean<PersonaDTO>(PersonaDTO.class)).setWidth(350))
					.load("#{EmpresaCRUBean.representante}");
		} catch (Exception e) {
			error(e);
		}
	}

	public void setRepresentante(PersonaDTO persona) {
		registro.setRepresentante(persona);
		this.moneda.setText(persona.getNombres());
	}

	public void popupContador() {
		try {
			((PopupGenBean<PersonaDTO>) controllerPopup(new PopupGenBean<PersonaDTO>(PersonaDTO.class)).setWidth(350))
					.load("#{EmpresaCRUBean.contador}");
		} catch (Exception e) {
			error(e);
		}
	}

	public void setContador(PersonaDTO persona) {
		registro.setContador(persona);
		this.contador.setText(persona.getNombres());
	}

	public void cancel() {
		getController(EmpresaBean.class);
	}
}
