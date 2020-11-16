package org.pyt.app.load;

import org.pyt.app.beans.users.LoginBean;

import co.com.japl.ea.utls.LoadAppFxml;
import javafx.application.Application;
import javafx.stage.Stage;

public class App extends Application {

	@Override
	public void start(Stage primaryStage) throws Exception {
		loadBean();
	}

	public static void loadBean() throws Exception {
		var stage = new Stage();
		var loginBean = LoadAppFxml.loadFxml(stage, LoginBean.class);
		stage.setAlwaysOnTop(true);
		stage.show();
		stage.setWidth(300);
		stage.setHeight(280);
		stage.centerOnScreen();
		stage.setResizable(false);
		stage.setMaximized(false);
		loginBean.load(stage);
	}

}
