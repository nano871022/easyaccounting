package com.pyt.service.implement;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.pyt.common.annotations.Inject;
import org.pyt.common.common.UsuarioDTO;
import org.pyt.common.exceptions.QueryException;
import org.pyt.common.exceptions.ServiciosException;

import com.pyt.query.interfaces.IQuerySvc;
import com.pyt.service.abstracts.Services;
import com.pyt.service.dto.ServicioDTO;
import com.pyt.service.interfaces.IServiciosSvc;

public class ServiciosSvc extends Services implements IServiciosSvc {
	@Inject(resource = "com.pyt.query.implement.QuerySvc")
	private IQuerySvc querySvc;

	public List<ServicioDTO> getServicios(ServicioDTO dto, Integer init, Integer end) throws ServiciosException {
		List<ServicioDTO> lista = new ArrayList<ServicioDTO>();
		if (dto == null)
			throw new ServiciosException("El objeto servicios se encuentra vacio.");
		try {
			lista = querySvc.gets(dto, init, end);
		} catch (QueryException e) {
			throw new ServiciosException(e.getMensage(), e);
		}
		return lista;
	}

	public List<ServicioDTO> getAllServicios(ServicioDTO dto) throws ServiciosException {
		List<ServicioDTO> lista = new ArrayList<ServicioDTO>();
		if (dto == null)
			throw new ServiciosException("El objeto servicios se encuentra vacio.");
		try {
			lista = querySvc.gets(dto);
		} catch (QueryException e) {
			throw new ServiciosException(e.getMensage(), e);
		}
		return lista;
	}

	public ServicioDTO getServicio(ServicioDTO dto) throws ServiciosException {
		if (dto == null)
			throw new ServiciosException("El objeto servicio se encuentra vacio.");
		try {
			return querySvc.get(dto);
		} catch (QueryException e) {
			throw new ServiciosException(e.getMensage(), e);
		}
	}

	public void update(ServicioDTO dto, UsuarioDTO user) throws ServiciosException {
		if (dto == null)
			throw new ServiciosException("El objeto servicio se encuentra vacio.");
		if (StringUtils.isBlank(dto.getCodigo() ))
			throw new ServiciosException("El id de servicio se encuentra vacia.");
		try {
			querySvc.set(dto, user);
		} catch (QueryException e) {
			throw new ServiciosException(e.getMensage(), e);
		}
	}

	public ServicioDTO insert(ServicioDTO dto, UsuarioDTO user) throws ServiciosException {
		if (dto == null)
			throw new ServiciosException("El objeto servicio se encuentra vacio.");
		if (StringUtils.isNotBlank(dto.getCodigo()))
			throw new ServiciosException("El codigo de servicio no se encuentra vacio.");
		try {
			return querySvc.set(dto, user);
		} catch (QueryException e) {
			throw new ServiciosException(e.getMensage(), e);
		}
	}

	public void delete(ServicioDTO dto, UsuarioDTO user) throws ServiciosException {
		if (dto == null)
			throw new ServiciosException("El objeto servicio se encuentra vacio.");
		if (StringUtils.isBlank(dto.getCodigo()))
			throw new ServiciosException("El codigo servicio se encuentra vacio.");
		try {
			querySvc.del(dto, user);
		} catch (Exception e) {
			throw new ServiciosException(e.getMessage(), e);
		}

	}

	@Override
	public Integer getTotalRows(ServicioDTO dto) throws ServiciosException {
		if(dto == null)throw new ServiciosException("El servicio suministrado se encuentra vacio.");
		try {
			return querySvc.countRow(dto);
		} catch (QueryException e) {
			throw new ServiciosException(e.getMensage(), e);
		}
	}

}
