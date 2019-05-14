package org.pyt.app.beans.cuentaContable;

import org.apache.commons.lang3.StringUtils;
import org.ea.app.custom.PopupParametrizedControl;
import org.pyt.app.components.PopupGenBean;
import org.pyt.common.abstracts.ABean;
import org.pyt.common.annotations.Inject;
import org.pyt.common.constants.ParametroConstants;
import org.pyt.common.exceptions.CuentaContableException;

import com.pyt.service.dto.CuentaContableDTO;
import com.pyt.service.dto.EmpresaDTO;
import com.pyt.service.dto.ParametroDTO;
import com.pyt.service.interfaces.ICuentaContableSvc;

import co.com.arquitectura.annotation.proccessor.FXMLFile;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;

/**
 * Se encarga de procesar la pantalla de creacion y actualizacion de un concepto
 * 
 * @author Alejandro Parra
 * @since 2018-05-22
 */
@FXMLFile(path = "view/cuentaContable", file = "cuentaContable.fxml")
public class CuentaContableCRUBean extends ABean<CuentaContableDTO> {
	@Inject(resource = "com.pyt.service.implement.CuentaContableSvc")
	private ICuentaContableSvc cuentaContableSvc;
	@FXML
	private Label codigo;
	@FXML
	private TextField nombre;
	@FXML
	private TextField asociado;
	@FXML
	private TextField codigoCuenta;
	@FXML
	private PopupParametrizedControl naturaleza;
	@FXML
	private PopupParametrizedControl tipoPlanContable;
	@FXML
	private PopupParametrizedControl tipo;
	@FXML
	private PopupParametrizedControl empresa;
	@FXML
	private Label titulo;
	@FXML
	private BorderPane pane;

	@FXML
	public void initialize() {
		NombreVentana = "Agregando Nueva Cuenta Contable";
		titulo.setText(NombreVentana);
		registro = new CuentaContableDTO();
		naturaleza.setPopupOpenAction(()->popupNaturaleza());
		naturaleza.setCleanValue(()->registro.setNaturaleza(null));
		tipoPlanContable.setPopupOpenAction(()->popupTipoPlanContable());
		tipoPlanContable.setCleanValue(()->registro.setTipoPlanContable(null));
		tipo.setPopupOpenAction(()->popupTipoCuentaContable());
		tipo.setCleanValue(()->registro.setTipo(null));
		empresa.setPopupOpenAction(()->popupEmpresa());
		empresa.setCleanValue(()->registro.setEmpresa(null));
	}

	/**
	 * Pasa los campos de la pantalla el objeto dto
	 */
	private void load() {
		if (registro == null) {
			registro = new CuentaContableDTO();
		}
		registro.setNombre(nombre.getText());
		registro.setAsociado(asociado.getText());
		registro.setCodigoCuenta(codigoCuenta.getText());
	}

	private void loadFxml() {
		if (registro == null)
			return;
		codigo.setText(registro.getCodigo());
		nombre.setText(registro.getNombre());
		asociado.setText(registro.getAsociado());
		codigoCuenta.setText(registro.getCodigoCuenta());
		tipoPlanContable.setText(registro.getTipoPlanContable().getNombre());
		naturaleza.setText(registro.getNaturaleza().getDescripcion());
		tipo.setText(registro.getTipo().getDescripcion());
		empresa.setText(registro.getEmpresa().getNombre());
	}

	public void load(CuentaContableDTO dto) {
		if (dto != null && dto.getCodigo() != null) {
			registro = dto;
			loadFxml();
			titulo.setText("Modificando Cuenta Contable");
		} else {
			error("La cuenta contable es invalido para editar.");
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
		valid &= StringUtils.isNotBlank(registro.getCodigoCuenta());
		valid &= StringUtils.isNotBlank(registro.getNaturaleza().getCodigo());
		valid &= StringUtils.isNotBlank(registro.getTipoPlanContable().getCodigo());
		valid &= StringUtils.isNotBlank(registro.getTipo().getCodigo());
		valid &= StringUtils.isNotBlank(registro.getEmpresa().getCodigo());
		return valid;
	}

	public void add() {
		load();
		try {
			if (valid()) {
				if (StringUtils.isNotBlank(registro.getCodigo())) {
					cuentaContableSvc.update(registro, userLogin);
					notificar("Se guardo la cuenta contable correctamente.");
					cancel();
				} else {
					cuentaContableSvc.insert(registro, userLogin);
					codigo.setText(registro.getCodigo());
					notificar("Se agrego la cuenta contable correctamente.");
					cancel();
				}
			}
		} catch (CuentaContableException e) {
			error(e);
		}
	}

	public void cancel() {
		getController(CuentaContableBean.class);
	}
	
	public final void popupTipoPlanContable() {
		try {
			((PopupGenBean<ParametroDTO>) controllerPopup(new PopupGenBean<ParametroDTO>(ParametroDTO.class))
					.setWidth(350)
					.addDefaultValuesToGenericParametrized(ParametroConstants.FIELD_NAME_GROUP, ParametroConstants.GRUPO_TIPO_PLAN_CONTABLE)
					).load("#{CuentaContableCRUBean.tipoPlanContable}");
		} catch (Exception e) {
			error(e);
		}
	}
	
	public final void setTipoPlanContable(ParametroDTO tipoPlanContable) {
		registro.setTipoPlanContable(tipoPlanContable);
		this.tipoPlanContable.setText(tipoPlanContable.getNombre());
	}

	public final void popupNaturaleza() {
		try {
			((PopupGenBean<ParametroDTO>) controllerPopup(new PopupGenBean<ParametroDTO>(ParametroDTO.class))
					.setWidth(350)
					.addDefaultValuesToGenericParametrized(ParametroConstants.FIELD_NAME_GROUP, ParametroConstants.GRUPO_NATURALEZA)
					).load("#{CuentaContableCRUBean.naturaleza}");
		} catch (Exception e) {
			error(e);
		}
	}
	
	public final void setNaturaleza(ParametroDTO naturaleza) {
		registro.setNaturaleza(naturaleza);
		this.naturaleza.setText(naturaleza.getNombre());
	}

	public final void popupTipoCuentaContable() {
		try {
			((PopupGenBean<ParametroDTO>) controllerPopup(new PopupGenBean<ParametroDTO>(ParametroDTO.class))
					.setWidth(350)
					.addDefaultValuesToGenericParametrized(ParametroConstants.FIELD_NAME_GROUP, ParametroConstants.GRUPO_TIPO)
					).load("#{CuentaContableCRUBean.tipoCuentaContable}");
		} catch (Exception e) {
			error(e);
		}
	}
	
	public final void setTipoCuentaContable(ParametroDTO tipoCuentaContable) {
		registro.setTipo(tipoCuentaContable);
		tipo.setText(tipoCuentaContable.getDescripcion());
	}
	
	public final void popupEmpresa() {
		try {
			((PopupGenBean<EmpresaDTO>) controllerPopup(new PopupGenBean<EmpresaDTO>(EmpresaDTO.class))
					.setWidth(350)
					).load("#{CuentaContableCRUBean.empresa}");
		} catch (Exception e) {
			error(e);
		}
	}
	
	public final void setEmpresa(EmpresaDTO empresa) {
		registro.setEmpresa(empresa);
		this.empresa.setText(empresa.getNombre());
	}

}
