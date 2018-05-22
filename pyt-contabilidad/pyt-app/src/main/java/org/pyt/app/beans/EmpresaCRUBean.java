package org.pyt.app.beans;

import org.pyt.common.annotations.FXMLFile;
import org.pyt.common.common.ABean;
import org.pyt.common.common.LoadAppFxml;
import org.pyt.common.exceptions.LoadAppFxmlException;

import com.pyt.service.dto.EmpresaDTO;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;

/**
 * Se encarga de procesar la pantalla de creacion y actualizacion de una empresa
 * 
 * @author Alejandro Parra
 * @since 2018-05-22
 */
@FXMLFile(path = "/view", file = "empresa.fxml")
public class EmpresaCRUBean extends ABean<EmpresaDTO> {
	@FXML
	private EmpresaDTO registro;
	@FXML
	private TextField codigo;
	@FXML
	public void initialize() {
		registro = new EmpresaDTO();
	}

	public void add() {
		System.out.println("Agregando/Editando");
	}

	public void cancel() {
		System.out.println("Cancelando");
		try {
			LoadAppFxml.loadBeanFxml(null, EmpresaBean.class, "Lista Empresas");
		} catch (LoadAppFxmlException e) {
			System.err.println(e);
		}
	}

}
