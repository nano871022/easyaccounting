package org.pyt.common.poi.docs;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTBookmark;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTText;
import org.pyt.common.common.Log;

/**
 * Se encarga de buscar y usar los bookmar de los archivos docx
 * 
 * @author Alejandro Parra
 * @since 07-11-2018
 */
public abstract class BookmarkInDocx extends Poi {
	protected XWPFDocument docx;
	private final Log logger = Log.Log(this.getClass());

	/**
	 * Se encarag de obtener el marcador y se retorna el objeto con el cual se
	 * realiza el cambio del valor del mismo
	 * 
	 * @param nameBookmark
	 *            {@link String}
	 * @return {@link CTText}
	 */
	protected final CTText searchBookmark(List<XWPFParagraph> list, String nameBookmark) {
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
	protected final XWPFParagraph searchBookmarkP(List<XWPFParagraph> list, String nameBookmark) {
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
	protected final <T extends Object> void replaceBookmark(XWPFParagraph parrafo, String nameBookmark, T value)
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

	protected final void putStyleRun(XWPFRun run, XWPFRun runLast) {
		if (StringUtils.isNotBlank(runLast.getColor()))
			run.setColor(runLast.getColor());
		if (StringUtils.isNotBlank(runLast.getFontFamily()))
			run.setFontFamily(runLast.getFontFamily());
		run.setCapitalized(runLast.isCapitalized());
		run.setBold(runLast.isBold());
		run.setUnderline(runLast.getUnderline());
	}

	protected final void putStyleParagraph(XWPFParagraph paragraph, XWPFParagraph paragraphLast) {
		paragraph.setAlignment(paragraphLast.getAlignment());
		paragraph.setBorderBetween(paragraphLast.getBorderBetween());
		paragraph.setBorderBottom(paragraphLast.getBorderBottom());
		paragraph.setBorderLeft(paragraphLast.getBorderLeft());
		paragraph.setBorderRight(paragraphLast.getBorderRight());
		paragraph.setBorderTop(paragraphLast.getBorderTop());
		paragraph.setFontAlignment(paragraphLast.getFontAlignment());
		if (StringUtils.isNotBlank(paragraphLast.getStyle()))
			paragraph.setStyle(paragraphLast.getStyle());
		paragraph.setVerticalAlignment(paragraphLast.getVerticalAlignment());
		try {
			paragraph.setWordWrapped(paragraphLast.isWordWrapped());
		} catch (IndexOutOfBoundsException e) {
		}
	}
}
