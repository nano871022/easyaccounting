package org.ea.app.load;

import java.lang.reflect.InvocationTargetException;

import com.pyt.query.interfaces.IVerifyStructuredDB;

import javafx.animation.FadeTransition;
import javafx.application.Preloader;
import javafx.application.Preloader.StateChangeNotification.Type;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

/**
 * Esta clase es la primera que se carga antes de iniciar el proceso de carga de
 * la aplicación completa
 * 
 * @author Alejandro Parra
 * @since 15/05/2019
 */
public class Splash extends Preloader {
	private Stage stage;
	private Parent root;
	private FXMLLoader fxml;
	private final static String SPLASH_RESOURCE_FXML = "view/splash/splash.fxml";

	@Override
	public void start(Stage primaryStage) throws Exception {
		this.stage = primaryStage;
		try {
			fxml = new FXMLLoader(getClass().getClassLoader().getResource(SPLASH_RESOURCE_FXML));
			root = fxml.load();
			var scene = new Scene(root);
			primaryStage.setScene(scene);
		} catch (Throwable e) {
			System.err.println("Error al cargar el splash.");
			e.printStackTrace();
		}
		primaryStage.setAlwaysOnTop(true);
		primaryStage.setResizable(false);
		primaryStage.initStyle(StageStyle.UNDECORATED);
		primaryStage.show();
	}

	@Override
	public void handleStateChangeNotification(StateChangeNotification stateChangeNotification) {
		if (stateChangeNotification.getType() == Type.BEFORE_LOAD) {
			try {
				validStructureDB();
			} catch (ClassNotFoundException | InstantiationException | IllegalAccessException | IllegalArgumentException
					| InvocationTargetException | NoSuchMethodException | SecurityException e) {
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		if (stateChangeNotification.getType() == Type.BEFORE_START) {
			var ft = new FadeTransition(Duration.seconds(1), root);
			ft.setFromValue(1);
			ft.setToValue(0);
			ft.setCycleCount(1);
			ft.setOnFinished(e -> stage.hide());
			ft.play();
		}
	}

	public void validStructureDB() throws Exception {
		try {

			var analize = Class.forName("co.com.japl.ea.gdb.privates.impls.VerifyDB");
			var instance = analize.getConstructor().newInstance();
			if (instance instanceof IVerifyStructuredDB) {
				IVerifyStructuredDB verifyDB = (IVerifyStructuredDB) instance;
				var controller = fxml.getController();
				if (controller != null) {
					((ControllerSplash) controller).setVerifySctructureDB(verifyDB);
				}
				verifyDB.verifyDB();
				if (controller != null) {
					((ControllerSplash) controller).stop();
				}

			}
		} catch (Throwable e) {
			e.printStackTrace();
		}
	}
}
