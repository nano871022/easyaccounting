package com.pyt.service.implement;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.pyt.common.annotations.Inject;
import org.pyt.common.common.UsuarioDTO;
import org.pyt.common.exceptions.CuentaContableException;
import org.pyt.common.exceptions.QueryException;

import com.pyt.query.interfaces.IQuerySvc;
import com.pyt.service.abstracts.Services;
import com.pyt.service.dto.CuentaContableDTO;
import com.pyt.service.interfaces.ICuentaContableSvc;

public class CuentaContableSvc extends Services implements ICuentaContableSvc {
	@Inject(resource = "com.pyt.query.implement.QuerySvc")
	private IQuerySvc querySvc;

	@Override
	public List<CuentaContableDTO> getCuentaContables(CuentaContableDTO dto) throws CuentaContableException {
		if (dto == null)
			throw new CuentaContableException("No se suministro la cuenta contable.");
		try {
			return querySvc.gets(dto);
		} catch (QueryException e) {
			throw new CuentaContableException(e);
		}
	}

	@Override
	public List<CuentaContableDTO> getCuentaContables(CuentaContableDTO dto, Integer init, Integer rows)
			throws CuentaContableException {
		if (dto == null)
			throw new CuentaContableException("No se suministro la cuenta contable.");
		try {
			return querySvc.gets(dto, init, rows);
		} catch (QueryException e) {
			throw new CuentaContableException(e);
		}

	}

	@Override
	public CuentaContableDTO getCuentaContable(CuentaContableDTO dto) throws CuentaContableException {
		if (dto == null)
			throw new CuentaContableException("No se suministro la cuenta contable.");
		try {
			return querySvc.get(dto);
		} catch (QueryException e) {
			throw new CuentaContableException(e);
		}
	}

	@Override
	public CuentaContableDTO insert(CuentaContableDTO dto, UsuarioDTO user) throws CuentaContableException {
		if (user == null)
			throw new CuentaContableException("No se suministro el usuario.");
		if (dto == null)
			throw new CuentaContableException("No se suministro la cuenta contable.");
		if (StringUtils.isNotBlank(dto.getCodigo()))
			throw new CuentaContableException("El codigo de la cuenta contable existe.");
		try {
			return querySvc.set(dto, user);
		} catch (QueryException e) {
			throw new CuentaContableException(e);
		}
	}

	@Override
	public void update(CuentaContableDTO dto, UsuarioDTO user) throws CuentaContableException {
		if (user == null)
			throw new CuentaContableException("No se suministro el usuario.");
		if (dto == null)
			throw new CuentaContableException("No se suministro la cuenta contable.");
		if (StringUtils.isBlank(dto.getCodigo()))
			throw new CuentaContableException("El codigo de la cuenta contable no existe.");
		try {
			querySvc.set(dto, user);
		} catch (QueryException e) {
			throw new CuentaContableException(e);
		}
	}

	@Override
	public void delete(CuentaContableDTO dto, UsuarioDTO user) throws CuentaContableException {
		if (user == null)
			throw new CuentaContableException("No se suministro el usuario.");
		if (dto == null)
			throw new CuentaContableException("No se suministro la cuenta contable.");
		if (StringUtils.isBlank(dto.getCodigo()))
			throw new CuentaContableException("El codigo de la cuenta contable no existe.");
		try {
			querySvc.del(dto, user);
		} catch (QueryException e) {
			throw new CuentaContableException(e);
		}
	}

	@Override
	public Integer getTotalRows(CuentaContableDTO dto) throws CuentaContableException {
		if (dto == null)
			throw new CuentaContableException("No se suministro la cuenta contable.");
		try {
			return querySvc.countRow(dto);
		} catch (QueryException e) {
			throw new CuentaContableException(e);
		}
	}

}
