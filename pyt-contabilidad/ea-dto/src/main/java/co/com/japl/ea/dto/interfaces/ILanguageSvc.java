package co.com.japl.ea.dto.interfaces;

import java.util.List;

import co.com.japl.ea.dto.system.LanguagesDTO;
import co.com.japl.ea.exceptions.GenericServiceException;

public interface ILanguageSvc {
	public List<LanguagesDTO> getAll(LanguagesDTO dto) throws GenericServiceException;
}
