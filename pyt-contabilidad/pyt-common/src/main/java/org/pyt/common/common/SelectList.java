package org.pyt.common.common;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
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
		try {
			choiceBox.getSelectionModel().selectFirst();
		} catch (Exception e) {
		}
	}
	/**
	 * Se encarga de limpiiar la lista de items y agrega string seleccione
	 * @param choiceBox {@link ChoiceBox}
	 */
	public final static <S extends Object> void clear(ChoiceBox<S> choiceBox) {
		choiceBox.getItems().clear();
		choiceBox.getItems().add((S) AppConstants.SELECCIONE);
	}
	/**
	 * Se encarga de cargar registro por registro e indicar que se seleccione el primer registro por defecto
	 * @param choiceBox {@link ChoiceBox}
	 * @param value {@link Object}
	 */
	public final static <S extends Object, V extends Object> void add(ChoiceBox<S> choiceBox, V value) {
		ObservableList<S> observable = choiceBox.getItems();
		observable.add((S) value);
		try {
			choiceBox.getSelectionModel().selectFirst();
		} catch (Exception e) {
		}
	}

	/**
	 * Se encarga de poner una lista de tipo string dentro de un choicebox de tipo
	 * string
	 * 
	 * @param choiceBox
	 *            {@link ChoiceBox}
	 * @param lista
	 *            {@link List} of {@link String}
	 */
	public final static void put(ChoiceBox<String> choiceBox, List<String> lista) {
		choiceBox.getItems().clear();
		ObservableList<String> observable = choiceBox.getItems();
		observable.add(AppConstants.SELECCIONE);
		for (String valor : lista) {
			observable.add(valor);
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
	 * Se encarga de obtener la opcion seleccionada en el choice box
	 * 
	 * @param choiceBox
	 *            {@link ChoiceBox}
	 * @return {@link String}
	 */
	public final static String get(ChoiceBox<String> choiceBox) {
		return choiceBox.getValue();
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
	public final static <S, L, N extends Object, T extends ADto, M extends ADto> void selectItem(ChoiceBox<S> choiceBox,
			List<M> list, String nombreCampoList, T obj, String nombreCampoDto) {
		Boolean select = false;
		try {
			ObservableList<S> values = choiceBox.getItems();
			if (obj != null) {
				N fieldValue = null;
				N fieldValueList = null;
				N fieldValueDto = null;
				for (M dto : list) {
					fieldValue = dto.get(nombreCampoList);
					fieldValueList = dto.get(nombreCampoList);
					fieldValueDto = obj.get(nombreCampoDto);
					if (fieldValueList == fieldValueDto) {
						for (S valuee : values) {
							if (valuee == fieldValue) {
								choiceBox.getSelectionModel().select(valuee);
								select = true;
							}
						}
					}
				}
			}
			if (!select) {
				choiceBox.getSelectionModel().selectFirst();
			}
		} catch (ReflectionException e) {
			Log.logger(e);
		}
	}

	/**
	 * Se encarga de seleccionar el registro seleccionado
	 * 
	 * @param choiceBox
	 *            {@link ChoiceBox}
	 * @param list
	 *            {@link List} of {@link ADto}
	 * @param nombreShow
	 *            {@link String}
	 * @param value
	 *            {@link Object}
	 * @param nombreAssign
	 *            {@link String}
	 */
	public final static <S extends Object, M extends ADto, T extends Object> void selectItem(ChoiceBox<S> choiceBox,
			List<M> list, String nombreShow, T value, String nombreAssign) {
		ValidateValues vv = new ValidateValues();
		for (M dto : list) {
			try {
				if (StringUtils.isNotBlank(nombreAssign)) {
					T value2 = dto.get(nombreAssign);
					if (vv.validate(value2, value)) {
						S valu;
						valu = dto.get(nombreShow);
						choiceBox.getSelectionModel().select(valu);
						break;
					}
				}
			} catch (ReflectionException | ValidateValueException e) {
				Log.logger(e);
			}
		} // end for
	}

	/**
	 * Se encarga de seleccionar un registro cuando se suministra como valor un
	 * objeto tipo adto y es igual al tipo dela lista Se valida que el objeto se
	 * encuentre dentro de la lista y despues se selecciona
	 * 
	 * @param choiceBox
	 *            {@link ChoiceBox}
	 * @param list
	 *            {@link List}
	 * @param nombreShow
	 *            {@link String}
	 * @param adto
	 *            {@link ADto} extends
	 */
	public final static <S extends Object, M extends ADto> void selectItem(ChoiceBox<S> choiceBox, List<M> list,
			String nombreShow, M adto) {
		ValidateValues vv = new ValidateValues();
		for (M dto : list) {
			try {
				if (dto != null && adto != null)
					if (StringUtils.isNotBlank(dto.getCodigo()) && StringUtils.isNotBlank(adto.getCodigo())) {
						if (dto.getCodigo().compareTo(adto.getCodigo()) == 0) {
							choiceBox.getSelectionModel().select(dto.get(nombreShow));
							break;
						}
					} else {
						if (vv.validate(dto, adto)) {
							choiceBox.getSelectionModel().select(dto.get(nombreShow));
							break;
						}
					}
			} catch (ReflectionException | ValidateValueException e) {
				Log.logger(e);
			}
		} // end for
	}

	/**
	 * Se encarga de buscar un valor en la lista y seleccionarlo por defecto
	 * 
	 * @param choiceBox
	 *            {@link ChoiceBox}
	 * @param mapa
	 *            {@link Map}
	 */
	public final static <S, T, L extends Object> void selectItem(ChoiceBox<S> choiceBox, Map<S, T> mapa, T value) {
		ValidateValues vv = new ValidateValues();
		Boolean select = false;
		Set<S> sets = mapa.keySet();
		for (S key : sets) {
			T val = mapa.get(key);
			try {
				if (vv.validate(val, value)) {
					for (int i = 0; i < choiceBox.getItems().size(); i++) {
						if (vv.validate(choiceBox.getItems().get(i), value)) {
							choiceBox.getSelectionModel().select(i);
							break;
						}
					}
					choiceBox.getSelectionModel().select(key);
					select = true;
					break;
				}
			} catch (ValidateValueException e) {
				Log.logger("Se presento problema en la busqueda del objeto seleccionado.", e);
				choiceBox.getSelectionModel().selectFirst();
			}
		}
		if (!select) {
			choiceBox.getSelectionModel().selectFirst();
		}

	}
}
