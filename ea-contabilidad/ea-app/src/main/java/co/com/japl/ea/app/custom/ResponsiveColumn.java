package co.com.japl.ea.app.custom;

import java.util.Arrays;
import java.util.stream.Collectors;

import co.com.japl.ea.app.enums.ResponsiveSizeEnum;
import javafx.scene.Node;

public class ResponsiveColumn {
	private final Node content;
	private Integer maxColumn = 12;
	private Integer[] columnWidth;
	private ResponsiveSizeEnum responsive;

	public ResponsiveColumn(Node content) {
		this.content = content;
		clearColumnWidth();
	}

	public int getColumnWidth(ResponsiveSizeEnum resposive) {
		for (int i = responsive.getValue(); i >= 0; i--) {
			if (isValid(columnWidth[i])) {
				return columnWidth[i];
			}
		}
		return responsive.getValue();
	}

	public void unsetResponsiveEnum(ResponsiveSizeEnum responsive) {
		columnWidth[responsive.getValue()] = -1;
	}

	public void unsetAllResponsiveEnum() {
		clearColumnWidth();
	}

	private void clearColumnWidth() {
		columnWidth = Arrays.asList(ResponsiveSizeEnum.values()).stream().map(value -> -1).collect(Collectors.toList())
				.toArray(Integer[]::new);
	}

	public void setResposiveEnum(ResponsiveSizeEnum responsive) {
		this.responsive = responsive;
	}

	public void setResponsiveEnumColumnWidth(ResponsiveSizeEnum responsive, int width) {
		this.responsive = responsive;
		columnWidth[responsive.getValue()] = Math.max(1, Math.min(maxColumn, width));
	}

	public void setMaxColumns(int columns) {
		this.maxColumn = columns;
	}

	public Node getContent() {
		return content;
	}

	public boolean isValid(int value) {
		return value > 0 && value <= maxColumn;
	}
}
