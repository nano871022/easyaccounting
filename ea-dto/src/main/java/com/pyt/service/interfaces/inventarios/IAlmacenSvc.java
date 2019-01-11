package com.pyt.service.interfaces.inventarios;

import java.util.List;

import org.pyt.common.common.UsuarioDTO;
import org.pyt.common.exceptions.inventario.AlmacenException;

import com.pyt.service.dto.inventario.AlmacenDto;
import com.pyt.service.dto.inventario.ProductoUbicacionDto;
import com.pyt.service.dto.inventario.UbicacionDto;

/**
 * Se encarga de realizar ingreso de regisros de los almacenes, ubicacion y asignacion de producctos a las ubicaciones
 * @author Alejandro Parra
 * @since 01-12-2018
 */
public interface IAlmacenSvc {
	/**
	 * Se encarga de ingresar un registro en almacen
	 * @param almacen {@link AlmacenDto}
	 * @param usuario {@link UsuarioDTO}
	 * @return {@link AlmacenDto}
	 * @throws {@link AlmacenException}
	 */
	public AlmacenDto insert(AlmacenDto almacen, UsuarioDTO usuario)throws AlmacenException;
	/**
	 * Se encaga de ingresar un registro de ubicacion
	 * @param ubicacion {@link UbicacionDto}
	 * @param usuario {@link UsuarioDTO}
	 * @return {@link UbicacionDto}
	 * @throws {@link AlmacenException}
	 */
	public UbicacionDto insert(UbicacionDto ubicacion,UsuarioDTO usuario)throws AlmacenException;
	/**
	 * Se encarga de ingresar un registro de asignacion de un producto a una ubicacion
	 * @param asignar {@link ProductoUbicacionDto}
	 * @param usuario {@link UsuarioDTO}
	 * @return {@link ProductoUbicacionDto}
	 * @throws {@link AlmacenException}
	 */
	public ProductoUbicacionDto insert(ProductoUbicacionDto asignar,UsuarioDTO usuario)throws AlmacenException;
	/**
	 * Se encarga de actualizar un registro de almacen
	 * @param almacen {@link AlmacenDto}
	 * @param usuario {@link UsuarioDTO}
	 * @throws {@link AlmacenException}
	 */
	public void update(AlmacenDto almacen, UsuarioDTO usuario)throws AlmacenException;
	/**
	 * Se encarga de actualizar un registro de ubicaciones de un almacen
	 * @param ubicacion {@link UbicacionDto}
	 * @param usuario {@link UsuarioDTO}
	 * @throws {@link AlmacenException}
	 */
	public void update(UbicacionDto ubicacion,UsuarioDTO usuario)throws AlmacenException;
	/**
	 * Se encarga de actualizar un registro de asignacion del producto en la ubicacion
	 * @param asignar {@link ProductoUbicacionDto}
	 * @param usuario {@link UsuarioDTO}
	 * @throws {@link AlmacenException}
	 */
	public void update(ProductoUbicacionDto asignar,UsuarioDTO usuario)throws AlmacenException;
	/**
	 * Se encarga de obtener el registro buscando del almacen
	 * @param almacen {@link AlmacenDto}
	 * @param usuario {@link UsuarioDTO}
	 * @return {@link AlmacenDto}
	 * @throws {@link AlmacenException}
	 */
	public AlmacenDto almacen(AlmacenDto almacen)throws AlmacenException;
	/**
	 * Se encarga de obtener el regstro buscando de la ubicaicon
	 * @param ubicacion {@link UbicacionDto}
	 * @param usuario {@link UsuarioDTO}
	 * @return {@link UbicacionDto}
	 * @throws {@link AlmacenException}
	 */
	public UbicacionDto ubicacion(UbicacionDto ubicacion)throws AlmacenException;
	/**
	 * Se encarga de obtener el registro buscando de la asginacion del producto en la ubicacion
	 * @param asignar {@link ProductoUbicacionDto}
	 * @param usuario {@link UsuarioDTO}
	 * @return {@link ProductoUbicacionDto}
	 * @throws {@link AlmacenException}
	 */
	public ProductoUbicacionDto productoUbicacion(ProductoUbicacionDto asignar)throws AlmacenException;
	/**
	 * Se encarga de obtener los registros buscados de los almacenes
	 * @param almacen {@link AlmacenDto}
	 * @return {@link List} of {@link AlmacenDto}
	 * @throws {@link AlmacenException}
	 */
	public List<AlmacenDto> almacenes(AlmacenDto almacen)throws AlmacenException;
	/**
	 * Se encarga de obtener los registros buscados de la ubicacion
	 * @param ubicacion {@link UbicacionDto}
	 * @return {@link List} of {@link UbicacionDto}
	 * @throws {@link AlmacenException}
	 */
	public List<UbicacionDto> ubicaciones(UbicacionDto ubicacion)throws AlmacenException;
	/**
	 * Se encarga de obtener los registros biscados en la asignacion de productos en las ubicaciones
	 * @param asignar {@link ProductoUbicacionDto}
	 * @return {@link List} og {@link ProductoUbicacionDto}
	 * @throws {@link AlmacenException}
	 */
	public List<ProductoUbicacionDto> productoUbicaciones(ProductoUbicacionDto asignar)throws AlmacenException;
	/**
	 * Se encarga de obtener los registros buscados en los almacenes paginados
	 * @param almacen {@link AlmacenDto}
	 * @param inicio {@link Integer}
	 * @param cantidad {@link Integer}
	 * @return {@link List} og {@link AlmacenDto}
	 * @throws {@link AlmacenException}
	 */
	public List<AlmacenDto> almaceness(AlmacenDto almacen, Integer inicio,Integer cantidad)throws AlmacenException;
	/**
	 * Se encarga de obtener los registros buscados de las ubicaicones paginadas
	 * @param ubicacion {@link UbicacionDto}
	 * @param inicio {@link Integer}
	 * @param cantidad {@link Integer}
	 * @return {@link List} og {@link UbicacionDto}
	 * @throws {@link AlmacenException}
	 */
	public List<UbicacionDto> ubicaciones(UbicacionDto ubicacion, Integer inicio,Integer cantidad)throws AlmacenException;
	/**
	 * Se encarga de obtener los registros buscados de las asignaciones de productos en las ubicaciones paginadas
	 * @param asignar {@link ProductoUbicacionDto}
	 * @param inicio {@link Integer}
	 * @param cantidad {@link Integer}
	 * @return {@link List} of {@link ProductoUbicacionDto}
	 * @throws {@link AlmacenException}
	 */
	public List<ProductoUbicacionDto> productoUbicaciones(ProductoUbicacionDto asignar, Integer inicio,Integer cantidad)throws AlmacenException;
	/**
	 * Se encarga de eliminar un almacen
	 * @param almacen {@link AlmacenDto}
	 * @param usuario {@link UsuarioDTO}
	 * @throws {@link AlmacenException}
	 */
	public void del(AlmacenDto almacen,UsuarioDTO usuario)throws AlmacenException;
	/**
	 * Se encarga de eliminar una ubicacion
	 * @param almacen {@link UbicacionDto}
	 * @param usuario {@link UsuarioDTO}
	 * @throws {@link AlmacenException}
	 */
	public void del(UbicacionDto almacen,UsuarioDTO usuario)throws AlmacenException;
	/**
	 * Se encarga de eliminar un producto de ubicacion
	 * @param almacen {@link ProductoUbicacionDto}
	 * @param usuario {@link UsuarioDTO}
	 * @throws {@link AlmacenException}
	 */
	public void del(ProductoUbicacionDto almacen,UsuarioDTO usuario)throws AlmacenException;

}
