package co.com.japl.ea.interfaces;

import org.pyt.common.abstracts.ADto;

import javafx.scene.layout.GridPane;
/**
 * Se encarga de configurar los filtros, 
 * @author Alejo Parra
 *
 */
public interface IGenericFilters <C extends ADto,L extends ADto,F extends ADto>extends IGenericFieldColumns<C, L, F>{
	/**
	 * Se encarga de configurar los campos que se mostraran en los filtros y fueron configurados como campos genericos, esto se pone el {@link GridPane}
	 * @return {@link GridPane}
	 */
	public GridPane loadFilters();
}
