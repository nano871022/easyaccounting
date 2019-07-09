package com.pyt.service.interfaces.inventarios;

import java.util.List;

import org.pyt.common.common.UsuarioDTO;
import org.pyt.common.exceptions.inventario.ProductosException;
import org.pyt.common.exceptions.inventario.ResumenProductoException;

import com.pyt.service.dto.inventario.ProductoDTO;
import com.pyt.service.dto.inventario.ResumenProductoDTO;
/**
 * Se encarga el servicio de indicar los productos y resumens a 
 * @author Alejandro Parra
 * @since 01-12-2018
 */
public interface IProductosSvc {
	/**
	 * Se encargade ingresar nuevos productos
	 * @param producto {@link ProductoDTO}
	 * @param usuario {@link UsuarioDTO}
	 * @return {@link ProductoDTO}
	 * @throws {@link ProductosException}
	 */
	public ProductoDTO insert(ProductoDTO producto, UsuarioDTO usuario) throws ProductosException;
	/**
	 * Se encarga de ingresar nuevos resumen de productos
	 * @param resument {@link ResumenProductoDTO}
	 * @param usuario {@link UsuarioDTO}
	 * @return {@link ResumenProductoDTO}
	 * @throws {@link ResumenProductoException}
	 */
	public ResumenProductoDTO insert(ResumenProductoDTO resument,UsuarioDTO usuario)throws ResumenProductoException;
	/**
	 * Se encarga de contar los registros encontrados con el filtro aplicado
	 * @param dto {@link ProductoDTO}
	 * @return {@link Integer}
	 * @throws {@link ProductosException}
	 */
	public Integer getTotalRows(ProductoDTO dto)throws ProductosException;
	/**
	 * Se encargade actualizar los productos
	 * @param producto  {@link ProductoDTO}
	 * @param usuario {@link UsuarioDTO}
	 * @throws {@link ProductosException}
	 */
	public void update(ProductoDTO producto,UsuarioDTO usuario)	throws ProductosException;
	/**
	 * Se encarga de actualizar los resumenes de productos
	 * @param producto {@link ResumenProductoDTO}
	 * @param usuario {@link UsuarioDTO}
	 * @throws {@link ResumenProductoException}
	 */
	public void update(ResumenProductoDTO producto,UsuarioDTO usuario)throws ResumenProductoException;
	/**
	 * Se encarga de obtener el producto buscado
	 * @param producto {@link ProductoDTO}
	 * @return {@link ProductoDTO}
	 * @throws {@link ProductosException}
	 */
	public ProductoDTO producto(ProductoDTO producto)throws ProductosException;
	/**
	 * Se encarga de obtner el resumen del producto
	 * @param resumentProducto {@link ResumenProductoDTO}
	 * @return {@link ResumenProductoDTO}
	 * @throws {@link ResumenProductoException}
	 */
	public ResumenProductoDTO resumenProducto(ResumenProductoDTO resumentProducto)throws ResumenProductoException;
	/**
	 * Se encarga de obtener un resumen del producto apartir del producto asociado
	 * @param producto {@link ProductoDTO}
	 * @return {@link ResumenProductoDTO}
	 * @throws {@link ResumenProductoException}
	 */
	public ResumenProductoDTO resumenProducto(ProductoDTO producto)throws ResumenProductoException;
	/**
	 * Se encarga de obtener listado de productos 
	 * @param producto {@link ProductoDTO}
	 * @return {@link List} of {@link ProductoDTO}
	 * @throws {@link ProductosException}
	 */
	public List<ProductoDTO> productos(ProductoDTO producto)throws ProductosException;
	/**
	 * Se encarga de obtner listado de resumenes de productos
	 * @param resumen {@link ResumenProductoDTO}
	 * @return {@link List} of {@link ResumenProductoDTO}
	 * @throws {@link ResumenProductoException}
	 */
	public List<ResumenProductoDTO> resumenProductos(ResumenProductoDTO resumen)throws ResumenProductoException;
	/**
	 * Se encarga de obtener listado de productso paginados
	 * @param producto {@link ProductoDTO}
	 * @param inicio {@link Integer}
	 * @param cantidad {@link Integer}
	 * @return {@link List} of {@link ProductoDTO}
	 * @throws {@link ProductosException}
	 */
	public List<ProductoDTO> productos(ProductoDTO producto,Integer inicio,Integer cantidad)throws ProductosException;
	/**
	 * Se encarga de obtener listado de resumens de productos paginados
	 * @param resumen {@link ResumenProductoDTO}
	 * @param inicio {@link Integer}
	 * @param cantidad {@link Integer}
	 * @return {@link List} of {@link ResumenProductoDTO}
	 * @throws {@link ResumenProductoException}
	 */
	public List<ResumenProductoDTO> resumenProductos(ResumenProductoDTO resumen,Integer inicio,Integer cantidad)throws ResumenProductoException;
	/**
	 * Se encarga de eliminar un producto
	 * @param producto {@link ProductoDTO}
	 * @param usuario {@link UsuarioDTO}
	 * @throws {@link ProductosException}
	 */
	public void del(ProductoDTO producto,UsuarioDTO usuario)throws ProductosException;
	/**
	 * Se encarga de eliminar un resumen del producto 
	 * @param resumen {@link ResumenProductoDTO}
	 * @param usuario {@link UsuarioDTO}
	 * @throws {@link ResumenProductoException}
	 */
	public void del(ResumenProductoDTO resumen,UsuarioDTO usuario)throws ResumenProductoException;

}
