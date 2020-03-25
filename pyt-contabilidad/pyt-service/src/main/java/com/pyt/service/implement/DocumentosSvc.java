package com.pyt.service.implement;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.pyt.common.annotations.Inject;
import org.pyt.common.exceptions.DocumentosException;
import org.pyt.common.exceptions.QueryException;

import com.pyt.query.interfaces.IQuerySvc;
import com.pyt.service.abstracts.Services;
import com.pyt.service.dto.ConceptoDTO;
import com.pyt.service.dto.DetalleContableDTO;
import com.pyt.service.dto.DetalleDTO;
import com.pyt.service.dto.DocumentoDTO;
import com.pyt.service.dto.DocumentosDTO;
import com.pyt.service.interfaces.IDocumentosSvc;

import co.com.japl.ea.dto.system.UsuarioDTO;

public class DocumentosSvc extends Services implements IDocumentosSvc {
	@Inject(resource = "com.pyt.query.implement.QuerySvc")
	private IQuerySvc querySvc;

	public List<DocumentoDTO> getAllDocumentos(DocumentoDTO dto) throws DocumentosException {
		List<DocumentoDTO> lista = new ArrayList<DocumentoDTO>();
		if (dto == null)
			throw new DocumentosException("El objeto documento se encuentra vacio.");
		try {
			lista = querySvc.gets(dto);
		} catch (QueryException e) {
			throw new DocumentosException(e.getMensage(), e);
		}
		return lista;
	}

	public List<DocumentoDTO> getDocumentos(DocumentoDTO dto, Integer init, Integer end) throws DocumentosException {
		List<DocumentoDTO> lista = new ArrayList<DocumentoDTO>();
		if (dto == null)
			throw new DocumentosException("El objeto documento se encuentra vacio.");
		try {
			lista = querySvc.gets(dto, init, end);
		} catch (QueryException e) {
			throw new DocumentosException(e.getMensage(), e);
		}
		return lista;
	}

	@co.com.arquitectura.annotation.proccessor.Services(alcance = co.com.arquitectura.annotation.proccessor.Services.scope.EJB, tipo = co.com.arquitectura.annotation.proccessor.Services.kind.PUBLIC, alias = "Documento", descripcion = "Se encarga de obtner el documentos segun objeto lleno, debe contener el id.")
	public DocumentoDTO getDocumento(DocumentoDTO dto) throws DocumentosException {
		if (dto == null)
			throw new DocumentosException("El objeto documento se encuentra vacio.");
		try {
			return querySvc.get(dto);
		} catch (QueryException e) {
			throw new DocumentosException(e.getMensage(), e);
		}
	}

	public void update(DocumentoDTO dto, UsuarioDTO user) throws DocumentosException {
		if (dto == null)
			throw new DocumentosException("El objeto documento se encuentra vacio.");
		if (StringUtils.isBlank(dto.getCodigo()))
			throw new DocumentosException("El codigo del documento se encuentra vacio.");
		try {
			querySvc.set(dto, user);
		} catch (QueryException e) {
			throw new DocumentosException(e.getMensage(), e);
		}
	}

	public DocumentoDTO insert(DocumentoDTO dto, UsuarioDTO user) throws DocumentosException {
		if (dto == null)
			throw new DocumentosException("El objeto documento se encuentra vacio.");
		if (StringUtils.isNotBlank(dto.getCodigo()))
			throw new DocumentosException("El codigo del documento no se encuentra vacio.");
		try {
			return querySvc.set(dto, user);
		} catch (QueryException e) {
			throw new DocumentosException(e.getMensage(), e);
		}
	}

	public void delete(DocumentoDTO dto, UsuarioDTO user) throws DocumentosException {
		if (dto == null)
			throw new DocumentosException("El objeto documento se encuentra vacio.");
		if (StringUtils.isBlank(dto.getCodigo()))
			throw new DocumentosException("El codigo del documento se encuentra vacio.");
		try {
			querySvc.del(dto, user);
		} catch (QueryException e) {
			throw new DocumentosException(e.getMensage(), e);
		}
	}

	public List<ConceptoDTO> getAllConceptos(ConceptoDTO dto) throws DocumentosException {
		List<ConceptoDTO> lista = new ArrayList<ConceptoDTO>();
		if (dto == null)
			throw new DocumentosException("El objeto concepto se encuentra vacio.");
		try {
			lista = querySvc.gets(dto);
		} catch (QueryException e) {
			throw new DocumentosException(e.getMensage(), e);
		}
		return lista;
	}

