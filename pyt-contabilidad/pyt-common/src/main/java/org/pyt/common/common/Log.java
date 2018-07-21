package org.pyt.common.common;

import java.io.File;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

/**
 * Se encarga de manejar el log de l aaplicacion y es transeversal a toda la
 * aplicacion.
 * 
 * @author Alejandro Parra
 * @since 2018-05-24
 */
public final class Log {
	private Logger log = Logger.getLogger("application_logger.log");
	private static Log logger;
	private final static String properties = "../pyt-common/src/resource/log4j.properties";

	private final static Log Log() {
		if (logger == null) {
			logger = new Log();
			File f = new File(properties);
			System.out.println(f.getAbsolutePath() +" - "+f.exists());
			PropertyConfigurator.configure(properties);
		}
		return logger;
	}
	/**
	 * Mensaje de warning
	 * @param mensaje {@link String}
	 */
	public final static void warn(String mensaje) {
		Log().getLogger().warn(mensaje);
	}
	
	/**
	 * Se encaga de cargar un mensaje en el log
	 * 
	 * @param mensaje
	 *            {@link String}
	 */
	public final static void logger(String mensaje) {
		Log().getLogger().info(mensaje);
	}

	/**
	 * Se encarga de cargar un error en el log
	 * 
	 * @param error
	 *            {@link Exception}
	 */
	public final static <T extends Exception> void logger(T error) {
		Log().getLogger().error(error);
	}

	/**
	 * Se encarga de cargar un error en el log
	 * 
	 * @param error
	 *            {@link Exception}
	 */
	public final static <T extends Exception> void error(String error) {
		Log().getLogger().error(error);
	}

	/**
	 * Se encarga de cargar un error en el log con mensaje adicional
	 * 
	 * @param mensaje
	 *            {@link String}
	 * @param error
	 *            {@link Exception}
	 */
	public final static <T extends Exception> void logger(String mensaje, T error) {
		Log().getLogger().error(mensaje, error);;
	}

	public final Logger getLogger() {
		return log;
	}

}
