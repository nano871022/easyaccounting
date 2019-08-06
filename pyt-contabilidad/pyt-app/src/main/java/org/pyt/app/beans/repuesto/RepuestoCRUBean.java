package org.pyt.app.beans.repuesto;

import java.math.BigDecimal;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.pyt.common.annotations.Inject;
import org.pyt.common.common.SelectList;
import org.pyt.common.constants.ParametroConstants;
import org.pyt.common.exceptions.ParametroException;
import org.pyt.common.exceptions.inventario.ProductosException;
import org.pyt.common.exceptions.inventario.ResumenProductoException;
import org.pyt.common.exceptions.validates.ValidateValueException;
import org.pyt.common.validates.ValidateValues;

import com.pyt.service.dto.ParametroDTO;
import com.pyt.service.dto.inventario.ProductoDTO;
import com.pyt.service.dto.inventario.ResumenProductoDTO;
import com.pyt.service.interfaces.IParametrosSvc;
import com.pyt.service.interfaces.inventarios.IProductosSvc;

import co.com.arquitectura.annotation.proccessor.FXMLFile;
import co.com.japl.ea.beans.ABean;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.util.StringConverter;

/**
 * Se encarga de procesar la pantalla de creacion y actualizacion de una empresa
 * 
 * @author Alejandro Parra
 * @since 2018-05-22
 */
@FXMLFile(path = "view/repuesto", file = "repuesto.fxml")
public class RepuestoCRUBean extends ABean<ResumenProductoDTO> {
	@Inject(resource = "com.pyt.service.implement.inventario.ProductosSvc")
	private IProductosSvc productosSvc;
	@Inject(resource = "com.pyt.service.implement.ParametrosSvc")
	private IParametrosSvc parametrosSvc;
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
	private TextField cantidad;
	@FXML
	private TextField percentGanancia;
	@FXML
	private TextArea descripcion;
	@FXML
	private Label titulo;
	@FXML
	private BorderPane pane;
	@FXML
	private ChoiceBox<ParametroDTO> choiceBoxIva;
	private ValidateValues vv;
	private List<ParametroDTO> listIva;

	@FXML
	public void initialize() {
		NombreVentana = "Agregando Nuevo Repuesto";
		titulo.setText(NombreVentana);
		registro = new ResumenProductoDTO();
		try {
			listIva = parametrosSvc.getAllParametros(new ParametroDTO(), ParametroConstants.GRUPO_IVA);
		} catch (ParametroException e) {
			error(e);
		}
		vv = new ValidateValues();
		SelectList.put(choiceBoxIva, listIva);
		choiceBoxIva.converterProperty().set(new StringConverter<ParametroDTO>() {
			@Override
			public String toString(ParametroDTO object) {
				return object.getNombre();
			}

			@Override
			public ParametroDTO fromString(String string) {
				for (ParametroDTO param : listIva) {
					if (param.getNombre().contains(string)) {
						return param;
					}
				}
				return null;
			}
		});
		percentGanancia.focusedProperty().addListener((obs, oldval, newval) -> {
			try {
				BigDecimal ganancia = vv.cast(percentGanancia.getText(), BigDecimal.class);
				BigDecimal percent = ganancia.divide(new BigDecimal(100));
				BigDecimal valor = registro.getValorCompra().multiply(percent);
				BigDecimal venta = registro.getValorCompra().add(valor);
				valorVenta.setText(venta.toString());
			} catch (Exception ex) {
				error(ex);
			}
		});
		valorVenta.focusedProperty().addListener((obs, oldval, newval) -> {
			try {
				BigDecimal valor = vv.cast(valorVenta.getText(), BigDecimal.class);
				BigDecimal dif = valor.subtract(registro.getValorCompra());
				BigDecimal percent = dif.divide(registro.getValorCompra());
				percent = percent.multiply(new BigDecimal(100));
				percentGanancia.setText(percent.toString());
			} catch (Exception ex) {
				error(ex);
			}
		});
	}

