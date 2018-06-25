package com.pyt.service.implement;

import java.util.ArrayList;
import java.util.List;

import org.pyt.common.annotations.Inject;
import org.pyt.common.common.UsuarioDTO;
import org.pyt.common.exceptions.BancoException;
import org.pyt.common.exceptions.QueryException;

import com.pyt.query.interfaces.IQuerySvc;
import com.pyt.service.abstracts.Services;
import com.pyt.service.dto.BancoDTO;
import com.pyt.service.interfaces.IBancosSvc;

public class BancoSvc extends Services implements IBancosSvc {
	@Inject(resource = "com.pyt.query.implement.QuerySvc")
	private IQuerySvc querySvc;

	public List<BancoDTO> getBancos(BancoDTO dto, Integer init, Integer end)
			throws BancoException {
		List<BancoDTO> lista = new ArrayList<BancoDTO>();
		if (dto == null)
			throw new BancoException("El objeto banco se encuentra vacio.");
		try {
			lista = querySvc.gets(dto, init, end);
		} catch (QueryException e) {
			throw new BancoException(e.getMensage(), e);
		}
		return lista;
	}

	public List<BancoDTO> getAllBancos(BancoDTO dto) throws BancoException {
		List<BancoDTO> lista = new ArrayList<BancoDTO>();
		if (dto == null)
			throw new BancoException("El objeto banco se encuentra vacio.");
		try {
			lista = querySvc.gets(dto);
		} catch (QueryException e) {
			throw new BancoException(e.getMensage(), e);
		}
		return lista;
	}

	public BancoDTO getBancos(BancoDTO dto) throws BancoException {
		if (dto == null)
			throw new BancoException("El objeto banco se encuentra vacio.");
		try {
			return querySvc.get(dto);
		} catch (QueryException e) {
			throw new BancoException(e.getMensage(), e);
		}
	}

	public void update(BancoDTO dto, UsuarioDTO user) throws BancoException {
		if (dto == null)
			throw new BancoException("El objeto banco se encuentra vacio.");
		if (dto.getCodigo() == null)
			throw new BancoException("El id de actividad ica se encuentra vacia.");
		try {
			querySvc.set(dto, user);
		} catch (QueryException e) {
			throw new BancoException(e.getMensage(), e);
		}
	}

	public BancoDTO insert(BancoDTO dto, UsuarioDTO user) throws BancoException {
		if (dto == null)
			throw new BancoException("El objeto banco se encuentra vacio.");
		if (dto.getCodigo() != null)
			throw new BancoException("El codigo de banco no se encuentra vacio.");
		try {
			return querySvc.set(dto, user);
		} catch (QueryException e) {
			throw new BancoException(e.getMensage(), e);
		}
	}

	public void delete(BancoDTO dto, UsuarioDTO user) throws BancoException {
		if (dto == null)
			throw new BancoException("El objeto banco se encuentra vacio.");
		if (dto.getCodigo() == null)
			throw new BancoException("El codigo banco se encuentra vacio.");
		try {
			querySvc.del(dto, user);
		} catch (Exception e) {
			throw new BancoException(e.getMessage(), e);
		}

	}

}
