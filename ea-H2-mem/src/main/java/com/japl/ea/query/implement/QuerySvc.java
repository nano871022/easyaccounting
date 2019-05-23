package com.japl.ea.query.implement;

import java.io.FileNotFoundException;
import java.lang.reflect.InvocationTargetException;
import java.nio.charset.Charset;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.h2.tools.RunScript;
import org.h2.tools.Script;
import org.pyt.common.abstracts.ADto;
import org.pyt.common.common.I18n;
import org.pyt.common.common.UsuarioDTO;
import org.pyt.common.constants.LanguageConstant;
import org.pyt.common.exceptions.QueryException;
import org.pyt.common.exceptions.ReflectionException;

import com.japl.ea.query.privates.H2Connect;
import com.japl.ea.query.privates.Constants.QueryConstants;
import com.japl.ea.query.privates.jpa.ParametroJPA;
import com.japl.ea.query.privates.utils.StatementQuerysUtil;
import com.pyt.query.interfaces.IAdvanceQuerySvc;
import com.pyt.query.interfaces.IQuerySvc;
import com.pyt.service.dto.ParametroDTO;

public class QuerySvc implements IQuerySvc, IAdvanceQuerySvc {

	private H2Connect db;
	private StatementQuerysUtil squ;
	private I18n i18n;

	public QuerySvc() {
		db = H2Connect.getInstance();
		squ = new StatementQuerysUtil();
		i18n = new I18n();
	}

