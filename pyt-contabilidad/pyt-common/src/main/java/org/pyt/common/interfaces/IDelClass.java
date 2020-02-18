package org.pyt.common.interfaces;

import java.time.LocalDateTime;
/**
 * Interfaz para generalizar las clases de eliminacion
 * @author Alejandro Parra
 * @since 26/07/2018
 */
public interface IDelClass {
	public LocalDateTime getFechaElimina();
	public void setFechaElimina(LocalDateTime fecha);
	public String getUsuarioELimina();
	public void setUsuarioElimina(String usuario);
}
