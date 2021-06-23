package co.com.japl.ea.interfaces;

import static org.pyt.common.constants.AppConstants.CONST_FIELD_ORDEN;
import static org.pyt.common.constants.AppConstants.CONST_FIELD_ORDER;
import static org.pyt.common.constants.languages.DinamicFields.CONST_ERR_FIELD_NOT_FOUND_PROCCES;
import static org.pyt.common.constants.languages.DinamicFields.CONST_ERR_FIELD_ORDER_VALIDATION;

import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.pyt.common.annotation.generics.DefaultFieldToGeneric.Uses;

import co.com.japl.ea.common.abstracts.ADto;
import co.com.japl.ea.dto.dto.ParametroDTO;
import co.com.japl.ea.dto.interfaces.IParametrosSvc;
import co.com.japl.ea.dto.system.ConfigGenericFieldDTO;
import co.com.japl.ea.exceptions.ParametroException;
import co.com.japl.ea.exceptions.ReflectionException;
import co.com.japl.ea.exceptions.validates.ValidateValueException;
import co.com.japl.ea.utls.DataTableFXMLUtil;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.input.MouseEvent;

/**
 * La interface se encarga de agregar las columnas
 * 
 * @author Alejo Parra
 *
 * @param <C>
 * @param <L>
 * @param <F>
 */
public interface IGenericColumns<L extends ADto, F extends ADto> extends IGenericCommons<L, F> {

	private Uses getUsesByUsedTypeGeneric(TypeGeneric typeGeneric) {
		if (Uses.FILTER.toString().contentEquals(typeGeneric.toString()))
			return Uses.FILTER;
		else if (Uses.COLUMN.toString().contentEquals(typeGeneric.toString()))
			return Uses.COLUMN;
		else
			return Uses.FIELD;
	}

	public IParametrosSvc getParametersSvc();

	/**
	 * Se encarga de cargar las columnas dentro de la {@link TableView}
	 * 
	 * @return
	 */
	@SuppressWarnings({ "unchecked" })
	default void loadColumns(String... stylesTable) {
		var list = getListGenericsFields(TypeGeneric.COLUMN);
		var table = genericFormsUtils.configTableView(getTableView());
		if (list == null) {
			var typeField = getUsesByUsedTypeGeneric(TypeGeneric.COLUMN);
			var generics = LoadFieldsFactory.getAnnotatedField(ConfigGenericFieldDTO.class, getClazz(), typeField);
			if (generics == null) {
				alerta(i18n().valueBundle(CONST_ERR_FIELD_NOT_FOUND_PROCCES));
				return;
			} else {
				list = (List<L>) generics;
			}
		}
		list.stream().sorted((object1, object2) -> {
			try {
				Object order1;
				order1 = object1.get(CONST_FIELD_ORDER);
				var order2 = object2.get(CONST_FIELD_ORDER);
				if (order1 != null && order2 != null) {
					return validateValuesUtils.compareNumbers(order1, order2);
				}
				order1 = object1.get(CONST_FIELD_ORDEN);
				order2 = object2.get(CONST_FIELD_ORDEN);
				if (order1 != null && order2 != null) {
					return validateValuesUtils.compareNumbers(order1, order2);
				}
			} catch (ReflectionException e) {
				logger().DEBUG(i18n().valueBundle(CONST_ERR_FIELD_ORDER_VALIDATION), e);
			}
			return 0;
		}).forEach(field -> {
			var factory = LoadFieldsFactory.getInstance(field);
			var fieldName = factory.getNameField();
			var column = new TableColumn<F, String>(factory.getLabelText().get());
			column.setCellValueFactory(cellValue -> {
				try {
					var sop = new SimpleObjectProperty<String>();
					var valueField = cellValue.getValue().get(fieldName);
					if (valueField instanceof ADto) {
						var value = ((ADto) valueField).get(factory.getNameFieldToShowInComboBox());
						sop.setValue(validateValuesUtils.cast(value, String.class));
					} else {
						var result = findParametroGroup(valueField, factory);
						if (result != null) {
							sop.setValue(validateValuesUtils.cast(((ParametroDTO) result).getNombre(), String.class));
						} else {
							sop.setValue(validateValuesUtils.cast(valueField, String.class));
						}
					}
					return sop;
				} catch (ReflectionException | ValidateValueException e) {
					logger().DEBUG(e);
				}
				return null;
			});
			table.getColumns().add(column);
		});
		getTableView().setOnMouseClicked(eventHandler -> selectedRow(eventHandler));
		Arrays.asList(stylesTable).forEach(styleTable -> getTableView().getStyleClass().add(styleTable));
	}

	@SuppressWarnings("unchecked")
	private <D, O extends ADto> O findParametroGroup(D valueField, IFieldsCreator factory) {
		try {
			if (!(valueField instanceof ParametroDTO)) {
				var parametro = factory.getParametroDto();
				if (StringUtils.isNotBlank(parametro.getGrupo())) {
					var grupoSave = parametro.getGrupo();
					if (StringUtils.isNotBlank(parametro.getGrupo())) {
						parametro.setGrupo(getParametersSvc().getIdByParametroGroup(parametro.getGrupo()));
					}
					parametro = getParametroFromFindGroup(parametro, grupoSave);
					var parametros = getParametersSvc().getAllParametros(parametro);
					if (parametros.size() > 0) {
						return (O) parametros.stream()
								.filter(row -> validateValuesUtils.validate(valueField,
										validateValuesUtils.cast(row.getValor(), valueField.getClass())))
								.findAny().orElse(null);
					}
				}
			}
		} catch (ParametroException e) {
			logger().logger(e);
		}
		return null;
	}

	void selectedRow(MouseEvent eventHandler);

	/**
	 * Se encarga de obtener la {@link TableView}
	 * 
	 * @return {@link TableView}
	 */
	public TableView<F> getTableView();

	public DataTableFXMLUtil<F, F> getTable();
}
