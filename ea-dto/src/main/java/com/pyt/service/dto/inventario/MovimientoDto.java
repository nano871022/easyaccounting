package com.pyt.service.dto.inventario;

import java.math.BigDecimal;

import org.pyt.common.annotation.proccess.IsNotBlank;
import org.pyt.common.annotation.proccess.Valid;
import org.pyt.common.annotation.proccess.ValueInObject;
import org.pyt.common.common.ADto;

public class MovimientoDto extends ADto {
	private static final long serialVersionUID = -4793142919555790830L;
	@IsNotBlank
	@Valid
	@ValueInObject(field="nombre")
	private ProductoDto producto;
	@IsNotBlank
	private BigDecimal precioCompra;
	@IsNotBlank
	private Integer cantidad;
	private BigDecimal valor;
	@IsNotBlank
	@Valid(dto=ParametroInventarioDTO.class,fieldIn="nombre")
	private ParametroInventarioDTO tipo;
	public ProductoDto getProducto() {
		return producto;
	}
	public void setProducto(ProductoDto producto) {
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
