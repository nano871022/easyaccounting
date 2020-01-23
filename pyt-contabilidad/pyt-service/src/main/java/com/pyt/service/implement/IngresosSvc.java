package com.pyt.service.implement;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.pyt.common.annotations.Inject;
import org.pyt.common.exceptions.IngresoException;
import org.pyt.common.exceptions.QueryException;

import com.pyt.query.interfaces.IQuerySvc;
import com.pyt.service.abstracts.Services;
import com.pyt.service.dto.IngresoDTO;
import com.pyt.service.interfaces.IIngresosSvc;

import co.com.japl.ea.dto.system.UsuarioDTO;

public class IngresosSvc extends Services implements IIngresosSvc{
	@Inject(resource = "com.pyt.query.implement.QuerySvc")
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
			throw new IngresoException("El objeto ingreso se encuentra vacio.");
		if (StringUtils.isBlank(dto.getCodigo()))
			throw new IngresoException("El id de ingreso se encuentra vacia.");
		try {
			querySvc.set(dto, user);
		} catch (QueryException e) {
			throw new IngresoException(e.getMensage(), e);
		}
	}

	public IngresoDTO insert(IngresoDTO dto, UsuarioDTO user) throws IngresoException {
		if (dto == null)
			throw new IngresoException("El objeto ingreso se encuentra vacio.");
		if (StringUtils.isNotBlank(dto.getCodigo()))
			throw new IngresoException("El codigo de ingreso no se encuentra vacio.");
		try {
			return querySvc.set(dto, user);
		} catch (QueryException e) {
			throw new IngresoException(e.getMensage(), e);
		}
	}

	public void delete(IngresoDTO dto, UsuarioDTO user) throws IngresoException {
		if (dto == null)
			throw new IngresoException("El objeto ingreso se encuentra vacio.");
		if (StringUtils.isBlank(dto.getCodigo() ))
			throw new IngresoException("El codigo ingreso se encuentra vacio.");
		try {
			querySvc.del(dto, user);
		} catch (Exception e) {
			throw new IngresoException(e.getMessage(), e);
		}

	}

	@Override
	public Integer getTotalRow(IngresoDTO dto) throws IngresoException {
		if(dto == null)throw new IngresoException("El dto suministrado se encuentra vacio.");
		Integer count = 0;
		try {
			count = querySvc.countRow(dto);
		} catch (QueryException e) {
			throw new IngresoException("Se presento error en la obtencion de registros.",e);
		}
		return count;
	}

}
