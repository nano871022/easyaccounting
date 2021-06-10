package co.com.japl.ea.app.load;

import static org.pyt.common.constants.PropertiesConstants.PROP_APP_POM;

import java.net.URL;
import java.time.Duration;
import java.util.ResourceBundle;

import co.com.japl.ea.common.properties.CachePropertiesPOM;
import co.com.japl.ea.query.interfaces.IVerifyStructuredDB;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class ControllerSplash implements Initializable, Runnable {
	@FXML
	private ImageView fondo;
	@FXML
	private ProgressBar progress;
	@FXML
	private TextField percentage;
	@FXML
	private Label version;
	private IVerifyStructuredDB verify;
	private Boolean runningSplash = true;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		CachePropertiesPOM.instance().load(getClass(), PROP_APP_POM).get("project.version")
				.ifPresent(value -> version.setText(value));
		var image = new Image("@../../images/logo_ea.jpg");
		fondo.setImage(image);
		new Thread(this).start();
	}

	public final void setVerifySctructureDB(IVerifyStructuredDB verify) {
		this.verify = verify;
	}

	public final void stop() {
		runningSplash = false;
	}

	@Override
	public void run() {
		while (runningSplash) {
			try {
				Thread.sleep(50);
				Double value = 0.0;
				if (verify != null) {
					value = (double) (verify.counScriptRuns() * 100) / verify.countScripts();
					value = value / 100;
				}
				if (Double.isNaN(value)) {
					continue;
				}
				String vlue = (value * 100) + " %";
				percentage.setText(vlue);
				progress.setProgress(value);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		try {
			Thread.sleep(Duration.ofSeconds(10).toMillis());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
