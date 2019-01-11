package org.pyt.common.common;

import org.apache.commons.lang3.StringUtils;
import org.pyt.common.annotations.Inject;
import org.pyt.common.constants.AppConstants;
import org.pyt.common.controller.LocatorController;
import org.pyt.common.exceptions.LoadAppFxmlException;
import org.pyt.common.exceptions.ReflectionException;
import org.pyt.common.reflection.Reflection;

import javafx.scene.layout.Pane;
import javafx.stage.Stage;

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
			logger.error("El bean " + classOfBean.getName() + " no puede ser cargado.");
			error(e);
		}
		return null;
	}

	/**
	 * Se encarga de realizar la invoicacion para obtener o introducir datos a un
	 * bean que se encuentre en uso
	 * 
	 * @param caller
	 *            {@link String} anotacion bean . metodo
	 * @param value
	 *            {@link Object} valores que se desean inyectar en el metodo
	 * @return {@link Object} valor retornado cuando es un get
	 * @throws {@link
	 *             Exception}
	 */
	@SuppressWarnings({ "unchecked", "rawtypes", "hiding" })
	protected <T extends Object, S extends Object> S caller(String caller, T... value) throws Exception {
		S returns = null;
		Class clase = this.getClass();
		if (value.length == 0) {
			returns = LocatorController.getInstance().setClass(clase).call(caller, null);
		} else if (value.length == 1) {
			LocatorController.getInstance().setClass(clase).call(caller, value[0]);
		} else {
			throw new Exception("Se suministraron varios objetos, solo se necesita uno.");
		}
		return returns;
	}

	protected final void destroy() {
		try {
			LocatorController.getInstance().removeLoadInController(this);
		} catch (Exception e) {
			mensajeIzquierdo(e);
		}
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
			logger.logger("El bean " + classOfBean.getName() + " no puede ser cargado.", e);
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
		logger.logger(error);
		if (StringUtils.isNotBlank(mensaje)) {
			comunicacion.setComando(AppConstants.COMMAND_POPUP_ERROR, mensaje);
		}
	}

	@SuppressWarnings("unchecked")
	public void error(String msn) {
		logger.logger(msn);
		comunicacion.setComando(AppConstants.COMMAND_POPUP_ERROR, msn);
	}

	@SuppressWarnings("unchecked")
	public void error(Throwable e) {
		String mensaje = e.getMessage();
		logger.logger(e);
		if (StringUtils.isNotBlank(mensaje)) {
			comunicacion.setComando(AppConstants.COMMAND_POPUP_ERROR, mensaje);
		}
	}

	@SuppressWarnings({ "unchecked", "hiding" })
	public final <T extends Exception> void mensajeIzquierdo(T error) {
		String mensaje = error.getMessage();
		logger.logger(error);
		if (StringUtils.isNotBlank(mensaje)) {
			comunicacion.setComando(AppConstants.COMMAND_MSN_IZQ, mensaje);
		}
	}

	@SuppressWarnings({ "unchecked", "hiding" })
	public final <T extends Throwable> void mensajeIzquierdo(T error) {
		String mensaje = error.getMessage();
		logger.logger(error);
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

	/**
	 * Se encarga de cargar de crear una ventana con la informacion de un fxml y
	 * retorna el bean de despliegue creado
	 * 
	 * @param clase
	 *            extends {@link ABean}
	 * @return extends {@link ABean} object
	 * @throws {@link
	 *             LoadAppFxmlException}
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	protected final <T extends ABean> T controllerPopup(Class<T> clase) throws LoadAppFxmlException {
		return (T) LoadAppFxml.loadBeanFxml(new Stage(), clase);
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
