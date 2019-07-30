package com.pyt.service.interfaces;

import java.util.List;

import org.pyt.common.common.UsuarioDTO;
import org.pyt.common.exceptions.CuentaContableException;

import com.pyt.service.dto.CuentaContableDTO;

/**
 * Se implementa la interfaz para modificar lass cuenta contables
 * 
 * @author Alejandro Parra
 * @since 12/07/2018
 */
public interface ICuentaContableSvc {
	/**
	 * Se encarga de obtener todas las cuenta contables
	 * 
	 * @param dto {@link CuentaContableDTO}
	 * @return {@link List} of cuenta contable
	 * @throws {@link CuentaContableException}
	 */
	public List<CuentaContableDTO> getCuentaContables(CuentaContableDTO dto) throws CuentaContableException;

	/**
	 * Se encarga de obtener todas las cuenta contables
	 * 
	 * @param dto  {@link CuentaContableDTO}
	 * @param init {@link Integer}
	 * @param rows {@link Integer}
	 * @return {@link List} of cuenta contable
	 * @throws {@link CuentaContableException}
	 */
	public List<CuentaContableDTO> getCuentaContables(CuentaContableDTO dto, Integer init, Integer rows)
			throws CuentaContableException;

	/**
	 * Se encarga de obtener una cuenta contable
	 * 
	 * @param dto {@link CuentaContableDTO}
	 * @return {@link CuentaContableDTO}
	 * @throws {@link CuentaContableException}
	 */
	public CuentaContableDTO getCuentaContable(CuentaContableDTO dto) throws CuentaContableException;

	/**
	 * Se encarga de agregar una nueva cuenta contable
	 * 
	 * @param dto  {@link CuentaContableDTO}
	 * @param user {@link UsuarioDTO}
	 * @return {@link CuentaContableDTO}
	 * @throws CuentaContableException
	 */
	public CuentaContableDTO insert(CuentaContableDTO dto, UsuarioDTO user) throws CuentaContableException;

	/**
	 * Se encarga de exportar para ser usado por servicio de carga de datos
	 * 
	 * @param dto  {@link CuentaContableDTO}
	 * @param user {@link UsuarioDTO}
	 * @return {@link CuentaContableDTO}
	 * @throws {@link CuentaContableException}
	 */
	public CuentaContableDTO insertService(CuentaContableDTO dto, UsuarioDTO user) throws CuentaContableException;

	/**
	 * Se encarga de actualziar una cuenta contable
	 * 
	 * @param dto  {@link CuentaContableDTO}
	 * @param user {@link UsuarioDTO}
	 * @throws {@link CuentaContableException}
	 */
	public void update(CuentaContableDTO dto, UsuarioDTO user) throws CuentaContableException;

	/**
	 * Se encarga de eliminar una cuenta contable
	 * 
	 * @param dto  {@link CuentaContableDTO}
	 * @param user {@link UsuarioDTO}
	 * @throws {@link CuentaContableException}
	 */
	public void delete(CuentaContableDTO dto, UsuarioDTO user) throws CuentaContableException;

	/**
	 * Se encargad e obtener la cantidad de registros encotrados aplicando el filtro
	 * 
	 * @param dto {@link CuentaContableDTO}
	 * @throws {@link CuentaContableException}
	 */
	public Integer getTotalRows(CuentaContableDTO dto) throws CuentaContableException;

}
