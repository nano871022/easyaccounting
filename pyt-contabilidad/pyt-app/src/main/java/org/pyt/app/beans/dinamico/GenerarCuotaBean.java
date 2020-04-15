package org.pyt.app.beans.dinamico;

import static org.pyt.common.constants.InsertResourceConstants.CONST_RESOURCE_IMPL_SVC_CONFIG_GENERIC_FIELD;

import java.time.LocalDate;
import java.time.Period;

import org.apache.commons.lang3.StringUtils;
import org.controlsfx.glyphfont.FontAwesome.Glyph;
import org.pyt.common.annotations.Inject;
import org.pyt.common.constants.PermissionConstants;

import com.pyt.service.dto.DocumentoDTO;
import com.pyt.service.interfaces.IConfigGenericFieldSvc;
import com.pyt.service.interfaces.IGenericServiceSvc;

import co.com.arquitectura.annotation.proccessor.FXMLFile;
import co.com.japl.ea.beans.abstracts.AGenericInterfacesFieldBean;
import co.com.japl.ea.common.button.apifluid.ButtonsImpl;
import co.com.japl.ea.dto.contabilidad.GenerarCuotaDTO;
import co.com.japl.ea.utls.PermissionUtil;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

@FXMLFile(file = "generar_cuota.fxml", path = "view/dinamico")
public class GenerarCuotaBean extends AGenericInterfacesFieldBean<GenerarCuotaDTO> {
	@Inject(resource = "com.pyt.service.implement.GenericServiceSvc")
	private IGenericServiceSvc<GenerarCuotaDTO> quoteSvc;
	@FXML
	private VBox central;
	@FXML
	private Label title;
	@Inject(resource = CONST_RESOURCE_IMPL_SVC_CONFIG_GENERIC_FIELD)
	private IConfigGenericFieldSvc configGenericSvc;
	@FXML
	private HBox buttons;
	private BorderPane panel;
	private GridPane gridPane;

	@FXML
	public void initialize() {
		try {
			registro = new GenerarCuotaDTO();
			central.getChildren().add(gridPane);
			setClazz(GenerarCuotaDTO.class);
			fields = configGenericSvc.getFieldToFields(this.getClass(), GenerarCuotaDTO.class);
			visibleButtons();
			loadFields(TypeGeneric.FIELD);
			ButtonsImpl.Stream(HBox.class).setLayout(buttons).setName("fxml.btn.generate").action(this::generar)
					.icon(Glyph.GEAR).isVisible(save).isVisible(edit).setName("fxml.btn.cancel").action(this::cancel)
					.build();
		} catch (Exception e) {
			error(e);
		}
	}

	public final void load(DocumentoDTO documento) {
		registro = new GenerarCuotaDTO();
		registro.setDocumento(documento);
		registro.setFechaInicio(LocalDate.now().plus(Period.parse("P1W")));
		registro.setNumeroCuotas(1);
		loadFields(TypeGeneric.FIELD);
	}

	@Override
	public GridPane getGridPane(TypeGeneric typeGeneric) {
		return gridPane;
	}

	public final void generar() {
		try {
			if (StringUtils.isBlank(registro.getCodigo())) {
				quoteSvc.insert(registro, getUsuario());
				notificar(i18n().valueBundle("mensaje.generatequote.succesful"));
			}
		} catch (Exception e) {
			error(e);
		}
	}

	public final void cancel() {
		Stage stage = (Stage) panel.getScene().getWindow();
		stage.close();
		destroy();
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
