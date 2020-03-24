package org.pyt.app.beans.repuesto;

import java.math.BigDecimal;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.pyt.common.annotations.Inject;
import org.pyt.common.constants.ParametroInventarioConstants;
import org.pyt.common.exceptions.inventario.InventarioException;
import org.pyt.common.validates.ValidFields;
import org.pyt.common.validates.ValidateValues;

import com.pyt.service.dto.inventario.MovimientoDTO;
import com.pyt.service.dto.inventario.ParametroInventarioDTO;
import com.pyt.service.dto.inventario.ProductoDTO;
import com.pyt.service.interfaces.inventarios.IInventarioSvc;
import com.pyt.service.interfaces.inventarios.IParametroInventariosSvc;

import co.com.arquitectura.annotation.proccessor.FXMLFile;
import co.com.japl.ea.beans.abstracts.ABean;
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
public class PopupAgregarMovimientoBean extends ABean<MovimientoDTO> {
	@Inject(resource = "com.pyt.service.implement.inventario.InventarioSvc")
	private IInventarioSvc inventarioSvc;
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
	private String caller;

	@FXML
	public void initialize() {
		NombreVentana = i18n().get("fxml.title.add.movement");
		registro = new MovimientoDTO();
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
			parametro.setNombre("Entrada");
			List<ParametroInventarioDTO> parametros = parametroSvc.getAllParametros(parametro,
					ParametroInventarioConstants.GRUPO_DESC_TIPO_MOVIMIENTO);
			if (parametros == null || parametros.size() == 0)
				throw new Exception(i18n().get("err.movements.havent.been.found"));
			if (parametros.size() > 1)
				throw new Exception(i18n().get("err.rows.havent.been.found"));
			if (parametros.size() == 1) {
				registro.setTipo(parametros.get(0));
			}
		} catch (Exception e) {
			error(e);
		}
		cantidad.setText("");
		valorTotal.setText("");
		valorUnidad.setText("");
	}

	public final void load(String llamar, ProductoDTO producto) throws Exception {
		if (producto == null)
			throw new Exception(i18n().get("err.product.is.empty"));
		if (StringUtils.isBlank(producto.getCodigo())) {
			throw new Exception(i18n().get("err.product.isnt.registered"));
		}
		registro.setProducto(producto);
		nombre.setText(producto.getNombre());
		ref.setText(producto.getReferencia());
		caller = llamar;
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
		try {
			loadFxml();
			if (valid()) {
				inventarioSvc.agregarInventario(registro, getUsuario());
				notificarI18n("mensaje.product.was.stocktaking");
				caller(caller, true);
				cancelar();
			}
		} catch (InventarioException e) {
			error(e);
		} catch (Exception e) {
			error(e);
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
		valid &= ValidFields.valid(registro.getCantidad(), cantidad, true, 0, null,
				i18n().valueBundle("err.movement.field.product.quaity.empty"));
		valid &= ValidFields.valid(registro.getPrecioCompra(), valorUnidad, true, 0, null,
				i18n().valueBundle("err.movement.field.purchaseprice.empty"));
		valid &= ValidFields.valid(registro.getValor(), valorTotal, true, 0, null,
				i18n().valueBundle("err.movement.field.totalvalue.empty"));
		return valid;
	}

	public final void cancelar() {
		Stage stage = (Stage) panel.getScene().getWindow();
		stage.close();
		destroy();
	}

	@Override
	protected void visibleButtons() {
		// TODO Auto-generated method stub

	}
}
