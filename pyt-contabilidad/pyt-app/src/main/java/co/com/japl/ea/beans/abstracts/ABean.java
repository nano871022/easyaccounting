package co.com.japl.ea.beans.abstracts;

import java.util.Optional;

import org.pyt.common.abstracts.ADto;
import org.pyt.common.annotations.Inject;
import org.pyt.common.common.Comunicacion;
import org.pyt.common.common.I18n;
import org.pyt.common.common.Log;
import org.pyt.common.common.RefreshCodeValidation;
import org.pyt.common.constants.RefreshCodeConstant;
import org.pyt.common.exceptions.ReflectionException;

import com.pyt.service.interfaces.IGenericServiceSvc;

import co.com.japl.ea.controllers.LocatorController;
import co.com.japl.ea.dto.system.LanguagesDTO;
import co.com.japl.ea.dto.system.UsuarioDTO;
import co.com.japl.ea.interfaces.IBean;
import co.com.japl.ea.utls.LoginUtil;

/**
 * Se encarga de implementar {@link IBean} para contruir controlladores de
 * interfaces creadas en fxml
 * 
 * @author Alejandro Parra
 * @since 10/04/2019
 * @param <T>
 */
public abstract class ABean<T extends ADto> implements IBean<T> {
	protected T registro;
	@Inject(resource = "com.pyt.service.implement.GenericServiceSvc")
	private IGenericServiceSvc<LanguagesDTO> languagesSvc;
	protected String NombreVentana;
	@SuppressWarnings("rawtypes")
	@Inject
	protected Comunicacion comunicacion;
	protected Log logger = Log.Log(this.getClass());

	public ABean() {
		try {
			inject();
			LocatorController.getInstance().setClass(this.getClass()).putLoadInController(this);
		} catch (ReflectionException e) {
			logger.logger("Reflection: ", e);
		} catch (RuntimeException e) {
			logger.logger("RunTimeException: ", e);
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

	private void loadLanguagesDB(I18n language) {
		try {
			var dto = new LanguagesDTO();
			var list = languagesSvc.getAll(dto);
			language.setLanguagesDB(list);
		} catch (Exception e) {
			logger.logger(e);
		}
	}

	public I18n i18n() {
		var i18n = I18n.instance();
		if (i18n.isEmptyDBLanguages()
				|| RefreshCodeValidation.getInstance().validate(RefreshCodeConstant.CONST_ADD_CACHE_LANGUAGE)) {
			loadLanguagesDB(i18n);
		}
		return i18n;
	}

	protected final UsuarioDTO getUsuario() {
		return Optional.ofNullable(LoginUtil.getUsuarioLogin()).orElse(new UsuarioDTO());
	}

	protected final void setUsuario(UsuarioDTO usuario) {
		LoginUtil.setUsuarioLogin(Optional.ofNullable(usuario).orElse(new UsuarioDTO()));
	}
}
