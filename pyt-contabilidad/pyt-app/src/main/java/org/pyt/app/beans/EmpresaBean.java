package org.pyt.app.beans;

import java.util.ArrayList;
import java.util.List;

import org.pyt.app.components.DataTableFXML;
import org.pyt.common.annotations.FXMLFile;
import org.pyt.common.annotations.Inject;
import org.pyt.common.common.ABean;
import org.pyt.common.exceptions.EmpresasException;

import com.pyt.service.dto.EmpresaDTO;
import com.pyt.service.interfaces.IEmpresasSvc;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
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
	private javafx.scene.control.TableView<EmpresaDTO> tabla;
	@FXML
	private TextField codigo;
	@FXML
	private TextField nombre;
	@FXML
	private TextField email;
	@FXML
	private Button btnMod;
	@FXML
	private HBox paginador;
	private DataTableFXML<EmpresaDTO, EmpresaDTO> dt;

	@FXML
	public void initialize() {
		NombreVentana = "Lista Empresas";
		registro = new EmpresaDTO();
		lazy();
	}

	/**
	 * encargada de crear el objeto que va controlar la tabla
	 */
	public void lazy() {
		dt = new DataTableFXML<EmpresaDTO, EmpresaDTO>(paginador, tabla) {
			@Override
			public List<EmpresaDTO> getList(EmpresaDTO filter, Integer page, Integer rows) {
				List<EmpresaDTO> lista = new ArrayList<EmpresaDTO>();
				try {
					lista = empresaSvc.getEmpresas(filter, page, rows);
				} catch (EmpresasException e) {
					error(e);
				}
				return lista;
			}

			@Override
			public Integer getTotalRows(EmpresaDTO filter) {
				Integer count = 0;
				try {
					count = empresaSvc.getTotalRows(filter);
				} catch (EmpresasException e) {
					error(e);
				}
				return count;
			}

			@Override
			public EmpresaDTO getFilter() {
				EmpresaDTO filtro = new EmpresaDTO();
				filtro.setCodigo(codigo.getText());
				filtro.setNombre(codigo.getText());
				filtro.setCorreoElectronico(email.getText());
				return filtro;
			}
		};
	}

	public void clickTable() {
		btnMod.setVisible(isSelected());
	}

	public void add() {
		getController(EmpresaCRUBean.class);
	}

	public void search() {
		dt.search();
	}

	public void del() {
		try {
			registro = dt.getSelectedRow();
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
		registro = dt.getSelectedRow();
		if (registro != null) {
			getController(EmpresaCRUBean.class).load(registro);
		} else {
			notificar("No se ha seleccionado una empresa.");
		}
	}

	public Boolean isSelected() {
		return dt.isSelected();
	}

	public DataTableFXML<EmpresaDTO, EmpresaDTO> getDt() {
		return dt;
	}
}