	public List<ConceptoDTO> getConceptos(ConceptoDTO dto, Integer init, Integer end) throws DocumentosException {
		List<ConceptoDTO> lista = new ArrayList<ConceptoDTO>();
		if (dto == null)
			throw new DocumentosException("El objeto concepto se encuentra vacio.");
		try {
			lista = querySvc.gets(dto, init, end);
		} catch (QueryException e) {
			throw new DocumentosException(e.getMensage(), e);
		}
		return lista;
	}

	public ConceptoDTO getConcepto(ConceptoDTO dto) throws DocumentosException {
		if (dto == null)
			throw new DocumentosException("El objeto concepto se encuentra vacio.");
		try {
			return querySvc.get(dto);
		} catch (QueryException e) {
			throw new DocumentosException(e.getMensage(), e);
		}
	}

	public void update(ConceptoDTO dto, UsuarioDTO user) throws DocumentosException {
		if (dto == null)
			throw new DocumentosException("El objeto concepto se encuentra vacio.");
		if (StringUtils.isBlank(dto.getCodigo()))
			throw new DocumentosException("El codigo del concepto se encuentra vacio.");
		try {
			querySvc.set(dto, user);
		} catch (QueryException e) {
			throw new DocumentosException(e.getMensage(), e);
		}
	}

	public ConceptoDTO insert(ConceptoDTO dto, UsuarioDTO user) throws DocumentosException {
		if (dto == null)
			throw new DocumentosException("El objeto concepto se encuentra vacio.");
		if (StringUtils.isNotBlank(dto.getCodigo()))
			throw new DocumentosException("El codigo del concepto no se encuentra vacio.");
		try {
			return querySvc.set(dto, user);
		} catch (QueryException e) {
			throw new DocumentosException(e.getMensage(), e);
		}
	}

	public void delete(ConceptoDTO dto, UsuarioDTO user) throws DocumentosException {
		if (dto == null)
			throw new DocumentosException("El objeto concepto se encuentra vacio.");
		if (StringUtils.isNotBlank(dto.getCodigo()))
			throw new DocumentosException("El codigo del concepto se encuentra vacio.");
		try {
			querySvc.del(dto, user);
		} catch (QueryException e) {
			throw new DocumentosException(e.getMensage(), e);
		}
	}

	@Override
	public Integer getTotalRows(ConceptoDTO filter) throws DocumentosException {
		if (filter == null)
			throw new DocumentosException("Se encontro el concepto vacio.");
		try {
			return querySvc.countRow(filter);
		} catch (QueryException e) {
			throw new DocumentosException(e.getMensage(), e);
		}
	}

	@Override
	public List<DocumentosDTO> getDocumentos(DocumentosDTO dto, Integer page, Integer rows) throws DocumentosException {
		List<DocumentosDTO> lista = new ArrayList<DocumentosDTO>();
		if (dto == null)
			throw new DocumentosException("El documento se encuentra vacio.");
		try {
			lista = querySvc.gets(dto, page, rows);
		} catch (QueryException e) {
			throw new DocumentosException("Se presentaron errores al obtener los registros.", e);
		}
		return lista;
	}

	@Override
	public Integer getTotalCount(DocumentosDTO dto) throws DocumentosException {
		if (dto == null)
			throw new DocumentosException("El documento se encuentra vacio.");
		try {
			return querySvc.countRow(dto);
		} catch (QueryException e) {
			throw new DocumentosException("No se puedo obtener la cantidad de registros", e);
		}
	}

	@Override
	public DocumentosDTO insert(DocumentosDTO dto, UsuarioDTO user) throws DocumentosException {
		if (dto == null)
			throw new DocumentosException("El documento se encuentra vacio.");
		if (StringUtils.isNotBlank(dto.getCodigo()))
			throw new DocumentosException("El codigo del documento no se encuentra vacio.");
		try {
			return querySvc.set(dto, user);
		} catch (QueryException e) {
			throw new DocumentosException("No se logro agregar el documento.", e);
		}
	}

	@Override
	public void update(DocumentosDTO dto, UsuarioDTO user) throws DocumentosException {
		if (dto == null)
			throw new DocumentosException("El documento se encuentra vacio.");
		if (StringUtils.isBlank(dto.getCodigo()))
			throw new DocumentosException("El codigo del documento se encuentra vacio.");
		try {
			querySvc.set(dto, user);
		} catch (QueryException e) {
			throw new DocumentosException("No se logro actualizar el documento.");
		}
	}

