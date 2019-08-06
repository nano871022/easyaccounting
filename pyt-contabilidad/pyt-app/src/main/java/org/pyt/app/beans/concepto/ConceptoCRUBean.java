package org.pyt.app.beans.concepto;

import org.apache.commons.lang3.StringUtils;
import org.ea.app.custom.PopupParametrizedControl;
import org.pyt.app.components.PopupGenBean;
import org.pyt.common.annotations.Inject;
import org.pyt.common.constants.CuentaContableConstants;
import org.pyt.common.constants.ParametroConstants;
import org.pyt.common.exceptions.DocumentosException;

import com.pyt.service.dto.ConceptoDTO;
import com.pyt.service.dto.CuentaContableDTO;
import com.pyt.service.dto.EmpresaDTO;
import com.pyt.service.dto.ParametroDTO;
import com.pyt.service.interfaces.IDocumentosSvc;
import com.pyt.service.interfaces.IParametrosSvc;

import co.com.arquitectura.annotation.proccessor.FXMLFile;
import co.com.japl.ea.beans.ABean;
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
@FXMLFile(path = "view/concepto", file = "concepto.fxml")
public class ConceptoCRUBean extends ABean<ConceptoDTO> {
	@Inject(resource = "com.pyt.service.implement.DocumentosSvc")
	private IDocumentosSvc documentoSvc;
	@Inject(resource = "com.pyt.service.implement.ParametrosSvc")
	private IParametrosSvc parametroSvc;
	@FXML
	private Label codigo;
	@FXML
	private TextField nombre;
	@FXML
	private TextField subConcepto;
	@FXML
	private TextField descripcion;
	@FXML
	private PopupParametrizedControl empresa;
	@FXML
	private PopupParametrizedControl cuentasGastos;
	@FXML
	private PopupParametrizedControl cuentasXPagar;
	@FXML
	private PopupParametrizedControl estado;
	@FXML
	private Label titulo;
	@FXML
	private BorderPane pane;
	public final static String FIELD_NAME = "nombre";

	@FXML
	public void initialize() {
		NombreVentana = "Agregando Nuevo Concepto";
		titulo.setText(NombreVentana);
		registro = new ConceptoDTO();
		empresa.setPopupOpenAction(()->popupEmpresa());
		empresa.setCleanValue(()->registro.setEmpresa(null));
		estado.setPopupOpenAction(()->popupEstado());
		estado.setCleanValue(()->registro.setEstado(null));
		cuentasGastos.setPopupOpenAction(()->popupCuentaGasto());
		cuentasGastos.setCleanValue(()->registro.setCuentaGasto(null));
		cuentasXPagar.setPopupOpenAction(()->popupCuentaXPagar());
		cuentasXPagar.setCleanValue(()->registro.setCuentaXPagar(null));
	}

	/**
	 * Pasa los campos de la pantalla el objeto dto
	 */
	private void load() {
		if (registro == null) {
			registro = new ConceptoDTO();
		}
		registro.setCodigo(codigo.getText());
		registro.setNombre(nombre.getText());
		registro.setDescripcion(descripcion.getText());
		registro.setSubconcepto(subConcepto.getText());
	}

	private void loadFxml() {
		if (registro == null)
			return;
		codigo.setText(registro.getCodigo());
		nombre.setText(registro.getNombre());
		descripcion.setText(registro.getDescripcion());
		subConcepto.setText(registro.getSubconcepto());
		empresa.setText(registro.getEmpresa().getNombre());
		estado.setText(registro.getEstado().getDescripcion());
		cuentasGastos.setText(registro.getCuentaGasto().getNombre());
		cuentasXPagar.setText(registro.getCuentaXPagar().getNombre());
	}

