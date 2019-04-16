package org.pyt.common.common;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

import org.pyt.common.annotations.Inject;
import org.pyt.common.constants.CSSConstant;
import org.pyt.common.controller.LocatorController;
import org.pyt.common.exceptions.ReflectionException;

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
public abstract class GenericToBean<T extends ADto> extends Application implements IBean<T> {
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
	private Map<String, Object> defaultValuesGenericParametrized;
	private I18n languages;

	public GenericToBean() {
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
		primaryStage.show();
	}

	/**
	 * Se encarhad e obtener una nueva instancia de la clase usada como generico de
	 * la aplicacion
	 * 
	 * @return T generico usado, extiende de {@link ADto}
	 * @throws {@link InvocationTargetException}
	 * @throws {@link IllegalAccessException}
	 * @throws {@link InstantiationException}
	 * @throws {@link NoSuchMethodException}
	 */
	protected final T getInstaceOfGenericADto()
			throws InvocationTargetException, IllegalAccessException, InstantiationException, NoSuchMethodException {
		return clazz.getConstructor().newInstance();
	}

	/**
	 * Se encarga la ventana que esta cargando este bean generico, si es un popup lo
	 * cierra, pero si se carga sobre la aplicacion principal la cierra tambien.
	 */
	protected final void closeWindow() {
		primaryStage.close();
	}

	public abstract void initialize();

	protected final Map<String, Object> getDefaultValuesToGenericParametrized() {
		return defaultValuesGenericParametrized;
	}

	@SuppressWarnings("unchecked")
	public final <S extends GenericToBean<T>> S addDefaultValuesToGenericParametrized(String key, Object value) {
		if (defaultValuesGenericParametrized == null) {
			defaultValuesGenericParametrized = new HashMap<String, Object>();
		}
		defaultValuesGenericParametrized.put(key, value);
		return (S) this;
	}

	public final void setDefaultValuesToGenericParametrized(Map<String, Object> defaultParameterized) {
		this.defaultValuesGenericParametrized = defaultParameterized;
	}

	public Class<T> getClassParameterized() {
		return clazz;
	}

	public void setClassParameterized(Class<T> clazz) {
		this.clazz = clazz;
	}

	protected T assingValuesParameterized(T dto) {
		if (defaultValuesGenericParametrized == null) {
			defaultValuesGenericParametrized = new HashMap<String, Object>();
		}
		defaultValuesGenericParametrized.forEach((key, value) -> {
			try {
				dto.set(key, value);
			} catch (ReflectionException e) {
				logger().logger(e);
			}
		});
		return dto;
	}
	
	protected void sizeWindow(Integer width,Integer height) {
		this.width = width.doubleValue();
		this.height = height.doubleValue();
	}

	public I18n i18n() {
		if (languages == null) {
			languages = new I18n();
		}
		return languages;
	}

}
