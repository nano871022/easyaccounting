package co.com.japl.ea.dto.dto;

import co.com.arquitectura.annotation.proccessor.DelClass;
import co.com.arquitectura.annotation.proccessor.UpdClass;
import co.com.japl.ea.common.abstracts.ADto;
@DelClass(nombre="co.com.japl.ea.dto.dto.dels.CiudadDelDTO")
@UpdClass(nombre="co.com.japl.ea.dto.dto.upds.CiudadUpdDTO")

public class CiudadDTO extends ADto{
	private static final long serialVersionUID = 8327265201569056423L;
	
	private String nombre;
	private DepartamentoDTO departamento;
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public DepartamentoDTO getDepartamento() {
		return departamento;
	}
	public void setDepartamento(DepartamentoDTO departamento) {
		this.departamento = departamento;
	}
}
