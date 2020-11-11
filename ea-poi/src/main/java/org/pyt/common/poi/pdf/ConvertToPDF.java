package org.pyt.common.poi.pdf;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.poi.xwpf.usermodel.XWPFDocument;

import fr.opensagres.poi.xwpf.converter.pdf.PdfConverter;
import fr.opensagres.poi.xwpf.converter.pdf.PdfOptions;

/**
 * Esta clase realza la conversion de archivos a pdf tipo de archivos POI
 * 
 * @author Alejandro Parra
 * @since 06-11-2018
 */
public final class ConvertToPDF {
	private String fileOut;
	private String fileInput;
	private final static String TYPE_DOCX = "docx";
	private final static String TYPE_PDF = "pdf";
	
	
	/**
	 * Se encarga de convertir un documento de word docx a un archivo de pdf
	 * @throws {@link IOException}
	 */
	public final void DocxToPDF() throws IOException {
		InputStream doc = new FileInputStream(new File(fileInput));
		XWPFDocument document = new XWPFDocument(doc);
		PdfOptions options = PdfOptions.create();
		OutputStream out = new FileOutputStream(new File(fileOut.replace(TYPE_DOCX, TYPE_PDF)));
		try {
			PdfConverter.getInstance().convert(document, out, options);
		} catch (Exception e) {
			throw e;
		} finally {
			doc.close();
			document.close();
			out.close();
		}
	}
	
	public final void setFileNameOutput(String fileOutput) {
		fileOut = fileOutput;
	}
	
	public final void setFileNameInput(String fileInput) {
		this.fileInput = fileInput;
	}
	
	public final String getFilePdfOutput() {
		return fileOut.replace(TYPE_DOCX, TYPE_PDF);
	}
}
