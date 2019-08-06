package org.pyt.app.load;

import co.com.japl.ea.beans.LoadAppFxml;
import javafx.application.Application;
import javafx.stage.Stage;

public class App extends Application {

	@Override
	public void start(Stage primaryStage) throws Exception {
		LoadAppFxml.loadFxml(primaryStage, Template.class);
	}

}
