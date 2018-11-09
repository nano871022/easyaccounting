package org.pyt.common.poi.docs;

import java.util.HashMap;
import java.util.Map;

/**
 * Se encarga para generalizar el uso de los bookmarks
 * 
 * @author Alejandro Parra
 * @since 01-08-2018
 */
public class Bookmark {
	private Map<String, Object> mapa;
	/**
	 * Se agrga bookmark y valor uno a uno
	 * @param bookmark {@link String}
	 * @param value {@link Object}
	 */
	public final <T extends Object> Bookmark add(String bookmark, T value) {
		if (mapa == null) {
			mapa = new HashMap<String, Object>();
		}
		mapa.put(bookmark, value);
		return this;
	}
	/**
	 * Se agregarn valor mapas al tiempo
	 * @param mapa {@link Map} {@link String} {@link Object}
	 */
	public final void add(Map<String, Object> mapa) {
		if (mapa == null) {
			mapa = new HashMap<String, Object>();
		}
		this.mapa.putAll(mapa);
	}

	/**
	 * Se encarga de obtner el valor asociado al book mark
	 * 
	 * @param bookmark
	 *            {@link String}
	 * @return {@link Object}
	 */
	@SuppressWarnings("unchecked")
	public final <T extends Object> T getValue(String bookmark) {
		if (bookmark == null)
			return null;
		if (mapa == null || mapa.size() == 0)
			return null;
		return (T) mapa.get(bookmark);
	}

	/**
	 * Se encarga de retornar los bookmarks dentro del mapa
	 * 
	 * @return
	 */
	public final String[] getBookmarks() {
		return mapa.keySet().toArray(new String[mapa.size()]);
	}

	public final Map<String, Object> getMapa() {
		return mapa;
	}

	public final static Bookmark instance() {
		return new Bookmark();
	}
}
