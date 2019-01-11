package org.pyt.app.beans.cuentaContable;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.pyt.common.annotations.Inject;
import org.pyt.common.common.ABean;
import org.pyt.common.common.SelectList;
import org.pyt.common.constants.ParametroConstants;
import org.pyt.common.exceptions.CuentaContableException;
import org.pyt.common.exceptions.EmpresasException;
import org.pyt.common.exceptions.ParametroException;

import com.pyt.service.dto.CuentaContableDTO;
import com.pyt.service.dto.EmpresaDTO;
import com.pyt.service.dto.ParametroDTO;
import com.pyt.service.interfaces.ICuentaContableSvc;
import com.pyt.service.interfaces.IEmpresasSvc;
import com.pyt.service.interfaces.IParametrosSvc;

import co.com.arquitectura.annotation.proccessor.FXMLFile;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;

/**
 * Se encarga de procesar la pantalla de creacion y actualizacion de un concepto
 * 
 * @author Alejandro Parra
 * @since 2018-05-22
 */
@FXMLFile(path = "view/cuentaContable", file = "cuentaContable.fxml")
public class CuentaContableCRUBean extends ABean<CuentaContableDTO> {
	@Inject(resource = "com.pyt.service.implement.CuentaContableSvc")
	private ICuentaContableSvc cuentaContableSvc;
	@Inject(resource = "com.pyt.service.implement.ParametrosSvc")
	private IParametrosSvc parametroSvc;
	@Inject(resource = "com.pyt.service.implement.EmpresaSvc")
	private IEmpresasSvc empresaSvc;
	@FXML
	private Label codigo;
	@FXML
	private TextField nombre;
	@FXML
	private TextField asociado;
	@FXML
	private TextField codigoCuenta;
	@FXML
	private ChoiceBox<String> naturaleza;
	@FXML
	private ChoiceBox<String> tipoPlanContable;
	@FXML
	private ChoiceBox<String> tipo;
	@FXML
	private ChoiceBox<String> empresa;
	@FXML
	private Label titulo;
	@FXML
	private BorderPane pane;
	private List<ParametroDTO> listNaturaleza;
	private List<ParametroDTO> listTipoPlanContable;
	private List<EmpresaDTO> listEmpresa;
	private List<ParametroDTO> listTipo;

	@FXML
	public void initialize() {
		NombreVentana = "Agregando Nueva Cuenta Contable";
		titulo.setText(NombreVentana);
		registro = new CuentaContableDTO();
		ParametroDTO estados = new ParametroDTO();
		try {
			listTipoPlanContable = parametroSvc.getAllParametros(estados, ParametroConstants.GRUPO_TIPO_PLAN_CONTABLE);
			listNaturaleza = parametroSvc.getAllParametros(estados, ParametroConstants.GRUPO_NATURALEZA);
			listTipo = parametroSvc.getAllParametros(estados, ParametroConstants.GRUPO_TIPO);
			logger.logger("Se encontraron "+listTipo.size()+" registrosde tipo.");
			listEmpresa = empresaSvc.getAllEmpresas(new EmpresaDTO());
		} catch (ParametroException e) {
			error(e);
		} catch (EmpresasException e) {
			error(e);
		}
		SelectList.put(tipoPlanContable, listTipoPlanContable, "nombre");
		tipoPlanContable.getSelectionModel().selectFirst();
		SelectList.put(tipo, listTipo, "nombre");
		tipo.getSelectionModel().selectFirst();
		SelectList.put(naturaleza, listNaturaleza, "nombre");
		naturaleza.getSelectionModel().selectFirst();
		SelectList.put(empresa, listEmpresa, "nombre");
		empresa.getSelectionModel().selectFirst();
	}

	/**
	 * Pasa los campos de la pantalla el objeto dto
	 */
	private void load() {
		if (registro == null) {
			registro = new CuentaContableDTO();
		}
		registro.setNombre(nombre.getText());
		registro.setAsociado(asociado.getText());
		registro.setCodigoCuenta(codigoCuenta.getText());
		registro.setNaturaleza(SelectList.get(naturaleza, listNaturaleza, "nombre"));
		registro.setTipoPlanContable(SelectList.get(tipoPlanContable, listTipoPlanContable, "nombre"));
		registro.setTipo(SelectList.get(tipo, listTipo, "nombre"));
		registro.setEmpresa(SelectList.get(empresa,listEmpresa,"nombre"));
	}

	private void loadFxml() {
		if (registro == null)
			return;
		codigo.setText(registro.getCodigo());
		nombre.setText(registro.getNombre());
		asociado.setText(registro.getAsociado());
		codigoCuenta.setText(registro.getCodigoCuenta());
		SelectList.put(tipoPlanContable, listTipoPlanContable, "nombre");
		SelectList.selectItem(tipoPlanContable, listTipoPlanContable, "nombre", registro.getTipoPlanContable());
		SelectList.put(naturaleza, listNaturaleza, "nombre");
		SelectList.selectItem(naturaleza, listNaturaleza, "nombre", registro.getNaturaleza());
		SelectList.put(tipo, listTipo, "nombre");
		SelectList.selectItem(tipo, listTipo, "nombre", registro.getTipo());
		SelectList.put(empresa, listEmpresa, "nombre");
		SelectList.selectItem(empresa, listEmpresa, "nombre", registro.getEmpresa());
	}

	public void load(CuentaContableDTO dto) {
		if (dto != null && dto.getCodigo() != null) {
			registro = dto;
			loadFxml();
			titulo.setText("Modificando Cuenta Contable");
		} else {
			error("La cuenta contable es invalido para editar.");
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
//		valid &= StringUtils.isNotBlank(registro.getAsociado());
		valid &= StringUtils.isNotBlank(registro.getCodigoCuenta());
		valid &= StringUtils.isNotBlank(registro.getNaturaleza().getCodigo());
		valid &= StringUtils.isNotBlank(registro.getTipoPlanContable().getCodigo());
		valid &= StringUtils.isNotBlank(registro.getTipo().getCodigo());
		valid &= StringUtils.isNotBlank(registro.getEmpresa().getCodigo());
		return valid;
	}

	public void add() {
		load();
		try {
			if (valid()) {
				if (StringUtils.isNotBlank(registro.getCodigo())) {
					cuentaContableSvc.update(registro, userLogin);
					notificar("Se guardo la cuenta contable correctamente.");
					cancel();
				} else {
					cuentaContableSvc.insert(registro, userLogin);
					codigo.setText(registro.getCodigo());
					notificar("Se agrego la cuenta contable correctamente.");
					cancel();
				}
			}
		} catch (CuentaContableException e) {
			error(e);
		}
	}

	public void cancel() {
		getController(CuentaContableBean.class);
	}

}
