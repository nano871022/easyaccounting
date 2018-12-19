package com.pyt.service.implement.inventario;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.pyt.common.annotations.Inject;
import org.pyt.common.common.UsuarioDTO;
import org.pyt.common.exceptions.inventario.InventarioException;
import org.pyt.common.exceptions.inventario.MovimientoException;
import org.pyt.common.exceptions.inventario.ResumenProductoException;

import com.pyt.service.abstracts.Services;
import com.pyt.service.dto.inventario.MovimientoDto;
import com.pyt.service.dto.inventario.ProductoDto;
import com.pyt.service.dto.inventario.RestarCantidadDto;
import com.pyt.service.dto.inventario.ResumenProductoDto;
import com.pyt.service.dto.inventario.saldoDto;
import com.pyt.service.interfaces.inventarios.IInventarioSvc;
import com.pyt.service.interfaces.inventarios.IMovimientoSvc;
import com.pyt.service.interfaces.inventarios.IProductosSvc;

public class InventarioSvc extends Services implements IInventarioSvc {
	@Inject(resource = "com.pyt.service.implement.inventario.MovimientoSvc")
	private IMovimientoSvc movimientoSvc;
	@Inject(resource = "com.pyt.service.implement.inventario.ProductosSvc")
	private IProductosSvc productosSvc;

	@Override
	public void agregarInventario(MovimientoDto dto, UsuarioDTO usuario) throws InventarioException {
		if (dto == null)
			throw new InventarioException("No se suministro el movimiento.");
		if (usuario == null)
			throw new InventarioException("No se suministro el usuario.");
		if (dto.getProducto() == null)
			throw new InventarioException("El movimiento no tiene el producto.");
		if (StringUtils.isBlank(dto.getProducto().getCodigo()))
			throw new InventarioException("El producto suministrado no se encuentra creado.");
		try {
			dto = movimientoSvc.insert(dto, usuario);
			ResumenProductoDto resumen = new ResumenProductoDto();
			resumen.setProducto(dto.getProducto());
			List<ResumenProductoDto> list = productosSvc.resumenProductos(resumen);
			if (list == null || list.size() == 0) {
				resumen.setCantidad(0);
				resumen.setValorCompra(dto.getPrecioCompra());
				resumen = productosSvc.insert(resumen, usuario);
			} else if (list.size() > 1) {
				movimientoSvc.del(dto, usuario);
				throw new InventarioException("Se encontraron varios resumenes, contacte con el administrador.");
			}
			saldoDto saldoOld = ultimoSaldo(resumen.getProducto());
			resumen = list.get(0);
			if(resumen.getCantidad() == null) {
				resumen.setCantidad(0);
			}
			resumen.setCantidad(resumen.getCantidad() + dto.getCantidad());
			productosSvc.update(resumen, usuario);
			BigDecimal totalMovimiento = dto.getPrecioCompra().multiply(new BigDecimal(dto.getCantidad()));
			saldoDto saldo = new saldoDto();
			saldo.setMovimiento(dto);
			saldo.setCantidad(resumen.getCantidad());
			saldo.setTotal(saldoOld.getTotal().add(totalMovimiento));
			saldo = movimientoSvc.insert(saldo, usuario);
		} catch (MovimientoException e) {
			throw new InventarioException("Se presento error con el ingreso del movimiento.", e);
		} catch (ResumenProductoException e) {
			throw new InventarioException("Se presento error en la actualizacion del resumen del producto.", e);
		}
		/**
		 * Para agrar un movimeitno en el kardex se realiza: 1. se agrega el movimiento
		 * a la tabla de movimientos 2. se busca en la tabla de productos el producto
		 * anterior 3. se ajusta la cantidad de valores. 4. se ajusta el valor de compra
		 * y venta para los valores segun si es fifo o lifo
		 */

	}

