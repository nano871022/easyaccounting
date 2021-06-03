package co.com.japl.ea.dto.system;

import co.com.japl.ea.common.abstracts.ADto;

public class ToggleDTO extends ADto {
	private String nombre;
	private Boolean activado;
	
	public ToggleDTO() {}

	public ToggleDTO(String name, Boolean cached) {
		this.nombre = name;
		this.activado = cached;
	}
	
	public void setNombre(String name) {
		this.nombre = name;
	}
	public String getName() {
		return nombre;
	}
	
	public void setActivado(boolean state) {
		activado = state;
	}
	
	public Boolean isActivado() {
		return activado;
	}
	
	
}
