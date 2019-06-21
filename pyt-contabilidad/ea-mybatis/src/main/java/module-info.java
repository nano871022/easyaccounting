
module ea_mybatis {
 requires ea_common;
 requires ea_query;
 requires ea_sql;
 requires java.sql;
 requires com.h2database;
requires ea_exceptions;
requires ea_dto;
requires commons.lang3;
requires org.mybatis;
 provides com.pyt.query.interfaces.IQuerySvc with co.com.japl.ea.mybatis.impl.QueryMyBatisSvc;
}
