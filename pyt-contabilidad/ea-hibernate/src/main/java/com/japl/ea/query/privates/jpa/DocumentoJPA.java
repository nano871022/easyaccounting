package com.japl.ea.query.privates.jpa;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
/**
 * Es el documento que se encarga de realizar una nota credito,debito y otros tipo de documentos
 * @author alejandro parra
 * @since 06/05/2018
 */
@Entity(name="TBL_DOCUMENT")
@Table(name="TBL_DOCUMENT")
public class DocumentoJPA extends AJPA{
	@ManyToOne @JoinColumn(name="sdocumenttype")
	private ParametroJPA tipoDocumento;
	@ManyToOne @JoinColumn(name="sstate")
	private ParametroJPA estado;
	@Column(name="dnote")
	private Date fechaNota;
	@Column(name="sdescription")
	private String descripcion;
	@ManyToOne @JoinColumn(name="sbank")
	private BancoJPA banco;
	@Column(name="nvalue")
	private BigDecimal valor;
	@Column(name="snumbernote")
	private String numeroNota;
	@Column(name="dregister")
	private Date fechaRegistro;
	@Column(name="dannulement")
	private Date fechaAnulacion;
	@ManyToOne @JoinColumn(name="senterprise")
	private EmpresaJPA tercero;
	@ManyToOne @JoinColumn(name="smoney")
	private ParametroJPA moneda;
	
	public EmpresaJPA getTercero() {
		return tercero;
	}
	public void setTercero(EmpresaJPA tercero) {
		this.tercero = tercero;
	}
	public ParametroJPA getMoneda() {
		return moneda;
	}
	public void setMoneda(ParametroJPA moneda) {
		this.moneda = moneda;
	}
	public ParametroJPA getTipoDocumento() {
		return tipoDocumento;
	}
	public void setTipoDocumento(ParametroJPA tipoDocumento) {
		this.tipoDocumento = tipoDocumento;
	}
	public ParametroJPA getEstado() {
		return estado;
	}
	public void setEstado(ParametroJPA estado) {
		this.estado = estado;
	}
	public Date getFechaNota() {
		return fechaNota;
	}
	public void setFechaNota(Date fechaNota) {
		this.fechaNota = fechaNota;
	}
	public String getDescripcion() {
		return descripcion;
	}
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
	public BancoJPA getBanco() {
		return banco;
	}
	public void setBanco(BancoJPA banco) {
		this.banco = banco;
	}
	public BigDecimal getValor() {
		return valor;
	}
	public void setValor(BigDecimal valor) {
		this.valor = valor;
	}
	public String getNumeroNota() {
		return numeroNota;
	}
	public void setNumeroNota(String numeroNota) {
		this.numeroNota = numeroNota;
	}
	public Date getFechaRegistro() {
		return fechaRegistro;
	}
	public void setFechaRegistro(Date fechaRegistro) {
		this.fechaRegistro = fechaRegistro;
	}
	public Date getFechaAnulacion() {
		return fechaAnulacion;
	}
	public void setFechaAnulacion(Date fechaAnulacion) {
		this.fechaAnulacion = fechaAnulacion;
	}
	

}
