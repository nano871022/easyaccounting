package co.com.japl.ea.loader.interfaces;

/**
 * Interfaz usada para lambada para procesar la linea acabada de leer para ralixzar un proceso generic
 * @author Alejandro Parra
 * @since 18/11/2018
 */
public interface ILineRead {
	void getRow(String line)throws Exception;
}
