package org.pyt.common.common;

import java.util.List;

import org.pyt.common.constants.AppConstants;
import org.pyt.common.exceptions.ReflectionException;

import javafx.collections.ObservableList;
import javafx.scene.control.ChoiceBox;

/**
 * Se encargo de cargar registros a un choicebox opcion seleccionable
 * 
 * @author Alejandro Parra
 * @since 2018-05-23
 */
public final class SelectList {
	/**
	 * Se encarga de ponder los datos en el choice box con obserble list
	 * 
	 * @param choiceBox
	 *            {@link ChoiceBox}
	 * @param lista
	 *            {@link List}
	 * @param campoDto
	 *            {@link String} nombre del campo del dto que se muestra en el
	 *            objeto
	 */
	@SuppressWarnings("unchecked")
	public final static <S extends Object, T extends ADto> void put(ChoiceBox<S> choiceBox, List<T> lista,
			String campoDto) {
		try {
			choiceBox.getItems().clear();
			ObservableList<S> observable = choiceBox.getItems();
			observable.add((S) AppConstants.SELECCIONE);
			for (T obj : lista) {
				observable.add(obj.get(campoDto));
			}
		} catch (ReflectionException e) {
			System.err.println(e);
		}
	}

	/**
	 * Se encarga de buscar dentro de una lista el valor seleccionado en choiceBox
	 * 
	 * @param choiceBox
	 *            {@link ChoiceBox}
	 * @param lista
	 *            {@link List}
	 * @param nombreCampoDto
	 *            {@link String} nombre del campo en la lista a comparra
	 * @return {@link Object} objeto de la lista resultante
	 */
	public final static <S extends Object, T extends ADto> T get(ChoiceBox<S> choiceBox, List<T> lista,
			String nombreCampoDto) {
		try {
			for (T dto : lista) {
				if (choiceBox.getValue() == dto.get(nombreCampoDto)) {
					return dto;
				}
			}
		} catch (ReflectionException e) {
			System.err.println(e);
		}
		return null;
	}

	public final static <S extends Object, T extends ADto> void selectItem(ChoiceBox<S> choiceBox, T obj,
			String nombreCampoDto) {
		try {
			for (S obs : choiceBox.getItems()) {
				if (obj.get(nombreCampoDto) == obs) {
					choiceBox.getSelectionModel().select(obs);
				}
			}
		} catch (ReflectionException e) {
			System.err.println(e);
		}
	}
}
