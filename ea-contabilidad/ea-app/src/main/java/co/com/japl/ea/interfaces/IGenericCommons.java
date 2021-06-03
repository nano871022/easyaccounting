package co.com.japl.ea.interfaces;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.pyt.common.common.UtilControlFieldFX;
import org.pyt.common.constants.ParametroConstants;

import co.com.japl.ea.common.abstracts.ADto;
import co.com.japl.ea.common.validates.ValidateValues;
import co.com.japl.ea.dto.dto.ParametroDTO;
import co.com.japl.ea.dto.interfaces.IParametrosSvc;
import co.com.japl.ea.exceptions.ParametroException;
import co.com.japl.ea.service.implement.GenericServiceSvc;

/**
 * Este es la interface generica la cual se usara por los diferentes
 * implementaciones
 * 
 * @author Alejo Parra
 *
 * @param <L>
 * @param <F>
 */
public interface IGenericCommons<L extends ADto, F extends ADto> extends INotificationMethods {
	final UtilControlFieldFX genericFormsUtils = new UtilControlFieldFX();
	final ValidateValues validateValuesUtils = new ValidateValues();

	enum TypeGeneric {
		FIELD, COLUMN, FILTER
	}

	/**
	 * Muestra la cantidad de columnas a poner en el objeto usado
	 * 
	 * @return {@link Integer}
	 */
	public Integer getMaxColumns(TypeGeneric typeGeneric);

	/**
	 * Contiene la lista de campos genericos a procesar, que fueron configurados en
	 * la tablla indicada
	 * 
	 * @return {@link List} < {@link ADto} >
	 */
	public List<L> getListGenericsFields(TypeGeneric typeGeneric);

	/**
	 * Contiene la clase del dto que se esta usando en los campos
	 * 
	 * @return {@link Class} < {@link ADto} >
	 */
	public Class<F> getClazz();

	/**
	 * Servicio de parametros, este debe ser el generico, con esto permite realizar
	 * consultas dinamicamente.
	 * 
	 * @return {@link GenericServiceSvc} < {@link ParametroDTO} >
	 */
	public IParametrosSvc getParametersSvc();

	default ParametroDTO getParametroFromFindGroup(ParametroDTO parametroDto, String nameGroup)
			throws ParametroException {
		if (StringUtils.isBlank(parametroDto.getGrupo())) {
			ParametroDTO parametroDTO = new ParametroDTO();
			parametroDTO.setNombre(nameGroup);
			parametroDTO.setGrupo(ParametroConstants.GRUPO_PRINCIPAL);
			parametroDTO.setEstado(ParametroConstants.COD_ESTADO_PARAMETRO_ACTIVO_STR);
			var listParams = getParametersSvc().getAllParametros(parametroDTO);
			if (listParams.size() > 1) {
				throw new ParametroException(
						i18n().valueBundle("err.msn.param.not.unique", parametroDto.getGrupo()).get());
			}
			if (listParams.size() == 0) {
				throw new ParametroException(
						i18n().valueBundle("err.msn.param.not.exist", parametroDto.getGrupo()).get());
			}
			parametroDto.setGrupo(listParams.get(0).getCodigo());
		}
		return parametroDto;
	}

}
