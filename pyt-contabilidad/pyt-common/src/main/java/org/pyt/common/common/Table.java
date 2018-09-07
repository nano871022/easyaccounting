package org.pyt.common.common;

import java.util.List;

import javafx.collections.ObservableList;
import javafx.scene.control.TableView;

/**
 * Se encarga de obtener funciones que permitan manejar las tabals sobre javafx
 * 
 * @author Alejandro Parra
 * @since 2018-05-19
 *
 */
public class Table {
	/**
	 * Este metodos e encarga de coger una lista y ponerla dentro de
	 * {@link TableView}
	 * 
	 * @param table
	 *            {@link TableView}
	 * @param lista
	 *            {@link List}
	 */
	public final static <T extends Object> void put(TableView<T> table, List<T> lista) {
		table.getItems().clear();
		ObservableList<T> observable = table.getItems();
		if (lista != null && lista.size() > 0) {
			observable.addAll(lista);
		}
		table.setItems(observable);
	}
	/**
	 * Verifica si tiene algun registro seleccionado en la tabla
	 * @param table {@link TableView}
	 * @return {@link Boolean}
	 */
	public final  static <T extends Object> Boolean isSelected(TableView<T> table) {
		return table.getSelectionModel().getSelectedItems().size() > 0;
	}
	/**
	 * Retorna los registros seleccionados en la tabla
	 * @param table {@link TableView}
	 * @return {@link List} of {@link Object}
	 */
	public final  static<T extends Object> List<T> getSelectedRows(TableView<T> table) {
		return table.getSelectionModel().getSelectedItems();
	}
	/**
	 * Verifica si tiene mas de un registro seleccionado en la tabla
	 * @param table {@link TableView}
	 * @return Boolean
	 */
	public final  static<T extends Object> Boolean isMultiSelected(TableView<T> table) {
		return table.getSelectionModel().getSelectedItems().size() > 1;
	}
	/**
	 * Verifica que solo contenfa un registro seleccionado en la tabla
	 * @param table {@link TableView}
	 * @return {@link Boolean}
	 */
	public final static <T extends Object> Boolean isUniqueSelected(TableView<T> table) {
		return table.getSelectionModel().getSelectedItems().size() == 1;
	}
}
