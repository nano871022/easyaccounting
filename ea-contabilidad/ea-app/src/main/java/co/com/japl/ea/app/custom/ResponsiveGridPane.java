package co.com.japl.ea.app.custom;

import static co.com.japl.ea.app.enums.ResponsiveSizeEnum.LARGE;
import static co.com.japl.ea.app.enums.ResponsiveSizeEnum.better;
import static javafx.geometry.Pos.TOP_CENTER;

import java.util.ArrayList;
import java.util.List;

import org.pyt.common.common.ListUtils;
import org.pyt.common.constants.CSSConstant;

import co.com.japl.ea.app.enums.ResponsiveSizeEnum;
import javafx.beans.value.ObservableValue;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;

/**
 * Se crea un metodo para generar una grid pane que se aresponsive al tamaño de
 * la ventana
 * 
 * @author alejo parra
 * @since 01/07/2021
 * @version 1.0
 */
public class ResponsiveGridPane extends GridPane {
	private ResponsiveSizeEnum responsiveEnum = LARGE;
	private List<ResponsiveRow> rows;

	public ResponsiveGridPane() {
		super();
		rows = new ArrayList<ResponsiveRow>();
		setAlignment(TOP_CENTER);
		columnConstraints();
		widthProperty().addListener(this::eventWidthHandler);
	}

	private final Integer maxColumn = 12;

	private void eventWidthHandler(ObservableValue<?> observable, Number older, Number newer) {
		var responsive = better(newer.intValue());
		if (responsiveEnum != responsive) {
			responsiveEnum = responsive;
			calculateNodePosition();
		}
	}

	private void columnConstraints() {
		getColumnConstraints().clear();

		double width = 100.0 / maxColumn;
		for (int i = 0; i < maxColumn; i++) {
			var columnConstraint = new ColumnConstraints();
			columnConstraint.setPercentWidth(width);
			getColumnConstraints().add(columnConstraint);
		}

	}

	private void calculateNodePosition() {
		int current = 0;
		for (var row : rows) {
			current += row.calculateRowPosition(current, responsiveEnum);
		}
	}

	public ResponsiveGridPane newRow() {
		rows.add(new ResponsiveRow(maxColumn));
		return this;
	}

	public void add(Node node) {
		if (ListUtils.isBlank(rows)) {
			rows.add(new ResponsiveRow(maxColumn));
		}
		var columns = new ResponsiveColumn(node);
		if (node instanceof Label e) {
			var lengthText = e.getText().length() * 10;
			var responsive = better(lengthText);
			columns.setResponsiveEnumColumnWidth(responsive, 2);
		} else {
			columns.setResponsiveEnumColumnWidth(responsiveEnum, 2);
		}
		rows.get(rows.size() - 1).addColumn(columns);
		calculateNodePosition();
		getChildren().add(columns.getContent());
		getStylesheets().add(CSSConstant.CONST_PRINCIPAL);
		getStyleClass().add("borderView");
		setFillWidth(columns.getContent(), true);
		setFillHeight(columns.getContent(), true);
	}

	public void addRow(ResponsiveRow row) {
		if (rows.contains(row)) {
			return;
		}
		rows.add(row);
		calculateNodePosition();
		for (var column : row.getColumns()) {
			getChildren().add(column.getContent());
			setFillWidth(column.getContent(), true);
			setFillHeight(column.getContent(), true);
		}
	}

	public void removeRow(ResponsiveRow row) {
		rows.remove(row);
		calculateNodePosition();
		for (var column : row.getColumns()) {
			getChildren().remove(column.getContent());
		}
	}

	public ResponsiveSizeEnum getResponsiveEnum() {
		return responsiveEnum;
	}
}
