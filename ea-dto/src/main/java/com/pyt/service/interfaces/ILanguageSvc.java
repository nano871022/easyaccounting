package com.pyt.service.interfaces;

import java.util.List;

import org.pyt.common.exceptions.GenericServiceException;

import co.com.japl.ea.dto.system.LanguagesDTO;

public interface ILanguageSvc {
	public List<LanguagesDTO> getAll(LanguagesDTO dto) throws GenericServiceException;
}
