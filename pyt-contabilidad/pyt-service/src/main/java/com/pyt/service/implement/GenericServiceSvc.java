package com.pyt.service.implement;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.pyt.common.abstracts.ADto;
import org.pyt.common.annotations.Inject;
import org.pyt.common.constants.LanguageConstant;
import org.pyt.common.exceptions.GenericServiceException;
import org.pyt.common.exceptions.QueryException;

import com.pyt.query.interfaces.IQuerySvc;
import com.pyt.service.abstracts.Services;
import com.pyt.service.interfaces.IGenericServiceSvc;

import co.com.japl.ea.dto.system.UsuarioDTO;

public class GenericServiceSvc<T extends ADto> extends Services implements IGenericServiceSvc<T> {
	@Inject(resource = "com.pyt.query.implement.QuerySvc")
	private IQuerySvc querySvc;

	public List<T> gets(T dto, Integer init, Integer end) throws GenericServiceException {
		List<T> lista = new ArrayList<T>();
		if (dto == null)
			throw new GenericServiceException(i18n().valueBundle(LanguageConstant.GENERIC_SERVICE_EMPTY_DTO));
		try {
			lista = querySvc.gets(dto, init, end);
		} catch (QueryException e) {
			throw new GenericServiceException(e.getMensage(), e);
		}
		return lista;
	}

	public List<T> getAll(T dto) throws GenericServiceException {
		List<T> lista = new ArrayList<T>();
		if (dto == null)
			throw new GenericServiceException(i18n().valueBundle(LanguageConstant.GENERIC_SERVICE_EMPTY_DTO));
		try {
			lista = querySvc.gets(dto);
		} catch (QueryException e) {
			throw new GenericServiceException(e.getMensage(), e);
		}
		return lista;
	}

	public T get(T dto) throws GenericServiceException {
		if (dto == null)
			throw new GenericServiceException(i18n().valueBundle(LanguageConstant.GENERIC_SERVICE_EMPTY_DTO));
		try {
			return querySvc.get(dto);
		} catch (QueryException e) {
			throw new GenericServiceException(e.getMensage(), e);
		}
	}

	public void update(T dto, UsuarioDTO user) throws GenericServiceException {
		if (dto == null)
			throw new GenericServiceException(i18n().valueBundle(LanguageConstant.GENERIC_SERVICE_EMPTY_DTO));
		if (StringUtils.isBlank(dto.getCodigo() ))
			throw new GenericServiceException(i18n().valueBundle(LanguageConstant.GENERIC_SERVICE_CODE_EMPTY_DTO));
		try {
			querySvc.set(dto, user);
		} catch (QueryException e) {
			throw new GenericServiceException(e.getMensage(), e);
		}
	}

	public T insert(T dto, UsuarioDTO user) throws GenericServiceException {
		if (dto == null)
			throw new GenericServiceException(i18n().valueBundle(LanguageConstant.GENERIC_SERVICE_EMPTY_DTO));
		if (StringUtils.isNotBlank(dto.getCodigo()))
			throw new GenericServiceException(i18n().valueBundle(LanguageConstant.GENERIC_SERVICE_CODE_EMPTY_DTO));
		try {
			return querySvc.set(dto, user);
		} catch (QueryException e) {
			throw new GenericServiceException(e.getMensage(), e);
		}
	}

	public void delete(T dto, UsuarioDTO user) throws GenericServiceException {
		if (dto == null)
			throw new GenericServiceException(i18n().valueBundle(LanguageConstant.GENERIC_SERVICE_EMPTY_DTO));
		if (StringUtils.isBlank(dto.getCodigo()))
			throw new GenericServiceException(i18n().valueBundle(LanguageConstant.GENERIC_SERVICE_CODE_EMPTY_DTO));
		try {
			querySvc.del(dto, user);
		} catch (Exception e) {
			throw new GenericServiceException(e.getMessage(), e);
		}

	}

	@Override
	public Integer getTotalRows(T dto) throws GenericServiceException {
		if(dto == null)throw new GenericServiceException(i18n().valueBundle(LanguageConstant.GENERIC_SERVICE_EMPTY_DTO));
		try {
			return querySvc.countRow(dto);
		} catch (QueryException e) {
			throw new GenericServiceException(e.getMensage(), e);
		}
	}

}
