package com.pyt.service.implement;

import java.util.ArrayList;
import java.util.List;

import org.pyt.common.annotations.Inject;
import org.pyt.common.annotations.PostConstructor;
import org.pyt.common.common.Log;
import org.pyt.common.common.UsuarioDTO;
import org.pyt.common.exceptions.EmpresasException;
import org.pyt.common.exceptions.QueryException;
import org.pyt.common.exceptions.ReflectionException;
import org.pyt.common.reflection.Reflection;

import com.pyt.query.interfaces.IQuerySvc;
import com.pyt.service.dto.EmpresaDTO;
import com.pyt.service.interfaces.IEmpresasSvc;

public class EmpresaSvc extends Reflection implements IEmpresasSvc {
	@Inject(resource = "com.pyt.query.implement.QuerySvc")
	private IQuerySvc querySvc;

	@PostConstructor
	public void load() {
		try {
			inject();
		} catch (ReflectionException e) {
			Log.logger(e);
		}
	}

	public List<EmpresaDTO> getEmpresas(EmpresaDTO dto, Integer init, Integer end) throws EmpresasException {
		List<EmpresaDTO> lista = new ArrayList<EmpresaDTO>();
		if (dto == null)
			throw new EmpresasException("El objeto empresa se encuentra vacio.");
		try {
			lista = querySvc.gets(dto, init, end);
		} catch (QueryException e) {
			throw new EmpresasException(e.getMensage(), e);
		}
		return lista;
	}

	public List<EmpresaDTO> getAllEmpresas(EmpresaDTO dto) throws EmpresasException {
		List<EmpresaDTO> lista = new ArrayList<EmpresaDTO>();
		if (dto == null)
			throw new EmpresasException("El objeto empresa se encuentra vacio.");
		try {
			lista = querySvc.gets(dto);
		} catch (QueryException e) {
			throw new EmpresasException(e.getMensage(), e);
		}
		return lista;
	}

	public EmpresaDTO getEmpresa(EmpresaDTO dto) throws EmpresasException {
		if (dto == null)
			throw new EmpresasException("El objeto empresa se encuentra vacio.");
		try {
			return querySvc.get(dto);
		} catch (QueryException e) {
			throw new EmpresasException(e.getMensage(), e);
		}
	}

	public void update(EmpresaDTO dto, UsuarioDTO user) throws EmpresasException {
		if (dto == null)
			throw new EmpresasException("El objeto empresa se encuentra vacio.");
		if (dto.getCodigo() == null)
			throw new EmpresasException("El id de empresa se encuentra vacia.");
		try {
			querySvc.set(dto, user);
		} catch (QueryException e) {
			throw new EmpresasException(e.getMensage(), e);
		}
	}

	public EmpresaDTO insert(EmpresaDTO dto, UsuarioDTO user) throws EmpresasException {
		if (dto == null)
			throw new EmpresasException("El objeto empresa se encuentra vacio.");
		if(user == null)
			throw new EmpresasException("El usuario no fue suinistrado.");
		if (dto.getCodigo() != null)
			throw new EmpresasException("El codigo de empresa no se encuentra vacio.");
		try {
			return querySvc.set(dto, user);
		} catch (QueryException e) {
			throw new EmpresasException(e.getMensage(), e);
		}
	}

	public void delete(EmpresaDTO dto, UsuarioDTO user) throws EmpresasException {
		if (dto == null)
			throw new EmpresasException("El objeto empresa se encuentra vacio.");
		if (dto.getCodigo() == null)
			throw new EmpresasException("El codigo empresa se encuentra vacio.");
		try {
			querySvc.del(dto, user);
		} catch (Exception e) {
			throw new EmpresasException(e.getMessage(), e);
		}

	}

	@Override
	public Integer getTotalRows(EmpresaDTO dto) throws EmpresasException {
		if (dto == null)
			throw new EmpresasException("El objeto empresa se encuentra vacio.");
		try {
			return querySvc.countRow(dto);
		} catch (QueryException e) {
			throw new EmpresasException(e.getMensage(), e);
		}
	}

}
