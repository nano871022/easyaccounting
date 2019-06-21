
module ea_hibernate {
 requires ea_common;
 requires ea_query;
 requires ea_sql;
 requires java.sql;
 requires com.h2database;
requires ea_exceptions;
requires commons.lang3;
requires ea_constantes;
requires ea_dto;
requires org.hibernate.orm.core;
 provides com.pyt.query.interfaces.IQuerySvc with com.japl.ea.query.implement.QueryJPASvc;
}
