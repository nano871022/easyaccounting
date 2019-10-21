package co.com.japl.ea.interfaces;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.apache.commons.collections4.MultiValuedMap;
import org.pyt.common.abstracts.ADto;
import org.pyt.common.annotation.generics.AssingValue;
import org.pyt.common.common.SelectList;
import org.pyt.common.constants.LanguageConstant;
import org.pyt.common.exceptions.GenericServiceException;
import org.pyt.common.exceptions.ReflectionException;
import org.pyt.common.exceptions.validates.ValidateValueException;

import com.pyt.service.dto.ParametroDTO;
import com.pyt.service.implement.GenericServiceSvc;
import com.pyt.service.interfaces.IGenericServiceSvc;

import javafx.scene.Node;
import javafx.scene.control.ChoiceBox;
import javafx.scene.layout.GridPane;

/**
 * Se encarga de generalizar las funciones que usaran las diferentes
 * implementaciones de campos genericos puestos sobre gridPanel
 * 
 * @author Alejo Parra
 *
 * @param <C>
 * @param <L>
 * @param <F>
 */
public interface IGenericFields<L extends ADto, F extends ADto> extends IGenericCommons<L, F> {
	/**
	 * Objeto tipo {@link GridPane}
	 * 
	 * @return {@link GridPane}
	 */
	public GridPane getGridPane();

	/**
	 * Instancia del objeto dto donde se obtienen los valores y se ponene los
	 * valores, dto maestro
	 * 
	 * @return {@link ADto}
	 */
	public F getInstanceDto();

	/**
	 * Servicio de parametros, este debe ser el generico, con esto permite realizar
	 * consultas dinamicamente.
	 * 
	 * @return {@link GenericServiceSvc} < {@link ParametroDTO} >
	 */
	public IGenericServiceSvc<ParametroDTO> getParametersSvc();

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
	public MultiValuedMap<String, Node> getMapFields();

	private void calcNextColumnAndRow(Index index) {
		if (getMaxColumns() > index.column) {
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

	/**
	 * Se encarga de configurar los campos que se mostraran en los filtros y fueron
	 * configurados como campos genericos, esto se pone el {@link GridPane}
	 * 
	 */
	default void loadFields(String... stylesGrid) {
		var list = getListGenericsFields();
		if (list == null)
			throw new RuntimeException(i18n().valueBundle(LanguageConstant.GENERIC_FIELD_NOT_FOUND_FIELD_TO_USE));
		var index = new Index();
		assingValueAnnotations();
		genericFormsUtils.configGridPane(getGridPane());
		list.forEach(field -> {
			try {
				var factory = LoadFieldsFactory.getInstance(field);
				var label = genericFormsUtils.createLabel(factory.getLabelText(), factory.getToolTip());
				var fieldControl = factory.create();
				var nameField = factory.getNameField();
				var typeField = getInstanceDto().getType(nameField);
				getMapFields().put(nameField, fieldControl);
				loadValuesInChoiceBox(typeField, factory, fieldControl, nameField);
				loadValuesFormToDTO(fieldControl, typeField, nameField);
				getGridPane().add(label, index.column, index.row);
				getGridPane().add(fieldControl, index.column + 1, index.row);
				Arrays.asList(stylesGrid).forEach(styleGrid -> getGridPane().getStyleClass().add(styleGrid));
				loadValueIntoForm(typeField, nameField, factory, fieldControl);
				calcNextColumnAndRow(index);
			} catch (ReflectionException e) {
				logger().logger("Problema al obtener el tipo del campo.", e);
			} catch (GenericServiceException e) {
				logger().logger("Problema al obtener la lista de valores.", e);
			}
		});
	}

	@SuppressWarnings({ "rawtypes" })
	private <N extends Node> void loadValueIntoForm(Class typeField, String fieldName, IFieldsCreator factory, N node)
			throws ReflectionException, GenericServiceException {
		if (genericFormsUtils.isChoiceBox(node)) {
			var list = getSelectedListToChoiceBox(fieldName, typeField, factory);
			genericFormsUtils.loadValuesInFxmlToChoice(factory.getNameFieldToShowInComboBox(), node,
					getInstanceDto().get(fieldName), null, list);
		} else {
			genericFormsUtils.loadValuesInFxml(getInstanceDto(), getGridPane());
		}
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void loadValuesFormToDTO(Node fieldControl, Class typeField, String nameField) {

		genericFormsUtils.inputListenerToAssingValue(fieldControl, value -> {
			try {
				getInstanceDto().set(nameField, validateValuesUtils.cast(value, typeField));
			} catch (ReflectionException e) {
				logger().logger("Problema al obtener el campo.", e);
			} catch (ValidateValueException e) {
				logger().logger("Problema al validar el valor.", e);
			}
		});

	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void loadValuesInChoiceBox(Class typeField, IFieldsCreator factory, Node fieldControl, String nameField)
			throws GenericServiceException {
		if (genericFormsUtils.isChoiceBox(fieldControl)) {
			var list = getSelectedListToChoiceBox(nameField, typeField, factory);
			SelectList.put((ChoiceBox) fieldControl, list, factory.getNameFieldToShowInComboBox());
		}
	}

	@SuppressWarnings("rawtypes")
	private boolean isParametroDTO(Class clazz) {
		try {
			if (clazz.cast(ParametroDTO.class) != null) {
				return true;
			}
		} catch (ClassCastException e) {
			logger().logger("Problema al realizar casteo.", e);
		}
		return false;
	}

	/**
	 * Se encarga de limpiar todos los campos configurados
	 */
	default void clearFields() {
		getMapFields().values().forEach(field -> genericFormsUtils.cleanValueByFieldFX(field));
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private <D extends ADto> List<D> getSelectedListToChoiceBox(String nameField, Class typeField,

			IFieldsCreator factory) throws GenericServiceException {
		var list = getMapListToChoiceBox().get(nameField);
		if (isParametroDTO(typeField) && list == null) {
			var parametroDto = factory.getParametroDto();
			var parametros = getParametersSvc().getAll(parametroDto);
			getMapListToChoiceBox().put(nameField, parametros);
			return (List<D>) parametros;
		}
		return new ArrayList<D>((Collection<? extends D>) list);
	}

	/**
	 * Se encarga asignar las anotaciones para poner los valores en el dto
	 * 
	 * @param dto extends {@link Object}
	 * @return extends {@link Object}
	 */
	@SuppressWarnings("unchecked")
	private F assingValueAnnotations() {
		var dto = getInstanceDto();
		Class<F> clazz = (Class<F>) dto.getClass();
		Arrays.asList(clazz.getDeclaredFields()).stream()
				.filter(field -> field.getAnnotation(AssingValue.class) != null).forEach(field -> {
					Arrays.asList(field.getAnnotationsByType(AssingValue.class)).forEach(annotation -> {
						try {
							dto.set(annotation.nameField(), annotation.value());
						} catch (ReflectionException e) {
							logger().logger(e);
						}
					});
				});
		return dto;
	}
}
