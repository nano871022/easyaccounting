package com.japl.ea.query.privates.Constants;

public class QueryConstants {
	public final static String CONST_KEY_OPEN = "[";
	public final static String CONST_KEY_CLOSE = "]";
	public final static String CONST_EMPTY = "";
	public final static String CONST_SPACE = " ";
	public final static String CONST_COMMA = ",";
	public final static String CONST_EQUAL = "=";
	public final static String CONST_QUOTE = "'";
	public final static String CONST_NULL = "NULL";
	public final static String CONST_AND = "AND";
	public final static String CONST_INSERT = "INSERT";
	public final static String CONST_WHERE = "WHERE";
	public final static String CONST_FIELD_COUNT = "size";
	public final static String CONST_FORMAT_DATE = "yyyy-MM-dd";
	public final static String CONST_TYPE_INT = "int";
	public final static String CONST_TYPE_STRING = "string";
	public final static String CONST_TYPE_CHAR = "char";
	public final static String CONST_TYPE_LONG = "long";
	public final static String CONST_TYPE_BOOL = "boolean";
	public final static String CONST_TYPE_BIG = "big";
	public final static String CONST_TYPE_NUMBER = "NUMBER";
	public final static String CONST_TYPE_VARCHAR_2 = "VARCHAR2(%d)";
	public final static String CONST_TYPE_DATE = "date";
	public final static String CONST_TYPE_DATE_TIME = "DATETIME";
	public final static String SQL_DROP_TABLE = "DROP TABLE ";
	public final static String SQL_CREATE_TABLE = "create table %s (%s)";
	public final static String SQL_INSERT = "INSERT INTO %s (%s) VALUES (%s)";
	public final static String SQL_UPDATE = "UPDATE %s SET %s WHERE %s";
	public final static String SQL_DELETE = "DELETE FROM %s WHERE %s";
	public final static String SQL_COUNT_ROW = "SELECT count(1) as size FROM %s WHERE %s";
	public final static String SQL_SELECT = "SELECT %s FROM %s WHERE %s";
	public final static String SQL_SELECT_LIMIT = "SELECT %s FROM %s WHERE %s LIMIT %s";
	public final static String SQL_JPA_SELECT = "SELECT t FROM %s t WHERE %s";
	public final static String SQL_CREATE_TRIGGER = "CREATE TRIGGER tgr_%s %s %s ON %s FOR EACH ROW CALL \"com.japl.ea.query.privates.triggers.%s\"";
}
