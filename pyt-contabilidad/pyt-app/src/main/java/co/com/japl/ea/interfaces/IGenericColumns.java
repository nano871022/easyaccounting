package co.com.japl.ea.interfaces;

import org.pyt.common.abstracts.ADto;

import javafx.scene.control.TableView;
/**
 * La interface se encarga  de agregar las columnas
 * @author Alejo Parra
 *
 * @param <C>
 * @param <L>
 * @param <F>
 */
public interface IGenericColumns  <C extends ADto,L extends ADto,F extends ADto>extends IGenericCommons<L, F>{
	/**
	 * Se encarga de cargar las columnas dentro de la {@link TableView}
	 * @return
	 */
	public TableView<C> loadColumns();
	/**
	 * Se encarga de obtener la {@link TableView}
	 * @return {@link TableView}
	 */
	public TableView<C> getTableView();
}
