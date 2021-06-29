package co.com.japl.ea.interfaces;

import static org.pyt.common.constants.LanguageConstant.CONST_FXML_BTN_CLEAN;
import static org.pyt.common.constants.LanguageConstant.CONST_FXML_BTN_SEARCH;
import static org.pyt.common.constants.languages.DinamicFields.CONST_ERR_CLEAN_FIELDS;
import static org.pyt.common.constants.languages.DinamicFields.CONST_ERR_CONFIG_FIELD_NOT_FOUND_;
import static org.pyt.common.constants.languages.DinamicFields.CONST_ERR_DO_CASTING;
import static org.pyt.common.constants.languages.DinamicFields.CONST_ERR_GET_FIELD;
import static org.pyt.common.constants.languages.DinamicFields.CONST_ERR_GET_LIST_VALUES;
import static org.pyt.common.constants.languages.DinamicFields.CONST_ERR_GET_TYPE_FIELD;
import static org.pyt.common.constants.languages.DinamicFields.CONST_ERR_IN_PARAMETERS;
import static org.pyt.common.constants.languages.DinamicFields.CONST_ERR_IN_REFLECTION;
import static org.pyt.common.constants.languages.DinamicFields.CONST_ERR_VALID_VALUES;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.function.Function;

import org.apache.commons.collections4.MultiValuedMap;
import org.apache.commons.lang3.StringUtils;
import org.controlsfx.glyphfont.FontAwesome.Glyph;
import org.pyt.common.annotation.generics.AssingValue;
import org.pyt.common.annotation.generics.DefaultFieldToGeneric.Uses;
import org.pyt.common.common.ListUtils;
import org.pyt.common.common.SelectList;
import org.pyt.common.constants.AppConstants;
import org.pyt.common.constants.LanguageConstant;
import org.pyt.common.constants.ParametroConstants;

import co.com.japl.ea.app.beans.languages.LanguageBean;
import co.com.japl.ea.common.abstracts.ADto;
import co.com.japl.ea.common.validates.ValidFields;
import co.com.japl.ea.dto.dto.ParametroDTO;
import co.com.japl.ea.dto.system.ConfigGenericFieldDTO;
import co.com.japl.ea.exceptions.LoadAppFxmlException;
import co.com.japl.ea.exceptions.ParametroException;
import co.com.japl.ea.exceptions.ReflectionException;
import co.com.japl.ea.exceptions.validates.ValidateValueException;
import co.com.japl.ea.utls.LoadAppFxml;
import javafx.scene.Node;
import javafx.scene.control.ChoiceBox;
import javafx.scene.layout.GridPane;

/**
 * Se encarga de generalizar las funciones que usaran las diferentes
 * implementaciones de campos genericos puestos sobre gridPanel
 * 
 * @author Alejo Parra
 *
 * @param <L> type for {@link List} que contiene los campos configurados como
 *            genericos
 * @param <F> type to {@link ADto} que tiene los campos configurados
 */
public interface IGenericFields<L extends ADto, F extends ADto> extends IGenericCommons<L, F> {
	/**
	 * Objeto tipo {@link GridPane}
	 * 
	 * @return {@link GridPane}
	 */
	public GridPane getGridPane(TypeGeneric typeGeneric);

	/**
	 * Instancia del objeto dto donde se obtienen los valores y se ponene los
	 * valores, dto maestro
	 * 
	 * @return {@link ADto}
	 */
	public F getInstanceDto(TypeGeneric typeGeneric);

	/**
	 * se encarga de retornar la lista de campos que se poenen en campos de tipo
	 * lista.
	 * 
	 * @return {@link MultiValuedMap} < {@link String} , {@link ADto} >
	 */
	public MultiValuedMap<String, Object> getMapListToChoiceBox();

	/**
	 * Entrega un {@link MultiValuedMap} en el cual estaran los campos configruados
	 * 
	 * @return {@link MultiValuedMap}
	 */
	public MultiValuedMap<String, Node> getMapFields(TypeGeneric typeGeneric);

