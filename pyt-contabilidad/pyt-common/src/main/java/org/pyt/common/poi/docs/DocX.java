package org.pyt.common.poi.docs;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
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

import fr.opensagres.poi.xwpf.converter.pdf.PdfConverter;
import fr.opensagres.poi.xwpf.converter.pdf.PdfOptions;

/**
 * Se ecarga de llenar los marcadores dentro de un archivo de word docx
 * 
 * @author Alejandro Parra
 * @since 27/07/2018
 */
public class DocX extends Poi {
	private XWPFDocument docx;
	private List<TableBookmark> listBookmarks;
	private Bookmark bookmarks;
	private Log logger = Log.Log(this.getClass());

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
	 * Se encarga de buscar en las tablas dentro del documento para cambiar los book
	 * marks y crear registro nuevos segun valores indicados
	 * 
	 * @param maps
	 *            {@link Map}
	 * @throws {@link
	 *             ValidateValueException}
	 */
	private final void tables(List<TableBookmark> listMaps) throws Exception {
		if (listMaps == null || listMaps.size() == 0)
			return;
		for (int i = 0; i < listMaps.size(); i++) {
			TableBookmark table = listMaps.get(i);
			String[] marks = table.getBookmarks();
			XWPFTableRow row = searchTabla(marks);
			while (table.hasNext()) {
				Bookmark bookmark = table.next();
				if (row != null) {
					cells(row, row.getTableCells(), bookmark, table.getPosition() + 1);
				}
			}
		}
	}

	private final void cells(XWPFTableRow row, List<XWPFTableCell> celdas, Bookmark nameBookmarks, Integer position) {
		try {
			String[] keys = nameBookmarks.getBookmarks();
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
									if (p.getRuns().size() > 0) {
										putStyleRun(run, p.getRuns().get(0));
									}
								}
								run.setText(valid.cast(nameBookmarks.getValue(key), String.class), 0);
								break;
							}
						}
					}
				}

			}
			rowUsar = null;
		} catch (ValidateValueException e) {
			logger.logger(e);
		}
	}

	private final void putStyleCell(XWPFTableCell cell, XWPFTableCell cellLast) {
		if (StringUtils.isNotBlank(cellLast.getColor()))
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
		if (StringUtils.isNotBlank(pLast.getStyle()))
			p.setStyle(pLast.getStyle());
		p.setVerticalAlignment(pLast.getVerticalAlignment());
		try {
			p.setWordWrapped(pLast.isWordWrapped());
		} catch (IndexOutOfBoundsException e) {
		}
	}

	private final void putStyleRun(XWPFRun run, XWPFRun runLast) {
		if (StringUtils.isNotBlank(runLast.getColor()))
			run.setColor(runLast.getColor());
		if (StringUtils.isNotBlank(runLast.getFontFamily()))
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
				if (bookMark.getName().contentEquals(nameBookmark)) {
					return p;
				}
			}
		}
		return null;
	}

	/**
	 * Se encarga de buscar dentro de las tabals un bookname
	 * 
	 * @param list
	 *            List of XWPFTable
	 * @param nameBookmark
	 *            String
	 * @return
	 */
	private final XWPFParagraph searchBookmarkT(List<XWPFTable> list, String nameBookmark) {
		for (XWPFTable t : list) {
			List<XWPFTableRow> rows = t.getRows();
			for (XWPFTableRow row : rows) {
				List<XWPFTableCell> cells = row.getTableCells();
				for (XWPFTableCell cell : cells) {
					List<XWPFParagraph> paragraphs = cell.getParagraphs();
					for (XWPFParagraph p : paragraphs) {
						List<CTBookmark> lists = p.getCTP().getBookmarkStartList();
						for (CTBookmark bookMark : lists) {
							if (bookMark.getName().contentEquals(nameBookmark)) {
								return p;
							}
						}
					}
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
			fo.close();
			docx.close();
		}
	}

	/**
	 * se encarga de agregar la tabla de bookmarks
	 * 
	 * @param tableBookmarks
	 *            {@link TableBookmark}
	 */
	public final void addTableBookmark(TableBookmark tableBookmarks) {
		if (tableBookmarks == null)
			return;
		if (tableBookmarks.getSize() == 0)
			return;
		if (listBookmarks == null) {
			listBookmarks = new ArrayList<TableBookmark>();
		}
		this.listBookmarks.add(tableBookmarks);
	}

	public final void setBookmarks(Bookmark bookmarks) {
		this.bookmarks = bookmarks;
	}

	
}
