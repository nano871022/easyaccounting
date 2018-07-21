package org.pyt.app.load;

import org.pyt.common.common.LoadAppFxml;

import javafx.application.Application;
import javafx.stage.Stage;

public class App extends Application {

	@Override
	public void start(Stage primaryStage) throws Exception {
		LoadAppFxml.loadFxml(primaryStage, Template.class);
	}

	public final static void main(String... args) {
		App.launch(args);
	}

}
