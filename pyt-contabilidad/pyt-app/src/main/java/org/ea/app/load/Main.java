package org.ea.app.load;

import org.pyt.app.load.App;

import javafx.application.Application;

public class Main {
	private final static String CONST_JAVAFX_PRELOADER = "javafx.preloader";
	
	public static void main(String[] args) {
		System.setProperty(CONST_JAVAFX_PRELOADER,Splash.class.getName());
		Application.launch(App.class, args);
	}
}
