package co.com.japl.ea.dto.interfaces.inventarios;

import co.com.japl.ea.dto.dto.inventario.MovimientoDTO;
import co.com.japl.ea.dto.system.UsuarioDTO;
import co.com.japl.ea.exceptions.inventario.InventarioException;

/**
 * Se encarga de realiazar ingresos de inventarios como movimientos de los
 * mismos en forma kardex
 * 
 * @author Alejandro Parra
 * @since 01-12-2018
 */
public interface IInventarioSvc {
	/**
	 * Se enccarga de agregar la cantidad de productos en el inventario y realizar los movimientos necesarios
	 * @param dto {@link MovimientoDTO}
	 * @param usuario {@link UsuarioDTO}
	 * @throws {@link InventarioException}
	 */
	public void agregarInventario(MovimientoDTO dto,UsuarioDTO usuario)throws InventarioException;
	/**
	 * Se encarga de retirar un producto del inventarioy segun la configuracion lifo y fifo y segun la cantidad se cuadra los precios de compra y venta
	 * @param dto {@link MovimientoDTO}
	 * @param usuario {@link UsuarioDTO}
	 * @throws {@link InventarioException}
	 */
	public void retirarInventario(MovimientoDTO dto,UsuarioDTO usuario)throws InventarioException;
}
