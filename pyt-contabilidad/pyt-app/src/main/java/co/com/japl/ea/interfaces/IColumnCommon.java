package co.com.japl.ea.interfaces;

import java.util.Map;

import org.pyt.common.abstracts.ADto;

import javafx.scene.control.TableView;

public interface IColumnCommon<V extends ADto, O> {

	public TableView<V> getTableView();

	public Map<String, O> getColumns();

	public void setColumns(Map<String, O> columns);

	TableView<V> loadColumnsIntoTableView() throws Exception;
}
