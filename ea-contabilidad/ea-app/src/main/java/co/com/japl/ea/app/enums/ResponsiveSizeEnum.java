package co.com.japl.ea.app.enums;

import java.util.Arrays;

public enum ResponsiveSizeEnum {
	XSMALL(0, 0, 380), SMALL(1, 380, 576), MEDIUMSMALL(2, 576, 720), MEDIUM(3, 720, 900), MEDIUMLARGE(4, 900, 1000),
	LARGE(5, 1000, 1200), XLARGE(6, 1200, 1400), XXLARGE(6, 1400, 5000);

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
