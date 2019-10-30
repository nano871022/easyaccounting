package com.pyt.service.implement;

import java.util.List;

import org.pyt.common.abstracts.ADto;
import org.pyt.common.annotations.Inject;
import org.pyt.common.exceptions.ConfigGenericFieldException;
import org.pyt.common.exceptions.GenericServiceException;

import com.pyt.service.abstracts.Services;
import com.pyt.service.interfaces.IConfigGenericFieldSvc;
import com.pyt.service.interfaces.IGenericServiceSvc;

import co.com.japl.ea.dto.system.ConfigGenericFieldDTO;

public class ConfigGenericFieldSvc extends Services implements IConfigGenericFieldSvc {
	@Inject(resource = "com.pyt.service.implement.GenericServiceSvc")
	private IGenericServiceSvc<ConfigGenericFieldDTO> servicesSvc;
	@Override
	public <B, D extends ADto> List<ConfigGenericFieldDTO> getFieldToFilters(Class<B> bean, Class<D> dto)
			throws ConfigGenericFieldException {
		if(bean == null || dto == null) {
			throw new ConfigGenericFieldException("ALguno de los parametros suministrado se encuentra vacio.");
		}
		var config = new ConfigGenericFieldDTO();
		config.setClassPath(dto.getCanonicalName());
		config.setClassPathBean(bean.getCanonicalName());
		config.setIsFilter(true);
		try {
			return servicesSvc.getAll(config);
		} catch (GenericServiceException e) {
			throw new ConfigGenericFieldException(e);
		}
	}
	@Override
	public <B, D extends ADto> List<ConfigGenericFieldDTO> getFieldToFields(Class<B> bean, Class<D> dto)
			throws ConfigGenericFieldException {
		if(bean == null || dto == null) {
			throw new ConfigGenericFieldException("ALguno de los parametros suministrado se encuentra vacio.");
		}
		var config = new ConfigGenericFieldDTO();
		config.setClassPath(dto.getCanonicalName());
		config.setClassPathBean(bean.getCanonicalName());
		try {
			return servicesSvc.getAll(config);
		} catch (GenericServiceException e) {
			throw new ConfigGenericFieldException(e);
		}
	}
	@Override
	public <B, D extends ADto> List<ConfigGenericFieldDTO> getFieldToColumns(Class<B> bean, Class<D> dto)
			throws ConfigGenericFieldException {
		if(bean == null || dto == null) {
			throw new ConfigGenericFieldException("ALguno de los parametros suministrado se encuentra vacio.");
		}
		var config = new ConfigGenericFieldDTO();
		config.setClassPath(dto.getCanonicalName());
		config.setClassPathBean(bean.getCanonicalName());
		config.setIsColumn(true);
		try {
			return servicesSvc.getAll(config);
		} catch (GenericServiceException e) {
			throw new ConfigGenericFieldException(e);
		}
	}

}
