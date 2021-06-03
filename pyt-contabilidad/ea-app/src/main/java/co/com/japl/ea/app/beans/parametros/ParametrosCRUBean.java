package co.com.japl.ea.app.beans.parametros;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.apache.commons.lang3.StringUtils;
import org.controlsfx.glyphfont.FontAwesome.Glyph;
import org.pyt.common.annotations.Inject;
import org.pyt.common.common.DtoUtils;
import org.pyt.common.common.ListUtils;
import org.pyt.common.common.SelectList;
import org.pyt.common.constants.ParametroConstants;
import org.pyt.common.constants.PermissionConstants;

import co.com.arquitectura.annotation.proccessor.FXMLFile;
import co.com.japl.ea.app.components.ConfirmPopupBean;
import co.com.japl.ea.beans.abstracts.ABean;
import co.com.japl.ea.common.button.apifluid.ButtonsImpl;
import co.com.japl.ea.common.validates.ValidFields;
import co.com.japl.ea.common.validates.ValidateValues;
import co.com.japl.ea.dto.dto.ParametroDTO;
import co.com.japl.ea.dto.dto.ParametroGrupoDTO;
import co.com.japl.ea.dto.interfaces.IParametrosSvc;
import co.com.japl.ea.exceptions.LoadAppFxmlException;
import co.com.japl.ea.exceptions.ParametroException;
import co.com.japl.ea.exceptions.validates.ValidateValueException;
import co.com.japl.ea.utls.PermissionUtil;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;

/**
 * Se encarga de controlar la pantalla de parametros cru
 * 
 * @author Alejandro Parra
 * @since 22-06-2018
 */
@FXMLFile(path = "view/parametros", file = "parametro.fxml", nombreVentana = "Parametro CRUD")
public class ParametrosCRUBean extends ABean<ParametroDTO> {
	@Inject(resource = "com.pyt.service.implement.ParametrosSvc")
	private IParametrosSvc parametroSvc;
	@FXML
	private Label codigo;
	@FXML
	private Label lOrden;
	@FXML
	private TextField nombre;
	@FXML
	private TextField descripcion;
	@FXML
	private TextField grupo;
	@FXML
	private TextField valor;
	@FXML
	private TextField valor2;
	@FXML
	private TextField orden;
	@FXML
	private ChoiceBox<String> cGrupo;
	@FXML
	private Label lGrupo;
	@FXML
	private ChoiceBox<String> estado;
	private ParametroDTO registro;
	private ParametroGrupoDTO parametroGrupo;
	private ValidateValues validate;
	@FXML
	private HBox buttons;

	@FXML
	public void initialize() {
		validate = new ValidateValues();
		registro = new ParametroDTO();
		SelectList.put(estado, ParametroConstants.mapa_estados_parametros);
		SelectList.put(cGrupo, ParametroConstants.MAPA_GRUPOS);
		estado.getSelectionModel().selectFirst();
		cGrupo.getSelectionModel().selectFirst();
		lGrupo.setVisible(false);
		cGrupo.setVisible(false);
		visibleButtons();
		ButtonsImpl.Stream(HBox.class).setLayout(buttons).setName("fxml.btn.save").action(this::createBtn)
				.icon(Glyph.SAVE).isVisible(save).setName("fxml.btn.edit").action(this::modifyBtn).icon(Glyph.EDIT)
				.isVisible(edit).setName("fxml.btn.delete").action(this::deleteBtn).icon(Glyph.REMOVE).isVisible(delete)
				.setName("fxml.btn.cancel").action(this::cancelBtn).build();
	}

	/**
	 * Se encarga de cargar el objeto registro.
	 */
	private void loadInDto() {
		registro.setNombre(nombre.getText());
		registro.setDescripcion(descripcion.getText());
		registro.setGrupo(grupo.getText());
		registro.setValor(valor.getText());
		registro.setValor2(valor2.getText());
		try {
			if (validate.isCast(orden.getText(), Long.class)) {
				registro.setOrden(validate.cast(orden.getText(), Long.class));
			}
		} catch (ValidateValueException e) {
			error(e);
		}
		registro.setEstado(String.valueOf(ParametroConstants.mapa_estados_parametros.get(estado.getValue())));
		if (StringUtils.isNotBlank(cGrupo.getValue()) && cGrupo.isVisible()) {
			parametroGrupo.setGrupo((String) SelectList.get(cGrupo, ParametroConstants.MAPA_GRUPOS));
		}
	}

	/**
	 * Se encarga de cargar el parametro para poder realizar crud sobre el registro
	 * 
	 * @param dto {@link ParametroDTO}
	 */
	public void load(ParametroDTO dto) {
		if (DtoUtils.isBlank(dto)) {
			newParameter();
		} else if (DtoUtils.isNotBlankFields(dto, "grupo") && !DtoUtils.haveCode(dto) && !isGrupoPrincipal(dto)) {
			newParameterWithGroup(dto);
		} else {
			editParameter(dto);
		}
		visibleButtons();
	}

