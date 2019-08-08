package co.com.japl.ea.beans;

import org.pyt.app.components.IGenericFilter;
import org.pyt.common.abstracts.ADto;
import org.pyt.common.annotations.Inject;
import org.pyt.common.common.Comunicacion;
import org.pyt.common.common.I18n;
import org.pyt.common.common.Log;
import org.pyt.common.constants.CSSConstant;
import org.pyt.common.exceptions.ReflectionException;

import co.com.japl.ea.controllers.LocatorController;
import co.com.japl.ea.dto.system.UsuarioDTO;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

/**
 * Bean especializado para creacion de beans que no usan fxml sino implementan
 * los campos en la propua clase
 * 
 * @author Alejandro Parra
 * @since 10/04/2019
 * @param <T>
 */
public abstract class AGenericToBean<T extends ADto> extends Application implements IBean<T>, IGenericFilter<T> {
	protected T registro;
	protected BorderPane panel;
	protected UsuarioDTO userLogin;
	protected String NombreVentana;
	protected Stage primaryStage;
	private Class<T> clazz;
	protected String caller;
	private Double width;
	private Double height;
	protected String tittleWindowI18n;
	@SuppressWarnings("rawtypes")
	@Inject
	protected Comunicacion comunicacion;
	protected Log logger = Log.Log(this.getClass());
	private I18n languages;

	public AGenericToBean() {
		try {
			userLogin = new UsuarioDTO();
			userLogin.setNombre("nano871022");
			panel = new BorderPane();
			inject();
			LocatorController.getInstance().setClass(this.getClass()).putLoadInController(this);
		} catch (ReflectionException e) {
			logger.logger("Reflection: ", e);
		} catch (Exception e) {
			logger.logger(e);
		}
	}

	@SuppressWarnings("rawtypes")
	public final Comunicacion comunicacion() {
		return comunicacion;
	}

	public final T getRegistro() {
		return registro;
	}

	public final void setRegistro(T registro) {
		this.registro = registro;
	}

	public final String getNombreVentana() {
		return NombreVentana;
	}

	public final void setNombreVentana(String nombreVentana) {
		this.NombreVentana = nombreVentana;
	}

	@SuppressWarnings({ "hiding", "unchecked" })
	public final <T extends Object> T meThis() {
		return (T) this;
	}

	public final Log logger() {
		return logger;
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		this.primaryStage = primaryStage;
		if (width != null) {
			primaryStage.setWidth(width);
		}
		if (height != null) {
			primaryStage.setHeight(height);
		}
		primaryStage.setTitle(i18n().valueBundle(tittleWindowI18n));
		Scene scene = new Scene(panel);
		scene.getStylesheets().add(CSSConstant.CONST_PRINCIPAL);
		primaryStage.setScene(scene);
		primaryStage.hide();
	}

	protected void showWindow() {
		primaryStage.show();
	}

	@SuppressWarnings("unchecked")
	public final <S extends AGenericToBean<T>> S setWidth(double width) {
		primaryStage.setWidth(width);
		return (S) this;
	}

	/**
	 * Se encarga la ventana que esta cargando este bean generico, si es un popup lo
	 * cierra, pero si se carga sobre la aplicacion principal la cierra tambien.
	 */
	protected final void closeWindow() {
		primaryStage.close();
	}

	public abstract void initialize();

	protected void sizeWindow(Integer width, Integer height) {
		this.width = width.doubleValue();
		this.height = height.doubleValue();
	}

	public I18n i18n() {
		if (languages == null) {
			languages = new I18n();
		}
		return languages;
	}

	public Class<T> getClazz() {
		return clazz;
	}
	
	public void setClazz(Class<T> clazz) {
		this.clazz= clazz;
	}
	
	public I18n getI18n() {
		return languages;
	}

	public Log getLogger() {
		return logger;
	}
	
}
