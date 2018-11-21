package org.pyt.common.common;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.apache.commons.lang3.StringUtils;

/**
 * Se encarga de escribir en el archivo
 * 
 * @author alejo
 *
 */
public class LogWriter implements Runnable {
	private String nameLogger = "./logger/logger.log";
	private String nameLoggerTrace = "./logger/stracTrace.log";
	private final static String NAME_PATH_PROPERTIES = "path-log";
	private final static String TRACT_PATH_PROPERTIES = "path-trace";
	private List<String> impresiones;
	private List<Exception> excepciones;
	private Long sleep = (long) 1000;
	private static LogWriter logWriter;
	private Thread hilo;

	private LogWriter() {
		impresiones = new ArrayList<String>();
		excepciones = new ArrayList<Exception>();
	}

	/**
	 * Se encarga de obtener una instancia unica
	 * 
	 * @return
	 */
	public final static LogWriter getInstance() {
		if (logWriter == null) {
			logWriter = new LogWriter();
			logWriter.loadProperties();
			logWriter.hilo = new Thread(logWriter);
			logWriter.hilo.start();
		}
		return logWriter;
	}

	/**
	 * Realiza la impresion de las excepciones
	 * 
	 * @param excepciones
	 */
	public final synchronized <T extends Exception> void printStrace(T excepciones) {
		this.excepciones.add(excepciones);
	}

	/**
	 * Se encarga de cargar las propiedades
	 */
	private void loadProperties() {
		try {
			Properties properties = PropertiesUtils.getInstance().setNameProperties("log.properties").load()
					.getProperties();
			String valor = properties.getProperty(NAME_PATH_PROPERTIES);
			if (StringUtils.isNotBlank(valor)) {
				System.out.println("Name Log:" + valor);
				nameLogger = valor;
			} else {
				System.out.println("Se esta tomando el path por defecto " + nameLogger);
			}
			String trace = properties.getProperty(TRACT_PATH_PROPERTIES);
			if (StringUtils.isNotBlank(trace)) {
				System.out.println("Name Log:" + trace);
				nameLoggerTrace = trace;
			} else {
				System.out.println("Se esta tomando el path por defecto " + nameLoggerTrace);
			}
		} catch (Exception e) {
			System.err.println(e);
		}
	}

	/**
	 * Crea la isntancia para cargar el writer
	 * 
	 * @return
	 */
	private WriteFile loadWriter() {
		return new WriteFile().file(nameLogger);
	}

	private final <T extends Exception> void printTrace(T excepcion) {
		try {
			File file = new File(nameLoggerTrace);
			OutputStream out = new FileOutputStream(file);
			PrintStream s = new PrintStream(out);
			excepcion.printStackTrace(s);
			s.close();
			out.close();
		} catch (Exception e) {
			addImpresion("Error presentado en printTrace::"+e.getMessage());
		}
	}

	@Override
	public void run() {
		while (true) {
			if (excepciones.size() > 0) {
				printTrace(excepciones.get(0));
				removeException();
			}
			if (impresiones.size() > 0) {
				String linea = impresiones.get(0);
				loadWriter().writer(linea);
				remove();
			} else {
				try {
					Thread.sleep(sleep);
				} catch (InterruptedException e) {
					System.err.println(e);
				}
			}
		}
	}

	private void remove() {
		if (impresiones.size() > 0) {
			impresiones.remove(0);
		}
	}

	private void removeException() {
		if (excepciones.size() > 0) {
			excepciones.remove(0);
		}
	}

	public synchronized void addImpresion(String impresion) {
		impresiones.add(impresion);
	}
}
