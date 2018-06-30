package org.pyt.app.beans.servicio;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.pyt.app.components.DataTableFXML;
import org.pyt.common.annotations.FXMLFile;
import org.pyt.common.annotations.Inject;
import org.pyt.common.common.ABean;
import org.pyt.common.exceptions.ServiciosException;

import com.pyt.service.dto.ServicioDTO;
import com.pyt.service.interfaces.IServiciosSvc;

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
@FXMLFile(path = "view/servicio", file = "listServicio.fxml")
public class ServicioBean extends ABean<ServicioDTO> {
	@Inject(resource = "com.pyt.service.implement.ServiciosSvc")
	private IServiciosSvc serviciosSvc;
	@FXML
	private javafx.scene.control.TableView<ServicioDTO> tabla;
	@FXML
	private TextField nombre;
	@FXML
	private TextField descripcion;
	@FXML
	private Button btnMod;
	@FXML
	private HBox paginador;
	private DataTableFXML<ServicioDTO, ServicioDTO> dt;

	@FXML
	public void initialize() {
		NombreVentana = "Lista de Servicios";
		registro = new ServicioDTO();
		lazy();
	}

	/**
	 * encargada de crear el objeto que va controlar la tabla
	 */
	public void lazy() {
		dt = new DataTableFXML<ServicioDTO, ServicioDTO>(paginador, tabla) {
			@Override
			public List<ServicioDTO> getList(ServicioDTO filter, Integer page, Integer rows) {
				List<ServicioDTO> lista = new ArrayList<ServicioDTO>();
				try {
					lista = serviciosSvc.getServicios(filter, page, rows);
				} catch (ServiciosException e) {
					error(e);
				}
				return lista;
			}

			@Override
			public Integer getTotalRows(ServicioDTO filter) {
				Integer count = 0;
				try {
					count = serviciosSvc.getTotalRows(filter);
				} catch (ServiciosException e) {
					error(e);
				}
				return count;
			}

			@Override
			public ServicioDTO getFilter() {
				ServicioDTO filtro = new ServicioDTO();
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
		getController(ServicioCRUBean.class);
	}

	public void search() {
		dt.search();
	}

	public void del() {
		try {
			registro = dt.getSelectedRow();
			if (registro != null) {
				serviciosSvc.delete(registro, userLogin);
				notificar("Se ha eliminaro la empresa.");
				dt.search();
			} else {
				notificar("No se ha seleccionado un servicio.");
			}
		} catch (ServiciosException e) {
			error(e);
		}
	}

	public void set() {
		registro = dt.getSelectedRow();
		if (registro != null) {
			getController(ServicioCRUBean.class).load(registro);
		} else {
			notificar("No se ha seleccionado un servicio.");
		}
	}

	public Boolean isSelected() {
		return dt.isSelected();
	}

	public DataTableFXML<ServicioDTO, ServicioDTO> getDt() {
		return dt;
	}
}
