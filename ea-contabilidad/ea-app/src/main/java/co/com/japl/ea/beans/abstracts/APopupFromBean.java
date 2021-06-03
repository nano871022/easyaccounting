package co.com.japl.ea.beans.abstracts;

import org.pyt.common.annotations.Inject;
import org.pyt.common.common.Comunicacion;
import org.pyt.common.common.Log;

import co.com.japl.ea.common.abstracts.ADto;
import co.com.japl.ea.exceptions.LoadAppFxmlException;
import co.com.japl.ea.interfaces.IBean;
import co.com.japl.ea.interfaces.INotificationMethods;
import javafx.stage.Stage;

public abstract class APopupFromBean<D extends ADto> implements IBean<D>, INotificationMethods {
	protected Class<D> clazz;
	protected Stage primaryStage;
	protected Double width;
	protected Double height;
	@SuppressWarnings("rawtypes")
	@Inject
	protected Comunicacion comunicacion;
	protected Log logger = Log.Log(this.getClass());
	protected Stage stage;

	public APopupFromBean(Class<D> clazz) {
		this.clazz = clazz;
	}

	public abstract void start(Stage primaryStage) throws Exception;

	public abstract void load() throws LoadAppFxmlException;

	public void showWindow() {
		primaryStage.show();
	}

	public void sizeWindow(Integer width, Integer height) {
		this.width = width.doubleValue();
		this.height = height.doubleValue();
	}

	@Override
	public Log logger() {
		return logger;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public Comunicacion comunicacion() {
		return comunicacion;
	}

	public void setStage(Stage primaryStage) {
		this.stage = primaryStage;
	}

	/**
	 * Se encarga la ventana que esta cargando este bean generico, si es un popup lo
	 * cierra, pero si se carga sobre la aplicacion principal la cierra tambien.
	 */
	protected final void closeWindow() {
		primaryStage.close();
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> T meThis() {
		return (T) this;
	}
}
