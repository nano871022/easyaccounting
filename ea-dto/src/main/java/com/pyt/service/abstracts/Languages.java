package com.pyt.service.abstracts;

import java.util.List;

import org.pyt.common.annotations.Inject;
import org.pyt.common.common.Log;
import org.pyt.common.exceptions.GenericServiceException;
import org.pyt.common.reflection.Reflection;
import com.pyt.service.interfaces.ILanguageSvc;

import co.com.japl.ea.dto.system.LanguagesDTO;

public final class Languages implements Reflection {
	@Inject(resource = "com.pyt.service.implement.LanguagesSvc")
	private ILanguageSvc languagesSvc;
	private static Languages languages;
	private Log logger = Log.Log(this.getClass());
	
	private Languages() {}
	public synchronized static Languages instance() {
		if(languages == null) {
			languages = new Languages();
			languages.inject();
		}
		return languages;
	}
	
	public synchronized List<LanguagesDTO> getAll(LanguagesDTO dto)throws GenericServiceException{
		return languagesSvc.getAll(dto);
	}
	
	@Override
	public Log logger() {
		return logger;
	}
	
	
}
