package org.pyt.app.beans.banco;

import org.apache.commons.lang3.StringUtils;
import org.ea.app.custom.PopupParametrizedControl;
import org.pyt.app.components.PopupGenBean;
import org.pyt.common.annotations.Inject;
import org.pyt.common.constants.ParametroConstants;
import org.pyt.common.exceptions.BancoException;
import org.pyt.common.validates.ValidFields;

import com.pyt.service.dto.BancoDTO;
import com.pyt.service.dto.ParametroDTO;
import com.pyt.service.interfaces.IBancosSvc;
import com.pyt.service.interfaces.IParametrosSvc;

import co.com.arquitectura.annotation.proccessor.FXMLFile;
import co.com.japl.ea.beans.abstracts.ABean;
import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;

/**
 * Se encarga de procesar la pantalla de creacion y actualizacion de una
 * actividad ica
 * 
 * @author Alejandro Parra
 * @since 2018-05-22
 */
@FXMLFile(path = "view/banco", file = "banco.fxml")
public class BancoCRUBean extends ABean<BancoDTO> {

	@Inject(resource = "com.pyt.service.implement.BancoSvc")
	private IBancosSvc bancoSvc;
	@Inject(resource = "com.pyt.service.implement.ParametrosSvc")
	private IParametrosSvc parametrosSvc;
	@FXML
	private Label codigo;
	@FXML
	private TextField nombre;
	@FXML
	private TextField descripcion;
	@FXML
	private TextField numeroCuenta;
	@FXML
	private PopupParametrizedControl tipoBanco;
	@FXML
	private PopupParametrizedControl tipoCuenta;
	@FXML
	private PopupParametrizedControl estado;
	@FXML
	private DatePicker fechaApertura;
	@FXML
	private DatePicker fechaCierre;
	@FXML
	private Label titulo;
	@FXML
	private BorderPane pane;

	@FXML
	public void initialize() {
		NombreVentana = i18n().valueBundle("fxml.title.add.bank").get();
		titulo.setText(NombreVentana);
		registro = new BancoDTO();
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
	}

	/**
	 * Pasa los campos de la pantalla el objeto dto
	 */
	private void load() {
		if (registro == null) {
			registro = new BancoDTO();
		}
		registro.setCodigo(codigo.getText());
		registro.setNombre(nombre.getText());
		registro.setDescripcion(descripcion.getText());
		registro.setNumeroCuenta(numeroCuenta.getText());
		if (fechaApertura != null) {
			registro.setFechaApertura(fechaApertura.getValue());
		}
		if (fechaCierre != null) {
			registro.setFechaCierre(fechaCierre.getValue());
		}
	}

	private void loadFxml() {
		if (registro == null)
			return;
		codigo.setText(registro.getCodigo());
		nombre.setText(registro.getNombre());
		descripcion.setText(registro.getDescripcion());
		numeroCuenta.setText(registro.getNumeroCuenta());
		fechaApertura.setValue(registro.getFechaApertura());
		fechaCierre.setValue(registro.getFechaCierre());
		tipoBanco.setText(registro.getTipoBanco().getDescripcion());
		tipoCuenta.setText(registro.getTipoCuenta().getDescripcion());
		estado.setText(registro.getEstado().getDescripcion());
	}

	public void load(BancoDTO dto) {
		if (dto != null && dto.getCodigo() != null) {
			registro = dto;
			loadFxml();
			titulo.setText("Modificando Banco");
		} else {
			error("EL banco es invalido para editar.");
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
				i18n().valueBundle("err.valid.banco.field.nombre.empty"));
		valid &= ValidFields.valid(registro.getDescripcion(), descripcion, true, 1, 100,
				i18n().valueBundle("err.valid.banco.field.description.empty"));
		valid &= ValidFields.valid(registro.getNumeroCuenta(), numeroCuenta, true, 6, 20,
				i18n().valueBundle("err.valid.banco.field.numerocuota.empty"));
		valid &= ValidFields.valid(registro.getFechaApertura(), fechaApertura, true, null, null,
				i18n().valueBundle("err.valid.banco.field.fechaapertura.empty"));
		valid &= ValidFields.valid(registro.getTipoBanco(), tipoBanco, true, null, null,
				i18n().valueBundle("err.valid.banco.field.tipobanco.empty"));
		valid &= ValidFields.valid(registro.getTipoCuenta(), tipoCuenta, true, null, null,
				i18n().valueBundle("err.valid.banco.field.tipocuenta.empty"));
		valid &= ValidFields.valid(registro.getEstado(), estado, true, null, null,
				i18n().valueBundle("err.valid.banco.field.estado.empty"));
		return valid;
	}

	public final void popupOpenTipoBanco() {
		try {
			controllerPopup(new PopupGenBean<ParametroDTO>(ParametroDTO.class))
					.addDefaultValuesToGenericParametrized(ParametroConstants.FIELD_NAME_GROUP,
							ParametroConstants.GRUPO_TIPO_BANCO)
					.load("#{BancoCRUBean.tipoBanco}");
		} catch (Exception e) {
			error(e);
		}
	}

	public final void setTipoBanco(ParametroDTO parametro) {
		registro.setTipoBanco(parametro);
		tipoBanco.setText(parametro.getNombre());
	}

	public final void popupOpenTipoCuentas() {
		try {
			controllerPopup(new PopupGenBean<ParametroDTO>(ParametroDTO.class))
					.addDefaultValuesToGenericParametrized(ParametroConstants.FIELD_NAME_GROUP,
							ParametroConstants.GRUPO_TIPO_CUENTA)
					.load("#{BancoCRUBean.tipoCuentas}");
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
					.load("#{BancoCRUBean.estado}");
		} catch (Exception e) {
			error(e);
		}
	}

	public final void setEstado(ParametroDTO parametro) {
		registro.setEstado(parametro);
		estado.setText(parametro.getDescripcion());
	}

	public void add() {
		load();
		try {
			if (valid()) {
				if (StringUtils.isNotBlank(registro.getCodigo())) {
					bancoSvc.update(registro, getUsuario());
					notificarI18n("mensaje.banco.insert.success");
					cancel();
				} else {
					bancoSvc.insert(registro, getUsuario());
					codigo.setText(registro.getCodigo());
					notificarI18n("mensaje.banco.update.success");
					cancel();
				}
			}
		} catch (BancoException e) {
			error(e);
		}
	}

	public void cancel() {
		getController(BancoBean.class);
	}

}
