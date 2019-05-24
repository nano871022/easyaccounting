package com.japl.ea.query.implement;

import java.io.FileNotFoundException;
import java.lang.reflect.InvocationTargetException;
import java.nio.charset.Charset;
import java.sql.SQLException;
import java.util.ArrayList;
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
import org.pyt.common.reflection.ReflectionUtils;

import com.japl.ea.query.privates.H2Connect;
import com.japl.ea.query.privates.utils.StatementQuerysUtil;
import com.pyt.query.interfaces.IQuerySvc;
import com.pyt.service.dto.ParametroDTO;

public class QueryJPASvc implements IQuerySvc {

	private H2Connect db;
	private StatementQuerysUtil squ;
	private I18n i18n;

	public QueryJPASvc() {
		db = H2Connect.getInstance();
		squ = new StatementQuerysUtil();
		i18n = new I18n();
	}

	@SuppressWarnings("unchecked")
	private <T extends Object, D extends ADto> Class<T> getJPAByDto(D dto) throws ClassNotFoundException {
		var name = dto.getClass().getSimpleName().replace("DTO", "JPA");
		var packageName = "com.japl.ea.query.privates.jpa.";
		return (Class<T>) Class.forName(packageName + name);
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T extends ADto> List<T> gets(T obj, Integer init, Integer end) throws QueryException {
		List<T> list = new ArrayList<T>();
		try {
			if (db.getHibernateConnect() != null) {
				if (!db.getHibernateConnect().getTransaction().isActive()) {
					db.getHibernateConnect().beginTransaction();
				}
				var builder = db.getHibernateConnect().getCriteriaBuilder();
				var querys = builder.createQuery(getJPAByDto(obj));
				var root = querys.from(getJPAByDto(obj));
				querys.select(root);
				var map = squ.getFieldToWhereJPA(obj);
				map.keySet().forEach(key -> {
					if (map.get(key) instanceof String && ((String) map.get(key)).contains("%")) {
						querys.where(builder.like(root.get(key), ((String) map.get(key))));
					} else {
						querys.where(builder.equal(root.get(key), map.get(key)));
					}
				});
				var create = db.getHibernateConnect().createQuery(querys);
				if (end > 0) {
					create.setFirstResult(init);
					create.setMaxResults(init + end);
				}
				var lists = create.getResultList();
				lists.forEach(jpa -> list.add((T) ReflectionUtils.instanciar().copy(jpa, obj.getClass())));
			}
		} catch (ReflectionException | IllegalArgumentException | SecurityException | ClassNotFoundException e) {
			throw new QueryException(i18n.valueBundle(LanguageConstant.LANGUAGE_ERROR_QUERY_SEARCH), e);
		}
		return list;
	}

	@Override
	public <T extends ADto> List<T> gets(T obj) throws QueryException {
		List<T> list = new ArrayList<T>();
		list = gets(obj, 0, 0);
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

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public <T extends ADto> T set(T obj, UsuarioDTO user) throws QueryException {
		try {
			if (db.getHibernateConnect() != null) {
				if (!db.getHibernateConnect().getTransaction().isActive()) {
					db.getHibernateConnect().beginTransaction();
				}
				var builder = db.getHibernateConnect();
				if (StringUtils.isNotBlank(obj.getCodigo())) {
					obj.setActualizador(user.getNombre());
					obj.setFechaActualizacion(new Date());
				} else {
					obj.setCreador(user.getNombre());
					obj.setFechaCreacion(new Date());
					obj.setCodigo(squ.genConsecutivo(obj.getClass(),
							countRow(obj.getClass().getConstructor().newInstance())));
				}
				Class classJpa = getJPAByDto(obj);
				var jpa = ReflectionUtils.instanciar().copyFromDto(obj, classJpa);
				builder.saveOrUpdate(jpa);
				db.getHibernateConnect().getTransaction().commit();
				return (T) ReflectionUtils.instanciar().copy(jpa, obj.getClass());
			}
		} catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException
				| NoSuchMethodException | SecurityException | ClassNotFoundException e) {
			throw new QueryException(i18n.valueBundle(LanguageConstant.LANGUAGE_ERROR_QUERY_INSERT_UPDATE), e);
		}
		return obj;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public <T extends ADto> void del(T obj, UsuarioDTO user) throws QueryException {
		try {
			if (db.getHibernateConnect() != null) {
				if (!db.getHibernateConnect().getTransaction().isActive()) {
					db.getHibernateConnect().beginTransaction();
				}
				var builder = db.getHibernateConnect();
				Class classJpa = getJPAByDto(obj);
				var jpa = ReflectionUtils.instanciar().copyFromDto(obj, classJpa);
				builder.delete(jpa);
				db.getHibernateConnect().getTransaction().commit();
				obj =  (T) ReflectionUtils.instanciar().copy(jpa, obj.getClass());
			}
		} catch ( IllegalArgumentException | SecurityException | ClassNotFoundException e) {
			throw new QueryException(i18n.valueBundle(LanguageConstant.LANGUAGE_ERROR_QUERY_DELETE_ROW), e);
		}
	}

	@Override
	public <T extends ADto> Integer countRow(T obj) throws QueryException {
		try {
			if (db.getHibernateConnect() != null) {
				if (!db.getHibernateConnect().getTransaction().isActive()) {
					db.getHibernateConnect().beginTransaction();
				}
				var builder = db.getHibernateConnect().getCriteriaBuilder();
				var querys = builder.createQuery(Long.class);
				var root = querys.from(getJPAByDto(obj));
				querys.select(builder.count(root.get("codigo")));
				var map = squ.getFieldToWhereJPA(obj);
				map.keySet().forEach(key -> {
					if (map.get(key) instanceof String && (((String) map.get(key)).contains("%")
							|| ((String) map.get(key)).contains("_"))) {
						querys.where(builder.like(root.get(key), ((String) map.get(key))));
					} else {
						querys.where(builder.equal(root.get(key), map.get(key)));
					}
				});
				var create = db.getHibernateConnect().createQuery(querys);
				var result = create.getSingleResult();
				return Integer.valueOf(result.toString());
			}
		} catch (ReflectionException | IllegalArgumentException | SecurityException | ClassNotFoundException e) {
			throw new QueryException(i18n.valueBundle(LanguageConstant.LANGUAGE_ERROR_QUERY_COUNT_ROWS), e);
		}
		return 0;
	}

	public final static void main(String... strings) {
		var usuario = new UsuarioDTO();
		usuario.setNombre("MASTER");
		var query = new QueryJPASvc();
		var dto = new ParametroDTO();
//		dto.setNombre("test%");
		try {
			// query.createTableStandard(dto);
			// ResultSet rs = query.queryLaunch("select * from mem_parametro");
			// query.del(dto, usuario);
//			 query.set(dto, usuario);
			// query.createTableStandard(new ParametroDelDTO());
			// query.createTrigger(ParametroDTO.class, triggerOption.BEFORE,
			// triggerAction.DELETE);
			System.out.println("Size::" + query.gets(dto, 0, 2).size());
			query.gets(dto, 0, 2).forEach(row -> System.out.println(row.toStringAll()));
			/*
			 * query.gets(dto, 1, 2).forEach(obj -> { try { query.del(obj, usuario); } catch
			 * (QueryException e) { e.printStackTrace(); } });
			 */
			System.out.println(query.countRow(dto));
		} catch (QueryException e) {
			e.printStackTrace();
		} catch (Exception e) {
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
