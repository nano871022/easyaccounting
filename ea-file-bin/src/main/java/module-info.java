
module ea_file_bin {
 exports com.pyt.query.implement;
 requires ea_query;
requires ea_common;
requires ea_exceptions;
requires commons.lang3;
requires arquitectura.anotaciones;
 provides com.pyt.query.interfaces.IQuerySvc with com.pyt.query.implement.QuerySvc;
}
