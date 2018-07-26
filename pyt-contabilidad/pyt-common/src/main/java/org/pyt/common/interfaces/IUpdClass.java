package org.pyt.common.interfaces;

import java.time.LocalDateTime;
/**
 * Interfaz para generalizar las clases de actualizacion
 * @author Alejandro Parra
 * @since 26/07/2018
 */
public interface IUpdClass {
	public LocalDateTime getFechaActualizado();
	public void setFechaActualizado(LocalDateTime fecha);
	public String getUsuarioActualizo();
	public void setUsuarioActualizo(String usuario);
}
