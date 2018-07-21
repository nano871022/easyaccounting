package org.pyt.app.beans.parametros;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.pyt.common.annotations.FXMLFile;
import org.pyt.common.annotations.Inject;
import org.pyt.common.common.ABean;
import org.pyt.common.common.SelectList;
import org.pyt.common.common.ValidateValues;
import org.pyt.common.constants.ParametroConstants;
import org.pyt.common.exceptions.ParametroException;
import org.pyt.common.exceptions.ValidateValueException;

import com.pyt.service.dto.ParametroDTO;
import com.pyt.service.dto.ParametroGrupoDTO;
import com.pyt.service.interfaces.IParametrosSvc;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

/**
 * Se encarga de controlar la pantalla de parametros cru
 * 
 * @author Alejandro Parra
 * @since 22-06-2018
 */
@FXMLFile(path = "view/parametros", file = "parametro.fxml", nombreVentana = "Parametro CRUD")
public class ParametrosCRUBean extends ABean<ParametroDTO> {
	@Inject(resource = "com.pyt.service.implement.ParametrosSvc")
	private IParametrosSvc parametroSvc;
	@FXML
	private Label codigo;
	@FXML
	private Label lOrden;
	@FXML
	private TextField nombre;
	@FXML
	private TextField descripcion;
	@FXML
	private TextField grupo;
	@FXML
	private TextField valor;
	@FXML
	private TextField valor2;
	@FXML
	private TextField orden;
	@FXML
	private ChoiceBox<String> cGrupo;
	@FXML
	private Label lGrupo;
	@FXML
	private ChoiceBox<String> estado;
	private ParametroDTO registro;
	private ParametroGrupoDTO parametroGrupo;
	@FXML
	private Button insert;
	@FXML
	private Button update;
	@FXML
	private Button delete;
	private ValidateValues validate;

	@FXML
	public void initialize() {
		validate = new ValidateValues();
		registro = new ParametroDTO();
		SelectList.put(estado, ParametroConstants.mapa_estados_parametros);
		SelectList.put(cGrupo, ParametroConstants.MAPA_GRUPOS);
		estado.getSelectionModel().selectFirst();
		cGrupo.getSelectionModel().selectFirst();
		lGrupo.setVisible(false);
		cGrupo.setVisible(false);
		insert.setVisible(false);
		update.setVisible(false);
		delete.setVisible(false);
	}

	/**
	 * Se encarga de cargar el objeto registro.
	 */
	private void load() {
		registro.setNombre(nombre.getText());
		registro.setDescripcion(descripcion.getText());
		registro.setGrupo(grupo.getText());
		registro.setValor(valor.getText());
		registro.setValor2(valor2.getText());
		try {
			if (validate.isCast(orden.getText(), Long.class)) {
				registro.setOrden(validate.cast(orden.getText(), Long.class));
			}
		} catch (ValidateValueException e) {
			error(e);
		}
		registro.setEstado((String) ParametroConstants.mapa_estados_parametros.get(estado.getValue()));
		if (StringUtils.isNotBlank(cGrupo.getValue())) {
			parametroGrupo.setGrupo((String) SelectList.get(cGrupo, ParametroConstants.MAPA_GRUPOS));
		}
		insert.setVisible(true);
	}

	/**
	 * Se encarga de cargar el parametro para poder realizar crud sobre el registro
	 * 
	 * @param dto
	 *            {@link ParametroDTO}
	 */
	public void load(ParametroDTO dto) {
		if (StringUtils.isBlank(dto.getGrupo()) && (dto == null || StringUtils.isBlank(dto.getCodigo()))) {
			registro = new ParametroDTO();
			insert.setVisible(true);
		}
		lGrupo.setVisible(false);
		cGrupo.setVisible(false);

		if (dto != null && (StringUtils.isBlank(dto.getCodigo()) && StringUtils.isNotBlank(dto.getGrupo())
				&& dto.getGrupo().contains("*"))) {
			registro = dto;
			lGrupo.setVisible(true);
			cGrupo.setVisible(true);
			insert.setVisible(true);
		}

		if (dto != null && StringUtils.isNotBlank(dto.getCodigo()) && !dto.getGrupo().contains("*")) {
			registro = dto;
			assign();
			update.setVisible(true);
			delete.setVisible(true);
		} else {
			if (StringUtils.isNotBlank(dto.getCodigo())) {
				parametroGrupo = new ParametroGrupoDTO();
				parametroGrupo.setParametro(dto.getCodigo());
				try {
					List<ParametroGrupoDTO> list = parametroSvc.getParametroGrupo(parametroGrupo);
					if (list != null && list.size() > 0) {
						parametroGrupo = list.get(0);
					}
					if (list != null && list.size() > 1) {
						error("Se encontraron varios registros para el parametro " + dto.getCodigo());
					}
				} catch (ParametroException e) {
					error(e);
				}
			}else {
				parametroGrupo = new ParametroGrupoDTO();
			}
			registro = dto;
			grupo.setEditable(false);
			lGrupo.setVisible(true);
			cGrupo.setVisible(true);
			if (registro.getGrupo().equalsIgnoreCase("*")) {
				orden.setVisible(false);
				lOrden.setVisible(false);
			} else {
				try {
					if (StringUtils.isBlank(registro.getCodigo())) {
						registro.setOrden(parametroSvc.totalCount(registro).longValue() + 1);
					}
				} catch (ParametroException e) {
				}
			}
			assign();
			if (StringUtils.isNotBlank(registro.getCodigo())) {
				update.setVisible(true);
				delete.setVisible(true);
			} else {
				insert.setVisible(true);
			}
		}
	}

