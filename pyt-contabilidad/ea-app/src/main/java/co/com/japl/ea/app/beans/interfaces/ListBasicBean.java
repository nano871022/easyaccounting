package co.com.japl.ea.app.beans.interfaces;

import co.com.japl.ea.utls.DataTableFXMLUtil;
import javafx.fxml.FXML;

/**
 * Interfaz creada para parametrizar el como se debe crear una pagina basica de
 * listado de objetos.
 * 
 * @author Alejandro Parra
 * @since 22-06-2018
 */
public interface ListBasicBean {
	/**
	 * Se encarga de ser inicializado el objeto por medio de la anotacion @FXML, que
	 * se debe poner en el nombre del metodo.
	 */
	@FXML
	public void initialize();

	/**
	 * Este metodo es el encargado de tener la implementacion de
	 * {@link DataTableFXMLUtil}
	 */
	public void lazy();

	/**
	 * Metodo encargado de indicar cuando en la tabal se a realizado un click sobre
	 * esta
	 */
	public void clickTable();

	/**
	 * este metodo verifica si la tabla se a seleccionado algun registro
	 * 
	 * @return {@link Boolean}
	 */
	public Boolean isSelected();
}
