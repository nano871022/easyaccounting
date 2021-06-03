package co.com.japl.ea.app.beans.concepto;

import org.apache.commons.lang3.StringUtils;
import org.controlsfx.glyphfont.FontAwesome.Glyph;
import org.pyt.common.annotations.Inject;
import org.pyt.common.common.DtoUtils;
import org.pyt.common.constants.CuentaContableConstants;
import org.pyt.common.constants.ParametroConstants;
import org.pyt.common.constants.PermissionConstants;

import co.com.arquitectura.annotation.proccessor.FXMLFile;
import co.com.japl.ea.app.custom.PopupParametrizedControl;
import co.com.japl.ea.beans.abstracts.ABean;
import co.com.japl.ea.common.button.apifluid.ButtonsImpl;
import co.com.japl.ea.common.validates.ValidFields;
import co.com.japl.ea.dto.dto.ConceptoDTO;
import co.com.japl.ea.dto.dto.CuentaContableDTO;
import co.com.japl.ea.dto.dto.EmpresaDTO;
import co.com.japl.ea.dto.dto.ParametroDTO;
import co.com.japl.ea.dto.interfaces.IDocumentosSvc;
import co.com.japl.ea.dto.interfaces.IParametrosSvc;
import co.com.japl.ea.exceptions.DocumentosException;
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
	private FlowPane buttons;
	private BooleanProperty save;
	private BooleanProperty edit;

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
		save = new SimpleBooleanProperty();
		edit = new SimpleBooleanProperty();
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
			visibleButtons();
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

	public void visibleButtons() {
		var save = DtoUtils.haveCode(registro) && PermissionUtil.INSTANCE()
				.havePerm(PermissionConstants.CONST_PERM_CREATE, ConceptoBean.class, getUsuario().getGrupoUser());
		var edit = !DtoUtils.haveCode(registro) && PermissionUtil.INSTANCE()
				.havePerm(PermissionConstants.CONST_PERM_CREATE, ConceptoBean.class, getUsuario().getGrupoUser());
		this.save.setValue(save);
		this.edit.setValue(edit);
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
