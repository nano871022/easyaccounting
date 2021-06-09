package org.pyt.common.common;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

/**
 * Esta clase contiene validaciones sobre las listas para no ser implementados
 * los fragmentos de codigo en todas las clases La clase no se puede instanciar
 * 
 * @author Alejandro Parra
 * @since 15-02-2020
 */
public class ListUtils {

	private ListUtils() {
	}

	/**
	 * Verifíca que todas las lista esten vacías
	 * 
	 * @param lists {@link List}
	 * @return {@link Boolean}
	 */
	@SuppressWarnings({ "rawtypes" })
	public static Boolean isBlank(List... lists) {
		if (lists.length == 0) {
			return true;
		}
		return !isNotBlank(lists);
	}

	/**
	 * Verifíca si la lista esta nula o es una instancia sin registros
	 * 
	 * @param <O>  {@link Object}
	 * @param list {@link List}
	 * @return {@link Boolean}
	 */
	public static <O> Boolean isBlank(List<O> list) {
		return list == null || list.size() == 0;
	}

	public static <O> Boolean isBlank(Collection<O> list) {
		return list == null || list.size() == 0;
	}

	/**
	 * Se encarga de validar que todas las listas suministradas contengan registros
	 * 
	 * @param lists {@link Array} {@link List}
	 * @return {@link Boolean}
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static Boolean isNotBlank(List... lists) {
		if (lists.length == 0) {
			return false;
		}
		var valid = true;
		for (var list : lists) {
			valid &= isNotBlank(list);
		}
		return valid;
	}

	public static <T> Boolean isNotBlank(T[] lists) {
		if (lists == null || lists.length == 0) {
			return false;
		}
		return Arrays.asList(lists).stream().allMatch(value->value != null);
	}

	
	/**
	 * Verifíca si la lista no se encuentra vacía
	 * 
	 * @param <O>  {@link Object}
	 * @param list {@link List}
	 * @return {@link Boolean}
	 */
	public static <O> Boolean isNotBlank(List<O> list) {
		return list != null && list.size() > 0;
	}

	@SuppressWarnings("null")
	public static <O> Boolean isNotBlank(Collection<O> list) {
		return list != null && list.size() > 0;
	}

	/**
	 * Verifíca si la lista solo contiene un item.
	 * 
	 * @param <O>  {@link Object}
	 * @param list {@link List}
	 * @return {@link Boolean}
	 */
	public static <O> Boolean haveOneItem(List<O> list) {
		return list != null && list.size() == 1;
	}


	/**
	 * Verifíca si la lista contiene mas de un item
	 * 
	 * @param <O>  {@link Object}
	 * @param list {@link List}
	 * @return {@link Boolean}
	 */
	public static <O> Boolean haveMoreOneItem(List<O> list) {
		return list != null && list.size() > 1;
	}

	/**
	 * Verifíca si la lista tiene cierta cantidad de items entre un rango posible
	 * 
	 * @param <O>  {@link Object}
	 * @param list {@link List}
	 * @param min  {@link Integer}
	 * @param max  {@link Integer}
	 * @return {@link Boolean}
	 */
	public static <O> Boolean haveItemsBetween(List<O> list, Integer min, Integer max) {
		return list != null && list.size() >= min && list.size() <= max;
	}

	/**
	 * Verifíca si la lista tiene cierta cantidad de items.
	 * 
	 * @param <O>   {@link Object}
	 * @param list  {@link List}
	 * @param items {@link Integer}
	 * @return {@link Boolean}
	 */
	public static <O> Boolean haveItemsEqual(List<O> list, Integer items) {
		return list != null && list.size() == items;
	}

}
