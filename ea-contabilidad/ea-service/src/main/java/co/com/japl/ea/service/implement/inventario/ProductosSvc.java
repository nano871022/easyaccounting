package co.com.japl.ea.service.implement.inventario;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.pyt.common.annotations.Inject;

import co.com.japl.ea.query.interfaces.IQuerySvc;
import co.com.arquitectura.annotation.proccessor.Services.Type;
import co.com.arquitectura.annotation.proccessor.Services.kind;
import co.com.arquitectura.annotation.proccessor.Services.scope;
import co.com.japl.ea.dto.abstracts.Services;
import co.com.japl.ea.dto.dto.inventario.ProductoDTO;
import co.com.japl.ea.dto.dto.inventario.ResumenProductoDTO;
import co.com.japl.ea.dto.interfaces.inventarios.IProductosSvc;
import co.com.japl.ea.dto.system.UsuarioDTO;
import co.com.japl.ea.exceptions.QueryException;
import co.com.japl.ea.exceptions.inventario.ProductosException;
import co.com.japl.ea.exceptions.inventario.ResumenProductoException;

public class ProductosSvc extends Services implements IProductosSvc {
	@Inject(resource = "co.com.japl.ea.query.implement.QuerySvc")
	private IQuerySvc querySvc;
	
	@co.com.arquitectura.annotation.proccessor.Services(alcance = scope.EJB, alias = "Ingreso Productos", descripcion = "Servicio generico para ingreso de productos.", tipo = kind.PUBLIC, type = Type.CREATE)
	@Override
	public ProductoDTO insert(ProductoDTO producto, UsuarioDTO usuario) throws ProductosException {
		if (producto == null)
			throw new ProductosException("No se encontro producto suministrado.");
		if (usuario == null)
			throw new ProductosException("No se encontro usuario suministrado.");
		if (StringUtils.isNotBlank(producto.getCodigo()))
			throw new ProductosException("El producto ya se encuentra creado.");
		try {
			List<ProductoDTO> list =  productos(producto);
			if(list != null && list.size() > 0) {
				throw new ProductosException("Se encontro otro producto con los mismos valores suministrados.");
			}
			return querySvc.set(producto, usuario);
		} catch (QueryException e) {
			throw new ProductosException("Se encontro problema en el ingreso del registro.", e);
		}
	}

	@Override
	public ResumenProductoDTO insert(ResumenProductoDTO resumen, UsuarioDTO usuario) throws ResumenProductoException {
		if (resumen == null)
			throw new ResumenProductoException("No se encontro resumen de producto suministrado.");
		if (usuario == null)
			throw new ResumenProductoException("No se encontro usuario suministrado.");
		if (StringUtils.isNotBlank(resumen.getCodigo()))
			throw new ResumenProductoException("El resumen de producto ya se encuentra creado.");
		try {
			return querySvc.set(resumen, usuario);
		} catch (QueryException e) {
			throw new ResumenProductoException("Se encontro problema en el ingreso del registro.", e);
		}
	}

	@Override
	public void update(ProductoDTO producto, UsuarioDTO usuario) throws ProductosException {
		if (producto == null)
			throw new ProductosException("No se encontro producto suministrado.");
		if (usuario == null)
			throw new ProductosException("No se encontro usuario suministrado.");
		if (StringUtils.isBlank(producto.getCodigo()))
			throw new ProductosException("El producto no se encuentra creado.");
		try {
			querySvc.set(producto, usuario);
		} catch (QueryException e) {
			throw new ProductosException("Se encontro problema en la actualizacion del registro.", e);
		}
	}

	@Override
	public void update(ResumenProductoDTO producto, UsuarioDTO usuario) throws ResumenProductoException {
		if (producto == null)
			throw new ResumenProductoException("No se encontro resumen de producto suministrado.");
		if (usuario == null)
			throw new ResumenProductoException("No se encontro usuario suministrado.");
		if (StringUtils.isBlank(producto.getCodigo()))
			throw new ResumenProductoException("El resumen de producto no se encuentra creado.");
		try {
			querySvc.set(producto, usuario);
		} catch (QueryException e) {
			throw new ResumenProductoException("Se encontro problema en la actualizacion del registro.", e);
		}
	}

	@Override
	public ProductoDTO producto(ProductoDTO producto) throws ProductosException {
		if (producto == null)
			throw new ProductosException("No se encontro producto suministrado.");
		try {
			return querySvc.get(producto);
		} catch (QueryException e) {
			throw new ProductosException("Se encontro problema en la obtancion del registro.", e);
		}
	}

