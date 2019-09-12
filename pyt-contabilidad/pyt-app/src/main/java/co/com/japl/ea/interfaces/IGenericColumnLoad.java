package co.com.japl.ea.interfaces;

import java.util.List;

import org.pyt.common.abstracts.ADto;

import com.pyt.service.dto.DocumentosDTO;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;

public interface IGenericColumnLoad extends IGenericFieldCommon {

	public TableView getTableView();

	public List<DocumentosDTO> getColumns();

	/**
	 * Se encarga de crear la grilla con todos los campos que se encontraton y
	 * ponerlos en el fxml
	 * 
	 * @throws org.pyt.common.exceptions.ReflectionException
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	default <N, M extends ADto> TableView loadColumnsIntoTableView() {

		var campos = getColumns();
		if (campos == null) {
			warning(getI18n().valueBundle("field_doesnt_found_to_process"));
			return new TableView();
		}
		var table = configTableView();
		for (DocumentosDTO docs : campos) {
			var column = new TableColumn(docs.getFieldLabel());
			column.setCellValueFactory(new PropertyValueFactory(docs.getFieldName()));
			table.getColumns().add(column);
		} // end for
		return table;
	}

	/**
	 * Se encarga de configurar el grid panel para el formulario
	 * 
	 * @return {@link GridPane}
	 */
	private TableView configTableView() {
		var table = getTableView();
		table.setMaxWidth(1.7976931348623157E308);
		table.setPadding(new Insets(10));
		BorderPane.setAlignment(table, Pos.TOP_LEFT);
		return table;
	}
}
