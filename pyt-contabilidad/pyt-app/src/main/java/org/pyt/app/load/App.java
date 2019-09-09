package org.pyt.app.load;

import org.pyt.app.beans.users.LoginBean;

import co.com.japl.ea.beans.LoadAppFxml;
import javafx.application.Application;
import javafx.stage.Stage;

public class App extends Application {

	@Override
	public void start(Stage primaryStage) throws Exception {
		var stage = new Stage();
		LoadAppFxml.loadFxml(stage, LoginBean.class);
		stage.setAlwaysOnTop(true);
		stage.show();
		stage.setWidth(400);
		stage.setHeight(400);
		stage.centerOnScreen();
		stage.setResizable(false);
		stage.setMaximized(false);
	}

}
