package com.pyt.service.dto.dels;

import java.time.LocalDateTime;

import org.pyt.common.interfaces.IDelClass;

import com.pyt.service.dto.DetalleDTO;

public class DetalleDelDTO extends DetalleDTO implements IDelClass {
	private LocalDateTime fechaElimina;
	private String usuario;
	@Override
	public LocalDateTime getFechaElimina() {
		return fechaElimina;
	}

	@Override
	public void setFechaElimina(LocalDateTime fecha) {
		fechaElimina = fecha;
	}

	@Override
	public String getUsuarioELimina() {
		return usuario;
	}

	@Override
	public void setUsuarioElimina(String usuario) {
		this.usuario = usuario;
	}

}
