package org.pyt.common.common;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
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
		OutputStream os = null;
		if (validFile()) {
			File file = new File(namefile);
			copyOldFile(file);
			try {
				msn += LINE_JUMP;
				os = new FileOutputStream(file, true);
				os.write(msn.getBytes());
				os.close();
			} catch (FileNotFoundException e) {
			} catch (IOException e) {
			}
		}
	}

	private final Boolean validFile() {
		File f = null;
		String[] splits = null;
		StringBuilder path = new StringBuilder();
		if (namefile.contains(SLASH)) {
			splits = namefile.split(SLASH);
		}
		if (namefile.contains(DOUBLE_BACKSLASH)) {
			splits = namefile.split(DOUBLE_BACKSLASH);
		}
		if (splits != null && splits.length > 0) {
			for (String split : splits) {
				if (path.length() > 0) {
					path.append(separator);
				}
				path.append(split);
				if (split.equalsIgnoreCase(DOT))
					continue;
				f = new File(path.toString());
				if (!f.exists()) {
					if (!path.toString().contains(DOT_LOG)) {
						f.mkdirs();
//						System.out.println("Ruta log::"+f.getAbsolutePath());
					} else {
						try {
							OutputStream os = new FileOutputStream(f);
							os.write(EMPTY.getBytes());
							os.flush();
							os.close();
						} catch (IOException e) {
							return false;
						}
					}
				}
			}
		} else {
			return false;
		}
		return true;
	}
}
