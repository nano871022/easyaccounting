package co.com.japl.ea.loader.implement;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;

import co.com.japl.ea.loader.interfaces.IFileLoader;
import co.com.japl.ea.loader.interfaces.ILineRead;
import co.com.japl.ea.loader.pojo.FilePOJO;
import co.com.japl.ea.loader.pojo.ResultsPOJO;

public class FileLoadText implements IFileLoader {
	private FilePOJO file;
	private List<ResultsPOJO> resultados;

	private final static String charset = "UTF-8";

	public void setFile(FilePOJO file) {
		this.file = file;
	}

	public void load(ILineRead row) throws Exception {
		ByteArrayInputStream bais = new ByteArrayInputStream(file.getByte());
		InputStream is = bais;
		BufferedReader bf = new BufferedReader(new InputStreamReader(is, charset));
		String line = null;
		Integer numLine = 1;
		while ((line = bf.readLine()) != null) {
			row.getRow(line, numLine);
			numLine++;
		}
	}

	public void addResults(Integer num, String... errores) throws Exception {
		if (resultados == null) {
			resultados = new ArrayList<ResultsPOJO>();
		}
		resultados.add(new ResultsPOJO(num, errores));
	}

	public FilePOJO genFileOut() throws Exception {
		if (resultados == null || resultados.size() == 0)
			throw new Exception("No se ha cargado los resultados del ingreso de registros.");
		FilePOJO fileOutput = new FilePOJO();
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		ByteArrayInputStream bais = new ByteArrayInputStream(file.getByte());
		InputStream is = bais;
		BufferedReader bf = new BufferedReader(new InputStreamReader(is, charset));
		String line = null;
		Integer numLine = 1;
		while ((line = bf.readLine()) != null) {
			for (ResultsPOJO resultado : resultados) {
				if (resultado.getNumLine() == numLine) {
					line += file.getSeparate()
							+ (resultado.getErrores() == null || resultado.getErrores().size() == 0 ? "OK"
									: String.join(file.getSeparate(),
											resultado.getErrores().toArray(new String[resultado.getErrores().size()])));
					baos.write(line.getBytes());
					break;
				}
			}
			numLine++;
		}
		baos.close();
		fileOutput.setBytes(baos.toByteArray());
		fileOutput.setNameFile("output_" + file.getNameFile());
		fileOutput.setSeparate(file.getSeparate());
		fileOutput.setTypeFile(file.getTypeFile());
		fileOutput.setSize(Long.valueOf(baos.size()));
		return fileOutput;
	}
}
