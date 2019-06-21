
module ea_gdb {
 requires ea_common;
 requires ea_query;
 requires ea_sql;
 requires java.sql;
 requires com.h2database;
requires ea_exceptions;
requires ea_constantes;
 provides com.pyt.query.interfaces.IQuerySvc with co.com.japl.ea.gdb.impls.QueryGDBSvc;
}
