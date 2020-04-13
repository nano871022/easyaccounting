package co.com.japl.ea.gdb.privates.interfaces;

import java.util.Map;

import org.pyt.common.abstracts.ADto;
import org.pyt.common.exceptions.querys.StatementSqlException;
import org.pyt.common.reflection.ReflectionDto;

/**
 * Se encarga de estandarizar las diferentes sentencias posibles de usar
 * 
 * @author Alejandro Parra
 * @since 2019-06-18
 * @param <T>
 */
public interface IStatementSql<T extends ReflectionDto> {
	public static enum TypeAdditionalWhere { LESS,GREATER,LESSTHAT,GREATERTHAT,BETWEEN,SUBSELECT,DIFFERENT}
	
	default String select(T dto) throws StatementSqlException {
		return select(dto, 0, 0, false,false);
	}

	default String select(T dto, Integer init, Integer size) throws StatementSqlException {
		return select(dto, init, size, false,false);
	}
	
	default String select_count(T dto) throws StatementSqlException{
		return select(dto, 0, 0, false,true);
	}

	public String select(T dto, int init, int size, boolean insertValue, boolean count) throws StatementSqlException;

	public String update(T dto) throws StatementSqlException;

	public String insert(T dto) throws StatementSqlException;

	public String delete(T dto) throws StatementSqlException;
	
	public <O,D extends ADto>String appyWhere(T dto, String name, TypeAdditionalWhere additionalWhere,D dto2,String nameSelect, O... values) throws StatementSqlException;
	
	public <O,D extends ADto> String order(T dto,Map<String,String> fieldOrder) throws StatementSqlException;
}
