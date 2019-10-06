package co.com.japl.ea.interfaces;

import org.apache.commons.collections4.MultiValuedMap;
import org.apache.commons.collections4.map.MultiValueMap;
import org.pyt.common.abstracts.ADto;
import org.pyt.common.common.UtilControlFieldFX;

import com.pyt.service.dto.ParametroDTO;
import com.pyt.service.implement.GenericServiceSvc;
import com.sun.javafx.collections.MapAdapterChange;

import javafx.scene.Node;
import javafx.scene.layout.GridPane;
/**
 * Se encarga de generalizar las funciones que usaran  las diferentes implementaciones de campos genericos puestos sobre gridPanel
 * @author Alejo Parra
 *
 * @param <C>
 * @param <L>
 * @param <F>
 */
public interface IGenericFieldCommon<C extends ADto,L extends ADto, F extends ADto> extends IGenericCommons<L, F>{
	final UtilControlFieldFX genericFormsUtils = new UtilControlFieldFX();
	/**
	 * Objeto tipo {@link GridPane}
	 * @return {@link GridPane}
	 */
	public GridPane getGridPane();
	/**
	 * Instancia del objeto dto donde se obtienen los valores y se ponene los valores, dto maestro
	 * @return {@link ADto}
	 */
	public F getInstanceDto();
	/**
	 * Servicio de parametros, este debe ser el generico, con esto permite realizar consultas dinamicamente.
	 * @return {@link GenericServiceSvc} < {@link ParametroDTO} >
	 */
	public GenericServiceSvc<ParametroDTO> getParametersSvc();
	/**
	 * se encarga de retornar la lista de campos que se poenen en campos de tipo lista.
	 * @return {@link MultiValueMap} < {@link String} , {@link ADto} >
	 */
	public MultiValuedMap<String, C> getMapListToChoiceBox();
	/**
	 * Entrega un {@link MultiValuedMap} en el cual estaran los campos configruados
	 * @return {@link MultiValuedMap}
	 */
	public MultiValuedMap<String,Node> getMapFiles();
}
