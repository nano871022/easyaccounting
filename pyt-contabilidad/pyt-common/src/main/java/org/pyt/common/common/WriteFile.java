package org.pyt.common.common;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;

/**
 * Se encarga de escribir un archivo de texto
 * 
 * @author Alejandro Parra
 * @since 2018-07-22
 */
public final class WriteFile {
	private String namefile;
	private String separator;

	public WriteFile() {
		separator = System.getProperty("file.separator");
	}

	public final WriteFile file(String file) {
		this.namefile = file;
		return this;
	}
	
	public final void writer(String msn) {
		OutputStream os = null;
		if (validFile()) {
			File file = new File(namefile);
			try {
				msn += "\n";
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
		if (namefile.contains("/")) {
			splits = namefile.split("/");
		}
		if (namefile.contains("\\")) {
			splits = namefile.split("\\");
		}
		if (splits != null && splits.length > 0) {
			for (String split : splits) {
				if (path.length() > 0) {
					path.append(separator);
				}
				path.append(split);
				if(split.equalsIgnoreCase("."))
					continue;
				f = new File(path.toString());
				if (!f.exists()) {
					if (!path.toString().contains(".log")) {
						f.mkdirs();
					} else {
						try {
							OutputStream os = new FileOutputStream(f);
							os.write("".getBytes());
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
