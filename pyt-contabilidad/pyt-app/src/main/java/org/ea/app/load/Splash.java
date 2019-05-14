package org.ea.app.load;

import javafx.application.Preloader;
import javafx.application.Preloader.StateChangeNotification.Type;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;

public class Splash extends Preloader {
	private Stage stage;
	@Override
	public void start(Stage primaryStage) throws Exception {
		 this.stage = primaryStage;
	       Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("view/splash/splash.fxml"));
	       Scene scene = new Scene(root);
	       primaryStage.setScene(scene);
	       primaryStage.show();
	}
	@Override
	   public void handleStateChangeNotification(StateChangeNotification stateChangeNotification) {
	      if (stateChangeNotification.getType() == Type.BEFORE_START) {
	         stage.hide();
	      }
	   }
}
