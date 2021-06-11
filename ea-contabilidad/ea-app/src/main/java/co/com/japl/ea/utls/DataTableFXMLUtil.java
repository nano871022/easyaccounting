package co.com.japl.ea.utls;

import java.math.BigDecimal;
import java.util.List;
import java.util.function.Consumer;

import org.pyt.common.common.Log;
import org.pyt.common.common.Table;

import co.com.japl.ea.common.abstracts.ADto;
import co.com.japl.ea.common.validates.ValidateValues;
import co.com.japl.ea.exceptions.ReflectionException;
import co.com.japl.ea.exceptions.validates.ValidateValueException;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.scene.control.Pagination;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;

/**
 * ; * Se encarga de controlar los diferentes aspectos de la tabla como mostrar
 * la informacion necesaria, se desarrolla un sistema de control
 * 
 * @author Alejandro Parra
 * @since 18/06/2018
 */
public abstract class DataTableFXMLUtil<S extends Object, T extends ADto> extends Table {
	private HBox paginas;
	private Integer currentPage;
	private Integer pages;
	private Integer total;
	private Integer rows;
	private List<S> list;
	private javafx.scene.control.TableView<S> table;
	private ValidateValues validate = new ValidateValues();
	private Log logger = Log.Log(this.getClass());
	private Boolean firstSearch;
	private Pagination pagination;
	private BooleanProperty visiblePaginator;

	Consumer<List<S>> predicateSelected;

	private DataTableFXMLUtil() {
		currentPage = 0;
		rows = 10;
		total = 5;
		pages = 1;
		firstSearch = true;
		visiblePaginator = new SimpleBooleanProperty(false);
	}

	public DataTableFXMLUtil(HBox paginas, javafx.scene.control.TableView<S> table) {
		this();
		this.paginas = paginas;
		this.table = table;
		loadPaginator();
		search();
	}

	public DataTableFXMLUtil(HBox paginas, javafx.scene.control.TableView<S> table, Boolean firstSearch) {
		this();
		this.firstSearch = firstSearch;
		this.paginas = paginas;
		this.table = table;
		search();
		loadPaginator();
	}

	private final void loadPaginator() {
		if (paginas != null) {
			pagination = new Pagination();
			pagination.setMaxPageIndicatorCount(20);
			pagination.visibleProperty().bind(visiblePaginator);
			pagination.setPageFactory((init) -> {
				currentPage = init;
				search();
				return new GridPane();
			});
			paginas.getChildren().add(pagination);
		}
	}

	public final void selectRow(Consumer<List<S>> consumer) {
		predicateSelected = consumer;
		table.selectionModelProperty()
				.addListener((cahnge, oldva, newval) -> predicateSelected.accept(getSelectedRows()));
	}

	/**
	 * Se encarga de realilzar la busqueda de la lista de registros y de la cantidad
	 * de registros encontrados
	 */
	public final void search() {
		if (table.isVisible() && firstSearch) {
			T filter = getFilter();
			Integer init = currentPage;
			if (init >= 1) {
				init = currentPage * rows;
			}
			list = getList(filter, init, rows);
			total = getTotalRows(filter);
			pages = (int) Math.round((Double.valueOf(total) / Double.valueOf(rows)) + 0.4);
			if (pagination.getPageCount() != pages) {
				pagination.setPageCount(pages);
				visiblePaginator.setValue(pages > 1);
			}
			Table.put(table, list);
		} else if (table.isVisible()) {
			firstSearch = true;
		}
	}

	public final BigDecimal sumatoria(List<T> list, String nombreCampo) {
		BigDecimal cant = new BigDecimal(0);
		if (list != null && list.size() > 0) {
			for (T dto : list) {
				try {
					if (dto.get(nombreCampo) == null)
						continue;
					cant = cant.add(validate.cast(dto.get(nombreCampo), BigDecimal.class));
				} catch (ReflectionException | ValidateValueException e) {
					logger.logger(e);
				}
			}
		}
		return cant;
	}

	/**
	 * Se encarga de contener la consulta a realizar para obtner los datos de la bd
	 * 
	 * @return {@link List} from extend Object
	 */
	public abstract List<S> getList(T filter, Integer page, Integer rows);

	/**
	 * Se encarga de contener la consulta a realziar para obtener la cantidad de
	 * registros
	 * 
	 * @return {@link Integer}
	 */
	public abstract Integer getTotalRows(T filter);

	/**
	 * Se encarga de configurar el filtro para retornar el objeto para realizar la
	 * consulta correctamente,
	 * 
	 * @return extends {@link ADto}
	 */
	public abstract T getFilter();

	public final Boolean isSelected() {
		return table.getSelectionModel().getSelectedItems().size() > 0;
	}

	public final List<S> getSelectedRows() {
		return table.getSelectionModel().getSelectedItems();
	}

	public final Boolean isMultiSelected() {
		return table.getSelectionModel().getSelectedItems().size() > 1;
	}

	public final S getSelectedRow() {
		return table.getSelectionModel().getSelectedItem();
	}

	public final Integer getCurrentPage() {
		return currentPage;
	}

	public final Integer getTotal() {
		return total;
	}

	public final Integer getRows() {
		return rows;
	}

	public final List<S> getList() {
		return list;
	}

	public final Integer getPages() {
		return pages;
	}
}
