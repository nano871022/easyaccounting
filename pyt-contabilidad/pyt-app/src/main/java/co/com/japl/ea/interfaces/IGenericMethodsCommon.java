package co.com.japl.ea.interfaces;

import java.util.List;
import java.util.Map;

import org.pyt.common.abstracts.ADto;

public interface IGenericMethodsCommon<T extends ADto, S extends ADto, F extends ADto> extends ICommonMethods<S> {
	/**
	 * Se elamacenan los campos que fueron creados en el formulario
	 * 
	 * @return
	 */
	public Map<String, Object> getConfigFields();

	/**
	 * Se encarga de almacenar las lista para poner en los choicebox
	 * 
	 * @return
	 */
	public Map<String, Object> getConfigFieldTypeList();

	public List<F> getFields();

	public T getInstanceDTOUse();

}
