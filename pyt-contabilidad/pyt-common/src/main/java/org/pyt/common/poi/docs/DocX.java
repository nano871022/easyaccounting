package org.pyt.common.poi.docs;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTText;
import org.pyt.common.common.Log;
import org.pyt.common.exceptions.ValidateValueException;

/**
 * Se ecarga de llenar los marcadores dentro de un archivo de word docx
 * 
 * @author Alejandro Parra
 * @since 27/07/2018
 */
public class DocX extends TableInDocx {
	private Bookmark bookmarks;
	private final Log logger = Log.Log(DocX.class);

	/**
	 * Se encarga de generar la conversion de los marcadores por los valores en
	 * parrafos como en tablas de todo el documento.
	 * 
	 * @throws Exception
	 */
	public final void generar() throws Exception {
		try {
			if (validFile(file)) {
				docx = new XWPFDocument(new FileInputStream(file));
				for (String key : bookmarks.getBookmarks()) {
					XWPFParagraph parrafo = searchBookmarkP(docx.getParagraphs(), key);
					if (parrafo == null) {
						parrafo = searchBookmarkT(docx.getTables(), key);
					}
					if (parrafo != null) {
						replaceBookmark(parrafo, key, bookmarks.getValue(key));
					}
				}
				tables(listBookmarks);
				writeFileOut();
			} else {
				logger.error("Se presento error en la validacion del archivo.");
			}
		} catch (IOException | ValidateValueException e) {
			logger.logger(e);
		}
	}

	/**
	 * Se crea usa para cambiar el marcador por un valor.
	 * 
	 * @param bookmark
	 *            {@link CTText}
	 * @param value
	 *            {@link Object}
	 * @throws {@link
	 *             ValidateValueException}
	 */
	@SuppressWarnings("unused")
	private final <T extends Object> void changeMarcador(CTText bookmark, T value) throws ValidateValueException {
		bookmark.setStringValue(valid.cast(value, String.class));
	}

	/**
	 * Se encarga de escribir el archivo de salida en la ruta indicada el documento
	 * que se ha modificado
	 * 
	 * @throws {@link
	 *             IOException}
	 */
	private final void writeFileOut() throws IOException {
		if (!validFile(fileOut)) {
			FileOutputStream fo = new FileOutputStream(new File(fileOut));
			docx.write(fo);
			fo.close();
			docx.close();
		}
	}


	public final void setBookmarks(Bookmark bookmarks) {
		this.bookmarks = bookmarks;
	}

	
}
