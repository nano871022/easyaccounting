package org.pyt.app.beans.dinamico;

import java.util.List;
import java.util.Map;

import org.pyt.common.abstracts.ADto;
import org.pyt.common.common.I18n;
import org.pyt.common.common.Log;
import org.pyt.common.validates.ValidateValues;

import com.pyt.service.dto.DocumentosDTO;

public interface IGenericFieldCommon {
	ValidateValues validateValue = new ValidateValues();

	public Map<String, Object> getConfigFields();

	public void warning(String msn);

	public void error(Throwable error);

	public <T extends ADto> T getInstanceDTOUse();

	public Map<String, Object> getConfigFieldTypeList();

	public List<DocumentosDTO> getFields();

	public Log getLogger();

	public I18n getI18n();

}
