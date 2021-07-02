package co.com.japl.ea.app.custom;

import java.util.ArrayList;
import java.util.List;

import co.com.japl.ea.app.enums.ResponsiveSizeEnum;
import javafx.scene.layout.GridPane;

public class ResponsiveRow {
	private List<ResponsiveColumn> columns;
	private Integer maxColumns;

	public ResponsiveRow(Integer maxColumns) {
		columns = new ArrayList<ResponsiveColumn>();
		this.maxColumns = maxColumns;
	}

	public int calculateRowPosition(Integer lastGridPaneRow, ResponsiveSizeEnum responsiveEnum) {
		int inputRow = lastGridPaneRow;
		if (columns.isEmpty()) {
			return 0;
		}
		addNode(lastGridPaneRow, responsiveEnum);
		return lastGridPaneRow - inputRow + 1;
	}

	private void addNode(Integer lastGridPaneRow, ResponsiveSizeEnum responsiveEnum) {
		int currentGridPPaneColumn = 0;
		for (var column : columns) {
			int contentWidth = column.getColumnWidth(responsiveEnum);
			// incrementa fila y reinicia columna
			if (currentGridPPaneColumn + contentWidth > maxColumns) {
				lastGridPaneRow++;
				currentGridPPaneColumn = 0;
			}
			if (responsiveEnum == ResponsiveSizeEnum.XSMALL) {
				contentWidth = maxColumns;
				lastGridPaneRow++;
				currentGridPPaneColumn = 0;
			} else if (responsiveEnum == ResponsiveSizeEnum.SMALL) {
				contentWidth = 6;
				if (currentGridPPaneColumn == 2) {
					lastGridPaneRow++;
					currentGridPPaneColumn = 0;
				}
			} else if (responsiveEnum == ResponsiveSizeEnum.MEDIUMSMALL) {
				contentWidth = 5;
				if (currentGridPPaneColumn == 4) {
					lastGridPaneRow++;
					currentGridPPaneColumn = 0;
				}
			} else if (responsiveEnum == ResponsiveSizeEnum.MEDIUM) {
				contentWidth = 4;
				if (currentGridPPaneColumn == 6) {
					lastGridPaneRow++;
					currentGridPPaneColumn = 0;
				}
			} else if (responsiveEnum == ResponsiveSizeEnum.MEDIUMLARGE) {
				contentWidth = 3;
				if (currentGridPPaneColumn == 8) {
					lastGridPaneRow++;
					currentGridPPaneColumn = 0;
				}
			} else if (responsiveEnum == ResponsiveSizeEnum.LARGE) {
				contentWidth = 2;
				if (currentGridPPaneColumn == 10) {
					lastGridPaneRow++;
					currentGridPPaneColumn = 0;
				}
			} else if (responsiveEnum == ResponsiveSizeEnum.XLARGE) {
				contentWidth = 1;
				if (currentGridPPaneColumn == 12) {
					lastGridPaneRow++;
					currentGridPPaneColumn = 0;
				}
			}

			GridPane.setConstraints(column.getContent(), currentGridPPaneColumn, lastGridPaneRow, contentWidth, 1);
			currentGridPPaneColumn += contentWidth;
		}
	}

	public void addColumn(ResponsiveColumn column) {
		if (column == null) {
			return;
		}
		columns.add(column);
	}

	public void removeColumn(ResponsiveColumn column) {
		columns.remove(column);
	}

	public void clear() {
		columns.clear();
	}

	public List<ResponsiveColumn> getColumns() {
		return columns;
	}

}
