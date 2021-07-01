package co.com.japl.ea.interfaces;

import java.util.function.Consumer;

import org.apache.commons.lang3.StringUtils;
import org.pyt.common.constants.AppConstants;

import co.com.japl.ea.app.components.PopupGenBean;
import co.com.japl.ea.beans.abstracts.ABean;
import co.com.japl.ea.beans.abstracts.AGenericInterfacesReflectionBean;
import co.com.japl.ea.beans.abstracts.AGenericToBean;
import co.com.japl.ea.common.abstracts.ADto;
import co.com.japl.ea.common.reflection.Reflection;
import co.com.japl.ea.controllers.LocatorController;
import co.com.japl.ea.exceptions.LoadAppFxmlException;
import co.com.japl.ea.utls.LoadAppFxml;
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
public interface IBean<T extends ADto> extends Reflection, INotificationMethods {
	/**
	 * Se encarga de obtener el fxml y el controlador para ponerlo sobre la pantalla
	 * 
	 * @param classOfBean {@link Class}
	 * @return {@link ABean}
	 */
	default <L extends ADto, S extends ABean<L>> S getController(Class<S> classOfBean) {
		try {
			return LoadAppFxml.BeanFxmlScroller(null, classOfBean);
		} catch (LoadAppFxmlException e) {
			logger().error("El bean " + classOfBean.getName() + " no puede ser cargado.");
			error(e);
		}
		return null;
	}

	/**
	 * Se encarga de realizar la invoicacion para obtener o introducir datos a un
	 * bean que se encuentre en uso
	 * 
	 * @param caller {@link String} anotacion bean . metodo
	 * @param value  {@link Object} valores que se desean inyectar en el metodo
	 * @return {@link Object} valor retornado cuando es un get
	 * @throws {@link Exception}
	 */
	@SuppressWarnings({ "unchecked", "rawtypes", "hiding" })
	default <T extends Object, S extends Object> S caller(String caller, T... value) throws Exception {
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

	default <T> void caller(Consumer<T> caller, T... value) throws Exception {
		Class<?> clase = this.getClass();
		if (value.length == 0) {
			caller.accept(null);
		} else if (value.length == 1) {
			caller.accept(value[0]);
		} else {
			throw new Exception("Se suministraron varios objetos, solo se necesita uno.");
		}
	}

	default void destroy() {
		try {
			LocatorController.getInstance().removeLoadInController(meThis());
		} catch (Exception e) {
			mensajeIzquierdo(e);
		}
	}

	/**
	 * Se encarga de obtener el fxml y el controlador para ponerlo sobre la pantalla
	 * 
	 * @param classOfBean {@link Class}
	 * @return {@link ABean}
	 */
	default <M extends Pane, L extends ADto, S extends ABean<L>> S getController(M layout, Class<S> classOfBean) {
		try {
			return LoadAppFxml.beanFxmlPane(layout, classOfBean);
		} catch (LoadAppFxmlException e) {
			logger().logger("El bean " + classOfBean.getName() + " no puede ser cargado.", e);
			error(e);
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	default <S extends ADto, P extends PopupGenBean<S>> P controllerGenPopup(Class<S> clazz) throws Exception {
		return (P) controllerPopup(new PopupGenBean<S>(clazz));
	}

	/**
	 * Obtener controlador cuando se extiende de una clase creada con campos
	 * configurados por pantalla de genericos
	 * 
	 * @param <D>
	 * @param <S>
	 * @param genericBean
	 * @return
	 */
	default <D extends ADto, S extends AGenericInterfacesReflectionBean<D>> S controllerPopup(S genericBean) {
		try {
			return LoadAppFxml.loadBeanFX(genericBean);
		} catch (LoadAppFxmlException e) {
			logger().logger("El bean " + genericBean.getClass().getName() + " no puede ser cargado.", e);
			error(e);
		}
		return null;
	}

	default <D extends ADto, S extends AGenericToBean<D>> S controllerPopup(S genericBean) {
		try {
			return LoadAppFxml.loadBeanFX(genericBean);
		} catch (LoadAppFxmlException e) {
			logger().logger("El bean " + genericBean.getClass().getName() + " no puede ser cargado.", e);
			error(e);
		}
		return null;
	}

	@SuppressWarnings({ "unchecked", "hiding" })
	default <T extends Exception> void mensajeIzquierdo(T error) {
		String mensaje = error.getMessage();
		logger().logger(error);
		if (StringUtils.isNotBlank(mensaje)) {
			comunicacion().setComando(AppConstants.COMMAND_MSN_IZQ, mensaje);
		}
	}

	@SuppressWarnings({ "unchecked", "hiding" })
	default <T extends Throwable> void mensajeIzquierdo(T error) {
		String mensaje = error.getMessage();
		logger().logger(error);
		if (StringUtils.isNotBlank(mensaje)) {
			comunicacion().setComando(AppConstants.COMMAND_MSN_IZQ, mensaje);
		}
	}

	@SuppressWarnings("unchecked")
	default void mensajeIzquierdo(String mensaje) {
		comunicacion().setComando(AppConstants.COMMAND_MSN_IZQ, mensaje);
	}

	@SuppressWarnings("unchecked")
	default void mensajeDerecho(String mensaje) {
		comunicacion().setComando(AppConstants.COMMAND_MSN_DER, mensaje);
	}

	@SuppressWarnings("unchecked")
	default void mensajeCentral(String mensaje) {
		comunicacion().setComando(AppConstants.COMMAND_MSN_CTR, mensaje);
	}

	@SuppressWarnings("unchecked")
	default void progreso(Double valor) {
		comunicacion().setComando(AppConstants.COMMAND_PROGRESS, valor);
	}

	/**
	 * Se encarga de cargar de crear una ventana con la informacion de un fxml y
	 * retorna el bean de despliegue creado
	 * 
	 * @param clase extends {@link ABean}
	 * @return extends {@link ABean} object
	 * @throws {@link LoadAppFxmlException}
	 */
	@SuppressWarnings({ "unchecked", "rawtypes", "hiding" })
	default <T extends ABean> T controllerPopup(Class<T> clase) throws LoadAppFxmlException {
		return (T) LoadAppFxml.loadBeanFxml(new Stage(), clase);
	}

	public T getRegistro();

	public void setRegistro(T registro);

	public String getNombreVentana();

	public void setNombreVentana(String nombreVentana);

	@SuppressWarnings("hiding")
	public <T extends Object> T meThis();

}
