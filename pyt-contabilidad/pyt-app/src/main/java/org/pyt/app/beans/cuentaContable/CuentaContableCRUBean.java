package org.pyt.app.beans.cuentaContable;

import org.apache.commons.lang3.StringUtils;
import org.controlsfx.glyphfont.FontAwesome.Glyph;
import org.ea.app.custom.PopupParametrizedControl;
import org.pyt.common.annotations.Inject;
import org.pyt.common.common.DtoUtils;
import org.pyt.common.constants.ParametroConstants;
import org.pyt.common.constants.PermissionConstants;
import org.pyt.common.exceptions.CuentaContableException;
import org.pyt.common.validates.ValidFields;

import com.pyt.service.dto.CuentaContableDTO;
import com.pyt.service.dto.EmpresaDTO;
import com.pyt.service.dto.ParametroDTO;
import com.pyt.service.dto.ParametroGrupoDTO;
import com.pyt.service.interfaces.ICuentaContableSvc;
import com.pyt.service.interfaces.IGenericServiceSvc;

import co.com.arquitectura.annotation.proccessor.FXMLFile;
import co.com.japl.ea.beans.abstracts.ABean;
import co.com.japl.ea.common.button.apifluid.ButtonsImpl;
import co.com.japl.ea.utls.PermissionUtil;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;

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
	@Inject(resource = "com.pyt.service.implement.GenericServiceSvc")
	private IGenericServiceSvc<ParametroGrupoDTO> parametroSvc;
	@FXML
	private Label codigo;
	@FXML
	private TextField nombre;
	@FXML
	private PopupParametrizedControl asociado;
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
	private FlowPane buttons;
	private BooleanProperty save;
	private BooleanProperty edit;

	@FXML
	public void initialize() {
		NombreVentana = i18n().get("fxml.title.add.new.accountingaccount");
		save = new SimpleBooleanProperty();
		edit = new SimpleBooleanProperty();
		titulo.setText(NombreVentana);
		titulo.getStyleClass().add("titulo-page");
		titulo.setGraphic(
				new org.controlsfx.glyphfont.Glyph("FontAwesome", org.controlsfx.glyphfont.FontAwesome.Glyph.TAGS));
		registro = new CuentaContableDTO();
		naturaleza.setPopupOpenAction(() -> popupNaturaleza());
		naturaleza.setCleanValue(() -> registro.setNaturaleza(null));
		tipoPlanContable.setPopupOpenAction(() -> popupTipoPlanContable());
		tipoPlanContable.setCleanValue(() -> registro.setTipoPlanContable(null));
		tipo.setPopupOpenAction(() -> popupTipoCuentaContable());
		tipo.setCleanValue(() -> registro.setTipo(null));
		empresa.setPopupOpenAction(() -> popupEmpresa());
		empresa.setCleanValue(() -> registro.setEmpresa(null));
		asociado.setPopupOpenAction(() -> popupAsociado());
		asociado.setCleanValue(() -> registro.setAsociado(null));
		visibleButtons();
		ButtonsImpl.Stream(FlowPane.class).setLayout(buttons).setName("fxml.btn.save").action(this::add)
				.icon(Glyph.SAVE).isVisible(save).setName("fxml.btn.edit").action(this::add).icon(Glyph.SAVE)
				.isVisible(edit).setName("fxml.btn.cancel").action(this::cancel).build();

	}

	/**
	 * Pasa los campos de la pantalla el objeto dto
	 */
	private void load() {
		if (registro == null) {
			registro = new CuentaContableDTO();
		}
		if (!nombre.getText().isEmpty()) {
			registro.setNombre(nombre.getText());
		}
		if (!codigoCuenta.getText().isEmpty()) {
			registro.setCodigoCuenta(codigoCuenta.getText());
		}
	}

	private void loadFxml() {
		if (registro == null)
			return;
		codigo.setText(registro.getCodigo());
		nombre.setText(registro.getNombre());
		asociado.setText(getCuentaAsociadaShow());
		codigoCuenta.setText(registro.getCodigoCuenta());
		tipoPlanContable.setText(registro.getTipoPlanContable().getNombre());
		naturaleza.setText(registro.getNaturaleza().getDescripcion());
		tipo.setText(registro.getTipo().getDescripcion());
		empresa.setText(registro.getEmpresa().getNombre());
	}

	private final String getCuentaAsociadaShow() {
		if (registro.getAsociado().getCodigoCuenta() != null && registro.getAsociado().getNombre() != null) {
			return registro.getAsociado().getCodigoCuenta() + ":" + registro.getAsociado().getNombre();
		}
		return null;
	}

	public void load(CuentaContableDTO dto) {
		if (dto != null && dto.getCodigo() != null) {
			registro = dto;
			visibleButtons();
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
		valid &= ValidFields.valid(registro.getNombre(), nombre, true, 1, 100,
				i18n().valueBundle("err.accountingaccount.field.name.empty"));
		valid &= ValidFields.valid(registro.getCodigoCuenta(), codigoCuenta, true, null, null,
				i18n().valueBundle("err.accountingaccount.field.accountcode.empty"));
		valid &= ValidFields.valid(registro.getNaturaleza(), naturaleza, true, null, null,
				i18n().valueBundle("err.accountingaccount.field.nature.empty"));
		valid &= ValidFields.valid(registro.getTipoPlanContable(), tipoPlanContable, true, null, null,
				i18n().valueBundle("err.accountingaccount.field.accountingplantype.empty"));
		valid &= ValidFields.valid(registro.getTipo(), tipo, true, null, null,
				i18n().valueBundle("err.accountingaccount.field.type.empty"));
		valid &= ValidFields.valid(registro.getEmpresa(), empresa, true, null, null,
				i18n().valueBundle("err.accountingaccount.field.enterprise.empty"));
		return valid;
	}

	public void add() {
		load();
		try {
			if (valid()) {
				if (StringUtils.isNotBlank(registro.getCodigo())) {
					cuentaContableSvc.update(registro, getUsuario());
					notificarI18n("mensaje.accountingaccount.was.updated.succesfull");
					cancel();
				} else {
					cuentaContableSvc.insert(registro, getUsuario());
					codigo.setText(registro.getCodigo());
					notificarI18n("mensaje.accountingaccount.was.inserted.succesfull");
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

	public final void popupAsociado() {
		try {
			controllerGenPopup(CuentaContableDTO.class).load("#{CuentaContableCRUBean.asociado}");
		} catch (Exception e) {
			error(e);
		}
	}

	public final void setAsociado(CuentaContableDTO cuentaContable) {
		if (cuentaContable != null) {
			registro.setAsociado(cuentaContable);
			asociado.setText(getCuentaAsociadaShow());
		}
	}

	public final void popupTipoPlanContable() {
		try {
			controllerGenPopup(ParametroDTO.class).setWidth(350)
					.addDefaultValuesToGenericParametrized(ParametroConstants.FIELD_NAME_GROUP,
							ParametroConstants.GRUPO_TIPO_PLAN_CONTABLE)
					.load("#{CuentaContableCRUBean.tipoPlanContable}");
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
			var bean = controllerGenPopup(ParametroDTO.class);
			bean.setWidth(350).addDefaultValuesToGenericParametrized(ParametroConstants.FIELD_NAME_GROUP,
					ParametroConstants.GRUPO_NATURALEZA);
			bean.load("#{CuentaContableCRUBean.naturaleza}");
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
			controllerGenPopup(ParametroDTO.class).setWidth(350)
					.addDefaultValuesToGenericParametrized(ParametroConstants.FIELD_NAME_GROUP,
							ParametroConstants.GRUPO_TIPO)
					.load("#{CuentaContableCRUBean.tipoCuentaContable}");
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
			controllerGenPopup(EmpresaDTO.class).setWidth(350).load("#{CuentaContableCRUBean.empresa}");
		} catch (Exception e) {
			error(e);
		}
	}

	public final void setEmpresa(EmpresaDTO empresa) {
		registro.setEmpresa(empresa);
		this.empresa.setText(empresa.getNombre());
	}

	protected void visibleButtons() {
		var save = !DtoUtils.haveCode(registro) && PermissionUtil.INSTANCE()
				.havePerm(PermissionConstants.CONST_PERM_CREATE, CuentaContableDTO.class, getUsuario().getGrupoUser());
		var edit = DtoUtils.haveCode(registro) && PermissionUtil.INSTANCE()
				.havePerm(PermissionConstants.CONST_PERM_CREATE, CuentaContableDTO.class, getUsuario().getGrupoUser());
		this.save.setValue(save);
		this.edit.setValue(edit);
	}

}
