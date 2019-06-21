
module ea_dto {
	exports com.pyt.service.abstracts;
	exports com.pyt.service.dto;
	exports com.pyt.service.dto.dels;
	exports com.pyt.service.dto.inventario;
	exports com.pyt.service.dto.upds;
	exports com.pyt.service.interfaces;
	exports com.pyt.service.interfaces.inventarios;
	exports com.pyt.service.pojo;
	requires ea_common;
	requires ea_anotaciones;
	requires ea_exceptions;
	requires ea_constantes;
	requires ea_loader;
	requires java.desktop;
	requires arquitectura.anotaciones;
}
