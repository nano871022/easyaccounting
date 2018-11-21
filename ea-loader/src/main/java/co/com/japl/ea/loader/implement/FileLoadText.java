package co.com.japl.ea.loader.implement;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;

import co.com.japl.ea.loader.interfaces.IFileLoader;
import co.com.japl.ea.loader.interfaces.ILineRead;
import co.com.japl.ea.loader.pojo.FilePOJO;

public class FileLoadText implements IFileLoader {
	private FilePOJO file;
	private final static String charset = "UTF-8";
	public void setFile(FilePOJO file) {
		this.file = file;
	}

	public void load(ILineRead row) throws Exception{
		ByteArrayInputStream bais = new ByteArrayInputStream(file.getByte());
		InputStream is = bais;
		BufferedReader bf = new BufferedReader(new InputStreamReader(is, charset));
		String line = null;
		while((line = bf.readLine()) != null){
			row.getRow(line);
		}
	}

}
