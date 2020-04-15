package org.pyt.app.beans.dinamico;

import static org.pyt.common.constants.InsertResourceConstants.CONST_RESOURCE_IMPL_SVC_CONFIG_GENERIC_FIELD;

import org.apache.commons.lang3.StringUtils;
import org.controlsfx.glyphfont.FontAwesome.Glyph;
import org.pyt.common.annotations.Inject;
import org.pyt.common.constants.PermissionConstants;

import com.pyt.service.interfaces.IConfigGenericFieldSvc;
import com.pyt.service.interfaces.IGenericServiceSvc;

import co.com.arquitectura.annotation.proccessor.FXMLFile;
import co.com.japl.ea.beans.abstracts.AGenericInterfacesFieldBean;
import co.com.japl.ea.common.button.apifluid.ButtonsImpl;
import co.com.japl.ea.dto.contabilidad.CuotaDTO;
import co.com.japl.ea.utls.PermissionUtil;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

@FXMLFile(file = "cuota.fxml", path = "view/dinamico")
public class CuotaBean extends AGenericInterfacesFieldBean<CuotaDTO> {
	@Inject(resource = "com.pyt.service.implement.GenericServiceSvc")
	private IGenericServiceSvc<CuotaDTO> quoteSvc;
	@FXML
	private VBox central;
	@FXML
	private Label title;
	@Inject(resource = CONST_RESOURCE_IMPL_SVC_CONFIG_GENERIC_FIELD)
	private IConfigGenericFieldSvc configGenericSvc;
	@FXML
	private HBox buttons;
	private GridPane gridPane;

	@FXML
	public void initialize() {
		try {
			registro = new CuotaDTO();
			central.getChildren().add(gridPane);
			setClazz(CuotaDTO.class);
			fields = configGenericSvc.getFieldToFields(this.getClass(), CuotaDTO.class);
			visibleButtons();
			loadFields(TypeGeneric.FIELD);
			ButtonsImpl.Stream(HBox.class).setLayout(buttons).setName("fxml.btn.save").action(this::insert)
					.icon(Glyph.SAVE).isVisible(save).setName("fxml.btn.edit").action(this::update).icon(Glyph.EDIT)
					.isVisible(edit).setName("fxml.btn.cancel").action(this::cancel).build();
		} catch (Exception e) {
			error(e);
		}
	}

	public final void load() {
		registro = new CuotaDTO();
		loadFields(TypeGeneric.FIELD);
	}

	public final void load(CuotaDTO dto) {
		registro = dto;
		visibleButtons();
		loadFields(TypeGeneric.FIELD);
	}

	@Override
	public GridPane getGridPane(TypeGeneric typeGeneric) {
		return gridPane;
	}

	public final void insert() {
		try {
			if (StringUtils.isBlank(registro.getCodigo())) {
				quoteSvc.insert(registro, getUsuario());
				notificar(i18n().valueBundle("mensaje.group.user.inserted"));
			}
		} catch (Exception e) {
			error(e);
		}
	}

	public final void update() {
		try {
			if (StringUtils.isNotBlank(registro.getCodigo())) {
				{
					quoteSvc.update(registro, getUsuario());
					notificar(i18n().valueBundle("mensaje.group.user.updated"));
				}
			}
		} catch (Exception e) {
			error(e);
		}
	}

	public final void cancel() {
		getController(ListCuotaBean.class);
	}

	@Override
	public Integer getMaxColumns(TypeGeneric typeGeneric) {
		return 2;
	}

	@Override
	protected void visibleButtons() {
		var save = PermissionUtil.INSTANCE().havePerm(PermissionConstants.CONST_PERM_CREATE, ListCuotaBean.class,
				getUsuario().getGrupoUser());
		var edit = PermissionUtil.INSTANCE().havePerm(PermissionConstants.CONST_PERM_UPDATE, ListCuotaBean.class,
				getUsuario().getGrupoUser());
		this.save.setValue(save);
		this.edit.setValue(edit);
	}

}
