package com.pyt.service.interfaces;

import java.util.List;

import co.com.japl.ea.dto.system.UsuarioDTO;

public interface IUsersSvc {

	public void create(UsuarioDTO newUser, UsuarioDTO user) throws Exception;

	public void delete(UsuarioDTO delUser, UsuarioDTO user) throws Exception;

	public List<UsuarioDTO> getAll(UsuarioDTO user, Integer init, Integer size) throws Exception;

	public UsuarioDTO get(UsuarioDTO user) throws Exception;

	public void update(UsuarioDTO updUser, UsuarioDTO user) throws Exception;

	public void countRow(UsuarioDTO updUser) throws Exception;

	public UsuarioDTO login(UsuarioDTO user, Boolean remember) throws Exception;

	public void logout(UsuarioDTO user) throws Exception;

	public void passwordChange(String newPassword, String newPassword2, String oldPassword, UsuarioDTO user)
			throws Exception;
}
