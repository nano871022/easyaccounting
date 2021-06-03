package co.com.japl.ea.query.interfaces;

/**
 * Interfaz usada para usar la verificacion de la base de datos segun lo puesto
 * en el package ea-sql, para de esta forma cada vez que se inicie la aplicacion
 * verifique la estructura y actualice si es necesario. Se debe implementar esta
 * interfaz para cada motor o tiepo de conexion que se realice.
 * 
 * @author Alejandro Parra
 * @since 09/07/2019
 */
public interface IVerifyStructuredDB {
	/**
	 * Se encarga de realizar los llamados necesarios para verificar la estructura
	 */
	public void verifyDB()throws Exception ;
	
	public Integer countScripts();
	public Integer counScriptRuns();
}