	private void assign() {
		codigo.setText(registro.getCodigo());
		nombre.setText(registro.getNombre());
		descripcion.setText(registro.getDescripcion());
		grupo.setText(registro.getGrupo());
		valor.setText(registro.getValor());
		valor2.setText(registro.getValor2());
		if (registro.getOrden() != null) {
			orden.setText(registro.getOrden().toString());
		}
		SelectList.selectItem(estado, ParametroConstants.mapa_estados_parametros, registro.getEstado());
		if (parametroGrupo != null && StringUtils.isNotBlank(parametroGrupo.getCodigo())) {
			if (cGrupo.isVisible() && StringUtils.isNotBlank(parametroGrupo.getGrupo())) {
				SelectList.selectItem(cGrupo, ParametroConstants.MAPA_GRUPOS, parametroGrupo.getGrupo());
			}
		}
		grupo.setEditable(false);
	}

	/**
	 * Se encaga de validar el registro
	 * 
	 * @return
	 */
	private Boolean valid() {
		Boolean valid = true;
		valid &= StringUtils.isNotBlank(registro.getNombre());
		valid &= StringUtils.isNotBlank(registro.getDescripcion());
		valid &= StringUtils.isNotBlank(registro.getGrupo());
		valid &= StringUtils.isNotBlank(registro.getEstado());
		if (!registro.getGrupo().contentEquals("*")) {
			valid &= StringUtils.isNotBlank(registro.getValor());
			valid &= registro.getOrden() != null;
		}
		return valid;
	}

	/**
	 * Se encaraga de crear el registro
	 */
	public void createBtn() {
		load();
		try {
			if (valid()) {
				registro = parametroSvc.insert(registro, userLogin);
				if (StringUtils.isNotBlank(parametroGrupo.getGrupo())) {
					parametroGrupo.setParametro(registro.getCodigo());
					parametroGrupo = parametroSvc.insert(parametroGrupo, userLogin);
				}
				codigo.setText(registro.getCodigo());
				notificar("Se ha creado el parametro correctamente.");
			} else {
				error("Se encontro problema en la validacion.");
			}
		} catch (ParametroException e) {
			error(e);
		}
	}

	/**
	 * Se encarga de modificar el registro
	 */
	public void modifyBtn() {
		load();
		try {
			if (valid()) {
				parametroSvc.update(registro, userLogin);
				if (StringUtils.isNotBlank(parametroGrupo.getGrupo())) {
					parametroGrupo.setParametro(registro.getCodigo());
					parametroSvc.update(parametroGrupo, userLogin);
				}
				notificar("Se ha modificado el parametro correctamente.");
			} else {
				error("Se encontro problema en la validacion.");
			}
		} catch (ParametroException e) {
			error(e);
		}
	}

	/**
	 * Se encarga de eliminar el registro
	 */
	public void deleteBtn() {
		load();
		try {
			if (StringUtils.isNotBlank(registro.getCodigo())) {
				if (StringUtils.isNotBlank(parametroGrupo.getCodigo())) {
					parametroSvc.delete(parametroGrupo, userLogin);
				}
				parametroSvc.delete(registro, userLogin);
				notificar("El parametro fue eliminado correctamente.");
			} else {
				error("La eliminacion del codigo es vacio.");
			}
		} catch (ParametroException e) {
			error(e);
		}
	}

	/**
	 * Se encarga de cancelar el registro y devolverse al bean principal
	 */
	public void cancelBtn() {
		getController(ParametrosBean.class);
	}

}
