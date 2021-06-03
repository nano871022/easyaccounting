package co.com.japl.ea.app.beans.banco;

import java.util.List;

import org.apache.commons.collections4.MultiValuedMap;
import org.apache.commons.collections4.multimap.ArrayListValuedHashMap;
import org.controlsfx.glyphfont.FontAwesome.Glyph;
import org.pyt.common.annotations.Inject;
import org.pyt.common.constants.ParametroConstants;
import org.pyt.common.constants.PermissionConstants;

import co.com.arquitectura.annotation.proccessor.FXMLFile;
import co.com.japl.ea.app.components.ConfirmPopupBean;
import co.com.japl.ea.app.components.PopupGenBean;
import co.com.japl.ea.app.custom.PopupParametrizedControl;
import co.com.japl.ea.beans.abstracts.AGenericInterfacesBean;
import co.com.japl.ea.common.button.apifluid.ButtonsImpl;
import co.com.japl.ea.dto.dto.BancoDTO;
import co.com.japl.ea.dto.dto.ParametroDTO;
import co.com.japl.ea.dto.interfaces.IBancosSvc;
import co.com.japl.ea.dto.interfaces.IParametrosSvc;
import co.com.japl.ea.dto.system.ConfigGenericFieldDTO;
import co.com.japl.ea.exceptions.BancoException;
import co.com.japl.ea.utls.PermissionUtil;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;

/**
 * Bean encargado de crear las actividades ica
 * 
 * @author alejandro parra
 * @since 07/05/2018
 */
@FXMLFile(path = "view/banco", file = "listBanco.fxml")
public class BancoBean extends AGenericInterfacesBean<BancoDTO> {
	@Inject(resource = "com.pyt.service.implement.BancoSvc")
	private IBancosSvc bancoSvc;
	@Inject(resource = "com.pyt.service.implement.ParametrosSvc")
	private IParametrosSvc parametroSvc;
	@FXML
	private javafx.scene.control.TableView<BancoDTO> tabla;
	@FXML
	private TextField nombre;
	@FXML
	private TextField descripcion;
	@FXML
	private TextField numeroCuenta;
	@FXML
	private PopupParametrizedControl tipoCuenta;
	@FXML
	private PopupParametrizedControl tipoBanco;
	@FXML
	private PopupParametrizedControl estado;
	@FXML
	private HBox paginador;
	@FXML
	private FlowPane panel;
	private BooleanProperty save;
	private BooleanProperty update;
	private BooleanProperty delete;
	private BooleanProperty view;
	private StringProperty spName;
	private StringProperty spDescription;

	private MultiValuedMap<TypeGeneric, ConfigGenericFieldDTO> configGenericFields;

	@FXML
	public void initialize() {
		NombreVentana = i18n().valueBundle("fxml.tittle.list.bank").get();
		registro = new BancoDTO();
		configGenericFields = new ArrayListValuedHashMap<>();
		findFields(TypeGeneric.COLUMN, BancoDTO.class, BancoBean.class)
				.forEach(row -> configGenericFields.put(TypeGeneric.COLUMN, row));
		loadDataModel(paginador, tabla);
		loadColumns();
		configProperties();
		configFields();
		visibleButtons();
		ButtonsImpl.Stream(panel.getClass()).setLayout(panel).setName("fxml.btn.create").isVisible(save)
				.icon(Glyph.SAVE).action(this::add).setName("fxml.btn.update").isVisible(update).icon(Glyph.EDIT)
				.action(this::set).setName("fxml.btn.delete").isVisible(delete).icon(Glyph.REMOVE).action(this::del)
				.setName("fxml.btn.view").isVisible(view).icon(Glyph.FILE_TEXT).action(this::set).build();
	}

	protected void configProperties() {
		save = new SimpleBooleanProperty();
		update = new SimpleBooleanProperty();
		delete = new SimpleBooleanProperty();
		view = new SimpleBooleanProperty();
		spName = new SimpleStringProperty();
		spDescription = new SimpleStringProperty();
	}

	protected void configFields() {
		tipoBanco.setPopupOpenAction(() -> popupOpenTipoBanco());
		tipoBanco.setCleanValue(() -> {
			registro.setTipoBanco(null);
			tipoBanco.setText(null);
		});
		tipoCuenta.setPopupOpenAction(() -> popupOpenTipoCuentas());
		tipoCuenta.setCleanValue(() -> {
			registro.setTipoCuenta(null);
			tipoCuenta.setText(null);
		});
		estado.setPopupOpenAction(() -> popupOpenEstado());
		estado.setCleanValue(() -> {
			registro.setEstado(null);
			estado.setText(null);
		});
		spName.addListener(value -> registro.setNombre(spName.get()));
		spDescription.addListener(value -> registro.setDescripcion(spDescription.get()));
		nombre.textProperty().bindBidirectional(spName);
		descripcion.textProperty().bindBidirectional(spDescription);
	}

