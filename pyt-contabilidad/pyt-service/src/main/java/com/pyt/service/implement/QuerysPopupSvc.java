package com.pyt.service.implement;

import java.util.ArrayList;
import java.util.List;

import org.pyt.common.annotations.Inject;
import org.pyt.common.common.ADto;
import org.pyt.common.common.UsuarioDTO;
import org.pyt.common.exceptions.QueryException;
import org.pyt.common.exceptions.QuerysPopupException;

import com.pyt.query.interfaces.IQuerySvc;
import com.pyt.service.abstracts.Services;
import com.pyt.service.interfaces.IQuerysPopup;

public class QuerysPopupSvc extends Services implements IQuerysPopup {
	@Inject(resource = "com.pyt.query.implement.QuerySvc")
	private IQuerySvc querys;
	@Override
	public <T extends ADto> List<T> list(T filter, UsuarioDTO user) throws QuerysPopupException {
		if(filter ==  null) throw new QuerysPopupException("No se suminsitro el filtro.");
		if(user == null)throw new QuerysPopupException("No se suministro el usuario");
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
		if(filter ==  null) throw new QuerysPopupException("No se suminsitro el filtro.");
		if(user == null)throw new QuerysPopupException("No se suministro el usuario");
		if(inicial == null)throw new QuerysPopupException("No se suministro el registro inicial");
		if(cantidad == null)throw new QuerysPopupException("No se suministro la cantidad de registros");
		if(cantidad == 0)throw new QuerysPopupException("La cantidad de registros no puede ser 0.");
		if(inicial < 0)throw new QuerysPopupException("El valor de reigistro inicial no puede ser meno a 0");
		if(cantidad < 0)throw new QuerysPopupException("La cantidad no puede ser menor a 0");
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
		if(filter ==  null) throw new QuerysPopupException("No se suminsitro el filtro.");
		if(user == null)throw new QuerysPopupException("No se suministro el usuario");
		Integer records = 0;
		try {
			records = querys.countRow(filter);
		}catch(QueryException e) {
			throw new QuerysPopupException(e);
		}
		return records;
	}

}
