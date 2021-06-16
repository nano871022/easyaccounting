package org.pyt.common.common;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Se encarga de escribir un archivo de texto
 * 
 * @author Alejandro Parra
 * @since 2018-07-22
 */
public final class WriteFile {
	private String namefile;
	private String separator;
	private final static String SLASH = "/";
	private final static String DOUBLE_BACKSLASH = "\\";
	private final static String LINE_JUMP = "\n";
	private final static String DOT = ".";
	private final static String DOT_LOG = ".log";
	private final static String EMPTY = "";
	private final static String FORMAT_DATE_FILE = "yyyyMMdd";

	public WriteFile() {
		separator = System.getProperty("file.separator");
	}

	public final WriteFile file(String file) {
		this.namefile = file;
		return this;
	}

	/**
	 * Se encarga de crear una copia del archivo con otra fecha.
	 * 
	 * @param file {@link File}
	 */
	private final void copyOldFile(File file) {
		if (file.exists()) {
			var d = new Date(file.lastModified());
			var posDotEnd = file.getName().lastIndexOf(DOT);
			var ext = file.getName().substring(posDotEnd);
			d.setTime(0);
			var now = new Date();
			now.setTime(0);
			var comp = d.before(now);
			if (comp) {
				var sdf = new SimpleDateFormat(FORMAT_DATE_FILE);
				file.renameTo(new File(namefile.replace(ext, sdf.format(d) + ext)));
			}
		}
	}

	public final void writer(String msn) {
		try {
			var pathFile = Paths.get(namefile).normalize().toAbsolutePath();
			OutputStream os = null;
			if (pathFile.toFile().exists()) {
				copyOldFile(pathFile.toFile());
			}
			if(pathFile.toFile().exists()) {
				os = new  FileOutputStream(pathFile.toFile(),true);
			}else {
				os = Files.newOutputStream(pathFile,StandardOpenOption.CREATE_NEW);
			}
			msn += LINE_JUMP;
//			os = new FileOutputStream(file, true);
			os.write(msn.getBytes());
			os.close();
		} catch (IOException e) {
			System.err.println(e);
		}
	}
}
