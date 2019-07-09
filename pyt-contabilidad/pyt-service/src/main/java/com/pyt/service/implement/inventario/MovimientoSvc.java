package com.pyt.service.implement.inventario;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.pyt.common.annotations.Inject;
import org.pyt.common.common.UsuarioDTO;
import org.pyt.common.exceptions.QueryException;
import org.pyt.common.exceptions.inventario.MovimientoException;

import com.pyt.query.interfaces.IQuerySvc;
import com.pyt.service.abstracts.Services;
import com.pyt.service.dto.inventario.MovimientoDTO;
import com.pyt.service.dto.inventario.RestarCantidadDTO;
import com.pyt.service.dto.inventario.SaldoDTO;
import com.pyt.service.interfaces.inventarios.IMovimientoSvc;

import co.com.arquitectura.annotation.proccessor.Services.Type;
import co.com.arquitectura.annotation.proccessor.Services.kind;
import co.com.arquitectura.annotation.proccessor.Services.scope;

public class MovimientoSvc extends Services implements IMovimientoSvc {
	@Inject(resource = "com.pyt.query.implement.QuerySvc")
	private IQuerySvc querySvc;

	@Override
	public MovimientoDTO insert(MovimientoDTO movimiento, UsuarioDTO usuario) throws MovimientoException {
		if(movimiento == null )throw new MovimientoException("No se encontro movimiento suministrado.");
		if(usuario == null)throw new MovimientoException("No se encontro usuario suministrado.");
		if(StringUtils.isNotBlank(movimiento.getCodigo()))throw new MovimientoException("El movimiento ya se encuentra creado.");
		try {
			return querySvc.set(movimiento, usuario);
		}catch (QueryException e) {
			throw new MovimientoException("Se encontro problema en el ingreso del registro.",e);
		}
	}

	@Override
	public SaldoDTO insert(SaldoDTO saldo, UsuarioDTO usuario) throws MovimientoException {
		if(saldo == null )throw new MovimientoException("No se encontro saldo suministrado.");
		if(usuario == null)throw new MovimientoException("No se encontro usuario suministrado.");
		if(StringUtils.isNotBlank(saldo.getCodigo()))throw new MovimientoException("El saldo ya se encuentra creado.");
		try {
			return querySvc.set(saldo, usuario);
		}catch (QueryException e) {
			throw new MovimientoException("Se encontro problema en el ingreso del registro.",e);
		}
	}

	@Override
	public void update(MovimientoDTO movimiento, UsuarioDTO usuario) throws MovimientoException {
		if(movimiento == null )throw new MovimientoException("No se encontro el movimiento suministrado.");
		if(usuario == null)throw new MovimientoException("No se encontro usuario suministrado.");
		if(StringUtils.isBlank(movimiento.getCodigo()))throw new MovimientoException("El movimiento no se encuentra creado.");
		try {
			querySvc.set(movimiento, usuario);
		}catch (QueryException e) {
			throw new MovimientoException("Se encontro problema en la actualizacion del registro.",e);
		}
	}

	@Override
	public void update(SaldoDTO saldo, UsuarioDTO usuario) throws MovimientoException {
		if(saldo == null )throw new MovimientoException("No se encontro el saldo suministrado.");
		if(usuario == null)throw new MovimientoException("No se encontro usuario suministrado.");
		if(StringUtils.isBlank(saldo.getCodigo()))throw new MovimientoException("El saldo no se encuentra creado.");
		try {
			querySvc.set(saldo, usuario);
		}catch (QueryException e) {
			throw new MovimientoException("Se encontro problema en la actualizacion del registro.",e);
		}
	}

	@Override
	public MovimientoDTO movimiento(MovimientoDTO movimiento) throws MovimientoException {
		if(movimiento == null )throw new MovimientoException("No se encontro el movimiento suministrado.");
		try {
			return querySvc.get(movimiento);
		}catch (QueryException e) {
			throw new MovimientoException("Se encontro problema en la obtancion del registro.",e);
		}
	}

	@Override
	public SaldoDTO saldo(SaldoDTO saldo) throws MovimientoException {
		if(saldo == null )throw new MovimientoException("No se encontro el saldo suministrado.");
		try {
			return querySvc.get(saldo);
		}catch (QueryException e) {
			throw new MovimientoException("Se encontro problema en la obtancion del registro.",e);
		}
	}

	@Override
	public List<MovimientoDTO> movimientos(MovimientoDTO movimiento) throws MovimientoException {
		if(movimiento == null )throw new MovimientoException("No se encontro el movimiento suministrado.");
		try {
			return querySvc.gets(movimiento);
		}catch (QueryException e) {
			throw new MovimientoException("Se encontro problema en la obtancion del registro.",e);
		}
	}

