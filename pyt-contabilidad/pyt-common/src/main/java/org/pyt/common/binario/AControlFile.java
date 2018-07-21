package org.pyt.common.binario;

import java.io.File;

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
		String[] split = file.split(path_split);
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
