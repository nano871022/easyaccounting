package co.com.japl.ea.service.implement;

import java.util.ArrayList;
import java.util.List;

import org.pyt.common.annotations.Inject;

import co.com.japl.ea.query.interfaces.IQuerySvc;
import co.com.japl.ea.dto.abstracts.Services;
import co.com.japl.ea.dto.dto.DetalleDTO;
import co.com.japl.ea.dto.dto.FacturaDTO;
import co.com.japl.ea.dto.interfaces.IFacturacionSvc;
import co.com.japl.ea.dto.system.UsuarioDTO;
import co.com.japl.ea.exceptions.FacturacionException;
import co.com.japl.ea.exceptions.QueryException;

public class FacturacionSvc extends Services implements IFacturacionSvc {
	@Inject(resource = "co.com.japl.ea.query.implement.QuerySvc")
	private IQuerySvc querySvc;

	public List<FacturaDTO> getAllFacturas(FacturaDTO dto) throws FacturacionException {
		List<FacturaDTO> lista = new ArrayList<FacturaDTO>();
		if (dto == null)
			throw new FacturacionException("El objeto factura se encuentra vacio.");
		try {
			lista = querySvc.gets(dto);
		} catch (QueryException e) {
			throw new FacturacionException(e.getMensage(), e);
		}
		return lista;
	}

	public List<FacturaDTO> getFacturas(FacturaDTO dto, Integer init, Integer end) throws FacturacionException {
		List<FacturaDTO> lista = new ArrayList<FacturaDTO>();
		if (dto == null)
			throw new FacturacionException("El objeto factura se encuentra vacio.");
		try {
			lista = querySvc.gets(dto, init, end);
		} catch (QueryException e) {
			throw new FacturacionException(e.getMensage(), e);
		}
		return lista;
	}

	public FacturaDTO getFactura(FacturaDTO dto) throws FacturacionException {
		if (dto == null)
			throw new FacturacionException("El objeto factura se encuentra vacio.");
		try {
			return querySvc.get(dto);
		} catch (QueryException e) {
			throw new FacturacionException(e.getMensage(), e);
		}
	}

	public void update(FacturaDTO dto, UsuarioDTO user) throws FacturacionException {
		if (dto == null)
			throw new FacturacionException("El objeto factura se encuentra vacio.");
		if (dto.getCodigo() == null)
			throw new FacturacionException("El codigo de factura se encuentra vacio.");
		try {
			querySvc.set(dto, user);
		} catch (QueryException e) {
			throw new FacturacionException(e.getMensage(), e);
		}
	}

	public FacturaDTO insert(FacturaDTO dto, UsuarioDTO user) throws FacturacionException {
		if (dto == null)
			throw new FacturacionException("El objeto factura se encuentra vacio.");
		if (dto.getCodigo() != null)
			throw new FacturacionException("El codigo de factura no se encuentra vacio.");
		try {
			return querySvc.set(dto, user);
		} catch (QueryException e) {
			throw new FacturacionException(e.getMensage(), e);
		}
	}

	public void delete(FacturaDTO dto, UsuarioDTO user) throws FacturacionException {
		if (dto == null)
			throw new FacturacionException("El objeto factura se encuentra vacio.");
		if (dto.getCodigo() == null)
			throw new FacturacionException("El codigo de factura se encuentra vacio.");
		try {
			querySvc.del(dto, user);
		} catch (QueryException e) {
			throw new FacturacionException(e.getMensage(), e);
		}
	}

	public List<DetalleDTO> getAllDetalle(DetalleDTO dto) throws FacturacionException {
		List<DetalleDTO> lista = new ArrayList<DetalleDTO>();
		if (dto == null)
			throw new FacturacionException("El objeto detalle se encuentra vacio.");
		try {
			lista = querySvc.gets(dto);
		} catch (QueryException e) {
			throw new FacturacionException(e.getMensage(), e);
		}
		return lista;
	}

	public List<DetalleDTO> getDetalle(DetalleDTO dto, Integer init, Integer end) throws FacturacionException {
		List<DetalleDTO> lista = new ArrayList<DetalleDTO>();
		if (dto == null)
			throw new FacturacionException("El objeto detalle se encuentra vacio.");
		try {
			lista = querySvc.gets(dto, init, end);
		} catch (QueryException e) {
			throw new FacturacionException(e.getMensage(), e);
		}
		return lista;
	}

	public DetalleDTO getDetalle(DetalleDTO dto) throws FacturacionException {
		if (dto == null)
			throw new FacturacionException("El objeto detalle se encuentra vacio.");
		if (dto.getCodigo() == null)
			throw new FacturacionException("El codigo de detalle se encuentra vacio.");
		try {
			return querySvc.get(dto);
		} catch (QueryException e) {
			throw new FacturacionException(e.getMensage(), e);
		}
	}

	public void update(DetalleDTO dto, UsuarioDTO user) throws FacturacionException {
		if (dto == null)
			throw new FacturacionException("El objeto detalle se encuentra vacio.");
		if (dto.getCodigo() == null)
			throw new FacturacionException("El codigo de detalle se encuentra vacio.");
		try {
			querySvc.set(dto, user);
		} catch (QueryException e) {
			throw new FacturacionException(e.getMensage(), e);
		}
	}

	public DetalleDTO insert(DetalleDTO dto, UsuarioDTO user) throws FacturacionException {
		if (dto == null)
			throw new FacturacionException("El objeto detalle se encuentra vacio.");
		if (dto.getCodigo() != null)
			throw new FacturacionException("El codigo de detalle no se encuentra vacio.");
		try {
			return querySvc.set(dto, user);
		} catch (QueryException e) {
			throw new FacturacionException(e.getMensage(), e);
		}
	}

	public void delete(DetalleDTO dto, UsuarioDTO user) throws FacturacionException {
		if (dto == null)
			throw new FacturacionException("El objeto detalle se encuentra vacio.");
		if (dto.getCodigo() == null)
			throw new FacturacionException("El codigo de detalle se encuentra vacio.");
		try {
			querySvc.del(dto, user);
		} catch (QueryException e) {
			throw new FacturacionException(e.getMensage(), e);
		}
	}

}
