package com.pyt.service.interfaces.inventarios;

import java.util.List;

import org.pyt.common.exceptions.inventario.MovimientoException;

import com.pyt.service.dto.inventario.MovimientoDTO;
import com.pyt.service.dto.inventario.RestarCantidadDTO;
import com.pyt.service.dto.inventario.SaldoDTO;

import co.com.japl.ea.dto.system.UsuarioDTO;

/**
 * Se encarga de realizar los movimientos de los productos
 * @author Alejandro Parra
 * @since 01-12-2018
 */
public interface IMovimientoSvc {
	/**
	 * Se encarga de ingresar un restar cantidad
	 * @param movimiento {@link RestarCantidadDTO}
	 * @param usuario {@link UsuarioDTO}
	 * @return {@link RestarCantidadDTO}
	 * @throws {@link MovimientoException}
	 */
	public RestarCantidadDTO insert(RestarCantidadDTO restar,UsuarioDTO usuario)throws MovimientoException;
	/**
	 * 
	 * Se encarga de ingresar un movimiento
	 * @param movimiento {@link MovimientoDTO}
	 * @param usuario {@link UsuarioDTO}
	 * @return {@link MovimientoDTO}
	 * @throws {@link MovimientoException}
	 */
	public MovimientoDTO insert(MovimientoDTO movimiento,UsuarioDTO usuario)throws MovimientoException;
	/**
	 * Se encarga de ingresar un saldo
	 * @param saldo {@link SaldoDTO}
	 * @param usuario {@link UsuarioDTO}
	 * @return {@link SaldoDTO}
	 * @throws {@link MovimientoException}
	 */
	public SaldoDTO insert(SaldoDTO saldo,UsuarioDTO usuario)throws MovimientoException;
	/**
	 * Se encarga de actualizar para restar la cantidad
	 * @param movimiento {@link RestarCantidadDTO}
	 * @param usuario {@link UsuarioDTO}
	 * @throws {@link MovimientoException}
	 */
	public void update(RestarCantidadDTO restar, UsuarioDTO usuario)throws MovimientoException;
	/**
	 * Se encarga de actualizar unn movimiento 
	 * @param movimiento {@link MovimientoDTO}
	 * @param usuario {@link UsuarioDTO}
	 * @throws {@link MovimientoException}
	 */
	public void update(MovimientoDTO movimiento, UsuarioDTO usuario)throws MovimientoException;
	/**
	 * Se encarga de acualizar un saldo
	 * @param saldo {@link SaldoDTO}
	 * @param usuario {@link UsuarioDTO}
	 * @throws {@link MovimientoException}
	 */
	public void update(SaldoDTO saldo,UsuarioDTO usuario)throws MovimientoException;
	/**
	 * Se encarga de obtener un movimiento a buscar
	 * @param movimiento {@link MovimientoDTO}
	 * @return {@link MovimientoDTO}
	 * @throws {@link MovimientoException}
	 */
	public MovimientoDTO movimiento(MovimientoDTO movimiento)throws MovimientoException;
	/**
	 * Se encarga de obtener un saldo a buscar
	 * @param saldo {@link SaldoDTO}
	 * @return {@link SaldoDTO}
	 * @throws {@link MovimientoException}
	 */
	public SaldoDTO saldo(SaldoDTO saldo)throws MovimientoException	;
	/**
	 * Se encarga de otener los movimientos
	 * @param movimiento {@link MovimientoDTO}
	 * @return  {@link List} of {@link MovimientoDTO}
	 * @throws {@link MovimientoException}
	 */
	public List<MovimientoDTO> movimientos(MovimientoDTO movimiento)throws MovimientoException;
	/**
	 * Se encarga de otener los restar cantidad
	 * @param movimiento {@link RestarCantidadDTO}
	 * @return  {@link List} of {@link RestarCantidadDTO}
	 * @throws {@link MovimientoException}
	 */
	public List<RestarCantidadDTO> restarCantidad(RestarCantidadDTO restar)throws MovimientoException;

	/**
	 * Se encarga de obtener los saldos
	 * @param saldo {@link SaldoDTO}
	 * @return {@link List} of {@link SaldoDTO}
	 * @throws {@link MovimientoException}
	 */
	public List<SaldoDTO> saldos(SaldoDTO saldo)throws MovimientoException;
	/**
	 * Se encarga de obtener los movimientos paginados
	 * @param movimiento {@link MovimientoDTO}
	 * @param inicio {@link Integer}
	 * @param cantidad {@link Integer}
	 * @return {@link List} of {@link MovimientoDTO}
	 * @throws {@link MovimientoException}
	 */
	public List<MovimientoDTO> movimientos(MovimientoDTO movimiento,Integer inicio,Integer cantidad)throws MovimientoException;
	/**
	 * Se encarga de obtener los saldos pagonados
	 * @param saldo {@link SaldoDTO}
	 * @param inicio {@link Integer}
	 * @param cantidad {@link Integer}
	 * @return {@link List} of {@link MovimientoDTO}
	 * @throws {@link MovimientoException}
	 */
	public List<SaldoDTO> saldos(SaldoDTO saldo,Integer inicio,Integer cantidad)throws MovimientoException;
	/**
	 * Se encarga de eliminar el movimiento
	 * @param movimiento MovimientoDto
	 * @param usuario {@link UsuarioDTO}
	 * @throws {@link MovimientoException}
	 */
	public void del(MovimientoDTO movimiento,UsuarioDTO usuario)throws MovimientoException;
	/**
	 * Se encarga dde eliminar un saldo
	 * @param saldo {@link SaldoDTO}
	 * @param usuario {@link UsuarioDTO}
	 * @throws {@link MovimientoException}
	 */
	public void del(SaldoDTO saldo,UsuarioDTO usuario)throws MovimientoException;
	/**
	 * Se encarga dde eliminar un restar cantidad
	 * @param saldo {@link RestarCantidadDTO}
	 * @param usuario {@link UsuarioDTO}
	 * @throws {@link MovimientoException}
	 */
	public void del(RestarCantidadDTO saldo,UsuarioDTO usuario)throws MovimientoException;

}