	/**
	 * Se encarga de realizar de retornar un objeto completamente cargado en dto
	 * 
	 * @param rs    {@link ResultSet}
	 * @param dto   extends {@link ADto}
	 * @param names {@link List} < {@link String}
	 * @return extends {@link ADto}
	 * @throws ReflectionException
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 * @throws IllegalArgumentException
	 * @throws InvocationTargetException
	 * @throws NoSuchMethodException
	 * @throws SecurityException
	 * @throws QueryException
	 * @throws SQLException
	 */
	@SuppressWarnings("unchecked")
	private final <T extends ADto> T getSearchResult(ResultSet rs, T dto, List<String> names)
			throws ReflectionException, InstantiationException, IllegalAccessException, IllegalArgumentException,
			InvocationTargetException, NoSuchMethodException, SecurityException, QueryException, SQLException {
		T newInstance = (T) dto.getClass().getConstructor().newInstance();
		for (String name : names) {
			if (newInstance.typeField(name).asSubclass(ADto.class) != null) {
				var subInstance = newInstance.typeField(name).getConstructor().newInstance();
				((ADto) subInstance).set(name, rs.getObject(name));
				subInstance = get((T) subInstance);
				newInstance.set(name, subInstance);
			} else {
				newInstance.set(name, rs.getObject(name));
			}
		}
		return newInstance;
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T extends ADto> List<T> gets(T obj, Integer init, Integer end) throws QueryException {
		String query = QueryConstants.SQL_SELECT_LIMIT;
		List<T> list = new ArrayList<T>();
		ResultSet rs;
		try {
			List<String> names = obj.getNameFields();
			query = String.format(query, squ.fieldToSelect(obj), squ.getTableName(obj), squ.fieldToWhere(obj, false),
					init + QueryConstants.CONST_COMMA + end);
			if (db.getHibernateConnect() != null) {
				db.getHibernateConnect().beginTransaction();
				var create = db.getHibernateConnect().createCriteria(ParametroJPA.class);
//				var create = db.getHibernateConnect().createQuery(query.substring(0, query.indexOf("LIMIT")));
//				create.setFirstResult(init);
//				create.setMaxResults(init+end);
//				list = (List<T>) create.getResultList();
				var lists = create.list();
			} else {
				rs = db.executeQuery(query);
				while (rs.next()) {
					list.add(getSearchResult(rs, obj, names));
				}
			}
		} catch (SQLException | ReflectionException | InstantiationException | IllegalAccessException
				| IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException e) {
			throw new QueryException(i18n.valueBundle(LanguageConstant.LANGUAGE_ERROR_QUERY_SEARCH), e);
		}
		return list;
	}

	@Override
	public <T extends ADto> List<T> gets(T obj) throws QueryException {
		List<T> list = new ArrayList<T>();
		String query = QueryConstants.SQL_SELECT;
		ResultSet rs;
		try {
			List<String> names = obj.getNameFields();
			query = String.format(query, squ.fieldToSelect(obj), squ.getTableName(obj), squ.fieldToWhere(obj, false));
			rs = db.executeQuery(query);
			while (rs.next()) {
				list.add(getSearchResult(rs, obj, names));
			}
		} catch (SQLException | ReflectionException | InstantiationException | IllegalAccessException
				| IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException e) {
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

	public <T extends ADto> void dropTable(T obj) throws QueryException {
		try {
			var query = QueryConstants.SQL_DROP_TABLE + squ.getTableName(obj);
			db.getStatement().executeUpdate(query);
		} catch (SQLException e) {
			throw new QueryException(i18n.valueBundle(LanguageConstant.LANGUAGE_ERROR_QUERY_DROP_TABLE), e);
		}
	}

	@Override
	public <T extends ADto> T set(T obj, UsuarioDTO user) throws QueryException {
		var query = QueryConstants.SQL_INSERT;
		try {
			if (StringUtils.isNotBlank(obj.getCodigo())) {
				query = QueryConstants.SQL_UPDATE;
				obj.setActualizador(user.getNombre());
				obj.setFechaActualizacion(new Date());
			} else {
				obj.setCreador(user.getNombre());
				obj.setFechaCreacion(new Date());
				obj.setCodigo(
						squ.genConsecutivo(obj.getClass(), countRow(obj.getClass().getConstructor().newInstance())));
			}
			query = String.format(query, squ.getTableName(obj), squ.fieldToSelect(obj),
					squ.fieldToWhere(obj, query.contains(QueryConstants.CONST_INSERT)));
			Boolean rs = db.executeIUD(query);
			if (rs) {
				if (StringUtils.isNotBlank(obj.getCodigo())) {
					return get(obj);
				}
				return obj;
			} else {
				throw new QueryException(i18n.valueBundle(LanguageConstant.LANGUAGE_ERROR_QUERY_EXEC));
			}
		} catch (SQLException | ReflectionException | InstantiationException | IllegalAccessException
				| IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException e) {
			throw new QueryException(i18n.valueBundle(LanguageConstant.LANGUAGE_ERROR_QUERY_INSERT_UPDATE), e);
		}
	}

	@Override
	public <T extends ADto> void del(T obj, UsuarioDTO user) throws QueryException {
		String query = QueryConstants.SQL_DELETE;
		try {
			query = String.format(query, squ.getTableName(obj), squ.fieldToWhere(obj, false));
			if (!db.executeIUD(query)) {
				throw new QueryException(i18n.valueBundle(LanguageConstant.LANGUAGE_ERROR_QUERY_EXEC_DELETE));
			}
		} catch (SQLException | ReflectionException | IllegalArgumentException | SecurityException e) {
			throw new QueryException(i18n.valueBundle(LanguageConstant.LANGUAGE_ERROR_QUERY_DELETE_ROW), e);
		}
	}

	@Override
	public <T extends ADto> Integer countRow(T obj) throws QueryException {
		String query = QueryConstants.SQL_COUNT_ROW;
		ResultSet rs;
		try {
			query = String.format(query, squ.getTableName(obj), squ.fieldToWhere(obj, false));
			if (query.substring(query.indexOf(QueryConstants.CONST_WHERE)).trim()
					.replace(QueryConstants.CONST_WHERE, QueryConstants.CONST_EMPTY).length() == 0) {
				query += " 1";
			}
			rs = db.executeQuery(query);
			while (rs.next()) {
				return rs.getInt(QueryConstants.CONST_FIELD_COUNT);
			}
		} catch (SQLException | ReflectionException | IllegalArgumentException | SecurityException e) {
			throw new QueryException(i18n.valueBundle(LanguageConstant.LANGUAGE_ERROR_QUERY_COUNT_ROWS), e);
		}
		return 0;
	}

	public ResultSet queryLaunch(String query) throws QueryException {
		try {
			return db.getStatement().executeQuery(query);
		} catch (SQLException e) {
			throw new QueryException(i18n.valueBundle(LanguageConstant.LANGUAGE_ERROR_QUERY_LAUNCHER), e);
		}
	}

	public final static void main(String... strings) {
		var usuario = new UsuarioDTO();
		usuario.setNombre("MASTER");
		var query = new QuerySvc();
		var dto = new ParametroDTO();
		dto.setNombre("test1");
		try {
			// query.createTableStandard(dto);
			// ResultSet rs = query.queryLaunch("select * from mem_parametro");
			// query.del(dto, usuario);
			// query.set(dto, usuario);
			// query.createTableStandard(new ParametroDelDTO());
			// query.createTrigger(ParametroDTO.class, triggerOption.BEFORE,
			// triggerAction.DELETE);
			System.out.println("Size::"+query.gets(dto, 0, 2).size());
			query.gets(dto, 1, 2).forEach(obj -> {
				try {
					query.del(obj, usuario);
				} catch (QueryException e) {
					e.printStackTrace();
				}
			});
			System.out.println(query.countRow(dto));
		} catch (QueryException e) {
			e.printStackTrace();
		} finally {
			System.exit(0);
		}
	}

	@SuppressWarnings("static-access")
	public final void backup() throws FileNotFoundException, SQLException {
		var sc = new Script();
		var password = "";
		var user = "";
		var url = "jdbc:h2:~/db";
		var fileName = ".sql";
		sc.process(url, user, password, fileName, null, null);
	}

	public final void runScript() throws SQLException {
		var fileName = "";
		var charset = Charset.defaultCharset();
		var continueOnError = true;
		var password = "";
		var user = "";
		var url = "jdbc:h2:~/db";
		RunScript.execute(url, user, password, fileName, charset, continueOnError);
	}

}
