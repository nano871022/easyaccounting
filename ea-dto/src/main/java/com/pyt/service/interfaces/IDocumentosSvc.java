package com.pyt.service.interfaces;

import java.util.List;

import javax.swing.text.Document;

import org.pyt.common.exceptions.DocumentosException;

import com.pyt.service.dto.ConceptoDTO;
import com.pyt.service.dto.DetalleContableDTO;
import com.pyt.service.dto.DetalleDTO;
import com.pyt.service.dto.DocumentoDTO;
import com.pyt.service.dto.DocumentosDTO;

import co.com.japl.ea.dto.system.UsuarioDTO;

/**
 * Se encarga de realizar crud sobre doccumentos
 * 
 * @author alejandro parra
 * @since 06/05/2018
 */
public interface IDocumentosSvc {
	/**
	 * Se encarga de obtner todos los registros de documentos
	 * 
	 * @param dto
	 *            {@link DocumentoDTO}
	 * @return {@link List} of {@link Document}
	 * @throws {@link
	 *             DocumentosException}
	 */
	public List<DocumentoDTO> getAllDocumentos(DocumentoDTO dto) throws DocumentosException;

	/**
	 * Se encarga de obtneer registros de {@link DocumentoDTO} paginado
	 * 
	 * @param dto
	 *            {@link DocumentoDTO}
	 * @param init
	 *            {@link Integer}
	 * @param end
	 *            Integ
	 * @return {@link List} of {@link DocumentoDTO}
	 * @throws DocumentosException
	 */
	public List<DocumentoDTO> getDocumentos(DocumentoDTO dto, Integer init, Integer end) throws DocumentosException;

	/**
	 * Se encarga de obtner un registro de {@link DocumentoDTO}
	 * 
	 * @param dto
	 *            {@link DocumentoDTO}
	 * @return {@link DocumentoDTO}
	 * @throws DocumentosException
	 */
	public DocumentoDTO getDocumento(DocumentoDTO dto) throws DocumentosException;
	/**
	 * Se encarga de obtener la cantidad de registros encontrados aplicando el filtro
	 * @param dto {@link DocumentoDTO}
	 * @return {@link Integer}
	 * @throws {@link DocumentosException}
	 */
	public Integer getTotalCount(DocumentoDTO dto) throws DocumentosException;
	
	/**
	 * Se encarga de actualizar un registro en la tabla de {@link DocumentoDTO}
	 * 
	 * @param dto
	 *            {@link DocumentoDTO}
	 * @param user
	 *            {@link UsuarioDTO}
	 * @throws {@link
	 *             DocumentosException}
	 */
	public void update(DocumentoDTO dto, UsuarioDTO user) throws DocumentosException;

	/**
	 * Se encarga de insertar un registro de {@link DocumentoDTO}
	 * 
	 * @param dto
	 *            {@link DocumentoDTO}
	 * @param user
	 *            {@link UsuarioDTO}
	 * @return {@link DocumentoDTO}
	 * @throws {@link
	 *             DocumentosException}
	 */
	public DocumentoDTO insert(DocumentoDTO dto, UsuarioDTO user) throws DocumentosException;

	/**
	 * Se encarga de eliminar un registro de {@link DocumentoDTO}
	 * 
	 * @param dto
	 *            {@link DocumentoDTO}
	 * @param user
	 *            {@link UsuarioDTO}
	 * @throws {@link
	 *             DocumentosException}
	 */
	public void delete(DocumentoDTO dto, UsuarioDTO user) throws DocumentosException;

	/**
	 * Se encarga de obtener todos los regisros de {@link DetalleConceptoDTO}
	 * 
	 * @param dto
	 *            {@link DetalleConceptoDTO}
	 * @return {@link List} of {@link DetalleConceptoDTO}
	 * @throws {@link
	 *             DocumentosException}
	 */
	public List<DetalleDTO> getAllDetalles(DetalleDTO dto) throws DocumentosException;
	
	/**
	 * Se encarga de obtner todos los registro de {@link DetalleConceptoDTO}
	 * paginados
	 * 
	 * @param dto
	 *            {@link DetalleDTO}
	 * @param init
	 *            {@link Integer}
	 * @param end
	 *            {@link Integer}
	 * @return {@link List} of {@link DetalleDTO}
	 * @throws {@link
	 *             DocumentosException}
	 */
	public List<DetalleDTO> getDetalles(DetalleDTO dto, Integer init, Integer end)
			throws DocumentosException;
	
	/**
	 * Se encarga de obtener el registro de detalle {@link DetalleConceptoDTO}
	 * 
	 * @param dto
	 *            {@link DetalleConceptoDTO}
	 * @return {@link DetalleConceptoDTO}
	 * @throws {@link
	 *             DocumentosException}
	 */
	public DetalleDTO getDetalle(DetalleDTO dto) throws DocumentosException;
	
