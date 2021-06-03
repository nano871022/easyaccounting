package co.com.japl.ea.dto.interfaces;
/**
 * Se encarga de servicios de facturacion 
 * @author alejandro parra
 * @since 06/05/2018
 */

import java.util.List;

import co.com.japl.ea.dto.dto.DetalleDTO;
import co.com.japl.ea.dto.dto.FacturaDTO;
import co.com.japl.ea.dto.system.UsuarioDTO;
import co.com.japl.ea.exceptions.FacturacionException;

/**
 * Se encarga de realizar un servicio de {@link FacturaDTO}
 * 
 * @author alejandro parra
 * @since 06/05/2018
 */
public interface IFacturacionSvc {
	/**
	 * Se encarga de obtener todas los registros de {@link FacturaDTO}
	 * 
	 * @param dto
	 *            {@link FacturaDTO}
	 * @return {@link List} of {@link FacturaDTO}
	 * @throws {@link
	 *             FacturacionException}
	 */
	public List<FacturaDTO> getAllFacturas(FacturaDTO dto) throws FacturacionException;
	/**
	 * Se encarga de obtner las {@link FacturaDTO} paginadas
	 * @param dto {@link FacturaDTO}
	 * @param init {@link Integer}
	 * @param end {@link Integer}
	 * @return {@link List} of {@link FacturaDTO}
	 * @throws {@link FacturacionException}
	 */
	public List<FacturaDTO> getFacturas(FacturaDTO dto, Integer init, Integer end) throws FacturacionException;
	/**
	 * Se encarga de obtener un registro de {@link FacturaDTO}
	 * @param dto {@link FacturaDTO}
	 * @return {@link FacturaDTO}
	 * @throws {@link FacturacionException}
	 */
	public FacturaDTO getFactura(FacturaDTO dto) throws FacturacionException;
	/**
	 * Se encarga de realizar una actualizacion del registro de {@link FacturaDTO}
	 * @param dto {@link FacturaDTO}
	 * @param user {@link UsuarioDTO}
	 * @throws {@link FacturacionException}
	 */
	public void update(FacturaDTO dto, UsuarioDTO user) throws FacturacionException;
	/**
	 * Se encarga de realizar un insert del registrod de {@link FacturaDTO}
	 * @param dto {@link FacturaDTO}
	 * @param user {@link UsuarioDTO}
	 * @return {@link FacturaDTO}
	 * @throws {@link FacturacionException}
	 */
	public FacturaDTO insert(FacturaDTO dto, UsuarioDTO user) throws FacturacionException;
	/**
	 * Se encarga de eliminar un registro de la tabla de {@link FacturaDTO}
	 * @param dto {@link FacturaDTO}
	 * @param user {@link UsuarioDTO}
	 * @throws {@link FacturacionException}
	 */
	public void delete(FacturaDTO dto, UsuarioDTO user) throws FacturacionException;
	/**
	 * Se encarga de obener toodos los registros de {@link DetalleDTO}
	 * @param dto {@link FacturaDTO}
	 * @return {@link List} of {@link DetalleDTO}
	 * @throws {@link FacturacionException}
	 */
	public List<DetalleDTO> getAllDetalle(DetalleDTO dto) throws FacturacionException;
	/**
	 * Se encarga de obtener los registros de detalle de paginacion
	 * @param dto {@link DetalleDTO}
	 * @param init {@link Integer}
	 * @param end {@link Integer}
	 * @return {@link List} of {@link DetalleDTO}
	 * @throws {@link FacturacionException}
	 */
	public List<DetalleDTO> getDetalle(DetalleDTO dto, Integer init, Integer end) throws FacturacionException;
	/**
	 * Se encarga de obtener un registro de {@link DetalleDTO}
	 * @param dto {@link DetalleDTO}
	 * @return {@link DetalleDTO}
	 * @throws {@link FacturacionException}
	 */
	public DetalleDTO getDetalle(DetalleDTO dto) throws FacturacionException;
	/**
	 * Se encarga de actualizar un registro de {@link DetalleDTO}
	 * @param dto {@link DetalleDTO}
	 * @param user {@link UsuarioDTO}
	 * @throws {@link FacturacionException}
	 */
	public void update(DetalleDTO dto, UsuarioDTO user) throws FacturacionException;
	/**
	 * Se encarga dde insertar un registro en {@link DetalleDTO}
	 * @param dto {@link DetalleDTO}
	 * @param user {@link UsuarioDTO}
	 * @return {@link DetalleDTO}
	 * @throws {@link FacturacionException}
	 */
	public DetalleDTO insert(DetalleDTO dto, UsuarioDTO user) throws FacturacionException;
	/**
	 * Se enncarga dde eliminar el registro del {@link DetalleDTO}
	 * @param dto {@link DetalleDTO}
	 * @param user {@link UsuarioDTO}
	 * @throws {@link FacturacionException}
	 */
	public void delete(DetalleDTO dto, UsuarioDTO user) throws FacturacionException;
}
