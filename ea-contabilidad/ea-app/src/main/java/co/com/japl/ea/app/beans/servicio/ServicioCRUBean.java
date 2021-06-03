package co.com.japl.ea.app.beans.servicio;

import org.apache.commons.lang3.StringUtils;
import org.controlsfx.glyphfont.FontAwesome.Glyph;
import org.pyt.common.annotations.Inject;
import org.pyt.common.common.DtoUtils;
import org.pyt.common.constants.PermissionConstants;

import co.com.arquitectura.annotation.proccessor.FXMLFile;
import co.com.japl.ea.beans.abstracts.AGenericInterfacesFieldBean;
import co.com.japl.ea.common.button.apifluid.ButtonsImpl;
import co.com.japl.ea.common.validates.ValidFields;
import co.com.japl.ea.common.validates.ValidateValues;
import co.com.japl.ea.dto.dto.ServicioDTO;
import co.com.japl.ea.dto.interfaces.IServiciosSvc;
import co.com.japl.ea.exceptions.ServiciosException;
import co.com.japl.ea.utls.PermissionUtil;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;

/**
 * Se encarga de procesar la pantalla de creacion y actualizacion de una empresa
 * 
 * @author Alejandro Parra
 * @since 2018-05-22
 */
@FXMLFile(path = "view/servicio", file = "servicio.fxml")
public class ServicioCRUBean extends AGenericInterfacesFieldBean<ServicioDTO> {
	@Inject(resource = "com.pyt.service.implement.ServiciosSvc")
	private IServiciosSvc servicioSvc;
	@FXML
	private Label titulo;
	@FXML
	private BorderPane pane;
	private ValidateValues vv;
	@FXML
	private HBox buttons;
	@FXML
	private GridPane fields;

	@FXML
	public void initialize() {
		NombreVentana = i18n().get("fxml.title.add.new.service");
		titulo.setText(NombreVentana);
		registro = new ServicioDTO();
		vv = new ValidateValues();
		setClazz(ServicioDTO.class);
		loadFields();
		loadFields(TypeGeneric.FIELD);
		visibleButtons();
		ButtonsImpl.Stream(HBox.class).setLayout(buttons).setName("fxml.btn.save").action(this::add).icon(Glyph.SAVE)
				.isVisible(save).setName("fxml.btn.edit").action(this::add).icon(Glyph.SAVE).isVisible(save)
				.setName("fxml.btn.close").action(this::cancel).build();
	}

	public void load(ServicioDTO dto) {
		if (dto != null && dto.getCodigo() != null) {
			registro = dto;
			titulo.setText(i18n().get("mensaje.modifing.service"));
			loadFields(TypeGeneric.FIELD);
			visibleButtons();
		} else {
			error(i18n().get("err.service.cant.edit"));
			cancel();
		}
	}

	private Boolean valid() {
		Boolean valid = true;
		valid &= ValidFields.valid(registro.getNombre(), mapFieldUseds.get("nombre").stream().findFirst().get(), true,
				1, 100, i18n().valueBundle("err.service.field.name.empty"));
		valid &= ValidFields.valid(registro.getDescripcion(),
				mapFieldUseds.get("descripcion").stream().findFirst().get(), true, 1, 100,
				i18n().valueBundle("err.service.field.description.empty"));
		valid &= ValidFields.valid(registro.getValorManoObra(),
				mapFieldUseds.get("valorManoObra").stream().findFirst().get(), true, null, null,
				i18n().valueBundle("err.service.field.value.empty"));
		return valid;
	}

	public void add() {
		try {
			if (valid()) {
				if (StringUtils.isNotBlank(registro.getCodigo())) {
					servicioSvc.update(registro, getUsuario());
					notificarI18n("mensaje.service.have.been.updated.succefull");
					cancel();
				} else {
					servicioSvc.insert(registro, getUsuario());
					notificarI18n("mensaje.service.have.been.inserted.succefull");
					cancel();
				}
			}
		} catch (ServiciosException e) {
			error(e);
		}
	}

	public void cancel() {
		getController(ServicioBean.class);
	}

	@Override
	protected void visibleButtons() {
		var save = !DtoUtils.haveCode(registro) && PermissionUtil.INSTANCE()
				.havePerm(PermissionConstants.CONST_PERM_CREATE, ServicioBean.class, getUsuario().getGrupoUser());
		var edit = DtoUtils.haveCode(registro) && PermissionUtil.INSTANCE()
				.havePerm(PermissionConstants.CONST_PERM_UPDATE, ServicioBean.class, getUsuario().getGrupoUser());
		this.save.setValue(save);
		this.edit.setValue(edit);
	}

	@Override
	public GridPane getGridPane(TypeGeneric typeGeneric) {
		return fields;
	}

	@Override
	public Integer getMaxColumns(TypeGeneric typeGeneric) {
		return 2;
	}

}
