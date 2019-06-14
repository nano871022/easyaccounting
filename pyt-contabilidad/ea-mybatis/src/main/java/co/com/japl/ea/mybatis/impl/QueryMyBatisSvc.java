package co.com.japl.ea.mybatis.impl;

import java.io.IOException;
import java.util.List;

import org.pyt.common.abstracts.ADto;
import org.pyt.common.common.Log;
import org.pyt.common.common.UsuarioDTO;
import org.pyt.common.exceptions.QueryException;

import com.pyt.query.interfaces.IQuerySvc;

import co.com.japl.ea.mybatis.privates.ConnectionMyBatis;
import co.com.japl.ea.mybatis.privates.constants.MyBatisConstants;

public class QueryMyBatisSvc implements IQuerySvc {

	private ConnectionMyBatis connect;
	private Log logger = Log.Log(this.getClass());

	public QueryMyBatisSvc() {
		try {
			connect = new ConnectionMyBatis();
		} catch (IOException e) {
			logger.logger(e);
		}
	}

	@Override
	public <T extends ADto> List<T> gets(T obj, Integer init, Integer end) throws QueryException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <T extends ADto> List<T> gets(T obj) throws QueryException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <T extends ADto> T get(T obj) throws QueryException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <T extends ADto> T set(T obj, UsuarioDTO user) throws QueryException {
		if (obj != null) {
			if (obj.getCodigo().isEmpty()) {
				var session = connect.openConnection();
				var returns = session.insert(
						obj.getClass().getName() + MyBatisConstants.CONST_DOT + MyBatisConstants.CONST_PROCESS_INSERT,
						obj);
				session.commit();
				session.close();
			}
		}
		return null;
	}

	@Override
	public <T extends ADto> void del(T obj, UsuarioDTO user) throws QueryException {
		if (obj != null) {
			if (obj.getCodigo().isEmpty()) {
				var session = connect.openConnection();
				var returns = session.delete(
						obj.getClass().getName() + MyBatisConstants.CONST_DOT + MyBatisConstants.CONST_PROCESS_DELETE,
						obj);
				session.commit();
				session.close();
			}
		}
	}

	@Override
	public <T extends ADto> Integer countRow(T obj) throws QueryException {
		// TODO Auto-generated method stub
		return null;
	}

}
