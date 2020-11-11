package org.pyt.common.poi.gen;

import java.io.File;

import org.apache.commons.lang3.StringUtils;
import org.pyt.common.common.Log;
import org.pyt.common.exceptions.POIException;
import org.pyt.common.poi.docs.Bookmark;
import org.pyt.common.poi.docs.DocX;
import org.pyt.common.poi.docs.TableBookmark;
import org.pyt.common.poi.pdf.ConvertToPDF;

/**
 * Se encarga de generar un archivo docx segun los bookmark de tablas como de
 * datos y convertirlo en un pdf de salida
 * 
 * @author Alejandro Parra
 * @since 06-11-2018
 */
public final class DocxToPDFGen {
	private Log logger = Log.Log(DocxToPDFGen.class);
	private DocX doc;
	private ConvertToPDF pdf;
	private String fileInput;
	private String fileOutput;
	private Boolean delFileLast;
	private String fileTemporal;

	public DocxToPDFGen() {
		doc = new DocX();
		pdf = new ConvertToPDF();
		delFileLast = false;
	}

	/**
	 * Se encarga de generar el el docx poner sus valores de los bookmark y ademas
	 * se creara el pdf
	 * 
	 * @return {@link String} ruta y nombre del archivo de salida.
	 */
	public final synchronized String generate() throws POIException {
		if (StringUtils.isBlank(fileOutput))
			throw new POIException("No se a ingresado el nombre del archivo de salida.");
		if (StringUtils.isBlank(fileTemporal))
			throw new POIException("No se a ingresado el nombre del archivo temporal.");
		if (StringUtils.isBlank(fileInput))
			throw new POIException("No se a ingresado el nombre de la platilla para generar.");
		if (!(new File(fileInput).exists())) {
			throw new POIException("La ruta o nombre del template suministrado para generar no existe.");
		}
		try {
			doc.generar();
			pdf.DocxToPDF();
			String pdfOutput = pdf.getFilePdfOutput();
			logger.info("PDF generado " + pdfOutput);
			if (delFileLast) {
				File file = new File(fileOutput);
				if (file.exists())
					file.delete();
				else
					logger.error("No se logro eliminar el archivo " + fileOutput);
			}
			return pdfOutput;
		} catch (Exception e) {
			throw new POIException(e);
		}
	}

	public final void setFileInput(String fileInput) {
		this.fileInput = fileInput;
		doc.setFile(fileInput);
	}

	public final void setFileOutput(String fileOutput) {
		this.fileOutput = fileOutput;
//		doc.setFileOut(fileOutput);
		pdf.setFileNameOutput(fileOutput);
	}
	
	public final void setFileTemporal(String fileTemporal) {
		this.fileTemporal = fileTemporal;
		doc.setFileOut(fileTemporal);
		pdf.setFileNameInput(fileTemporal);
//		pdf.setFileNameOutput(fileOutput);
	}

	public final void addTableBookmark(TableBookmark tableBookmarks) {
		doc.addTableBookmark(tableBookmarks);
	}

	public final void setBookmarks(Bookmark bookmarks) {
		doc.setBookmarks(bookmarks);
	}

	public final void setDelFileLast(Boolean del) {
		delFileLast = del;
	}

	public final static void main(String... strings) {
		Bookmark bookmark = Bookmark.instance().add("idTabla", 10).add("nombreTabla", "nombre del registro")
				.add("valorTabla", "valor del registro");
		Bookmark bookmark2 = Bookmark.instance().add("idTabla", 11).add("nombreTabla", "nombre del registro 2")
				.add("valorTabla", "valor del registro 2");
		Bookmark bookmark3 = Bookmark.instance().add("idTabla", 12).add("nombreTabla", "nombre del registro 3")
				.add("valorTabla", "valor del registro 3");
		Bookmark bookmark4 = Bookmark.instance().add("idTabla", 13).add("nombreTabla", "nombre del registro 4")
				.add("valorTabla", "valor del registro 4");
		Bookmark bookmark5 = Bookmark.instance().add("cantidad", 2).add("descripcionTabla2", "descripcion tabla 2")
				.add("valorTabla2", 1000).add("subTotalTabla", 2000);
		Bookmark bookmark6 = Bookmark.instance().add("cantidad", 3).add("descripcionTabla2", "descripcion tabla 3")
				.add("valorTabla2", 2000).add("subTotalTabla", 6000);
		TableBookmark tabla1 = TableBookmark.instance().add(bookmark).add(bookmark2).add(bookmark3).add(bookmark4);
		TableBookmark tabla2 = TableBookmark.instance();
		tabla2.add(bookmark5);
		tabla2.add(bookmark6);
		Bookmark bookmarks = Bookmark.instance().add("Prueba", " valor a cambiar ")
				.add("between", " valor entre textos ").add("remplazo", " valor de remplazo ").add("totalTabla1", 20000)
				.add("totalTabla", 100000);
		String nameFile = "";
		DocxToPDFGen dtpdfg = new DocxToPDFGen();
		dtpdfg.addTableBookmark(tabla1);
		// dtpdfg.addTableBookmark(tabla2);
		dtpdfg.setBookmarks(bookmarks);
		dtpdfg.setFileInput("./docs/text.docx");
		dtpdfg.setFileOutput("/home/alejo/Escritorio/reporte_test1.docx");
		try {
			nameFile = dtpdfg.generate();
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println(nameFile);
		System.exit(0);
	}

}
