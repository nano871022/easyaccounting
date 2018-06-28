package org.pyt.common.common;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.pyt.common.constants.AppConstants;
import org.pyt.common.exceptions.ReflectionException;
import org.pyt.common.exceptions.ValidateValueException;

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
			Log.logger(e);
		}
	}

	/**
	 * Se encarga de configurar el choice box para agregar los registros a ser
	 * seleccionados, se agrega es el nombre del mapa
	 * 
	 * @param choiceBox
	 *            {@link ChoiceBox}
	 * @param mapa
	 *            {@link Map}
	 */
	@SuppressWarnings("unchecked")
	public final static <S extends Object> void put(ChoiceBox<S> choiceBox, Map<S, Object> mapa) {
		choiceBox.getItems().clear();
		ObservableList<S> observable = choiceBox.getItems();
		observable.add((S) AppConstants.SELECCIONE);
		Set<S> sets = mapa.keySet();
		for (S key : sets) {
			observable.add(key);
		}
	}

	/**
	 * Obtiene el valor asociado a la lista del mapa key
	 * 
	 * @param choiceBox
	 *            {@link ChoiceBox}
	 * @param mapa
	 *            {@link Map}
	 * @return {@link Object} extended
	 */
	@SuppressWarnings("unused")
	public final static <S, T extends Object> T get(ChoiceBox<S> choiceBox, Map<S, T> mapa) {
		ObservableList<S> observable = choiceBox.getItems();
		Set<S> sets = mapa.keySet();
		for (S key : sets) {
			if (choiceBox.getValue() == key) {
				return mapa.get(key);
			}
		}
		return null;
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
			Log.logger(e);
		}
		return null;
	}

	/**
	 * Se encarga de buscar un valor en la lista y seleccionarlo por defecto
	 * 
	 * @param choiceBox
	 *            {@link ChoiceBox}
	 * @param obj
	 *            {@link ADto} extends
	 * @param nombreCampoDto
	 *            {@link String}
	 */
	public final static <S extends Object, T extends ADto> void selectItem(ChoiceBox<S> choiceBox, T obj,
			String nombreCampoDto) {
		Boolean select = false;
		try {
			if (obj != null) {
				for (S obs : choiceBox.getItems()) {
					if (obj.get(nombreCampoDto) == obs) {
						choiceBox.getSelectionModel().select(obs);
						select = true;
					}
				}
			}
			if (select) {
				choiceBox.getSelectionModel().selectFirst();
			}
		} catch (ReflectionException e) {
			Log.logger(e);
		}
	}

	/**
	 * Se encarga de buscar un valor en la lista y seleccionarlo por defecto
	 * 
	 * @param choiceBox
	 *            {@link ChoiceBox}
	 * @param mapa
	 *            {@link Map}
	 */
	public final static <S, T extends Object> void selectItem(ChoiceBox<S> choiceBox, Map<S, T> mapa,T value) {
		ValidateValues vv = new ValidateValues();
		Boolean select = false;
		Set<S> sets = mapa.keySet();
		for(S key : sets) {
			T val = mapa.get(key);
			try {
				if(vv.validate(val, value)) {
					choiceBox.getSelectionModel().select(key);
				}
			} catch (ValidateValueException e) {
				Log.logger("Se presento problema en la busqueda del objeto seleccionado.",e);
				choiceBox.getSelectionModel().selectFirst();
			}
		}
		if(select) {
			choiceBox.getSelectionModel().selectFirst();
		}

	}
}
