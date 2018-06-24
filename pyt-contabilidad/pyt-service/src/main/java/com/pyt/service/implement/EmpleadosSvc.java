package com.pyt.service.implement;

import java.util.ArrayList;
import java.util.List;

import org.pyt.common.common.UsuarioDTO;
import org.pyt.common.exceptions.EmpleadoException;
import org.pyt.common.exceptions.QueryException;

import com.pyt.query.interfaces.IQuerySvc;
import com.pyt.service.dto.PersonaDTO;
import com.pyt.service.dto.TrabajadorDTO;
import com.pyt.service.interfaces.IEmpleadosSvc;

public class EmpleadosSvc implements IEmpleadosSvc {

	private IQuerySvc querySvc;

	public List<TrabajadorDTO> getAllTrabajadores(TrabajadorDTO dto) throws EmpleadoException {
		List<TrabajadorDTO> lista = new ArrayList<TrabajadorDTO>();
		if (dto == null)
			throw new EmpleadoException("El objeto trabajaro esta vacio.");
		try {
			lista = querySvc.gets(dto);
		} catch (QueryException e) {
			throw new EmpleadoException(e.getMensage(), e);
		}
		return lista;
	}

	public List<TrabajadorDTO> getTrabajadores(TrabajadorDTO dto, Integer init, Integer end) throws EmpleadoException {
		List<TrabajadorDTO> lista = new ArrayList<TrabajadorDTO>();
		if (dto == null)
			throw new EmpleadoException("El objeto trabajaro esta vacio.");
		try {
			lista = querySvc.gets(dto, init, end);
		} catch (QueryException e) {
			throw new EmpleadoException(e.getMensage(), e);
		}
		return lista;
	}

	public TrabajadorDTO getTrabajador(TrabajadorDTO dto) throws EmpleadoException {
		if (dto == null)
			throw new EmpleadoException("El objeto trabajaro esta vacio.");
		try {
			return querySvc.get(dto);
		} catch (QueryException e) {
			throw new EmpleadoException(e.getMensage(), e);
		}
	}

	public void update(TrabajadorDTO dto, UsuarioDTO user) throws EmpleadoException {
		if (dto == null)
			throw new EmpleadoException("El objeto trabajaro esta vacio.");
		if (dto.getCodigo() == null)
			throw new EmpleadoException("El codigo del trabajador se encuentra vacio.");
		try {
			querySvc.set(dto, user);
		} catch (QueryException e) {
			throw new EmpleadoException(e.getMensage(), e);
		}
	}

	public void delete(TrabajadorDTO dto, UsuarioDTO user) throws EmpleadoException {
		if (dto == null)
			throw new EmpleadoException("El objeto trabajaro esta vacio.");
		if (dto.getCodigo() == null)
			throw new EmpleadoException("El codigo del trabajador se encuentra vacio.");
		try {
			querySvc.del(dto, user);
		} catch (QueryException e) {
			throw new EmpleadoException(e.getMensage(), e);
		}
	}

	public TrabajadorDTO insert(TrabajadorDTO dto, UsuarioDTO user) throws EmpleadoException {
		if (dto == null)
			throw new EmpleadoException("El objeto trabajaro esta vacio.");
		if (dto.getCodigo() != null)
			throw new EmpleadoException("El codigo del trabajador no se encuentra vacio.");
		try {
			return querySvc.set(dto, user);
		} catch (QueryException e) {
			throw new EmpleadoException(e.getMensage(), e);
		}
	}

	public List<PersonaDTO> getAllPersonas(PersonaDTO dto) throws EmpleadoException {
		List<PersonaDTO> lista = new ArrayList<PersonaDTO>();
		if (dto == null)
			throw new EmpleadoException("El objeto Persona esta vacio.");
		try {
			lista = querySvc.gets(dto);
		} catch (QueryException e) {
			throw new EmpleadoException(e.getMensage(), e);
		}
		return lista;
	}

	public List<PersonaDTO> getPersona(PersonaDTO dto, Integer init, Integer end) throws EmpleadoException {
		List<PersonaDTO> lista = new ArrayList<PersonaDTO>();
		if (dto == null)
			throw new EmpleadoException("El objeto Persona esta vacio.");
		try {
			lista = querySvc.gets(dto, init, end);
		} catch (QueryException e) {
			throw new EmpleadoException(e.getMensage(), e);
		}
		return lista;
	}

	public PersonaDTO getPersona(PersonaDTO dto) throws EmpleadoException {
		if (dto == null)
			throw new EmpleadoException("El objeto Persona esta vacio.");
		try {
			return querySvc.get(dto);
		} catch (QueryException e) {
			throw new EmpleadoException(e.getMensage(), e);
		}
	}

	public void update(PersonaDTO dto, UsuarioDTO user) throws EmpleadoException {
		if (dto == null)
			throw new EmpleadoException("El objeto Persona esta vacio.");
		if (dto.getCodigo() == null)
			throw new EmpleadoException("El codigo del persona se encuentra vacio.");
		try {
			querySvc.set(dto, user);
		} catch (QueryException e) {
			throw new EmpleadoException(e.getMensage(), e);
		}
	}

	public PersonaDTO insert(PersonaDTO dto, UsuarioDTO user) throws EmpleadoException {
		if (dto == null)
			throw new EmpleadoException("El objeto Persona esta vacio.");
		if (dto.getCodigo() != null)
			throw new EmpleadoException("El codigo del persona se encuentra vacio.");
		try {
			return querySvc.set(dto, user);
		} catch (QueryException e) {
			throw new EmpleadoException(e.getMensage(), e);
		}
	}

	public void delete(PersonaDTO dto, UsuarioDTO user) throws EmpleadoException {
		if (dto == null)
			throw new EmpleadoException("El objeto Persona esta vacio.");
		if (dto.getCodigo() == null)
			throw new EmpleadoException("El codigo del persona se encuentra vacio.");
		try {
			querySvc.del(dto, user);
		} catch (QueryException e) {
			throw new EmpleadoException(e.getMensage(), e);
		}
	}

	@Override
	public Integer getTotalRows(TrabajadorDTO dto) throws EmpleadoException {
		if (dto == null)
			throw new EmpleadoException("EL objeto Trabajador esta vacio");
		try {
			return querySvc.countRow(dto);
		} catch (QueryException e) {
			throw new EmpleadoException(e.getMensage(), e);
		}
	}

}
