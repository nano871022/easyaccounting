package com.pyt.service.implement;

import static org.pyt.common.constants.LanguageConstant.CONST_ERR_COUNT_USER_EMPTY;
import static org.pyt.common.constants.LanguageConstant.CONST_ERR_CREATE_USER_CODE_NOT_EMPTY;
import static org.pyt.common.constants.LanguageConstant.CONST_ERR_CREATE_USER_EMPTY;
import static org.pyt.common.constants.LanguageConstant.CONST_ERR_DEL_USER_CODE_EMPTY;
import static org.pyt.common.constants.LanguageConstant.CONST_ERR_DEL_USER_EMPTY;
import static org.pyt.common.constants.LanguageConstant.CONST_ERR_GET_ALL_USER_EMPTY;
import static org.pyt.common.constants.LanguageConstant.CONST_ERR_GET_ALL_USER_PAGE;
import static org.pyt.common.constants.LanguageConstant.CONST_ERR_GET_USER_EMPTY;
import static org.pyt.common.constants.LanguageConstant.CONST_ERR_IP_MACHINE_NOT_SUBMIT;
import static org.pyt.common.constants.LanguageConstant.CONST_ERR_NEW_PASS_USER_DIFERENT;
import static org.pyt.common.constants.LanguageConstant.CONST_ERR_OLD_PASS_USER_DIFERENT;
import static org.pyt.common.constants.LanguageConstant.CONST_ERR_UPD_USER_CODE_EMPTY;
import static org.pyt.common.constants.LanguageConstant.CONST_ERR_UPD_USER_EMPTY;
import static org.pyt.common.constants.LanguageConstant.CONST_ERR_USER_NOT_SUBMIT;
import static org.pyt.common.constants.languages.Login.CONST_ERR_IP_MACHINE_EMPTY;
import static org.pyt.common.constants.languages.Login.CONST_ERR_IP_PUBLIC_EMPTY;
import static org.pyt.common.constants.languages.Login.CONST_ERR_USER_EMPTY;

import java.net.UnknownHostException;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.pyt.common.annotations.Inject;
import org.pyt.common.exceptions.QueryException;

import com.pyt.query.interfaces.IQuerySvc;
import com.pyt.service.abstracts.Services;
import com.pyt.service.interfaces.IUsersSvc;

import co.com.japl.ea.dto.system.LoginDTO;
import co.com.japl.ea.dto.system.UsuarioDTO;
import co.com.japl.services.privates.utils.LoginUtil;

public class UserSvc extends Services implements IUsersSvc {
	@Inject(resource = "com.pyt.query.implement.QuerySvc")
	private IQuerySvc querySvc;

	@Override
	public void create(UsuarioDTO newUser, UsuarioDTO user) throws Exception {
		if (newUser == null) {
			throw new Exception(i18n().valueBundle(CONST_ERR_CREATE_USER_EMPTY).get());
		}
		if (StringUtils.isNotBlank(newUser.getCodigo())) {
			throw new Exception(i18n().valueBundle(CONST_ERR_CREATE_USER_CODE_NOT_EMPTY).get());
		}
		querySvc.insert(newUser, user);
	}

	@Override
	public void delete(UsuarioDTO delUser, UsuarioDTO user) throws Exception {
		if (delUser == null) {
			throw new Exception(i18n().valueBundle(CONST_ERR_DEL_USER_EMPTY).get());
		}
		if (StringUtils.isBlank(delUser.getCodigo())) {
			throw new Exception(i18n().valueBundle(CONST_ERR_DEL_USER_CODE_EMPTY).get());
		}
		delUser.setPassword(null);
		querySvc.del(delUser, user);
	}

