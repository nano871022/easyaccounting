package org.pyt.app.beans.persona;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.pyt.app.components.ConfirmPopupBean;
import org.pyt.common.annotations.Inject;
import org.pyt.common.common.OptI18n;
import org.pyt.common.exceptions.EmpleadoException;

import com.pyt.service.dto.PersonaDTO;
import com.pyt.service.interfaces.IEmpleadosSvc;

import co.com.arquitectura.annotation.proccessor.FXMLFile;
import co.com.japl.ea.beans.abstracts.ABean;
import co.com.japl.ea.utls.DataTableFXMLUtil;
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
@FXMLFile(path = "view/persona", file = "listPersonas.fxml")
public class PersonaBean extends ABean<PersonaDTO> {
	@Inject(resource = "com.pyt.service.implement.EmpleadosSvc")
	private IEmpleadosSvc empleadosSvc;
	@FXML
	private javafx.scene.control.TableView<PersonaDTO> tabla;
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
	private TableColumn<PersonaDTO, String> nombre;
	@FXML
	private TableColumn<PersonaDTO, String> apellido;
	@FXML
	private TableColumn<PersonaDTO, String> email;
	@FXML
	private TableColumn<PersonaDTO, String> cedula;
	@FXML
	private TableColumn<PersonaDTO, String> fechaNacimiento;
	private DataTableFXMLUtil<PersonaDTO, PersonaDTO> dt;
	private final String formatoFechaShow = "dd/MM/YYY";

	@FXML
	public void initialize() {
		NombreVentana = "Lista Personas";
		registro = new PersonaDTO();
		fechaNacimiento.setCellValueFactory(e -> {
			SimpleObjectProperty<String> sp = new SimpleObjectProperty<String>();
			if (e.getValue() != null && e.getValue().getFechaNacimiento() != null) {
				var fecha = new SimpleDateFormat(formatoFechaShow).format(e.getValue().getFechaNacimiento());
				sp.setValue(fecha);
			}
			return sp;
		});
		nombre.setCellValueFactory(e -> {
			SimpleObjectProperty<String> sp = new SimpleObjectProperty<String>();
			sp.setValue(e.getValue().getNombre());
			return sp;
		});
		apellido.setCellValueFactory(e -> {
			SimpleObjectProperty<String> sp = new SimpleObjectProperty<String>();
			sp.setValue(e.getValue().getApellido());
			return sp;
		});
		cedula.setCellValueFactory(e -> {
			SimpleObjectProperty<String> sp = new SimpleObjectProperty<String>();
			sp.setValue(e.getValue().getDocumento());
			return sp;
		});
		email.setCellValueFactory(e -> {
			SimpleObjectProperty<String> sp = new SimpleObjectProperty<String>();
			sp.setValue(e.getValue().getEmail());
			return sp;
		});
		lazy();
	}

	/**
	 * encargada de crear el objeto que va controlar la tabla
	 */
	public void lazy() {
		dt = new DataTableFXMLUtil<PersonaDTO, PersonaDTO>(paginador, tabla) {
			@Override
			public List<PersonaDTO> getList(PersonaDTO filter, Integer page, Integer rows) {
				List<PersonaDTO> lista = new ArrayList<PersonaDTO>();
				try {
					lista = empleadosSvc.getPersona(getFilter(), page - 1, rows);
				} catch (EmpleadoException e) {
					error(e);
				}
				return lista;
			}

			@Override
			public Integer getTotalRows(PersonaDTO filter) {
				Integer count = 0;
				try {
					count = empleadosSvc.getTotalRows(filter);
				} catch (EmpleadoException e) {
					error(e);
				}
				return count;
			}

			@Override
			public PersonaDTO getFilter() {
				PersonaDTO filtro = new PersonaDTO();
				if (!documento.getText().isEmpty()) {
					filtro.setDocumento(documento.getText());
				}
				if (!nombres.getText().isEmpty()) {
					filtro.setNombre(nombres.getText());
				}
				if (!apellidos.getText().isEmpty()) {
					filtro.setApellido(apellidos.getText());
				}
				return filtro;
			}
		};
	}

	public void clickTable() {
		btnMod.setVisible(isSelected());
	}

	public void add() {
		getController(PersonaCRUBean.class);
	}

	public void search() {
		dt.search();
	}

	public void del() {
		try {
			controllerPopup(ConfirmPopupBean.class).load("#{PersonaBean.delete}",
					OptI18n.process(val -> "¿Desea eliminar los registros seleccionados?", null));
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
				empleadosSvc.delete(registro, getUsuario());
				notificar("Se ha eliminaro la persona.");
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
			getController(PersonaCRUBean.class).load(registro);
		} else {
			notificar("No se ha seleccionado una empresa.");
		}
	}

	public Boolean isSelected() {
		return dt.isSelected();
	}

	public DataTableFXMLUtil<PersonaDTO, PersonaDTO> getDt() {
		return dt;
	}
}
