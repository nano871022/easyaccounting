package co.com.japl.ea.interfaces;

import java.util.List;
import java.util.Map;

import org.pyt.common.abstracts.ADto;
import org.pyt.common.common.I18n;
import org.pyt.common.common.Log;
import org.pyt.common.validates.ValidateValues;

import com.pyt.service.interfaces.IGenericServiceSvc;

public interface IGenericMethodsCommon<T extends ADto, S extends ADto, F extends ADto> {
	final ValidateValues validateValues = new ValidateValues();

	public Log getLogger();

	public I18n getI18n();

	public void warning(String msn);

	public void error(Throwable error);

	public IGenericServiceSvc<S> getServiceSvc();

	public Map<String, Object> getConfigFields();

	public Map<String, Object> getConfigFieldTypeList();

	public List<F> getFields();

	public T getInstanceDTOUse();

}
