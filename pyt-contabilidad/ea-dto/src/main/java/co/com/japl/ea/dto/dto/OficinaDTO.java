package co.com.japl.ea.dto.dto;

import co.com.arquitectura.annotation.proccessor.DelClass;
import co.com.arquitectura.annotation.proccessor.UpdClass;
import co.com.japl.ea.common.abstracts.ADto;
@DelClass(nombre="co.com.japl.ea.dto.dto.dels.OficinaDelDTO")
@UpdClass(nombre="co.com.japl.ea.dto.dto.upds.OficinaUpdDTO")
public class OficinaDTO extends ADto {
	private static final long serialVersionUID = -1103952032633883291L;
	private String nombre;
	private String detalle;
	private String direccion;
	private CiudadDTO ciudad;
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public String getDetalle() {
		return detalle;
	}
	public void setDetalle(String detalle) {
		this.detalle = detalle;
	}
	public String getDireccion() {
		return direccion;
	}
	public void setDireccion(String direccion) {
		this.direccion = direccion;
	}
	public CiudadDTO getCiudad() {
		return ciudad;
	}
	public void setCiudad(CiudadDTO ciudad) {
		this.ciudad = ciudad;
	}
}
