package co.com.japl.ea.service.implement;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.pyt.common.annotations.Inject;
import org.pyt.common.constants.ParametroConstants;

import co.com.japl.ea.query.interfaces.IQuerySvc;
import co.com.arquitectura.annotation.proccessor.Services.Type;
import co.com.arquitectura.annotation.proccessor.Services.kind;
import co.com.arquitectura.annotation.proccessor.Services.scope;
import co.com.japl.ea.dto.abstracts.Services;
import co.com.japl.ea.dto.dto.CuentaContableDTO;
import co.com.japl.ea.dto.dto.ParametroGrupoDTO;
import co.com.japl.ea.dto.interfaces.ICuentaContableSvc;
import co.com.japl.ea.dto.system.UsuarioDTO;
import co.com.japl.ea.exceptions.CuentaContableException;
import co.com.japl.ea.exceptions.QueryException;

public class CuentaContableSvc extends Services implements ICuentaContableSvc {
	@Inject(resource = "co.com.japl.ea.query.implement.QuerySvc")
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
		if (dto == null)
			throw new CuentaContableException("No se suministro la cuenta contable.");
		try {
			return querySvc.set(dto, user);
		} catch (QueryException e) {
			throw new CuentaContableException(e);
		}
	}

	@co.com.arquitectura.annotation.proccessor.Services(alcance = scope.EJB, alias = "Ingreso cuenta contable", descripcion = "Ingreso de servicios de cuenta contables", tipo = kind.PUBLIC, type = Type.CREATE)
	@Override
	public CuentaContableDTO insertService(CuentaContableDTO dto, UsuarioDTO user) throws CuentaContableException {
		if (user == null)
			throw new CuentaContableException("No se suministro el usuario.");
		if (dto == null)
			throw new CuentaContableException("No se suministro la cuenta contable.");
		if (StringUtils.isNotBlank(dto.getCodigo()))
			throw new CuentaContableException("El codigo de la cuenta contable existe.");
		try {
			if (dto.getAsociado() != null && StringUtils.isNotBlank(dto.getAsociado().getCodigoCuenta())) {
				dto.setAsociado(querySvc.get(dto.getAsociado()));
			}
			if (dto.getEmpresa() != null && StringUtils.isNotBlank(dto.getEmpresa().getNombre())) {
				dto.setEmpresa(querySvc.get(dto.getEmpresa()));
			}
			if (dto.getTipoPlanContable() != null && StringUtils.isNotBlank(dto.getTipoPlanContable().getNombre())) {
				var grupo = new ParametroGrupoDTO();
				grupo.setGrupo(ParametroConstants.GRUPO_TIPO_PLAN_CONTABLE);
				grupo = querySvc.get(grupo);
				dto.getTipoPlanContable().setEstado(ParametroConstants.COD_ESTADO_PARAMETRO_ACTIVO_STR);
				dto.getTipoPlanContable().setGrupo(grupo.getParametro());
				dto.setTipoPlanContable(querySvc.get(dto.getTipoPlanContable()));
			}
			if (dto.getNaturaleza() != null && StringUtils.isNotBlank(dto.getNaturaleza().getNombre())) {
				var grupo = new ParametroGrupoDTO();
				grupo.setGrupo(ParametroConstants.GRUPO_NATURALEZA);
				grupo = querySvc.get(grupo);
				dto.getNaturaleza().setEstado(ParametroConstants.COD_ESTADO_PARAMETRO_ACTIVO_STR);
				dto.getNaturaleza().setGrupo(grupo.getParametro());
				dto.setNaturaleza(querySvc.get(dto.getNaturaleza()));
			}
			if (dto.getTipo() != null && StringUtils.isNotBlank(dto.getTipo().getNombre())) {
				var grupo = new ParametroGrupoDTO();
				grupo.setGrupo(ParametroConstants.GRUPO_TIPO);
				grupo = querySvc.get(grupo);
				dto.getTipo().setEstado(ParametroConstants.COD_ESTADO_PARAMETRO_ACTIVO_STR);
				dto.getTipo().setGrupo(grupo.getParametro());
				dto.setTipo(querySvc.get(dto.getTipo()));
			}
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
