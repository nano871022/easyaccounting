package co.com.japl.ea.mybatis.impl;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.binding.BindingException;
import org.apache.ibatis.session.RowBounds;
import org.apache.ibatis.session.SqlSession;
import org.pyt.common.abstracts.ADto;
import org.pyt.common.common.Log;
import org.pyt.common.common.UsuarioDTO;
import org.pyt.common.exceptions.QueryException;

import com.pyt.query.interfaces.IQuerySvc;
import com.pyt.query.utils.StatementQuerysUtil;

import co.com.japl.ea.mybatis.privates.ConnectionMyBatis;
import co.com.japl.ea.mybatis.privates.constants.MyBatisConstants;
import co.com.japl.ea.mybatis.privates.interfaces.GenericInterfaces;

public class QueryMyBatisSvc implements IQuerySvc {

	private ConnectionMyBatis connect;
	private Log logger = Log.Log(this.getClass());
	private StatementQuerysUtil squ;

	public QueryMyBatisSvc() {
		try {
			connect = new ConnectionMyBatis();
			squ = new StatementQuerysUtil();
		} catch (IOException e) {
			logger.logger(e);
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T extends ADto> List<T> gets(T obj, Integer init, Integer end) throws QueryException {
		var list = new ArrayList<T>();
		if (obj != null) {
			var session = connect.openConnection();

			var listObject = session.selectList(getStatement(obj, MyBatisConstants.CONST_PROCESS_SELECT), obj,
					new RowBounds(init, end));
			listObject.forEach(object -> list.add((T) object));
			session.commit();
			session.close();
		}
		return list;
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T extends ADto> List<T> gets(T obj) throws QueryException {
		var list = new ArrayList<T>();
		if (obj != null) {
			var session = connect.openConnection();
			List listObject = null;
			var clazz = verifyMapper(obj, session);
			if (clazz != null) {
				GenericInterfaces generic = (GenericInterfaces) session.getMapper(clazz);
				listObject = generic.gets(obj);
			} else {
				listObject = session.selectList(getStatement(obj, MyBatisConstants.CONST_PROCESS_SELECT), obj);
			}
			listObject.forEach(object -> list.add((T) object));
			session.commit();
			session.close();
		}
		return list;
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T extends ADto> T get(T obj) throws QueryException {
		T returns = null;
		if (obj != null) {
			var session = connect.openConnection();
			var clazz = verifyMapper(obj, session);
			if (clazz != null) {
				GenericInterfaces generic = (GenericInterfaces) session.getMapper(clazz);
				returns = (T) generic.get(obj);
			} else {
				returns = session.selectOne(getStatement(obj, MyBatisConstants.CONST_PROCESS_SELECT), obj);
			}
			session.commit();
			session.close();
		}
		return returns;
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T extends ADto> T set(T obj, UsuarioDTO user) throws QueryException {
		try {
			var nowDate = Date.from(LocalDate.now().atStartOfDay(ZoneId.systemDefault()).toInstant());
			if (obj != null) {
				SqlSession session = null;
				var returns = 0;
				if (StringUtils.isBlank(obj.getCodigo())) {
					obj.setCodigo(squ.genConsecutivo(obj.getClass(),
							countRow(obj.getClass().getConstructor().newInstance())));
					obj.setCreador(user.getNombre());
					obj.setFechaCreacion(nowDate);
					session = connect.openConnection();
					var clazz = verifyMapper(obj, session);
					if (clazz != null) {
						GenericInterfaces generic = (GenericInterfaces) session.getMapper(clazz);
						generic.insert(obj);
						returns = 1;
					} else {
						returns = session.insert(getStatement(obj, MyBatisConstants.CONST_PROCESS_INSERT), obj);
					}
				} else {
					session = connect.openConnection();
					obj.setActualizador(user.getNombre());
					obj.setFechaActualizacion(nowDate);
					returns = session.update(getStatement(obj, MyBatisConstants.CONST_PROCESS_UPDATE), obj);
				}
				session.commit();
				session.close();
				if (returns == 0) {
					throw new QueryException("No se ingreso/actualizo ningún registro.");
				} else {
					obj = get(obj);
				}
			}
		} catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException
				| NoSuchMethodException | SecurityException e) {
			throw new QueryException("Error presentado con obtencion del constructor", e);
		}
		return obj;
	}

	@Override
	public <T extends ADto> void del(T obj, UsuarioDTO user) throws QueryException {
		if (obj != null) {
			if (StringUtils.isNotBlank(obj.getCodigo())) {
				var session = connect.openConnection();
				var returns = 0;
				var clazz = verifyMapper(obj, session);
				if (clazz != null) {
					GenericInterfaces generic = (GenericInterfaces) session.getMapper(clazz);
					generic.delete(obj);
					returns = 1;
				} else {
					returns = session.delete(getStatement(obj, MyBatisConstants.CONST_PROCESS_DELETE), obj);
				}
				if (returns == 0) {
					throw new QueryException("No se elimino ningún registro.");
				}
				session.commit();
				session.close();
			}
		}
	}

	@Override
	public <T extends ADto> Integer countRow(T obj) throws QueryException {
		Integer count = 0;
		if (obj != null) {
			var session = connect.openConnection();
			var clazz = verifyMapper(obj, session);
			count = session.selectOne(getStatement(obj, MyBatisConstants.CONST_PROCESS_SELECT_COUNT), obj);
			if (count == null) {
				count = 0;
			}
			session.commit();
			session.close();
		}
		return count;
	}

	private final <T extends ADto> String getStatement(T obj, String type) {
		return obj.getClass().getName().replace("DTO", "").toLowerCase() + MyBatisConstants.CONST_DOT + type;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private final <T extends Object,S extends Object> Class verifyMapper(T dto, SqlSession session) {
		try {
			if (dto != null) {
				var name = dto.getClass().getSimpleName().replace("DTO", "");
				var path = "co.com.japl.ea.mybatis.privates.interfaces.";
				var clazz = Class.forName(path + name);
				if (clazz != null) {
					if (clazz.asSubclass(GenericInterfaces.class) != null) {
						S cclazz = null;
						try {
							cclazz = (S) session.getMapper(clazz);
						}catch(BindingException e) {
							logger.logger(e);
						}
						if (cclazz == null) {
							session.getConfiguration().addMapper(clazz);
							return clazz;
						}else {
							return clazz;
						}
					}
				}
			}
		} catch (ClassNotFoundException e) {
			logger.logger(e);
		}
		return null;
	}

}
