package org.pyt.app.beans.repuesto;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.pyt.app.components.ConfirmPopupBean;
import org.pyt.app.components.DataTableFXML;
import org.pyt.common.annotations.Inject;
import org.pyt.common.common.ABean;
import org.pyt.common.common.LoadAppFxml;
import org.pyt.common.common.ValidateValues;
import org.pyt.common.exceptions.inventario.ProductosException;

import com.pyt.service.dto.inventario.MovimientoDto;
import com.pyt.service.dto.inventario.ProductoDto;
import com.pyt.service.interfaces.inventarios.IProductosSvc;

import co.com.arquitectura.annotation.proccessor.FXMLFile;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

/**
 * Bean encargado de crear las empresas
 * 
 * @author alejandro parra
 * @since 07/05/2018
 */
@FXMLFile(path = "view/repuesto", file = "addmovimiento.fxml", nombreVentana = "Agregar Movimiento")
public class PopupAgregarMovimientoBean extends ABean<MovimientoDto> {
	@Inject(resource = "com.pyt.service.implement.inventario.ProductosSvc")
	private IProductosSvc productosSvc;
	@FXML
	private TextField nombre;
	@FXML
	private TextField ref;
	@FXML
	private TextField cantidad;
	@FXML
	private TextField valorUnidad;
	@FXML
	private TextField valorTotal;
	@FXML
	private BorderPane panel;
	@Inject
	private ValidateValues vv;

	@FXML
	public void initialize() {
		NombreVentana = "Lista de Repuestos";
		registro = new MovimientoDto();
		valorUnidad.focusedProperty().addListener((obs, old, news) -> {
			try {
				BigDecimal valorU = vv.cast(valorUnidad.getText(), BigDecimal.class);
				BigDecimal cant = vv.cast(cantidad.getText(), BigDecimal.class);
				if (valorU != null && cant != null) {
					BigDecimal total = valorU.multiply(cant);
					valorTotal.setText(total.toString());
				}
			} catch (Exception e) {
				error(e);
			}
		});
		cantidad.setText("");
		valorTotal.setText("");
		valorUnidad.setText("");
	}

	public final void load(ProductoDto producto) throws Exception {
		if (producto == null)
			throw new Exception("El producto suministrado esta vacio.");
		if(StringUtils.isBlank(producto.getCodigo())) throw new Exception("El producto suministrado no ha sido registraddo.");
		registro.setProducto(producto);
		nombre.setText(producto.getNombre());
		ref.setText(producto.getReferencia());
	}

	public final void agregar() {
		if(valid()) {
			
		}
	}
	
	private final boolean valid() {
		boolean valid = true;
		valid &= StringUtils.isNotBlank(cantidad.getText());
		valid &= StringUtils.isNotBlank(valorUnidad.getText());
		valid &= StringUtils.isNotBlank(valorTotal.getText());
		return valid;
	}

	public final void cancelar() {
		Stage stage = (Stage) panel.getScene().getWindow();
		stage.close();
		destroy();
	}

}
