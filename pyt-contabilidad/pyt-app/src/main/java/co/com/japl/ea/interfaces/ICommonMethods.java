package co.com.japl.ea.interfaces;

import org.pyt.common.abstracts.ADto;
import org.pyt.common.common.I18n;
import org.pyt.common.common.Log;
import org.pyt.common.common.UtilControlFieldFX;
import org.pyt.common.validates.ValidateValues;

import com.pyt.service.interfaces.IGenericServiceSvc;

public interface ICommonMethods<S extends ADto> {
	final ValidateValues validateValues = new ValidateValues();
	final UtilControlFieldFX controlFieldUtils = new UtilControlFieldFX();

	public Log getLogger();

	public I18n getI18n();

	public void warning(String msn);

	public void error(Throwable error);

	public IGenericServiceSvc<S> getServiceSvc();
}
