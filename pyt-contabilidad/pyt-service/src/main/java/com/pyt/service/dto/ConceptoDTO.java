package com.pyt.service.dto;

import org.pyt.common.common.ADto;

/**
 * un concepto contable indica a que se va a contabilidar
 * @author alejandro parra
 * @since 06/05/2018
 */
public class ConceptoDTO extends ADto{
	private static final long serialVersionUID = 8544091132270171494L;
	private String codigo;
	private String nombre;
	private String descripcion;
	private ParametroDTO estado;
	private EmpresaDTO empresa;
	private String subconcepto;
	private CuentaContableDTO cuentaGasto;
	private CuentaContableDTO cuentaXPagar;
	
	public String getCodigo() {
		return codigo;
	}
	
	public String getSubconcepto() {
		return subconcepto;
	}

	public void setSubconcepto(String subconcepto) {
		this.subconcepto = subconcepto;
	}

	public CuentaContableDTO getCuentaGasto() {
		return cuentaGasto;
	}

	public void setCuentaGasto(CuentaContableDTO cuentaGasto) {
		this.cuentaGasto = cuentaGasto;
	}

	public CuentaContableDTO getCuentaXPagar() {
		return cuentaXPagar;
	}

	public void setCuentaXPagar(CuentaContableDTO cuentaXPagar) {
		this.cuentaXPagar = cuentaXPagar;
	}

	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public String getDescripcion() {
		return descripcion;
	}
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
	public ParametroDTO getEstado() {
		return estado;
	}
	public void setEstado(ParametroDTO estado) {
		this.estado = estado;
	}
	public EmpresaDTO getEmpresa() {
		return empresa;
	}
	public void setEmpresa(EmpresaDTO empresa) {
		this.empresa = empresa;
	}
	
}
