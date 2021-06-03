package co.com.japl.ea.service.implement;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.pyt.common.annotations.Inject;

import co.com.japl.ea.common.abstracts.ADto;
import co.com.japl.ea.dto.abstracts.Services;
import co.com.japl.ea.dto.interfaces.IConfigGenericFieldSvc;
import co.com.japl.ea.dto.interfaces.IGenericServiceSvc;
import co.com.japl.ea.dto.system.ConfigGenericFieldDTO;
import co.com.japl.ea.exceptions.ConfigGenericFieldException;
import co.com.japl.ea.exceptions.GenericServiceException;

public class ConfigGenericFieldSvc extends Services implements IConfigGenericFieldSvc {
	@Inject(resource = "com.pyt.service.implement.GenericServiceSvc")
	private IGenericServiceSvc<ConfigGenericFieldDTO> servicesSvc;
	@Override
	public <B, D extends ADto> List<ConfigGenericFieldDTO> getFieldToFilters(Class<B> bean, Class<D> dto)
			throws ConfigGenericFieldException {
		if(bean == null || dto == null) {
			throw new ConfigGenericFieldException( i18n().valueBundle("err.parameter.send.empty").get());
		}
		var config = new ConfigGenericFieldDTO();
		config.setClassPath(dto.getCanonicalName());
		config.setClassPathBean(bean.getCanonicalName());
		config.setIsFilter(true);
		try {
			return servicesSvc.getAll(config).stream()
					.sorted((val1, val2) ->
					Optional.ofNullable(val1.getOrden()).orElse(0)
							.compareTo(Optional.ofNullable(val2.getOrden()).orElse(0))
						).collect(Collectors.toList());
		} catch (GenericServiceException e) {
			throw new ConfigGenericFieldException(e);
		}
	}
	@Override
	public <B, D extends ADto> List<ConfigGenericFieldDTO> getFieldToFields(Class<B> bean, Class<D> dto)
			throws ConfigGenericFieldException {
		if(bean == null || dto == null) {
			throw new ConfigGenericFieldException(i18n().valueBundle("err.parameter.send.empty").get());
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
			throw new ConfigGenericFieldException(i18n().valueBundle("err.parameter.send.empty").get());
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
