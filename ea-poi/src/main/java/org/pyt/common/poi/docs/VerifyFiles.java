package org.pyt.common.poi.docs;

import java.io.File;
import java.io.IOException;

import org.apache.commons.lang3.StringUtils;
import org.pyt.common.common.Log;

/**
 * Se encarga de verificar la existencia del archivo
 * @author Alejandro Parra
 * @since 27-07-2018
 */
public abstract class VerifyFiles {
	private final static String DOT = ".";
	private final static String SLASH = "/";
	private Log logger = Log.Log(this.getClass());

	protected String separator = System.getProperty("file.separator");
	/**
	 * Se encarga de validar el archivo si existe y si no crearlo
	 * 
	 * @param files
	 *            {@link String} ruta del archivo
	 * @return {@link Boolean}
	 */
	protected final Boolean validFile(String files,Boolean createFile) {
		String nameFile = files;
		String[] split = null;
		if (files.contains(SLASH) && !SLASH.equalsIgnoreCase(separator)) {
			nameFile = files.replace(SLASH, separator);
		}
		if (new File(nameFile).exists()) {
			return true;
		}
		try {
			split = nameFile.split(separator);
		} catch (Exception e) {
			try {
				split = nameFile.split(separator + separator);
			} catch (Exception e1) {
				throw e1;
			}
		}
		if (split != null && split.length > 1) {
			StringBuilder paths = new StringBuilder();
			File file = null;
			try {
				for (String path : split) {
					if (paths.length() > 0) {
						paths.append(separator);
					}
					if(StringUtils.isNotBlank(path)) {
						paths.append(path);
					}else {
						paths.append(separator);
						continue;
					}
					
					if (path.equalsIgnoreCase(DOT)) {
						continue;
					}
					file = new File(paths.toString());
					if (!file.exists() && !path.contains(DOT)) {
						file.mkdirs();
					} else if (!file.exists() && path.contains(DOT) && createFile) {
						if (file.createNewFile()) {
							return true;
						}
					}
				}
			} catch (IOException e) {
				logger.logger(e);
			}
		}
		return false;
	}

}