	@Override
	public ResumenProductoDTO resumenProducto(ResumenProductoDTO resumentProducto) throws ResumenProductoException {
		if (resumentProducto == null)
			throw new ResumenProductoException("No se encontro resumen de producto suministrado.");
		try {
			return querySvc.get(resumentProducto);
		} catch (QueryException e) {
			throw new ResumenProductoException("Se encontro problema en la obtancion del registro.", e);
		}
	}

	@Override
	public List<ProductoDTO> productos(ProductoDTO producto) throws ProductosException {
		if (producto == null)
			throw new ProductosException("No se encontro producto suministrado.");
		try {
			return querySvc.gets(producto);
		} catch (QueryException e) {
			throw new ProductosException("Se encontro problema en la obtancion del registro.", e);
		}
	}

	@Override
	public List<ResumenProductoDTO> resumenProductos(ResumenProductoDTO resumen) throws ResumenProductoException {
		if (resumen == null)
			throw new ResumenProductoException("No se encontro resumen de producto suministrado.");
		try {
			return querySvc.gets(resumen);
		} catch (QueryException e) {
			throw new ResumenProductoException("Se encontro problema en la obtancion del registro.", e);
		}
	}

	@Override
	public List<ProductoDTO> productos(ProductoDTO producto, Integer inicio, Integer cantidad)
			throws ProductosException {
		if (producto == null)
			throw new ProductosException("No se encontro producto suministrado.");
		if (inicio == null)
			throw new ProductosException("No se encontro registro de inicio");
		if (cantidad == null)
			throw new ProductosException("No se encontro cantidad a paginar");
		try {
			return querySvc.gets(producto, inicio, cantidad);
		} catch (QueryException e) {
			throw new ProductosException("Se encontro problema en la obtancion del registro.", e);
		}
	}

	@Override
	public List<ResumenProductoDTO> resumenProductos(ResumenProductoDTO resumen, Integer inicio, Integer cantidad)
			throws ResumenProductoException {
		if (resumen == null)
			throw new ResumenProductoException("No se encontro resumen de producto suministrado.");
		if (inicio == null)
			throw new ResumenProductoException("No se encontro registro de inicio");
		if (cantidad == null)
			throw new ResumenProductoException("No se encontro cantidad a paginar");
		try {
			return querySvc.gets(resumen, inicio, cantidad);
		} catch (QueryException e) {
			throw new ResumenProductoException("Se encontro problema en la obtancion del registro.", e);
		}
	}

	@Override
	public void del(ProductoDTO producto, UsuarioDTO usuario) throws ProductosException {
		if (producto == null)
			throw new ProductosException("No se encontro producto suministrado.");
		if (usuario == null)
			throw new ProductosException("No se encontro usuario suministrado.");
		if (StringUtils.isBlank(producto.getCodigo()))
			throw new ProductosException("El producto no se encuentra creado.");
		try {
			querySvc.del(producto, usuario);
		} catch (QueryException e) {
			throw new ProductosException("Se encontro problema en la eliminacion del registro.", e);
		}
	}

	@Override
	public void del(ResumenProductoDTO resumen, UsuarioDTO usuario) throws ResumenProductoException {
		if (resumen == null)
			throw new ResumenProductoException("No se encontro producto suministrado.");
		if (usuario == null)
			throw new ResumenProductoException("No se encontro usuario suministrado.");
		if (StringUtils.isBlank(resumen.getCodigo()))
			throw new ResumenProductoException("El producto no se encuentra creado.");
		try {
			querySvc.del(resumen, usuario);
		} catch (QueryException e) {
			throw new ResumenProductoException("Se encontro problema en la eliminacion del registro.", e);
		}
	}

	@Override
	public ResumenProductoDTO resumenProducto(ProductoDTO producto) throws ResumenProductoException {
		if (producto == null)
			throw new ResumenProductoException("El producto suministrado se encuentra vacio.");
		ResumenProductoDTO dto = new ResumenProductoDTO();
		dto.setProducto(producto);
		try {
			return querySvc.get(dto);
		} catch (QueryException e) {
			throw new ResumenProductoException("Error al obtener los registros ", e);
		}
	}

	@Override
	public Integer getTotalRows(ProductoDTO dto) throws ProductosException {
		if (dto == null)
			throw new ProductosException("No se suministro el filtro indicado.");
		try {
			return querySvc.countRow(dto);
		} catch (QueryException e) {
			throw new ProductosException("Error al obtener la cantidad de registros.", e);
		}
	}

}
