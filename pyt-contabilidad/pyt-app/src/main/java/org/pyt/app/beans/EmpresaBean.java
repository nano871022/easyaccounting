package org.pyt.app.beans;

import java.util.Iterator;

import org.pyt.common.annotations.FXMLFile;
import org.pyt.common.annotations.Inject;
import org.pyt.common.common.ABean;
import org.pyt.common.common.Table;
import org.pyt.common.exceptions.EmpresasException;

import com.pyt.service.dto.EmpresaDTO;
import com.pyt.service.interfaces.IEmpresasSvc;

import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;

/**
 * Bean encargado de crear las empresas
 * 
 * @author alejandro parra
 * @since 07/05/2018
 */
@FXMLFile(path = "view", file = "listEmpresa.fxml")
public class EmpresaBean extends ABean<EmpresaDTO> {
	@Inject(resource = "com.pyt.service.implement.EmpresaSvc")
	private IEmpresasSvc empresaSvc;
	@FXML
	private javafx.scene.control.TableView<EmpresaDTO> lsEmpresa;
	@FXML
	private TextField codigo;
	@FXML
	private TextField nombre;
	@FXML
	private TextField email;
	@FXML
	private Button btnMod;
	@FXML
	private Button btnPrimer;
	@FXML
	private Button btnUltimo;
	@FXML
	private Button btnSiguiente;
	@FXML
	private Button btnAtras;
	@FXML
	private HBox paginas;
	@FXML
	private HBox nPages;
	private Integer currentPage;
	private Integer total;
	private Integer rows;

	@FXML
	public void initialize() {
		NombreVentana = "Lista Empresas";
		filtro = new EmpresaDTO();
		registro = new EmpresaDTO();
		page = 1;
		rows = 10;
		total = 5;
		currentPage = 1;
		search();
		loadPages();
	}

	public void clickTable() {
		btnMod.setVisible(isSelected());
	}

	/**
	 * Carga la informacion de los campos en el filtro
	 */
	private void loadFiltro() {
		filtro.setCodigo(codigo.getText());
		filtro.setNombre(codigo.getText());
		filtro.setCorreoElectronico(email.getText());
	}

	private void loadPages() {
		for (int i = 0; i < 5; i++) {
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
			});
			nPages.getChildren().add(page);
			btnAtras.setVisible(false);
			btnPrimer.setVisible(false);
		}
	}

	/**
	 * Se encarga de obtener todos los registros
	 */
	public void search() {
		try {
			loadFiltro();
			lista = empresaSvc.getEmpresas(filtro, page, rows);
			totalRows = empresaSvc.getTotalRows(filtro);
			Table.put(lsEmpresa, lista);
		} catch (EmpresasException e) {
			error(e);
		}
	}

	public void add() {
		getController(EmpresaCRUBean.class);
	}

	public void del() {
		try {
			registro = lsEmpresa.getSelectionModel().getSelectedItem();
			if (registro != null) {
				empresaSvc.delete(registro, userLogin);
			} else {
				notificar("No se ha seleccionado una empresa.");
			}
		} catch (EmpresasException e) {
			error(e);
		}
	}

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
	}

	public void first() {
		currentPage = 1;
		reloadPages();
		btnSiguiente.setVisible(true);
		btnUltimo.setVisible(true);
		btnAtras.setVisible(false);
		btnPrimer.setVisible(false);
	}

	public void before() {
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

	public void next() {
		currentPage++;
		reloadPages();
		if (currentPage == total) {
			btnSiguiente.setVisible(false);
			btnUltimo.setVisible(false);
		} else {
			btnSiguiente.setVisible(true);
			btnUltimo.setVisible(true);
		}
		btnAtras.setVisible(true);
		btnPrimer.setVisible(true);
		
	}

	public void last() {
		currentPage = total;
		reloadPages();
		btnSiguiente.setVisible(false);
		btnUltimo.setVisible(false);
		btnAtras.setVisible(true);
		btnPrimer.setVisible(true);
	}

	public void set() {
		registro = lsEmpresa.getSelectionModel().getSelectedItem();
		if (registro != null) {
			getController(EmpresaCRUBean.class).load(registro);
		} else {
			notificar("No se ha seleccionado una empresa.");
		}
	}

	public javafx.scene.control.TableView<EmpresaDTO> getLsEmpresa() {
		return lsEmpresa;
	}

	public Boolean isSelected() {
		return lsEmpresa.getSelectionModel().getSelectedItems().size() > 0;
	}
}
