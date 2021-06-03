package co.com.japl.ea.dto.dto.inventario;

import java.math.BigDecimal;

import org.pyt.common.annotation.proccess.IsNotBlank;
import org.pyt.common.annotation.proccess.Valid;
import org.pyt.common.annotation.proccess.ValueInObject;

import co.com.japl.ea.common.abstracts.ADto;

public class MovimientoDTO extends ADto {
	private static final long serialVersionUID = -4793142919555790830L;
	@IsNotBlank
	@Valid
	@ValueInObject(field="nombre")
	private ProductoDTO producto;
	@IsNotBlank
	private BigDecimal precioCompra;
	@IsNotBlank
	private Integer cantidad;
	private BigDecimal valor;
	private ParametroInventarioDTO tipo;
	public ProductoDTO getProducto() {
		return producto;
	}
	public void setProducto(ProductoDTO producto) {
		this.producto = producto;
	}
	public BigDecimal getPrecioCompra() {
		return precioCompra;
	}
	public void setPrecioCompra(BigDecimal precioCompra) {
		this.precioCompra = precioCompra;
	}
	public Integer getCantidad() {
		return cantidad;
	}
	public void setCantidad(Integer cantidad) {
		this.cantidad = cantidad;
	}
	public ParametroInventarioDTO getTipo() {
		return tipo;
	}
	public void setTipo(ParametroInventarioDTO tipo) {
		this.tipo = tipo;
	}
	public BigDecimal getValor() {
		return valor;
	}
	public void setValor(BigDecimal valor) {
		this.valor = valor;
	}
}
