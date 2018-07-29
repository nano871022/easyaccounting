package org.pyt.common.poi.docs;

import org.pyt.common.common.ValidateValues;

/**
 * Se encarga dde contener los metodos y propiedades para ser generico el uso del poi
 * @author Alejandro Parra
 * @since 27-07-2018
 */
public abstract class Poi extends VerifyFiles {
	protected String file;
	protected String fileOut;
	protected ValidateValues valid;
	public Poi() {
		valid = new ValidateValues();
	}
	
	public final void setFile(String pathFile) {
		file = pathFile;
	}

	public final void setFileOut(String pathFile) {
		fileOut = pathFile;
	}
}
