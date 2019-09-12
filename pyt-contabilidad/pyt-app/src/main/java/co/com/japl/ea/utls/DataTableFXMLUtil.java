package co.com.japl.ea.utls;

import java.math.BigDecimal;
import java.util.Iterator;
import java.util.List;

import org.pyt.common.abstracts.ADto;
import org.pyt.common.common.Log;
import org.pyt.common.common.Table;
import org.pyt.common.exceptions.ReflectionException;
import org.pyt.common.exceptions.validates.ValidateValueException;
import org.pyt.common.validates.ValidateValues;

import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;

/**
 * Se encarga de controlar los diferentes aspectos de la tabla como mostrar la
 * informacion necesaria, se desarrolla un sistema de control
 * 
 * @author Alejandro Parra
 * @since 18/06/2018
 */
public abstract class DataTableFXMLUtil<S extends Object, T extends ADto> extends Table {
	private Button btnPrimer;
	private Button btnUltimo;
	private Button btnSiguiente;
	private Button btnAtras;
	private HBox paginas;
	private HBox nPages;
	private Integer currentPage;
	private Integer page;
	private Integer total;
	private Integer rows;
	private Long cantidad;
	private List<S> list;
	private javafx.scene.control.TableView<S> table;
	private ValidateValues validate = new ValidateValues();
	private Log logger = Log.Log(this.getClass());
	private Boolean firstSearch;

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
		loadPaginator();
		search();
	}

	public DataTableFXMLUtil(HBox paginas, javafx.scene.control.TableView<S> table, Boolean firstSearch) {
		this();
		this.firstSearch = firstSearch;
		this.paginas = paginas;
		this.table = table;
		loadPaginator();
		search();
	}

	private final void loadPaginator() {
		if (paginas != null) {
			paginas.getChildren().add(0, btnPrimer = new Button("<<"));
			paginas.getChildren().add(1, btnAtras = new Button("<"));
			paginas.getChildren().add(2, nPages = new HBox());
			paginas.getChildren().add(paginas.getChildren().size(), btnSiguiente = new Button(">"));
			paginas.getChildren().add(paginas.getChildren().size(), btnUltimo = new Button(">>"));
			btnUltimo.setMinWidth(40);
			btnPrimer.setMinWidth(40);
			btnPrimer.onActionProperty().set(e -> first());
			btnAtras.onActionProperty().set(e -> before());
			btnSiguiente.onActionProperty().set(e -> next());
			btnUltimo.onActionProperty().set(e -> last());
		}
	}

	/**
	 * Se encarga de realilzar la busqueda de la lista de registros y de la cantidad
	 * de registros encontrados
	 */
	public final void search() {
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
			loadPages();
		} else if (table.isVisible()) {
			firstSearch = true;
		}
	}

	/**
	 * Se encargad e recargar los hijos de la paginacion
	 */
	private final void reloadPages() {
		Iterator<Node> ite = nPages.getChildren().iterator();
		while (ite.hasNext()) {
			Node node = ite.next();
			if (((Label) node).getText().contentEquals(String.valueOf(currentPage))) {
				((Label) node).setStyle("-fx-text-fill:blue;-fx-underline:true;");
			} else {
				((Label) node).setStyle("-fx-text-fill:black;-fx-underline:false;");
			}
		}
		search();
	}

	/**
	 * Se encargaa de cambiar de pagina a la primera
	 */
	public final void first() {
		currentPage = 1;
		reloadPages();
		btnSiguiente.setVisible(true);
		btnUltimo.setVisible(true);
		btnAtras.setVisible(false);
		btnPrimer.setVisible(false);
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
	 * Se encarga de cambiar a la pagina anteriore
	 */
	public final void before() {
		currentPage--;
		reloadPages();
		if (currentPage == 1) {
			btnAtras.setVisible(false);
			btnPrimer.setVisible(false);
		} else {
			btnAtras.setVisible(true);
			btnPrimer.setVisible(true);
		}
		btnSiguiente.setVisible(true);
		btnUltimo.setVisible(true);
	}

	/**
	 * Se encarga dde cambiar a la siguiente pagina
	 */
	public final void next() {
		currentPage++;
		reloadPages();
		if (currentPage == cantidad.intValue()) {
			btnSiguiente.setVisible(false);
			btnUltimo.setVisible(false);
		} else {
			btnSiguiente.setVisible(true);
			btnUltimo.setVisible(true);
		}
		btnAtras.setVisible(true);
		btnPrimer.setVisible(true);

	}

	/**
	 * Se encarga de cambiar a la ultima pagina encontrada
	 */
	public final void last() {
		currentPage = cantidad.intValue();
		reloadPages();
		btnSiguiente.setVisible(false);
		btnUltimo.setVisible(false);
		btnAtras.setVisible(true);
		btnPrimer.setVisible(true);
	}

	/**
	 * Se encarga de cargar el numero de paginas
	 */
	public final void loadPages() {
		nPages.getChildren().clear();
		Double d = (double) 0.9;
		if (total > rows) {
			d = (double) (total / rows);
		}
		cantidad = (long) Math.round(d + 0.5);
		if (cantidad > 1) {
			for (int i = 0; i < cantidad; i++) {
				Label page = new Label(String.valueOf(i + 1));
				page.setPadding(new Insets(5));
				page.setStyle("-fx-cursor:hand");
				if (i == 0) {
					page.setStyle("-fx-text-fill:blue;-fx-underline:true;");
				}
				page.addEventHandler(MouseEvent.MOUSE_CLICKED, (e) -> {
					Iterator<Node> ite = nPages.getChildren().iterator();
					while (ite.hasNext()) {
						Node node = ite.next();
						((Label) node).setStyle("-fx-text-fill:black;-fx-underline:false;");
					}
					page.setStyle("-fx-text-fill:blue;-fx-underline:true;");
					currentPage = Integer.valueOf(page.getText());
					search();
				});
				nPages.getChildren().add(page);
				btnAtras.setVisible(false);
				btnPrimer.setVisible(false);
			}
		} else {
			btnAtras.setVisible(false);
			btnPrimer.setVisible(false);
			btnSiguiente.setVisible(false);
			btnUltimo.setVisible(false);
		}
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
