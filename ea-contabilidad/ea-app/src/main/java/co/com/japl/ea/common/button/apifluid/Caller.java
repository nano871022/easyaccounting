package co.com.japl.ea.common.button.apifluid;

/**
 * Interface funcional para realizar un llamado a algo, pero que no recibe
 * ninguna informacion y no retorna ninguna informacion
 * 
 * @author Alejandro Parra|
 * @since 29/02/2020
 */
@FunctionalInterface
public interface Caller {
	/**
	 * Usado para ejecutar el contenido del metod y no rretorna ningun dato
	 */
	public void call();
}