	@Override
	public void delete(DocumentosDTO dto, UsuarioDTO user) throws DocumentosException {
		if (dto == null)
			throw new DocumentosException("El documento se encuentra vacio.");
		if (StringUtils.isBlank(dto.getCodigo()))
			throw new DocumentosException("El codigo del documento se encuentra vacio.");
		try {
			querySvc.del(dto, user);
		} catch (QueryException e) {
			throw new DocumentosException("No se logro eliminar el documento.", e);
		}
	}

	@Override
	public List<DocumentosDTO> getDocumentos(DocumentosDTO dto) throws DocumentosException {
		if (dto == null)
			throw new DocumentosException("El dto suministrado se encuentra vacio.");
		if (dto.getDoctype() == null)
			throw new DocumentosException("No se ha suministrado el tipo de documento a obtener.");
		List<DocumentosDTO> lista = new ArrayList<DocumentosDTO>();
		try {
			lista = querySvc.gets(dto);
		} catch (QueryException e) {
			throw new DocumentosException("Se presneto un problema al obtener los campos asociados", e);
		}
		return lista;
	}

	@Override
	public Integer getTotalCount(DocumentoDTO dto) throws DocumentosException {
		if (dto == null)
			throw new DocumentosException("El documento de filtro se encuentra vacio.");
		Integer cantidad = 0;
		try {
			cantidad = querySvc.countRow(dto);
		} catch (QueryException e) {
			throw new DocumentosException("Se presento un error en obtener la cantidad de registros.", e);
		}
		return cantidad;
	}
	@co.com.arquitectura.annotation.proccessor.Services(alcance = co.com.arquitectura.annotation.proccessor.Services.scope.EJB, tipo = co.com.arquitectura.annotation.proccessor.Services.kind.PUBLIC, alias = "Detalle Documento", descripcion = "Se encarga de obtener los detalles asociados al documento.")
	@Override
	public List<DetalleDTO> getAllDetalles(DetalleDTO dto) throws DocumentosException {
		List<DetalleDTO> lista = new ArrayList<DetalleDTO>();
		if (dto == null)
			throw new DocumentosException("El objeto detallese encuentra vacio.");
		try {
			lista = querySvc.gets(dto);
		} catch (QueryException e) {
			throw new DocumentosException(e.getMensage(), e);
		}
		return lista;
	}

	@Override
	public List<DetalleDTO> getDetalles(DetalleDTO dto, Integer init, Integer end) throws DocumentosException {
		List<DetalleDTO> lista = new ArrayList<DetalleDTO>();
		if (dto == null)
			throw new DocumentosException("El objeto detallese encuentra vacio.");
		try {
			lista = querySvc.gets(dto, init, end);
		} catch (QueryException e) {
			throw new DocumentosException(e.getMensage(), e);
		}
		return lista;
	}

	@Override
	public DetalleDTO getDetalle(DetalleDTO dto) throws DocumentosException {
		if (dto == null)
			throw new DocumentosException("El objeto detallese encuentra vacio.");
		try {
			return querySvc.get(dto);
		} catch (QueryException e) {
			throw new DocumentosException(e.getMensage(), e);
		}
	}

	@Override
	public void update(DetalleDTO dto, UsuarioDTO user) throws DocumentosException {
		if (dto == null)
			throw new DocumentosException("El objeto detallese encuentra vacio.");
		if (StringUtils.isBlank(dto.getCodigo()))
			throw new DocumentosException("El codigo del detalle no se encuentra..");
		try {
			querySvc.set(dto, user);
		} catch (QueryException e) {
			throw new DocumentosException(e.getMensage(), e);
		}
	}

	@Override
	public DetalleDTO insert(DetalleDTO dto, UsuarioDTO user) throws DocumentosException {
		if (dto == null)
			throw new DocumentosException("El objeto detallese encuentra vacio.");
		if (StringUtils.isNotBlank(dto.getCodigo()))
			throw new DocumentosException("El codigo del detalle no se encuentra vacio.");
		try {
			return querySvc.set(dto, user);
		} catch (QueryException e) {
			throw new DocumentosException(e.getMensage(), e);
		}
	}

