package org.pyt.app.beans.cuentaContable;

import java.util.List;

import org.controlsfx.glyphfont.FontAwesome.Glyph;
import org.ea.app.custom.PopupParametrizedControl;
import org.pyt.app.components.ConfirmPopupBean;
import org.pyt.common.annotations.Inject;
import org.pyt.common.constants.ParametroConstants;
import org.pyt.common.constants.PermissionConstants;

import com.pyt.service.dto.CuentaContableDTO;
import com.pyt.service.dto.EmpresaDTO;
import com.pyt.service.dto.ParametroDTO;
import com.pyt.service.dto.ParametroGrupoDTO;
import com.pyt.service.interfaces.ICuentaContableSvc;
import com.pyt.service.interfaces.IGenericServiceSvc;

import co.com.arquitectura.annotation.proccessor.FXMLFile;
import co.com.japl.ea.beans.abstracts.AGenericInterfacesBean;
import co.com.japl.ea.common.button.apifluid.ButtonsImpl;
import co.com.japl.ea.dto.system.ConfigGenericFieldDTO;
import co.com.japl.ea.utls.PermissionUtil;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.fxml.FXML;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;

/**
 * Bean encargado de crear las empresas
 * 
 * @author alejandro parra
 * @since 07/05/2018
 */
@FXMLFile(path = "view/cuentaContable", file = "listCuentaContable.fxml")
public class CuentaContableBean extends AGenericInterfacesBean<CuentaContableDTO> {
	@Inject(resource = "com.pyt.service.implement.CuentaContableSvc")
	private ICuentaContableSvc cuentaContableSvc;
	@Inject(resource = "com.pyt.service.implement.GenericServiceSvc")
	private IGenericServiceSvc<ParametroGrupoDTO> parametroGrupoSvc;
	@FXML
	private javafx.scene.control.TableView<CuentaContableDTO> tabla;
	@FXML
	private TextField nombre;
	@FXML
	private PopupParametrizedControl asociado;
	@FXML
	private PopupParametrizedControl tipoPlanContable;
	@FXML
	private PopupParametrizedControl tipoCuentaContables;
	@FXML
	private HBox paginador;
	private CuentaContableDTO filter;
	private List<ConfigGenericFieldDTO> config;
	@FXML
	private FlowPane buttons;

	@FXML
	public void initialize() {
		NombreVentana = i18n().get("fxml.title.list.accountingaccount");
		registro = new CuentaContableDTO();
		filter = new CuentaContableDTO();
		registro.setNaturaleza(new ParametroDTO());
		registro.setTipoPlanContable(new ParametroDTO());
		registro.setTipo(new ParametroDTO());
		registro.setEmpresa(new EmpresaDTO());
		asociado.setPopupOpenAction(() -> popupAsociado());
		asociado.setCleanValue(() -> filter.setAsociado(null));
		tipoCuentaContables.setPopupOpenAction(() -> popupTipoCuentaContable());
		tipoCuentaContables.setCleanValue(() -> filter.setTipo(null));
		tipoPlanContable.setPopupOpenAction(() -> popupTipoPlanContable());
		tipoPlanContable.setCleanValue(() -> filter.setTipoPlanContable(null));
		config = findFields(TypeGeneric.COLUMN, CuentaContableDTO.class, CuentaContableBean.class);
		save = new SimpleBooleanProperty();
		edit = new SimpleBooleanProperty();
		delete = new SimpleBooleanProperty();
		view = new SimpleBooleanProperty();
		visibleButtons();
		loadDataModel(paginador, tabla);
		loadColumns();
		ButtonsImpl.Stream(FlowPane.class).setLayout(buttons).setName("fxml.btn.save").action(this::add)
				.icon(Glyph.SAVE).isVisible(save).setName("fxml.btn.edit").action(this::set).icon(Glyph.SAVE)
				.isVisible(edit).setName("fxml.btn.delete").action(this::del).icon(Glyph.REMOVE).isVisible(delete)
				.setName("fxml.btn.view").action(this::set).icon(Glyph.FILE_TEXT).isVisible(view).build();
	}

	public final void popupTipoPlanContable() {
		try {
			var popup = controllerGenPopup(ParametroDTO.class);
			popup.setWidth(250).addDefaultValuesToGenericParametrized(ParametroConstants.FIELD_NAME_GROUP,
					ParametroConstants.GRUPO_TIPO_PLAN_CONTABLE);
			popup.load("#{CuentaContableBean.tipoPlanContable}");
		} catch (Exception e) {
			error(e);
		}
	}

