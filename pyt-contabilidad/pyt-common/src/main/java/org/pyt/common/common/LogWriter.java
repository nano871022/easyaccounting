package org.pyt.common.common;

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
	private final static String NAME_PATH_PROPERTIES = "path-log";
	private List<String> impresiones;
	private Long sleep = (long) 1000;
	private static LogWriter logWriter;
	private Thread hilo;
	private LogWriter() {
		impresiones = new ArrayList<String>();
	}
	/**
	 * Se encarga de obtener una instancia unica
	 * @return
	 */
	public final static LogWriter getInstance() {
		if(logWriter == null) {
			logWriter = new LogWriter();
			logWriter.loadProperties();
			logWriter.hilo = new Thread(logWriter);
			logWriter.hilo.start();
		}
		return logWriter;
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
				System.out.println("Name Log:"+valor);
				nameLogger = valor;
			} else {
				System.out.println("Se esta tomando el path por defecto " + nameLogger);
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

	@Override
	public void run() {
		while (true) {
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

	public synchronized void addImpresion(String impresion) {
		impresiones.add(impresion);
	}
}
