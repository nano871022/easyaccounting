package org.pyt.app.beans.repuesto;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.pyt.app.components.ConfirmPopupBean;
import org.pyt.app.components.DataTableFXML;
import org.pyt.common.annotations.FXMLFile;
import org.pyt.common.annotations.Inject;
import org.pyt.common.common.ABean;
import org.pyt.common.common.LoadAppFxml;
import org.pyt.common.exceptions.RepuestoException;

import com.pyt.service.dto.RepuestoDTO;
import com.pyt.service.interfaces.IRepuestosSvc;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

/**
 * Bean encargado de crear las empresas
 * 
 * @author alejandro parra
 * @since 07/05/2018
 */
@FXMLFile(path = "view/repuesto", file = "listRepuesto.fxml")
public class RepuestoBean extends ABean<RepuestoDTO> {
	@Inject(resource = "com.pyt.service.implement.RepuestosSvc")
	private IRepuestosSvc repuestoSvc;
	@FXML
	private javafx.scene.control.TableView<RepuestoDTO> tabla;
	@FXML
	private TextField nombre;
	@FXML
	private Button btnMod;
	@FXML
	private HBox paginador;
	private DataTableFXML<RepuestoDTO, RepuestoDTO> dt;

	@FXML
	public void initialize() {
		NombreVentana = "Lista de Repuestos";
		registro = new RepuestoDTO();
		lazy();
	}

	/**
	 * encargada de crear el objeto que va controlar la tabla
	 */
	public void lazy() {
		dt = new DataTableFXML<RepuestoDTO, RepuestoDTO>(paginador, tabla) {
			@Override
			public List<RepuestoDTO> getList(RepuestoDTO filter, Integer page, Integer rows) {
				List<RepuestoDTO> lista = new ArrayList<RepuestoDTO>();
				try {
					lista = repuestoSvc.getRepuestos(filter, page-1, rows);
				} catch (RepuestoException e) {
					error(e);
				}
				return lista;
			}

			@Override
			public Integer getTotalRows(RepuestoDTO filter) {
				Integer count = 0;
				try {
					count = repuestoSvc.getTotalRows(filter);
				} catch (RepuestoException e) {
					error(e);
				}
				return count;
			}

			@Override
			public RepuestoDTO getFilter() {
				RepuestoDTO filtro = new RepuestoDTO();
				if (StringUtils.isNotBlank(nombre.getText())) {
					filtro.setNombre(nombre.getText());
				}
				return filtro;
			}
		};
	}

	public void clickTable() {
		btnMod.setVisible(isSelected());
	}

	public void add() {
		getController(RepuestoCRUBean.class);
	}

	public void search() {
		dt.search();
	}

	public void del() {
		try {
			LoadAppFxml.loadBeanFxml(new Stage(), ConfirmPopupBean.class).load("#{RepuestoBean.delete}", "¿Desea eliminar los registros seleccionados?");
		}catch(Exception e) {
			error(e);
		}
	}
	public void setDelete(Boolean valid) {
		try {
			if(!valid)return;
			registro = dt.getSelectedRow();
			if (registro != null) {
				repuestoSvc.delete(registro, userLogin);
				notificar("Se ha eliminado el repuesto.");
				dt.search();
			} else {
				notificar("No se ha seleccionado un repuesto.");
			}
		} catch (RepuestoException e) {
			error(e);
		}
	}

	public void set() {
		registro = dt.getSelectedRow();
		if (registro != null) {
			getController(RepuestoCRUBean.class).load(registro);
		} else {
			notificar("No se ha seleccionado un repuesto.");
		}
	}

	public Boolean isSelected() {
		return dt.isSelected();
	}

	public DataTableFXML<RepuestoDTO, RepuestoDTO> getDt() {
		return dt;
	}
}
