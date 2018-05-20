package org.pyt.app.beans;

import org.pyt.common.annotations.FXMLFile;
import org.pyt.common.annotations.Inject;
import org.pyt.common.common.Table;
import org.pyt.common.exceptions.EmpresasException;

import com.pyt.service.dto.EmpresaDTO;
import com.pyt.service.interfaces.IEmpresasSvc;

import javafx.fxml.FXML;
import javafx.scene.control.TableView;

/**
 * Bean encargado de crear las empresas
 * 
 * @author alejandro parra
 * @since 07/05/2018
 */
@FXMLFile(path = "/view", file = "listEmpresa.fxml")
public class EmpresaBean extends ABean<EmpresaDTO> {
	@Inject(resource = "com.pyt.service.implement.EmpresaSvc")
	private IEmpresasSvc empresaSvc;
	@FXML
	private TableView<EmpresaDTO> lsEmpresa;

	@FXML
	public void initialize() {
		try {
			filtro = new EmpresaDTO();
			registro = new EmpresaDTO();
			page = 1;
			rows = 10;
			lista = empresaSvc.getEmpresas(new EmpresaDTO(), page, rows);
			Table.put(lsEmpresa, lista);
		} catch (EmpresasException e) {
			System.err.println(e);
		}
	}

	/**
	 * Se encarga de obtener todos los registros
	 */
	public void search() {
		try {
			lista = empresaSvc.getEmpresas(filtro, page, rows);
			totalRows = empresaSvc.getTotalRows(filtro);
			Table.put(lsEmpresa, lista);
		} catch (EmpresasException e) {
			error(e);
		}
	}

	public void edit() {

	}

	public void save() {

	}

	public void cancel() {

	}

	public TableView<EmpresaDTO> getLsEmpresa() {
		return lsEmpresa;
	}

}
