package com.pyt.service.implement;

import java.util.List;

import org.pyt.common.annotations.Inject;
import org.pyt.common.exceptions.GenericServiceException;
import org.pyt.common.exceptions.QueryException;

import com.pyt.query.interfaces.IQuerySvc;
import com.pyt.service.abstracts.Services;
import com.pyt.service.interfaces.ILanguageSvc;

import co.com.japl.ea.dto.system.LanguagesDTO;

public class LanguagesSvc extends Services implements ILanguageSvc {
	@Inject(resource = "com.pyt.query.implement.QuerySvc")
	private IQuerySvc querySvc;
	@Override
	public List<LanguagesDTO> getAll(LanguagesDTO dto) throws GenericServiceException{
		try {
			return querySvc.gets(dto);
		} catch (QueryException e) {
			throw new GenericServiceException("Error en obtener registros.",e);
		}
	}

}
