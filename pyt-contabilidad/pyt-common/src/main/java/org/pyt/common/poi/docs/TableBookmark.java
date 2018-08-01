package org.pyt.common.poi.docs;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Se encarga de almacenar todos los bookmark con sus valores en lista que se
 * buscaran en las tablas.
 * 
 * @author Alejandro Parra
 * @since 01-08-2018
 */
public class TableBookmark {
	private List<Map<String, Object>> list;
	private String[] bookmarks;
	private Integer position;

	/**
	 * Agrega una nueva asociacion de bookmark y valor
	 * 
	 * @param mapa
	 *            {@link Map} {@link String} {@link Object}
	 */
	public final <T extends Object> void add(Map<String, Object> mapa) {
		if (list == null) {
			list = new ArrayList<Map<String, Object>>();
		}
		if (bookmarks == null) {
			Set<String> keys = mapa.keySet();
			bookmarks = keys.toArray(new String[keys.size()]);
		}
		list.add(mapa);
	}

	/**
	 * Se encarga de avanzar entre los registros y retorna un map del bookmark
	 * 
	 * @return {@link Map}
	 */
	public final Map<String, Object> next() {
		if (position == null) {
			position = 0;
		} else if (position + 1 < list.size()) {
			position += 1;
		} else {
			return null;
		}
		return list.get(position);
	}

	/**
	 * Se encarga de obtener el valor de un bookmar de la posicion actual
	 * 
	 * @param bookmark
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public final <T extends Object> T getBookmakValue(String bookmark) {
		if (position == null) {
			position = 0;
		}
		if (bookmark == null)
			return null;
		if (list == null)
			return null;
		if (list.size() >= position)
			return null;
		return (T) list.get(position).get(bookmark);
	}

	/**
	 * Verifica si el bookmark se encuentr ane la lista
	 * 
	 * @param bookmark
	 *            {@link String}
	 * @return {@link Boolean}
	 */
	public final Boolean hasBookmark(String bookmark) {
		if (bookmarks.length > 0) {
			for (String bookMark : bookmarks) {
				if (bookMark.contains(bookmark)) {
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * Se encargad e indicar si hay una siguiente registor
	 * 
	 * @return {@link Boolean}
	 */
	public final Boolean hasNext() {
		if(position == null  && list.size() > 0 )return true;
		return (position + 1 < list.size());
	}

	public final Integer getPosition() {
		return position;
	}

	public final Map<String, Object> getMap(Integer pos) {
		return list.get(pos);
	}

	public final String[] getBookmarks() {
		return bookmarks;
	}

	public final Integer getSize() {
		if (list == null)
			return 0;
		return list.size();
	}
}
