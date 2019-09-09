package com.pyt.service.implement;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.pyt.common.annotations.Inject;

import com.pyt.query.interfaces.IQuerySvc;
import com.pyt.service.abstracts.Services;
import com.pyt.service.interfaces.IUsersSvc;

import co.com.japl.ea.dto.system.UsuarioDTO;

public class UserSvc extends Services implements IUsersSvc {
	@Inject(resource = "com.pyt.query.implement.QuerySvc")
	private IQuerySvc querySvc;
	private final static String CONST_ERR_CREATE_USER_EMPTY = "";
	private final static String CONST_ERR_CREATE_USER_CODE_NOT_EMPTY = "";
	private final static String CONST_ERR_DEL_USER_EMPTY = "";
	private final static String CONST_ERR_DEL_USER_CODE_EMPTY = "";
	private final static String CONST_ERR_GET_ALL_USER_EMPTY = "";
	private final static String CONST_ERR_GET_ALL_USER_CODE_NOT_EMPTY = "";
	private final static String CONST_ERR_GET_ALL_USER_PAGE = "";
	private final static String CONST_ERR_GET_USER_EMPTY = "";
	private final static String CONST_ERR_GET_USER_CODE_NOT_EMPTY = "";
	private final static String CONST_ERR_UPD_USER_EMPTY = "";
	private final static String CONST_ERR_UPD_USER_CODE_EMPTY = "";
	private final static String CONST_ERR_COUNT_USER_EMPTY = "";
	private final static String CONST_ERR_COUNT_USER_CODE_EMPTY = "";
	private final static String CONST_ERR_NEW_PASS_USER_DIFERENT = "";
	private final static String CONST_ERR_OLD_PASS_USER_DIFERENT = "";

	@Override
	public void create(UsuarioDTO newUser, UsuarioDTO user) throws Exception {
		if (newUser == null) {
			throw new Exception(i18n().valueBundle(CONST_ERR_CREATE_USER_EMPTY));
		}
		if (StringUtils.isNotBlank(newUser.getCodigo())) {
			throw new Exception(i18n().valueBundle(CONST_ERR_CREATE_USER_CODE_NOT_EMPTY));
		}
		querySvc.insert(newUser, user);
	}

	@Override
	public void delete(UsuarioDTO delUser, UsuarioDTO user) throws Exception {
		if (delUser == null) {
			throw new Exception(i18n().valueBundle(CONST_ERR_DEL_USER_EMPTY));
		}
		if (StringUtils.isBlank(delUser.getCodigo())) {
			throw new Exception(i18n().valueBundle(CONST_ERR_DEL_USER_CODE_EMPTY));
		}
		delUser.setPassword(null);
		querySvc.del(delUser, user);
	}

	@Override
	public List<UsuarioDTO> getAll(UsuarioDTO user, Integer init, Integer size) throws Exception {
		if (user == null) {
			throw new Exception(i18n().valueBundle(CONST_ERR_GET_ALL_USER_EMPTY));
		}
		if (StringUtils.isNotBlank(user.getCodigo())) {
			throw new Exception(i18n().valueBundle(CONST_ERR_GET_ALL_USER_CODE_NOT_EMPTY));
		}
		if (init < 0 || size < 0) {
			throw new Exception(i18n().valueBundle(CONST_ERR_GET_ALL_USER_PAGE));
		}
		user.setPassword(null);
		var found = querySvc.gets(user, init, size);
		found.forEach(puser -> puser.setPassword(null));
		return found;
	}

	@Override
	public UsuarioDTO get(UsuarioDTO user) throws Exception {
		if (user == null) {
			throw new Exception(i18n().valueBundle(CONST_ERR_GET_USER_EMPTY));
		}
		if (StringUtils.isNotBlank(user.getCodigo())) {
			throw new Exception(i18n().valueBundle(CONST_ERR_GET_USER_CODE_NOT_EMPTY));
		}
		user.setPassword(null);
		var found = querySvc.get(user);
		found.setPassword(null);
		return found;
	}

	@Override
	public void update(UsuarioDTO updUser, UsuarioDTO user) throws Exception {
		if (updUser == null) {
			throw new Exception(i18n().valueBundle(CONST_ERR_UPD_USER_EMPTY));
		}
		if (StringUtils.isBlank(updUser.getCodigo())) {
			throw new Exception(i18n().valueBundle(CONST_ERR_UPD_USER_CODE_EMPTY));
		}
		querySvc.update(updUser, user);
	}

	@Override
	public void countRow(UsuarioDTO updUser) throws Exception {
		if (updUser == null) {
			throw new Exception(i18n().valueBundle(CONST_ERR_COUNT_USER_EMPTY));
		}
		if (StringUtils.isBlank(updUser.getCodigo())) {
			throw new Exception(i18n().valueBundle(CONST_ERR_COUNT_USER_CODE_EMPTY));
		}
		updUser.setPassword(null);
		querySvc.countRow(updUser);

	}

	@Override
	public UsuarioDTO login(UsuarioDTO user, Boolean remember) throws Exception {
		var pass = user.getPassword();
		user.setPassword(null);
		var foundUser = get(user);
		if (foundUser.getPassword().contentEquals(pass)) {
			foundUser.setPassword(null);
			return foundUser;
		}
		return null;
	}

	@Override
	public void logout(UsuarioDTO user) throws Exception {
		// TODO logout from sistem.
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
			foundUser.setPassword(newPassword);
			update(foundUser, user);
		} else {
			throw new Exception(CONST_ERR_OLD_PASS_USER_DIFERENT);
		}

	}

}
