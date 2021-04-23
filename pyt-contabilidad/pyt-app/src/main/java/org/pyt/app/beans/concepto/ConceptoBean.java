package org.pyt.app.beans.concepto;

import java.util.List;

import org.controlsfx.glyphfont.FontAwesome.Glyph;
import org.ea.app.custom.PopupParametrizedControl;
import org.pyt.app.components.ConfirmPopupBean;
import org.pyt.common.annotations.Inject;
import org.pyt.common.constants.ParametroConstants;
import org.pyt.common.constants.PermissionConstants;
import org.pyt.common.exceptions.DocumentosException;

import com.pyt.service.dto.ConceptoDTO;
import com.pyt.service.dto.EmpresaDTO;
import com.pyt.service.dto.ParametroDTO;
import com.pyt.service.interfaces.IDocumentosSvc;
import com.pyt.service.interfaces.IEmpresasSvc;
import com.pyt.service.interfaces.IParametrosSvc;

import co.com.arquitectura.annotation.proccessor.FXMLFile;
import co.com.japl.ea.beans.abstracts.AGenericInterfacesBean;
import co.com.japl.ea.common.button.apifluid.ButtonsImpl;
import co.com.japl.ea.dto.system.ConfigGenericFieldDTO;
import co.com.japl.ea.utls.PermissionUtil;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
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
@FXMLFile(path = "view/concepto", file = "listConcepto.fxml")
public class ConceptoBean extends AGenericInterfacesBean<ConceptoDTO> {
	@Inject(resource = "com.pyt.service.implement.DocumentosSvc")
	private IDocumentosSvc documentoSvc;
	@Inject(resource = "com.pyt.service.implement.ParametrosSvc")
	private IParametrosSvc parametroSvc;
	@Inject(resource = "com.pyt.service.implement.EmpresaSvc")
	private IEmpresasSvc empresaSvc;
	@FXML
	private javafx.scene.control.TableView<ConceptoDTO> tabla;
	@FXML
	private PopupParametrizedControl empresa;
	@FXML
	private TextField nombre;
	@FXML
	private TextField descripcion;
	@FXML
	private PopupParametrizedControl estado;
	@FXML
	private Button btnMod;
	@FXML
	private Button btnDel;
	@FXML
	private HBox paginador;
	private ConceptoDTO filter;
	@FXML
	private FlowPane buttons;
	private List<ConfigGenericFieldDTO> columns;

	@FXML
	public void initialize() {
		NombreVentana = i18n().get("fxml.title.concept.list");
		registro = new ConceptoDTO();
		filter = new ConceptoDTO();
		estado.setPopupOpenAction(() -> popupEstado());
		estado.setCleanValue(() -> filter.setEstado(null));
		empresa.setPopupOpenAction(() -> popupEmpresa());
		empresa.setCleanValue(() -> filter.setEmpresa(null));
		save = new SimpleBooleanProperty();
		edit = new SimpleBooleanProperty();
		delete = new SimpleBooleanProperty();
		view = new SimpleBooleanProperty();
		columns = findFields(TypeGeneric.COLUMN, ConceptoDTO.class, ConceptoBean.class);
		loadDataModel(paginador, tabla);
		loadColumns();
		visibleButtons();
		ButtonsImpl.Stream(FlowPane.class).setLayout(buttons).setName("fxml.btn.new").action(this::add).icon(Glyph.SAVE)
				.isVisible(save).setName("fxml.btn.edit").action(this::set).icon(Glyph.SAVE).isVisible(edit)
				.setName("fxml.btn.delete").action(this::del).icon(Glyph.REMOVE).isVisible(delete)
				.setName("fxml.btn.view").action(this::set).icon(Glyph.FILE_TEXT).isVisible(view).build();
	}

	public void clickTable() {
		visibleButtons();
	}

	public final void popupEstado() {
		try {
			controllerGenPopup(ParametroDTO.class).setWidth(350)
					.addDefaultValuesToGenericParametrized(ParametroConstants.FIELD_NAME_GROUP,
							ParametroConstants.GRUPO_ESTADO_CONCEPTO)
					.load("#{ConceptoBean.estado}");
		} catch (Exception e) {
			error(e);
		}
	}

	public final void setEstado(ParametroDTO parametro) {
		registro.setEstado(parametro);
		estado.setText(parametro.getDescripcion());
	}

	public final void popupEmpresa() {
		try {
			controllerGenPopup(EmpresaDTO.class).setWidth(350).load("#{ConceptoBean.empresa}");
		} catch (Exception e) {
			error(e);
		}
	}

	public final void setEmpresa(EmpresaDTO empresa) {
		registro.setEmpresa(empresa);
		this.empresa.setText(empresa.getNombre());
	}

	public void add() {
		getController(ConceptoCRUBean.class);
	}

	public void search() {
		dataTable.search();
	}

	public void del() {
		try {
			controllerPopup(ConfirmPopupBean.class).load("#{ConceptoBean.delete}",
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
				documentoSvc.delete(registro, getUsuario());
				notificarI18n("mensaje.concept.have.been.deleted");
				dataTable.search();
			} else {
				notificarI18n("mensaje.concept.havent.been.selected");
			}
		} catch (DocumentosException e) {
			error(e);
		}
	}

	public void set() {
		registro = dataTable.getSelectedRow();
		if (registro != null) {
			getController(ConceptoCRUBean.class).load(registro);
		} else {
			notificarI18n("err.concept.wasnt.selected");
		}
	}

	public void visibleButtons() {
		var save = PermissionUtil.INSTANCE().havePerm(PermissionConstants.CONST_PERM_CREATE, ConceptoBean.class,
				getUsuario().getGrupoUser());
		var edit = dataTable.isSelected() && PermissionUtil.INSTANCE().havePerm(PermissionConstants.CONST_PERM_UPDATE,
				ConceptoBean.class, getUsuario().getGrupoUser());
		var delete = dataTable.isSelected() && PermissionUtil.INSTANCE().havePerm(PermissionConstants.CONST_PERM_DELETE,
				ConceptoBean.class, getUsuario().getGrupoUser());
		var view = !save && PermissionUtil.INSTANCE().havePerm(PermissionConstants.CONST_PERM_READ, ConceptoBean.class,
				getUsuario().getGrupoUser());
		this.save.setValue(save);
		this.edit.setValue(edit);
		this.delete.setValue(delete);
		this.view.setValue(view);
	}

	public Boolean isSelected() {
		return dataTable.isSelected();
	}

	@Override
	public void selectedRow(MouseEvent eventHandler) {
		visibleButtons();
	}

	@Override
	public TableView<ConceptoDTO> getTableView() {
		return tabla;
	}

	@Override
	public Integer getMaxColumns(TypeGeneric typeGeneric) {
		return 4;
	}

	@Override
	public List<ConfigGenericFieldDTO> getListGenericsFields(TypeGeneric typeGeneric) {
		return columns;
	}

	@Override
	public Class<ConceptoDTO> getClazz() {
		return ConceptoDTO.class;
	}

	@Override
	public GridPane getGridPane(TypeGeneric typeGeneric) {
		return null;
	}

	@Override
	public ConceptoDTO getFilterToTable(ConceptoDTO filter) {
		return this.filter;
	}
}
