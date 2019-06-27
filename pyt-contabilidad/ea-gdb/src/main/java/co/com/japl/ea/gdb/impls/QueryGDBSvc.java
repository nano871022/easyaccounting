package co.com.japl.ea.gdb.impls;

import java.lang.reflect.InvocationTargetException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.pyt.common.abstracts.ADto;
import org.pyt.common.common.I18n;
import org.pyt.common.common.UsuarioDTO;
import org.pyt.common.constants.LanguageConstant;
import org.pyt.common.exceptions.QueryException;
import org.pyt.common.exceptions.ReflectionException;
import org.pyt.common.exceptions.querys.StatementSqlException;

import com.pyt.query.interfaces.IQuerySvc;

import co.com.japl.ea.gdb.privates.constants.QueryConstants;
import co.com.japl.ea.gdb.privates.impls.ConnectionJDBC;
import co.com.japl.ea.gdb.privates.impls.StatementFactory;
import co.com.japl.ea.gdb.privates.interfaces.IStatementSql;
import co.com.japl.ea.gdb.privates.utils.StatementQuerysUtil;

public class QueryGDBSvc implements IQuerySvc {

	private ConnectionJDBC db;
	private I18n i18n;
	private StatementFactory sfactory;
	private String motor;

	public QueryGDBSvc() {
		db = ConnectionJDBC.getInstance();
		i18n = new I18n();
		sfactory = new StatementFactory();
		motor = "";
	}

	@SuppressWarnings("unchecked")
	private final <T extends ADto> T getSearchResult(ResultSet rs, T dto, List<String> names)
			throws ReflectionException, InstantiationException, IllegalAccessException, IllegalArgumentException,
			InvocationTargetException, NoSuchMethodException, SecurityException, QueryException, SQLException {
		T newInstance = (T) dto.getClass().getConstructor().newInstance();
		for (String name : names) {
			try {
				var clazz = newInstance.typeField(name).asSubclass(ADto.class);
				var subInstance = clazz.getConstructor().newInstance();
				((ADto) subInstance).set(name, rs.getObject(name));
				subInstance = get((T) subInstance);
				newInstance.set(name, subInstance);
			} catch(ClassCastException e){
				newInstance.set(name, rs.getObject(name));
			}
		}
		return newInstance;
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T extends ADto> List<T> gets(T obj, Integer init, Integer end) throws QueryException {
		List<T> list = new ArrayList<T>();
		ResultSet rs;
		try {
			var statement = (IStatementSql<T>) sfactory.getStatement(motor, obj.getClass());
			List<String> names = obj.getNameFields();
			var query = statement.select(obj, init, end);
			rs = db.executeQuery(query);
			while (rs.next()) {
				list.add(getSearchResult(rs, obj, names));
			}
		} catch (SQLException | ReflectionException | InstantiationException | IllegalAccessException
				| IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException
				| StatementSqlException e) {
			throw new QueryException(i18n.valueBundle(LanguageConstant.LANGUAGE_ERROR_QUERY_SEARCH), e);
		}
		return list;
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T extends ADto> List<T> gets(T obj) throws QueryException {
		List<T> list = new ArrayList<T>();
		ResultSet rs;
		try {
			IStatementSql<T> ssql = (IStatementSql<T>) sfactory.getStatement(motor, obj.getClass());
			List<String> names = obj.getNameFields();
			var query = ssql.select(obj);
			rs = db.executeQuery(query);
			while (rs.next()) {
				list.add(getSearchResult(rs, obj, names));
			}
		} catch (SQLException | ReflectionException | InstantiationException | IllegalAccessException
				| IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException
				| StatementSqlException e) {
			throw new QueryException(i18n.valueBundle(LanguageConstant.LANGUAGE_ERROR_QUERY_SEARCH), e);
		}
		return list;
	}

	@Override
	public <T extends ADto> T get(T obj) throws QueryException {
		List<T> list = gets(obj);
		if (list == null || list.size() == 0) {
			throw new QueryException(i18n.valueBundle(LanguageConstant.LANGUAGE_ERROR_QUERY_NOT_FOUND_ROW));
		}
		if (list.size() > 1) {
			throw new QueryException(
					String.format(i18n.valueBundle(LanguageConstant.LANGUAGE_ERROR_QUERY_FOUND_ROWS), list.size()));
		}
		return list.get(0);
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T extends ADto> T set(T obj, UsuarioDTO user) throws QueryException {

		try {
			var newDto = false;
			var statement = (IStatementSql<T>) sfactory.getStatement(motor, obj.getClass());
			if (StringUtils.isNotBlank(obj.getCodigo())) {
				obj.setActualizador(user.getNombre());
				obj.setFechaActualizacion(new Date());
			} else {
				obj.setCreador(user.getNombre());
				obj.setFechaCreacion(new Date());
				obj.setCodigo(new StatementQuerysUtil().genConsecutivo(obj.getClass(),
						countRow(obj.getClass().getConstructor().newInstance())));
				newDto = true;
			}
			var query = newDto ? statement.insert(obj) : statement.update(obj);
			Boolean rs = db.executeIUD(query);
			if (rs) {
				if (StringUtils.isNotBlank(obj.getCodigo())) {
					return get(obj);
				}
				return obj;
			} else {
				throw new QueryException(i18n.valueBundle(LanguageConstant.LANGUAGE_ERROR_QUERY_EXEC));
			}
		} catch (SQLException | InstantiationException | IllegalAccessException | IllegalArgumentException
				| InvocationTargetException | NoSuchMethodException | SecurityException | StatementSqlException e) {
			throw new QueryException(i18n.valueBundle(LanguageConstant.LANGUAGE_ERROR_QUERY_INSERT_UPDATE), e);
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T extends ADto> void del(T obj, UsuarioDTO user) throws QueryException {
		try {
			var statement = (IStatementSql<T>) sfactory.getStatement(motor, obj.getClass());
			var query = statement.delete(obj);
			if (!db.executeIUD(query)) {
				throw new QueryException(i18n.valueBundle(LanguageConstant.LANGUAGE_ERROR_QUERY_EXEC_DELETE));
			}
		} catch (SQLException | IllegalArgumentException | SecurityException | StatementSqlException e) {
			throw new QueryException(i18n.valueBundle(LanguageConstant.LANGUAGE_ERROR_QUERY_DELETE_ROW), e);
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T extends ADto> Integer countRow(T obj) throws QueryException {
		ResultSet rs;
		try {
			var statement = (IStatementSql<T>) sfactory.getStatement(motor, obj.getClass());
			var query = statement.select_count(obj);
			rs = db.executeQuery(query);
			while (rs.next()) {
				return rs.getInt(QueryConstants.CONST_FIELD_COUNT);
			}
		} catch (SQLException | IllegalArgumentException | SecurityException | StatementSqlException e) {
			throw new QueryException(i18n.valueBundle(LanguageConstant.LANGUAGE_ERROR_QUERY_COUNT_ROWS), e);
		}
		return 0;
	}
}
