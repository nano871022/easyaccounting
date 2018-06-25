package com.pyt.service.implement;

import java.util.ArrayList;
import java.util.List;

import org.pyt.common.annotations.Inject;
import org.pyt.common.common.UsuarioDTO;
import org.pyt.common.exceptions.DocumentosException;
import org.pyt.common.exceptions.QueryException;

import com.pyt.query.interfaces.IQuerySvc;
import com.pyt.service.abstracts.Services;
import com.pyt.service.dto.ConceptoDTO;
import com.pyt.service.dto.DetalleConceptoDTO;
import com.pyt.service.dto.DocumentoDTO;
import com.pyt.service.interfaces.IDocumentosSvc;

public class DocumentosSvc extends Services  implements IDocumentosSvc {
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
		if (dto.getCodigo() == null)
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
		if (dto.getCodigo() != null)
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
		if (dto.getCodigo() == null)
			throw new DocumentosException("El codigo del documento se encuentra vacio.");
		try {
			querySvc.del(dto, user);
		} catch (QueryException e) {
			throw new DocumentosException(e.getMensage(), e);
		}
	}

	public List<DetalleConceptoDTO> getAllDetalles(DetalleConceptoDTO dto) throws DocumentosException {
		List<DetalleConceptoDTO> lista = new ArrayList<DetalleConceptoDTO>();
		if (dto == null)
			throw new DocumentosException("El objeto detalle concepto se encuentra vacio.");
		try {
			lista = querySvc.gets(dto);
		} catch (QueryException e) {
			throw new DocumentosException(e.getMensage(), e);
		}
		return lista;
	}

	public List<DetalleConceptoDTO> getDetalles(DetalleConceptoDTO dto, Integer init, Integer end)
			throws DocumentosException {
		List<DetalleConceptoDTO> lista = new ArrayList<DetalleConceptoDTO>();
		if (dto == null)
			throw new DocumentosException("El objeto detalle concepto se encuentra vacio.");
		try {
			lista = querySvc.gets(dto, init, end);
		} catch (QueryException e) {
			throw new DocumentosException(e.getMensage(), e);
		}
		return lista;
	}

	public DetalleConceptoDTO getDetalle(DetalleConceptoDTO dto) throws DocumentosException {
		if (dto == null)
			throw new DocumentosException("El objeto detalle concepto se encuentra vacio.");
		try {
			return querySvc.get(dto);
		} catch (QueryException e) {
			throw new DocumentosException(e.getMensage(), e);
		}
	}

	public void update(DetalleConceptoDTO dto, UsuarioDTO user) throws DocumentosException {
		if (dto == null)
			throw new DocumentosException("El objeto detalle de concepto se encuentra vacio.");
		if (dto.getCodigo() == null)
			throw new DocumentosException("El codigo del detalle de concepto se encuentra vacio.");
		try {
			querySvc.set(dto, user);
		} catch (QueryException e) {
			throw new DocumentosException(e.getMensage(), e);
		}
	}

	public DetalleConceptoDTO insert(DetalleConceptoDTO dto, UsuarioDTO user) throws DocumentosException {
		if (dto == null)
			throw new DocumentosException("El objeto detalle concepto se encuentra vacio.");
		if (dto.getCodigo() != null)
			throw new DocumentosException("El codigo del detalle concepto no se encuentra vacio.");
		try {
			return querySvc.set(dto, user);
		} catch (QueryException e) {
			throw new DocumentosException(e.getMensage(), e);
		}
	}

	public void delete(DetalleConceptoDTO dto, UsuarioDTO user) throws DocumentosException {
		if (dto == null)
			throw new DocumentosException("El objeto detalle concepto se encuentra vacio.");
		if (dto.getCodigo() == null)
			throw new DocumentosException("El codigo detalle del concepto se encuentra vacio.");
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
		if (dto.getCodigo() == null)
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
		if (dto.getCodigo() != null)
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
		if (dto.getCodigo() == null)
			throw new DocumentosException("El codigo del concepto se encuentra vacio.");
		try {
			querySvc.del(dto, user);
		} catch (QueryException e) {
			throw new DocumentosException(e.getMensage(), e);
		}
	}

}
