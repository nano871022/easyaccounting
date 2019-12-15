package org.pyt.common.common;

import java.io.EOFException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Optional;

import org.pyt.common.properties.LogWriter;

/**
 * Se encarga de manejar el log de l aaplicacion y es transeversal a toda la
 * aplicacion.
 * 
 * @author Alejandro Parra
 * @since 2018-05-24
 */
public final class Log {

	public static enum LEVEL {
		INFO, WARN, ERROR, DEBUG
	};
	private String nameClase;
	private final static String NullPointerExceptionMessage = "Null pointer exception";
	private final static String EOFExceptionMessage = "When open file throw EOF exception";
	private final static String CONST_DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
	private final static String CONST_4STR_FORMAT= "%s %s : %s : %s";
	private final static String CONST_3STR_FORMAT=  "%s.%s:%s";
	private String lastMessage = "";
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
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern(CONST_DATE_TIME_FORMAT);
		String timer = ldt.format(formatter);
		return timer;
	}

	public final synchronized void msnBuild(String msn, LEVEL type) {
		if(Optional.ofNullable(lastMessage).orElse("").contentEquals(msn))return;
		lastMessage = msn;
		if (logWriter.getModesToPrint().contains(type.toString())) {
			String line = String.format(CONST_4STR_FORMAT, now(), nameClase, type, msn);
			logWriter.addImpresion(line);
		}
	}

	/**
	 * Mensaje de warning
	 * 
	 * @param mensaje {@link String}
	 */
	public final synchronized void warn(String mensaje) {
		msnBuild(mensaje, LEVEL.WARN);
	}

	/**
	 * Se encaga de cargar un mensaje en el log
	 * 
	 * @param mensaje {@link String}
	 */
	public final synchronized void logger(String mensaje) {
		msnBuild(mensaje, LEVEL.INFO);
	}

	/**
	 * Se encarga de cargar un error en el log
	 * 
	 * @param error {@link Exception}
	 */
	public final synchronized <T extends Exception> void logger(T error) {
		logWriter.printStrace(error);
		if(error instanceof EOFException){
			msnBuild(EOFExceptionMessage, LEVEL.ERROR);
		}else if (error.getCause() instanceof NullPointerException || error instanceof NullPointerException) {
			msnBuild(NullPointerExceptionMessage, LEVEL.ERROR);
		} else {
			msnBuild(error.getMessage(), LEVEL.ERROR);
		}
		printDEBUGCause(error);
	}

	private final synchronized <T extends Throwable>  void printDEBUGCause(T error) {
		if (error.getCause() != null) {
			if (error.getMessage() != error.getCause().getMessage()) {
				msnBuild(error.getCause().getMessage(), LEVEL.DEBUG);
				if (error.getCause().getStackTrace() != null && error.getCause().getStackTrace().length > 0) {
					Arrays.asList(error.getCause().getStackTrace()).forEach(action -> msnBuild(
							action.getFileName() + "." + action.getMethodName() + ":" + action.getLineNumber(),
							LEVEL.DEBUG));
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
	public final synchronized <T extends Throwable> void logger(T error) {
		if (error.getCause() instanceof NullPointerException) {
			msnBuild(NullPointerExceptionMessage, LEVEL.ERROR);
		} else {
			msnBuild(error.getMessage(), LEVEL.ERROR);
		}
	}

	/**
	 * Se encarga de cargar un error en el log
	 * 
	 * @param error {@link Exception}
	 */
	public final  synchronized<T extends Exception> void error(String error) {
		msnBuild(error, LEVEL.ERROR);
	}

	public final synchronized void info(String mensaje) {
		msnBuild(mensaje, LEVEL.INFO);
	}

	public final synchronized void DEBUG(String mensaje) {
		msnBuild(mensaje, LEVEL.DEBUG);
	}

	public final synchronized <T extends Throwable> void DEBUG(T mensaje) {
		printThrowable(mensaje, LEVEL.DEBUG);
		printSuppressed(LEVEL.DEBUG, mensaje.getSuppressed());
		printStackTrace(LEVEL.DEBUG, mensaje.getStackTrace());
	}

	public final synchronized <T extends Throwable> void DEBUG(String message, T error) {
		msnBuild(message, LEVEL.DEBUG);
		printThrowable(error, LEVEL.DEBUG);
		printSuppressed(LEVEL.DEBUG, error.getSuppressed());
		printStackTrace(LEVEL.DEBUG, error.getStackTrace());
	}

	/**
	 * Se encarga de cargar un error en el log con mensaje adicional
	 * 
	 * @param mensaje {@link String}
	 * @param error   {@link Exception}
	 */
	public final synchronized <T extends Exception> void logger(String mensaje, T error) {
		msnBuild(mensaje, LEVEL.ERROR);
		printThrowable(error, LEVEL.ERROR);
		printSuppressed(LEVEL.ERROR, error.getSuppressed());
		printStackTrace(LEVEL.ERROR, error.getStackTrace());
	}

	private final synchronized void printSuppressed(LEVEL nivel, Throwable... throwable) {
		if(throwable != null && throwable.length > 0) {
			Arrays.asList(throwable).forEach(row -> printThrowable(row, nivel));
		}
	}
	
	private final synchronized void printStackTrace(LEVEL nivel, StackTraceElement... elements) {
		if (elements != null && elements.length > 0) {
			Arrays.asList(elements).forEach(action -> msnBuild(String.format(CONST_3STR_FORMAT, action.getFileName(),
					action.getMethodName(), action.getLineNumber()), nivel));
		}
	}

	private final synchronized void printThrowable(Throwable throwable, LEVEL nivel) {
		if (throwable != null) {
			msnBuild(throwable.getMessage(), nivel);
			printThrowable(throwable.getCause(), nivel);
		}
	}

}
