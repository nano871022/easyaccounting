package co.com.japl.ea.loader.interfaces;

import java.util.Arrays;

import co.com.japl.ea.loader.pojo.FilePOJO;

/**
 * Se encarga de implemntar una interface para generalizar el uso de carga de archivos sin importar el tipo de archivo este se cargada dependiedo del archivo
 * @author Alejandro Parra
 * @since 18/11/2018 
 */
public interface IFileLoader {
	/**
	 * Se encarga de recivid el {@link FilePOJO} con los datos del archivos encontrados
	 * @param file {@link FilePOJO}
	 */
	public void setFile(FilePOJO file);
	/**
	 * Se encagra de ejecutar la lectura de los registros por medio de una expresion lamda
	 * @param row {@link ILineRead} expresion lambda
	 * @throws {@link Exception}
	 */
	public void load(ILineRead row)throws Exception;
	/**
	 * Se encarga de agregar el resultado de el cargue 
	 * @param num {@link Integer}
	 * @param codigo {@link String}
	 * @param errores {@link String} {@link Arrays}
	 * @throws {@link Exception}
	 */
	public void addResults(Integer num,String... errores)throws Exception;
	/**
	 * Se encarga de generar un archivo de salida 
	 * @throws {@link Exception}
	 */
	public FilePOJO genFileOut()throws Exception;
}
