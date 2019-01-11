package com.pyt.service.interfaces.inventarios;

import java.util.List;

import org.pyt.common.common.UsuarioDTO;
import org.pyt.common.exceptions.inventario.MovimientoException;

import com.pyt.service.dto.inventario.MovimientoDto;
import com.pyt.service.dto.inventario.RestarCantidadDto;
import com.pyt.service.dto.inventario.saldoDto;

/**
 * Se encarga de realizar los movimientos de los productos
 * @author Alejandro Parra
 * @since 01-12-2018
 */
public interface IMovimientoSvc {
	/**
	 * Se encarga de ingresar un restar cantidad
	 * @param movimiento {@link RestarCantidadDto}
	 * @param usuario {@link UsuarioDTO}
	 * @return {@link RestarCantidadDto}
	 * @throws {@link MovimientoException}
	 */
	public RestarCantidadDto insert(RestarCantidadDto restar,UsuarioDTO usuario)throws MovimientoException;
	/**
	 * 
	 * Se encarga de ingresar un movimiento
	 * @param movimiento {@link MovimientoDto}
	 * @param usuario {@link UsuarioDTO}
	 * @return {@link MovimientoDto}
	 * @throws {@link MovimientoException}
	 */
	public MovimientoDto insert(MovimientoDto movimiento,UsuarioDTO usuario)throws MovimientoException;
	/**
	 * Se encarga de ingresar un saldo
	 * @param saldo {@link saldoDto}
	 * @param usuario {@link UsuarioDTO}
	 * @return {@link saldoDto}
	 * @throws {@link MovimientoException}
	 */
	public saldoDto insert(saldoDto saldo,UsuarioDTO usuario)throws MovimientoException;
	/**
	 * Se encarga de actualizar para restar la cantidad
	 * @param movimiento {@link RestarCantidadDto}
	 * @param usuario {@link UsuarioDTO}
	 * @throws {@link MovimientoException}
	 */
	public void update(RestarCantidadDto restar, UsuarioDTO usuario)throws MovimientoException;
	/**
	 * Se encarga de actualizar unn movimiento 
	 * @param movimiento {@link MovimientoDto}
	 * @param usuario {@link UsuarioDTO}
	 * @throws {@link MovimientoException}
	 */
	public void update(MovimientoDto movimiento, UsuarioDTO usuario)throws MovimientoException;
	/**
	 * Se encarga de acualizar un saldo
	 * @param saldo {@link saldoDto}
	 * @param usuario {@link UsuarioDTO}
	 * @throws {@link MovimientoException}
	 */
	public void update(saldoDto saldo,UsuarioDTO usuario)throws MovimientoException;
	/**
	 * Se encarga de obtener un movimiento a buscar
	 * @param movimiento {@link MovimientoDto}
	 * @return {@link MovimientoDto}
	 * @throws {@link MovimientoException}
	 */
	public MovimientoDto movimiento(MovimientoDto movimiento)throws MovimientoException;
	/**
	 * Se encarga de obtener un saldo a buscar
	 * @param saldo {@link saldoDto}
	 * @return {@link saldoDto}
	 * @throws {@link MovimientoException}
	 */
	public saldoDto saldo(saldoDto saldo)throws MovimientoException	;
	/**
	 * Se encarga de otener los movimientos
	 * @param movimiento {@link MovimientoDto}
	 * @return  {@link List} of {@link MovimientoDto}
	 * @throws {@link MovimientoException}
	 */
	public List<MovimientoDto> movimientos(MovimientoDto movimiento)throws MovimientoException;
	/**
	 * Se encarga de otener los restar cantidad
	 * @param movimiento {@link RestarCantidadDto}
	 * @return  {@link List} of {@link RestarCantidadDto}
	 * @throws {@link MovimientoException}
	 */
	public List<RestarCantidadDto> restarCantidad(RestarCantidadDto restar)throws MovimientoException;

	/**
	 * Se encarga de obtener los saldos
	 * @param saldo {@link saldoDto}
	 * @return {@link List} of {@link saldoDto}
	 * @throws {@link MovimientoException}
	 */
	public List<saldoDto> saldos(saldoDto saldo)throws MovimientoException;
	/**
	 * Se encarga de obtener los movimientos paginados
	 * @param movimiento {@link MovimientoDto}
	 * @param inicio {@link Integer}
	 * @param cantidad {@link Integer}
	 * @return {@link List} of {@link MovimientoDto}
	 * @throws {@link MovimientoException}
	 */
	public List<MovimientoDto> movimientos(MovimientoDto movimiento,Integer inicio,Integer cantidad)throws MovimientoException;
	/**
	 * Se encarga de obtener los saldos pagonados
	 * @param saldo {@link saldoDto}
	 * @param inicio {@link Integer}
	 * @param cantidad {@link Integer}
	 * @return {@link List} of {@link MovimientoDto}
	 * @throws {@link MovimientoException}
	 */
	public List<saldoDto> saldos(saldoDto saldo,Integer inicio,Integer cantidad)throws MovimientoException;
	/**
	 * Se encarga de eliminar el movimiento
	 * @param movimiento MovimientoDto
	 * @param usuario {@link UsuarioDTO}
	 * @throws {@link MovimientoException}
	 */
	public void del(MovimientoDto movimiento,UsuarioDTO usuario)throws MovimientoException;
	/**
	 * Se encarga dde eliminar un saldo
	 * @param saldo {@link saldoDto}
	 * @param usuario {@link UsuarioDTO}
	 * @throws {@link MovimientoException}
	 */
	public void del(saldoDto saldo,UsuarioDTO usuario)throws MovimientoException;
	/**
	 * Se encarga dde eliminar un restar cantidad
	 * @param saldo {@link RestarCantidadDto}
	 * @param usuario {@link UsuarioDTO}
	 * @throws {@link MovimientoException}
	 */
	public void del(RestarCantidadDto saldo,UsuarioDTO usuario)throws MovimientoException;

}
