package co.com.japl.ea.service.implement;

import static org.pyt.common.constants.LanguageConstant.CONST_EXC_QUERYS_COUNT_CANT_LESS_0;
import static org.pyt.common.constants.LanguageConstant.CONST_EXC_QUERYS_COUNT_REG_CANT_0;
import static org.pyt.common.constants.LanguageConstant.CONST_EXC_QUERYS_NOT_SUBMITION_FILTER;
import static org.pyt.common.constants.LanguageConstant.CONST_EXC_QUERYS_NOT_SUBMITION_REG_COUNT;
import static org.pyt.common.constants.LanguageConstant.CONST_EXC_QUERYS_NOT_SUBMITION_REG_INIT;
import static org.pyt.common.constants.LanguageConstant.CONST_EXC_QUERYS_NOT_SUBMITION_USER;
import static org.pyt.common.constants.LanguageConstant.CONST_EXC_QUERYS_VAL_REG_INIT_CANT_LESS_0;

import java.util.ArrayList;
import java.util.List;

import org.pyt.common.annotations.Inject;

import co.com.japl.ea.query.interfaces.IQuerySvc;
import co.com.japl.ea.common.abstracts.ADto;
import co.com.japl.ea.dto.abstracts.Services;
import co.com.japl.ea.dto.interfaces.IQuerysPopup;
import co.com.japl.ea.dto.system.UsuarioDTO;
import co.com.japl.ea.exceptions.QueryException;
import co.com.japl.ea.exceptions.QuerysPopupException;

public class QuerysPopupSvc extends Services implements IQuerysPopup {
	@Inject(resource = "co.com.japl.ea.query.implement.QuerySvc")
	private IQuerySvc querys;
	@Override
	public <T extends ADto> List<T> list(T filter, UsuarioDTO user) throws QuerysPopupException {
		if (filter == null)
			throw new QuerysPopupException(i18n().valueBundle(CONST_EXC_QUERYS_NOT_SUBMITION_FILTER).get());
		if (user == null)
			throw new QuerysPopupException(i18n().valueBundle(CONST_EXC_QUERYS_NOT_SUBMITION_USER).get());
		List<T> list = new ArrayList<T>();
		try {
			list = querys.gets(filter);
		}catch(QueryException e) {
			throw new QuerysPopupException(e);
		}
		return list;
	}

	@Override
	public <T extends ADto> List<T> list(T filter, Integer inicial, Integer cantidad, UsuarioDTO user)
			throws QuerysPopupException {
		if (filter == null)
			throw new QuerysPopupException(i18n().valueBundle(CONST_EXC_QUERYS_NOT_SUBMITION_FILTER).get());
		if (user == null)
			throw new QuerysPopupException(i18n().valueBundle(CONST_EXC_QUERYS_NOT_SUBMITION_USER).get());
		if (inicial == null)
			throw new QuerysPopupException(i18n().valueBundle(CONST_EXC_QUERYS_NOT_SUBMITION_REG_INIT).get());
		if (cantidad == null)
			throw new QuerysPopupException(i18n().valueBundle(CONST_EXC_QUERYS_NOT_SUBMITION_REG_COUNT).get());
		if (cantidad == 0)
			throw new QuerysPopupException(i18n().valueBundle(CONST_EXC_QUERYS_COUNT_REG_CANT_0).get());
		if (inicial < 0)
			throw new QuerysPopupException(i18n().valueBundle(CONST_EXC_QUERYS_VAL_REG_INIT_CANT_LESS_0).get());
		if (cantidad < 0)
			throw new QuerysPopupException(i18n().valueBundle(CONST_EXC_QUERYS_COUNT_CANT_LESS_0).get());
		List<T> list = new ArrayList<T>();
		try {
			if(inicial == 1)inicial = 0;
			list = querys.gets(filter,inicial,cantidad);
		}catch(QueryException e) {
			throw new QuerysPopupException(e);
		}
		return list;
	}

	@Override
	public <T extends ADto> Integer records(T filter, UsuarioDTO user) throws QuerysPopupException {
		if (filter == null)
			throw new QuerysPopupException(i18n().valueBundle(CONST_EXC_QUERYS_NOT_SUBMITION_FILTER).get());
		if (user == null)
			throw new QuerysPopupException(i18n().valueBundle(CONST_EXC_QUERYS_NOT_SUBMITION_USER).get());
		Integer records = 0;
		try {
			records = querys.countRow(filter);
		}catch(QueryException e) {
			throw new QuerysPopupException(e);
		}
		return records;
	}

}