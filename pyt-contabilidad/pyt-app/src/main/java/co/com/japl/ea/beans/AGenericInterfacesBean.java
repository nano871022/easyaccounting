package co.com.japl.ea.beans;

import java.util.Map;

import org.pyt.app.components.IGenericColumn;
import org.pyt.app.components.IGenericFilter;
import org.pyt.common.abstracts.ADto;
import org.pyt.common.annotations.Inject;
import org.pyt.common.common.I18n;
import org.pyt.common.common.Log;

import com.pyt.service.interfaces.IGenericServiceSvc;
import com.pyt.service.pojo.GenericPOJO;

import co.com.japl.ea.dto.system.ConfigGenericFieldDTO;

public abstract class AGenericInterfacesBean<T extends ADto> extends ABean<T>
		implements IGenericColumn<T>, IGenericFilter<T> {

	@Inject(resource = "com.pyt.service.implement.GenericServiceSvc")
	private IGenericServiceSvc<ConfigGenericFieldDTO> configGenericSvc;
	private Map<String, GenericPOJO<T>> filtersMap;
	private Map<String, GenericPOJO<T>> columnsMap;
	protected T filtro;
	private Class<T> classTypeDto;

	@Override
	public Map<String, GenericPOJO<T>> getFilters() {
		return filtersMap;
	}

	@Override
	public void setFilters(Map<String, GenericPOJO<T>> filters) {
		filtersMap = filters;
	}

	@Override
	public Map<String, GenericPOJO<T>> getColumns() {
		return columnsMap;
	}

	@Override
	public void setColumns(Map<String, GenericPOJO<T>> columns) {
		columnsMap = columns;
	}

	@Override
	public Log getLogger() {
		return logger;
	}

	@Override
	public I18n getI18n() {
		return i18n();
	}

	@Override
	public void setClazz(Class<T> clazz) {
		classTypeDto = clazz;
	}

	@Override
	public T getFilter() {
		return filtro;
	}

	@Override
	public void setFilter(T filter) {
		filtro = filter;
	}

	@Override
	public IGenericServiceSvc<ConfigGenericFieldDTO> configGenericFieldSvc() {
		return configGenericSvc;
	}

	@Override
	public Class<T> getClazz() {
		return classTypeDto;
	}
}
