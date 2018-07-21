package org.pyt.app.beans.banco;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.pyt.common.annotations.FXMLFile;
import org.pyt.common.annotations.Inject;
import org.pyt.common.common.ABean;
import org.pyt.common.common.SelectList;
import org.pyt.common.constants.ParametroConstants;
import org.pyt.common.exceptions.BancoException;
import org.pyt.common.exceptions.ParametroException;

import com.pyt.service.dto.BancoDTO;
import com.pyt.service.dto.ParametroDTO;
import com.pyt.service.interfaces.IBancosSvc;
import com.pyt.service.interfaces.IParametrosSvc;

import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
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
	private ChoiceBox<String> tipoBanco;
	@FXML
	private ChoiceBox<String> tipoCuenta;
	@FXML
	private ChoiceBox<String> estado;
	@FXML
	private DatePicker fechaApertura;
	@FXML
	private DatePicker fechaCierre;
	@FXML
	private Label titulo;
	@FXML
	private BorderPane pane;
	private List<ParametroDTO> estados;
	private List<ParametroDTO> tipoBancos;
	private List<ParametroDTO> tipoCuentas;

	@FXML
	public void initialize() {
		NombreVentana = "Agregando Nuevo Banco";
		titulo.setText(NombreVentana);
		registro = new BancoDTO();
		ParametroDTO pCuentas = new ParametroDTO();
		ParametroDTO pBancos = new ParametroDTO();
		ParametroDTO pEstados = new ParametroDTO();
		try {
			tipoCuentas = parametrosSvc.getAllParametros(pCuentas,ParametroConstants.GRUPO_TIPO_CUENTA);
			tipoBancos = parametrosSvc.getAllParametros(pBancos,ParametroConstants.GRUPO_TIPO_BANCO);
			estados = parametrosSvc.getAllParametros(pEstados,ParametroConstants.GRUPO_ESTADO_BANCO);
		} catch (ParametroException e) {
			error(e);
		}
		tipoCuenta.getSelectionModel().selectFirst();
		tipoBanco.getSelectionModel().selectFirst();
		estado.getSelectionModel().selectFirst();
		SelectList.put(tipoCuenta, tipoCuentas, "nombre");
		SelectList.put(tipoBanco, tipoBancos, "nombre");
		SelectList.put(estado, estados, "nombre");
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
		registro.setTipoBanco(SelectList.get(tipoBanco, tipoBancos, "nombre"));
		registro.setTipoCuenta(SelectList.get(tipoCuenta, tipoCuentas, "nombre"));
		registro.setEstado(SelectList.get(estado, estados, "nombre"));
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
		SelectList.selectItem(tipoBanco, tipoBancos, "nombre", registro.getTipoBanco());
		SelectList.selectItem(tipoCuenta, tipoCuentas, "nombre", registro.getTipoCuenta());
		SelectList.selectItem(estado, estados, "nombre", registro.getEstado());
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
		valid &= StringUtils.isNotBlank(registro.getNombre());
		valid &= StringUtils.isNotBlank(registro.getDescripcion());
		valid &= StringUtils.isNotBlank(registro.getNumeroCuenta());
		valid &= registro.getFechaApertura() != null;
		valid &= StringUtils.isNotBlank(registro.getTipoBanco().getCodigo());
		valid &= StringUtils.isNotBlank(registro.getTipoCuenta().getCodigo());
		valid &= StringUtils.isNotBlank(registro.getEstado().getCodigo());
		return valid;
	}

	public void add() {
		load();
		try {
			if (valid()) {
				if (StringUtils.isNotBlank(registro.getCodigo())) {
					bancoSvc.update(registro, userLogin);
					notificar("Se guardo el banco correctamente.");
					cancel();
				} else {
					bancoSvc.insert(registro, userLogin);
					codigo.setText(registro.getCodigo());
					notificar("Se agrego el banco correctamente.");
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
