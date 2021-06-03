package co.com.japl.ea.loader.pojo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.ArrayUtils;

/**
 * Se encarga de tener la informacion del archivo a cargar o procesar
 * 
 * @author Alejandro Parra
 * @since 18/11/2018
 */
public class FilePOJO implements Serializable {
	private static final long serialVersionUID = -5059253920586732639L;
	private Byte[] bytes;
	private String nameFile;
	private String typeFile;
	private Long size;
	private String separate;
	
	public String getSeparate() {
		return separate;
	}

	public void setSeparate(String separate) {
		this.separate = separate;
	}

	public Byte[] getBytes() {
		return bytes;
	}
	
	public byte[] getByte() {
		byte[] bits = new byte[bytes.length];
		bits = ArrayUtils.toPrimitive(bytes);
		return bits;
	}

	public void setBytes(byte[] bytes) {
		List<Byte> lista = new ArrayList<Byte>();
		if (bytes != null && bytes.length > 0) {
			Byte[] Bytes = new Byte[bytes.length];
			for (byte y : bytes) {
				lista.add(y);
			}
			this.bytes = lista.toArray(new Byte[bytes.length]);
		}
	}

	public void setBytes(Byte[] bytes) {
		this.bytes = bytes;
	}

	public String getNameFile() {
		return nameFile;
	}

	public void setNameFile(String nameFile) {
		this.nameFile = nameFile;
	}

	public String getTypeFile() {
		return typeFile;
	}

	public void setTypeFile(String typeFile) {
		this.typeFile = typeFile;
	}

	public Long getSize() {
		return size;
	}

	public void setSize(Long size) {
		this.size = size;
	}

}
