package co.com.japl.ea.gdb.privates.impls;

import org.pyt.common.abstracts.ADto;
import org.pyt.common.exceptions.ReflectionException;
import org.pyt.common.exceptions.querys.StatementSqlException;

import co.com.japl.ea.gdb.privates.constants.QueryConstants;
import co.com.japl.ea.gdb.privates.interfaces.IStatementSql;
import co.com.japl.ea.gdb.privates.utils.StatementQuerysUtil;

public class BasicStatementSql<T extends ADto> implements IStatementSql<T> {
	private StatementQuerysUtil squ;

	public BasicStatementSql() {
		squ = new StatementQuerysUtil();
	}

	@Override
	public String select(T dto, int init, int size, boolean insertValue, boolean count) throws StatementSqlException {
		String query = init > 0 && size > 0 ? QueryConstants.SQL_SELECT_LIMIT
				: count ? QueryConstants.SQL_COUNT_ROW : QueryConstants.SQL_SELECT;
		try {
			query = String.format(query, squ.fieldToSelect(dto), squ.getTableName(dto),
					squ.fieldToWhere(dto, insertValue), init + QueryConstants.CONST_COMMA + size);
			if (query.substring(query.indexOf(QueryConstants.CONST_WHERE)).trim()
					.replace(QueryConstants.CONST_WHERE, QueryConstants.CONST_EMPTY).length() == 0) {
				query += " 1";
			}
		} catch (ReflectionException e) {
			throw new StatementSqlException("Se presento problema en generacion sentencia sql select", e);
		}
		return query;
	}

	@Override
	public String update(T dto) throws StatementSqlException {
		var query = QueryConstants.SQL_UPDATE;
		try {
			query = String.format(query, squ.getTableName(dto), squ.fieldToSelect(dto), squ.fieldToWhere(dto, false));
		} catch (ReflectionException e) {
			throw new StatementSqlException("Se presento problema en generacion sentencia sql update", e);
		}
		return query;
	}

	@Override
	public String insert(T dto) throws StatementSqlException {
		var query = QueryConstants.SQL_INSERT;
		try {
			String.format(query, squ.getTableName(dto), squ.fieldToSelect(dto), squ.fieldToWhere(dto, true));
		} catch (ReflectionException e) {
			throw new StatementSqlException("Se presento problema en generacion sentencia sql update", e);
		}
		return null;
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

}
