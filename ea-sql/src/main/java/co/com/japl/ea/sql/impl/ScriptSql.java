package co.com.japl.ea.sql.impl;

import java.io.File;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import co.com.japl.ea.sql.privates.Constants;

/**
 * Esta clase se encarga de obtener todos los arhcivos sql que se encuentran en
 * la base de datos ordenados para poder ejecutar
 * 
 * @author Alejandro Parra
 * @since 07/06/2019
 */
public class ScriptSql {
	private Map<Integer, Map<String, File>> mapa;
	private static ScriptSql scripts;

	private ScriptSql() {
	}

	public final static ScriptSql getInstance() {
		if (scripts == null) {
			scripts = new ScriptSql();
		}
		return scripts;
	}

	public Map<Integer, Map<String, File>> analizer() throws URISyntaxException {
		if (mapa == null) {
			mapa = generateMap();
		}
		return mapa;
	}

	private final Map<Integer, Map<String, File>> generateMap() throws URISyntaxException {
		var map = new HashMap<Integer, Map<String, File>>();
		var in = getClass().getProtectionDomain().getCodeSource().getLocation().getPath() + Constants.CONST_PATH;
		var f = new File(in);
		if (f.exists() && f.isDirectory()) {
			var f2 = new File(in + Constants.CONST_FOLDER_TABLES);
			if (f2.exists() && f2.isDirectory()) {
				Arrays.stream(f2.listFiles()).forEach(file -> {
					var name = file.getName();
					assignToMap(map, getNumber(name), getName(name), file);
				});
			}
		}
		return map;
	}

	private final String getName(String name) {
		var nameUse = name.replace(getNumber(name) + Constants.CONST_SPLIT_GUION, Constants.CONST_EMPTY_STRING);
		var split = nameUse.split(Constants.CONST_SPLIT_NAME);
		for (int i = 0; i < split.length; i++) {
			split[i] = split[i].substring(0, 1).toUpperCase() + split[i].substring(1);
		}
		var nameJoin = String.join(Constants.CONST_EMPTY_STRING, split).replace(Constants.CONST_SUBFIX_NAME,
				Constants.CONST_EMPTY_STRING);
		return nameJoin;
	}

	private final Integer getNumber(String name) {
		return Integer.valueOf(name.substring(0,name.indexOf(Constants.CONST_SPLIT_GUION)));
	}

	private final void assignToMap(Map<Integer, Map<String, File>> map, Integer number, String name, File file) {
		Map<String, File> subMap = null;
		if (map.containsKey(number)) {
			subMap = map.get(number);
		}
		if (subMap == null) {
			subMap = new HashMap<String, File>();
		}
		if (subMap.containsKey(name)) {
			System.err.println("Se encontro el mismo archivo " + name);
		}
		subMap.put(name, file);
		if (!map.containsKey(number)) {
			map.put(number, subMap);
		}
	}
}
