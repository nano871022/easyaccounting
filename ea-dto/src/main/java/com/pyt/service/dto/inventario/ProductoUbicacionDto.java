package com.pyt.service.dto.inventario;

import org.pyt.common.abstracts.ADto;

public class ProductoUbicacionDto extends ADto {
	private static final long serialVersionUID = -940384022485305996L;
	private ProductoDto producto;
	private UbicacionDto ubicacionAlmacen;
	private Integer cantidad;
	public ProductoDto getProducto() {
		return producto;
	}
	public void setProducto(ProductoDto producto) {
		this.producto = producto;
	}
	public UbicacionDto getUbicacionAlmacen() {
		return ubicacionAlmacen;
	}
	public void setUbicacionAlmacen(UbicacionDto ubicacionAlmacen) {
		this.ubicacionAlmacen = ubicacionAlmacen;
	}
	public Integer getCantidad() {
		return cantidad;
	}
	public void setCantidad(Integer cantidad) {
		this.cantidad = cantidad;
	}
}
