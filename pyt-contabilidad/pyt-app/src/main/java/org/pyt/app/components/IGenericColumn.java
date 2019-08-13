package org.pyt.app.components;

import java.util.Map;

import org.pyt.common.abstracts.ADto;
import org.pyt.common.common.UtilControlFieldFX;
import org.pyt.common.constants.LanguageConstant;
import org.pyt.common.constants.StylesPrincipalConstant;
import org.pyt.common.exceptions.ReflectionException;

import com.pyt.service.pojo.GenericPOJO;

import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

public interface IGenericColumn<T extends ADto> extends IGenericCommon<T> {

	Map<String, GenericPOJO<T>> getColumns();

	void setColumns(Map<String, GenericPOJO<T>> columns);

	TableView<T> getTableView();

	/**
	 * Se encarga de configurar el mapa de filtros y agregar los campos de filtros a
	 * la pantalla
	 * 
	 * @throws {@link IllegalAccessException}
	 */
	default void configColumns() throws IllegalAccessException {
		var filters = getMapFieldsByObject(getColumns(), GenericPOJO.Type.COLUMN);
		setColumns(filters);
		var util = new UtilControlFieldFX();
		getColumns().forEach((key, value) -> {
			var input = util.getFieldByField(value.getField());
			if (input != null) {
				var column = new TableColumn<T, String>(getI18n().valueBundle(
						LanguageConstant.GENERIC_FORM_COLUMN + getClazz().getSimpleName() + "." + value.getNameShow()));
				column.setCellValueFactory(table -> {
					try {
						var sop = new SimpleObjectProperty<String>();
						sop.setValue(table.getValue().get(value.getField().getName()));
						return sop;
					} catch (ReflectionException e) {
						getLogger().logger(e);
					}
					return null;
				});
				getTableView().getColumns().add(column);
				if (value.getWidth() > 0) {
					input.prefWidth(value.getWidth());
				}
			}
		});
		getTableView().getStyleClass().add(StylesPrincipalConstant.CONST_TABLE_CUSTOM);

	}
}
