package org.pyt.app.load;

import org.pyt.app.beans.EmpresaBean;
import org.pyt.common.common.LoadAppFxml;
import org.pyt.common.constants.AppConstants;

import javafx.application.Application;
import javafx.stage.Stage;

public class App extends Application {

	@Override
	public void start(Stage primaryStage) throws Exception {
		LoadAppFxml.loadBeanFxml(primaryStage, EmpresaBean.class, AppConstants.TITTLE_APP);
	}

	public final static void main(String... args) {
		App.launch(args);
	}

}
