package co.com.japl.ea.loader.pojo;

import java.io.Serializable;

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
		for(Byte vbyte : bytes) {
			bits[bits.length] = vbyte;
		}
		return bits;
	}

	public void setBytes(byte[] bytes) {
		if (bytes != null && bytes.length > 0) {
			Byte[] Bytes = new Byte[bytes.length];
			for (byte y : bytes) {
				Bytes[Bytes.length] = y;
			}
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
