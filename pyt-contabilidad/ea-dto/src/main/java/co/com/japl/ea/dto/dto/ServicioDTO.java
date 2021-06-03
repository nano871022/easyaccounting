package co.com.japl.ea.dto.dto;

import co.com.arquitectura.annotation.proccessor.DelClass;
import co.com.arquitectura.annotation.proccessor.UpdClass;
import co.com.japl.ea.common.abstracts.ADto;

/**
 * Nombre de los servicios que ofrece la empresa
 * @author alejandro parra 
 * @since 06/05/2018
 */
@DelClass(nombre="co.com.japl.ea.dto.dto.dels.ServicioDelDTO")
@UpdClass(nombre="co.com.japl.ea.dto.dto.upds.ServicioUpdDTO")

public class ServicioDTO extends ADto{
	private static final long serialVersionUID = -1416881593237077200L;
	private String nombre;
	private Long  valorManoObra;
	private String descripcion;
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
	public Long getValorManoObra() {
		return valorManoObra;
	}
	public void setValorManoObra(Long valorManoObra) {
		this.valorManoObra = valorManoObra;
	}
}
