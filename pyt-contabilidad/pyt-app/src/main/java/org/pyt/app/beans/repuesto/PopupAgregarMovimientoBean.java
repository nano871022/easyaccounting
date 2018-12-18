package org.pyt.app.beans.repuesto;

import java.math.BigDecimal;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.pyt.common.annotations.Inject;
import org.pyt.common.common.ABean;
import org.pyt.common.common.ValidFields;
import org.pyt.common.common.ValidateValues;
import org.pyt.common.constants.ParametroInventarioConstants;

import com.pyt.service.dto.inventario.MovimientoDto;
import com.pyt.service.dto.inventario.ParametroInventarioDTO;
import com.pyt.service.dto.inventario.ProductoDto;
import com.pyt.service.interfaces.inventarios.IParametroInventariosSvc;
import com.pyt.service.interfaces.inventarios.IProductosSvc;

import co.com.arquitectura.annotation.proccessor.FXMLFile;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

/**
 * Bean encargado de crear las empresas
 * 
 * @author alejandro parra
 * @since 07/05/2018
 */
@FXMLFile(path = "view/repuesto", file = "addmovimiento.fxml", nombreVentana = "Agregar Producto")
public class PopupAgregarMovimientoBean extends ABean<MovimientoDto> {
	@Inject(resource = "com.pyt.service.implement.inventario.ProductosSvc")
	private IProductosSvc productosSvc;
	@Inject(resource = "com.pyt.service.implement.inventario.ParametroInventariosSvc")
	private IParametroInventariosSvc parametroSvc;
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
		NombreVentana = "Agregar Producto";
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
		try {
			ParametroInventarioDTO parametro = new ParametroInventarioDTO();
			parametro.setNombre("entrada");
			List<ParametroInventarioDTO> parametros = parametroSvc.getAllParametros(parametro, ParametroInventarioConstants.GRUPO_DESC_TIPO_MOVIMIENTO);
			if(parametros == null || parametros.size() == 0) throw new Exception("No se encontro tipos de movimientos.");
			if(parametros.size() > 1) throw new Exception("Se encontro varios registros.");
			if(parametros.size() == 1) {
				registro.setTipo(parametros.get(0));
			}
		}catch(Exception e) {
			error(e);
		}
		cantidad.setText("");
		valorTotal.setText("");
		valorUnidad.setText("");
	}

	public final void load(ProductoDto producto) throws Exception {
		if (producto == null)
			throw new Exception("El producto suministrado esta vacio.");
		if (StringUtils.isBlank(producto.getCodigo()))
			throw new Exception("El producto suministrado no ha sido registraddo.");
		registro.setProducto(producto);
		nombre.setText(producto.getNombre());
		ref.setText(producto.getReferencia());
	}

	private final void loadFxml() {
		try {
			registro.setCantidad(vv.cast(cantidad.getText(), Integer.class));
			registro.setPrecioCompra(vv.cast(valorUnidad.getText(), BigDecimal.class));
			registro.setValor(vv.cast(valorTotal.getText(), BigDecimal.class));
		} catch (Exception e) {
			error(e);
		}
	}

	public final void agregar() {
		if (valid()) {
			loadFxml();
		}
	}

	/**
	 * Se encarga de validar los campos del formulario segun la configuracion de
	 * validacion
	 * 
	 * @return {@link Boolean}
	 */
	private final boolean valid() {
		boolean valid = true;
		valid &= ValidFields.numeric(cantidad, true, 0, null, "Cantidad productos");
		valid &= ValidFields.numeric(valorUnidad, true, new BigDecimal(0), null, "Valor unidad");
		valid &= ValidFields.numeric(valorTotal, true, new BigDecimal(0), null, "Valor total");
		return valid;
	}

	public final void cancelar() {
		Stage stage = (Stage) panel.getScene().getWindow();
		stage.close();
		destroy();
	}
}
