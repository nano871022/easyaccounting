package org.pyt.common.poi.docs;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFTable;
import org.apache.poi.xwpf.usermodel.XWPFTableCell;
import org.apache.poi.xwpf.usermodel.XWPFTableRow;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTBookmark;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTText;
import org.pyt.common.common.Log;
import org.pyt.common.common.ValidateValues;
import org.pyt.common.exceptions.ValidateValueException;
import org.pyt.common.reflection.ReflectionUtils;

/**
 * Se ecarga de llenar los marcadores dentro de un archivo de word docx
 * 
 * @author Ing-0-0013
 *
 */
public final class DocX {
	private String file = "./docs/text.docx";
	private String fileOut = "./docs/text2.docx";
	private String separator = System.getProperty("file.separator");
	private XWPFDocument docx;
	private ValidateValues valid;

	public DocX() {
		valid = new ValidateValues();
	}

	public final void get() {
		try {
			if (validFile(file)) {
				docx = new XWPFDocument(new FileInputStream(file));

				CTText bookmark = searchBookmark(docx.getParagraphs(), "prueba");
				changeMarcador(bookmark, ".Cambiando valor del marcador.");
				List<Map<String, Object>> listMarks = new ArrayList<Map<String, Object>>();
				Map<String, Object> maps = new HashMap<String, Object>();
				maps.put("idTabla", 10);
				maps.put("nombreTabla", "nombre del registro");
				maps.put("valorTabla", "valor del registro");
				listMarks.add(maps);
				tables(listMarks);
				addRow.addRow(newRow, 2);
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
	public final void tables(List<Map<String, Object>> listMaps) throws ValidateValueException {
		if (listMaps == null || listMaps.size() == 0)
			return;
		Map<String, Object> maps = listMaps.get(0);
		String[] marks = maps.keySet().stream().toArray(size -> new String[size]);
		XWPFTableRow row = searchTabla(marks);
		for (int i = 0; i < listMaps.size(); i++) {
			Map<String, Object> map = listMaps.get(i);
			if (i + 1 > 1) {
				ReflectionUtils.clone((T) row);
				row.getTable().addRow(row, i + 1);
			}
			if (row != null) {
				for (String nameBookmark : marks) {
					CTText book = getBookmark(row, nameBookmark);
					changeMarcador(book, maps.get(nameBookmark));
				}
			}
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
	 * Se crea usa para cambiar el marcador por un valor.
	 * 
	 * @param bookmark
	 *            {@link CTText}
	 * @param value
	 *            {@link Object}
	 * @throws {@link
	 *             ValidateValueException}
	 */
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
	 * Se encarga de validar el archivo si existe y si no crearlo
	 * 
	 * @param files
	 *            {@link String} ruta del archivo
	 * @return {@link Boolean}
	 */
	private final Boolean validFile(String files) {
		String nameFile = files;
		String[] split = null;
		if (files.contains("/") && !"/".equalsIgnoreCase(separator)) {
			nameFile = files.replace("/", separator);
		}
		if (new File(nameFile).exists()) {
			return true;
		}
		try {
			split = nameFile.split(separator);
		} catch (Exception e) {
			try {
				split = nameFile.split(separator + separator);
			} catch (Exception e1) {
				throw e1;
			}
		}
		if (split != null && split.length > 0) {
			StringBuilder paths = new StringBuilder();
			File file = null;
			try {
				for (String path : split) {
					if (paths.length() > 0) {
						paths.append(separator);
					}
					paths.append(path);
					if (path.equalsIgnoreCase(".")) {
						continue;
					}
					file = new File(paths.toString());
					if (!file.exists() && !path.contains(".")) {
						file.mkdirs();
					} else if (!file.exists() && path.contains(".")) {
						if (file.createNewFile()) {
							return true;
						}
					}
				}
			} catch (IOException e) {
				Log.logger(e);
			}
		}
		return false;
	}

	public final void setFile(String pathFile) {
		file = pathFile;
	}

	public final void setFileOut(String pathFile) {
		fileOut = pathFile;
	}

	public final static void main(String... strings) {
		new DocX().get();
	}

}
