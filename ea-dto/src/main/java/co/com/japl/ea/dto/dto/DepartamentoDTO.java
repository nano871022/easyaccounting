package co.com.japl.ea.dto.dto;

import co.com.arquitectura.annotation.proccessor.DelClass;
import co.com.arquitectura.annotation.proccessor.UpdClass;
import co.com.japl.ea.common.abstracts.ADto;
@DelClass(nombre="co.com.japl.ea.dto.dto.dels.DepartamentoDelDTO")
@UpdClass(nombre="co.com.japl.ea.dto.dto.upds.DepartamentoUpdDTO")

public class DepartamentoDTO extends ADto {
	private static final long serialVersionUID = 2988034817404294221L;
	private String nombre;
	private PaisDTO pais;
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public PaisDTO getPais() {
		return pais;
	}
	public void setPais(PaisDTO pais) {
		this.pais = pais;
	}
}
