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
	private List<TableBookmark> listBookmarks;
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
	public final void tables(List<TableBookmark> listMaps) throws Exception {
		if (listMaps == null || listMaps.size() == 0)
			return;
		for (int i = 0; i < listMaps.size(); i++) {
			TableBookmark table = listMaps.get(i);
			String[] marks = table.getBookmarks();
			XWPFTableRow row = searchTabla(marks);
			while(table.hasNext()){
				Map<String, Object> map = table.next();
				cells(row, row.getTableCells(), map, table.getPosition());
			}
		}
	}

	private final void cells(XWPFTableRow row, List<XWPFTableCell> celdas, Map<String, Object> nameBookmarks,
			Integer position) {
		try {
			// XWPFTableRow rowUsar = new XWPFTableRow(row.getCtRow(), row.getTable());
			Set<String> keys = nameBookmarks.keySet();
			// for (int i = 0; i < celdas.size(); i++)
			// rowUsar.removeCell(0);
			XWPFTableRow rowUsar = null;
			for (int pos = 0; pos < celdas.size(); pos++) {
				XWPFTableCell cell = celdas.get(pos);
				for (XWPFParagraph p : cell.getParagraphs()) {
					for (CTBookmark b : p.getCTP().getBookmarkStartList()) {
						for (String key : keys) {
							if (b.getName().contains(key)) {
								XWPFRun run = null;
								XWPFParagraph para = null;

								if (rowUsar == null) {
									if (position == 1) {
										rowUsar = row;
									} else {
										rowUsar = row.getTable().insertNewTableRow(position);
									}
								}
								XWPFTableCell cellOut = rowUsar.getCell(pos);
								if (cellOut == null) {
									cellOut = rowUsar.addNewTableCell();
									putStyleCell(cellOut, cell);
								} else {
									putStyleCell(cellOut, cell);
								}
								if (cellOut.getParagraphs().size() > 0) {
									para = cellOut.getParagraph(p.getCTP());
									if (para == null) {
										para = cellOut.addParagraph();
										putStyleParagraph(para, p);
									}
								} else {
									para = cellOut.addParagraph();
									putStyleParagraph(para, p);
								}
								if (para.getRuns().size() > 0) {
									run = para.getRuns().get(0);
									putStyleRun(run, p.getRuns().get(0));
								} else {
									run = para.createRun();
									putStyleRun(run, p.getRuns().get(0));
								}
								run.setText(valid.cast(nameBookmarks.get(key), String.class), 0);
								break;
							}
						}
					}
				}

			}
			rowUsar = null;
		} catch (ValidateValueException e) {
			Log.logger(e);
		}
	}

	private final void putStyleCell(XWPFTableCell cell, XWPFTableCell cellLast) {
		cell.setColor(cellLast.getColor());
	}

	private final void putStyleParagraph(XWPFParagraph p, XWPFParagraph pLast) {
		p.setAlignment(pLast.getAlignment());
		p.setBorderBetween(pLast.getBorderBetween());
		p.setBorderBottom(pLast.getBorderBottom());
		p.setBorderLeft(pLast.getBorderLeft());
		p.setBorderRight(pLast.getBorderRight());
		p.setBorderTop(pLast.getBorderTop());
		p.setFontAlignment(pLast.getFontAlignment());
		p.setStyle(pLast.getStyle());
		p.setVerticalAlignment(pLast.getVerticalAlignment());
		try {
			p.setWordWrapped(pLast.isWordWrapped());
		} catch (IndexOutOfBoundsException e) {
		}
	}

	private final void putStyleRun(XWPFRun run, XWPFRun runLast) {
		run.setColor(runLast.getColor());
		run.setFontFamily(runLast.getFontFamily());
		run.setCapitalized(runLast.isCapitalized());
		run.setBold(runLast.isBold());
		run.setUnderline(runLast.getUnderline());
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

	/**
	 * se encarga de agregar la tabla de bookmarks
	 * 
	 * @param tableBookmarks
	 *            {@link TableBookmark}
	 */
	public void addTableBookmark(TableBookmark tableBookmarks) {
		if (tableBookmarks == null)
			return;
		if (tableBookmarks.getSize() == 0)
			return;
		if (listBookmarks == null) {
			listBookmarks = new ArrayList<TableBookmark>();
		}
		this.listBookmarks.add(tableBookmarks);
	}

	public void setBookmarks(Map<String, Object> bookmarks) {
		this.bookmarks = bookmarks;
	}

	public final static void main(String... strings) {
		Map<String, Object> maps = new HashMap<String, Object>();
		maps.put("idTabla", 10);
		maps.put("nombreTabla", "nombre del registro");
		maps.put("valorTabla", "valor del registro");
		Map<String, Object> maps2 = new HashMap<String, Object>();
		maps2.put("idTabla", 11);
		maps2.put("nombreTabla", "nombre del registro 2");
		maps2.put("valorTabla", "valor del registro 2");
		Map<String, Object> maps3 = new HashMap<String, Object>();
		maps3.put("idTabla", 12);
		maps3.put("nombreTabla", "nombre del registro 3");
		maps3.put("valorTabla", "valor del registro 3");
		Map<String, Object> maps4 = new HashMap<String, Object>();
		maps4.put("cantidad", 2);
		maps4.put("descripcionTabla2", "descripcion tabla 2");
		maps4.put("valorTabla2", 1000);
		maps4.put("subTotalTabla", 2000);
		TableBookmark tabla1 = new TableBookmark();
		tabla1.add(maps);
		tabla1.add(maps2);
		tabla1.add(maps3);
		TableBookmark tabla2 = new TableBookmark();
		tabla2.add(maps4);

		Map<String, Object> bookmarks = new HashMap<String, Object>();
		bookmarks.put("Prueba", " valor a cambiar ");
		bookmarks.put("between", " valor entre textos ");
		bookmarks.put("remplazo", " valor de remplazo ");
		DocX doc = new DocX();
		doc.addTableBookmark(tabla1);
		doc.addTableBookmark(tabla2);
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
