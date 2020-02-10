package org.pyt.common.common;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.pyt.common.abstracts.ADto;
import org.pyt.common.constants.AppConstants;
import org.pyt.common.exceptions.ReflectionException;
import org.pyt.common.exceptions.validates.ValidateValueException;
import org.pyt.common.reflection.ReflectionUtils;
import org.pyt.common.validates.ValidateValues;

import javafx.collections.ObservableList;
import javafx.scene.control.ChoiceBox;
import javafx.util.StringConverter;

/**
 * Se encargo de cargar registros a un choicebox opcion seleccionable
 * 
 * @author Alejandro Parra
 * @since 2018-05-23
 */
public final class SelectList {
	private static Log logger = Log.Log(SelectList.class);

	/**
	 * Se encarga de ponder los datos en el choice box con obserble list
	 * 
	 * @param choiceBox {@link ChoiceBox}
	 * @param lista     {@link List}
	 * @param campoDto  {@link String} nombre del campo del dto que se muestra en el
	 *                  objeto
	 */
	@SuppressWarnings("unchecked")
	public final static <S extends Object, T extends ADto> void put(ChoiceBox<S> choiceBox, List<T> lista,
			String campoDto) {
		if (campoDto == null)
			return;
		choiceBox.getItems().clear();
		ObservableList<S> observable = choiceBox.getItems();
		observable.add((S) AppConstants.SELECCIONE);
		if (lista != null) {
			lista.stream().filter(obj -> obj != null).forEach(obj -> {
				try {
					S v = obj.get(campoDto);
					if (v != null) {
						observable.add(v);
					}
				} catch (ReflectionException e) {
					logger.DEBUG(e);
				}
			});
		}
		try {
			choiceBox.getSelectionModel().selectFirst();
		} catch (Exception e) {
			logger.DEBUG(e);
		}
	}


	/**
	 * Se encarga de limpiiar la lista de items y agrega string seleccione
	 * 
	 * @param choiceBox {@link ChoiceBox}
	 */
	@SuppressWarnings("unchecked")
	public final static <S extends Object> void clear(ChoiceBox<S> choiceBox) {
		choiceBox.getItems().clear();
		choiceBox.getItems().add((S) AppConstants.SELECCIONE);
	}

	/**
	 * Se encarga de cargar registro por registro e indicar que se seleccione el
	 * primer registro por defecto
	 * 
	 * @param choiceBox {@link ChoiceBox}
	 * @param value     {@link Object}
	 */
	@SuppressWarnings("unchecked")
	public final static <S extends Object, V extends Object> void add(ChoiceBox<S> choiceBox, V value) {
		ObservableList<S> observable = choiceBox.getItems();
		observable.add((S) value);
		try {
			choiceBox.getSelectionModel().selectFirst();
		} catch (Exception e) {
			logger.DEBUG(e);
		}
	}

	/**
	 * Se encarga de poner una lista de tipo string dentro de un choicebox de tipo
	 * string
	 * 
	 * @param choiceBox {@link ChoiceBox}
	 * @param lista     {@link List} of {@link String}
	 */
	@SuppressWarnings("unchecked")
	public final static <S extends Object> void put(ChoiceBox<S> choiceBox, List<S> lista) {
		choiceBox.getItems().clear();
		if (lista == null || lista.size() == 0)
			return;
		ObservableList<S> observable = choiceBox.getItems();
		if (lista.get(0) instanceof String) {
			observable.add((S) AppConstants.SELECCIONE);
		}

		lista.forEach(valor -> observable.add(valor));
	}


	/**
	 * Se encarga de configurar el choice box para agregar los registros a ser
	 * seleccionados, se agrega es el nombre del mapa
	 * 
	 * @param choiceBox {@link ChoiceBox}
	 * @param mapa      {@link Map}
	 */
	@SuppressWarnings("unchecked")
	public final static <S extends Object> void put(ChoiceBox<S> choiceBox, Map<S, Object> mapa) {
		choiceBox.getItems().clear();
		ObservableList<S> observable = choiceBox.getItems();
		observable.add((S) AppConstants.SELECCIONE);
		Set<S> sets = mapa.keySet();
		sets.forEach(key -> observable.add(key));
	}

