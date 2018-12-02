package com.pyt.service.implement.inventerio;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.pyt.common.annotations.Inject;
import org.pyt.common.common.UsuarioDTO;
import org.pyt.common.exceptions.QueryException;
import org.pyt.common.exceptions.inventario.ProductosException;
import org.pyt.common.exceptions.inventario.ResumenProductoException;

import com.pyt.query.interfaces.IQuerySvc;
import com.pyt.service.dto.inventario.ProductoDto;
import com.pyt.service.dto.inventario.ResumenProductoDto;
import com.pyt.service.implement.ServiciosSvc;
import com.pyt.service.interfaces.inventarios.IProductosSvc;

public class ProductosSvc extends ServiciosSvc implements IProductosSvc {
	@Inject(resource = "com.pyt.query.implement.QuerySvc")
	private IQuerySvc querySvc;

	@Override
	public ProductoDto insert(ProductoDto producto, UsuarioDTO usuario) throws ProductosException {
		if(producto== null )throw new ProductosException("No se encontro producto suministrado.");
		if(usuario == null)throw new ProductosException("No se encontro usuario suministrado.");
		if(StringUtils.isNotBlank(producto.getCodigo()))throw new ProductosException("El producto ya se encuentra creado.");
		try {
			return querySvc.set(producto, usuario);
		}catch (QueryException e) {
			throw new ProductosException("Se encontro problema en el ingreso del registro.",e);
		}
	}

	@Override
	public ResumenProductoDto insert(ResumenProductoDto resumen, UsuarioDTO usuario) throws ResumenProductoException {
		if(resumen== null )throw new ResumenProductoException("No se encontro resumen de producto suministrado.");
		if(usuario == null)throw new ResumenProductoException("No se encontro usuario suministrado.");
		if(StringUtils.isNotBlank(resumen.getCodigo()))throw new ResumenProductoException("El resumen de producto ya se encuentra creado.");
		try {
			return querySvc.set(resumen, usuario);
		}catch (QueryException e) {
			throw new ResumenProductoException("Se encontro problema en el ingreso del registro.",e);
		}
	}

	@Override
	public void update(ProductoDto producto, UsuarioDTO usuario) throws ProductosException {
		if(producto== null )throw new ProductosException("No se encontro producto suministrado.");
		if(usuario == null)throw new ProductosException("No se encontro usuario suministrado.");
		if(StringUtils.isBlank(producto.getCodigo()))throw new ProductosException("El producto no se encuentra creado.");
		try {
			querySvc.set(producto, usuario);
		}catch (QueryException e) {
			throw new ProductosException("Se encontro problema en la actualizacion del registro.",e);
		}
	}

	@Override
	public void update(ResumenProductoDto producto, UsuarioDTO usuario) throws ResumenProductoException {
		if(producto== null )throw new ResumenProductoException("No se encontro resumen de producto suministrado.");
		if(usuario == null)throw new ResumenProductoException("No se encontro usuario suministrado.");
		if(StringUtils.isBlank(producto.getCodigo()))throw new ResumenProductoException("El resumen de producto no se encuentra creado.");
		try {
			querySvc.set(producto, usuario);
		}catch (QueryException e) {
			throw new ResumenProductoException("Se encontro problema en la actualizacion del registro.",e);
		}
	}

	@Override
	public ProductoDto producto(ProductoDto producto) throws ProductosException {
		if(producto== null )throw new ProductosException("No se encontro producto suministrado.");
		try {
			return querySvc.get(producto);
		}catch (QueryException e) {
			throw new ProductosException("Se encontro problema en la obtancion del registro.",e);
		}
	}

	@Override
	public ResumenProductoDto resumenProducto(ResumenProductoDto resumentProducto) throws ResumenProductoException {
		if(resumentProducto== null )throw new ResumenProductoException("No se encontro resumen de producto suministrado.");
		try {
			return querySvc.get(resumentProducto);
		}catch (QueryException e) {
			throw new ResumenProductoException("Se encontro problema en la obtancion del registro.",e);
		}
	}

	@Override
	public List<ProductoDto> productos(ProductoDto producto) throws ProductosException {
		if(producto== null )throw new ProductosException("No se encontro producto suministrado.");
		try {
			return querySvc.gets(producto);
		}catch (QueryException e) {
			throw new ProductosException("Se encontro problema en la obtancion del registro.",e);
		}
	}

	@Override
	public List<ResumenProductoDto> resumenProductos(ResumenProductoDto resumen) throws ResumenProductoException {
		if(resumen== null )throw new ResumenProductoException("No se encontro resumen de producto suministrado.");
		try {
			return querySvc.gets(resumen);
		}catch (QueryException e) {
			throw new ResumenProductoException("Se encontro problema en la obtancion del registro.",e);
		}
	}

	@Override
	public List<ProductoDto> productos(ProductoDto producto, Integer inicio, Integer cantidad)
			throws ProductosException {
		if(producto== null )throw new ProductosException("No se encontro producto suministrado.");
		if(inicio == null) throw new ProductosException("No se encontro registro de inicio");
		if(cantidad == null)throw new ProductosException("No se encontro cantidad a paginar");
		try {
			return querySvc.gets(producto,inicio,cantidad);
		}catch (QueryException e) {
			throw new ProductosException("Se encontro problema en la obtancion del registro.",e);
		}
	}

	@Override
	public List<ResumenProductoDto> resumenProductos(ResumenProductoDto resumen, Integer inicio, Integer cantidad)
			throws ResumenProductoException {
		if(resumen== null )throw new ResumenProductoException("No se encontro resumen de producto suministrado.");
		if(inicio == null) throw new ResumenProductoException("No se encontro registro de inicio");
		if(cantidad == null)throw new ResumenProductoException("No se encontro cantidad a paginar");
		try {
			return querySvc.gets(resumen,inicio,cantidad);
		}catch (QueryException e) {
			throw new ResumenProductoException("Se encontro problema en la obtancion del registro.",e);
		}
	}

	@Override
	public void del(ProductoDto producto, UsuarioDTO usuario) throws ProductosException {
		if(producto== null )throw new ProductosException("No se encontro producto suministrado.");
		if(usuario == null)throw new ProductosException("No se encontro usuario suministrado.");
		if(StringUtils.isBlank(producto.getCodigo()))throw new ProductosException("El producto no se encuentra creado.");
		try {
			querySvc.del(producto, usuario);
		}catch (QueryException e) {
			throw new ProductosException("Se encontro problema en la eliminacion del registro.",e);
		}
	}

	@Override
	public void del(ResumenProductoDto resumen, UsuarioDTO usuario) throws ResumenProductoException {
		if(resumen== null )throw new ResumenProductoException("No se encontro producto suministrado.");
		if(usuario == null)throw new ResumenProductoException("No se encontro usuario suministrado.");
		if(StringUtils.isBlank(resumen.getCodigo()))throw new ResumenProductoException("El producto no se encuentra creado.");
		try {
			querySvc.del(resumen, usuario);
		}catch (QueryException e) {
			throw new ResumenProductoException("Se encontro problema en la eliminacion del registro.",e);
		}
	}

}
