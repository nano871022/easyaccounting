package org.pyt.app.beans.empresa;

import java.util.ArrayList;
import java.util.List;

import org.pyt.app.components.ConfirmPopupBean;
import org.pyt.app.components.DataTableFXML;
import org.pyt.common.annotations.Inject;
import org.pyt.common.exceptions.EmpresasException;

import com.pyt.service.dto.EmpresaDTO;
import com.pyt.service.interfaces.IEmpresasSvc;

import co.com.arquitectura.annotation.proccessor.FXMLFile;
import co.com.japl.ea.beans.ABean;
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
@FXMLFile(path = "view", file = "empresa/listEmpresa.fxml")
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
	private Button btnDel;
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
					lista = empresaSvc.getEmpresas(filter, page - 1, rows);
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
				if (!codigo.getText().isEmpty()) {
					filtro.setCodigo(codigo.getText());
				}
				if (!nombre.getText().isEmpty()) {
					filtro.setNombre(nombre.getText());
				}
				if (!email.getText().isEmpty()) {
					filtro.setCorreoElectronico(email.getText());
				}
				return filtro;
			}
		};
	}

	public void clickTable() {
		btnMod.setVisible(isSelected());
		btnDel.setVisible(isSelected());
	}

	public void add() {
		getController(EmpresaCRUBean.class);
	}

	public void search() {
		dt.search();
	}

	public void del() {
		try {
			controllerPopup(ConfirmPopupBean.class).load("#{EmpresaBean.delete}",
					"¿Desea eliminar los registros seleccionados?");
		} catch (Exception e) {
			error(e);
		}
	}

	public void setDelete(Boolean valid) {
		try {
			if (!valid)
				return;
			registro = dt.getSelectedRow();
			if (registro != null) {
				empresaSvc.delete(registro, userLogin);
				notificar("Se ha eliminaro la empresa.");
				dt.search();
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