	@Override
	public List<UsuarioDTO> getAll(UsuarioDTO user, Integer init, Integer size) throws Exception {
		if (user == null) {
			throw new Exception(i18n().valueBundle(CONST_ERR_GET_ALL_USER_EMPTY).get());
		}
		if (init < 0 || size < 0) {
			throw new Exception(i18n().valueBundle(CONST_ERR_GET_ALL_USER_PAGE).get());
		}
		user.setPassword(null);
		var found = querySvc.gets(user, init, size);
		found.forEach(puser -> puser.setPassword(null));
		return found;
	}

	@Override
	public UsuarioDTO get(UsuarioDTO user) throws Exception {
		if (user == null) {
			throw new Exception(i18n().valueBundle(CONST_ERR_GET_USER_EMPTY).get());
		}
		user.setPassword(null);
		var found = querySvc.get(user);
		found.setPassword(null);
		return found;
	}

	@Override
	public void update(UsuarioDTO updUser, UsuarioDTO user) throws Exception {
		if (updUser == null) {
			throw new Exception(i18n().valueBundle(CONST_ERR_UPD_USER_EMPTY).get());
		}
		if (StringUtils.isBlank(updUser.getCodigo())) {
			throw new Exception(i18n().valueBundle(CONST_ERR_UPD_USER_CODE_EMPTY).get());
		}
		querySvc.update(updUser, user);
	}

	@Override
	public Integer countRow(UsuarioDTO updUser) throws Exception {
		if (updUser == null) {
			throw new Exception(i18n().valueBundle(CONST_ERR_COUNT_USER_EMPTY).get());
		}
		updUser.setPassword(null);
		return querySvc.countRow(updUser);

	}

	private final boolean validRemember(UsuarioDTO login, UsuarioDTO found, String ipMachine, Boolean remember)
			throws QueryException, UnknownHostException {
		if (remember) {
			String ipPublic = LoginUtil.remoteAddr();
			List<LoginDTO> listLogins = searchAllLogins(found, ipMachine, ipPublic, remember);

			if (listLogins.size() > 0
					&& login.getPassword() == null && StringUtils.isNotBlank(login
							.getCodigo())
					&& listLogins.stream().filter(row -> Optional.ofNullable(row.getFechaInicio()).isPresent()
							&& row.getFechaInicio().compareTo(LocalDate.now()) <= 0).count() > 0) {
				if (listLogins.stream().filter(row -> Optional.ofNullable(row.getFechaFin()).isPresent()).count() > 0) {
					if (listLogins.stream().filter(row -> row.getFechaFin().compareTo(LocalDate.now()) > 0)
							.count() > 0) {
						return true;
					}
				} else {
					return true;
				}
			}
		}
		return false;
	}

	@Override
	public UsuarioDTO login(UsuarioDTO user, String ipMachine, Boolean remember) throws Exception {
		UsuarioDTO found = null;
		var pass = Optional.ofNullable(user.getPassword());
		user.setPassword(null);
		var count = countRow(user);
		var loginRemember = false;
		if (count > 0) {
			var foundUser = querySvc.get(user);
			loginRemember = validRemember(user, foundUser, ipMachine, remember);
			if ((!loginRemember && foundUser.getPassword().contentEquals(pass.orElse("")))
					||(loginRemember && Optional.ofNullable(foundUser).isPresent())) {
				foundUser.setPassword(null);
				found = foundUser;
			}
		}
		if (Optional.ofNullable(found).isPresent()) {
			var ipPublic = LoginUtil.remoteAddr();
			List<LoginDTO> list = searchAllLogins(user, ipMachine, ipPublic, remember).stream()
					.filter(row -> !Optional.ofNullable(row.getFechaFin()).isPresent()|| row.getFechaFin().compareTo(LocalDate.now()) >= 0)
					.collect(Collectors.toList());
			if (list.size() > 0) {
				list.forEach(row -> {updateLogin(row, user);LoginUtil.INSTANCE().loadLogin(row);});
			} else {
				createLogin(found, ipMachine, ipPublic, remember);
			}
		}
		return found;
	}

