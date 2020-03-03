package org.pyt.app.beans.actividadIca;

import org.apache.commons.lang3.StringUtils;
import org.controlsfx.glyphfont.FontAwesome.Glyph;
import org.pyt.common.annotations.Inject;
import org.pyt.common.exceptions.ActividadIcaException;
import org.pyt.common.validates.ValidFields;

import com.pyt.service.dto.ActividadIcaDTO;
import com.pyt.service.interfaces.IActividadIcaSvc;

import co.com.arquitectura.annotation.proccessor.FXMLFile;
import co.com.japl.ea.beans.abstracts.AGenericInterfacesFieldBean;
import co.com.japl.ea.common.button.apifluid.ButtonsImpl;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;

/**
 * Se encarga de procesar la pantalla de creacion y actualizacion de una
 * actividad ica
 * 
 * @author Alejandro Parra
 * @since 2018-05-22
 */
@FXMLFile(path = "view/actividadIca", file = "actividadIca.fxml")
public class ActividadIcaCRUBean extends AGenericInterfacesFieldBean<ActividadIcaDTO> {
	@Inject(resource = "com.pyt.service.implement.ActividadIcaSvc")
	private IActividadIcaSvc actividadIcaSvc;
	@FXML
	private Label titulo;
	@FXML
	private BorderPane pane;
	@FXML
	private GridPane fields;
	@FXML
	private FlowPane buttons;

	@FXML
	public void initialize() {
		NombreVentana = i18n().valueBundle("fxml.title.add.ica.activity").get();
		titulo.setText(NombreVentana);
		registro = new ActividadIcaDTO();
		save = new SimpleBooleanProperty();
		edit = new SimpleBooleanProperty();
		classTypeDto = ActividadIcaDTO.class;
		loadFields();
		visibleFields(ActividadIcaBean.class);
		ButtonsImpl.Stream(FlowPane.class).setLayout(buttons).setName("fxml.btn.save").action(this::add).isVisible(save)
				.icon(Glyph.SAVE).setName("fxml.btn.edit").action(this::add).isVisible(edit).icon(Glyph.EDIT)
				.setName("fxml.btn.cancel").action(this::cancel).build();
		loadFields(TypeGeneric.FIELD);
	}

	public void load(ActividadIcaDTO dto) {
		if (dto != null && dto.getCodigo() != null) {
			registro = dto;
			visibleFields(ActividadIcaBean.class);
			titulo.setText(i18n("mensaje.title.activiadaica.modify"));
			loadFields(TypeGeneric.FIELD);
		} else {
			errorI18n("err.actividadica.invalid.toedit");
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
		valid &= ValidFields.validI18n(registro.getNombre(), mapFieldUseds.get("nombre").stream().findFirst().get(),
				true, 1, 100, i18n("err.valid.actividadica.field.name.empty"));
		valid &= ValidFields.validI18n(registro.getDescripcion(),
				mapFieldUseds.get("descripcion").stream().findFirst().get(), true, 1, 100,
				i18n("err.valid.actividadica.field.description.empty"));
		valid &= ValidFields.validI18n(registro.getBase(), mapFieldUseds.get("base").stream().findFirst().get(), true,
				1, 30, "err.valid.actividadica.field.base.empty");
		valid &= ValidFields.validI18n(registro.getTarifa(), mapFieldUseds.get("tarifa").stream().findFirst().get(),
				true, 1, 30, "err.valid.actividadica.field.rate.empty");
		valid &= ValidFields.validI18n(registro.getCodigoIca(),
				mapFieldUseds.get("codigoIca").stream().findFirst().get(), true, 1, 30,
				"err.valid.actividadica.field.codeica.empty");
		return valid;
	}

	public void add() {
		try {
			if (valid()) {
				if (StringUtils.isNotBlank(registro.getCodigo())) {
					actividadIcaSvc.update(registro, getUsuario());
					notificarI18n("mensaje.icaactivity,have.been.insert.succesfill");
					cancel();
				} else {
					actividadIcaSvc.insert(registro, getUsuario());
					notificarI18n("menaje.icaactivity.have.been.update.succesfull");
					cancel();
				}
			}
		} catch (ActividadIcaException e) {
			error(e);
		}
	}

	public void cancel() {
		getController(ActividadIcaBean.class);
	}

	@Override
	public GridPane getGridPane(TypeGeneric typeGeneric) {
		return fields;
	}

	@Override
	public Integer getMaxColumns(TypeGeneric typeGeneric) {
		return 4;
	}

}
