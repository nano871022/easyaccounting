package co.com.japl.ea.app.enums;

import java.util.Arrays;

public enum ResponsiveSizeEnum {
	XSMALL(0, 0, 420), SMALL(1, 420, 700), MEDIUMSMALL(2, 0, 0), MEDIUM(3, 700, 1200), MEDIUMLARGE(4, 0, 0),
	LARGE(5, 1200, 5000), XLARGE(6, 0, 0), XXLARGE(6, 0, 0);

	private int value;
	private int minWidth;
	private int maxWidth;

	ResponsiveSizeEnum(int value, int minWidth, int maxWidth) {
		this.value = value;
		this.minWidth = minWidth;
		this.maxWidth = maxWidth;
	}

	public int getValue() {
		return value;
	}

	public int getMinWidth() {
		return minWidth;
	}

	public int getMaxWidth() {
		return maxWidth;
	}

	public boolean between(int width) {
		return width >= minWidth && width < maxWidth;
	}

	public static ResponsiveSizeEnum better(int width) {
		return Arrays.asList(values()).stream().filter(value -> width >= value.minWidth && width < value.maxWidth)
				.findFirst().orElse(MEDIUM);
	}
}
