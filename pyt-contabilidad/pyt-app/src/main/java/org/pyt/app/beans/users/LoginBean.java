package org.pyt.app.beans.users;

import static org.pyt.common.constants.languages.Login.CONST_MSN_PASSWORD_ERROR;
import static org.pyt.common.constants.languages.Login.CONST_MSN_USER_ERROR;
import static org.pyt.common.constants.languages.Login.CONST_TITLE_LOGIN;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Optional;

import org.pyt.app.load.Template;
import org.pyt.common.annotations.Inject;
import org.pyt.common.constants.LanguageConstant;
import org.pyt.common.constants.StylesPrincipalConstant;
import org.pyt.common.exceptions.LoadAppFxmlException;
import org.pyt.common.validates.ValidFields;

import com.pyt.service.interfaces.IUsersSvc;

import co.com.arquitectura.annotation.proccessor.FXMLFile;
import co.com.japl.ea.beans.abstracts.ABean;
import co.com.japl.ea.dto.system.UsuarioDTO;
import co.com.japl.ea.utls.LoadAppFxml;
import co.com.japl.ea.utls.LoginUtil;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 * Se encarga de procesar la pantalla de creacion y actualizacion de una
 * actividad ica
 * 
 * @author Alejandro Parra
 * @since 2018-05-22
 */
@FXMLFile(path = "view/users", file = "login.fxml")
public class LoginBean extends ABean<UsuarioDTO> {

	@Inject(resource = "com.pyt.service.implement.UserSvc")
	private IUsersSvc usersSvc;
	@FXML
	private TextField user;
	@FXML
	private PasswordField password;
	@FXML
	private CheckBox remember;
	@FXML
	private Label message;
	@FXML
	private Label title;
	private Stage stage;
	private Boolean login = false;

	@FXML
	public void initialize() {
		registro = new UsuarioDTO();
		title.setText(i18n().valueBundle(CONST_TITLE_LOGIN).get());
		verifyLoginRemember();
	}

	private void verifyLoginRemember() {
		try {
			var login = LoginUtil.loadLogin();
			if (login != null) {
				remember.setSelected(true);
				LoginUtil.validUsuarioRememberFails(login);
				var found = usersSvc.login(login, remoteAddr(), remember.isSelected());
				if (found != null) {
					LoginUtil.compareUsuariosRememberAndFound(found, login);
					LoginUtil.writeRemember(found);
					setUsuario(found);
					this.login = true;
				}
			}
		} catch (IOException e) {
			error(e);
		} catch (ClassNotFoundException e) {
			error(e);
		} catch (RuntimeException e) {
			error(e);
		} catch (LoadAppFxmlException e) {
			error(e);
		} catch (Exception e) {
			error(e);
		}

	}

	public void load(Stage stage) {
		this.stage = stage;
		if (login) {
			try {
				loadTemplate();
			} catch (LoadAppFxmlException e) {
				error(e);
			}
		}
	}

	private final boolean valid() {
		var valid = true;
		valid &= ValidFields.valid(user, true, 3, 20, i18n().valueBundle(CONST_MSN_USER_ERROR));
		valid &= ValidFields.valid(password, true, 3, 20, i18n().valueBundle(CONST_MSN_PASSWORD_ERROR));
		return valid;
	}

	private void loadTemplate() throws LoadAppFxmlException {
		this.destroy();
		if (Optional.ofNullable(stage).isPresent()) {
			stage.hide();
		}
		LoadAppFxml.loadFxml(new Stage(), Template.class);
	}

	public void connect() {
		try {
			if (valid()) {
				registro.setNombre(user.getText());
				registro.setPassword(password.getText());
				var user = usersSvc.login(registro, remoteAddr(), remember.isSelected());
				user.setPassword(null);
				this.setUsuario(user);
				if (remember.isSelected()) {
					LoginUtil.writeRemember(getUsuario());
				}
				loadTemplate();
			}
		} catch (NullPointerException e) {
			registro.setPassword(null);
			password.setText(null);
			message.setText(i18n().valueBundle(LanguageConstant.CONST_ERR_NULL_POINTER_EXCEPTION_LOGIN).get());
			message.getStyleClass().add(StylesPrincipalConstant.CONST_MESSAGE_ERROR);
		} catch (RuntimeException e) {
			registro.setPassword(null);
			password.setText(null);
			alerta(e.getMessage());
			message.setText(e.getMessage());
			message.getStyleClass().add(StylesPrincipalConstant.CONST_MESSAGE_WARN);
		} catch (Exception e) {
			error(e);
			registro.setPassword(null);
			password.setText(null);
			message.setText(e.getMessage());
			message.getStyleClass().add(StylesPrincipalConstant.CONST_MESSAGE_ERROR);
		}
	}

	public void clearAll() {
		user.setText(null);
		user.setTooltip(null);
		password.setText(null);
		password.setTooltip(null);
		registro = new UsuarioDTO();
		remember.setSelected(false);
		message.setText(null);
	}

	public void cancel() {
		try {
			usersSvc.logout(registro, remoteAddr(), false);
			registro = new UsuarioDTO();
			clearAll();
		} catch (Exception e) {
			error(e);
		}
	}

	private String remoteAddr() throws UnknownHostException {
		// return getContext().getRemoteAddr();
		return InetAddress.getLocalHost().getHostAddress();
	}

}
