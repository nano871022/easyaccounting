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

	private String nameClase;
	private final static String INFO = "INFO";
	private final static String WARN = "WARN";
	private final static String ERROR = "ERROR";
	private final static String DEBUG = "DEBUG";
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

	public final synchronized void msnBuild(String msn, String type) {
		if(Optional.ofNullable(lastMessage).orElse("").contentEquals(msn))return;
		lastMessage = msn;
		if (logWriter.getModesToPrint().contains(type)) {
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
		msnBuild(mensaje, WARN);
	}

	/**
	 * Se encaga de cargar un mensaje en el log
	 * 
	 * @param mensaje {@link String}
	 */
	public final synchronized void logger(String mensaje) {
		msnBuild(mensaje, INFO);
	}

	/**
	 * Se encarga de cargar un error en el log
	 * 
	 * @param error {@link Exception}
	 */
	public final synchronized <T extends Exception> void logger(T error) {
		logWriter.printStrace(error);
		if(error instanceof EOFException){
			msnBuild(EOFExceptionMessage, ERROR);
		}else if (error.getCause() instanceof NullPointerException || error instanceof NullPointerException) {
			msnBuild(NullPointerExceptionMessage, ERROR);
		} else {
			msnBuild(error.getMessage(), ERROR);
		}
		printDEBUGCause(error);
	}

	private final synchronized <T extends Throwable>  void printDEBUGCause(T error) {
		if (error.getCause() != null) {
			if (error.getMessage() != error.getCause().getMessage()) {
				msnBuild(error.getCause().getMessage(), DEBUG);
				if (error.getCause().getStackTrace() != null && error.getCause().getStackTrace().length > 0) {
					Arrays.asList(error.getCause().getStackTrace()).forEach(action -> msnBuild(
							action.getFileName() + "." + action.getMethodName() + ":" + action.getLineNumber(),
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
	public final synchronized <T extends Throwable> void logger(T error) {
		if (error.getCause() instanceof NullPointerException) {
			msnBuild(NullPointerExceptionMessage, ERROR);
		} else {
			msnBuild(error.getMessage(), ERROR);
		}
	}

	/**
	 * Se encarga de cargar un error en el log
	 * 
	 * @param error {@link Exception}
	 */
	public final  synchronized<T extends Exception> void error(String error) {
		msnBuild(error, ERROR);
	}

	public final synchronized void info(String mensaje) {
		msnBuild(mensaje, INFO);
	}

	public final synchronized void DEBUG(String mensaje) {
		msnBuild(mensaje, DEBUG);
	}

	/**
	 * Se encarga de cargar un error en el log con mensaje adicional
	 * 
	 * @param mensaje {@link String}
	 * @param error   {@link Exception}
	 */
	public final synchronized <T extends Exception> void logger(String mensaje, T error) {
		msnBuild(mensaje, ERROR);
		printThrowable(error);
		if (error.getSuppressed() != null && error.getSuppressed().length > 0) {
			Arrays.asList(error.getSuppressed()).forEach(row -> printThrowable(row));
		}
		Arrays.asList(error.getStackTrace()).forEach(action -> 
		msnBuild(
				String.format(CONST_3STR_FORMAT, action.getFileName(), action.getMethodName(), action.getLineNumber())
				, DEBUG));
	}

	public final synchronized void printThrowable(Throwable throwable) {
		if (throwable != null) {
			msnBuild(throwable.getMessage(), ERROR);
			printThrowable(throwable.getCause());
		}
	}

}