	private void editParametertGroupPrincipal(ParametroDTO dto) {
		if (DtoUtils.haveCode(dto) && StringUtils.isNotBlank(dto.getGrupo())
				&& dto.getGrupo().contentEquals(ParametroConstants.GRUPO_PRINCIPAL)) {
			showGroup(true);
			searchParametroGrupoSelected(dto);
		}
	}

	private void searchParametroGrupoSelected(ParametroDTO dto) {
		parametroGrupo = new ParametroGrupoDTO();
		parametroGrupo.setParametro(dto.getCodigo());
		try {
			List<ParametroGrupoDTO> list = parametroSvc.getParametroGrupo(parametroGrupo);
			if (ListUtils.haveOneItem(list)) {
				parametroGrupo = list.get(0);
			} else if (ListUtils.haveMoreOneItem(list)) {
				error(i18n().valueBundle("err.rows.was.found.alotof.to.parameter", dto.getCodigo()));
			} else if (ListUtils.isBlank(list)) {
				error(i18n().valueBundle("err.rows.wasnt.found.to.parameter", dto.getCodigo()));
			}
		} catch (ParametroException e) {
			error(e);
		}
	}

	private boolean isGrupoPrincipal(ParametroDTO dto) {
		return StringUtils.isNotBlank(dto.getGrupo())
				&& dto.getGrupo().contentEquals(ParametroConstants.GRUPO_PRINCIPAL);
	}

	private void showGroup(Boolean show) {
		grupo.setEditable(false);
		lGrupo.setVisible(show);
		cGrupo.setVisible(show);
	}

	private void addNextOrder() {
		try {
			registro.setOrden(parametroSvc.totalCount(registro).longValue() + 1);
		} catch (ParametroException e) {
			error(e);
		}
	}

	private void showOrder(Boolean show) {
		orden.setVisible(show);
		lOrden.setVisible(show);
	}

	private void newParameter() {
		registro = new ParametroDTO();
		registro.setGrupo(ParametroConstants.GRUPO_PRINCIPAL);
		parametroGrupo = new ParametroGrupoDTO();
		showGroup(true);
		showOrder(false);
		assign();
	}

	private void editParameter(ParametroDTO dto) {
		editParametertGroupPrincipal(dto);
		registro = dto;
		showOrder(!isGrupoPrincipal(dto));
		assign();
	}

	private void newParameterWithGroup(ParametroDTO dto) {
		showGroup(false);
		showOrder(true);
		registro = dto;
		addNextOrder();
		assign();
	}

	private void assign() {
		codigo.setText(registro.getCodigo());
		nombre.setText(registro.getNombre());
		descripcion.setText(registro.getDescripcion());
		grupo.setText(registro.getGrupo());
		valor.setText(registro.getValor());
		valor2.setText(registro.getValor2());
		if (registro.getOrden() != null) {
			orden.setText(registro.getOrden().toString());
		}
		if (registro.getEstado() != null) {
			SelectList.selectItem(estado, ParametroConstants.mapa_estados_parametros,
					Integer.valueOf(registro.getEstado()));
		}
		if (parametroGrupo != null && StringUtils.isNotBlank(parametroGrupo.getCodigo())) {
			if (cGrupo.isVisible() && StringUtils.isNotBlank(parametroGrupo.getGrupo())) {
				SelectList.selectItem(cGrupo, ParametroConstants.MAPA_GRUPOS, parametroGrupo.getGrupo());
			}
		}
		grupo.setEditable(false);
	}

	/**
	 * Se encaga de validar el registro
	 * 
	 * @return
	 */
	private Boolean valid() {
		Boolean valid = true;
		valid &= ValidFields.valid(nombre, true, 2, 100, i18n().valueBundle("valid.parameter.name.is.empty"));
		valid &= ValidFields.valid(descripcion, true, 2, 100,
				i18n().valueBundle("valid.parameter.description.is.empty"));
		if (!(Optional.ofNullable(grupo.getText()).isPresent()
				&& Optional.ofNullable(grupo.getText()).get().contains("*"))) {
			valid &= ValidFields.valid(grupo, true, 2, 100, i18n().valueBundle("valid.parameter.group.is.empty"));
		}
		valid &= ValidFields.valid(estado, true, i18n().valueBundle("valid.parameter.state.is.empty"));
		if (!registro.getGrupo().contentEquals("*")) {
			valid &= ValidFields.numeric(valor, true, new BigDecimal("-1000000"), new BigDecimal("10000000000"),
					i18n().valueBundle("valid.parameter.value.is.empty"));
			valid &= ValidFields.numeric(orden, true, 1, 1000, i18n().valueBundle("valid.parameter.order.is.empty"));
		}
		return valid;
	}

