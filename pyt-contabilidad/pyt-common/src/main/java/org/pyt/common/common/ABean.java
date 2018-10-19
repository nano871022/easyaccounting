package org.pyt.common.common;

import javax.swing.plaf.metal.MetalBorders.PopupMenuBorder;

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
	 * @param classOfBean {@link Class}
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
	 * @param classOfBean {@link Class}
	 * @return {@link ABean}
	 */
	public <M extends Pane,L extends ADto, S extends ABean<L>> S getController(M layout,Class<S> classOfBean) {
		try {
			return LoadAppFxml.beanFxmlPane(layout, classOfBean);
		} catch (LoadAppFxmlException e) {
			Log.logger("El bean " + classOfBean.getName() + " no puede ser cargado.", e);
			error(e);
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	public void notificar(String msn) {
		System.out.println(msn);
		comunicacion.setComando(AppConstants.COMMAND_MSN_IZQ, msn);
	}

	@SuppressWarnings("hiding")
	public <T extends Exception> void error(T error) {
		Log.logger(error);
		System.err.println(error);
	}

	@SuppressWarnings("unchecked")
	public void error(String msn) {
		Log.logger(msn);
		System.err.println(msn);
		comunicacion.setComando(AppConstants.COMMAND_MSN_CTR, msn);
	}

	public void error(Throwable e) {

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
