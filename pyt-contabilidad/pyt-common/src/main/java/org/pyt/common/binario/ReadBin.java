package org.pyt.common.binario;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;

import org.pyt.common.exceptions.FileBinException;

/**
 * Se encarga de leer archivos binarios para poder ser proesados
 * 
 * @author Alejandro Parra
 * @since 22-06-2018
 */
public class ReadBin {
	/**
	 * Se encarga de leer el archivo
	 * 
	 * @param dto
	 * @throws FileBinException
	 */
	@SuppressWarnings({ "unchecked" })
	public final <T extends Object> T read(String file) throws FileBinException {
		FileInputStream fis = null;
		ObjectInputStream ois = null;
		T obj = null;
		try {
			if (file != null) {
				File dat = new File(file);
				if (!dat.exists()) {
					dat.createNewFile();
				}
				fis = new FileInputStream(file);
				if (fis.getChannel().size() > 0) {
					ois = new ObjectInputStream(fis);
					obj = (T) ois.readObject();
					ois.close();
				}
				fis.close();
			}
		} catch (FileNotFoundException e) {
			throw new FileBinException("Se presento problema en la lectura del archivo.", e);
		} catch (IOException e) {
			throw new FileBinException("Se presento problema en la lectura del archivo.", e);
		} catch (ClassNotFoundException e) {
			try {
				ois.close();
			} catch (IOException e1) {
				throw new FileBinException("Se presento problema en la lectura.", e);
			}
			throw new FileBinException("Se presento problema en la lectura del archivo.", e);
		}
		return obj;
	}

}
