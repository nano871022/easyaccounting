package com.pyt.service.implement;

import java.util.List;

import org.pyt.common.common.UsuarioDTO;
import org.pyt.common.exceptions.CuentaContableException;

import com.pyt.service.dto.CuentaContableDTO;
import com.pyt.service.interfaces.ICuentaContableSvc;

public class CuentaContableSvc implements ICuentaContableSvc {

	@Override
	public List<CuentaContableDTO> getCuentaContables(CuentaContableDTO dto) throws CuentaContableException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<CuentaContableDTO> getCuentaContables(CuentaContableDTO dto, Integer init, Integer rows)
			throws CuentaContableException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public CuentaContableDTO getCuentaContable(CuentaContableDTO dto) throws CuentaContableException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public CuentaContableDTO insert(CuentaContableDTO dto, UsuarioDTO user) throws CuentaContableException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void update(CuentaContableDTO dto, UsuarioDTO user) throws CuentaContableException {
		// TODO Auto-generated method stub

	}

	@Override
	public void delete(CuentaContableDTO dto, UsuarioDTO user) throws CuentaContableException {
		// TODO Auto-generated method stub

	}

	@Override
	public Integer getTotalRows(CuentaContableDTO dto) throws CuentaContableException {
		return 0;
	}

}
