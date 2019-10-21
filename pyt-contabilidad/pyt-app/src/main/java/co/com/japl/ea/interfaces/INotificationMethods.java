package co.com.japl.ea.interfaces;

import org.apache.commons.lang3.StringUtils;
import org.pyt.common.common.Comunicacion;
import org.pyt.common.common.I18n;
import org.pyt.common.common.Log;
import org.pyt.common.constants.AppConstants;

public interface INotificationMethods {
	
	public Log logger();
	
	@SuppressWarnings("rawtypes")
	public Comunicacion comunicacion();
	
	@SuppressWarnings("unchecked")
	default void alerta(String mensaje) {
		comunicacion().setComando(AppConstants.COMMAND_POPUP_WARN, mensaje);
	}

	@SuppressWarnings("unchecked")
	default void notificar(String msn) {
		comunicacion().setComando(AppConstants.COMMAND_POPUP_INFO, msn);
	}

	@SuppressWarnings({ "hiding", "unchecked" })
	default <T extends Exception> void error(T error) {
		String mensaje = error.getMessage();
		logger().logger(error);
		if (StringUtils.isNotBlank(mensaje)) {
			comunicacion().setComando(AppConstants.COMMAND_POPUP_ERROR, mensaje);
		}
	}

	@SuppressWarnings("unchecked")
	default void error(String msn) {
		logger().logger(msn);
		comunicacion().setComando(AppConstants.COMMAND_POPUP_ERROR, msn);
	}

	@SuppressWarnings("unchecked")
	default void error(Throwable e) {
		String mensaje = e.getMessage();
		logger().logger(e);
		if (StringUtils.isNotBlank(mensaje)) {
			comunicacion().setComando(AppConstants.COMMAND_POPUP_ERROR, mensaje);
		}
	}
	/**
	 * Retorna los {@link I18n} configutrado para manejar la internacionalizacion
	 * @return
	 */
	public I18n i18n();
}