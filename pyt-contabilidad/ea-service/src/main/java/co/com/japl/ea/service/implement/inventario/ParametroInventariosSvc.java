package com.pyt.service.implement.inventario;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.pyt.common.annotations.Inject;
import org.pyt.common.exceptions.ParametroException;
import org.pyt.common.exceptions.QueryException;

import co.com.japl.ea.query.interfaces.IQuerySvc;
import com.pyt.service.abstracts.Services;
import com.pyt.service.dto.inventario.ParametroGrupoInventarioDTO;
import com.pyt.service.dto.inventario.ParametroInventarioDTO;
import com.pyt.service.interfaces.inventarios.IParametroInventariosSvc;

import co.com.japl.ea.dto.system.UsuarioDTO;

public class ParametroInventariosSvc extends Services implements IParametroInventariosSvc {
	@Inject(resource = "co.com.japl.ea.query.implement.QuerySvc")
	protected IQuerySvc querySvc;

	public List<ParametroInventarioDTO> getParametros(ParametroInventarioDTO dto, Integer init, Integer end)
			throws ParametroException {
		List<ParametroInventarioDTO> lista = new ArrayList<ParametroInventarioDTO>();
		List<ParametroInventarioDTO> lista2 = new ArrayList<ParametroInventarioDTO>();
		if (dto == null)
			throw new ParametroException("El objeto empresa se encuentra vacio.");
		try {
			lista = querySvc.gets(dto, init, end);
			if (dto.getGrupo().equalsIgnoreCase("*")) {
				for (ParametroInventarioDTO dt : lista) {
					if (dt.getGrupo() != null && dt.getGrupo().equalsIgnoreCase("*")) {
						lista2.add(dt);
					}
				}
				if (lista2.size() > 0)
					return lista2;
			}
		} catch (QueryException e) {
			throw new ParametroException(e.getMensage(), e);
		}
		return lista;
	}

	public List<ParametroInventarioDTO> getAllParametros(ParametroInventarioDTO dto) throws ParametroException {
		List<ParametroInventarioDTO> lista = new ArrayList<ParametroInventarioDTO>();
		if (dto == null)
			throw new ParametroException("El objeto empresa se encuentra vacio.");
		try {
			lista = querySvc.gets(dto);
		} catch (QueryException e) {
			throw new ParametroException(e.getMensage(), e);
		}
		return lista;
	}

	public ParametroInventarioDTO getParametro(ParametroInventarioDTO dto) throws ParametroException {
		if (dto == null)
			throw new ParametroException("El objeto empresa se encuentra vacio.");
		try {
			return querySvc.get(dto);
		} catch (QueryException e) {
			throw new ParametroException(e.getMensage(), e);
		}
	}

	public void update(ParametroInventarioDTO dto, UsuarioDTO user) throws ParametroException {
		if (dto == null)
			throw new ParametroException("El objeto parametro se encuentra vacio.");
		if (StringUtils.isBlank(dto.getCodigo()))
			throw new ParametroException("El id de parametro se encuentra vacia.");
		try {
			querySvc.set(dto, user);
		} catch (QueryException e) {
			throw new ParametroException(e.getMensage(), e);
		}
	}

	public ParametroInventarioDTO insert(ParametroInventarioDTO dto, UsuarioDTO user) throws ParametroException {
		if (dto == null)
			throw new ParametroException("El objeto empresa se encuentra vacio.");
		if (dto.getCodigo() != null)
			throw new ParametroException("El codigo de empresa no se encuentra vacio.");
		try {
			return querySvc.set(dto, user);
		} catch (QueryException e) {
			throw new ParametroException(e.getMensage(), e);
		}
	}

	public void delete(ParametroInventarioDTO dto, UsuarioDTO user) throws ParametroException {
		if (dto == null)
			throw new ParametroException("El objeto empresa se encuentra vacio.");
		if (dto.getCodigo() == null)
			throw new ParametroException("El codigo empresa se encuentra vacio.");
		try {
			querySvc.del(dto, user);
		} catch (Exception e) {
			throw new ParametroException(e.getMessage(), e);
		}

	}

	@Override
	public Integer totalCount(ParametroInventarioDTO dto) throws ParametroException {
		if (dto == null)
			throw new ParametroException("No se suministro el dto para realizar filtro");
		try {
			return querySvc.countRow(dto);
		} catch (QueryException e) {
			throw new ParametroException("Se presento problema en el conteo de registro de parametros.", e);
		}
	}

	@Override
	public List<ParametroGrupoInventarioDTO> getParametroGrupo(ParametroGrupoInventarioDTO dto)
			throws ParametroException {
		List<ParametroGrupoInventarioDTO> lista = new ArrayList<ParametroGrupoInventarioDTO>();
		if (dto == null)
			throw new ParametroException("No se suministro el parametro grupo para realizar el filtro.");
		try {
			lista = querySvc.gets(dto);
		} catch (QueryException e) {
			throw new ParametroException("No se logro obtner los grupos asociados a parametros.", e);
		}
		return lista;
	}

	@Override
	public ParametroGrupoInventarioDTO insert(ParametroGrupoInventarioDTO dto, UsuarioDTO user)
			throws ParametroException {
		if (dto == null)
			throw new ParametroException("Se encontro el parameto grupo vacio.");
		if (user == null)
			throw new ParametroException("Se encontro el usuario vacio.");
		try {
			dto = querySvc.set(dto, user);
		} catch (QueryException e) {
			throw new ParametroException("Se presento un problema en el ingreso del registro.");
		}
		return dto;
	}

	@Override
	public void update(ParametroGrupoInventarioDTO dto, UsuarioDTO user) throws ParametroException {
		if (dto == null)
			throw new ParametroException("Se encontro el parameto grupo vacio.");
		if (user == null)
			throw new ParametroException("Se encontro el usuario vacio.");
		try {
			querySvc.set(dto, user);
		} catch (QueryException e) {
			throw new ParametroException("Se presento un problema en la actualizacion del registro.");
		}
	}

	@Override
	public void delete(ParametroGrupoInventarioDTO dto, UsuarioDTO user) throws ParametroException {
		if (dto == null)
			throw new ParametroException("Se encontro el parameto grupo vacio.");
		if (user == null)
			throw new ParametroException("Se encontro el usuario vacio.");
		try {
			querySvc.del(dto, user);
		} catch (QueryException e) {
			throw new ParametroException("Se presento un problema en la eliminacion del registro.");
		}
	}

	@Override
	public List<ParametroInventarioDTO> getAllParametros(ParametroInventarioDTO dto, String grupo)
			throws ParametroException {
		if (dto == null) {
			throw new ParametroException("No se suministro el parametro para aplicar el filtro de busqueda.");
		}
		ParametroGrupoInventarioDTO pgrupo = new ParametroGrupoInventarioDTO();
		if (StringUtils.isNotBlank(grupo)) {
			pgrupo.setGrupo(grupo);
			List<ParametroGrupoInventarioDTO> list = getParametroGrupo(pgrupo);
			if (list != null && list.size() == 1) {
				pgrupo = list.get(0);
			}
		}
		if (pgrupo != null && StringUtils.isNotBlank(pgrupo.getCodigo())) {
			dto.setGrupo(pgrupo.getParametro());
		} else {
			dto.setGrupo(grupo);
		}
		return getAllParametros(dto);
	}

}