	@Override
	public void delete(DetalleDTO dto, UsuarioDTO user) throws DocumentosException {
		if (dto == null)
			throw new DocumentosException("El objeto detallese encuentra vacio.");
		if (StringUtils.isBlank(dto.getCodigo()))
			throw new DocumentosException("El codigo del detalle no se encuentra.");
		try {
			querySvc.del(dto, user);
		} catch (QueryException e) {
			throw new DocumentosException(e.getMensage(), e);
		}

	}
	@co.com.arquitectura.annotation.proccessor.Services(alcance = co.com.arquitectura.annotation.proccessor.Services.scope.EJB, tipo = co.com.arquitectura.annotation.proccessor.Services.kind.PUBLIC, alias = "Detalle Contable", descripcion = "Se encarga de obtner el detalle contable asociados al documento.")
	@Override
	public List<DetalleContableDTO> getAllDetalles(DetalleContableDTO dto) throws DocumentosException {
		if (dto == null)
			throw new DocumentosException("No se suministro el detalle contable.");
		try {
			return querySvc.gets(dto);
		} catch (QueryException e) {
			throw new DocumentosException("Se presento error en la eliminacion del detalle contable.", e);
		}
	}

	@Override
	public List<DetalleContableDTO> getDetalles(DetalleContableDTO dto, Integer init, Integer end)
			throws DocumentosException {
		if (dto == null)
			throw new DocumentosException("No se suministro el detalle contable.");
		try {
			return querySvc.gets(dto, init, end);
		} catch (QueryException e) {
			throw new DocumentosException("Se presento error en la eliminacion del detalle contable.", e);
		}
	}

	@Override
	public DetalleContableDTO getDetalle(DetalleContableDTO dto) throws DocumentosException {
		if (dto == null)
			throw new DocumentosException("No se suministro el detalle contable.");
		try {
			return querySvc.get(dto);
		} catch (QueryException e) {
			throw new DocumentosException("Se presento error en la eliminacion del detalle contable.", e);
		}
	}

	@Override
	public Integer getTotalCount(DetalleContableDTO dto) throws DocumentosException {
		if (dto == null)
			throw new DocumentosException("No se suministro el detalle contable.");
		try {
			return querySvc.countRow(dto);
		} catch (QueryException e) {
			throw new DocumentosException("Se presento error en la eliminacion del detalle contable.", e);
		}
	}

	@Override
	public void update(DetalleContableDTO dto, UsuarioDTO user) throws DocumentosException {
		if (dto == null)
			throw new DocumentosException("No se suministro el detalle contable.");
		if (StringUtils.isBlank(dto.getCodigo()))
			throw new DocumentosException("El detalle contable no se suministro.");
		try {
			querySvc.set(dto, user);
		} catch (QueryException e) {
			throw new DocumentosException("Se presento error en la eliminacion del detalle contable.", e);
		}
	}

	@Override
	public DetalleContableDTO insert(DetalleContableDTO dto, UsuarioDTO user) throws DocumentosException {
		if (dto == null)
			throw new DocumentosException(i18n().get("err.accountingdetail.is.empty"));
		if (StringUtils.isNotBlank(dto.getCodigo()))
			throw new DocumentosException(i18n().get("err.accountingdetail.wasnt.entered"));
		try {
			return querySvc.set(dto, user);
		} catch (QueryException e) {
			throw new DocumentosException(i18n().get("err.accountingdetail.have.a.error"), e);
		}
	}

	@Override
	public void delete(DetalleContableDTO dto, UsuarioDTO user) throws DocumentosException {
		if (dto == null)
			throw new DocumentosException("No se suministro el detalle contable.");
		if (StringUtils.isBlank(dto.getCodigo()))
			throw new DocumentosException("El detalle contable no se suministro.");
		try {
			querySvc.del(dto, user);
		} catch (QueryException e) {
			throw new DocumentosException("Se presento error en la eliminacion del detalle contable.", e);
		}
	}

	@Override
	public Integer getTotalRows(DetalleDTO filter) throws DocumentosException {
		if (filter == null)
			throw new DocumentosException("No se suministro el detalle.");
		try {
			return querySvc.countRow(filter);
		} catch (QueryException e) {
			throw new DocumentosException("Se presento error en el conteo de los detalles.", e);
		}
	}

	@Override
	public void generarCuentaPorPagar(DocumentoDTO documento, UsuarioDTO user) throws DocumentosException {

	}

	@Override
	public void generarCuentaPorCobrar(DocumentoDTO documento, UsuarioDTO user) throws DocumentosException {

	}

	@Override
	public Boolean facturaHasCuentaPorCobrar(DocumentoDTO documento, UsuarioDTO user) throws DocumentosException {
		return false;
	}

	@Override
	public Boolean facturaHasCuentaPorPagar(DocumentoDTO documento, UsuarioDTO user) throws DocumentosException {
		return false;
	}

}
