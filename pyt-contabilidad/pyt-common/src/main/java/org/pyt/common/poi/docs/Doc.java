package org.pyt.common.poi.docs;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFTable;
import org.apache.poi.xwpf.usermodel.XWPFTableCell;
import org.apache.poi.xwpf.usermodel.XWPFTableRow;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTBookmark;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTText;
import org.pyt.common.common.Log;

public final class Doc {
	private String file = "./docs/text.docx";
	private String fileOut = "./docs/text2.docx";
	private String separator = System.getProperty("file.separator");

	public final void get() {
		try {
			if (validFile(file)) {
				XWPFDocument doc = new XWPFDocument(new FileInputStream(file));
				List<XWPFParagraph> list = doc.getParagraphs();
				for (XWPFParagraph p : list) {
					List<CTBookmark> lists = p.getCTP().getBookmarkStartList();
					for (CTBookmark bookMark : lists) {
						System.out.println(bookMark.getName());
						bookMark.getId();
						if (bookMark.getName().contains("Prueba")) {
							CTText t = p.getCTP().addNewR().addNewT();
							t.setStringValue("New value");
						}
					}
				}
				List<XWPFTable> listT = doc.getTables();
				XWPFTable addRow = null;
				XWPFTableRow newRow = null;
				for(XWPFTable t : listT) {
					List<XWPFTableRow> listRows = t.getRows();
					for(XWPFTableRow row : listRows) {
						List<XWPFTableCell> listCells = row.getTableCells();
						for(XWPFTableCell cell : listCells) {
							List<XWPFParagraph> listPara = cell.getParagraphs();
							for(XWPFParagraph para : listPara) {
								List<CTBookmark> listBookmark = para.getCTP().getBookmarkStartList();
								for(CTBookmark book : listBookmark) {
									if(book.getName().contains("idTabla")) {
										addRow = t;
										newRow = row;
										CTText tt = para.getCTP().addNewR().addNewT();
										tt.setStringValue("10");
										
									}else if(book.getName().contains("nombreTabla")) {
										CTText tt = para.getCTP().addNewR().addNewT();
										tt.setStringValue("nombre del registro");
									}else if(book.getName().contains("valorTabla")) {
										CTText tt = para.getCTP().addNewR().addNewT();
										tt.setStringValue("valor del registro 1");
									}
								}
							}
						}
					}
				}
				
				addRow.addRow(newRow,2);
				if(validFile(fileOut)) {
					FileOutputStream fo = new FileOutputStream(new File(fileOut));
					doc.write(fo);
				}
			} else {
				Log.error("Se presento error en la validacion del archivo.");
			}
		} catch (IOException e) {
			Log.logger(e);
		}
	}

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

	public final static void main(String... strings) {
		new Doc().get();
	}

}
