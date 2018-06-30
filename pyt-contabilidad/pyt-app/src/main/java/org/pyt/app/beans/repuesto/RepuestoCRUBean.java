package org.pyt.app.beans.repuesto;

import org.apache.commons.lang3.StringUtils;
import org.pyt.common.annotations.FXMLFile;
import org.pyt.common.annotations.Inject;
import org.pyt.common.common.ABean;
import org.pyt.common.common.ValidateValues;
import org.pyt.common.exceptions.RepuestoException;
import org.pyt.common.exceptions.ValidateValueException;

import com.pyt.service.dto.RepuestoDTO;
import com.pyt.service.interfaces.IRepuestosSvc;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;

/**
 * Se encarga de procesar la pantalla de creacion y actualizacion de una empresa
 * 
 * @author Alejandro Parra
 * @since 2018-05-22
 */
@FXMLFile(path = "view/repuesto", file = "repuesto.fxml")
public class RepuestoCRUBean extends ABean<RepuestoDTO> {
	@Inject(resource = "com.pyt.service.implement.RepuestosSvc")
	private IRepuestosSvc repuestoSvc;
	@FXML
	private Label codigo;
	@FXML
	private TextField nombre;
	@FXML
	private TextField porcentajeIva;
	@FXML
	private TextField valorMercado;
	@FXML
	private TextField valorVenta;
	@FXML
	private TextField referencia;
	@FXML
	private Label titulo;
	@FXML
	private BorderPane pane;
	private ValidateValues vv;

	@FXML
	public void initialize() {
		NombreVentana = "Agregando Nuevo Repuesto";
		titulo.setText(NombreVentana);
		registro = new RepuestoDTO();
		vv = new ValidateValues();
	}

	/**
	 * Pasa los campos de la pantalla el objeto dto
	 */
	private void load() {
		if (registro == null) {
			registro = new RepuestoDTO();
		}
		registro.setCodigo(codigo.getText());
		registro.setNombre(nombre.getText());
		registro.setReferencia(referencia.getText());
		try {
			registro.setPorcentajeIva(vv.cast(porcentajeIva.getText(), Long.class));
			registro.setValorMercado(vv.cast(valorMercado.getText(), Long.class));
			registro.setValorVenta(vv.cast(valorVenta.getText(), Long.class));
		} catch (ValidateValueException e) {
			error(e);
		}
	}

	private void loadFxml() {
		if (registro == null)
			return;
		codigo.setText(registro.getCodigo());
		nombre.setText(registro.getNombre());
		porcentajeIva.setText(String.valueOf(registro.getPorcentajeIva()));
		valorMercado.setText(String.valueOf(registro.getValorMercado()));
		valorVenta.setText(String.valueOf(registro.getValorVenta()));
		referencia.setText(String.valueOf(registro.getReferencia()));
	}

	public void load(RepuestoDTO dto) {
		if (dto != null && dto.getCodigo() != null) {
			registro = dto;
			loadFxml();
			titulo.setText("Modificando Repuesto");
		} else {
			error("EL repuesto es invalido para editar.");
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
		return valid;
	}

	public void add() {
		load();
		try {
			if (valid()) {
				if (StringUtils.isNotBlank(registro.getCodigo())) {
					repuestoSvc.update(registro, userLogin);
					notificar("Se guardo el repuesto correctamente.");
					cancel();
				} else {
					repuestoSvc.insert(registro, userLogin);
					codigo.setText(registro.getCodigo());
					notificar("Se agrego el repuesto correctamente.");
					cancel();
				}
			}
		} catch (RepuestoException e) {
			error(e);
		}
	}

	public void cancel() {
		getController(RepuestoBean.class);
	}

}
