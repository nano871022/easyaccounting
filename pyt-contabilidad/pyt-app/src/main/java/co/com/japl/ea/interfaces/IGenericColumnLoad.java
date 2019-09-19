package co.com.japl.ea.interfaces;

import org.pyt.common.abstracts.ADto;

import com.pyt.service.dto.DocumentosDTO;

import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

public interface IGenericColumnLoad<V extends ADto, O> extends IGenericMethodsCommon, IColumnCommon<V, O> {

	/**
	 * Se encarga de crear la grilla con todos los campos que se encontraton y
	 * ponerlos en el fxml
	 * 
	 * @throws org.pyt.common.exceptions.ReflectionException
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	default TableView<V> loadColumnsIntoTableView() throws Exception {

		var campos = getColumns();
		if (campos == null) {
			warning(getI18n().valueBundle("field_doesnt_found_to_process"));
			return new TableView();
		}
		var table = controlFieldUtils.configTableView(getTableView());
		campos.entrySet().stream().forEach(set -> {
			var docs = set.getValue();
			var column = new TableColumn(((DocumentosDTO) docs).getFieldLabel());
			column.setCellValueFactory(new PropertyValueFactory(((DocumentosDTO) docs).getFieldName()));
			table.getColumns().add(column);
		});
		return table;
	}

}
