package co.com.japl.ea.dto.abstracts;

import java.util.List;

import org.pyt.common.annotations.Inject;
import org.pyt.common.common.Log;

import co.com.japl.ea.common.reflection.Reflection;
import co.com.japl.ea.dto.interfaces.ILanguageSvc;
import co.com.japl.ea.dto.system.LanguagesDTO;
import co.com.japl.ea.exceptions.GenericServiceException;

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
