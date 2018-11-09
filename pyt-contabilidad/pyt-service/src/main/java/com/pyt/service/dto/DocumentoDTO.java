package com.pyt.service.dto;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.pyt.common.annotations.DelClass;
import org.pyt.common.annotations.NoEdit;
import org.pyt.common.annotations.UpdClass;
import org.pyt.common.common.ADto;
/**
 * Es el documento que se encarga de realizar una nota credito,debito y otros tipo de documentos
 * @author alejandro parra
 * @since 06/05/2018
 */
@DelClass(nombre="com.pyt.service.dto.dels.DocumentoDelDTO")
@UpdClass(nombre="com.pyt.service.dto.upds.DocumentoUpdDTO")

public class DocumentoDTO extends ADto{
	private static final long serialVersionUID = 284440016164488458L;
	private ParametroDTO tipoDocumento;
	private ParametroDTO estado;
	private Date fechaNota;
	private String descripcion;
	private BancoDTO banco;
	@NoEdit
	private BigDecimal valor;
	private String numeroNota;
	private Date fechaRegistro;
	private Date fechaAnulacion;
	private EmpresaDTO tercero;
	private ParametroDTO moneda;
	
	public EmpresaDTO getTercero() {
		return tercero;
	}
	public void setTercero(EmpresaDTO tercero) {
		this.tercero = tercero;
	}
	public ParametroDTO getMoneda() {
		return moneda;
	}
	public void setMoneda(ParametroDTO moneda) {
		this.moneda = moneda;
	}
	public ParametroDTO getTipoDocumento() {
		return tipoDocumento;
	}
	public void setTipoDocumento(ParametroDTO tipoDocumento) {
		this.tipoDocumento = tipoDocumento;
	}
	public ParametroDTO getEstado() {
		return estado;
	}
	public void setEstado(ParametroDTO estado) {
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
	public BancoDTO getBanco() {
		return banco;
	}
	public void setBanco(BancoDTO banco) {
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
