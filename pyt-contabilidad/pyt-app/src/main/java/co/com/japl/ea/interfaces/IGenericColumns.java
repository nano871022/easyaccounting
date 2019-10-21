package co.com.japl.ea.interfaces;

import java.util.Arrays;

import org.pyt.common.abstracts.ADto;
import org.pyt.common.exceptions.ReflectionException;
import org.pyt.common.exceptions.validates.ValidateValueException;

import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

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
	/**
	 * Se encarga de cargar las columnas dentro de la {@link TableView}
	 * 
	 * @return
	 */
	@SuppressWarnings({ "unchecked" })
	default void loadColumns(String... stylesTable) {
		var list = getListGenericsFields();
		var table = genericFormsUtils.configTableView(getTableView());
		if (list == null) {
			alerta(i18n().valueBundle("field_doesnt_found_to_process"));
			return;
		}
		list.stream().sorted((object1, object2) -> {
			try {
				Object order1;
				order1 = object1.get("order");
				var order2 = object2.get("order");
				if (order1 != null && order2 != null) {
					return validateValuesUtils.compareNumbers(order1, order2);
				}
			} catch (ReflectionException e) {
				logger().logger("Problema en validacion de campos de order.", e);
			}
			return 0;
		}).forEach(field -> {
			var factory = LoadFieldsFactory.getInstance(field);
			var fieldName = factory.getNameField();
			var column = new TableColumn<C, String>(factory.getLabelText());
			column.setCellValueFactory(cellValue -> {
				try {
					var sop = new SimpleObjectProperty<String>();
					sop.setValue(validateValuesUtils.cast(cellValue.getValue().get(fieldName), String.class));
					return sop;
				} catch (ReflectionException | ValidateValueException e) {
					logger().logger(e);
				}
				return null;
			});
			table.getColumns().add(column);
		});

		Arrays.asList(stylesTable).forEach(styleTable -> getTableView().getStyleClass().add(styleTable));
	}

	/**
	 * Se encarga de obtener la {@link TableView}
	 * 
	 * @return {@link TableView}
	 */
	public TableView<C> getTableView();
}
