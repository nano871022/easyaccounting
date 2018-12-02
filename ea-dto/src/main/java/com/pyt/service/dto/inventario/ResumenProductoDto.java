package com.pyt.service.dto.inventario;

import org.pyt.common.common.ADto;

import com.pyt.service.dto.ParametroDTO;

public class ResumenProductoDto extends ADto {
	private static final long serialVersionUID = -4721145467642162080L;
	private ProductoDto producto;
	private Integer cantidad;
	private ParametroDTO ivaPercentAplicarVenta;
	private ParametroDTO gananciaPercentVenta;
	public ProductoDto getProducto() {
		return producto;
	}
	public void setProducto(ProductoDto producto) {
		this.producto = producto;
	}
	public Integer getCantidad() {
		return cantidad;
	}
	public void setCantidad(Integer cantidad) {
		this.cantidad = cantidad;
	}
	public ParametroDTO getIvaPercentAplicarVenta() {
		return ivaPercentAplicarVenta;
	}
	public void setIvaPercentAplicarVenta(ParametroDTO ivaPercentAplicarVenta) {
		this.ivaPercentAplicarVenta = ivaPercentAplicarVenta;
	}
	public ParametroDTO getGananciaPercentVenta() {
		return gananciaPercentVenta;
	}
	public void setGananciaPercentVenta(ParametroDTO gananciaPercentVenta) {
		this.gananciaPercentVenta = gananciaPercentVenta;
	}
}
