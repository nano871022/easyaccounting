package com.pyt.service.interfaces.inventarios;

import java.util.List;

import org.pyt.common.common.UsuarioDTO;
import org.pyt.common.exceptions.inventario.ProductosException;
import org.pyt.common.exceptions.inventario.ResumenProductoException;

import com.pyt.service.dto.inventario.ProductoDto;
import com.pyt.service.dto.inventario.ResumenProductoDto;
/**
 * Se encarga el servicio de indicar los productos y resumens a 
 * @author Alejandro Parra
 * @since 01-12-2018
 */
public interface IProductosSvc {
	/**
	 * Se encargade ingresar nuevos productos
	 * @param producto {@link ProductoDto}
	 * @param usuario {@link UsuarioDTO}
	 * @return {@link ProductoDto}
	 * @throws {@link ProductosException}
	 */
	public ProductoDto insert(ProductoDto producto, UsuarioDTO usuario) throws ProductosException;
	/**
	 * Se encarga de ingresar nuevos resumen de productos
	 * @param resument {@link ResumenProductoDto}
	 * @param usuario {@link UsuarioDTO}
	 * @return {@link ResumenProductoDto}
	 * @throws {@link ResumenProductoException}
	 */
	public ResumenProductoDto insert(ResumenProductoDto resument,UsuarioDTO usuario)throws ResumenProductoException;
	/**
	 * Se encargade actualizar los productos
	 * @param producto  {@link ProductoDto}
	 * @param usuario {@link UsuarioDTO}
	 * @throws {@link ProductosException}
	 */
	public void update(ProductoDto producto,UsuarioDTO usuario)	throws ProductosException;
	/**
	 * Se encarga de actualizar los resumenes de productos
	 * @param producto {@link ResumenProductoDto}
	 * @param usuario {@link UsuarioDTO}
	 * @throws {@link ResumenProductoException}
	 */
	public void update(ResumenProductoDto producto,UsuarioDTO usuario)throws ResumenProductoException;
	/**
	 * Se encarga de obtener el producto buscado
	 * @param producto {@link ProductoDto}
	 * @return {@link ProductoDto}
	 * @throws {@link ProductosException}
	 */
	public ProductoDto producto(ProductoDto producto)throws ProductosException;
	/**
	 * Se encarga de obtner el resumen del producto
	 * @param resumentProducto {@link ResumenProductoDto}
	 * @return {@link ResumenProductoDto}
	 * @throws {@link ResumenProductoException}
	 */
	public ResumenProductoDto resumenProducto(ResumenProductoDto resumentProducto)throws ResumenProductoException;
	/**
	 * Se encarga de obtener listado de productos 
	 * @param producto {@link ProductoDto}
	 * @return {@link List} of {@link ProductoDto}
	 * @throws {@link ProductosException}
	 */
	public List<ProductoDto> productos(ProductoDto producto)throws ProductosException;
	/**
	 * Se encarga de obtner listado de resumenes de productos
	 * @param resumen {@link ResumenProductoDto}
	 * @return {@link List} of {@link ResumenProductoDto}
	 * @throws {@link ResumenProductoException}
	 */
	public List<ResumenProductoDto> resumenProductos(ResumenProductoDto resumen)throws ResumenProductoException;
	/**
	 * Se encarga de obtener listado de productso paginados
	 * @param producto {@link ProductoDto}
	 * @param inicio {@link Integer}
	 * @param cantidad {@link Integer}
	 * @return {@link List} of {@link ProductoDto}
	 * @throws {@link ProductosException}
	 */
	public List<ProductoDto> productos(ProductoDto producto,Integer inicio,Integer cantidad)throws ProductosException;
	/**
	 * Se encarga de obtener listado de resumens de productos paginados
	 * @param resumen {@link ResumenProductoDto}
	 * @param inicio {@link Integer}
	 * @param cantidad {@link Integer}
	 * @return {@link List} of {@link ResumenProductoDto}
	 * @throws {@link ResumenProductoException}
	 */
	public List<ResumenProductoDto> resumenProductos(ResumenProductoDto resumen,Integer inicio,Integer cantidad)throws ResumenProductoException;
	/**
	 * Se encarga de eliminar un producto
	 * @param producto {@link ProductoDto}
	 * @param usuario {@link UsuarioDTO}
	 * @throws {@link ProductosException}
	 */
	public void del(ProductoDto producto,UsuarioDTO usuario)throws ProductosException;
	/**
	 * Se encarga de eliminar un resumen del producto 
	 * @param resumen {@link ResumenProductoDto}
	 * @param usuario {@link UsuarioDTO}
	 * @throws {@link ResumenProductoException}
	 */
	public void del(ResumenProductoDto resumen,UsuarioDTO usuario)throws ResumenProductoException;

}