	public final void popupTipoCuentaContable() {
		try {
			var popup = controllerGenPopup(ParametroDTO.class);
			popup.setWidth(250).addDefaultValuesToGenericParametrized(ParametroConstants.FIELD_NAME_GROUP,
					ParametroConstants.GRUPO_TIPO);
			popup.load("#{CuentaContableBean.tipoCuentaContable}");
		} catch (Exception e) {
			error(e);
		}
	}

	public final void popupAsociado() {
		try {
			controllerGenPopup(CuentaContableDTO.class).load("#{CuentaContableBean.asociado}");
		} catch (Exception e) {
			error(e);
		}
	}

	public final void setAsociado(CuentaContableDTO asociado) {
		if (asociado != null) {
			filter.setAsociado(asociado);
			this.asociado.setText(asociado.getCodigoCuenta() + ":" + asociado.getNombre());
		}
	}

	public final void setTipoPlanContable(ParametroDTO tipoPlanContable) {
		if (tipoPlanContable != null) {
			filter.setTipoPlanContable(tipoPlanContable);
			this.tipoPlanContable.setText(tipoPlanContable.getNombre());
		}
	}

	public final void setTipoCuentaContable(ParametroDTO tipoCuentaContable) {
		if (tipoCuentaContable != null) {
			filter.setTipo(tipoCuentaContable);
			tipoCuentaContables.setText(tipoCuentaContable.getNombre());
		}
	}

	public void add() {
		getController(CuentaContableCRUBean.class);
	}

	public void search() {
		dataTable.search();
	}

	public void set() {
		registro = dataTable.getSelectedRow();
		getController(CuentaContableCRUBean.class).load(registro);
	}

	public void del() {
		try {
			controllerPopup(ConfirmPopupBean.class).load("#{CuentaContableBean.delete}",
					i18n().get("mensaje.wish.do.delete.selected.rows"));
		} catch (Exception e) {
			error(e);
		}
	}

	public void setDelete(Boolean valid) {
		try {
			if (!valid)
				return;
			registro = dataTable.getSelectedRow();
			if (registro != null) {
				cuentaContableSvc.delete(registro, getUsuario());
				notificarI18n("mensaje.accountingaccount.have.been.deleted");
				dataTable.search();
			} else {
				alertaI18n("mensaje.accountingaccount.havent.been.selected");
			}
		} catch (Exception e) {
			error(e);
		}
	}

	protected void visibleButtons() {
		var save = PermissionUtil.INSTANCE().havePerm(PermissionConstants.CONST_PERM_CREATE, CuentaContableBean.class,
				getUsuario().getGrupoUser());
		var edit = PermissionUtil.INSTANCE().havePerm(PermissionConstants.CONST_PERM_UPDATE, CuentaContableBean.class,
				getUsuario().getGrupoUser());
		var delete = PermissionUtil.INSTANCE().havePerm(PermissionConstants.CONST_PERM_DELETE, CuentaContableBean.class,
				getUsuario().getGrupoUser());
		var view = PermissionUtil.INSTANCE().havePerm(PermissionConstants.CONST_PERM_READ, CuentaContableBean.class,
				getUsuario().getGrupoUser());
		this.save.setValue(save);
		this.edit.setValue(edit);
		this.delete.setValue(delete);
		this.view.setValue(view);

	}

	@Override
	public void selectedRow(MouseEvent eventHandler) {
		visibleButtons();
	}

	@Override
	public TableView<CuentaContableDTO> getTableView() {
		return tabla;
	}

	@Override
	public Integer getMaxColumns(TypeGeneric typeGeneric) {
		return null;
	}

	@Override
	public List<ConfigGenericFieldDTO> getListGenericsFields(TypeGeneric typeGeneric) {
		return config;
	}

	@Override
	public Class<CuentaContableDTO> getClazz() {
		return CuentaContableDTO.class;
	}

	@Override
	public GridPane getGridPane(TypeGeneric typeGeneric) {
		return null;
	}

	@Override
	public CuentaContableDTO getFilterToTable(CuentaContableDTO filter) {
		return this.filter;
	}

}