	@Override
	public List<SaldoDTO> saldos(SaldoDTO saldo) throws MovimientoException {
		if(saldo == null )throw new MovimientoException("No se encontro el saldo suministrado.");
		try {
			return querySvc.gets(saldo);
		}catch (QueryException e) {
			throw new MovimientoException("Se encontro problema en la obtancion del registro.",e);
		}
	}

	@Override
	public List<MovimientoDTO> movimientos(MovimientoDTO movimiento, Integer inicio, Integer cantidad)
			throws MovimientoException {
		if(movimiento == null )throw new MovimientoException("No se encontro el movimiento suministrado.");
		if(inicio == null) throw new MovimientoException("No se encontro registro de inicio");
		if(cantidad == null)throw new MovimientoException("No se encontro cantidad a paginar");
		try {
			return querySvc.gets(movimiento,inicio,cantidad);
		}catch (QueryException e) {
			throw new MovimientoException("Se encontro problema en la obtancion del registro.",e);
		}
	}

	@Override
	public List<SaldoDTO> saldos(SaldoDTO saldo, Integer inicio, Integer cantidad) throws MovimientoException {
		if(saldo == null )throw new MovimientoException("No se encontro el saldo suministrado.");
		if(inicio == null) throw new MovimientoException("No se encontro registro de inicio");
		if(cantidad == null)throw new MovimientoException("No se encontro cantidad a paginar");
		try {
			return querySvc.gets(saldo,inicio,cantidad);
		}catch (QueryException e) {
			throw new MovimientoException("Se encontro problema en la obtancion del registro.",e);
		}
	}

	@Override
	public void del(MovimientoDTO movimiento, UsuarioDTO usuario) throws MovimientoException {
		if(movimiento == null )throw new MovimientoException("No se encontro el movimiento suministrado.");
		if(usuario == null)throw new MovimientoException("No se encontro usuario suministrado.");
		if(StringUtils.isBlank(movimiento.getCodigo()))throw new MovimientoException("El movimiento no se encuentra creado.");
		try {
			querySvc.del(movimiento, usuario);
		}catch (QueryException e) {
			throw new MovimientoException("Se encontro problema en la eliminacion del registro.",e);
		}
	}

	@Override
	public void del(SaldoDTO saldo, UsuarioDTO usuario) throws MovimientoException {
		if(saldo == null )throw new MovimientoException("No se encontro el saldo suministrado.");
		if(usuario == null)throw new MovimientoException("No se encontro usuario suministrado.");
		if(StringUtils.isBlank(saldo.getCodigo()))throw new MovimientoException("El saldo no se encuentra creado.");
		try {
			querySvc.del(saldo, usuario);
		}catch (QueryException e) {
			throw new MovimientoException("Se encontro problema en la eliminacion del registro.",e);
		}
	}

	@Override
	public RestarCantidadDTO insert(RestarCantidadDTO restar, UsuarioDTO usuario) throws MovimientoException {
		if(restar == null )throw new MovimientoException("No se encontro restar cantidad suministrado.");
		if(usuario == null)throw new MovimientoException("No se encontro usuario suministrado.");
		if(StringUtils.isNotBlank(restar.getCodigo()))throw new MovimientoException("El restar cantidad ya se encuentra creado.");
		try {
			return querySvc.set(restar, usuario);
		}catch (QueryException e) {
			throw new MovimientoException("Se encontro problema en el ingreso del registro.",e);
		}
	}

	@Override
	public void update(RestarCantidadDTO restar, UsuarioDTO usuario) throws MovimientoException {
		if(restar == null )throw new MovimientoException("No se encontro el restar cantidad suministrado.");
		if(usuario == null)throw new MovimientoException("No se encontro usuario suministrado.");
		if(StringUtils.isBlank(restar.getCodigo()))throw new MovimientoException("El restar cantidad no se encuentra creado.");
		try {
			querySvc.set(restar, usuario);
		}catch (QueryException e) {
			throw new MovimientoException("Se encontro problema en la actualizacion del registro.",e);
		}
	}

	@Override
	public List<RestarCantidadDTO> restarCantidad(RestarCantidadDTO restar) throws MovimientoException {
		if(restar == null )throw new MovimientoException("No se encontro el restar cantidad suministrado.");
		try {
			return querySvc.gets(restar);
		}catch (QueryException e) {
			throw new MovimientoException("Se encontro problema en la obtancion del registro.",e);
		}
	}

	@Override
	public void del(RestarCantidadDTO restar, UsuarioDTO usuario) throws MovimientoException {
		if(restar == null )throw new MovimientoException("No se encontro el restar cantidad suministrado.");
		if(usuario == null)throw new MovimientoException("No se encontro usuario suministrado.");
		if(StringUtils.isBlank(restar.getCodigo()))throw new MovimientoException("El restar cantidad no se encuentra creado.");
		try {
			querySvc.del(restar, usuario);
		}catch (QueryException e) {
			throw new MovimientoException("Se encontro problema en la eliminacion del registro.",e);
		}
	}
}
