package org.pyt.app.beans.trabajador;

import java.util.ArrayList;
import java.util.List;

import org.pyt.app.components.ConfirmPopupBean;
import org.pyt.app.components.DataTableFXML;
import org.pyt.common.annotations.Inject;
import org.pyt.common.common.ABean;
import org.pyt.common.exceptions.EmpleadoException;

import com.pyt.service.dto.PersonaDTO;
import com.pyt.service.dto.TrabajadorDTO;
import com.pyt.service.interfaces.IEmpleadosSvc;

import co.com.arquitectura.annotation.proccessor.FXMLFile;
import javafx.beans.property.SimpleObjectProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;

/**
 * Bean encargado de listar los trabajadores
 * 
 * @author alejandro parra
 * @since 21/06/2018
 */
@FXMLFile(path = "view/persona", file = "listTrabajador.fxml")
public class TrabajadorBean extends ABean<TrabajadorDTO> {
	@Inject(resource = "com.pyt.service.implement.EmpleadosSvc")
	private IEmpleadosSvc empleadosSvc;
	@FXML
	private javafx.scene.control.TableView<TrabajadorDTO> tabla;
	@FXML
	private TextField nombres;
	@FXML
	private TextField apellidos;
	@FXML
	private TextField documento;
	@FXML
	private Button btnMod;
	@FXML
	private HBox paginador;
	@FXML
	private TableColumn<TrabajadorDTO, String> nombre;
	@FXML
	private TableColumn<TrabajadorDTO, String> apellido;
	@FXML
	private TableColumn<TrabajadorDTO, String> email;
	@FXML
	private TableColumn<TrabajadorDTO, String> cedula;
	@FXML
	private TableColumn<TrabajadorDTO, String> fechaNacimiento;
	private DataTableFXML<TrabajadorDTO, TrabajadorDTO> dt;

	@FXML
	public void initialize() {
		NombreVentana = "Lista Empleados";
		registro = new TrabajadorDTO();
		fechaNacimiento.setCellValueFactory(e->{
			SimpleObjectProperty<String> sp = new SimpleObjectProperty<String>();
			sp.setValue(e.getValue().getPersona().getFechaNacimiento().toString());
			return sp;
		});
		nombre.setCellValueFactory(e->{
			SimpleObjectProperty<String> sp = new SimpleObjectProperty<String>();
			sp.setValue(e.getValue().getPersona().getNombre());
			return sp;
		});
		apellido.setCellValueFactory(e->{
			SimpleObjectProperty<String> sp = new SimpleObjectProperty<String>();
			sp.setValue(e.getValue().getPersona().getApellido());
			return sp;
		});
		cedula.setCellValueFactory(e->{
			SimpleObjectProperty<String> sp = new SimpleObjectProperty<String>();
			sp.setValue(e.getValue().getPersona().getDocumento());
			return sp;
		});
		email.setCellValueFactory(e->{
			SimpleObjectProperty<String> sp = new SimpleObjectProperty<String>();
			sp.setValue(e.getValue().getCorreo());
			return sp;
		});
		lazy();
	}

	/**
	 * encargada de crear el objeto que va controlar la tabla
	 */
	public void lazy() {
		dt = new DataTableFXML<TrabajadorDTO, TrabajadorDTO>(paginador, tabla) {
			@Override
			public List<TrabajadorDTO> getList(TrabajadorDTO filter, Integer page, Integer rows) {
				List<TrabajadorDTO> lista = new ArrayList<TrabajadorDTO>();
				try {
					lista = empleadosSvc.getTrabajadores(getFilter(), page-1, rows);
				} catch (EmpleadoException e) {
					error(e);
				}
				return lista;
			}

			@Override
			public Integer getTotalRows(TrabajadorDTO filter) {
				Integer count = 0;
				try {
					count = empleadosSvc.getTotalRows(filter);
				} catch (EmpleadoException e) {
					error(e);
				}
				return count;
			}

			@Override
			public TrabajadorDTO getFilter() {
				TrabajadorDTO filtro = new TrabajadorDTO();
				filtro.setPersona(new PersonaDTO());
				filtro.getPersona().setDocumento(documento.getText());
				filtro.getPersona().setNombre(nombres.getText());
				filtro.getPersona().setApellido(apellidos.getText());
				return filtro;
			}
		};
	}

	public void clickTable() {
		btnMod.setVisible(isSelected());
	}

	public void add() {
		getController(TrabajadorCRUBean.class);
	}

	public void search() {
		dt.search();
	}

	public void del() {
		try {
			controllerPopup(ConfirmPopupBean.class).load("#{TrabajadorBean.delete}", "¿Desea eliminar los registros seleccionados?");
		}catch(Exception e) {
			error(e);
		}
	}
	public void setDelete(Boolean valid) {
		try {
			if(!valid)return;	
			registro = dt.getSelectedRow();
			if (registro != null) {
				empleadosSvc.delete(registro, userLogin);
				notificar("Se ha eliminaro la empresa.");
				dt.search();
			} else {
				notificar("No se ha seleccionado una empresa.");
			}
		} catch (EmpleadoException e) {
			error(e);
		}
	}

	public void set() {
		registro = dt.getSelectedRow();
		if (registro != null) {
			getController(TrabajadorCRUBean.class).load(registro);
		} else {
			notificar("No se ha seleccionado una empresa.");
		}
	}

	public Boolean isSelected() {
		return dt.isSelected();
	}

	public DataTableFXML<TrabajadorDTO, TrabajadorDTO> getDt() {
		return dt;
	}
}
