package co.com.japl.ea.dto.dto;

import co.com.arquitectura.annotation.proccessor.DelClass;
import co.com.arquitectura.annotation.proccessor.UpdClass;
import co.com.japl.ea.common.abstracts.ADto;
@DelClass(nombre="co.com.japl.ea.dto.dto.dels.PaisDelDTO")
@UpdClass(nombre="co.com.japl.ea.dto.dto.upds.PaisUpdDTO")

public class PaisDTO extends ADto {
	private static final long serialVersionUID = -7715514480948023886L;
	private String nombre;
	private ParametroDTO estado;
	
	public ParametroDTO getEstado() {
		return estado;
	}
	public void setEstado(ParametroDTO estado) {
		this.estado = estado;
	}
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
}
