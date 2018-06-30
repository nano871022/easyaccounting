package org.pyt.app.beans.concepto;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.pyt.common.annotations.FXMLFile;
import org.pyt.common.annotations.Inject;
import org.pyt.common.common.ABean;
import org.pyt.common.common.SelectList;
import org.pyt.common.exceptions.DocumentosException;
import org.pyt.common.exceptions.EmpresasException;
import org.pyt.common.exceptions.ParametroException;

import com.pyt.service.dto.ConceptoDTO;
import com.pyt.service.dto.EmpresaDTO;
import com.pyt.service.dto.ParametroDTO;
import com.pyt.service.interfaces.IDocumentosSvc;
import com.pyt.service.interfaces.IEmpresasSvc;
import com.pyt.service.interfaces.IParametrosSvc;

import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;

/**
 * Se encarga de procesar la pantalla de creacion y actualizacion de un concepto
 * 
 * @author Alejandro Parra
 * @since 2018-05-22
 */
@FXMLFile(path = "view/concepto", file = "concepto.fxml")
public class ConceptoCRUBean extends ABean<ConceptoDTO> {
	@Inject(resource = "com.pyt.service.implement.DocumentosSvc")
	private IDocumentosSvc documentoSvc;
	@Inject(resource = "com.pyt.service.implement.ParametrosSvc")
	private IParametrosSvc parametroSvc;
	@Inject(resource = "com.pyt.service.implement.EmpresaSvc")
	private IEmpresasSvc empresaSvc;
	@FXML
	private Label codigo;
	@FXML
	private TextField nombre;
	@FXML
	private TextField descripcion;
	@FXML
	private ChoiceBox<String> empresa;
	@FXML
	private ChoiceBox<String> estado;
	@FXML
	private Label titulo;
	@FXML
	private BorderPane pane;
	private List<ParametroDTO> listEstado;
	private List<EmpresaDTO> listEmpresa;

	@FXML
	public void initialize() {
		NombreVentana = "Agregando Nuevo Concepto";
		titulo.setText(NombreVentana);
		registro = new ConceptoDTO();
		ParametroDTO estados = new ParametroDTO();
		try {
			listEmpresa = empresaSvc.getAllEmpresas(new EmpresaDTO());
			listEstado = parametroSvc.getAllParametros(estados);
		} catch (ParametroException e) {
			error(e);
		} catch (EmpresasException e) {
			error(e);
		}
		SelectList.put(empresa, listEmpresa, "nombre");
		SelectList.put(estado, listEstado, "nombre");
		empresa.getSelectionModel().selectFirst();
		estado.getSelectionModel().selectFirst();
	}

	/**
	 * Pasa los campos de la pantalla el objeto dto
	 */
	private void load() {
		if (registro == null) {
			registro = new ConceptoDTO();
		}
		registro.setCodigo(codigo.getText());
		registro.setNombre(nombre.getText());
		registro.setDescripcion(descripcion.getText());
		registro.setEmpresa(SelectList.get(empresa, listEmpresa, "nombre"));
		registro.setEstado(SelectList.get(estado, listEstado, "nombre"));
	}

	private void loadFxml() {
		if (registro == null)
			return;
		codigo.setText(registro.getCodigo());
		nombre.setText(registro.getNombre());
		descripcion.setText(registro.getDescripcion());
	}

	public void load(ConceptoDTO dto) {
		if (dto != null && dto.getCodigo() != null) {
			registro = dto;
			loadFxml();
			titulo.setText("Modificando concepto");
		} else {
			error("EL concepto es invalido para editar.");
			cancel();
		}
	}

	/**
	 * Se encarga de validar los campos que se encuentren llenos
	 * 
	 * @return {@link Boolean}
	 */
	private Boolean valid() {
		Boolean valid = true;
		valid &= StringUtils.isNotBlank(registro.getNombre());
		valid &= StringUtils.isNotBlank(registro.getDescripcion());
		valid &= registro.getEstado() != null && StringUtils.isNotBlank(registro.getEstado().getCodigo());
		valid &= registro.getEmpresa() != null && StringUtils.isNotBlank(registro.getEmpresa().getCodigo());
		return valid;
	}

	public void add() {
		load();
		try {
			if (valid()) {
				if (StringUtils.isNotBlank(registro.getCodigo())) {
					documentoSvc.update(registro, userLogin);
					notificar("Se guardo el concepto correctamente.");
					cancel();
				} else {
					documentoSvc.insert(registro, userLogin);
					codigo.setText(registro.getCodigo());
					notificar("Se agrego el concepto correctamente.");
					cancel();
				}
			}
		} catch (DocumentosException e) {
			error(e);
		}
	}

	public void cancel() {
		getController(ConceptoBean.class);
	}

}