	public void load(ConceptoDTO dto) {
		if (dto != null && dto.getCodigo() != null) {
			registro = dto;
			loadFxml();
			titulo.setText("Modificando concepto");
		} else {
			error("EL concepto es invalido para editar.");
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
		valid &= registro.getEstado() != null && StringUtils.isNotBlank(registro.getEstado().getCodigo());
		valid &= registro.getEmpresa() != null && StringUtils.isNotBlank(registro.getEmpresa().getCodigo());
		valid &= registro.getCuentaGasto() != null && StringUtils.isNotBlank(registro.getCuentaGasto().getCodigo());
		valid &= registro.getCuentaXPagar() != null && StringUtils.isNotBlank(registro.getCuentaXPagar().getCodigo());
		return valid;
	}

	public void add() {
		load();
		try {
			if (valid()) {
				if (StringUtils.isNotBlank(registro.getCodigo())) {
					documentoSvc.update(registro, userLogin);
					notificar("Se guardo el concepto correctamente.");
					cancel();
				} else {
					documentoSvc.insert(registro, userLogin);
					codigo.setText(registro.getCodigo());
					notificar("Se agrego el concepto correctamente.");
					cancel();
				}
			}
		} catch (DocumentosException e) {
			error(e);
		}
	}
	
	public final void popupEmpresa() {
		try {
			((PopupGenBean<EmpresaDTO>) controllerPopup(new PopupGenBean<EmpresaDTO>(EmpresaDTO.class))
					.setWidth(350)
			).load("#{ConceptoCRUBean.empresa}");
		} catch (Exception e) {
			error(e);
		}
	}
	
	public final void setEmpresa(EmpresaDTO empresa) {
		registro.setEmpresa(empresa);
		this.empresa.setText(empresa.getNombre());
	}
	
	public final void popupCuentaGasto() {
		try {
			((PopupGenBean<CuentaContableDTO>) controllerPopup(new PopupGenBean<CuentaContableDTO>(CuentaContableDTO.class))
					.setWidth(350)
					.addDefaultValuesToGenericParametrized(CuentaContableConstants.FIELD_ASOCIADO, CuentaContableConstants.CUENTA_GASTO)
					).load("#{ConceptoCRUBean.cuentaGasto}");
		} catch (Exception e) {
			error(e);
		}
	}
	
	public final void setCuentaGasto(CuentaContableDTO cuentaContable) {
		registro.setCuentaGasto(cuentaContable);
		cuentasGastos.setText(cuentaContable.getNombre());
	}
	
	public final void popupCuentaXPagar() {
		try {
			((PopupGenBean<CuentaContableDTO>) controllerPopup(new PopupGenBean<CuentaContableDTO>(CuentaContableDTO.class))
					.setWidth(350)
					.addDefaultValuesToGenericParametrized(CuentaContableConstants.FIELD_ASOCIADO, CuentaContableConstants.CUENTA_X_PAGAR)
					).load("#{ConceptoCRUBean.cuentaXPagar}");
		} catch (Exception e) {
			error(e);
		}
	}
	
	public final void setCuentaXPagar(CuentaContableDTO cuentaContable) {
		registro.setCuentaXPagar(cuentaContable);
		cuentasXPagar.setText(cuentaContable.getNombre());
	}
	
	public final void popupEstado() {
		try {
			((PopupGenBean<ParametroDTO>) controllerPopup(new PopupGenBean<ParametroDTO>(ParametroDTO.class)
					.addDefaultValuesToGenericParametrized(ParametroConstants.FIELD_NAME_GROUP,parametroSvc.getIdByParametroGroup(ParametroConstants.GRUPO_ESTADO_CONCEPTO)))
					.setWidth(350)
			).load("#{ConceptoCRUBean.estado}");
		} catch (Exception e) {
			error(e);
		}
	}
	
	public final void setEstado(ParametroDTO parametro) {
		registro.setEstado(parametro);
		estado.setText(parametro.getDescripcion());
	}
	

	public void cancel() {
		getController(ConceptoBean.class);
		destroy();
	}

}