	private void calcNextColumnAndRow(TypeGeneric typeGeneric, Index index) {
		if (getMaxColumns(typeGeneric) > index.column) {
			index.column++;
		} else {
			index.row++;
			index.column = 0;
		}
	}

	class Index {
		public int column = 0;
		public int row = 0;
	}

	private Uses getUsesByUsedTypeGeneric(TypeGeneric typeGeneric) {
		if (Uses.FILTER.toString().contentEquals(typeGeneric.toString()))
			return Uses.FILTER;
		else if (Uses.COLUMN.toString().contentEquals(typeGeneric.toString()))
			return Uses.COLUMN;
		else
			return Uses.FIELD;
	}

	private void loadDefaultValueDate(TypeGeneric typeGeneric, String valueDefault, Class typeField,
			IFieldsCreator factory) {
		if ("now".equalsIgnoreCase(valueDefault)) {
			if (typeField == LocalDate.class) {
				getInstanceDto(typeGeneric).set(factory.getNameField(), LocalDate.now());
			} else if (typeField == LocalDateTime.class) {
				getInstanceDto(typeGeneric).set(factory.getNameField(), LocalDateTime.now());
			} else if (typeField == Date.class) {
				getInstanceDto(typeGeneric).set(factory.getNameField(), new Date());
			}
		} else if (valueDefault.matches(AppConstants.CONST_FORMAT_DATE)) {
			var loadDate = LocalDate.parse(valueDefault);
			if (typeField == LocalDate.class) {
				getInstanceDto(typeGeneric).set(factory.getNameField(), loadDate);
			} else if (typeField == Date.class) {
				getInstanceDto(typeGeneric).set(factory.getNameField(),
						Date.from(loadDate.atStartOfDay(ZoneId.systemDefault()).toInstant()));
			}
		} else if (valueDefault.matches(AppConstants.CONST_FORMAT_DATE_TIME)) {
			var loadDate = LocalDateTime.parse(valueDefault);
			if (typeField == LocalDate.class) {
				getInstanceDto(typeGeneric).set(factory.getNameField(), loadDate);
			} else if (typeField == Date.class) {
				getInstanceDto(typeGeneric).set(factory.getNameField(),
						Date.from(loadDate.atZone(ZoneId.systemDefault()).toInstant()));
			}
		}
	}

	private void assignDefaultValue(TypeGeneric typeGeneric, IFieldsCreator factory) {
		if (factory.hasValueDefault()) {
			var eval = factory.getValueDefault();
			if (StringUtils.isNotBlank(eval)) {
				var typeField = getInstanceDto(typeGeneric).typeField(factory.getNameField());
				if (validateValuesUtils.isNumber(typeField)) {
					getInstanceDto(typeGeneric).set(factory.getNameField(), validateValuesUtils.cast(eval, typeField));
				}
				if (validateValuesUtils.isString(typeField)) {
					getInstanceDto(typeGeneric).set(factory.getNameField(), eval);
				}
				if (validateValuesUtils.isDate(typeField)) {
					loadDefaultValueDate(typeGeneric, eval, typeField, factory);
				}
			}
		}
	}

