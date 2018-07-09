package com.pyt.service.implement;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.pyt.common.annotations.Inject;
import org.pyt.common.common.UsuarioDTO;
import org.pyt.common.exceptions.ParametroException;
import org.pyt.common.exceptions.QueryException;

import com.pyt.query.interfaces.IQuerySvc;
import com.pyt.service.abstracts.Services;
import com.pyt.service.dto.ParametroDTO;
import com.pyt.service.dto.ParametroGrupoDTO;
import com.pyt.service.interfaces.IParametrosSvc;

public class ParametrosSvc extends Services implements IParametrosSvc {
	@Inject(resource = "com.pyt.query.implement.QuerySvc")
	protected IQuerySvc querySvc;

	public List<ParametroDTO> getParametros(ParametroDTO dto, Integer init, Integer end) throws ParametroException {
		List<ParametroDTO> lista = new ArrayList<ParametroDTO>();
		List<ParametroDTO> lista2 = new ArrayList<ParametroDTO>();
		if (dto == null)
			throw new ParametroException("El objeto empresa se encuentra vacio.");
		try {
			lista = querySvc.gets(dto, init, end);
			if(dto.getGrupo().equalsIgnoreCase("*")) {
				for(ParametroDTO dt : lista) {
					if(dt.getGrupo() != null && dt.getGrupo().equalsIgnoreCase("*")) {
						lista2.add(dt);
					}
				}
				if(lista2.size() > 0)return lista2;
			}
		} catch (QueryException e) {
			throw new ParametroException(e.getMensage(), e);
		}
		return lista;
	}

	public List<ParametroDTO> getAllParametros(ParametroDTO dto) throws ParametroException {
		List<ParametroDTO> lista = new ArrayList<ParametroDTO>();
		if (dto == null)
			throw new ParametroException("El objeto empresa se encuentra vacio.");
		try {
			lista = querySvc.gets(dto);
		} catch (QueryException e) {
			throw new ParametroException(e.getMensage(), e);
		}
		return lista;
	}

	public ParametroDTO getParametro(ParametroDTO dto) throws ParametroException {
		if (dto == null)
			throw new ParametroException("El objeto empresa se encuentra vacio.");
		try {
			return querySvc.get(dto);
		} catch (QueryException e) {
			throw new ParametroException(e.getMensage(), e);
		}
	}

	public void update(ParametroDTO dto, UsuarioDTO user) throws ParametroException {
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

	public ParametroDTO insert(ParametroDTO dto, UsuarioDTO user) throws ParametroException {
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

	public void delete(ParametroDTO dto, UsuarioDTO user) throws ParametroException {
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
	public Integer totalCount(ParametroDTO dto) throws ParametroException {
		if (dto == null)
			throw new ParametroException("No se suministro el dto para realizar filtro");
		try {
			return querySvc.countRow(dto);
		} catch (QueryException e) {
			throw new ParametroException("Se presento problema en el conteo de registro de parametros.", e);
		}
	}

	@Override
	public List<ParametroGrupoDTO> getParametroGrupo(ParametroGrupoDTO dto) throws ParametroException {
		List<ParametroGrupoDTO> lista = new ArrayList<ParametroGrupoDTO>();
		if(dto == null)throw new ParametroException("No se suministro el parametro grupo para realizar el filtro.");
		try {
			lista = querySvc.gets(dto);
		} catch (QueryException e) {
			throw new ParametroException("No se logro obtner los grupos asociados a parametros.",e);
		}
		return lista;
	}

	@Override
	public ParametroGrupoDTO insert(ParametroGrupoDTO dto, UsuarioDTO user) throws ParametroException {
		if(dto == null)throw new ParametroException("Se encontro el parameto grupo vacio.");
		if(user == null)throw new ParametroException("Se encontro el usuario vacio.");
		try {
			dto = querySvc.set(dto, user);
		} catch (QueryException e) {
			throw new ParametroException("Se presento un problema en el ingreso del registro.");
		}
		return dto;
	}

	@Override
	public void update(ParametroGrupoDTO dto, UsuarioDTO user) throws ParametroException {
		if(dto == null)throw new ParametroException("Se encontro el parameto grupo vacio.");
		if(user == null)throw new ParametroException("Se encontro el usuario vacio.");
		try {
			querySvc.set(dto, user);
		} catch (QueryException e) {
			throw new ParametroException("Se presento un problema en la actualizacion del registro.");
		}
	}

	@Override
	public void delete(ParametroGrupoDTO dto, UsuarioDTO user) throws ParametroException {
		if(dto == null)throw new ParametroException("Se encontro el parameto grupo vacio.");
		if(user == null)throw new ParametroException("Se encontro el usuario vacio.");
		try {
			querySvc.del(dto, user);
		} catch (QueryException e) {
			throw new ParametroException("Se presento un problema en la eliminacion del registro.");
		}
	}

	@Override
	public List<ParametroDTO> getAllParametros(ParametroDTO dto, String grupo) throws ParametroException {
		if(dto == null) {
			throw new ParametroException("No se suministro el parametro para aplicar el filtro de busqueda.");
		}
		ParametroGrupoDTO pgrupo = new ParametroGrupoDTO();
		if(StringUtils.isNotBlank(grupo)) {
			pgrupo.setGrupo(grupo);
			List<ParametroGrupoDTO> list = getParametroGrupo(pgrupo);
			if(list != null && list.size() == 1) {
				pgrupo = list.get(0);
			}
		}
		if(pgrupo != null && StringUtils.isNotBlank(pgrupo.getCodigo())) {
			dto.setGrupo(pgrupo.getParametro());
		}else {
			dto.setGrupo(grupo);
		}
		return getAllParametros(dto);
	}

}
