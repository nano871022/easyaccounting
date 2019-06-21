
module ea_service {
	requires ea_query;
	requires ea_common;
	requires ea_exceptions;
	requires ea_anotaciones;
	requires ea_dto;
	requires ea_loader;
	requires ea_constantes;
	requires ea_poi;
	requires Arquitectura_Anotaciones;
	requires arquitectura.librerias;
	requires org.apache.commons.lang3;

	exports com.pyt.service.implement;
	exports com.pyt.service.implement.inventario;
	exports com.pyt.service.proccess;

	uses com.pyt.query.interfaces.IQuerySvc;
}
