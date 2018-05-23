package com.pyt.service.implement;

import java.util.ArrayList;
import java.util.List;

import org.pyt.common.common.UsuarioDTO;
import org.pyt.common.exceptions.ActividadIcaException;
import org.pyt.common.exceptions.QueryException;

import com.pyt.service.dto.ActividadIcaDTO;
import com.pyt.service.interfaces.IActividadIcaSvc;
import com.pyt.service.interfaces.IQuerySvc;

public class ActividadIcaSvc implements IActividadIcaSvc {

	private IQuerySvc querySvc;

	public List<ActividadIcaDTO> getActividadesIca(ActividadIcaDTO dto, Integer init, Integer end)
			throws ActividadIcaException {
		List<ActividadIcaDTO> lista = new ArrayList<ActividadIcaDTO>();
		if (dto == null)
			throw new ActividadIcaException("El objeto de actividad ica se encuentra vacia.");
		try {
			lista = querySvc.gets(dto, init, end);
		} catch (QueryException e) {
			throw new ActividadIcaException(e.getMensage(), e);
		}
		return lista;
	}

	public List<ActividadIcaDTO> getAllActividadesIca(ActividadIcaDTO dto) throws ActividadIcaException {
		List<ActividadIcaDTO> lista = new ArrayList<ActividadIcaDTO>();
		if (dto == null)
			throw new ActividadIcaException("El objeto actividad ica se encuentra vacia.");
		try {
			lista = querySvc.gets(dto);
		} catch (QueryException e) {
			throw new ActividadIcaException(e.getMensage(), e);
		}
		return lista;
	}

	public ActividadIcaDTO getActividadesIca(ActividadIcaDTO dto) throws ActividadIcaException {
		if (dto == null)
			throw new ActividadIcaException("El objeto actividad ica se encuentra vacia.");
		try {
			return querySvc.get(dto);
		} catch (QueryException e) {
			throw new ActividadIcaException(e.getMensage(), e);
		}
	}

	public void update(ActividadIcaDTO dto, UsuarioDTO user) throws ActividadIcaException {
		if (dto == null)
			throw new ActividadIcaException("El objeto actividad ica se encuentra vacia.");
		if (dto.getCodigo() == null)
			throw new ActividadIcaException("El id de actividad ica se encuentra vacia.");
		try {
			querySvc.set(dto, user);
		} catch (QueryException e) {
			throw new ActividadIcaException(e.getMensage(), e);
		}
	}

	public ActividadIcaDTO insert(ActividadIcaDTO dto, UsuarioDTO user) throws ActividadIcaException {
		if (dto == null)
			throw new ActividadIcaException("El objeto actividad ica se encuentra vacia.");
		if (dto.getCodigo() != null)
			throw new ActividadIcaException("El codigo de actividad ica no se encuentra vacio.");
		try {
			return querySvc.set(dto, user);
		} catch (QueryException e) {
			throw new ActividadIcaException(e.getMensage(), e);
		}
	}

	public void delete(ActividadIcaDTO dto, UsuarioDTO user) throws ActividadIcaException {
		if (dto == null)
			throw new ActividadIcaException("El objeto actividad ica se encuentra vacia.");
		if (dto.getCodigo() == null)
			throw new ActividadIcaException("El codigo actividad ica se encuentra vacio.");
		try {
			querySvc.del(dto, user);
		} catch (Exception e) {
			throw new ActividadIcaException(e.getMessage(), e);
		}

	}

}
