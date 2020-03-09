package org.pyt.app.beans.repuesto;

import java.math.BigDecimal;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.controlsfx.glyphfont.FontAwesome.Glyph;
import org.pyt.common.annotations.Inject;
import org.pyt.common.common.DtoUtils;
import org.pyt.common.common.ListUtils;
import org.pyt.common.common.SelectList;
import org.pyt.common.constants.ParametroConstants;
import org.pyt.common.constants.PermissionConstants;
import org.pyt.common.exceptions.ParametroException;
import org.pyt.common.exceptions.inventario.ProductosException;
import org.pyt.common.exceptions.inventario.ResumenProductoException;
import org.pyt.common.exceptions.validates.ValidateValueException;
import org.pyt.common.validates.ValidFields;
import org.pyt.common.validates.ValidateValues;

import com.pyt.service.dto.ParametroDTO;
import com.pyt.service.dto.inventario.ProductoDTO;
import com.pyt.service.dto.inventario.ResumenProductoDTO;
import com.pyt.service.interfaces.IParametrosSvc;
import com.pyt.service.interfaces.inventarios.IProductosSvc;

import co.com.arquitectura.annotation.proccessor.FXMLFile;
import co.com.japl.ea.beans.abstracts.ABean;
import co.com.japl.ea.common.button.apifluid.ButtonsImpl;
import co.com.japl.ea.utls.PermissionUtil;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
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
	private HBox buttons;

	@FXML
	public void initialize() {
		NombreVentana = i18n().get("fxml.title.add.new.spartpart");
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
		nombre.textProperty().addListener(change -> validProduct());
		referencia.focusedProperty().addListener(change -> validProduct());
		visibleButtons();
		ButtonsImpl.Stream(HBox.class).setLayout(buttons).setName("fxml.btn.save").action(this::add).icon(Glyph.SAVE)
				.isVisible(save).setName("fxml.btn.edit").action(this::add).icon(Glyph.EDIT).isVisible(edit)
				.setName("fxml.btn.movements").action(this::agregarMovimiento).isVisible(edit)
				.setName("fxml.btn.cancel").action(this::cancel).build();
	}

	private void validProduct() {
		try {
			if (StringUtils.isNotBlank(nombre.getText()) && StringUtils.isNotBlank(referencia.getText())) {
				var producto = new ProductoDTO();
				producto.setNombre(nombre.getText());
				producto.setReferencia(referencia.getText());
				var list = productosSvc.productos(producto);
				if (ListUtils.isBlank(list)) {
					alerta(i18n().valueBundle("err.product.wasnt.found.add.movementing", producto.getNombre(),
							producto.getReferencia()));
				} else if (ListUtils.haveMoreOneItem(list)) {
					alerta(i18n().valueBundle("err.product.was.found.some.rows", producto.getNombre(),
							producto.getReferencia()));
				} else {
					registro.setProducto(list.get(0));
				}
			}
		} catch (ProductosException e) {
			error(e);
		}
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
		registro.setCantidad(0);
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
		if (DtoUtils.haveCode(dto)) {
			getResumenProducto(dto);
			loadFxml();
			titulo.setText(i18n().get("mensaje.modifing.spartpart"));
			visibleButtons();
		} else {
			errorI18n("err.spartpart.cant.edit");
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
		valid &= ValidFields.valid(registro.getProducto().getNombre(), nombre, true, 1, 30,
				i18n().valueBundle("err.valid.spartpart.field.name.empty"));
		valid &= ValidFields.valid(registro.getProducto().getDescripcion(), descripcion, true, 1, 100,
				i18n().valueBundle("err.valid.spartpart.field.description.empty"));
		valid &= ValidFields.valid(registro.getProducto().getReferencia(), referencia, true, 1, 30,
				i18n().valueBundle("err.valid.spartpart.field.reference.empty"));
		valid &= ValidFields.valid(registro.getCantidad(), cantidad, false, null, null,
				i18n().valueBundle("err.valid.spartpart.field.quantity.empty"));
		valid &= ValidFields.valid(registro.getGananciaPercentVenta(), percentGanancia, false, null, null,
				i18n().valueBundle("err.valid.spartpart.field.profitpercent.empty"));
		valid &= ValidFields.valid(registro.getValorCompra(), valorMercado, false, null, null,
				i18n().valueBundle("err.valid.spartpart.field.purchasevalue.empty"));
		valid &= ValidFields.valid(registro.getValorVenta(), valorVenta, false, null, null,
				i18n().valueBundle("err.valid.spartpart.field.salevalue.empty"));
		return valid;
	}

	private void validEmpty() {
		if (registro.getCantidad() == null) {
			registro.setCantidad(0);
		}
		if (registro.getValorCompra() == null) {
			registro.setValorCompra(new BigDecimal(0));
		}
		if (registro.getValorVenta() == null) {
			registro.setValorVenta(new BigDecimal(0));
		}
	}

	private void updateProduct() throws ProductosException {
		if (DtoUtils.haveCode(registro.getProducto())) {
			productosSvc.update(registro.getProducto(), getUsuario());
		}
	}

	private void insertProduct() throws ProductosException {
		if (!DtoUtils.haveCode(registro.getProducto())) {
			var prod = productosSvc.insert(registro.getProducto(), getUsuario());
			registro.setProducto(prod);
		}
	}

	private void insertProductSummary() throws ResumenProductoException {
		if (!DtoUtils.haveCode(registro)) {
			validEmpty();
			productosSvc.insert(registro, getUsuario());
			codigo.setText(registro.getCodigo());
			notificarI18n("mensaje.spartpart.have.been.inserted.succesfull");
		}
	}

	private void updateProductSummary() throws ResumenProductoException {
		if (DtoUtils.haveCode(registro)) {
			productosSvc.update(registro, getUsuario());
			notificarI18n("mensaje.spartpart.have.been.updated.succesfull");
		}
	}

	public void add() {
		fxmlLoad();
		try {
			if (valid()) {
				updateProduct();
				insertProduct();
				updateProductSummary();
				insertProductSummary();
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

	public final void setUpdate(Boolean valid) {
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

	@Override
	protected void visibleButtons() {
		var save = !DtoUtils.haveCode(registro) && PermissionUtil.INSTANCE()
				.havePerm(PermissionConstants.CONST_PERM_CREATE, RepuestoBean.class, getUsuario().getGrupoUser());
		var edit = DtoUtils.haveCode(registro) && PermissionUtil.INSTANCE()
				.havePerm(PermissionConstants.CONST_PERM_UPDATE, RepuestoBean.class, getUsuario().getGrupoUser());
		this.save.setValue(save);
		this.edit.setValue(edit);
	}

}