	public void add() {
		getController(BancoCRUBean.class);
	}

	public void del() {
		try {
			controllerPopup(ConfirmPopupBean.class).load("#{BancoBean.delete}",
					i18n().valueBundle("mensaje.wish.delete.selected.rows").get());
		} catch (Exception e) {
			error(e);
		}
	}

	public final void popupOpenTipoBanco() {
		try {
			controllerPopup(new PopupGenBean<ParametroDTO>(ParametroDTO.class))
					.addDefaultValuesToGenericParametrized(ParametroConstants.FIELD_NAME_GROUP,
							ParametroConstants.GRUPO_TIPO_BANCO)
					.setWidth(350).load("#{BancoBean.tipoBanco}");
		} catch (Exception e) {
			error(e);
		}
	}

	public final void setTipoBanco(ParametroDTO parametro) {
		registro.setTipoBanco(parametro);
		tipoBanco.setText(parametro.getDescripcion());
	}

	public final void popupOpenTipoCuentas() {
		try {
			controllerPopup(new PopupGenBean<ParametroDTO>(ParametroDTO.class))
					.addDefaultValuesToGenericParametrized(ParametroConstants.FIELD_NAME_GROUP,
							ParametroConstants.GRUPO_TIPO_CUENTA)
					.setWidth(250).load("#{BancoBean.tipoCuentas}");
		} catch (Exception e) {
			error(e);
		}
	}

	public final void setTipoCuentas(ParametroDTO parametro) {
		registro.setTipoCuenta(parametro);
		tipoCuenta.setText(parametro.getDescripcion());
	}

	public final void popupOpenEstado() {
		try {
			controllerPopup(new PopupGenBean<ParametroDTO>(ParametroDTO.class))
					.addDefaultValuesToGenericParametrized(ParametroConstants.FIELD_NAME_GROUP,
							ParametroConstants.GRUPO_ESTADO_BANCO)
					.setWidth(250).load("#{BancoBean.estado}");
		} catch (Exception e) {
			error(e);
		}
	}

	public final void setEstado(ParametroDTO parametro) {
		registro.setEstado(parametro);
		estado.setText(parametro.getDescripcion());
	}

	public final void search() {
		dataTable.search();
	}

	public void setDelete(Boolean valid) {
		try {
			if (!valid)
				return;
			registro = dataTable.getSelectedRow();
			if (registro != null) {
				bancoSvc.delete(registro, getUsuario());
				notificarI18n("mensaje.bank.have.been.deleted");
				dataTable.search();
			} else {
				notificarI18n("mensaje.bank.havent.been.deleted");
			}
		} catch (BancoException e) {
			error(e);
		}
	}

	public void set() {
		registro = dataTable.getSelectedRow();
		if (registro != null) {
			getController(BancoCRUBean.class).load(registro);
		} else {
			notificarI18n("mensaje.bank.havent.selected");
		}
	}

	public Boolean isSelected() {
		return dataTable.isSelected();
	}

	public void visibleButtons() {
		var edit = dataTable.isSelected() && PermissionUtil.INSTANCE().havePerm(PermissionConstants.CONST_PERM_UPDATE,
				BancoBean.class, getUsuario().getGrupoUser());
		var add = PermissionUtil.INSTANCE().havePerm(PermissionConstants.CONST_PERM_CREATE, BancoBean.class,
				getUsuario().getGrupoUser());
		var del = dataTable.isSelected() && PermissionUtil.INSTANCE().havePerm(PermissionConstants.CONST_PERM_DELETE,
				BancoBean.class, getUsuario().getGrupoUser());
		var view = !edit && !add && !del && dataTable.isSelected() && PermissionUtil.INSTANCE()
				.havePerm(PermissionConstants.CONST_PERM_READ, BancoBean.class, getUsuario().getGrupoUser());
		save.setValue(add);
		update.setValue(edit);
		delete.setValue(del);
		this.view.setValue(view);
	}

	@Override
	public void selectedRow(MouseEvent eventHandler) {
		visibleButtons();
	}

	@Override
	public TableView<BancoDTO> getTableView() {
		return tabla;
	}

	@Override
	public Integer getMaxColumns(TypeGeneric typeGeneric) {
		return null;
	}

	@Override
	public List<ConfigGenericFieldDTO> getListGenericsFields(TypeGeneric typeGeneric) {
		return (List<ConfigGenericFieldDTO>) configGenericFields.get(typeGeneric);
	}

	@Override
	public Class<BancoDTO> getClazz() {
		return BancoDTO.class;
	}

	@Override
	public GridPane getGridPane(TypeGeneric typeGeneric) {
		return null;
	}

	@Override
	public BancoDTO getFilterToTable(BancoDTO filter) {
		return registro;
	}
}
