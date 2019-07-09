package com.pyt.service.implement.inventario;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.pyt.common.annotations.Inject;
import org.pyt.common.common.UsuarioDTO;
import org.pyt.common.exceptions.QueryException;
import org.pyt.common.exceptions.inventario.AlmacenException;

import com.pyt.query.interfaces.IQuerySvc;
import com.pyt.service.abstracts.Services;
import com.pyt.service.dto.inventario.AlmacenDTO;
import com.pyt.service.dto.inventario.ProductoUbicacionDTO;
import com.pyt.service.dto.inventario.UbicacionDTO;
import com.pyt.service.interfaces.inventarios.IAlmacenSvc;

public class AlmacenSvc extends Services implements IAlmacenSvc {
	@Inject(resource = "com.pyt.query.implement.QuerySvc")
	private IQuerySvc querySvc;

	@Override
	public AlmacenDTO insert(AlmacenDTO almacen, UsuarioDTO usuario) throws AlmacenException {
		if(almacen == null )throw new AlmacenException("No se encontro almacen suministrado.");
		if(usuario == null)throw new AlmacenException("No se encontro usuario suministrado.");
		if(StringUtils.isNotBlank(almacen.getCodigo()))throw new AlmacenException("El almacen ya se encuentra creado.");
		try {
			return querySvc.set(almacen, usuario);
		}catch (QueryException e) {
			throw new AlmacenException("Se encontro problema en el ingreso del registro.",e);
		}
	}

	@Override
	public UbicacionDTO insert(UbicacionDTO ubicacion, UsuarioDTO usuario) throws AlmacenException {
		if(ubicacion == null )throw new AlmacenException("No se encontro ubicaicon suministrado.");
		if(usuario == null)throw new AlmacenException("No se encontro usuario suministrado.");
		if(StringUtils.isNotBlank(ubicacion.getCodigo()))throw new AlmacenException("La ubicacion ya se encuentra creado.");
		try {
			return querySvc.set(ubicacion, usuario);
		}catch (QueryException e) {
			throw new AlmacenException("Se encontro problema en el ingreso del registro.",e);
		}
	}

	@Override
	public ProductoUbicacionDTO insert(ProductoUbicacionDTO asignar, UsuarioDTO usuario) throws AlmacenException {
		if(asignar == null )throw new AlmacenException("No se encontro el producto a asignar a ubicacion suministrado.");
		if(usuario == null)throw new AlmacenException("No se encontro usuario suministrado.");
		if(StringUtils.isNotBlank(asignar.getCodigo()))throw new AlmacenException("El producto a asignar al aubicacion  ya se encuentra creado.");
		try {
			return querySvc.set(asignar, usuario);
		}catch (QueryException e) {
			throw new AlmacenException("Se encontro problema en el ingreso del registro.",e);
		}
	}

	@Override
	public void update(AlmacenDTO almacen, UsuarioDTO usuario) throws AlmacenException {
		if(almacen == null )throw new AlmacenException("No se encontro el almacen a actualizar suministrado.");
		if(usuario == null)throw new AlmacenException("No se encontro usuario suministrado.");
		if(StringUtils.isBlank(almacen.getCodigo()))throw new AlmacenException("El almacen a actualizar no se encuentra creado.");
		try {
			querySvc.set(almacen, usuario);
		}catch (QueryException e) {
			throw new AlmacenException("Se encontro problema en la actualizacion del registro.",e);
		}
		
	}

	@Override
	public void update(UbicacionDTO ubicacion, UsuarioDTO usuario) throws AlmacenException {
		if(ubicacion == null )throw new AlmacenException("No se encontro la ubicacion suministrado.");
		if(usuario == null)throw new AlmacenException("No se encontro usuario suministrado.");
		if(StringUtils.isBlank(ubicacion.getCodigo()))throw new AlmacenException("La aubicacion  no se encuentra creado.");
		try {
			querySvc.set(ubicacion, usuario);
		}catch (QueryException e) {
			throw new AlmacenException("Se encontro problema en la actualizacion del registro.",e);
		}
	}

	@Override
	public void update(ProductoUbicacionDTO asignar, UsuarioDTO usuario) throws AlmacenException {
		if(asignar == null )throw new AlmacenException("No se encontro el producto a asignar a ubicacion suministrado.");
		if(usuario == null)throw new AlmacenException("No se encontro usuario suministrado.");
		if(StringUtils.isBlank(asignar.getCodigo()))throw new AlmacenException("El producto a asignar la aubicacion  no se encuentra creado.");
		try {
			querySvc.set(asignar, usuario);
		}catch (QueryException e) {
			throw new AlmacenException("Se encontro problema en la actualizacion del registro.",e);
		}
	}

	@Override
	public AlmacenDTO almacen(AlmacenDTO almacen) throws AlmacenException {
		if(almacen== null )throw new AlmacenException("No se encontro el almacen suministrado.");
		try {
			return querySvc.get(almacen);
		}catch (QueryException e) {
			throw new AlmacenException("Se encontro problema en la obtancion del registro.",e);
		}
	}

	@Override
	public UbicacionDTO ubicacion(UbicacionDTO ubicacion) throws AlmacenException {
		if(ubicacion == null )throw new AlmacenException("No se encontro ubicacion suministrado.");
		try {
			return querySvc.get(ubicacion);
		}catch (QueryException e) {
			throw new AlmacenException("Se encontro problema en la obtancion del registro.",e);
		}
	}

