package org.pyt.app.beans.dinamico;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.pyt.common.abstracts.ADto;
import org.pyt.common.annotations.Inject;
import org.pyt.common.common.I18n;
import org.pyt.common.common.Log;

import com.pyt.query.interfaces.IQuerySvc;
import com.pyt.service.dto.DocumentosDTO;

import co.com.japl.ea.beans.ABean;
import co.com.japl.ea.beans.IUrlLoadBean;

public abstract class AListGenericDinamicBean<T extends ADto> extends ABean<T>
		implements IGenericFieldLoad, IGenericLoadValueFromField, IUrlLoadBean, IGenericColumnLoad {
	protected List<DocumentosDTO> genericFields;
	protected List<DocumentosDTO> genericColumns;
	private Map<String, Object> configFields;
	private Map<String, Object> configFieldList;
	@Inject(resource = "com.pyt.query.implement.QuerySvc")
	private IQuerySvc querySvc;

	public AListGenericDinamicBean() {
		configFields = new HashMap<String, Object>();
		configFieldList = new HashMap<String, Object>();
	}

	@Override
	public IQuerySvc getServiceSvc() {
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
