package co.com.japl.ea.dto.dto.upds;

import java.time.LocalDateTime;

import org.pyt.common.interfaces.IUpdClass;

import com.pyt.service.dto.ParametroDTO;

public class ParametroUpdDTO extends ParametroDTO implements IUpdClass{
	private static final long serialVersionUID = 1L;
	private LocalDateTime fechaActualizado;
	private String usuarioActualiza;
	public LocalDateTime getFechaActualizado() {
		return fechaActualizado;
	}
	public void setFechaActualizado(LocalDateTime fechaActualizado) {
		this.fechaActualizado = fechaActualizado;
	}
	@Override
	public String getUsuarioActualizo() {
		return usuarioActualiza;
	}
	@Override
	public void setUsuarioActualizo(String usuario) {
		usuarioActualiza = usuario;
	}
}
