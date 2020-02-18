package com.pyt.query.implement;


import java.util.ArrayList;
import java.util.List;

import org.pyt.common.abstracts.ABin;
import org.pyt.common.abstracts.ADto;
import org.pyt.common.binario.ReadBin;
import org.pyt.common.binario.WriteBin;
import org.pyt.common.exceptions.FileBinException;

/**
 * Se encarga de leer y escribir archivos binarios con objetos dto
 * 
 * @author Alejandro Parra
 * @since 22-06-2018
 */
public class FileBin extends ABin {
	private WriteBin wb;
	private ReadBin rb;

	public FileBin() {
		wb = new WriteBin();
		rb = new ReadBin();
	}

	/**
	 * Se encarga de guardar la lista en el archivo indicado
	 * 
	 * @param list
	 *            {@link List}
	 * @throws {@link
	 *             FileBinException}
	 */
	public final <T extends ADto> void write(List<T> list,Class<T> clas) throws FileBinException {
		if (list.size() == 0) {
			list = new ArrayList<T>();
			wb.write(this.getNameFile(clas), list);
		} else {
			wb.write(this.getNameFile(clas), list);
		}
	}

	/**
	 * Se encarga de obtener la informacion del archivo almacenado en el binario
	 * 
	 * @param dto
	 * @return
	 * @throws FileBinException
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public final <T extends ADto> List<T> loadRead(Class<T> dto) throws FileBinException {
		Object out = rb.read(this.getNameFile(dto));
		if (out instanceof List) {
			if (((List) out).size() > 0) {
				Object o = ((List) out).get(0);
				try {
					T t = (T) o;
					return (List<T>) out;
				} catch (ClassCastException e) {
					throw new FileBinException("No se puede verificar el tipo de objeto de la lista.", e);
				}
			}
		}
		return null;
	}

}