	/**
	 * Pasa los campos de la pantalla el objeto dto
	 */
	private void fxmlLoad() {
		if (registro == null) {
			registro = new ResumenProductoDTO();
			registro.setProducto(new ProductoDTO());
		}
		registro.setCodigo(codigo.getText());
		registro.getProducto().setNombre(nombre.getText());
		registro.getProducto().setReferencia(referencia.getText());
		registro.getProducto().setDescripcion(descripcion.getText());
		registro.setIvaPercentAplicarVenta(SelectList.get(choiceBoxIva));
		try {
			registro.setValorCompra(vv.cast(valorMercado.getText(), BigDecimal.class));
			registro.setValorVenta(vv.cast(valorVenta.getText(), BigDecimal.class));
			registro.setCantidad(vv.cast(cantidad.getText(), Integer.class));
			registro.setGananciaPercentVenta(vv.cast(percentGanancia.getText(), Long.class));
		} catch (ValidateValueException e) {
			error(e);
		}
	}

	private void loadFxml() {
		if (registro == null)
			return;
		codigo.setText(registro.getCodigo());
		nombre.setText(registro.getProducto().getNombre());
		SelectList.selectItem(choiceBoxIva, registro.getIvaPercentAplicarVenta());
		if (registro.getValorCompra() != null) {
			valorMercado.setText(String.valueOf(registro.getValorCompra()));
		}
		if (registro.getValorVenta() != null) {
			valorVenta.setText(String.valueOf(registro.getValorVenta()));
		}
		referencia.setText(String.valueOf(registro.getProducto().getReferencia()));
		if (registro.getCantidad() != null) {
			cantidad.setText(String.valueOf(registro.getCantidad()));
		}
		if (registro.getGananciaPercentVenta() != null) {
			percentGanancia.setText(String.valueOf(registro.getGananciaPercentVenta()));
		}
		descripcion.setText(registro.getProducto().getDescripcion());
	}

	public final void load() {
		registro = new ResumenProductoDTO();
		registro.setProducto(new ProductoDTO());
	}

	private void getResumenProducto(ProductoDTO dto) {
		try {
			registro = productosSvc.resumenProducto(dto);
		} catch (ResumenProductoException e) {
			registro = new ResumenProductoDTO();
			registro.setProducto(dto);
		}
	}

	public void load(ProductoDTO dto) {
		if (dto != null && dto.getCodigo() != null) {
			getResumenProducto(dto);
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
		valid &= StringUtils.isNotBlank(registro.getProducto().getNombre());
		return valid;
	}

	public void add() {
		fxmlLoad();
		try {
			if (valid()) {
				if (StringUtils.isNotBlank(registro.getCodigo())) {
					productosSvc.update(registro.getProducto(), userLogin);
					productosSvc.update(registro, userLogin);
					notificar("Se guardo el repuesto correctamente.");
					cancel();
				} else {
					ProductoDTO prod = productosSvc.insert(registro.getProducto(), userLogin);
					registro.setProducto(prod);
					productosSvc.insert(registro, userLogin);
					codigo.setText(registro.getCodigo());
					notificar("Se agrego el repuesto correctamente.");
					cancel();
				}
			}
		} catch (ResumenProductoException e) {
			error(e);
		} catch (ProductosException e) {
			error(e);
		}
	}

	public final void agregarMovimiento() {
		try {
			controllerPopup(PopupAgregarMovimientoBean.class).load("#{RepuestoCRUBean.update}", registro.getProducto());
		} catch (Exception e) {
			error(e);
		}
	}

	public final void update(boolean valid) {
		try {
			if (valid) {
				registro = productosSvc.resumenProducto(registro.getProducto());
				loadFxml();
			}
		} catch (ResumenProductoException e) {
			error(e);
		}
	}

	public void cancel() {
		getController(RepuestoBean.class);
	}

}
