package co.com.japl.ea.dto.dto.inventario;

import co.com.japl.ea.common.abstracts.ADto;

public class ProductoUbicacionDTO extends ADto {
	private static final long serialVersionUID = -940384022485305996L;
	private ProductoDTO producto;
	private UbicacionDTO ubicacionAlmacen;
	private Integer cantidad;
	public ProductoDTO getProducto() {
		return producto;
	}
	public void setProducto(ProductoDTO producto) {
		this.producto = producto;
	}
	public UbicacionDTO getUbicacionAlmacen() {
		return ubicacionAlmacen;
	}
	public void setUbicacionAlmacen(UbicacionDTO ubicacionAlmacen) {
		this.ubicacionAlmacen = ubicacionAlmacen;
	}
	public Integer getCantidad() {
		return cantidad;
	}
	public void setCantidad(Integer cantidad) {
		this.cantidad = cantidad;
	}
}
