package co.com.japl.ea.interfaces;

/**
 * Esta interfaz se encarga de especificar si un bean recibe parametros por la
 * url del menu y procesar estos parametros
 * 
 * @author Alejandro Parra
 * @since 09/08/2019
 */
public interface IUrlLoadBean {
	public void loadParameters(String... parameters);
}
