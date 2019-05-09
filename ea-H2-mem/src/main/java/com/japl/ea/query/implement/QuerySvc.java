package com.japl.ea.query.implement;

import java.lang.reflect.InvocationTargetException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.pyt.common.common.ADto;
import org.pyt.common.common.UsuarioDTO;
import org.pyt.common.exceptions.QueryException;
import org.pyt.common.exceptions.ReflectionException;

import com.japl.ea.query.privates.H2Connect;
import com.japl.ea.query.utils.StatementQuerysUtil;
import com.pyt.query.interfaces.IAdvanceQuerySvc;
import com.pyt.query.interfaces.IQuerySvc;
import com.pyt.service.dto.ParametroDTO;

public class QuerySvc implements IQuerySvc, IAdvanceQuerySvc {

	private H2Connect db;
	private StatementQuerysUtil squ;

	public QuerySvc() {
		db = new H2Connect();
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T extends ADto> List<T> gets(T obj, Integer init, Integer end) throws QueryException {
		List<T> list = new ArrayList<T>();
		String query = "SELECT %s FROM %s WHERE %s LIMIT %s";
		ResultSet rs;
		try {
			List<String> names = obj.getNameFields();
			query = String.format(query, squ.fieldToSelect(obj), squ.getTableName(obj), squ.fieldToWhere(obj, false),
					init + "," + end);
			rs = db.executeQuery(query);
			while (rs.next()) {
				T newInstance = null;
				for (String name : names) {
					newInstance = (T) obj.getClass().getConstructor().newInstance();
					newInstance.set(name, rs.getObject(name));
				}
				list.add(newInstance);
			}
		} catch (SQLException e) {
			throw new QueryException("Error en consulta", e);
		} catch (ReflectionException e) {
			throw new QueryException("Error en consulta", e);
		} catch (InstantiationException e) {
			throw new QueryException("Error en consulta", e);
		} catch (IllegalAccessException e) {
			throw new QueryException("Error en consulta", e);
		} catch (IllegalArgumentException e) {
			throw new QueryException("Error en consulta", e);
		} catch (InvocationTargetException e) {
			throw new QueryException("Error en consulta", e);
		} catch (NoSuchMethodException e) {
			throw new QueryException("Error en consulta", e);
		} catch (SecurityException e) {
			throw new QueryException("Error en consulta", e);
		}
		return list;
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T extends ADto> List<T> gets(T obj) throws QueryException {
		List<T> list = new ArrayList<T>();
		String query = "SELECT %s FROM %s WHERE %s";
		ResultSet rs;
		try {
			List<String> names = obj.getNameFields();
			query = String.format(query, squ.fieldToSelect(obj), squ.getTableName(obj), squ.fieldToWhere(obj, false));
			rs = db.executeQuery(query);
			while (rs.next()) {
				T newInstance = null;
				for (String name : names) {
					newInstance = (T) obj.getClass().getConstructor().newInstance();
					newInstance.set(name, rs.getObject(name));
				}
				list.add(newInstance);
			}
		} catch (SQLException e) {
			throw new QueryException("Error en consulta", e);
		} catch (ReflectionException e) {
			throw new QueryException("Error en consulta", e);
		} catch (InstantiationException e) {
			throw new QueryException("Error en consulta", e);
		} catch (IllegalAccessException e) {
			throw new QueryException("Error en consulta", e);
		} catch (IllegalArgumentException e) {
			throw new QueryException("Error en consulta", e);
		} catch (InvocationTargetException e) {
			throw new QueryException("Error en consulta", e);
		} catch (NoSuchMethodException e) {
			throw new QueryException("Error en consulta", e);
		} catch (SecurityException e) {
			throw new QueryException("Error en consulta", e);
		}
		return list;
	}

	@Override
	public <T extends ADto> T get(T obj) throws QueryException {
		List<T> list = gets(obj);
		if (list == null || list.size() == 0) {
			throw new QueryException("No se encontraron registros.");
		}
		if (list.size() > 1) {
			throw new QueryException("Se encontraron varios registros." + list.size());
		}
		return list.get(0);
	}

	public <T extends ADto> void createTableStandard(T obj) throws QueryException {
		try {
			String query = "create table %s (%s)";
			List<String> listFields = obj.getNameFields();
			String fields = "";
			for (String field : listFields) {
				var type = obj.getType(field);
				if (type != null) {
					if (fields.length() > 0)
						fields += ",";
					fields += field + " " + squ.typeDataDB(type.getSimpleName()) + " NULL";
				}
			}
			fields += (", codigo VARCHAR2(100) NULL");
			fields += (", fechaCreacion DATETIME NULL");
			fields += (", fechaActualizacion DATETIME NULL");
			fields += (", fechaEliminacion DATETIME NULL");
			fields += (", creador VARCHAR2(100)");
			fields += (", actualizador VARCHAR2(100)");
			fields += (", eliminador VARCHAR2(100)");
			query = String.format(query, squ.getTableName(obj), fields);
			db.getStatement().executeUpdate(query);
		} catch (ReflectionException | SQLException e) {
			throw new QueryException("Error en la creacion de la tabla.", e);
		}
	}

	public <T extends ADto> void dropTable(T obj) throws QueryException {
		try {
			var query = "DROP TABLE " + squ.getTableName(obj);
			db.getStatement().executeUpdate(query);
		} catch (SQLException e) {
			throw new QueryException("Error en la elimiancion", e);
		}
	}

	@Override
	public <T extends ADto> T set(T obj, UsuarioDTO user) throws QueryException {
		var query = "INSERT INTO %s (%s) VALUES (%s)";
		if (StringUtils.isNotBlank(obj.getCodigo())) {
			query = "UPDATE %s SET %s WHERE %s";
		}
		try {
			query = String.format(query, squ.getTableName(obj), squ.fieldToSelect(obj),
					squ.fieldToWhere(obj, query.contains("INSERT")));
			Boolean rs = db.executeIUD(query);
			if (rs) {
				return get(obj);
			} else {
				throw new QueryException("No se logro ejecutar el query.");
			}
		} catch (SQLException e) {
			throw new QueryException("Error en consulta", e);
		} catch (ReflectionException e) {
			throw new QueryException("Error en consulta", e);
		} catch (IllegalArgumentException e) {
			throw new QueryException("Error en consulta", e);
		} catch (SecurityException e) {
			throw new QueryException("Error en consulta", e);
		}
	}

	@Override
	public <T extends ADto> void del(T obj, UsuarioDTO user) throws QueryException {
		String query = "DELETE FROM %s WHERE %s";
		try {
			query = String.format(query, squ.getTableName(obj), squ.fieldToWhere(obj, false));
			if (!db.executeIUD(query)) {
				throw new QueryException("Error en la ejecucion de la sentencia.");
			}
		} catch (SQLException e) {
			throw new QueryException("Error en queryException", e);
		} catch (ReflectionException e) {
			throw new QueryException("Error en consulta", e);
		} catch (IllegalArgumentException e) {
			throw new QueryException("Error en consulta", e);
		} catch (SecurityException e) {
			throw new QueryException("Error en consulta", e);
		}
	}

	@Override
	public <T extends ADto> Integer countRow(T obj) throws QueryException {
		String query = "SELECT count(1) as size FROM %s WHERE %s";
		ResultSet rs;
		try {
			query = String.format(query, squ.getTableName(obj), squ.fieldToWhere(obj, false));
			rs = db.executeQuery(query);
			while (rs.next()) {
				return rs.getInt("size");
			}
		} catch (SQLException e) {
			throw new QueryException("Error en consulta", e);
		} catch (ReflectionException e) {
			throw new QueryException("Error en consulta", e);
		} catch (IllegalArgumentException e) {
			throw new QueryException("Error en consulta", e);
		} catch (SecurityException e) {
			throw new QueryException("Error en consulta", e);
		}
		return 0;
	}

	public ResultSet queryLaunch(String query) throws QueryException {
		try {
			return db.getStatement().executeQuery(query);
		} catch (SQLException e) {
			throw new QueryException("Error en launcher query.", e);
		}
	}

	public final static void main(String... strings) {
		var query = new QuerySvc();
		var dto = new ParametroDTO();
		dto.setNombre("test1");
		try {
			// query.createTableStandard(dto);
			// ResultSet rs = query.queryLaunch("select * from mem_parametro");
			// query.del(dto, new UsuarioDTO());
			// query.set(dto, new UsuarioDTO());
			System.out.println(query.gets(dto, 2, 2).size());
			System.out.println(query.countRow(dto));
		} catch (QueryException e) {
			e.printStackTrace();
		}
	}
}