	/**
	 * Obtiene el valor asociado a la lista del mapa key
	 * 
	 * @param choiceBox {@link ChoiceBox}
	 * @param mapa      {@link Map}
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
	 * @param choiceBox {@link ChoiceBox}
	 * @return {@link String}
	 */
	public final static <T extends Object> T get(ChoiceBox<T> choiceBox) {
		var select = choiceBox.selectionModelProperty().get().getSelectedItem();
		if (select != null) {
			if (select instanceof String) {
				if (AppConstants.SELECCIONE.contentEquals((String) select)) {
					return null;
				}
			}
			return select;
		}
		return null;
	}

	/**
	 * Se encarga de buscar dentro de una lista el valor seleccionado en choiceBox
	 * 
	 * @param selects        {@link ChoiceBox}
	 * @param list           {@link List}
	 * @param nombreCampoDto {@link String} nombre del campo en la lista a comparra
	 * @return {@link Object} objeto de la lista resultante
	 */
	public final static <S extends Object, T extends ADto> T get(ChoiceBox<S> selects, List<T> list,
			String nombreCampoDto) {
		try {
			if (list != null && selects != null) {
				var itemSelected = Optional.ofNullable(selects.selectionModelProperty().get().getSelectedItem());
				return list.stream()
						.filter(row -> itemSelected.isPresent() && itemSelected.get().equals(row.get(nombreCampoDto)))
						.findAny().orElse(null);
			}
		} catch (ReflectionException e) {
			logger.DEBUG(e);
		}
		return null;
	}

	/**
	 * Se encarga de buscar un valor en la lista y seleccionarlo por defecto
	 * 
	 * @param choiceBox      {@link ChoiceBox}
	 * @param obj            {@link ADto} extends
	 * @param nombreCampoDto {@link String}
	 */
	public final static <S, L, N extends Object, T extends ADto, M extends ADto> void selectItem(ChoiceBox<S> choiceBox,
			List<M> list, String nombreCampoList, T obj, String nombreCampoDto) {
		Boolean select = false;
		ValidateValues val = new ValidateValues();
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
					if (val.validate(fieldValueList, fieldValueDto)) {
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
			logger.DEBUG(e);
		} catch (ValidateValueException e) {
			logger.DEBUG(e);
		}
	}

	/**
	 * Se encarga de seleccionar el registro seleccionado
	 * 
	 * @param choiceBox    {@link ChoiceBox}
	 * @param list         {@link List} of {@link ADto}
	 * @param nombreShow   {@link String}
	 * @param value        {@link Object}
	 * @param nombreAssign {@link String}
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
				logger.DEBUG(e);
			}
		} // end for
	}

	/**
	 * Se encarga de seleccionar un registro cuando se suministra como valor un
	 * objeto tipo adto y es igual al tipo dela lista Se valida que el objeto se
	 * encuentre dentro de la lista y despues se selecciona
	 * 
	 * @param choiceBox  {@link ChoiceBox}
	 * @param list       {@link List}
	 * @param nombreShow {@link String}
	 * @param adto       {@link ADto} extends
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
				logger.DEBUG(e);
			}
		} // end for
	}

	/**
	 * Se encarga de buscar un valor en la lista y seleccionarlo por defecto
	 * 
	 * @param choiceBox {@link ChoiceBox}
	 * @param mapa      {@link Map}
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
				logger.DEBUG("Se presento problema en la busqueda del objeto seleccionado.", e);
				choiceBox.getSelectionModel().selectFirst();
			}
		}
		if (!select) {
			choiceBox.getSelectionModel().selectFirst();
		}
	}

	/**
	 * Se encarga dde seleccionar el registro
	 * 
	 * @param choiceBox {@link ChoiceBox}
	 * @param value     {@link Object}
	 */
	public final static <T extends Object> void selectItem(ChoiceBox<T> choiceBox, T value) {
		choiceBox.getSelectionModel().select(value);
	}

	public final static <S> void addItems(ChoiceBox<S> choiceBox, List<S> list, String... campos) {
		choiceBox.setConverter(new StringConverter<S>() {
			private StringBuilder name;

			@Override
			public String toString(S object) {
				name = new StringBuilder();
				for (String campo : campos) {
					try {
						name.append(ReflectionUtils.getValueField(object, campo).toString());
					} catch (ReflectionException e) {
					}
				}
				return name.toString();
			}

			@Override
			public S fromString(String string) {
				return null;
			}
		});
	}
}
