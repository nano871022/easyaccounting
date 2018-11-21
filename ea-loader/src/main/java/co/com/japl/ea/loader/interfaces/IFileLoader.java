package co.com.japl.ea.loader.interfaces;

import java.util.List;

import co.com.japl.ea.loader.pojo.FilePOJO;

/**
 * Se encarga de implemntar una interface para generalizar el uso de carga de archivos sin importar el tipo de archivo este se cargada dependiedo del archivo
 * @author Alejandro Parra
 * @since 18/11/2018 
 */
public interface IFileLoader {
	
	public void setFile(FilePOJO file);
	public void load(ILineRead row)throws Exception;
}
