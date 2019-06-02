package com.japl.ea.query.privates.jpa;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
@Entity(name="TBL_PRODUCT_LOCATION")
@Table(name="TBL_PRODUCT_LOCATION")
public class ProductoUbicacionJPA extends AJPA {
	@Column(name="sproduct")
	private ProductoJPA producto;
	@Column(name="slocation")
	private UbicacionJPA ubicacionAlmacen;
	@Column(name="nquantity")
	private Integer cantidad;
	public ProductoJPA getProducto() {
		return producto;
	}
	public void setProducto(ProductoJPA producto) {
		this.producto = producto;
	}
	public UbicacionJPA getUbicacionAlmacen() {
		return ubicacionAlmacen;
	}
	public void setUbicacionAlmacen(UbicacionJPA ubicacionAlmacen) {
		this.ubicacionAlmacen = ubicacionAlmacen;
	}
	public Integer getCantidad() {
		return cantidad;
	}
	public void setCantidad(Integer cantidad) {
		this.cantidad = cantidad;
	}
}
