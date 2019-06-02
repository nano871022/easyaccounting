package com.japl.ea.query.privates.jpa;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.pyt.service.dto.ParametroDTO;
@Entity(name="TBL_PRODUCT_SUMMARY")
@Table(name="TBL_PRODUCT_SUMMARY")
public class ResumenProductoJPA extends AJPA {
	@Column(name="sproduct")
	private ProductoJPA producto;
	@Column(name="nquantity")
	private Integer cantidad;
	@Column(name="sivapercentsaleapply")
	private ParametroDTO ivaPercentAplicarVenta;
	@Column(name="nprofitpercentsale")
	private Long gananciaPercentVenta;
	@Column(name="nsalevalue")
	private BigDecimal valorVenta;
	@Column(name="npurcharsevalue")
	private BigDecimal valorCompra;
	public ProductoJPA getProducto() {
		return producto;
	}
	public BigDecimal getValorVenta() {
		return valorVenta;
	}

	public void setValorVenta(BigDecimal valorVenta) {
		this.valorVenta = valorVenta;
	}

	public BigDecimal getValorCompra() {
		return valorCompra;
	}

	public void setValorCompra(BigDecimal valorCompra) {
		this.valorCompra = valorCompra;
	}

	public void setProducto(ProductoJPA producto) {
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
	public Long getGananciaPercentVenta() {
		return gananciaPercentVenta;
	}
	public void setGananciaPercentVenta(Long gananciaPercentVenta) {
		this.gananciaPercentVenta = gananciaPercentVenta;
	}
}
