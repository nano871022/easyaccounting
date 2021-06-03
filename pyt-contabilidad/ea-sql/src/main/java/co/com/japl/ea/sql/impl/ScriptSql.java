package co.com.japl.ea.sql.impl;

import java.io.File;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.pyt.common.constants.AppConstants;

import co.com.japl.ea.common.binario.ReadBin;
import co.com.japl.ea.common.binario.WriteBin;
import co.com.japl.ea.exceptions.FileBinException;
import co.com.japl.ea.sql.privates.Constants;

/**
 * Esta clase se encarga de obtener todos los arhcivos sql que se encuentran en
 * la base de datos ordenados para poder ejecutar
 * 
 * @author Alejandro Parra
 * @since 07/06/2019
 */
public class ScriptSql {
	private Map<Integer, Map<String, File>> mapaTables;
	private Map<String, File> mapaInserts;
	private Map<String, Map<String, File>> mapaChangedVersions;
	private static ScriptSql scripts;
	private ReadBin readBin;
	private final static String CONST_FILE_VERSION_RUNNER = "VersionRunner.bin";
	private List<String> versions;
	private Boolean loadVersion;

	private ScriptSql() {
		readBin = new ReadBin();
		loadVersion = true;
		try {
			versions = readBin.read(CONST_FILE_VERSION_RUNNER);
			if (versions != null) {
				loadVersion = !versions.contains(AppConstants.VERSION_DATABASE);
			}
		} catch (FileBinException e) {
			System.err.println(e);
		}
	}

	public final static ScriptSql getInstance() {
		if (scripts == null) {
			scripts = new ScriptSql();
		}
		return scripts;
	}

	public Map<String, Map<String, File>> getChangedVersions() {
		if (mapaChangedVersions == null || getHotReload()) {
			mapaChangedVersions = generateChangedVersions();
		}
		return mapaChangedVersions;
	}

	public Map<Integer, Map<String, File>> getTables() throws URISyntaxException {
		if (mapaTables == null || getHotReload()) {
			mapaTables = generateTables();
		}
		return mapaTables;
	}

	public final Map<String, File> getInserts() {
		if (mapaInserts == null || getHotReload()) {
			mapaInserts = generateInserts();
		}
		return mapaInserts;
	}

	private final Map<String, File> generateInserts() {
		var mapa = new HashMap<String, File>();
		var packages = getClass().getProtectionDomain().getCodeSource().getLocation().getPath() + Constants.CONST_PATH;
		var file = new File(packages);
		if (file.exists() && file.isDirectory()) {
			var inserts = new File(packages + Constants.CONST_FOLDER_INSERTS);
			if (inserts.exists() && inserts.isDirectory()) {
				Arrays.stream(inserts.listFiles()).forEach(insert -> {
					var name = insert.getName();
					mapa.put(getNameSimple(name), insert);
				});
			}
		}
		return mapa;
	}

	private final Map<String, Map<String, File>> generateChangedVersions() {
		var mapa = new HashMap<String, Map<String, File>>();
		var packages = getClass().getProtectionDomain().getCodeSource().getLocation().getPath()
				+ Constants.CONST_PATH_CHANGED;
		var foldersVersion = getFoldersVersions(packages);
		for (String version : foldersVersion) {
			var folderVersion = new File(packages + version);
			if (folderVersion.exists() && folderVersion.isDirectory()) {
				Arrays.stream(folderVersion.listFiles()).forEach(file -> {
					var name = file.getName();
					var map = mapa.get(version);
					if (map == null) {
						map = new HashMap<String, File>();
					}

					map.put(getNameSimple(name), file);
					mapa.put(version, map);
				});
				try {
					if (versions == null) {
						versions = new ArrayList<String>();
					}
					versions.add(version);
					new WriteBin().write(CONST_FILE_VERSION_RUNNER, versions);
				} catch (FileBinException e) {
					e.printStackTrace();
				}
			}
		}
		return mapa;
	}

	private final String[] getFoldersVersions(String packages) {
		var list = new ArrayList<String>();

		var folder = new File(packages);
		if (folder.exists() && folder.isDirectory()) {
			Arrays.stream(folder.listFiles()).filter(file -> validVersion(file))
					.forEach(file -> list.add(file.getName()));
		}
		return list.toArray(new String[list.size()]);
	}

	private Boolean validVersion(File file) {
		return file.exists() && file.isDirectory() && AppConstants.VERSION_DATABASE.compareTo(file.getName()) == 0
				&& loadVersion;
	}

	private final Map<Integer, Map<String, File>> generateTables() throws URISyntaxException {
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

	private final String getNameSimple(String name) {
		var nameUse = name;
		var split = nameUse.split(Constants.CONST_SPLIT_NAME);
		for (int i = 0; i < split.length; i++) {
			split[i] = split[i].substring(0, 1).toUpperCase() + split[i].substring(1);
		}
		var nameJoin = String.join(Constants.CONST_EMPTY_STRING, split).replace(Constants.CONST_SUBFIX_NAME,
				Constants.CONST_EMPTY_STRING);
		return nameJoin;
	}

	private final Integer getNumber(String name) {
		return Integer.valueOf(name.substring(0, name.indexOf(Constants.CONST_SPLIT_GUION)));
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

	private final Boolean getHotReload() {
		return false;
	}
}
