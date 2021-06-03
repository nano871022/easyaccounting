package co.com.japl.ea.dto.interfaces.inventarios;

import java.util.List;

import co.com.japl.ea.dto.dto.inventario.AlmacenDTO;
import co.com.japl.ea.dto.dto.inventario.ProductoUbicacionDTO;
import co.com.japl.ea.dto.dto.inventario.UbicacionDTO;
import co.com.japl.ea.dto.system.UsuarioDTO;
import co.com.japl.ea.exceptions.inventario.AlmacenException;

/**
 * Se encarga de realizar ingreso de regisros de los almacenes, ubicacion y asignacion de producctos a las ubicaciones
 * @author Alejandro Parra
 * @since 01-12-2018
 */
public interface IAlmacenSvc {
	/**
	 * Se encarga de ingresar un registro en almacen
	 * @param almacen {@link AlmacenDTO}
	 * @param usuario {@link UsuarioDTO}
	 * @return {@link AlmacenDTO}
	 * @throws {@link AlmacenException}
	 */
	public AlmacenDTO insert(AlmacenDTO almacen, UsuarioDTO usuario)throws AlmacenException;
	/**
	 * Se encaga de ingresar un registro de ubicacion
	 * @param ubicacion {@link UbicacionDTO}
	 * @param usuario {@link UsuarioDTO}
	 * @return {@link UbicacionDTO}
	 * @throws {@link AlmacenException}
	 */
	public UbicacionDTO insert(UbicacionDTO ubicacion,UsuarioDTO usuario)throws AlmacenException;
	/**
	 * Se encarga de ingresar un registro de asignacion de un producto a una ubicacion
	 * @param asignar {@link ProductoUbicacionDTO}
	 * @param usuario {@link UsuarioDTO}
	 * @return {@link ProductoUbicacionDTO}
	 * @throws {@link AlmacenException}
	 */
	public ProductoUbicacionDTO insert(ProductoUbicacionDTO asignar,UsuarioDTO usuario)throws AlmacenException;
	/**
	 * Se encarga de actualizar un registro de almacen
	 * @param almacen {@link AlmacenDTO}
	 * @param usuario {@link UsuarioDTO}
	 * @throws {@link AlmacenException}
	 */
	public void update(AlmacenDTO almacen, UsuarioDTO usuario)throws AlmacenException;
	/**
	 * Se encarga de actualizar un registro de ubicaciones de un almacen
	 * @param ubicacion {@link UbicacionDTO}
	 * @param usuario {@link UsuarioDTO}
	 * @throws {@link AlmacenException}
	 */
	public void update(UbicacionDTO ubicacion,UsuarioDTO usuario)throws AlmacenException;
	/**
	 * Se encarga de actualizar un registro de asignacion del producto en la ubicacion
	 * @param asignar {@link ProductoUbicacionDTO}
	 * @param usuario {@link UsuarioDTO}
	 * @throws {@link AlmacenException}
	 */
	public void update(ProductoUbicacionDTO asignar,UsuarioDTO usuario)throws AlmacenException;
	/**
	 * Se encarga de obtener el registro buscando del almacen
	 * @param almacen {@link AlmacenDTO}
	 * @param usuario {@link UsuarioDTO}
	 * @return {@link AlmacenDTO}
	 * @throws {@link AlmacenException}
	 */
	public AlmacenDTO almacen(AlmacenDTO almacen)throws AlmacenException;
	/**
	 * Se encarga de obtener el regstro buscando de la ubicaicon
	 * @param ubicacion {@link UbicacionDTO}
	 * @param usuario {@link UsuarioDTO}
	 * @return {@link UbicacionDTO}
	 * @throws {@link AlmacenException}
	 */
	public UbicacionDTO ubicacion(UbicacionDTO ubicacion)throws AlmacenException;
	/**
	 * Se encarga de obtener el registro buscando de la asginacion del producto en la ubicacion
	 * @param asignar {@link ProductoUbicacionDTO}
	 * @param usuario {@link UsuarioDTO}
	 * @return {@link ProductoUbicacionDTO}
	 * @throws {@link AlmacenException}
	 */
	public ProductoUbicacionDTO productoUbicacion(ProductoUbicacionDTO asignar)throws AlmacenException;
	/**
	 * Se encarga de obtener los registros buscados de los almacenes
	 * @param almacen {@link AlmacenDTO}
	 * @return {@link List} of {@link AlmacenDTO}
	 * @throws {@link AlmacenException}
	 */
	public List<AlmacenDTO> almacenes(AlmacenDTO almacen)throws AlmacenException;
	/**
	 * Se encarga de obtener los registros buscados de la ubicacion
	 * @param ubicacion {@link UbicacionDTO}
	 * @return {@link List} of {@link UbicacionDTO}
	 * @throws {@link AlmacenException}
	 */
	public List<UbicacionDTO> ubicaciones(UbicacionDTO ubicacion)throws AlmacenException;
	/**
	 * Se encarga de obtener los registros biscados en la asignacion de productos en las ubicaciones
	 * @param asignar {@link ProductoUbicacionDTO}
	 * @return {@link List} og {@link ProductoUbicacionDTO}
	 * @throws {@link AlmacenException}
	 */
	public List<ProductoUbicacionDTO> productoUbicaciones(ProductoUbicacionDTO asignar)throws AlmacenException;
	/**
	 * Se encarga de obtener los registros buscados en los almacenes paginados
	 * @param almacen {@link AlmacenDTO}
	 * @param inicio {@link Integer}
	 * @param cantidad {@link Integer}
	 * @return {@link List} og {@link AlmacenDTO}
	 * @throws {@link AlmacenException}
	 */
	public List<AlmacenDTO> almaceness(AlmacenDTO almacen, Integer inicio,Integer cantidad)throws AlmacenException;
	/**
	 * Se encarga de obtener los registros buscados de las ubicaicones paginadas
	 * @param ubicacion {@link UbicacionDTO}
	 * @param inicio {@link Integer}
	 * @param cantidad {@link Integer}
	 * @return {@link List} og {@link UbicacionDTO}
	 * @throws {@link AlmacenException}
	 */
	public List<UbicacionDTO> ubicaciones(UbicacionDTO ubicacion, Integer inicio,Integer cantidad)throws AlmacenException;
	/**
	 * Se encarga de obtener los registros buscados de las asignaciones de productos en las ubicaciones paginadas
	 * @param asignar {@link ProductoUbicacionDTO}
	 * @param inicio {@link Integer}
	 * @param cantidad {@link Integer}
	 * @return {@link List} of {@link ProductoUbicacionDTO}
	 * @throws {@link AlmacenException}
	 */
	public List<ProductoUbicacionDTO> productoUbicaciones(ProductoUbicacionDTO asignar, Integer inicio,Integer cantidad)throws AlmacenException;
	/**
	 * Se encarga de eliminar un almacen
	 * @param almacen {@link AlmacenDTO}
	 * @param usuario {@link UsuarioDTO}
	 * @throws {@link AlmacenException}
	 */
	public void del(AlmacenDTO almacen,UsuarioDTO usuario)throws AlmacenException;
	/**
	 * Se encarga de eliminar una ubicacion
	 * @param almacen {@link UbicacionDTO}
	 * @param usuario {@link UsuarioDTO}
	 * @throws {@link AlmacenException}
	 */
	public void del(UbicacionDTO almacen,UsuarioDTO usuario)throws AlmacenException;
	/**
	 * Se encarga de eliminar un producto de ubicacion
	 * @param almacen {@link ProductoUbicacionDTO}
	 * @param usuario {@link UsuarioDTO}
	 * @throws {@link AlmacenException}
	 */
	public void del(ProductoUbicacionDTO almacen,UsuarioDTO usuario)throws AlmacenException;

}
