package org.ea.app.load;

import org.pyt.app.load.App;

import javafx.application.Application;




public class Main {
	public static void main(String[] args) {
		System.setProperty("javafx.preloader",Splash.class.getName());
		Application.launch(App.class, args);
	}
}
