package org.pyt.common.common;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Se encarga de manejar el log de l aaplicacion y es transeversal a toda la
 * aplicacion.
 * 
 * @author Alejandro Parra
 * @since 2018-05-24
 */
public final class Log {
	private static Log logger;
	private WriteFile writer;
	private final static String properties = "properties/log4j.properties";
	private final static String propertie = "properties/data.properties";
	private final static String INFO = "INFO";
	private final static String WARN = "WARN";
	private final static String ERROR = "ERROR";

	private Log() {
		writer = new WriteFile();
		writer.file("./logger/logger.log");
	}

	private final static Log Log() {
		if (logger == null) {
			logger = new Log();
		}
		return logger;
	}

	private final String now() {
		LocalDateTime ldt = LocalDateTime.now();
		String pattern = "yyyy-MM-dd HH:mm:ss";
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
		String timer = ldt.format(formatter);
		return timer;
	}

	public final void msnBuild(String msn, String type) {
		String line = String.format("%s %s %s", now(), type, msn);
		Log().getWriteFile().writer(line);
	}

	/**
	 * Mensaje de warning
	 * 
	 * @param mensaje
	 *            {@link String}
	 */
	public final static void warn(String mensaje) {
		Log().msnBuild(mensaje, WARN);
	}

	/**
	 * Se encaga de cargar un mensaje en el log
	 * 
	 * @param mensaje
	 *            {@link String}
	 */
	public final static void logger(String mensaje) {
		Log().msnBuild(mensaje, INFO);
	}

	/**
	 * Se encarga de cargar un error en el log
	 * 
	 * @param error
	 *            {@link Exception}
	 */
	public final static <T extends Exception> void logger(T error) {
		if (error.getCause() instanceof NullPointerException) {
			Log().msnBuild("Null pointer exception", ERROR);
		} else {
			Log().msnBuild(error.getMessage(), ERROR);
		}
	}
	
	/**
	 * Se encarga de cargar un error en el log
	 * 
	 * @param error
	 *            {@link Exception}
	 */
	public final static <T extends Throwable> void logger(T error) {
		if (error.getCause() instanceof NullPointerException) {
			Log().msnBuild("Null pointer exception", ERROR);
		} else {
			Log().msnBuild(error.getMessage(), ERROR);
		}
	}

	/**
	 * Se encarga de cargar un error en el log
	 * 
	 * @param error
	 *            {@link Exception}
	 */
	public final static <T extends Exception> void error(String error) {
		Log().msnBuild(error, ERROR);
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
		Log().msnBuild(mensaje, ERROR);
	}

	public final WriteFile getWriteFile() {
		return writer;
	}
}
