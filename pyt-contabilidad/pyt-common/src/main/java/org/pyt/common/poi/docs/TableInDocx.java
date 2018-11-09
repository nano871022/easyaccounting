package org.pyt.common.poi.docs;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.apache.poi.xwpf.usermodel.XWPFTable;
import org.apache.poi.xwpf.usermodel.XWPFTableCell;
import org.apache.poi.xwpf.usermodel.XWPFTableRow;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTBookmark;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTcBorders;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTText;
import org.pyt.common.common.Log;
import org.pyt.common.exceptions.ValidateValueException;

/**
 * Se encarga de realizar y obtener y agregar la table dentro del los archivos
 * docx
 * 
 * @author Alejandro parra
 * @since 09-11-2018
 */
public abstract class TableInDocx extends BookmarkInDocx {
	protected List<TableBookmark> listBookmarks;
	private final Log logger = Log.Log(TableInDocx.class);
	/**
	 * Se encarga de buscar entre las tabals los bokkmarks y retorna la fila que
	 * contiene los bokkmarks buscados
	 * 
	 * @param nameBookmarks
	 *            {@link String} to arrays
	 * @return {@link XWPFTableRow}
	 */
	protected final XWPFTableRow searchTabla(String... nameBookmarks) {
		List<XWPFTable> listT = docx.getTables();// obiene todas las tablas en el documento
		for (XWPFTable t : listT) {
			List<XWPFTableRow> listRows = t.getRows();// obtienes todas lass filas por cada documento
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
	protected final CTText getBookmark(XWPFTableRow row, String nameBookmark) {
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
	protected final Boolean verifyBookmark(XWPFTableRow row, String... nameColumns) {
		List<XWPFTableCell> listCells = row.getTableCells();// obtiene todass las celdas por cada registro
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

	protected final void cells(XWPFTableRow row, List<XWPFTableCell> celdas, Bookmark nameBookmarks, Integer position) {
		try {
			String[] keys = nameBookmarks.getBookmarks();
			XWPFTableRow rowUsar = null;
			for (int pos = 0; pos < celdas.size(); pos++) {
				XWPFTableCell cell = celdas.get(pos);
				for (XWPFParagraph p : cell.getParagraphs()) {
					for (CTBookmark b : p.getCTP().getBookmarkStartList()) {
						for (String key : keys) {
							if (b.getName().contains(key)) {
								rowUsar = fill(key, row, rowUsar, cell, p, nameBookmarks, pos, position);
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

	/**
	 * Se ecnarga de llenar el documento
	 */
	private final XWPFTableRow fill(String key, XWPFTableRow row, XWPFTableRow rowUsar, XWPFTableCell cell,
			XWPFParagraph paragraph, Bookmark nameBookmarks, Integer posCell, Integer position)
			throws ValidateValueException {
		rowUsar = getRowUsar(row,rowUsar,position);
		XWPFTableCell cellOut = getCell(rowUsar,cell,posCell);
		XWPFParagraph para = getParagraph(paragraph,cellOut);
		XWPFRun run = getRun(paragraph,para);
		run.setText(valid.cast(nameBookmarks.getValue(key), String.class), 0);
		return rowUsar;
	}

	/**
	 * Se encarga de verificar si la fila row usar no se encuentra vacia la retorna
	 * para el uso de lo contrario crea nueva fila en la posicion indicada y similar
	 * a la fila de origen
	 * 
	 * @param row {@link XWPFTableRow} fila original con los marcadores originales
	 * @param rowUsar {@link XWPFTableRow} fila que se encuentra usando 
	 * @param position {@link Integer} Posicion
	 * @return {@link XWPFTableRow}
	 */
	private final XWPFTableRow getRowUsar(XWPFTableRow row, XWPFTableRow rowUsar, Integer position) {
		// obteniedo o agregando nueva fila a usar
		if (rowUsar == null) {
			if (position == 1) {
				rowUsar = row;
			} else {
				rowUsar = row.getTable().insertNewTableRow(position);
			}
		}
		return rowUsar;
	}
	/**
	 * Obtiene una nueva celda para la fila a usar y aplicar estilos a la celda apartir de los estilos de otra celda
	 * @param row {@link XWPFTableRow} fila de la tabla a usar
	 * @param cell {@link XWPFTableCell} celda original de donde se copia el estilo
	 * @param position {@link Integer} Numero de la celda para usar
	 * @return  {@link XWPFTableCell} Celda a usar con estilos aplicados.
	 */
	private final XWPFTableCell getCell(XWPFTableRow row,XWPFTableCell cell,Integer position) {
		XWPFTableCell cellOut = row.getCell(position);
		// configurando estilo de celda o agreando nueva celda
		if (cellOut == null) {
			cellOut = row.addNewTableCell();
			putStyleCell(cellOut, cell);
		} else {
			putStyleCell(cellOut, cell);
		}
		return cellOut;
	}
	/**
	 * Se encarga de obtener el paragraph dentro de la cedla  si no existe crea uno, le aplica el estilo del paragraph origen de la celda
	 * @param paragraph {@link XWPFParagraph} paragraph de la celda original en la que se esta usando
	 * @param cell {@link XWPFTableCell} celda de la fila en la cual se encuentra usando
	 * @return {@link XWPFParagraph} Paragraph de la celda usado
	 */
	private final XWPFParagraph getParagraph(XWPFParagraph paragraph,XWPFTableCell cell) {
		XWPFParagraph para = null;
		if (cell.getParagraphs().size() > 0) {
			para = cell.getParagraph(paragraph.getCTP());
			if(para == null) {
				para = cell.getParagraphs().get(0);
				if(para != null) {
					putStyleParagraph(para, paragraph);
				}
			}
			if (para == null) {
				para = cell.addParagraph();
				putStyleParagraph(para, paragraph);
			}
		} else {
			para = cell.addParagraph();
			putStyleParagraph(para, paragraph);
		}
		return para;
	}
	/**
	 * Se encarga de obtener el run dentro del paragraph y aplicar el estilo necesario
	 * @param paragraph {@link XWPFParagraph} paragrph original para obtener el run y estilo del mismo
	 * @param para {@link XWPFParagraph} paragrpah del cual se usa para crear o obtener el run.
	 * @return {@link XWPFRun} run para ser usado y aplicar el valor
	 */
	private final XWPFRun getRun(XWPFParagraph paragraph,XWPFParagraph para) {
		XWPFRun run = null;
		if (para.getRuns().size() > 0) {
			run = para.getRuns().get(0);
			putStyleRun(run, paragraph.getRuns().get(0));
		} else {
			run = para.createRun();
			if (paragraph.getRuns().size() > 0) {
				putStyleRun(run, paragraph.getRuns().get(0));
			}
		}
		return run;
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
	protected final void tables(List<TableBookmark> listMaps) throws Exception {
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

	/**
	 * Se encarga de buscar dentro de las tablas un bookname en especifico
	 * 
	 * @param list
	 *            List of XWPFTable
	 * @param nameBookmark
	 *            String
	 * @return
	 */
	protected final XWPFParagraph searchBookmarkT(List<XWPFTable> list, String nameBookmark) {
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

	/**
	 * Se encarga de poner el estlo en la nuneva celda de la celda original
	 * 
	 * @param cell
	 * @param cellLast
	 */
	protected final void putStyleCell(XWPFTableCell cell, XWPFTableCell cellLast) {
		if (StringUtils.isNotBlank(cellLast.getColor())) {
			cell.setColor(cellLast.getColor());
		}
		CTTcBorders borde = cellLast.getCTTc().getTcPr().getTcBorders();
		cell.getCTTc().getTcPr().setTcBorders(borde);
		
	}
	
	
}
