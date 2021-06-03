package co.com.japl.ea.dto.dto.dels;

import java.time.LocalDateTime;

import co.com.japl.ea.common.interfaces.IDelClass;
import co.com.japl.ea.dto.dto.ParametroDTO;
public class ParametroDelDTO extends ParametroDTO implements IDelClass{
	private static final long serialVersionUID = -6072273771164489849L;
	private LocalDateTime fechaElimina;
	private String usuarioElimina;
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
		return usuarioElimina;
	}
	@Override
	public void setUsuarioElimina(String usuario) {
		usuarioElimina = usuario;
	}
}