	@SuppressWarnings("unchecked")
	private List<L> getListFieldsAnnotates(TypeGeneric typeGeneric, List<L> list) {
		if (list == null) {
			var typeField = getUsesByUsedTypeGeneric(typeGeneric);
			var generics = LoadFieldsFactory.getAnnotatedField(ConfigGenericFieldDTO.class, getClazz(), typeField);
			if (generics == null) {
				throw new RuntimeException(
						i18n().valueBundle(LanguageConstant.GENERIC_FIELD_NOT_FOUND_FIELD_TO_USE).get());
			} else {
				list = (List<L>) generics;
			}
		}
		return list;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void loadPopupLanguages(IFieldsCreator factory) {
		var popup = new co.com.japl.ea.app.components.PopupFromBean(
				co.com.japl.ea.app.beans.languages.LanguageBean.class);
		try {
			LoadAppFxml.loadBeanFX(popup);
			LanguageBean bean = (LanguageBean) popup.getBean();
			bean.openPopup();
			bean.addCode(factory.getLabelText().get());
		} catch (LoadAppFxmlException e) {
			logger().logger(e);
		}
	}

	/**
	 * Se encarga de configurar los campos que se mostraran en los filtros y fueron
	 * configurados como campos genericos, esto se pone el {@link GridPane}
	 * 
	 */
	default void loadFields(TypeGeneric typeGeneric, String... stylesGrid) {
		var list = getListGenericsFields(typeGeneric);
		list = getListFieldsAnnotates(typeGeneric, list);
		var index = new Index();
		assingValueAnnotations(typeGeneric);
		genericFormsUtils.configGridPane(getGridPane(typeGeneric));
		try {
			list.sort((value1, value2) -> value1.get("position") != null && value2 != null
					? ((Integer) value1.get("position")).compareTo((Integer) value2.get("position"))
					: value1.get("orden") != null && value2 != null
							? ((Integer) value1.get("orden")).compareTo((Integer) value2.get("orden"))
							: 0);
		} catch (Exception e) {
			logger().DEBUG(e);
		}
		list.forEach(field -> {
			try {
				var factory = LoadFieldsFactory.getInstance(field);
				assignDefaultValue(typeGeneric, factory);
				if (factory.isVisible()) {
					var label = genericFormsUtils.createLabel(factory.getLabelText(),
							event -> loadPopupLanguages(factory), factory.getToolTip());
					var fieldControl = factory.create();
					var nameField = factory.getNameField();
					var typeField = getInstanceDto(typeGeneric).getType(nameField);
					getMapFields(typeGeneric).put(nameField, fieldControl);
					loadValuesInChoiceBox(typeGeneric, typeField, factory, fieldControl, nameField);
					loadValuesFormToDTO(typeGeneric, fieldControl, typeField, nameField);
					getGridPane(typeGeneric).add(label, index.column, index.row);
					getGridPane(typeGeneric).add(fieldControl, ++index.column, index.row);
					Arrays.asList(stylesGrid)
							.forEach(styleGrid -> getGridPane(typeGeneric).getStyleClass().add(styleGrid));
					loadValueIntoForm(typeGeneric, typeField, nameField, factory, fieldControl);
					calcNextColumnAndRow(typeGeneric, index);
				}
			} catch (ReflectionException e) {
				logger().DEBUG(i18n().valueBundle(CONST_ERR_GET_TYPE_FIELD), e);
			} catch (ParametroException e) {
				logger().DEBUG(i18n().valueBundle(CONST_ERR_GET_LIST_VALUES), e);
			}
		});
		configFieldFilter(typeGeneric, index);
	}

	@SuppressWarnings("unchecked")
	private void configFieldFilter(TypeGeneric typeGeneric, Index index) {
		if (TypeGeneric.FILTER == typeGeneric) {
			getGridPane(typeGeneric).add(genericFormsUtils.buttonGenericWithEventClicked(() -> {
				try {
					var table = ((IGenericColumns<L, F>) this).getTable();
					table.activeSearch();
					table.search();
				} catch (Exception e) {
				}
			}, i18n().valueBundle(CONST_FXML_BTN_SEARCH), Glyph.FONT.SEARCH), 0, ++index.row);
			getGridPane(typeGeneric).add(genericFormsUtils.buttonGenericWithEventClicked(() -> clearFields(typeGeneric),
					i18n().valueBundle(CONST_FXML_BTN_CLEAN), Glyph.FONT.GAVEL), 1, index.row);

		}
	}

	@SuppressWarnings({ "rawtypes" })
	private <N extends Node> void loadValueIntoForm(TypeGeneric typeGeneric, Class typeField, String fieldName,
			IFieldsCreator factory, N node) throws ReflectionException, ParametroException {
		if (genericFormsUtils.isChoiceBox(node)) {
			var list = getSelectedListToChoiceBox(typeGeneric, fieldName, typeField, factory);
			if (StringUtils.isBlank(factory.getNameFieldToShowInComboBox()) && list != null && list.size() > 0
					&& list.get(0) instanceof ParametroDTO) {
				var valueToParametro = getInstanceDto(typeGeneric).get(fieldName);
				if (valueToParametro != null) {
					var paramFound = ((ParametroDTO) list.stream()
							.filter(row -> ((ParametroDTO) row).getValor()
									.equalsIgnoreCase(validateValuesUtils.cast(valueToParametro, String.class)))
							.findAny().get());
					if (paramFound != null) {
						String nameFound = paramFound.getNombre();
						genericFormsUtils.loadValuesInFxmlToChoice(ParametroConstants.FIELD_NAME_PARAM, node,
								paramFound, null, list);
					}
				}
			} else {
				genericFormsUtils.loadValuesInFxmlToChoice(factory.getNameFieldToShowInComboBox(), node,
						getInstanceDto(typeGeneric).get(fieldName), null, list);
			}
		} else {
			genericFormsUtils.loadValuesInFxml(node, getInstanceDto(typeGeneric).get(fieldName));
		}
	}

	default void loadValueIntoForm(TypeGeneric typeGeneric, String fieldName) {
		try {
			if (!getMapFields(typeGeneric).containsKey(fieldName)) {
				throw new RuntimeException(i18n().valueBundle(CONST_ERR_CONFIG_FIELD_NOT_FOUND_).get());
			}
			var field = getInstanceDto(typeGeneric);
			var factory = LoadFieldsFactory.getInstance(field);
			var typeField = getInstanceDto(typeGeneric).getType(fieldName);
			var nodes = getMapFields(typeGeneric).get(fieldName);
			nodes.forEach(node -> {
				try {
					loadValueIntoForm(typeGeneric, typeField, fieldName, factory, node);
				} catch (ReflectionException e) {
					logger().DEBUG(i18n().valueBundle(CONST_ERR_IN_REFLECTION), e);
				} catch (ParametroException e) {
					logger().DEBUG(i18n().valueBundle(CONST_ERR_IN_PARAMETERS), e);
				}
			});
		} catch (ReflectionException e) {
			logger().DEBUG(i18n().valueBundle(CONST_ERR_IN_REFLECTION), e);
		}
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void loadValuesFormToDTO(TypeGeneric typeGeneric, Node fieldControl, Class typeField, String nameField) {

		genericFormsUtils.inputListenerToAssingValue(fieldControl, value -> {
			try {
				getInstanceDto(typeGeneric).set(nameField, validateValuesUtils.cast(value, typeField));
			} catch (ReflectionException e) {
				logger().DEBUG(i18n().valueBundle(CONST_ERR_GET_FIELD), e);
			} catch (ValidateValueException e) {
				logger().DEBUG(i18n().valueBundle(CONST_ERR_VALID_VALUES), e);
			}
		});

	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void loadValuesInChoiceBox(TypeGeneric typeGeneric, Class typeField, IFieldsCreator factory,
			Node fieldControl, String nameField) throws ParametroException {
		if (genericFormsUtils.isChoiceBox(fieldControl)) {
			var list = getSelectedListToChoiceBox(typeGeneric, nameField, typeField, factory);
			var nameShow = StringUtils.isNotBlank(factory.getNameFieldToShowInComboBox())
					? factory.getNameFieldToShowInComboBox()
					: ParametroConstants.FIELD_NAME_PARAM;
			SelectList.put((ChoiceBox) fieldControl, list, nameShow);
			((ChoiceBox) fieldControl).getSelectionModel().selectedItemProperty()
					.addListener((observable, oldValue, newValue) -> {
						var value = SelectList.get(((ChoiceBox) fieldControl), list, nameShow);
						if (StringUtils.isNotBlank(factory.getNameFieldToShowInComboBox())) {
							getInstanceDto(typeGeneric).set(nameField, value);
						} else if (value instanceof ParametroDTO) {
							getInstanceDto(typeGeneric).set(nameField, ((ParametroDTO) value).getValor());
						}
					});
		}
	}

	@SuppressWarnings("rawtypes")
	private boolean isParametroDTO(Class clazz) {
		try {
			if (clazz.cast(new ParametroDTO()) != null) {
				return true;
			}
		} catch (ClassCastException e) {
			logger().DEBUG(i18n().valueBundle(CONST_ERR_DO_CASTING), e);
		}
		return false;
	}

	/**
	 * Se encarga de limpiar todos los campos configurados
	 */
	default void clearFields(TypeGeneric typeGeneric) {
		getMapFields(typeGeneric).values().forEach(field -> genericFormsUtils.cleanValueByFieldFX(field));
		getMapFields(typeGeneric).keySet().stream().filter(field -> {
			try {
				return getInstanceDto(typeGeneric).getType(field) != null;
			} catch (Exception e) {
				return false;
			}
		}).forEach(field -> {
			try {
				getInstanceDto(typeGeneric).set(field, null);
			} catch (Exception e) {
				logger().DEBUG(i18n().valueBundle(CONST_ERR_CLEAN_FIELDS) + e.getMessage());
			}
		});
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private <D extends ADto> List<D> getSelectedListToChoiceBox(TypeGeneric typeGeneric, String nameField,
			Class typeField, IFieldsCreator factory) throws ParametroException {
		var choiceBox = getMapListToChoiceBox();
		if (choiceBox == null) {
			return new ArrayList<>();
		}
		var list = choiceBox.get(nameField);
		if ((StringUtils.isBlank(factory.getNameFieldToShowInComboBox()) || isParametroDTO(typeField))
				&& (list == null || list.size() == 0)) {
			var parametroDto = factory.getParametroDto();
			var parametrosSearch = getParametersSvc().getAllParametros(parametroDto);
			if (ListUtils.isNotBlank(parametrosSearch)) {
				choiceBox.put(nameField, parametrosSearch);
				return (List<D>) parametrosSearch;
			} else if (StringUtils.isNotBlank(parametroDto.getGrupo())) {
				var grupoSave = parametroDto.getGrupo();
				if (StringUtils.isNotBlank(parametroDto.getGrupo())) {
					parametroDto.setGrupo(getParametersSvc().getIdByParametroGroup(parametroDto.getGrupo()));
				}
				parametroDto = getParametroFromFindGroup(parametroDto, grupoSave);
				var parametros = getParametersSvc().getAllParametros(parametroDto);
				choiceBox.put(nameField, parametros);
				return (List<D>) parametros;
			}
		}
		var listOut = new ArrayList<D>();
		list.forEach(row -> {
			if (row instanceof List) {
				((List) row).forEach(reg -> listOut.add((D) reg));
			} else {
				listOut.add((D) row);
			}
		});
		return listOut;
	}

	/**
	 * Se encarga asignar las anotaciones para poner los valores en el dto
	 * 
	 * @param dto extends {@link Object}
	 * @return extends {@link Object}
	 */
	@SuppressWarnings("unchecked")
	private F assingValueAnnotations(TypeGeneric typeGeneric) {
		var dto = getInstanceDto(typeGeneric);
		Class<F> clazz = (Class<F>) dto.getClass();
		Arrays.asList(clazz.getDeclaredFields()).stream()
				.filter(field -> field.getAnnotation(AssingValue.class) != null).forEach(field -> {
					Arrays.asList(field.getAnnotationsByType(AssingValue.class)).forEach(annotation -> {
						try {
							dto.set(annotation.nameField(), annotation.value());
						} catch (ReflectionException e) {
							logger().DEBUG(e);
						}
					});
				});
		return dto;
	}

	default Boolean validFields() {
		var dto = getInstanceDto(TypeGeneric.FIELD);
		var list = getListGenericsFields(TypeGeneric.FIELD);
		Function<ADto, Boolean> maping = field -> {
			var factory = LoadFieldsFactory.getInstance(field);
			var node = getMapFields(TypeGeneric.FIELD).get(factory.getNameField()).stream().findFirst().get();
			var value = dto.get(factory.getNameField());
			return ValidFields.valid(value, node, true, null, null,
					i18n().valueBundle("err.valid.document.field.field.empty"));
		};
		return list.stream().filter(field -> LoadFieldsFactory.getInstance(field).isRequired()).map(maping).reduce(true,
				(val, result) -> val &= result);
	}

}
