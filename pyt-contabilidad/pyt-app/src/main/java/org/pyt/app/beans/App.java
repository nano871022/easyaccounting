package org.pyt.app.beans;

import org.pyt.common.constants.AppConstants;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class App extends Application {

	@Override
	public void start(Stage primaryStage) throws Exception {
		String file = new EmpresaBean().pathFileFxml();
		Parent root = FXMLLoader.load(getClass().getResource(file));
		Scene scene = new Scene(root);
		primaryStage.setTitle(AppConstants.TITTLE_APP);
		primaryStage.setScene(scene);
		primaryStage.show();
	}

	public final static void main(String... args) {
		App.launch(args);
	}

}
