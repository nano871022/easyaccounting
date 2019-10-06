package co.com.japl.ea.interfaces;

import java.util.List;

import org.pyt.common.abstracts.ADto;
/**
 * Este es la interface generica la cual se usara por los diferentes implementaciones
 * @author Alejo Parra
 *
 * @param <L>
 * @param <F>
 */
public interface IGenericCommons<L extends ADto,F extends ADto> extends INotificationMethods{
	/**
	 * Muestra la cantidad de columnas a poner en el objeto usado
	 * @return {@link Integer}
	 */
	public Integer getMaxColumns();
	/**
	 * Contiene la lista de campos genericos a procesar, que fueron configurados en la tablla indicada
	 * @return {@link List} < {@link ADto} >
	 */
	public List<L> getListGenericsFields();
	/**
	 * Contiene la clase del dto que se esta usando en los campos
	 * @return {@link Class} < {@link ADto} >
	 */
	public Class<F> getClazz();
}
