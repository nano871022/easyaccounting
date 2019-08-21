package co.com.japl.ea.beans;

import java.util.HashMap;
import java.util.Map;

import org.pyt.app.components.DataTableFXML;
import org.pyt.app.components.IGenericField;
import org.pyt.common.abstracts.ADto;
import org.pyt.common.annotations.Inject;
import org.pyt.common.common.I18n;
import org.pyt.common.common.Log;

import com.pyt.service.interfaces.IGenericServiceSvc;
import com.pyt.service.pojo.GenericPOJO;

import co.com.japl.ea.dto.system.ConfigGenericFieldDTO;

public abstract class AGenericInterfacesFieldBean<T extends ADto> extends ABean<T> implements IGenericField<T> {

	@Inject(resource = "com.pyt.service.implement.GenericServiceSvc")
	private IGenericServiceSvc<ConfigGenericFieldDTO> configGenericSvc;
	private Map<String, GenericPOJO<T>> fields;
	private Class<T> classTypeDto;
	private Map<String, Object> mapFieldUseds;

	@Override
	public Map<String, Object> getMapFieldUseds() {
		if (mapFieldUseds == null) {
			mapFieldUseds = new HashMap<String, Object>();
		}
		return mapFieldUseds;
	}

	@Override
	public Log getLogger() {
		return super.logger;
	}

	@Override
	public I18n getI18n() {
		return i18n();
	}

	@Override
	public Class<T> getClazz() {
		return classTypeDto;
	}

	@Override
	public void setClazz(Class<T> clazz) {
		classTypeDto = clazz;
	}

	@Override
	public DataTableFXML<T, T> getTable() {
		return null;
	}

	@Override
	public IGenericServiceSvc<ConfigGenericFieldDTO> configGenericFieldSvc() {
		return configGenericSvc;
	}

	@Override
	public Map<String, GenericPOJO<T>> getFields() {
		return fields;
	}

	@Override
	public void setFields(Map<String, GenericPOJO<T>> filters) {
		this.fields = filters;
	}

	@Override
	public T getRegister() {
		return registro;
	}

	@Override
	public void setRegister(T filter) {
		registro = filter;
	}
}