	@Override
	public void logout(UsuarioDTO user, String ipMachine,Boolean remember) throws Exception {
		if (!Optional.ofNullable(user).isPresent()) {
			throw new Exception(CONST_ERR_USER_NOT_SUBMIT);
		}
		if (!Optional.ofNullable(user).isPresent()) {
			throw new Exception(CONST_ERR_IP_MACHINE_NOT_SUBMIT);
		}
		var ipPublic = LoginUtil.remoteAddr();
		var founds = searchAllLogins(user, ipMachine, ipPublic, remember);
		founds.forEach(row -> {unEnabledLogin(row, user);LoginUtil.INSTANCE().removeLogin(row);});
	}

	@Override
	public void passwordChange(String newPassword, String newPassword2, String oldPassword, UsuarioDTO user)
			throws Exception {
		user.setPassword(null);
		var foundUser = get(user);
		if (!newPassword.contentEquals(oldPassword)) {
			throw new Exception(CONST_ERR_NEW_PASS_USER_DIFERENT);
		}
		if (foundUser.getPassword().contentEquals(oldPassword)) {
			var pass = DigestUtils.sha1Hex(newPassword);
			logger().logger("Test encript "+newPassword+" "+pass);
			foundUser.setPassword(newPassword);
			update(foundUser, user);
		} else {
			throw new Exception(CONST_ERR_OLD_PASS_USER_DIFERENT);
		}

	}

	private List<LoginDTO> searchAllLogins(UsuarioDTO usuario, String ipMachine, String ipPublic, Boolean recordar)
			throws QueryException {
		var login = new LoginDTO();
		login.setUsuario(usuario);
		login.setIpMaquina(ipMachine);
		login.setIpPublica(ipPublic);
		login.setRecordar(recordar);
		var result = querySvc.gets(login);
		var logins = result.stream().filter(row -> row.getEliminador() == null && row.getFechaEliminacion() == null)
				.filter(row -> row.getFechaFin() == null || row.getFechaFin().compareTo(LocalDate.now()) >= 0)
				.map(row -> row).collect(Collectors.toList());
		return logins;
	}

	private void createLogin(UsuarioDTO usuario, String ipMachine, String ipPublic, Boolean remember) throws Exception {
		var login = new LoginDTO();
		if (StringUtils.isBlank(ipMachine)) {
			throw new Exception(i18n().valueBundle(CONST_ERR_IP_MACHINE_EMPTY).get());
		}
		if (StringUtils.isBlank(ipPublic)) {
			throw new Exception(i18n().valueBundle(CONST_ERR_IP_PUBLIC_EMPTY).get());
		}

		if (usuario == null || StringUtils.isBlank(usuario.getCodigo())) {
			throw new Exception(i18n().valueBundle(CONST_ERR_USER_EMPTY).get());
		}
		login.setUsuario(usuario);
		login.setIpMaquina(ipMachine);
		login.setIpPublica(ipPublic);
		login.setRecordar(remember);
		login.setFechaInicio(LocalDate.now());
		login.setFechaFin(LocalDate.now().plusDays(8));
		querySvc.insert(login, usuario);
		LoginUtil.INSTANCE().loadLogin(login);
	}

	private void updateLogin(LoginDTO login, UsuarioDTO usuario) throws RuntimeException {
		try {
			if(!Optional.ofNullable(login.getFechaInicio()).isPresent()) {
				login.setFechaInicio(LocalDate.now());
			}
			login.setFechaFin(LocalDate.now().plusDays(8));
			querySvc.set(login, usuario);
		} catch (QueryException e) {
			throw new RuntimeException(e);
		}
	}

	private void unEnabledLogin(LoginDTO login, UsuarioDTO usuario) throws RuntimeException {
		try {
			login.setFechaFin(LocalDate.now());
			login.setFechaEliminacion(new Date());
			login.setEliminador(usuario.getNombre());
			login.setRecordar(false);
			querySvc.set(login, usuario);
		} catch (QueryException e) {
			throw new RuntimeException(e);
		}
	}

}
