package com.japl.ea.query.privates.jpa;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
@Entity(name="TBL_MOVEMENT")
@Table(name="TBL_MOVEMENT")
public class MovimientoJPA extends AJPA {
	@ManyToOne @JoinColumn(name="sproduct")
	private ProductoJPA producto;
	@Column(name="npurcharseprice")
	private BigDecimal precioCompra;
	@Column(name="nquantity")
	private Integer cantidad;
	@Column(name="nvalue")
	private BigDecimal valor;
	@ManyToOne @JoinColumn(name="stype")
	private ParametroInventarioJPA tipo;
	public ProductoJPA getProducto() {
		return producto;
	}
	public void setProducto(ProductoJPA producto) {
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
	public ParametroInventarioJPA getTipo() {
		return tipo;
	}
	public void setTipo(ParametroInventarioJPA tipo) {
		this.tipo = tipo;
	}
	public BigDecimal getValor() {
		return valor;
	}
	public void setValor(BigDecimal valor) {
		this.valor = valor;
	}
}
