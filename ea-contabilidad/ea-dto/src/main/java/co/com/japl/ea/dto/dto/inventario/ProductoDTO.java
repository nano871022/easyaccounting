package co.com.japl.ea.dto.dto.inventario;

import org.pyt.common.annotation.proccess.IsNotBlank;

import co.com.japl.ea.common.abstracts.ADto;

public class ProductoDTO extends ADto {
	private static final long serialVersionUID = -5885260384830867325L;
	@IsNotBlank
	private String nombre;
	private String descripcion;
	private String referencia;
	public String getReferencia() {
		return referencia;
	}
	public void setReferencia(String referencia) {
		this.referencia = referencia;
	}
	public String getDescripcion() {
		return descripcion;
	}
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
}
