package com.pyt.service.interfaces;
/**
 * Se encarga de realizar crud de lass empresas
 * @author alejandro parra
 * @since 06/05/2018
 */

import java.util.List;

import org.pyt.common.exceptions.EmpresasException;

import com.pyt.service.dto.EmpresaDTO;
import com.pyt.service.dto.UsuarioDTO;

public interface IEmpresasSvc {
	/**
	 * Se encarga de obtneer todos los registros de empresas
	 * @param dto {@link EmpresaDTO}
	 * @return {@link List} of {@link EmpresaDTO}
	 * @throws {@link EmpresasException}
	 */
	public List<EmpresaDTO> getAllEmpresas(EmpresaDTO dto)throws EmpresasException;
	/**
	 * Se encarha de obtener registros de {@link EmpresaDTO}
	 * @param dto {@link EmpresaDTO}
	 * @return {@link List} of {@link EmpresaDTO}
	 * @throws {@link EmpresasException}
	 */
	public List<EmpresaDTO> getEmpresas(EmpresaDTO dto,Integer init,Integer end)throws EmpresasException;
	/**
	 * Se encarga de obtener todos los registros segun el dto suminsitrado
	 * @param dto {@link EmpresaDTO}
	 * @return {@link Integer}
	 * @throws {@link EmpresasException}
	 */
	public Integer getTotalRows(EmpresaDTO dto)throws EmpresasException;
	/**
	 * Se encarga de obtner un registro de empresa
	 * @param dto {@link EmpresaDTO}
	 * @return {@link EmpresaDTO}
	 * @throws {@link EmpresasException}
	 */
	public EmpresaDTO getEmpresa(EmpresaDTO dto)throws EmpresasException;
	/**
	 * Se encarga de realizar la actualizacion del registro de {@link EmpresaDTO}
	 * @param dto {@link EmpresaDTO}
	 * @param user {@link UsuarioDTO}
	 * @throws {@link EmpresasException}
	 */
	public void update(EmpresaDTO dto,UsuarioDTO user)throws EmpresasException;
	/**
	 * Se encarga de realizar un insert del registro de {@link EmpresaDTO}
	 * @param dto {@link EmpresaDTO}
	 * @param user {@link UsuarioDTO}
	 * @return {@link EmpresaDTO}
	 * @throws EmpresasException
	 */
	public EmpresaDTO insert(EmpresaDTO dto,UsuarioDTO user)throws EmpresasException;
	/**
	 * Se encarga de eliminar un registro de {@link EmpresaDTO}
	 * @param dto {@link EmpresaDTO}
	 * @param user {@link UsuarioDTO}
	 * @throws {@link EmpresasException}
	 */
	public void delete(EmpresaDTO dto,UsuarioDTO user)throws EmpresasException;
}
