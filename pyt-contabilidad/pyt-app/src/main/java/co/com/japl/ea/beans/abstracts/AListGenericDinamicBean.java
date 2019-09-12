package co.com.japl.ea.beans.abstracts;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.pyt.common.abstracts.ADto;
import org.pyt.common.annotations.Inject;
import org.pyt.common.common.I18n;
import org.pyt.common.common.Log;

import com.pyt.service.dto.DocumentosDTO;
import com.pyt.service.interfaces.IGenericServiceSvc;

import co.com.japl.ea.interfaces.IGenericColumnLoad;
import co.com.japl.ea.interfaces.IGenericFieldLoad;
import co.com.japl.ea.interfaces.IGenericLoadValueFromField;
import co.com.japl.ea.interfaces.IUrlLoadBean;

public abstract class AListGenericDinamicBean<T extends ADto, S extends ADto, F extends ADto> extends ABean<T>
		implements IGenericFieldLoad, IGenericLoadValueFromField, IUrlLoadBean, IGenericColumnLoad {
	protected List<DocumentosDTO> genericFields;
	protected List<DocumentosDTO> genericColumns;
	private Map<String, Object> configFields;
	private Map<String, Object> configFieldList;
	@Inject(resource = "com.pyt.query.implement.GenericServiceSvc")
	private IGenericServiceSvc<S> querySvc;

	public AListGenericDinamicBean() {
		configFields = new HashMap<String, Object>();
		configFieldList = new HashMap<String, Object>();
	}

	@Override
	public IGenericServiceSvc<S> getServiceSvc() {
		return querySvc;
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
	public void warning(String msn) {
		alerta(msn);
	}

	@Override
	public void error(Throwable error) {
		error(error);
	}

	@Override
	public Map<String, Object> getConfigFields() {
		return configFields;
	}

	@Override
	public Map<String, Object> getConfigFieldTypeList() {
		return configFieldList;
	}

	@Override
	public List<DocumentosDTO> getColumns() {
		return genericColumns;
	}

	@Override
	public List<DocumentosDTO> getFields() {
		return genericFields;
	}
}
