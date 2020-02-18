package org.pyt.app.beans.concepto;

import org.apache.commons.lang3.StringUtils;
import org.ea.app.custom.PopupParametrizedControl;
import org.pyt.common.annotations.Inject;
import org.pyt.common.constants.CuentaContableConstants;
import org.pyt.common.constants.ParametroConstants;
import org.pyt.common.exceptions.DocumentosException;
import org.pyt.common.validates.ValidFields;

import com.pyt.service.dto.ConceptoDTO;
import com.pyt.service.dto.CuentaContableDTO;
import com.pyt.service.dto.EmpresaDTO;
import com.pyt.service.dto.ParametroDTO;
import com.pyt.service.interfaces.IDocumentosSvc;
import com.pyt.service.interfaces.IParametrosSvc;

import co.com.arquitectura.annotation.proccessor.FXMLFile;
import co.com.japl.ea.beans.abstracts.ABean;
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
		NombreVentana = i18n().valueBundle("fxml.title.add.new.concept").get();
		titulo.setText(NombreVentana);
		registro = new ConceptoDTO();
		empresa.setPopupOpenAction(() -> popupEmpresa());
		empresa.setCleanValue(() -> registro.setEmpresa(null));
		estado.setPopupOpenAction(() -> popupEstado());
		estado.setCleanValue(() -> registro.setEstado(null));
		cuentasGastos.setPopupOpenAction(() -> popupCuentaGasto());
		cuentasGastos.setCleanValue(() -> registro.setCuentaGasto(null));
		cuentasXPagar.setPopupOpenAction(() -> popupCuentaXPagar());
		cuentasXPagar.setCleanValue(() -> registro.setCuentaXPagar(null));
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
			titulo.setText(i18n().get("fxml.title.update.concept"));
		} else {
			error(i18n().valueBundle("err.concept.invalid.to.edit"));
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
				i18n().valueBundle("err.msn.concept.field.name.empty"));
		valid &= ValidFields.valid(registro.getDescripcion(), descripcion, true, 1, 100,
				i18n().valueBundle("err.msn.concept.field.description.empty"));
		valid &= ValidFields.valid(registro.getEstado(), estado, true, null, null,
				i18n().valueBundle("err.msn.concept.field.state.empty"));
		valid &= ValidFields.valid(registro.getEmpresa(), empresa, true, null, null,
				i18n().valueBundle("err.msn.concept.field.enterprise.empty"));
		valid &= ValidFields.valid(registro.getCuentaGasto(), cuentasGastos, true, null, null,
				i18n().valueBundle("err.msn.concept.field.expenseaccount.empty"));
		valid &= ValidFields.valid(registro.getCuentaXPagar(), cuentasXPagar, true, null, null,
				i18n().valueBundle("err.msn.concept.field.debtstopay.empty"));
		return valid;
	}

	public void add() {
		load();
		try {
			if (valid()) {
				if (StringUtils.isNotBlank(registro.getCodigo())) {
					documentoSvc.update(registro, getUsuario());
					notificarI18n(("mensaje.concept.have.been.update.succesfull"));
					cancel();
				} else {
					documentoSvc.insert(registro, getUsuario());
					codigo.setText(registro.getCodigo());
					notificarI18n(("mensaje.concept.have.been.insert.succesfull"));
					cancel();
				}
			}
		} catch (DocumentosException e) {
			error(e);
		}
	}

	public final void popupEmpresa() {
		try {
			controllerGenPopup(EmpresaDTO.class).setWidth(350).load("#{ConceptoCRUBean.empresa}");
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
			controllerGenPopup(CuentaContableDTO.class).setWidth(350)
					.addDefaultValuesToGenericParametrized(CuentaContableConstants.FIELD_ASOCIADO,
							CuentaContableConstants.CUENTA_GASTO)
					.load("#{ConceptoCRUBean.cuentaGasto}");
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
			controllerGenPopup(CuentaContableDTO.class).setWidth(350)
					.addDefaultValuesToGenericParametrized(CuentaContableConstants.FIELD_ASOCIADO,
							CuentaContableConstants.CUENTA_X_PAGAR)
					.load("#{ConceptoCRUBean.cuentaXPagar}");
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
			controllerGenPopup(ParametroDTO.class)
					.addDefaultValuesToGenericParametrized(ParametroConstants.FIELD_NAME_GROUP,
							ParametroConstants.GRUPO_ESTADO_CONCEPTO)
					.setWidth(350).load("#{ConceptoCRUBean.estado}");
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
