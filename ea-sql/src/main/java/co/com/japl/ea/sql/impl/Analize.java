package co.com.japl.ea.sql.impl;

import java.io.File;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import co.com.japl.ea.sql.privates.Constants;

public class Analize {

	public Map<String, File> analizer() throws URISyntaxException {
		var map = new HashMap<String, File>();
		var in = getClass().getProtectionDomain().getCodeSource().getLocation().getPath() + Constants.CONST_PATH;
		var f = new File(in);
		if (f.exists() && f.isDirectory()) {
			var f2 = new File(in + Constants.CONST_FOLDER_TABLES);
			if (f2.exists() && f2.isDirectory()) {
				Arrays.stream(f2.listFiles()).forEach(file -> {
					var name = file.getName();
					var split = name.split(Constants.CONST_SPLIT_NAME);
					for (int i = 0; i < split.length; i++) {
						split[i] = split[i].substring(0, 1).toUpperCase() + split[i].substring(1);
					}
					name = String.join(Constants.CONST_EMPTY_STRING, split).replace(Constants.CONST_SUBFIX_NAME, Constants.CONST_EMPTY_STRING);
					map.put(name, file);
				});
			}
		}
		return map;
	}
}
