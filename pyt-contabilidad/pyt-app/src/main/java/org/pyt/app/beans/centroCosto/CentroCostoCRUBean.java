package org.pyt.app.beans.centroCosto;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.ea.app.custom.PopupParametrizedControl;
import org.pyt.app.components.PopupGenBean;
import org.pyt.common.annotations.Inject;
import org.pyt.common.common.SelectList;
import org.pyt.common.constants.ParametroConstants;
import org.pyt.common.exceptions.CentroCostosException;
import org.pyt.common.exceptions.ParametroException;

import com.pyt.service.dto.CentroCostoDTO;
import com.pyt.service.dto.EmpresaDTO;
import com.pyt.service.dto.ParametroDTO;
import com.pyt.service.interfaces.ICentroCostosSvc;
import com.pyt.service.interfaces.IParametrosSvc;

import co.com.arquitectura.annotation.proccessor.FXMLFile;
import co.com.japl.ea.beans.abstracts.ABean;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;

/**
 * Se encarga de procesar la pantalla de creacion y actualizacion de una empresa
 * 
 * @author Alejandro Parra
 * @since 2018-05-22
 */
@FXMLFile(path = "view/centroCosto", file = "centroCosto.fxml")
public class CentroCostoCRUBean extends ABean<CentroCostoDTO> {
	@Inject(resource = "com.pyt.service.implement.CentroCostoSvc")
	private ICentroCostosSvc centroCostoSvc;
	@Inject(resource = "com.pyt.service.implement.ParametrosSvc")
	private IParametrosSvc parametroSvc;
	@FXML
	private Label codigo;
	@FXML
	private TextField nombre;
	@FXML
	private TextField descripcion;
	@FXML
	private TextField orden;
	@FXML
	private ChoiceBox<String> estado;
	@FXML
	private Label titulo;
	@FXML
	private BorderPane pane;
	@FXML
	private PopupParametrizedControl empresa;
	private List<ParametroDTO> listEstados;
	private final static String FIELD_NAME = "nombre";
	private final static String FIELD_VALUE = "valor";

	@FXML
	public void initialize() {
		NombreVentana = i18n().get("fxml.title.add.new.centercost");
		titulo.setText(NombreVentana);
		registro = new CentroCostoDTO();
		empresa.setPopupOpenAction(() -> popupEmpresa());
		empresa.setCleanValue(() -> registro.setEmpresa(null));

		try {
			listEstados = parametroSvc.getAllParametros(new ParametroDTO(),
					ParametroConstants.GRUPO_ESTADO_CENTRO_COSTO);
		} catch (ParametroException e) {
			error(e);
		}
		SelectList.put(estado, listEstados, FIELD_NAME);
		estado.getSelectionModel().selectFirst();
	}

	/**
	 * Pasa los campos de la pantalla el objeto dto
	 */
	private void load() {
		if (registro == null) {
			registro = new CentroCostoDTO();
		}
		registro.setCodigo(codigo.getText());
		registro.setNombre(nombre.getText());
		registro.setDescripcion(descripcion.getText());
		registro.setOrden(Integer.valueOf(orden.getText()));
		registro.setEstado((String) SelectList.get(estado, listEstados, FIELD_NAME).getCodigo());
	}

	private void loadFxml() {
		if (registro == null)
			return;
		codigo.setText(registro.getCodigo());
		nombre.setText(registro.getNombre());
		descripcion.setText(registro.getDescripcion());
		orden.setText(String.valueOf(registro.getOrden()));
		SelectList.put(estado, listEstados, FIELD_NAME);
		SelectList.selectItem(estado, listEstados, FIELD_NAME, registro.getEstado(), FIELD_NAME);
	}

	public void load(CentroCostoDTO dto) {
		if (dto != null && dto.getCodigo() != null) {
			registro = dto;
			loadFxml();
			titulo.setText(i18n().get("mensaje.editing.costcenter"));
		} else {
			error(i18n().get("err.costcenter.edit.invalid"));
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
		valid &= StringUtils.isNotBlank(registro.getEstado());
		valid &= (registro.getOrden()) != null;
		return valid;
	}

	public void add() {
		load();
		try {
			if (valid()) {
				if (StringUtils.isNotBlank(registro.getCodigo())) {
					centroCostoSvc.update(registro, getUsuario());
					notificarI18n("mensaje.costcenter.have.been.update.succefull");
					cancel();
				} else {
					centroCostoSvc.insert(registro, getUsuario());
					codigo.setText(registro.getCodigo());
					notificarI18n("mensaje.costcenter.have.been.insert.succesfull");
					cancel();
				}
			}
		} catch (CentroCostosException e) {
			error(e);
		}
	}

	public final void popupEmpresa() {
		try {
			controllerPopup(new PopupGenBean<EmpresaDTO>(EmpresaDTO.class)).load("#{CentroCostoCRUBean.empresa}");
		} catch (Exception e) {
			error(e);
		}
	}

	public final void setEmpresa(EmpresaDTO empresa) {
		if (empresa != null) {
			registro.setEmpresa(empresa);
			this.empresa.setText(empresa.getNombre());
		}
	}

	public void cancel() {
		getController(CentroCostoBean.class);
	}

}
