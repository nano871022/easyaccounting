package org.pyt.app.beans.empresa;

import org.apache.commons.lang3.StringUtils;
import org.ea.app.custom.PopupParametrizedControl;
import org.pyt.app.components.PopupGenBean;
import org.pyt.common.annotations.Inject;
import org.pyt.common.common.ABean;
import org.pyt.common.constants.AppConstants;
import org.pyt.common.constants.ParametroConstants;
import org.pyt.common.exceptions.EmpresasException;

import com.pyt.service.dto.EmpresaDTO;
import com.pyt.service.dto.ParametroDTO;
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
	private javafx.scene.control.TextField representante;
	@FXML
	private javafx.scene.control.TextField contador;
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
		moneda.setPopupOpenAction(()->popupMonedas());
		moneda.setCleanValue(()->{
			registro.setMonedaDefecto(null);
			this.moneda.setText("");
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
		registro.setNombreRepresentante(representante.getText());
		registro.setNombreContador(contador.getText());
		registro.setTarjetaProfeccionalContador(nContador.getText());
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
		representante.setText(registro.getNombreRepresentante());
		contador.setText(registro.getNombreContador());
		nContador.setText(registro.getTarjetaProfeccionalContador());
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
			.addDefaultValuesToGenericParametrized(ParametroConstants.FIELD_NAME_GROUP,parametroSvc.getIdByParametroGroup(ParametroConstants.GRUPO_MONEDA)))
			.addDefaultValuesToGenericParametrized(ParametroConstants.FIELD_NAME_STATE, ParametroConstants.COD_ESTADO_PARAMETRO_ACTIVO)
			).load("#{EmpresaCRUBean.moneda}");
		} catch (Exception e) {
			error(e);
		}
	}
	
	public void setMoneda(ParametroDTO moneda) {
		registro.setMonedaDefecto(moneda);
		this.moneda.setText(moneda.getDescripcion());
	}

	public void cancel() {
		getController(EmpresaBean.class);
	}
}
