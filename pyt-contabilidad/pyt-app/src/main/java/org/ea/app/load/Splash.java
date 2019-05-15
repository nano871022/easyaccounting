package org.ea.app.load;

import javafx.animation.FadeTransition;
import javafx.application.Preloader;
import javafx.application.Preloader.StateChangeNotification.Type;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
/**
 * Esta clase es la primera que se carga antes de iniciar el proceso de carga de la aplicación completa
 * @author Alejandro Parra
 * @since 15/05/2019
 */
public class Splash extends Preloader {
	private Stage stage;
	private Parent root;
	private final static String SPLASH_RESOURCE_FXML = "view/splash/splash.fxml";

	@Override
	public void start(Stage primaryStage) throws Exception {
		this.stage = primaryStage;
		root = FXMLLoader.load(getClass().getClassLoader().getResource(SPLASH_RESOURCE_FXML));
		var scene = new Scene(root);
		primaryStage.setScene(scene);
		primaryStage.setAlwaysOnTop(true);
		primaryStage.setResizable(false);
		primaryStage.initStyle(StageStyle.UNDECORATED);
		primaryStage.show();
	}

	@Override
	public void handleStateChangeNotification(StateChangeNotification stateChangeNotification) {
		if (stateChangeNotification.getType() == Type.BEFORE_LOAD) {
			// TODO analisis y verificacion de que la aplicacion este en buen estado para cargarse
		}
		if (stateChangeNotification.getType() == Type.BEFORE_START) {
			var ft = new FadeTransition(Duration.seconds(1), root);
			ft.setFromValue(1);
			ft.setToValue(0);
			ft.setCycleCount(1);
			ft.setOnFinished(e->stage.hide());
			ft.play();
		}
	}
}
