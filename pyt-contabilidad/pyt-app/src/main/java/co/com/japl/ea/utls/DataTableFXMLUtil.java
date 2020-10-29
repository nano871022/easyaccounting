package co.com.japl.ea.utls;

import java.math.BigDecimal;
import java.util.List;
import java.util.function.Consumer;

import org.pyt.common.abstracts.ADto;
import org.pyt.common.common.Log;
import org.pyt.common.common.Table;
import org.pyt.common.exceptions.ReflectionException;
import org.pyt.common.exceptions.validates.ValidateValueException;
import org.pyt.common.validates.ValidateValues;

import javafx.scene.control.Label;
import javafx.scene.control.Pagination;
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
	private Integer page;
	private Integer total;
	private Integer rows;
	private List<S> list;
	private javafx.scene.control.TableView<S> table;
	private ValidateValues validate = new ValidateValues();
	private Log logger = Log.Log(this.getClass());
	private Boolean firstSearch;
	private Pagination pagination;

	Consumer<List<S>> predicateSelected;

	public DataTableFXMLUtil() {
		currentPage = 1;
		rows = 10;
		total = 5;
		page = 1;
		firstSearch = true;
	}

	public DataTableFXMLUtil(HBox paginas, javafx.scene.control.TableView<S> table) {
		this();
		this.paginas = paginas;
		this.table = table;
		search();
		loadPagination();
	}

	public DataTableFXMLUtil(HBox paginas, javafx.scene.control.TableView<S> table, Boolean firstSearch) {
		this();
		this.firstSearch = firstSearch;
		this.paginas = paginas;
		this.table = table;
		search();
		loadPagination();
	}

	private void loadPagination() {
		logger.info("Cargar Paginacion");
		float div = (this.total / this.rows) + 0.6f;
		if (div == 0.6f) {
			div = 0;
		}
		Integer pages = Math.round(div);
		if (pagination != null)
			if ((pagination == null || (pages > 0 && pagination.getPageCount() != pages))) {
				this.pagination = new Pagination();
				paginas.getChildren().clear();
				paginas.getChildren().add(this.pagination);
				logger.info("Cantidad Paginas: " + total + "/" + rows + "=" + pages + " (" + div + ")");
				this.pagination.setPageCount(pages);
				this.pagination.setPageFactory(this::createPagina);
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
		logger.info("Realizando busqueda");
		if (table.isVisible() && firstSearch) {
			T filter = getFilter();
			Integer init = currentPage;
			if (init > 1) {
				init = (currentPage - 1) * rows;
			} else if (init == 1) {
				init = 0;
			}
			list = getList(filter, init, rows);
			total = getTotalRows(filter);
			Table.put(table, list);
			loadPagination();
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

	public Label createPagina(Integer i) {
		logger.info("Pagina: " + i);
		currentPage = i + 1;
		search();
		return new Label("");
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

	public final Integer getPage() {
		return page;
	}
}
