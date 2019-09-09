package org.pyt.app.beans.users;

import org.pyt.app.load.Template;
import org.pyt.common.annotations.Inject;
import org.pyt.common.validates.ValidFields;

import com.pyt.service.interfaces.IGenericServiceSvc;

import co.com.arquitectura.annotation.proccessor.FXMLFile;
import co.com.japl.ea.beans.ABean;
import co.com.japl.ea.beans.LoadAppFxml;
import co.com.japl.ea.dto.system.UsuarioDTO;
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

	private final static String CONST_MSN_USER_ERROR = "form.lbl.msn.error.user";
	private final static String CONST_MSN_PASSWORD_ERROR = "form.lbl.msn.error.password";
	private final static String CONST_TITLE_LOGIN = "form.title.login";

	@Inject(resource = "com.pyt.service.implement.GenericServiceSvc")
	private IGenericServiceSvc<UsuarioDTO> usersSvc;
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

	@FXML
	public void initialize() {
		registro = new UsuarioDTO();
		title.setText(i18n().valueBundle(CONST_TITLE_LOGIN));
	}

	private final boolean valid() {
		var valid = true;
		valid &= ValidFields.valid(user, true, 3, 20, i18n().valueBundle(CONST_MSN_USER_ERROR));
		valid &= ValidFields.valid(password, true, 3, 20, i18n().valueBundle(CONST_MSN_PASSWORD_ERROR));
		return valid;
	}

	public void connect() {
		try {
			if (valid()) {
				registro.setNombre(user.getText());
				registro.setPassword(password.getText());
				var user = usersSvc.get(registro);
				user.setPassword(null);
				this.userLogin = user;
				LoadAppFxml.loadFxml(new Stage(), Template.class);
				this.destroy();
			}
		} catch (Exception e) {
			error(e);
			registro.setPassword(null);
			password.setText(null);
			message.setText(e.getMessage());
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
		registro = new UsuarioDTO();
		clearAll();
	}

}
