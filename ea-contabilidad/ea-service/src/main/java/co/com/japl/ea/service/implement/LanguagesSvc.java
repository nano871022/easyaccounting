package co.com.japl.ea.service.implement;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.pyt.common.annotations.Inject;
import org.pyt.common.common.DtoUtils;
import org.pyt.common.common.ListUtils;

import co.com.japl.ea.query.interfaces.IQuerySvc;
import co.com.arquitectura.annotation.proccessor.Implements;
import co.com.arquitectura.annotation.proccessor.Services.Type;
import co.com.arquitectura.annotation.proccessor.Services.kind;
import co.com.arquitectura.annotation.proccessor.Services.scope;
import co.com.japl.ea.dto.abstracts.Services;
import co.com.japl.ea.dto.interfaces.ILanguageSvc;
import co.com.japl.ea.dto.system.LanguagesDTO;
import co.com.japl.ea.dto.system.UsuarioDTO;
import co.com.japl.ea.exceptions.GenericServiceException;
import co.com.japl.ea.exceptions.QueryException;
@Implements
public class LanguagesSvc extends Services implements ILanguageSvc {
	@Inject(resource = "co.com.japl.ea.query.implement.QuerySvc")
	private IQuerySvc querySvc;
	@Override
	public List<LanguagesDTO> getAll(LanguagesDTO dto) throws GenericServiceException{
		try {
			return querySvc.gets(dto);
		} catch (QueryException e) {
			throw new GenericServiceException("Error en obtener registros.",e);
		}
	}

	@co.com.arquitectura.annotation.proccessor.Services(alcance = scope.EJB, alias = "Ingreso Idiomas", descripcion = "Ingreso de servicios de idiomas", tipo = kind.PUBLIC, type = Type.CREATE)
	public LanguagesDTO insertService(LanguagesDTO dto, UsuarioDTO usuario) throws GenericServiceException {
		if (DtoUtils.isBlank(dto)) {
			throw new GenericServiceException(i18n().get("err.languages.is.empty"));
		}
		if (DtoUtils.isBlank(usuario)) {
			throw new GenericServiceException(i18n().get("err.users.is.empty"));
		}
		var list = Arrays.asList("S", "N", "1", "0");
		List<String> foundState = list.stream().filter(row -> row.contentEquals(dto.getState()))
				.collect(Collectors.toList());
		if (foundState.size() == 0) {
			throw new GenericServiceException(i18n().get("err.state.is.invalid"));
		} else if ("S".contentEquals(foundState.get(0))) {
			dto.setState("1");
		} else if ("N".contentEquals(foundState.get(0))) {
			dto.setState("0");
		}
		try {
			var found = querySvc.gets(dto);
			if (ListUtils.isBlank(found) && querySvc.insert(dto, usuario)) {
				return dto;
			} else {
				throw new GenericServiceException(i18n().get("err.languages.was.saved"));
			}
		} catch (QueryException e) {
			throw new GenericServiceException(i18n().get("err.languages.wasnt.inserted"), e);
		}
	}
}