	@Override
	public void retirarInventario(MovimientoDto dto, UsuarioDTO usuario) throws InventarioException {
		if (dto == null)
			throw new InventarioException("No se suministro el movimiento.");
		if (usuario == null)
			throw new InventarioException("No se suministro el usuario.");
		if (dto.getProducto() == null)
			throw new InventarioException("El movimiento no tiene el producto.");
		if (StringUtils.isBlank(dto.getProducto().getCodigo()))
			throw new InventarioException("El producto suministrado no se encuentra creado.");
		try {
			dto = movimientoSvc.insert(dto, usuario);
			ResumenProductoDto resumen = new ResumenProductoDto();
			resumen.setProducto(dto.getProducto());
			List<ResumenProductoDto> list = productosSvc.resumenProductos(resumen);
			if (list == null || list.size() == 0) {
				movimientoSvc.del(dto, usuario);
				throw new InventarioException("No se encontro resumen, contacte con el administrador.");
			} else if (list.size() > 1) {
				movimientoSvc.del(dto, usuario);
				throw new InventarioException("Se encontraron varios resumenes, contacte con el administrador.");
			}
			saldoDto saldoOld = ultimoSaldo(resumen.getProducto());
			resumen = list.get(0);
			resumen.setCantidad(resumen.getCantidad() - dto.getCantidad());
			productosSvc.update(resumen, usuario);
			BigDecimal totalMovimiento = dto.getPrecioCompra().multiply(new BigDecimal(dto.getCantidad()));
			saldoDto saldo = new saldoDto();
			saldo.setMovimiento(dto);
			saldo.setCantidad(resumen.getCantidad());
			saldo.setTotal(saldoOld.getTotal().subtract(totalMovimiento));
			saldo = movimientoSvc.insert(saldo, usuario);
			RestarCantidadDto restar = restarCantidad(dto.getProducto(), usuario);
			restar.setCantidad(restar.getCantidad()-dto.getCantidad());
			movimientoSvc.update(restar, usuario);
		} catch (MovimientoException e) {
			throw new InventarioException("Se presento error con el ingreso del movimiento.", e);
		} catch (ResumenProductoException e) {
			throw new InventarioException("Se presento error en la actualizacion del resumen del producto.", e);
		}

		/**
		 * Para agregar un movimiento en el kadex de retiro: 1. se agrega en la tabla el
		 * movminiento 2. se busca en la tabla de productos el producto anterior 3. se
		 * ajusta la cantida de valores 4. se ajusta el valor de compra y venta para los
		 * valores segun es fifo y lifo.
		 */
	}

	private final RestarCantidadDto restarCantidad(ProductoDto producto, UsuarioDTO usuario)
			throws InventarioException {
		try {
			MovimientoDto movimiento = new MovimientoDto();
			movimiento.setProducto(producto);
			Date fecha = null;
			RestarCantidadDto primero = null;
			List<MovimientoDto> movimientos = movimientoSvc.movimientos(movimiento);
			for (MovimientoDto mov : movimientos) {
				RestarCantidadDto restar = new RestarCantidadDto();
				restar.setMovimiento(mov);
				List<RestarCantidadDto> restars = movimientoSvc.restarCantidad(restar);
				if (restars == null || restars.size() == 0) {
					restar.setCantidad(mov.getCantidad());
					return movimientoSvc.insert(restar, usuario);
				}
				if(restars.size() > 1) {
					throw new InventarioException("Se encontraron varios registros de restar cantidad.");
				}
				if (restars.size() == 1) {
					restar = restars.get(0);
				}
				if (restar.getCantidad() > 0) {
					if (fecha == null || fecha.compareTo(restar.getFechaCreacion()) <= 0) {
						fecha = restar.getFechaCreacion();
						primero = restar;
					}
				}
			}
			return primero;
		} catch (MovimientoException e) {
			throw new InventarioException("Se presento problema con la obtencion del registro a restar cantidad.", e);
		}
	}

	/**
	 * Se encarga de obtener el ultimo saldo
	 * 
	 * @param producto
	 *            {@link ProductoDto}
	 * @return {@link saldoDto}
	 * @throws {@link
	 *             InventarioException}
	 */
	private final saldoDto ultimoSaldo(ProductoDto producto) throws InventarioException {
		if (producto == null || StringUtils.isBlank(producto.getCodigo()))
			throw new InventarioException("producto no suministrado.");
		try {
			MovimientoDto movimiento = new MovimientoDto();
			movimiento.setProducto(producto);
			List<MovimientoDto> list = movimientoSvc.movimientos(movimiento);
			Date fecha = null;
			movimiento = null;
			for (MovimientoDto mov : list) {
				if (fecha == null) {
					fecha = mov.getFechaCreacion();
					movimiento = mov;
					continue;
				}
				if (fecha.compareTo(mov.getFechaCreacion()) <= 0) {
					fecha = mov.getFechaCreacion();
					movimiento = mov;
				}
			}
			saldoDto saldo = new saldoDto();
			saldo.setMovimiento(movimiento);
			List<saldoDto> saldos = movimientoSvc.saldos(saldo);
			if (saldos == null || saldos.size() == 0) {
				saldo.setCantidad(0);
				saldo.setTotal(new BigDecimal(0));
				return saldo;
			}
			if (saldos.size() > 1) {
				throw new InventarioException("Se encontraron varios saldos.");
			}
			saldo = saldos.get(0);
			return saldo;
		} catch (MovimientoException e) {
			throw new InventarioException("Se presento un problema en la obtencion del movimiento.", e);
		}
	}
}
