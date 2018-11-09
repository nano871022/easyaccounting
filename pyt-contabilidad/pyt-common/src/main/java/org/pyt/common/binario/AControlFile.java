package org.pyt.common.binario;

import java.io.File;
import java.util.regex.PatternSyntaxException;

import org.pyt.common.common.Log;

/**
 * Se encarga de realziar algunos controles sobre los archivos
 * @author Alejandro para
 * @since 08-07-2018
 */
public abstract class AControlFile {
	private String path_split = "/";
	public AControlFile() {
		path_split = System.getProperty("file.separator");
	}
	/**
	 * Se encarga de verificar si la ruta existe, si no existe la crea.
	 * @param file {@link String}
	 */
	protected void verifyPath(String file) {
		String[] split = null;
		if(file.contains("/")  && !"/".equalsIgnoreCase(path_split)) {
			file = file.replace("/",path_split);
		}
		if(file.contains(path_split)) {
			try {
				split = file.split(path_split);
			}catch(PatternSyntaxException e) {
				try {
					
					split = file.split(path_split+path_split);
				}catch(Exception e1) {
					Log.logger(e1);
				}
			}catch(Exception e) {
				Log.logger(e);
			}
		}
		String path = "";
		File paths = null;
		String dir = "";
		if(split.length > 0) {
			for(int i = 0; i < split.length - 1 ; i++) {
				if(split[i].contains(".")) {
					path = "."+path_split;
				}else {
					dir = path+split[i];
					paths = new File(dir);
					if(!paths.exists()) {
						if(paths.mkdirs()) {
							path += path_split;
						}
					}
				}
			}
		}
	}
}
