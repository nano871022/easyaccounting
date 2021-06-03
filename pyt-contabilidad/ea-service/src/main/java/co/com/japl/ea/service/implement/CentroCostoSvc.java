package com.pyt.service.implement;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import org.apache.commons.lang3.StringUtils;
import org.pyt.common.annotations.Inject;
import org.pyt.common.common.DtoUtils;
import org.pyt.common.common.ListUtils;
import org.pyt.common.exceptions.CentroCostosException;
import org.pyt.common.exceptions.QueryException;

import co.com.japl.ea.query.interfaces.IQuerySvc;
import com.pyt.service.abstracts.Services;
import com.pyt.service.dto.CentroCostoDTO;
import com.pyt.service.interfaces.ICentroCostosSvc;

import co.com.arquitectura.annotation.proccessor.Services.Type;
import co.com.arquitectura.annotation.proccessor.Services.kind;
import co.com.arquitectura.annotation.proccessor.Services.scope;
import co.com.japl.ea.dto.system.UsuarioDTO;

public class CentroCostoSvc extends Services implements ICentroCostosSvc {
	@Inject(resource = "co.com.japl.ea.query.implement.QuerySvc")
	private IQuerySvc querySvc;

	public List<CentroCostoDTO> getCentroCostos(CentroCostoDTO dto, Integer init, Integer end)
			throws CentroCostosException {
		List<CentroCostoDTO> lista = new ArrayList<CentroCostoDTO>();
		if (dto == null)
			throw new CentroCostosException("El objeto centro de costo se encuentra vacio.");
		try {
			lista = querySvc.gets(dto, init, end);
		} catch (QueryException e) {
			throw new CentroCostosException(e.getMensage(), e);
		}
		return lista;
	}

	public List<CentroCostoDTO> getAllCentroCostos(CentroCostoDTO dto) throws CentroCostosException {
		List<CentroCostoDTO> lista = new ArrayList<CentroCostoDTO>();
		if (dto == null)
			throw new CentroCostosException("El objeto centro de costo se encuentra vacio.");
		try {
			lista = querySvc.gets(dto);
		} catch (QueryException e) {
			throw new CentroCostosException(e.getMensage(), e);
		}
		return lista;
	}

	public CentroCostoDTO getCentroCosto(CentroCostoDTO dto) throws CentroCostosException {
		if (dto == null)
			throw new CentroCostosException("El objeto centro de costo se encuentra vacio.");
		try {
			return querySvc.get(dto);
		} catch (QueryException e) {
			throw new CentroCostosException(e.getMensage(), e);
		}
	}

	public void update(CentroCostoDTO dto, UsuarioDTO user) throws CentroCostosException {
		if (dto == null)
			throw new CentroCostosException("El objeto centro de costo se encuentra vacio.");
		if (dto.getCodigo() == null)
			throw new CentroCostosException("El id de centro de costo se encuentra vacia.");
		try {
			querySvc.set(dto, user);
		} catch (QueryException e) {
			throw new CentroCostosException(e.getMensage(), e);
		}
	}

	public CentroCostoDTO insert(CentroCostoDTO dto, UsuarioDTO user) throws CentroCostosException {
		if (dto == null)
			throw new CentroCostosException("El objeto centro de costo se encuentra vacio.");
		if (StringUtils.isNotBlank(dto.getCodigo()))
			throw new CentroCostosException("El codigo de centro de costo no se encuentra vacio.");
		try {
			return querySvc.set(dto, user);
		} catch (QueryException e) {
			throw new CentroCostosException(e.getMensage(), e);
		}
	}

	public void delete(CentroCostoDTO dto, UsuarioDTO user) throws CentroCostosException {
		if (dto == null)
			throw new CentroCostosException("El objeto centro de costo se encuentra vacio.");
		if (dto.getCodigo() == null)
			throw new CentroCostosException("El codigo centro de costo se encuentra vacio.");
		try {
			querySvc.del(dto, user);
		} catch (Exception e) {
			throw new CentroCostosException(e.getMessage(), e);
		}

	}

	@Override
	public Integer getTotalRows(CentroCostoDTO dto) throws CentroCostosException {
		if(dto == null) {
			throw new CentroCostosException("El objeto centro de costo se encuentra vacio.");
		}
		try {
			return querySvc.countRow(dto);
		} catch (QueryException e) {
			throw new CentroCostosException(e.getMensage(), e);
		}
	}

	@co.com.arquitectura.annotation.proccessor.Services(alcance = scope.EJB, alias = "Ingreso Centro de Costos", descripcion = "Ingreso de servicios de centro de costos", tipo = kind.PUBLIC, type = Type.CREATE)
	public CentroCostoDTO insertService(CentroCostoDTO dto, UsuarioDTO user) throws CentroCostosException {
		if (DtoUtils.isBlank(dto)) {
			throw new CentroCostosException(i18n().get("err.costcenter.is.invalid"));
		}
		if (!DtoUtils.haveCode(user)) {
			throw new CentroCostosException(i18n().get("err.user.is.invalid"));
		}
		try {
			// VERIFICACION DEL CAMPO EMPRESA BUSCADO POR EL NIT
			if (DtoUtils.isNotBlankFields(dto.getEmpresa(), "nit")) {
				var enterprises = querySvc.gets(dto.getEmpresa());
				if (ListUtils.isNotBlank(enterprises)) {
					if (ListUtils.haveOneItem(enterprises)) {
						dto.setEmpresa(enterprises.get(0));
					} else {
						throw new CentroCostosException(i18n().get("err.enterprise.have.been.found.alotof.rows"));
					}
				} else {
					throw new CentroCostosException(i18n().get("err.enterprise.wasnt.found"));
				}
			} else {
				throw new CentroCostosException(i18n().get("err.enterprise.is.empty"));
			}
			// VERIFICACION DEL CAMPO ESTADO VALIDA QUE EL VALOR SUMINISTRADO SEA ALGUNO DE
			// LOS PERMITIDOS Y ADEMAS PONE EL VALOR CORRECTO
			var list = Arrays.asList("S", "N", "1", "0");
			if (StringUtils.isNotBlank(dto.getEstado())) {
				Stream<String> foundState = list.stream().filter(row -> row.contentEquals(dto.getEstado()));
				if (foundState.count() == 0) {
					throw new CentroCostosException(i18n().get("err.status.wasnt.found"));
				} else if ("S".contentEquals(foundState.findFirst().get())) {
					dto.setEstado("1");
				} else if ("N".contentEquals(foundState.findFirst().get())) {
					dto.setEstado("0");
				}
			} else if (StringUtils.isBlank(dto.getEstado())) {
				throw new CentroCostosException(i18n().get("err.status.is.empty"));
			}
			// VERIFICACION DEL CAMPO ORDEN, SI NO LO TIENE BUSCA LA CANTIDAD DE REGISTROS Y
			// LE SUMA 1
			if (dto.getOrden() == null) {
				var cc = new CentroCostoDTO();
				cc.setEstado("1");
				cc.setEmpresa(dto.getEmpresa());
				var found = querySvc.countRow(cc);
				if (found != null) {
					dto.setOrden(found + 1);
				} else {
					dto.setOrden(0);
				}
			}
			if (querySvc.get(dto) != null && querySvc.insert(dto, user)) {
				return dto;
			} else {
				throw new CentroCostosException(i18n().get("err.costcenter.was.saved"));
			}
		} catch (QueryException e) {
			throw new CentroCostosException(i18n().get("err.costcenter.have.problems"), e);
		}
	}
}
