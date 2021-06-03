package co.com.japl.ea.common.binario;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

import co.com.japl.ea.common.abstracts.AControlFile;
import co.com.japl.ea.common.abstracts.ADto;
import co.com.japl.ea.exceptions.FileBinException;

/**
 * se encarga de guardar la informacion en los archivos de forma binaria
 * 
 * @author Alejandro Parra
 * @since 21-06-2018
 */
public class WriteBin extends AControlFile{
	/**
	 * Se encarga de guardar el objeto en el archivo
	 * 
	 * @param dto
	 *            {@link ADto} extends
	 * @throws {@link
	 *             FileBinException}
	 */
	public final <T extends Object> void write(String file, T dto) throws FileBinException {
		FileOutputStream fos = null;
		ObjectOutputStream dos = null;
		try {
			if (file != null) {
				fos = new FileOutputStream(file);
				dos = new ObjectOutputStream(fos);
				dos.writeObject(dto);
				dos.close();
				fos.close();
			}
		} catch (IOException e) {
			throw new FileBinException("Se presento un problema en el almacenamiento de la informacion del archivo.",
					e);
		}
	}
}
