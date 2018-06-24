package com.pyt.service.implement;

import java.util.ArrayList;
import java.util.List;

import org.pyt.common.common.UsuarioDTO;
import org.pyt.common.exceptions.IngresoException;
import org.pyt.common.exceptions.QueryException;

import com.pyt.query.interfaces.IQuerySvc;
import com.pyt.service.dto.IngresoDTO;
import com.pyt.service.interfaces.IIngresosSvc;

public class IngresosSvc implements IIngresosSvc{

	private IQuerySvc querySvc;

	public List<IngresoDTO> getIngresos(IngresoDTO dto, Integer init, Integer end)
			throws IngresoException {
		List<IngresoDTO> lista = new ArrayList<IngresoDTO>();
		if (dto == null)
			throw new IngresoException("El objeto empresa se encuentra vacio.");
		try {
			lista = querySvc.gets(dto, init, end);
		} catch (QueryException e) {
			throw new IngresoException(e.getMensage(), e);
		}
		return lista;
	}

	public List<IngresoDTO> getAllIngresos(IngresoDTO dto) throws IngresoException {
		List<IngresoDTO> lista = new ArrayList<IngresoDTO>();
		if (dto == null)
			throw new IngresoException("El objeto empresa se encuentra vacio.");
		try {
			lista = querySvc.gets(dto);
		} catch (QueryException e) {
			throw new IngresoException(e.getMensage(), e);
		}
		return lista;
	}

	public IngresoDTO getIngreso(IngresoDTO dto) throws IngresoException {
		if (dto == null)
			throw new IngresoException("El objeto empresa se encuentra vacio.");
		try {
			return querySvc.get(dto);
		} catch (QueryException e) {
			throw new IngresoException(e.getMensage(), e);
		}
	}

	public void update(IngresoDTO dto, UsuarioDTO user) throws IngresoException {
		if (dto == null)
			throw new IngresoException("El objeto empresa se encuentra vacio.");
		if (dto.getCodigo() == null)
			throw new IngresoException("El id de empresa se encuentra vacia.");
		try {
			querySvc.set(dto, user);
		} catch (QueryException e) {
			throw new IngresoException(e.getMensage(), e);
		}
	}

	public IngresoDTO insert(IngresoDTO dto, UsuarioDTO user) throws IngresoException {
		if (dto == null)
			throw new IngresoException("El objeto empresa se encuentra vacio.");
		if (dto.getCodigo() != null)
			throw new IngresoException("El codigo de empresa no se encuentra vacio.");
		try {
			return querySvc.set(dto, user);
		} catch (QueryException e) {
			throw new IngresoException(e.getMensage(), e);
		}
	}

	public void delete(IngresoDTO dto, UsuarioDTO user) throws IngresoException {
		if (dto == null)
			throw new IngresoException("El objeto empresa se encuentra vacio.");
		if (dto.getCodigo() == null)
			throw new IngresoException("El codigo empresa se encuentra vacio.");
		try {
			querySvc.del(dto, user);
		} catch (Exception e) {
			throw new IngresoException(e.getMessage(), e);
		}

	}

}
