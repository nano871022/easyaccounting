package com.japl.ea.query.privates.jpa;

import java.math.BigDecimal;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
@Entity(name="TBL_PRODUCT_SUMMARY")
@Table(name="TBL_PRODUCT_SUMMARY")
public class ResumenProductoJPA extends AJPA {
	@ManyToOne @JoinColumn(name="sproduct")
	private ProductoJPA producto;
	@Column(name="nquantity")
	private Integer cantidad;
	@ManyToOne @JoinColumn(name="sivapercentsaleapply")
	private ParametroJPA ivaPercentAplicarVenta;
	@Column(name="nprofitpercentsale")
	private Long gananciaPercentVenta;
	@Column(name="nsalevalue")
	private BigDecimal valorVenta;
	@Column(name="npurcharsevalue")
	private BigDecimal valorCompra;
	@ManyToMany(targetEntity=IngresoJPA.class)
	private Set<IngresoJPA> servicios;
	
	public Set<IngresoJPA> getServicios() {
		return servicios;
	}
	public void setServicios(Set<IngresoJPA> servicios) {
		this.servicios = servicios;
	}
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
	public ParametroJPA getIvaPercentAplicarVenta() {
		return ivaPercentAplicarVenta;
	}
	public void setIvaPercentAplicarVenta(ParametroJPA ivaPercentAplicarVenta) {
		this.ivaPercentAplicarVenta = ivaPercentAplicarVenta;
	}
	public Long getGananciaPercentVenta() {
		return gananciaPercentVenta;
	}
	public void setGananciaPercentVenta(Long gananciaPercentVenta) {
		this.gananciaPercentVenta = gananciaPercentVenta;
	}
}
