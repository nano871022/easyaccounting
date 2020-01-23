package co.com.japl.ea.gdb.impls;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.pyt.common.abstracts.ADto;
import org.pyt.common.common.I18n;
import org.pyt.common.common.Log;
import org.pyt.common.common.RefreshCodeValidation;
import org.pyt.common.constants.LanguageConstant;
import org.pyt.common.constants.RefreshCodeConstant;
import org.pyt.common.exceptions.QueryException;
import org.pyt.common.exceptions.ReflectionException;
import org.pyt.common.exceptions.querys.StatementSqlException;
import org.pyt.common.exceptions.validates.ValidateValueException;
import org.pyt.common.validates.ValidateValues;

import com.pyt.query.interfaces.IQuerySvc;

import co.com.japl.ea.dto.system.UsuarioDTO;
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
	private Map<String, ADto> mapJoin;
	private Log logger = Log.Log(this.getClass());
	private static final String CONST_FIELD_NAME_DTO = "codigo";
	private ValidateValues validateValues;
	private StatementQuerysUtil statementQueryUtils;

	public QueryGDBSvc() {
		validateValues = new ValidateValues();
		db = ConnectionJDBC.getInstance();
		i18n = I18n.instance();
		sfactory = new StatementFactory();
		motor = "";
		mapJoin = new HashMap<String, ADto>();
		statementQueryUtils = StatementQuerysUtil.instance();
	}

	@SuppressWarnings("unchecked")
	private final <T extends ADto> T getSearchResult(ResultSet rs, T dto, List<String> names)
			throws ReflectionException, InstantiationException, IllegalAccessException, IllegalArgumentException,
			InvocationTargetException, NoSuchMethodException, SecurityException, QueryException, SQLException,
			ClassNotFoundException {
		T newInstance = (T) dto.getClass().getConstructor().newInstance();
		for (String name : names) {
			try {
				var clazz = newInstance.typeField(name).asSubclass(ADto.class);
				var subInstance = clazz.getConstructor().newInstance();
				((ADto) subInstance).set(CONST_FIELD_NAME_DTO, rs.getObject(name));
				newInstance.set(name, subInstance);
			} catch (ClassCastException e) {
				try {
					var value = rs.getObject(name);
					if (value != null) {
						var clazz = newInstance.typeField(name);
						if (clazz == Class.class) {
							var clasz = Class.forName((String) value);
							newInstance.set(name, clasz);
						} else if ((clazz == Boolean.class || clazz == boolean.class)
								&& (value instanceof Integer || value instanceof BigDecimal)) {
							if (value instanceof Integer) {
								newInstance.set(name, (Integer) value == 1 ? true : false);
							} else {
								newInstance.set(name,
										((BigDecimal) value).compareTo(new BigDecimal(1)) == 0 ? true : false);
							}
						} else {
							newInstance.set(name, validateValues.cast(value, clazz));
						}
					}
				} catch (NullPointerException | ValidateValueException e1) {
					logger.logger("No se encontro el campo " + name + " dentro de " + dto.getClass().getName(), e1);
				}
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
			findJoins(list);
		} catch (SQLException | ReflectionException | InstantiationException | IllegalAccessException
				| IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException
				| StatementSqlException | ClassNotFoundException e) {
			throw new QueryException(i18n.valueBundle(LanguageConstant.LANGUAGE_ERROR_QUERY_SEARCH), e);
		}
		return list;
	}

	/**
	 * Realiza busqueda sobre el dto suministrado buscando campos tipo ADto para
	 * realizar busquedas de ese objeto siempre y cuando tenga el codigo del objeto
	 * para que este sea buscando en la bd
	 * 
	 * @param dto {@link ADto}
	 * @return {@link ADto}
	 */
	private <T extends ADto, S extends ADto> T searchFieldTypeADTO(T dto) {
		if (dto != null) {
			var fields = dto.getClass().getDeclaredFields();
			for (Field field : fields) {
				try {
					var clazz = field.getType().asSubclass(ADto.class);
					S instance = dto.get(field.getName());
					if (StringUtils.isNotBlank(((ADto) instance).getCodigo())) {
						if (mapJoin.containsKey(instance.getCodigo())) {
							var cached = mapJoin.get(instance.getCodigo());
							dto.set(field.getName(), cached);
						} else {
							instance = get(instance);
							dto.set(field.getName(), instance);
							mapJoin.put(instance.getCodigo(), instance);
						}
					}

				} catch (Exception e) {

				}
			}
		}
		return dto;
	}

	private <T extends ADto> void findJoins(List<T> list) {
		if (RefreshCodeValidation.getInstance().validate(RefreshCodeConstant.CONST_CLEAN_CACHE_QUERY_JOIN)) {
			mapJoin.clear();
		}
		list.forEach(dto -> searchFieldTypeADTO(dto));
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
			findJoins(list);
		} catch (SQLException | ReflectionException | InstantiationException | IllegalAccessException
				| IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException
				| StatementSqlException | ClassNotFoundException e) {
			throw new QueryException(i18n.valueBundle(LanguageConstant.LANGUAGE_ERROR_QUERY_SEARCH), e);
		}
		return list;
	}

	@Override
	public <T extends ADto> T get(T obj) throws QueryException {
		List<T> list = gets(obj, 0, 1);
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
		var newDto = false;
		try {
			newDto = StringUtils.isBlank(obj.getCodigo());

			Boolean rs = newDto ? insert(obj, user) : update(obj, user);
			if (rs) {
				if (StringUtils.isNotBlank(obj.getCodigo())) {
					return get(obj);
				}
				if (StringUtils.isNotBlank(obj.getActualizador())) {
					updateJoinCache(obj);
				}
				return obj;
			} else {
				throw new QueryException(i18n.valueBundle(LanguageConstant.LANGUAGE_ERROR_QUERY_EXEC));
			}
		} catch (IllegalArgumentException | SecurityException e) {
			if (newDto) {
				obj.setCodigo(null);
			}
			throw new QueryException(i18n.valueBundle(LanguageConstant.LANGUAGE_ERROR_QUERY_INSERT_UPDATE), e);
		}
	}

	@SuppressWarnings("unchecked")
	public final <T extends ADto> Boolean update(T obj, UsuarioDTO user) throws QueryException {
		try {
			obj.setActualizador(user.getNombre());
			obj.setFechaActualizacion(new Date());
			var statement = (IStatementSql<T>) sfactory.getStatement(motor, obj.getClass());
			var query = statement.update(obj);
			return db.executeIUD(query);
		} catch (Exception e) {
			throw new QueryException(i18n.valueBundle(LanguageConstant.LANGUAGE_ERROR_QUERY_INSERT), e);
		}
	}

	@SuppressWarnings("unchecked")
	public final <T extends ADto> Boolean insert(T obj, UsuarioDTO user) throws QueryException {
		try {
			if (StringUtils.isBlank(obj.getCodigo())) {
				obj.setCodigo(statementQueryUtils.genConsecutivo(obj.getClass(),
						countRow(obj.getClass().getConstructor().newInstance())));
			} else {
				if (countRow(obj) > 0) {
					throw new QueryException(i18n.valueBundle(LanguageConstant.LANGUAGE_ERROR_QUERY_ROW_EXISTS));
				}
			}
			obj.setCreador(user.getNombre());
			obj.setFechaCreacion(new Date());

			var statement = (IStatementSql<T>) sfactory.getStatement(motor, obj.getClass());
			var query = statement.insert(obj);

			return db.executeIUD(query);
		} catch (Exception e) {
			throw new QueryException(i18n.valueBundle(LanguageConstant.LANGUAGE_ERROR_QUERY_INSERT), e);
		}
	}

	private void removeJoinCache(String code) {
		if (mapJoin.containsKey(code)) {
			mapJoin.remove(code);
		}
	}

	private <T extends ADto> void updateJoinCache(T cached) {
		if (mapJoin.containsKey(cached.getCodigo())) {
			mapJoin.put(cached.getCodigo(), cached);
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
			removeJoinCache(obj.getCodigo());
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
