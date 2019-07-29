package org.ea.app.load;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;

import org.pyt.app.load.App;

import javafx.application.Application;

public class Main {
	private final static String CONST_JAVAFX_PRELOADER = "javafx.preloader";
	private final static String CONST_ENV_CONSOLE_PRINT = "ConsolePrint";
	private final static String CONST_FORMAT_DATE = "yyyy-MM-dd HH:mm:ss.SSSS";
	private final static String CONST_PATH_NAME_ERROR = "./error_load_ea.log";
	private final static String CONST_FORMAT_ERROR = "%s::%s.%s.%s(%s)\n\r";
	private final static String CONST_FORMAT_MESSAGE_ERROR = "%s::%s";

	public static void main(String[] args) {
		try {
			System.setProperty(CONST_JAVAFX_PRELOADER, Splash.class.getName());
			Application.launch(App.class, args);
		} catch (Exception e) {
			error(e);
			System.exit(1);
		}
	}

	private static String getDateTimesNow() {
		LocalDateTime ldt = LocalDateTime.now();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern(CONST_FORMAT_DATE);
		return ldt.format(formatter);
	}

	private static void error(Throwable e) {
		try {
			if (e != null) {
				var timer = getDateTimesNow();
				print(String.format(CONST_FORMAT_MESSAGE_ERROR, timer, e.getMessage()));
				Arrays.asList(e.getStackTrace())
						.forEach(trace -> print(String.format(CONST_FORMAT_ERROR, timer, trace.getClass(),
								trace.getFileName(), trace.getMethodName(), String.valueOf(trace.getLineNumber()))));
				error(e.getCause());
			}
		} catch (Exception e1) {
			System.exit(1);
		}
	}

	private static void print(String message) {
		try {
			var cp = System.getenv(CONST_ENV_CONSOLE_PRINT);
			if (cp != null) {
				if (Boolean.valueOf(cp)) {
					System.err.println(message);
				}
			}
			var file = new File(CONST_PATH_NAME_ERROR);
			if (!file.exists()) {
				file.createNewFile();
				file = new File(CONST_PATH_NAME_ERROR);
			}
			var fw = new FileWriter(file, true);
			var bw = new BufferedWriter(fw);
			bw.write(message);
			bw.close();
		} catch (Exception e) {
			System.err.println(e);
		}
	}
}
