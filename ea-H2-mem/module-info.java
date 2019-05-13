
module com.japl.ea.query {
 requires org.pyt.common;
 requires com.pyt.query.interfaces;
 requires java.sql;
 requires org.h2.api;
 requires com.h2database;
requires org.pyt.common.exceptions;
 provides com.pyt.query.interfaces.IQuerySvc with com.japl.ea.query.implement.QuerySvc;
}
