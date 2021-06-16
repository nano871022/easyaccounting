package co.com.japl.ea.common.properties;
import static co.com.japl.ea.common.properties.LogProperties.instance;
import static co.com.japl.ea.constants.utils.EnviromentProperties.getConsolePrint;
import static co.com.japl.ea.constants.utils.EnviromentProperties.getPath;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.pyt.common.common.WriteFile;



/**
 * Se encarga de escribir en el archivo
 * 
 * @author alejo
 *
 */
public class LogWriter implements Runnable {
	private String nameLogger = "logger/logger.log";
	private String nameLoggerTrace = "logger/stracTrace.log";
	private final static String NAME_PATH_PROPERTIES = "path-log";
	private final static String TRACT_PATH_PROPERTIES = "path-trace";
	private final static String CONST_PROP_MODES = "modes";
	
	private List<String> impresiones;
	private List<Exception> excepciones;
	private Long sleep = (long) 1000;
	private static LogWriter logWriter;
	private Thread hilo;
	private boolean consolePrint = false;
	private String modes;

	private LogWriter() {
		impresiones = new ArrayList<String>();
		excepciones = new ArrayList<Exception>();
		this.consolePrint = getConsolePrint();
	}
	
	public String getModesToPrint() {
		if(modes == null) {
			modes = "ERROR";
		}
		return modes;
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
			String valor = instance().load().get(NAME_PATH_PROPERTIES);
			if (StringUtils.isNotBlank(valor)) {
				nameLogger = valor;
			} 
			String trace = instance().load().get(TRACT_PATH_PROPERTIES);
			if (StringUtils.isNotBlank(trace)) {
				nameLoggerTrace = trace;
			} 
			String mode = instance().load().get(CONST_PROP_MODES);
			if(StringUtils.isNotBlank(mode)) {
				modes = mode;
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
		return new WriteFile().file(getPath()+ nameLogger);
	}

	/**
	 * Se encarga de crear una copia del archivo con otra fecha.
	 * 
	 * @param file {@link File}
	 */
	private final void copyOldFile(File file) {
		if (file.exists()) {
			var d = new Date(file.lastModified());
			var posDotEnd = file.getName().lastIndexOf(".");
			var ext = file.getName().substring(posDotEnd);
			d.setTime(0);
			var now = new Date();
			now.setTime(0);
			var comp = d.before(now);
			if (comp) {
				var sdf = new SimpleDateFormat("yyyyMMdd");
				file.renameTo(new File(nameLoggerTrace.replace(ext, sdf.format(d) + ext)));
			}
		}
	}

	private final <T extends Exception> void printTrace(T excepcion) {
		try {
			File file = new File(getPath()+nameLoggerTrace);
			copyOldFile(file);
			OutputStream out = new FileOutputStream(file, file.exists());
			PrintStream s = new PrintStream(out);
			s.println(excepcion.getMessage());
			excepcion.printStackTrace(s);
			s.close();
			out.close();
		} catch (Exception e) {
			addImpresion("Error presentado en printTrace::" + e.getMessage());
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
				var writer = loadWriter();
				writer.writer(linea);
				if (consolePrint) {
					System.out.println(linea);
				}
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
