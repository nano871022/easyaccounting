package org.pyt.app.beans.actividadIca;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.pyt.app.components.DataTableFXML;
import org.pyt.common.annotations.FXMLFile;
import org.pyt.common.annotations.Inject;
import org.pyt.common.common.ABean;
import org.pyt.common.exceptions.ActividadIcaException;

import com.pyt.service.dto.ActividadIcaDTO;
import com.pyt.service.interfaces.IActividadIcaSvc;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;

/**
 * Bean encargado de crear las actividades ica
 * 
 * @author alejandro parra
 * @since 07/05/2018
 */
@FXMLFile(path = "view/actividadIca", file = "listActividadIca.fxml")
public class ActividadIcaBean extends ABean<ActividadIcaDTO> {
	@Inject(resource = "com.pyt.service.implement.ActividadIcaSvc")
	private IActividadIcaSvc actividadIcaSvc;
	@FXML
	private javafx.scene.control.TableView<ActividadIcaDTO> tabla;
	@FXML
	private TextField nombre;
	@FXML
	private TextField descripcion;
	@FXML
	private Button btnMod;
	@FXML
	private HBox paginador;
	private DataTableFXML<ActividadIcaDTO, ActividadIcaDTO> dt;

	@FXML
	public void initialize() {
		NombreVentana = "Lista Actividad Ica";
		registro = new ActividadIcaDTO();
		lazy();
	}

	/**
	 * encargada de crear el objeto que va controlar la tabla
	 */
	public void lazy() {
		dt = new DataTableFXML<ActividadIcaDTO, ActividadIcaDTO>(paginador, tabla) {
			@Override
			public List<ActividadIcaDTO> getList(ActividadIcaDTO filter, Integer page, Integer rows) {
				List<ActividadIcaDTO> lista = new ArrayList<ActividadIcaDTO>();
				try {
					lista = actividadIcaSvc.getActividadesIca(filter, page, rows);
				} catch (ActividadIcaException e) {
					error(e);
				}
				return lista;
			}

			@Override
			public Integer getTotalRows(ActividadIcaDTO filter) {
				Integer count = 0;
				try {
					count = actividadIcaSvc.getTotalRows(filter);
				} catch (ActividadIcaException e) {
					error(e);
				}
				return count;
			}

			@Override
			public ActividadIcaDTO getFilter() {
				ActividadIcaDTO filtro = new ActividadIcaDTO();
				if (StringUtils.isNotBlank(nombre.getText())) {
					filtro.setNombre(nombre.getText());
				}
				if (StringUtils.isNotBlank(descripcion.getText())) {
					filtro.setDescripcion(descripcion.getText());
				}
				return filtro;
			}
		};
	}

	public void clickTable() {
		btnMod.setVisible(isSelected());
	}

	public void add() {
		getController(ActividadIcaCRUBean.class);
	}

	public void search() {
		dt.search();
	}

	public void del() {
		try {
			registro = dt.getSelectedRow();
			if (registro != null) {
				actividadIcaSvc.delete(registro, userLogin);
				notificar("Se ha eliminaro la actividad Ica.");
				dt.search();
			} else {
				notificar("No se ha seleccionado una actividad ica.");
			}
		} catch (ActividadIcaException e) {
			error(e);
		}
	}

	public void set() {
		registro = dt.getSelectedRow();
		if (registro != null) {
			getController(ActividadIcaCRUBean.class).load(registro);
		} else {
			notificar("No se ha seleccionado una actividad ica.");
		}
	}

	public Boolean isSelected() {
		return dt.isSelected();
	}

	public DataTableFXML<ActividadIcaDTO, ActividadIcaDTO> getDt() {
		return dt;
	}
}
