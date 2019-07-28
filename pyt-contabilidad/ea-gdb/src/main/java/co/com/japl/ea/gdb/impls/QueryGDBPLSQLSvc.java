package co.com.japl.ea.gdb.impls;

import java.lang.reflect.InvocationTargetException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

import org.pyt.common.abstracts.ADto;
import org.pyt.common.common.I18n;
import org.pyt.common.constants.LanguageConstant;
import org.pyt.common.exceptions.QueryException;
import org.pyt.common.exceptions.ReflectionException;

import com.pyt.query.interfaces.IAdvanceQuerySvc;

import co.com.japl.ea.gdb.privates.constants.QueryConstants;
import co.com.japl.ea.gdb.privates.impls.ConnectionJDBC;
import co.com.japl.ea.gdb.privates.utils.StatementQuerysUtil;

public class QueryGDBPLSQLSvc implements IAdvanceQuerySvc {
	private StatementQuerysUtil squ;
	private I18n i18n;
	private ConnectionJDBC db;

	public QueryGDBPLSQLSvc() {
		squ = StatementQuerysUtil.instance();
		i18n = new I18n();
		db = ConnectionJDBC.getInstance();
	}

	@Override
	public <T extends ADto> void createTrigger(Class<T> obj, triggerOption to, triggerAction... ta)
			throws QueryException {
		try {
			if (ta.length == 0) {
				throw new QueryException(
						i18n.valueBundle(LanguageConstant.LANGUAGE_ERROR_QUERY_CREATE_TRIGGER_WITHOUT_ACTION));
			}
			if (to == null) {
				throw new QueryException(
						i18n.valueBundle(LanguageConstant.LANGUAGE_ERROR_QUERY_CREATE_TRIGGER_WITHOUT_OPTION));
			}
			if (obj == null) {
				throw new QueryException(
						i18n.valueBundle(LanguageConstant.LANGUAGE_ERROR_QUERY_CREATE_TRIGGER_WITHOUT_TABLE));
			}
			var actions = Arrays.toString(ta).replace(QueryConstants.CONST_KEY_OPEN, QueryConstants.CONST_EMPTY)
					.replace(QueryConstants.CONST_KEY_CLOSE, QueryConstants.CONST_EMPTY);
			var table = squ.getTableName(obj.getConstructor().newInstance());
			var query = QueryConstants.SQL_CREATE_TRIGGER;
			query = String.format(query, table, to.toString(), actions, table, squ.getNameTriggerPOJO(obj, to, ta));
			db.getStatement().executeUpdate(query);
		} catch (SQLException | InstantiationException | IllegalAccessException | IllegalArgumentException
				| InvocationTargetException | NoSuchMethodException | SecurityException e) {
			throw new QueryException(i18n.valueBundle(LanguageConstant.LANGUAGE_ERROR_QUERY_CREATE_TRIGGER), e);
		}
	}

	@Override
	public <T extends ADto> void createTableStandard(T obj) throws QueryException {
		try {
			String query = QueryConstants.SQL_CREATE_TABLE;
			List<String> listFields = obj.getNameFields();
			String fields = QueryConstants.CONST_EMPTY;
			for (String field : listFields) {
				var type = obj.getType(field);
				if (type != null) {
					if (fields.length() > 0)
						fields += QueryConstants.CONST_COMMA;
					fields += field + QueryConstants.CONST_SPACE + squ.typeDataDB(type.getSimpleName())
							+ QueryConstants.CONST_SPACE + QueryConstants.CONST_NULL;
				}
			}
			query = String.format(query, squ.getTableName(obj), fields);
			db.getStatement().executeUpdate(query);
		} catch (ReflectionException | SQLException e) {
			throw new QueryException(i18n.valueBundle(LanguageConstant.LANGUAGE_ERROR_QUERY_CREATE_TABLE), e);
		}
	}

	@Override
	public <T extends ADto> void dropTable(T obj) throws QueryException {
		try {
			var query = QueryConstants.SQL_DROP_TABLE + squ.getTableName(obj);
			db.getStatement().executeUpdate(query);
		} catch (SQLException e) {
			throw new QueryException(i18n.valueBundle(LanguageConstant.LANGUAGE_ERROR_QUERY_DROP_TABLE), e);
		}
	}

	public void backup() throws Exception {
		db.backup();
	}

	@Override
	public void runScript(String fileName) throws Exception {
		db.runScript(fileName);
	}

	public ResultSet queryLaunch(String query) throws QueryException {
		try {
			return db.getStatement().executeQuery(query);
		} catch (SQLException e) {
			throw new QueryException(i18n.valueBundle(LanguageConstant.LANGUAGE_ERROR_QUERY_LAUNCHER), e);
		}
	}
}
