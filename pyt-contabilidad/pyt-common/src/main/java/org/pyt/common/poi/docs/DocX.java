package org.pyt.common.poi.docs;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.apache.poi.xwpf.usermodel.XWPFTable;
import org.apache.poi.xwpf.usermodel.XWPFTableCell;
import org.apache.poi.xwpf.usermodel.XWPFTableRow;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTBookmark;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTText;
import org.pyt.common.common.Log;
import org.pyt.common.exceptions.ValidateValueException;

/**
 * Se ecarga de llenar los marcadores dentro de un archivo de word docx
 * 
 * @author Alejandro Parra
 * @since 27/07/2018
 */
public class DocX extends Poi {
	private XWPFDocument docx;
	private List<Map<String, Object>> listBookmarks;
	private Map<String, Object> bookmarks;

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
				for (String key : bookmarks.keySet()) {
					XWPFParagraph parrafo = searchBookmarkP(docx.getParagraphs(), key);
					if (parrafo != null) {
						replaceBookmark(parrafo, key, bookmarks.get(key));
					}
				}
				tables(listBookmarks);
				writeFileOut();
			} else {
				Log.error("Se presento error en la validacion del archivo.");
			}
		} catch (IOException | ValidateValueException e) {
			Log.logger(e);
		}
	}

	/**
	 * Se encarga de buscar en las tablas dentro del documento para cambiar los book
	 * marks y crear registro nuevos segun valores indicados
	 * 
	 * @param maps
	 *            {@link Map}
	 * @throws {@link
	 *             ValidateValueException}
	 */
	public final void tables(List<Map<String, Object>> listMaps) throws Exception {
		if (listMaps == null || listMaps.size() == 0)
			return;
		Map<String, Object> maps = listMaps.get(0);
		String[] marks = maps.keySet().stream().toArray(size -> new String[size]);
		XWPFTableRow row = searchTabla(marks);
		for (int i = 0; i < listMaps.size(); i++) {
			Map<String, Object> map = listMaps.get(i);
			cells(row, row.getTableCells(), map, i +1);
		}
	}

	private final void cells(XWPFTableRow row, List<XWPFTableCell> celdas, Map<String, Object> nameBookmarks,
			Integer position) {
		try {
			XWPFTableRow rowUsar = null;
			Set<String> keys = nameBookmarks.keySet();
			for (int pos = 0; pos < celdas.size(); pos++) {
				XWPFTableCell cell = celdas.get(pos);
				for (String key : keys) {
					CTText valor = searchBookmark(cell.getParagraphs(), key);
					if (valor != null) {
						if (rowUsar == null) {
								rowUsar = null;
								rowUsar = new XWPFTableRow(row.getCtRow(), row.getTable());
								rowUsar.getTable().addRow(rowUsar, position);
						}
//						rowUsar.removeCell(pos);
//						XWPFTableCell celll = rowUsar.createCell();
						XWPFTableCell celll = rowUsar.getCell(pos);
						
						if (celll != null) {
							valor.setStringValue(valid.cast(nameBookmarks.get(key), String.class));
//							celll.setText(valid.cast(nameBookmarks.get(key), String.class));
						} else {
							Log.logger("La celda con posicion " + pos
									+ " no fue encontrada, la cual debe ponerse el valor " + nameBookmarks.get(key)
									+ " en la columan " + key);
						}
					}
				}
			}
		} catch (ValidateValueException e) {
			Log.logger(e);
		}
	}

	/**
	 * Se encarga de buscar entre las tabals los bokkmarks y retorna la fila que
	 * contiene los bokkmarks buscados
	 * 
	 * @param nameBookmarks
	 *            {@link String} to arrays
	 * @return {@link XWPFTableRow}
	 */
	private final XWPFTableRow searchTabla(String... nameBookmarks) {
		List<XWPFTable> listT = docx.getTables();
		for (XWPFTable t : listT) {
			List<XWPFTableRow> listRows = t.getRows();
			for (XWPFTableRow row : listRows) {
				if (verifyBookmark(row, nameBookmarks)) {
					return row;
				}
			}
		}
		return null;
	}

	/**
	 * Se encarga de obtener el bookmark indicado de entre la fila para realizar la
	 * modificacion
	 * 
	 * @param row
	 *            {@link XWPFTableRow}
	 * @param nameBookmark
	 *            {@link String}
	 * @return {@link CTText}
	 */
	@SuppressWarnings("unused")
	private final CTText getBookmark(XWPFTableRow row, String nameBookmark) {
		List<XWPFTableCell> listCells = row.getTableCells();
		for (XWPFTableCell cell : listCells) {
			CTText bookmark = searchBookmark(cell.getParagraphs(), nameBookmark);
			if (bookmark != null) {
				return bookmark;
			}
		}
		return null;
	}

	/**
	 * Se encarga de verificar si la fila suministrada contiene los bookmarks en las
	 * diferentes celdas.
	 * 
	 * @param row
	 *            {@link XWPFTableRow}
	 * @param nameColumns
	 *            {@link String} columnas
	 * @return {@link Boolean}
	 */
	private final Boolean verifyBookmark(XWPFTableRow row, String... nameColumns) {
		List<XWPFTableCell> listCells = row.getTableCells();
		Boolean valid = true;
		Boolean valids = false;
		for (XWPFTableCell cell : listCells) {
			for (String nameBookmark : nameColumns) {
				CTText bookmark = searchBookmark(cell.getParagraphs(), nameBookmark);
				if (bookmark != null) {
					valids = true;
					break;
				}
			}
			valid &= valids;
		}
		return valid;
	}

	/**
	 * Se encarag de obtener el marcador y se retorna el objeto con el cual se
	 * realiza el cambio del valor del mismo
	 * 
	 * @param nameBookmark
	 *            {@link String}
	 * @return {@link CTText}
	 */
	private final CTText searchBookmark(List<XWPFParagraph> list, String nameBookmark) {
		for (XWPFParagraph p : list) {
			List<CTBookmark> lists = p.getCTP().getBookmarkStartList();
			for (CTBookmark bookMark : lists) {
				if (bookMark.getName().contains(nameBookmark)) {
					return p.getCTP().addNewR().addNewT();
				}
			}
		}
		return null;
	}

	/**
	 * Se encarga de realizar la bsuqueda del bookmark y retorna el parrafo donde se
	 * encontro el bookmark
	 * 
	 * @param list
	 *            {@link List} of {@link XWPFParagraph}
	 * @param nameBookmark
	 *            {@link String}
	 * @return {@link XWPFParagraph}
	 */
	private final XWPFParagraph searchBookmarkP(List<XWPFParagraph> list, String nameBookmark) {
		for (XWPFParagraph p : list) {
			List<CTBookmark> lists = p.getCTP().getBookmarkStartList();
			for (CTBookmark bookMark : lists) {
				if (bookMark.getName().contains(nameBookmark)) {
					return p;
				}
			}
		}
		return null;
	}

	/**
	 * Se encarga de remplazar el marcador sobre el parrafo
	 * 
	 * @param list
	 *            {@link List}
	 * @param nameBookmark
	 *            {@link String}
	 * @param value
	 *            {@link Object}
	 * @throws {@link
	 *             Exception}
	 */
	private final <T extends Object> void replaceBookmark(XWPFParagraph parrafo, String nameBookmark, T value)
			throws Exception {
		XWPFRun run = null;
		List<CTBookmark> lists = parrafo.getCTP().getBookmarkStartList();
		for (CTBookmark bookMark : lists) {
			if (bookMark.getName().contains(nameBookmark)) {
				run = parrafo.createRun();
				run.setText(valid.cast(value, String.class));
				parrafo.getCTP().getDomNode().insertBefore(run.getCTR().getDomNode(), bookMark.getDomNode());
				break;
			}
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
		if (validFile(fileOut)) {
			FileOutputStream fo = new FileOutputStream(new File(fileOut));
			docx.write(fo);
		}

	}

	public void setListBookmarks(List<Map<String, Object>> listBookmarks) {
		this.listBookmarks = listBookmarks;
	}

	public void setBookmarks(Map<String, Object> bookmarks) {
		this.bookmarks = bookmarks;
	}

	public final static void main(String... strings) {
		List<Map<String, Object>> listMarks = new ArrayList<Map<String, Object>>();
		Map<String, Object> maps = new HashMap<String, Object>();
		maps.put("idTabla", 10);
		maps.put("nombreTabla", "nombre del registro");
		maps.put("valorTabla", "valor del registro");
		Map<String, Object> maps2 = new HashMap<String, Object>();
		maps2.put("idTabla", 11);
		maps2.put("nombreTabla", "nombre del registro 2");
		maps2.put("valorTabla", "valor del registro 2");
		listMarks.add(maps);
		listMarks.add(maps2); 

		Map<String, Object> bookmarks = new HashMap<String, Object>();
		bookmarks.put("Prueba", " valor a cambiar ");
		bookmarks.put("between", " valor entre textos ");
		bookmarks.put("remplazo", " valor de remplazo ");
		DocX doc = new DocX();
		doc.setListBookmarks(listMarks);
		doc.setBookmarks(bookmarks);
		doc.setFile("./docs/text.docx");
		doc.setFileOut("./docs/text2.docx");
		try {
			doc.generar();
		} catch (Exception e) {
			System.err.println(e);
		}

	}

}
