package co.com.japl.ea.gdb.privates.impls;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.pyt.common.abstracts.ADto;
import org.pyt.common.common.DtoUtils;
import org.pyt.common.common.ListUtils;
import org.pyt.common.exceptions.ReflectionException;
import org.pyt.common.exceptions.querys.StatementSqlException;

import co.com.japl.ea.gdb.privates.constants.QueryConstants;
import co.com.japl.ea.gdb.privates.interfaces.IStatementSql;
import co.com.japl.ea.gdb.privates.utils.StatementQuerysUtil;

public class BasicStatementSql<T extends ADto> implements IStatementSql<T> {
	private StatementQuerysUtil squ;
	private final String CONST_GET_ALL_ROWS = " 1 ";
	public BasicStatementSql() {
		squ = StatementQuerysUtil.instance();
	}
	
	@Override
	public String select(T dto, int init, int size, boolean insertValue, boolean count) throws StatementSqlException {
		String query = init >= 0 && size > 0 ? QueryConstants.SQL_SELECT_LIMIT
				: count ? QueryConstants.SQL_COUNT_ROW : QueryConstants.SQL_SELECT;
		try {
			if (!count) {
				query = String.format(query, squ.fieldToSelect(dto), squ.getTableName(dto),
						squ.fieldToWhere(dto, insertValue), init + QueryConstants.CONST_COMMA + size);
			} else {
				query = String.format(query, squ.getTableName(dto), squ.fieldToWhere(dto, insertValue),
						init + QueryConstants.CONST_COMMA + size);
			}
			query = validWhereEmpty(query);
		} catch (ReflectionException e) {
			throw new StatementSqlException("Se presento problema en generacion sentencia sql select", e);
		}
		return query;
	}
	
	private final String validWhereEmpty(String query) {
		
		var limitPosition = query.indexOf(QueryConstants.CONST_LIMIT);
		var wherePosition = query.indexOf(QueryConstants.CONST_WHERE);
		var where = limitPosition > 0 ? query.substring(wherePosition, limitPosition): query.substring(wherePosition);
		var result = where.trim().replace(QueryConstants.CONST_WHERE, QueryConstants.CONST_EMPTY).length() == 0;
		if(result && limitPosition < 0) {
			query += CONST_GET_ALL_ROWS;
		}else if(result && limitPosition > 0) {
			 query = query.substring(0, wherePosition) + QueryConstants.CONST_WHERE +CONST_GET_ALL_ROWS+query.substring(limitPosition);
		}
		return query;
	}

	@Override
	public String update(T dto) throws StatementSqlException {
		var query = QueryConstants.SQL_UPDATE;
		try {
			query = String.format(query, squ.getTableName(dto), squ.fieldTOSetUpdate(dto), squ.fieldWhereToUpdate(dto));
		} catch (ReflectionException e) {
			throw new StatementSqlException("Se presento problema en generacion sentencia sql update", e);
		}
		return query;
	}

	@Override
	public String insert(T dto) throws StatementSqlException {
		var query = QueryConstants.SQL_INSERT;
		try {
			query = String.format(query, squ.getTableName(dto), squ.fieldToInsert(dto), squ.fieldToWhere(dto, true));
		} catch (ReflectionException e) {
			throw new StatementSqlException("Se presento problema en generacion sentencia sql update", e);
		}
		return query;
	}

	@Override
	public String delete(T dto) throws StatementSqlException {
		var query = QueryConstants.SQL_DELETE;
		try {
			query = String.format(query, squ.getTableName(dto), squ.fieldToWhere(dto, false));
		} catch (ReflectionException e) {
			throw new StatementSqlException("Se presento problema en generacion sentencia sql update", e);
		}
		return query;
	}


	@Override
	public <O,D extends ADto>String appyWhere(T dto, String name, TypeAdditionalWhere additionalWhere,D dto2,String nameSelect, O... values) throws StatementSqlException {
		var real = squ.getName(dto,name);
		switch(additionalWhere) {
		case BETWEEN:
					if(ListUtils.haveItemsEqual(values, 2)) {
						return squ.between(dto, name, values[0], values[1]);
					}
		case LESS:
					if(ListUtils.haveOneItem(values)) {
						return squ.less(dto, name, values[0]);
					}
		case LESSTHAT:
					if(ListUtils.haveOneItem(values)) {	
						return squ.lessThat(dto, name, values[0]);
					}
		case GREATER:
					if(ListUtils.haveOneItem(values)) {
						return squ.greater(dto, name, values[0]);
					}
		case GREATERTHAT:
				if(ListUtils.haveOneItem(values)) {
					return squ.greaterThat(dto, name, values[0]);
				}
		case SUBSELECT:
			if(ListUtils.isBlank(values) && StringUtils.isNotBlank(nameSelect) && DtoUtils.isNotBlank(dto2)) {
				return real +" IN "+ String.format(QueryConstants.SQL_SELECT,squ.fieldToSelect(dto2, nameSelect),squ.fieldToWhere(dto2, false),squ.fieldToWhere(dto2, false));
			}
		case DIFFERENT:
			return squ.different(dto, name);
		}
		return "";
	}
	
	public <O,D extends ADto> String order(T dto,Map<String,String> fieldOrder) throws StatementSqlException{
		List<String> orders = new ArrayList<>(); 
		fieldOrder.forEach((name,value)->{
			var real = squ.getName(dto, name);
			orders.add((orders.size()>0?",":"")+real + value);
		});
		return orders.parallelStream().reduce("", (valor1,valor2)->valor1+valor2);
	}

}
