package org.pyt.common.common;

import org.pyt.common.annotations.Inject;
import org.pyt.common.controller.LocatorController;
import org.pyt.common.exceptions.ReflectionException;
/**
 * Se encarga de implementar {@link IBean} para contruir controlladores de interfaces creadas en fxml
 * @author Alejandro Parra
 * @since 10/04/2019
 * @param <T>
 */
public abstract class ABean<T extends ADto> implements IBean<T> {
	protected T registro;
	protected UsuarioDTO userLogin;
	protected String NombreVentana;
	@SuppressWarnings("rawtypes")
	@Inject
	protected Comunicacion comunicacion;
	protected Log logger = Log.Log(this.getClass());

	public ABean() {
		try {
			userLogin = new UsuarioDTO();
			userLogin.setNombre("nano871022");
			inject();
			LocatorController.getInstance().setClass(this.getClass()).putLoadInController(this);
		} catch (ReflectionException e) {
			logger.logger("Reflection: ",e);
		} catch (Exception e) {
			logger.logger(e);
		}
	}
	
	@SuppressWarnings("rawtypes")
	public  final Comunicacion comunicacion() {
		return comunicacion;
	}
	
	public final T getRegistro() {
		return registro;
	}

	public final void setRegistro(T registro) {
		this.registro = registro; 
	}

	public final String getNombreVentana() {
		return NombreVentana;
	}

	public final void setNombreVentana(String nombreVentana) {
		this.NombreVentana = nombreVentana;
	}
	
	@SuppressWarnings({ "hiding", "unchecked" })
	public final <T extends Object> T meThis() {
		return (T) this;
	}

	public final Log logger() {
		return logger;
	}
	
}
