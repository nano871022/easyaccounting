package co.com.japl.ea.gdb.impls;

import java.util.List;

import org.pyt.common.abstracts.ADto;
import org.pyt.common.common.Log;
import org.pyt.common.common.UsuarioDTO;
import org.pyt.common.exceptions.QueryException;

import com.pyt.query.interfaces.IQuerySvc;

public class QueryGDBSvc implements IQuerySvc {
	private Log logger = Log.Log(this.getClass());
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
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <T extends ADto> void del(T obj, UsuarioDTO user) throws QueryException {
		// TODO Auto-generated method stub

	}

	@Override
	public <T extends ADto> Integer countRow(T obj) throws QueryException {
		// TODO Auto-generated method stub
		return null;
	}

}