	@Override
	public ProductoUbicacionDTO productoUbicacion(ProductoUbicacionDTO asignar)
			throws AlmacenException {
		if(asignar == null )throw new AlmacenException("No se encontro el producto a asignar a ubicacion suministrado.");
		try {
			return querySvc.get(asignar);
		}catch (QueryException e) {
			throw new AlmacenException("Se encontro problema en la obtancion del registro.",e);
		}
	}

	@Override
	public List<AlmacenDTO> almacenes(AlmacenDTO almacen) throws AlmacenException {
		if(almacen == null )throw new AlmacenException("No se encontro almacen suministrado.");
		try {
			return querySvc.gets(almacen);
		}catch (QueryException e) {
			throw new AlmacenException("Se encontro problema en la obtancion del registro.",e);
		}
	}

	@Override
	public List<UbicacionDTO> ubicaciones(UbicacionDTO ubicacion) throws AlmacenException {
		if(ubicacion == null )throw new AlmacenException("No se encontro la ubicacion suministrado.");
		try {
			return querySvc.gets(ubicacion);
		}catch (QueryException e) {
			throw new AlmacenException("Se encontro problema en la obtancion del registro.",e);
		}
	}

	@Override
	public List<ProductoUbicacionDTO> productoUbicaciones(ProductoUbicacionDTO asignar) throws AlmacenException {
		if(asignar == null )throw new AlmacenException("No se encontro el producto a asignar a ubicacion suministrado.");
		try {
			return querySvc.gets(asignar);
		}catch (QueryException e) {
			throw new AlmacenException("Se encontro problema en la obtancion del registro.",e);
		}
	}

	@Override
	public List<AlmacenDTO> almaceness(AlmacenDTO almacen, Integer inicio, Integer cantidad) throws AlmacenException {
		if(almacen == null )throw new AlmacenException("No se encontro el almacen suministrado.");
		if(inicio == null) throw new AlmacenException("No se encontro registro de inicio");
		if(cantidad == null)throw new AlmacenException("No se encontro cantidad a paginar");
		try {
			return querySvc.gets(almacen,inicio,cantidad);
		}catch (QueryException e) {
			throw new AlmacenException("Se encontro problema en la obtancion del registro.",e);
		}
	}

	@Override
	public List<UbicacionDTO> ubicaciones(UbicacionDTO ubicacion, Integer inicio, Integer cantidad)
			throws AlmacenException {
		if(ubicacion == null )throw new AlmacenException("No se encontro la ubicacion suministrado.");
		if(inicio == null) throw new AlmacenException("No se encontro registro de inicio");
		if(cantidad == null)throw new AlmacenException("No se encontro cantidad a paginar");
		try {
			return querySvc.gets(ubicacion,inicio,cantidad);
		}catch (QueryException e) {
			throw new AlmacenException("Se encontro problema en la obtancion del registro.",e);
		}
	}

	@Override
	public List<ProductoUbicacionDTO> productoUbicaciones(ProductoUbicacionDTO asignar, Integer inicio,
			Integer cantidad) throws AlmacenException {
		if(asignar == null )throw new AlmacenException("No se encontro el producto a asignar a ubicacion suministrado.");
		if(inicio == null) throw new AlmacenException("No se encontro registro de inicio");
		if(cantidad == null)throw new AlmacenException("No se encontro cantidad a paginar");
		try {
			return querySvc.gets(asignar,inicio,cantidad);
		}catch (QueryException e) {
			throw new AlmacenException("Se encontro problema en la obtancion del registro.",e);
		}
	}

	@Override
	public void del(AlmacenDTO almacen, UsuarioDTO usuario) throws AlmacenException {
		if(almacen == null )throw new AlmacenException("No se encontro el almacen suministrado.");
		if(usuario == null)throw new AlmacenException("No se encontro usuario suministrado.");
		if(StringUtils.isBlank(almacen.getCodigo()))throw new AlmacenException("El almacen  no se encuentra creado.");
		try {
			querySvc.del(almacen, usuario);
		}catch (QueryException e) {
			throw new AlmacenException("Se encontro problema en la eliminacion del registro.",e);
		}
	}

	@Override
	public void del(UbicacionDTO almacen, UsuarioDTO usuario) throws AlmacenException {
		if(almacen == null )throw new AlmacenException("No se encontro la ubicacion suministrado.");
		if(usuario == null)throw new AlmacenException("No se encontro usuario suministrado.");
		if(StringUtils.isBlank(almacen.getCodigo()))throw new AlmacenException("La aubicacion  no se encuentra creado.");
		try {
			querySvc.del(almacen, usuario);
		}catch (QueryException e) {
			throw new AlmacenException("Se encontro problema en la eliminacion del registro.",e);
		}
	}

	@Override
	public void del(ProductoUbicacionDTO almacen, UsuarioDTO usuario) throws AlmacenException {
		if(almacen == null )throw new AlmacenException("No se encontro el producto a asignar a ubicacion suministrado.");
		if(usuario == null)throw new AlmacenException("No se encontro usuario suministrado.");
		if(StringUtils.isBlank(almacen.getCodigo()))throw new AlmacenException("El producto a asignar la aubicacion  no se encuentra creado.");
		try {
			querySvc.del(almacen, usuario);
		}catch (QueryException e) {
			throw new AlmacenException("Se encontro problema en la eliminacion del registro.",e);
		}
	}

}
