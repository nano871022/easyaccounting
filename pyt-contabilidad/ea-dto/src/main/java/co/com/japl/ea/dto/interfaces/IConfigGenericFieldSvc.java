package co.com.japl.ea.dto.interfaces;

import java.util.List;

import co.com.japl.ea.common.abstracts.ADto;
import co.com.japl.ea.dto.system.ConfigGenericFieldDTO;
import co.com.japl.ea.exceptions.ConfigGenericFieldException;

public interface IConfigGenericFieldSvc {
	
	public <B,D extends ADto> List<ConfigGenericFieldDTO> getFieldToFilters(Class<B> bean,Class<D> dto)throws ConfigGenericFieldException;
	public <B,D extends ADto> List<ConfigGenericFieldDTO> getFieldToFields(Class<B> bean,Class<D> dto)throws ConfigGenericFieldException;
	public <B,D extends ADto> List<ConfigGenericFieldDTO> getFieldToColumns(Class<B> bean,Class<D> dto)throws ConfigGenericFieldException;
	
}
