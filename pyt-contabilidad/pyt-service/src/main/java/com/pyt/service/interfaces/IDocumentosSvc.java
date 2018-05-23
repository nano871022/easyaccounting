package com.pyt.service.interfaces;

import java.util.List;

import javax.swing.text.Document;

import org.pyt.common.common.UsuarioDTO;
import org.pyt.common.exceptions.DocumentosException;

import com.pyt.service.dto.ConceptoDTO;
import com.pyt.service.dto.DetalleConceptoDTO;
import com.pyt.service.dto.DocumentoDTO;

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
	public List<DetalleConceptoDTO> getAllDetalles(DetalleConceptoDTO dto) throws DocumentosException;

	/**
	 * Se encarga de obtner todos los registro de {@link DetalleConceptoDTO}
	 * paginados
	 * 
	 * @param dto
	 *            {@link DetalleConceptoDTO}
	 * @param init
	 *            {@link Integer}
	 * @param end
	 *            {@link Integer}
	 * @return {@link List} of {@link DetalleConceptoDTO}
	 * @throws {@link
	 *             DocumentosException}
	 */
	public List<DetalleConceptoDTO> getDetalles(DetalleConceptoDTO dto, Integer init, Integer end)
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
	public DetalleConceptoDTO getDetalle(DetalleConceptoDTO dto) throws DocumentosException;

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
	public void update(DetalleConceptoDTO dto, UsuarioDTO user) throws DocumentosException;

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
	public DetalleConceptoDTO insert(DetalleConceptoDTO dto, UsuarioDTO user) throws DocumentosException;

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
	public void delete(DetalleConceptoDTO dto, UsuarioDTO user) throws DocumentosException;

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

}
