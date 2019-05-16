package com.japl.ea.query.privates.Constants;

public class QueryConstants {
	public final static String CONST_KEY_OPEN = "[";
	public final static String CONST_KEY_CLOSE = "]";
	public final static String CONST_EMPTY = "";
	public final static String CONST_SPACE = " ";
	public final static String CONST_COMMA = ",";
	public final static String CONST_NULL = "NULL";
	public final static String CONST_INSERT = "INSERT";
	public final static String CONST_WHERE = "WHERE";
	public final static String CONST_FIELD_COUNT = "size";
	public final static String SQL_DROP_TABLE = "DROP TABLE ";
	public final static String SQL_CREATE_TABLE = "create table %s (%s)";
	public final static String SQL_INSERT = "INSERT INTO %s (%s) VALUES (%s)";
	public final static String SQL_UPDATE = "UPDATE %s SET %s WHERE %s";
	public final static String SQL_DELETE = "DELETE FROM %s WHERE %s";
	public final static String SQL_COUNT_ROW = "SELECT count(1) as size FROM %s WHERE %s";
	public final static String SQL_SELECT = "SELECT %s FROM %s WHERE %s";
	public final static String SQL_SELECT_LIMIT = "SELECT %s FROM %s WHERE %s LIMIT %s";
	public final static String SQL_CREATE_TRIGGER = "CREATE TRIGGER tgr_%s %s %s ON %s FOR EACH ROW CALL \"com.japl.ea.query.privates.triggers.%s\"";
}
