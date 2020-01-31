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

import org.apache.commons.collections4.MultiValuedMap;
import org.apache.commons.lang3.StringUtils;
import org.controlsfx.glyphfont.FontAwesome.Glyph;
import org.pyt.common.abstracts.ADto;
import org.pyt.common.annotation.generics.AssingValue;
import org.pyt.common.annotation.generics.DefaultFieldToGeneric.Uses;
import org.pyt.common.common.SelectList;
import org.pyt.common.constants.AppConstants;
import org.pyt.common.constants.LanguageConstant;
import org.pyt.common.exceptions.ParametroException;
import org.pyt.common.exceptions.ReflectionException;
import org.pyt.common.exceptions.validates.ValidateValueException;

import com.pyt.service.dto.ParametroDTO;
import com.pyt.service.implement.GenericServiceSvc;
import com.pyt.service.interfaces.IParametrosSvc;

import co.com.japl.ea.dto.system.ConfigGenericFieldDTO;
import javafx.beans.binding.Bindings;
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
	 * Servicio de parametros, este debe ser el generico, con esto permite realizar
	 * consultas dinamicamente.
	 * 
	 * @return {@link GenericServiceSvc} < {@link ParametroDTO} >
	 */
	public IParametrosSvc getParametersSvc();

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
					if ("now".equalsIgnoreCase(eval)) {
						if (typeField == LocalDate.class) {
							getInstanceDto(typeGeneric).set(factory.getNameField(), LocalDate.now());
						} else if (typeField == LocalDateTime.class) {
							getInstanceDto(typeGeneric).set(factory.getNameField(), LocalDateTime.now());
						} else if (typeField == Date.class) {
							getInstanceDto(typeGeneric).set(factory.getNameField(), new Date());
						}
					} else if (eval.matches(AppConstants.CONST_FORMAT_DATE)) {
						var loadDate = LocalDate.parse(eval);
						if (typeField == LocalDate.class) {
							getInstanceDto(typeGeneric).set(factory.getNameField(), loadDate);
						} else if (typeField == Date.class) {
							getInstanceDto(typeGeneric).set(factory.getNameField(),
									Date.from(loadDate.atStartOfDay(ZoneId.systemDefault()).toInstant()));
						}
					} else if (eval.matches(AppConstants.CONST_FORMAT_DATE_TIME)) {
						var loadDate = LocalDateTime.parse(eval);
						if (typeField == LocalDate.class) {
							getInstanceDto(typeGeneric).set(factory.getNameField(), loadDate);
						} else if (typeField == Date.class) {
							getInstanceDto(typeGeneric).set(factory.getNameField(),
									Date.from(loadDate.atZone(ZoneId.systemDefault()).toInstant()));
						}
					}
				}
			}
		}
	}

	/**
	 * Se encarga de configurar los campos que se mostraran en los filtros y fueron
	 * configurados como campos genericos, esto se pone el {@link GridPane}
	 * 
	 */
	@SuppressWarnings({ "unchecked", "static-access" })
	default void loadFields(TypeGeneric typeGeneric, String... stylesGrid) {
		var list = getListGenericsFields(typeGeneric);
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
		var index = new Index();
		assingValueAnnotations(typeGeneric);
		genericFormsUtils.configGridPane(getGridPane(typeGeneric));
		list.forEach(field -> {
			try {
				var factory = LoadFieldsFactory.getInstance(field);
				assignDefaultValue(typeGeneric, factory);
				if (factory.isVisible()) {
					var label = genericFormsUtils.createLabel(factory.getLabelText(), factory.getToolTip());
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
		if (TypeGeneric.FILTER == typeGeneric) {
			getGridPane(typeGeneric).add(genericFormsUtils.buttonGenericWithEventClicked(() -> {
				try {
					((IGenericColumns<L, F>) this).getTable().search();
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
			genericFormsUtils.loadValuesInFxmlToChoice(factory.getNameFieldToShowInComboBox(), node,
					getInstanceDto(typeGeneric).get(fieldName), null, list);
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
			((ChoiceBox) fieldControl).valueProperty()
					.bind(Bindings.createObjectBinding(() -> getInstanceDto(typeGeneric).get(nameField)));
			SelectList.put((ChoiceBox) fieldControl, list, factory.getNameFieldToShowInComboBox());
			((ChoiceBox) fieldControl).getSelectionModel().selectedItemProperty()
					.addListener((observable, oldValue, newValue) -> {
						var value = SelectList.get(((ChoiceBox) fieldControl), list,
								factory.getNameFieldToShowInComboBox());
						getInstanceDto(typeGeneric).set(nameField, value);
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
		var list = getMapListToChoiceBox().get(nameField);
		if (isParametroDTO(typeField) && list == null) {
			var parametroDto = factory.getParametroDto();
			if (StringUtils.isNotBlank(parametroDto.getGrupo())) {
				parametroDto.setGrupo(getParametersSvc().getIdByParametroGroup(parametroDto.getGrupo()));
			}
			var parametros = getParametersSvc().getAllParametros(parametroDto);
			getMapListToChoiceBox().put(nameField, parametros);
			return (List<D>) parametros;
		}
		var listOut = new ArrayList<D>();
		list.forEach(row -> listOut.add((D) row));
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
}
