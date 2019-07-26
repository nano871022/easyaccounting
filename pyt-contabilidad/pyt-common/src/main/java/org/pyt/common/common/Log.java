package org.pyt.common.common;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;

import org.pyt.common.properties.LogWriter;

/**
 * Se encarga de manejar el log de l aaplicacion y es transeversal a toda la
 * aplicacion.
 * 
 * @author Alejandro Parra
 * @since 2018-05-24
 */
public final class Log {

	private String nameClase;
	private final static String INFO = "INFO";
	private final static String WARN = "WARN";
	private final static String ERROR = "ERROR";
	private final static String DEBUG = "DEBUG";
	private LogWriter logWriter;

	private Log(Class clase) {
		nameClase = clase.getName();
		logWriter = LogWriter.getInstance();
	}

	public final static Log Log(Class clase) {
		return new Log(clase);
	}

	private final String now() {
		LocalDateTime ldt = LocalDateTime.now();
		String pattern = "yyyy-MM-dd HH:mm:ss";
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
		String timer = ldt.format(formatter);
		return timer;
	}

	public final synchronized void msnBuild(String msn, String type) {
		if (logWriter.getModesToPrint().contains(type)) {
			String line = String.format("%s %s : %s : %s", now(), nameClase, type, msn);
			logWriter.addImpresion(line);
		}
	}

	/**
	 * Mensaje de warning
	 * 
	 * @param mensaje {@link String}
	 */
	public final void warn(String mensaje) {
		msnBuild(mensaje, WARN);
	}

	/**
	 * Se encaga de cargar un mensaje en el log
	 * 
	 * @param mensaje {@link String}
	 */
	public final void logger(String mensaje) {
		msnBuild(mensaje, INFO);
	}

	/**
	 * Se encarga de cargar un error en el log
	 * 
	 * @param error {@link Exception}
	 */
	public final <T extends Exception> void logger(T error) {
		logWriter.printStrace(error);
		if (error.getCause() instanceof NullPointerException || error instanceof NullPointerException) {
			msnBuild("Null pointer exception", ERROR);
		} else {
			msnBuild(error.getMessage(), ERROR);
		}
		printDEBUGCause(error);
	}

	private final <T extends Throwable> void printDEBUGCause(T error) {
		if (error.getCause() != null) {
			if (error.getMessage() != error.getCause().getMessage()) {
				msnBuild(error.getCause().getMessage(), DEBUG);
				if (error.getCause().getStackTrace() != null && error.getCause().getStackTrace().length > 0) {
					Arrays.asList(error.getCause().getStackTrace()).forEach(action -> msnBuild(
							action.getFileName() + "." + action.getMethodName() + ":LN_" + action.getLineNumber(),
							DEBUG));
				}
				printDEBUGCause(error.getCause());
			} else {
				printDEBUGCause(error.getCause());
			}
		}
	}

	/**
	 * Se encarga de cargar un error en el log
	 * 
	 * @param error {@link Exception}
	 */
	public final <T extends Throwable> void logger(T error) {
		if (error.getCause() instanceof NullPointerException) {
			msnBuild("Null pointer exception", ERROR);
		} else {
			msnBuild(error.getMessage(), ERROR);
		}
	}

	/**
	 * Se encarga de cargar un error en el log
	 * 
	 * @param error {@link Exception}
	 */
	public final <T extends Exception> void error(String error) {
		msnBuild(error, ERROR);
	}

	public final void info(String mensaje) {
		msnBuild(mensaje, INFO);
	}

	public final void DEBUG(String mensaje) {
		msnBuild(mensaje, DEBUG);
	}

	/**
	 * Se encarga de cargar un error en el log con mensaje adicional
	 * 
	 * @param mensaje {@link String}
	 * @param error   {@link Exception}
	 */
	public final <T extends Exception> void logger(String mensaje, T error) {
		msnBuild(mensaje, ERROR);
		Arrays.asList(error.getStackTrace()).forEach(action -> msnBuild(
				action.getFileName() + "." + action.getMethodName() + ":LN_" + action.getLineNumber(), DEBUG));
	}

}
