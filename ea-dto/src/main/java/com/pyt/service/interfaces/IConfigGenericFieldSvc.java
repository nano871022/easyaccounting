package com.pyt.service.interfaces;

import java.util.List;

import org.pyt.common.abstracts.ADto;
import org.pyt.common.exceptions.ConfigGenericFieldException;

import co.com.japl.ea.dto.system.ConfigGenericFieldDTO;

public interface IConfigGenericFieldSvc {
	
	public <B,D extends ADto> List<ConfigGenericFieldDTO> getFieldToFilters(Class<B> bean,Class<D> dto)throws ConfigGenericFieldException;
	public <B,D extends ADto> List<ConfigGenericFieldDTO> getFieldToFields(Class<B> bean,Class<D> dto)throws ConfigGenericFieldException;
	public <B,D extends ADto> List<ConfigGenericFieldDTO> getFieldToColumns(Class<B> bean,Class<D> dto)throws ConfigGenericFieldException;
	
}