	/**
	 * Se encarga de actualizar un registro de {@link DetalleConceptoDTO}
	 * 
	 * @param dto
	 *            {@link DetalleConceptoDTO}
	 * @param user
	 *            {@link UsuarioDTO}
	 * @throws {@link
	 *             DocumentosException}
	 */
	public void update(DetalleDTO dto, UsuarioDTO user) throws DocumentosException;
	
	/**
	 * Se encarga de insertar un registro en el {@link DetalleConceptoDTO}
	 * 
	 * @param dto
	 *            {@link DetalleConceptoDTO}
	 * @param user
	 *            {@link UsuarioDTO}
	 * @return {@link DetalleConceptoDTO}
	 * @throws {@link
	 *             DocumentosException}
	 */
	public DetalleDTO insert(DetalleDTO dto, UsuarioDTO user) throws DocumentosException;
	
	/**
	 * Se encarga de eliminar un registro del {@link DetalleConceptoDTO}
	 * 
	 * @param dto
	 *            {@link DetalleConceptoDTO}
	 * @param user
	 *            {@link UsuarioDTO}
	 * @throws {@link
	 *             DocumentosException}
	 */
	public void delete(DetalleDTO dto, UsuarioDTO user) throws DocumentosException;

	/**
	 * Se encarga de obener todos los registros de {@link ConceptoDTO}
	 * 
	 * @param dto
	 *            {@link ConceptoDTO}
	 * @return {@link List} of {@link ConceptoDTO}
	 * @throws {@link
	 *             DocumentosException}
	 */
	public List<ConceptoDTO> getAllConceptos(ConceptoDTO dto) throws DocumentosException;

	/**
	 * Se encargad e obener los registros de {@link ConceptoDTO} paginados
	 * 
	 * @param dto
	 *            {@link ConceptoDTO}
	 * @param init
	 *            {@link Integer}
	 * @param end
	 *            {@link Integer}
	 * @return {@link List} of {@link ConceptoDTO}
	 * @throws {@link
	 *             DocumentosException}
	 */
	public List<ConceptoDTO> getConceptos(ConceptoDTO dto, Integer init, Integer end) throws DocumentosException;

	/**
	 * Se encarga de obtener un {@link ConceptoDTO}
	 * 
	 * @param dto
	 *            {@link ConceptoDTO}
	 * @return {@link ConceptoDTO}
	 * @throws {@link
	 *             DocumentosException}
	 */
	public ConceptoDTO getConcepto(ConceptoDTO dto) throws DocumentosException;

	/**
	 * Se encarga de actualizar un registro en {@link ConceptoDTO}
	 * 
	 * @param dto
	 *            {@link ConceptoDTO}
	 * @param user
	 *            {@link UsuarioDTO}
	 * @throws {@link
	 *             DocumentosException}
	 */
	public void update(ConceptoDTO dto, UsuarioDTO user) throws DocumentosException;

	/**
	 * Se encarga de insertar un registro en {@link ConceptoDTO}
	 * 
	 * @param dto
	 *            {@link ConceptoDTO}
	 * @param user
	 *            {@link UsuarioDTO}
	 * @return {@link ConceptoDTO}
	 * @throws {@link
	 *             DocumentosException}
	 */
	public ConceptoDTO insert(ConceptoDTO dto, UsuarioDTO user) throws DocumentosException;

	/**
	 * Se encarga de eliminar un registro de {@link ConceptoDTO}
	 * 
	 * @param dto
	 *            {@link ConceptoDTO}
	 * @param user
	 *            {@link UsuarioDTO}
	 * @throws {@link
	 *             DocumentosException}
	 */
	public void delete(ConceptoDTO dto, UsuarioDTO user) throws DocumentosException;

	/**
	 * Se encarga de contar los registros que se encontraron aplicando el filtro
	 * 
	 * @param filter
	 *            {@link ConceptoDTO}
	 * @return {@link Integer}
	 * @throws {@link
	 *             DocumentosException}
	 */
	public Integer getTotalRows(ConceptoDTO filter) throws DocumentosException;
	/**
	 * Se encarga de contar los registros que se encontraron aplicando el filtro
	 * 
	 * @param filter
	 *            {@link DetalleDTO}
	 * @return {@link Integer}
	 * @throws {@link
	 *             DocumentosException}
	 */
	public Integer getTotalRows(DetalleDTO filter) throws DocumentosException;

