package co.com.japl.ea.app.beans.interfaces;

/**
 * Intefaz para controlas los metodos usados para las pantallas de listas que
 * leen , eliminan , actualizan y crean, metodos usados y busqueda
 * 
 * @author Alejandro Parra
 * @since 22-06-2018
 */
public interface ListCRUDBean {
	/**
	 * Metodo usado para realizar la busqueda de los registros y ser mostrados en la
	 * tabla de lsitas
	 */
	public void searchBtn();

	/**
	 * Metodo usuado para realizar las acciones de creacion de un nuevo registro
	 */
	public void createBtn();

	/**
	 * Metodo usado para realizar las acciones de modificar un registros
	 */
	public void modifyBtn();

	/**
	 * Metodo usado para realizar las acciones de eliminacion de un registro.
	 */
	public void deleteBtn();

}