	/**
	 * Se encaraga de crear el registro
	 */
	public void createBtn() {
		loadInDto();
		try {
			if (valid()) {
				validParametroGrupo();
				registro = parametroSvc.insert(registro, getUsuario());
				if (DtoUtils.isNotBlankFields(parametroGrupo, "grupo")) {
					parametroGrupo.setParametro(registro.getCodigo());
					parametroGrupo = parametroSvc.insert(parametroGrupo, getUsuario());
				}
				codigo.setText(registro.getCodigo());
				notificarI18n("mensaje.parameter.have.been.created.succesfull");
			} else {
				errorI18n("err.validation.did.found.problem");
			}
		} catch (ParametroException e) {
			error(e);
		} catch (Exception e) {
			error(e);
		}
	}

	/**
	 * Se encarga de validar el parametro grupo
	 * 
	 * @throws {@link Exception}
	 */
	private final void validParametroGrupo() throws Exception {
		if (parametroGrupo != null && StringUtils.isNotBlank(parametroGrupo.getGrupo())) {
			ParametroGrupoDTO pGrupo = new ParametroGrupoDTO();
			pGrupo.setGrupo(parametroGrupo.getGrupo());
			List<ParametroGrupoDTO> lista = parametroSvc.getParametroGrupo(pGrupo);
			if (lista.size() > 0) {
				for (ParametroGrupoDTO grupo : lista) {
					if (StringUtils.isBlank(grupo.getParametro()))
						continue;
					ParametroDTO parametro = new ParametroDTO();
					parametro.setCodigo(grupo.getParametro());
					if (grupo != null && registro != null && StringUtils.isNotBlank(registro.getCodigo())
							&& grupo.getParametro().contains(registro.getCodigo())) {
						continue;
					}
					List<ParametroDTO> parametros = parametroSvc.getAllParametros(parametro);
					if (parametros.size() > 0) {
						throw new Exception(i18n().valueBundle("err.group.yet.was.assign.to", parametroGrupo.getGrupo(),
								parametros.get(0).getNombre()).get());
					} else {
						parametroSvc.delete(grupo, getUsuario());
					}
				}
			}
		}
	}

	/**
	 * Se encarga de modificar el registro
	 */
	public void modifyBtn() {
		loadInDto();
		try {
			if (valid()) {
				validParametroGrupo();
				parametroSvc.update(registro, getUsuario());
				if (DtoUtils.isNotBlankFields(parametroGrupo, "grupo")) {
					parametroGrupo.setParametro(registro.getCodigo());
					parametroSvc.update(parametroGrupo, getUsuario());
				}
				notificarI18n("mensaje.parameter.have.been.edited.succesfull");
			} else {
				errorI18n("err.validation.did.found.problem");
			}
		} catch (ParametroException e) {
			error(e);
		} catch (Exception e) {
			error(e);
		}
	}

	/**
	 * Se encarga de eliminar el registro
	 */
	public void deleteBtn() {
		try {
			controllerPopup(ConfirmPopupBean.class).load("#{ParametrosCRUBean.delete}",
					i18n("mensaje.wish.do.delete.selected.row"));
		} catch (LoadAppFxmlException e) {
			error(e);
		}
	}

	public final void setDelete(Boolean val) {
		if (val) {
			loadInDto();
			try {
				if (StringUtils.isNotBlank(registro.getCodigo())) {
					if (parametroGrupo != null && StringUtils.isNotBlank(parametroGrupo.getCodigo())) {
						parametroSvc.delete(parametroGrupo, getUsuario());
					}
					parametroSvc.delete(registro, getUsuario());
					notificarI18n("mensaje.parameter.was.deleted.succesfull");
					cancelBtn();
				} else {
					errorI18n("err.code.wasnt.delete.is.empty");
				}
			} catch (ParametroException e) {
				error(e);
			}
		}
	}

	/**
	 * Se encarga de cancelar el registro y devolverse al bean principal
	 */
	public void cancelBtn() {
		getController(ParametrosBean.class);
		destroy();
	}

	@Override
	protected void visibleButtons() {
		var save = !DtoUtils.haveCode(registro) && PermissionUtil.INSTANCE()
				.havePerm(PermissionConstants.CONST_PERM_CREATE, ParametrosBean.class, getUsuario().getGrupoUser());
		var edit = DtoUtils.haveCode(registro) && PermissionUtil.INSTANCE()
				.havePerm(PermissionConstants.CONST_PERM_UPDATE, ParametrosBean.class, getUsuario().getGrupoUser());
		this.save.setValue(save);
		this.edit.setValue(edit);

	}

}