	/**
	 * Obtiene los registros aplicando el filtro en documentos
	 * 
	 * @param dto
	 *            {@link DocumentosDTO}
	 * @param page
	 *            {@link Integer}
	 * @param rows
	 *            {@link Integer}
	 * @return {@link List} of {@link DocumentosDTO}
	 * @throws {@link
	 *             DocumentosException}
	 */
	public List<DocumentosDTO> getDocumentos(DocumentosDTO dto, Integer page, Integer rows) throws DocumentosException;
	/**
	 * Se encarga de obtener todos los campos configurados asociados con el dto suministraddo
	 * @param dto {@link DocumentosDTO}
	 * @return {@link List} of {@link DocumentosDTO}
	 * @throws {@link DocumentosException}
	 */
	public List<DocumentosDTO> getDocumentos(DocumentosDTO dto) throws DocumentosException;

	/**
	 * Se encarga de obtner la cantidad de registros encontrados apartir del filtro
	 * aplicado
	 * 
	 * @param dto {@link DocumentosDTO}
	 * @return {@link Integer}
	 * @throws DocumentosException
	 */
	public Integer getTotalCount(DocumentosDTO dto) throws DocumentosException;
	/**
	 * Se encarga de ingresar in registro para documentos
	 * @param dto {@link DocumentosDTO}
	 * @param user {@link UsuarioDTO}
	 * @return {@link DocumentosDTO}
	 * @throws {@link DocumentosException}
	 */
	public DocumentosDTO insert(DocumentosDTO dto, UsuarioDTO user) throws DocumentosException;
	/**
	 * Se encarga de actualizar un registro de documentos
	 * @param dto {@link DocumentosDTO}
	 * @param user {@link UsuarioDTO}
	 * @throws {@link DocumentosException}
	 */
	public void update(DocumentosDTO dto, UsuarioDTO user) throws DocumentosException;
	
	/**
	 * Se encarga de eliminar un registro de documentos
	 * 
	 * @param dto
	 *            {@link DocumentosDTO}
	 * @param user
	 *            {@link UsuarioDTO}
	 * @throws {@link
	 *             DocumentosException}
	 */
	public void delete(DocumentosDTO dto, UsuarioDTO user) throws DocumentosException;
	/**
	 * Se encarga de obtner todos los registros de detalle de contables
	 * 
	 * @param dto
	 *            {@link DetalleContableDTO}
	 * @return {@link List} of {@link DetalleContableDTO}
	 * @throws {@link
	 *             DocumentosException}
	 */
	public List<DetalleContableDTO> getAllDetalles(DetalleContableDTO dto) throws DocumentosException;

	/**
	 * Se encarga de obtneer registros de {@link DetalleContableDTO} paginado
	 * 
	 * @param dto
	 *            {@link DetalleContableDTO}
	 * @param init
	 *            {@link Integer}
	 * @param end
	 *            Integ
	 * @return {@link List} of {@link DetalleContableDTO}
	 * @throws DocumentosException
	 */
	public List<DetalleContableDTO> getDetalles(DetalleContableDTO dto, Integer init, Integer end) throws DocumentosException;

	/**
	 * Se encarga de obtner un registro de {@link DetalleContableDTO}
	 * 
	 * @param dto
	 *            {@link DetalleContableDTO}
	 * @return {@link DetalleContableDTO}
	 * @throws DocumentosException
	 */
	public DetalleContableDTO getDetalle(DetalleContableDTO dto) throws DocumentosException;
	/**
	 * Se encarga de obtener la cantidad de registros encontrados aplicando el filtro
	 * @param dto {@link DetalleContableDTO}
	 * @return {@link Integer}
	 * @throws {@link DocumentosException}
	 */
	public Integer getTotalCount(DetalleContableDTO dto) throws DocumentosException;
	
	/**
	 * Se encarga de actualizar un registro en la tabla de {@link DetalleContableDTO}
	 * 
	 * @param dto
	 *            {@link DetalleContableDTO}
	 * @param user
	 *            {@link UsuarioDTO}
	 * @throws {@link
	 *             DocumentosException}
	 */
	public void update(DetalleContableDTO dto, UsuarioDTO user) throws DocumentosException;

	/**
	 * Se encarga de insertar un registro de {@link DetalleContableDTO}
	 * 
	 * @param dto
	 *            {@link DetalleContableDTO}
	 * @param user
	 *            {@link UsuarioDTO}
	 * @return {@link DetalleContableDTO}
	 * @throws {@link
	 *             DocumentosException}
	 */
	public DetalleContableDTO insert(DetalleContableDTO dto, UsuarioDTO user) throws DocumentosException;

	/**
	 * Se encarga de eliminar un registro de {@link DetalleContableDTO}
	 * 
	 * @param dto
	 *            {@link DetalleContableDTO}
	 * @param user
	 *            {@link UsuarioDTO}
	 * @throws {@link
	 *             DocumentosException}
	 */
	public void delete(DetalleContableDTO dto, UsuarioDTO user) throws DocumentosException;

}
