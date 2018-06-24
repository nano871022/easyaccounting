package com.pyt.service.implement;

import java.util.ArrayList;
import java.util.List;

import org.pyt.common.common.UsuarioDTO;
import org.pyt.common.exceptions.CentroCostosException;
import org.pyt.common.exceptions.QueryException;

import com.pyt.query.interfaces.IQuerySvc;
import com.pyt.service.dto.CentroCostoDTO;
import com.pyt.service.interfaces.ICentroCostosSvc;

public class CentroCostoSvc implements ICentroCostosSvc {

	private IQuerySvc querySvc;

	public List<CentroCostoDTO> getCentroCostos(CentroCostoDTO dto, Integer init, Integer end)
			throws CentroCostosException {
		List<CentroCostoDTO> lista = new ArrayList<CentroCostoDTO>();
		if (dto == null)
			throw new CentroCostosException("El objeto centro de costo se encuentra vacio.");
		try {
			lista = querySvc.gets(dto, init, end);
		} catch (QueryException e) {
			throw new CentroCostosException(e.getMensage(), e);
		}
		return lista;
	}

	public List<CentroCostoDTO> getAllCentroCostos(CentroCostoDTO dto) throws CentroCostosException {
		List<CentroCostoDTO> lista = new ArrayList<CentroCostoDTO>();
		if (dto == null)
			throw new CentroCostosException("El objeto centro de costo se encuentra vacio.");
		try {
			lista = querySvc.gets(dto);
		} catch (QueryException e) {
			throw new CentroCostosException(e.getMensage(), e);
		}
		return lista;
	}

	public CentroCostoDTO getCentroCosto(CentroCostoDTO dto) throws CentroCostosException {
		if (dto == null)
			throw new CentroCostosException("El objeto centro de costo se encuentra vacio.");
		try {
			return querySvc.get(dto);
		} catch (QueryException e) {
			throw new CentroCostosException(e.getMensage(), e);
		}
	}

	public void update(CentroCostoDTO dto, UsuarioDTO user) throws CentroCostosException {
		if (dto == null)
			throw new CentroCostosException("El objeto centro de costo se encuentra vacio.");
		if (dto.getCodigo() == null)
			throw new CentroCostosException("El id de centro de costo se encuentra vacia.");
		try {
			querySvc.set(dto, user);
		} catch (QueryException e) {
			throw new CentroCostosException(e.getMensage(), e);
		}
	}

	public CentroCostoDTO insert(CentroCostoDTO dto, UsuarioDTO user) throws CentroCostosException {
		if (dto == null)
			throw new CentroCostosException("El objeto centro de costo se encuentra vacio.");
		if (dto.getCodigo() != null)
			throw new CentroCostosException("El codigo de centro de costo no se encuentra vacio.");
		try {
			return querySvc.set(dto, user);
		} catch (QueryException e) {
			throw new CentroCostosException(e.getMensage(), e);
		}
	}

	public void delete(CentroCostoDTO dto, UsuarioDTO user) throws CentroCostosException {
		if (dto == null)
			throw new CentroCostosException("El objeto centro de costo se encuentra vacio.");
		if (dto.getCodigo() == null)
			throw new CentroCostosException("El codigo centro de costo se encuentra vacio.");
		try {
			querySvc.del(dto, user);
		} catch (Exception e) {
			throw new CentroCostosException(e.getMessage(), e);
		}

	}

}
