package org.pyt.common.common;

import org.apache.commons.lang3.StringUtils;
import org.pyt.common.annotations.Inject;
import org.pyt.common.constants.AppConstants;
import org.pyt.common.exceptions.LoadAppFxmlException;
import org.pyt.common.exceptions.ReflectionException;
import org.pyt.common.reflection.Reflection;

import javafx.scene.layout.Pane;

/**
 * bstracto bean para heredar todos los bean y tener objetos pre creados y
 * metodos que ayudan a trabajar con los controladores de fxml
 * 
 * @author Alejandro Parra
 * @since 2018-05-23
 * @param <T>
 */
public abstract class ABean<T extends ADto> extends Reflection {
	protected T registro;
	protected UsuarioDTO userLogin;
	protected String NombreVentana;
	@SuppressWarnings("rawtypes")
	@Inject
	protected Comunicacion comunicacion;

	public ABean() {
		try {
			userLogin = new UsuarioDTO();
			userLogin.setNombre("nano871022");
			inject();
		} catch (ReflectionException e) {
			System.err.println(e);
		}
	}

	/**
	 * Se encarga de obtener el fxml y el controlador para ponerlo sobre la pantalla
	 * 
	 * @param classOfBean
	 *            {@link Class}
	 * @return {@link ABean}
	 */
	public <L extends ADto, S extends ABean<L>> S getController(Class<S> classOfBean) {
		try {
			return LoadAppFxml.BeanFxmlScroller(null, classOfBean);
		} catch (LoadAppFxmlException e) {
			Log.logger("El bean " + classOfBean.getName() + " no puede ser cargado.", e);
			error(e);
		}
		return null;
	}

	/**
	 * Se encarga de obtener el fxml y el controlador para ponerlo sobre la pantalla
	 * 
	 * @param classOfBean
	 *            {@link Class}
	 * @return {@link ABean}
	 */
	public <M extends Pane, L extends ADto, S extends ABean<L>> S getController(M layout, Class<S> classOfBean) {
		try {
			return LoadAppFxml.beanFxmlPane(layout, classOfBean);
		} catch (LoadAppFxmlException e) {
			Log.logger("El bean " + classOfBean.getName() + " no puede ser cargado.", e);
			error(e);
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	public void alerta(String mensaje) {
		comunicacion.setComando(AppConstants.COMMAND_POPUP_WARN, mensaje);
	}

	@SuppressWarnings("unchecked")
	public void notificar(String msn) {
		comunicacion.setComando(AppConstants.COMMAND_POPUP_INFO, msn);
	}

	@SuppressWarnings({ "hiding", "unchecked" })
	public <T extends Exception> void error(T error) {
		String mensaje = error.getMessage();
		Log.logger(error);
		if (StringUtils.isNotBlank(mensaje)) {
			comunicacion.setComando(AppConstants.COMMAND_POPUP_ERROR, mensaje);
		}
	}

	@SuppressWarnings("unchecked")
	public void error(String msn) {
		Log.logger(msn);
		comunicacion.setComando(AppConstants.COMMAND_POPUP_ERROR, msn);
	}

	@SuppressWarnings("unchecked")
	public void error(Throwable e) {
		String mensaje = e.getMessage();
		Log.logger(e);
		if (StringUtils.isNotBlank(mensaje)) {
			comunicacion.setComando(AppConstants.COMMAND_POPUP_ERROR, mensaje);
		}
	}

	@SuppressWarnings({ "unchecked", "hiding" })
	public final <T extends Exception> void mensajeIzquierdo(T error) {
		String mensaje = error.getMessage();
		Log.logger(error);
		if (StringUtils.isNotBlank(mensaje)) {
			comunicacion.setComando(AppConstants.COMMAND_MSN_IZQ, mensaje);
		}
	}

	@SuppressWarnings({ "unchecked", "hiding" })
	public final <T extends Throwable> void mensajeIzquierdo(T error) {
		String mensaje = error.getMessage();
		Log.logger(error);
		if (StringUtils.isNotBlank(mensaje)) {
			comunicacion.setComando(AppConstants.COMMAND_MSN_IZQ, mensaje);
		}
	}

	@SuppressWarnings("unchecked")
	public final void mensajeIzquierdo(String mensaje) {
		comunicacion.setComando(AppConstants.COMMAND_MSN_IZQ, mensaje);
	}

	@SuppressWarnings("unchecked")
	public final void mensajeDerecho(String mensaje) {
		comunicacion.setComando(AppConstants.COMMAND_MSN_DER, mensaje);
	}

	@SuppressWarnings("unchecked")
	public final void mensajeCentral(String mensaje) {
		comunicacion.setComando(AppConstants.COMMAND_MSN_CTR, mensaje);
	}

	@SuppressWarnings("unchecked")
	public final void progreso(Double valor) {
		comunicacion.setComando(AppConstants.COMMAND_PROGRESS, valor);
	}

	public T getRegistro() {
		return registro;
	}

	public void setRegistro(T registro) {
		this.registro = registro;
	}

	public String getNombreVentana() {
		return NombreVentana;
	}

	public void setNombreVentana(String nombreVentana) {
		NombreVentana = nombreVentana;
	}

}
