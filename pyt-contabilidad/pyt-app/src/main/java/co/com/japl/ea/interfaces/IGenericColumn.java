package co.com.japl.ea.interfaces;

import java.lang.reflect.InvocationTargetException;

import org.pyt.common.abstracts.ADto;
import org.pyt.common.constants.LanguageConstant;
import org.pyt.common.constants.StylesPrincipalConstant;
import org.pyt.common.exceptions.ReflectionException;
import org.pyt.common.exceptions.validates.ValidateValueException;

import com.pyt.service.pojo.GenericPOJO;

import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

public interface IGenericColumn<T extends ADto> extends IGenericCommon<T>, IColumnCommon<T, GenericPOJO<T>> {

	/**
	 * Se encarga de configurar el mapa de filtros y agregar los campos de filtros a
	 * la pantalla
	 * 
	 * @throws NoSuchMethodException
	 * @throws InstantiationException
	 * @throws InvocationTargetException
	 * 
	 * @throws                           {@link IllegalAccessException}
	 */
	@Override
	default TableView<T> loadColumnsIntoTableView() throws Exception {
		var columns = getConfigFields(getInstaceOfGenericADto(), true, false);
		if (columns == null)
			throw new Exception(getI18n().valueBundle(LanguageConstant.GENERIC_FIELD_NOT_FOUND_FIELD_TO_USE));
		setColumns(columns);
		getColumns().entrySet().stream().sorted((compare1, compare2) -> {
			if (compare1 == null || compare1.getValue().getOrder() == null)
				return -1;
			return compare1.getValue().getOrder().compareTo(compare2.getValue().getOrder());
		}).forEachOrdered(value -> {
			var nameShowColumn = getNameShowInLabel(value.getValue());
			var column = new TableColumn<T, String>(nameShowColumn);
			column.setCellValueFactory(table -> {
				try {
					var sop = new SimpleObjectProperty<String>();
					sop.setValue(validateValues.cast(table.getValue().get(value.getValue().getField().getName()),
							String.class));
					return sop;
				} catch (ReflectionException | ValidateValueException e) {
					getLogger().logger(e);
				}
				return null;
			});
			getTableView().getColumns().add(column);
		});
		getTableView().getStyleClass().add(StylesPrincipalConstant.CONST_TABLE_CUSTOM);
		return getTableView();
	}
}
