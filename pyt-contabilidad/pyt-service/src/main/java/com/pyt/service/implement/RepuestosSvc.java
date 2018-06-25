package com.pyt.service.implement;

import java.util.ArrayList;
import java.util.List;

import org.pyt.common.annotations.Inject;
import org.pyt.common.common.UsuarioDTO;
import org.pyt.common.exceptions.QueryException;
import org.pyt.common.exceptions.RepuestoException;

import com.pyt.query.interfaces.IQuerySvc;
import com.pyt.service.abstracts.Services;
import com.pyt.service.dto.RepuestoDTO;
import com.pyt.service.interfaces.IRepuestosSvc;

public class RepuestosSvc extends Services implements IRepuestosSvc{
	@Inject(resource = "com.pyt.query.implement.QuerySvc")
	private IQuerySvc querySvc;

	public List<RepuestoDTO> getRepuestos(RepuestoDTO dto, Integer init, Integer end)
			throws RepuestoException {
		List<RepuestoDTO> lista = new ArrayList<RepuestoDTO>();
		if (dto == null)
			throw new RepuestoException("El objeto repuesta se encuentra vacio.");
		try {
			lista = querySvc.gets(dto, init, end);
		} catch (QueryException e) {
			throw new RepuestoException(e.getMensage(), e);
		}
		return lista;
	}

	public List<RepuestoDTO> getAllRepuestos(RepuestoDTO dto) throws RepuestoException {
		List<RepuestoDTO> lista = new ArrayList<RepuestoDTO>();
		if (dto == null)
			throw new RepuestoException("El objeto repuesto se encuentra vacio.");
		try {
			lista = querySvc.gets(dto);
		} catch (QueryException e) {
			throw new RepuestoException(e.getMensage(), e);
		}
		return lista;
	}

	public RepuestoDTO getRepuesto(RepuestoDTO dto) throws RepuestoException {
		if (dto == null)
			throw new RepuestoException("El objeto repuesto  se encuentra vacio.");
		try {
			return querySvc.get(dto);
		} catch (QueryException e) {
			throw new RepuestoException(e.getMensage(), e);
		}
	}

	public void update(RepuestoDTO dto, UsuarioDTO user) throws RepuestoException {
		if (dto == null)
			throw new RepuestoException("El objeto repuesto se encuentra vacio.");
		if (dto.getCodigo() == null)
			throw new RepuestoException("El id de repuesto se encuentra vacia.");
		try {
			querySvc.set(dto, user);
		} catch (QueryException e) {
			throw new RepuestoException(e.getMensage(), e);
		}
	}

	public RepuestoDTO insert(RepuestoDTO dto, UsuarioDTO user) throws RepuestoException {
		if (dto == null)
			throw new RepuestoException("El objeto repuesto se encuentra vacio.");
		if (dto.getCodigo() != null)
			throw new RepuestoException("El codigo de repuesto no se encuentra vacio.");
		try {
			return querySvc.set(dto, user);
		} catch (QueryException e) {
			throw new RepuestoException(e.getMensage(), e);
		}
	}

	public void delete(RepuestoDTO dto, UsuarioDTO user) throws RepuestoException {
		if (dto == null)
			throw new RepuestoException("El objeto repuesto se encuentra vacio.");
		if (dto.getCodigo() == null)
			throw new RepuestoException("El codigo repuesto se encuentra vacio.");
		try {
			querySvc.del(dto, user);
		} catch (Exception e) {
			throw new RepuestoException(e.getMessage(), e);
		}

	}

}
