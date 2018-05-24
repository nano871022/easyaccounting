package org.pyt.app.beans;

import org.pyt.common.annotations.FXMLFile;
import org.pyt.common.annotations.Inject;
import org.pyt.common.common.ABean;
import org.pyt.common.common.Table;
import org.pyt.common.exceptions.EmpresasException;

import com.pyt.service.dto.EmpresaDTO;
import com.pyt.service.interfaces.IEmpresasSvc;

import javafx.fxml.FXML;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

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
	private TableView<EmpresaDTO> lsEmpresa;
	@FXML
	private TextField codigo;
	@FXML
	private TextField nombre;
	@FXML
	private TextField email;

	@FXML
	public void initialize() {
		NombreVentana = "Lista Empresas";
		filtro = new EmpresaDTO();
		registro = new EmpresaDTO();
		page = 1;
		rows = 10;
		search();
	}

	/**
	 * Carga la informacion de los campos en el filtro
	 */
	private void loadFiltro() {
		filtro.setCodigo(codigo.getText());
		filtro.setNombre(codigo.getText());
		filtro.setCorreoElectronico(email.getText());
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

	public void set() {
		registro = lsEmpresa.getSelectionModel().getSelectedItem();
		if (registro != null) {
			getController(EmpresaCRUBean.class).load(registro);
		} else {
			notificar("No se ha seleccionado una empresa.");
		}
	}

	public TableView<EmpresaDTO> getLsEmpresa() {
		return lsEmpresa;
	}

}
