
module com.japl.ea.query {
 requires org.pyt.common;
 requires com.pyt.query.interfaces;
 requires com.japl.ea.sql;
 requires java.sql;
 requires org.h2.api;
 requires com.h2database;
requires org.pyt.common.exceptions;
 provides com.pyt.query.interfaces.IQuerySvc with co.com.japl.ea.mybatis.impl.QueryMyBatisSvc;
}
