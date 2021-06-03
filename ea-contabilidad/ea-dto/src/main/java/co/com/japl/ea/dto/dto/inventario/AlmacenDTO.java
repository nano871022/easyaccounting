package co.com.japl.ea.dto.dto.inventario;

import co.com.japl.ea.common.abstracts.ADto;

public class AlmacenDTO extends ADto {
	private static final long serialVersionUID = -2700569796434943286L;
	private String nombre;
	private String descripcion;
	private String direccion;
	public String getDescripcion() {
		return descripcion;
	}
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
	public String getDireccion() {
		return direccion;
	}
	public void setDireccion(String direccion) {
		this.direccion = direccion;
	}
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
}
