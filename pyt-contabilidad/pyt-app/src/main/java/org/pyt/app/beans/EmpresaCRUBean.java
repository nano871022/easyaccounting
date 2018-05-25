package org.pyt.app.beans;

import java.util.List;

import org.pyt.common.annotations.FXMLFile;
import org.pyt.common.annotations.Inject;
import org.pyt.common.common.ABean;
import org.pyt.common.common.SelectList;
import org.pyt.common.constants.AppConstants;
import org.pyt.common.constants.ParametroConstants;
import org.pyt.common.exceptions.EmpresasException;
import org.pyt.common.exceptions.ParametroException;

import com.pyt.service.dto.EmpresaDTO;
import com.pyt.service.dto.ParametroDTO;
import com.pyt.service.interfaces.IEmpresasSvc;
import com.pyt.service.interfaces.IParametrosSvc;

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
@FXMLFile(path = "/view", file = "empresa.fxml")
public class EmpresaCRUBean extends ABean<EmpresaDTO> {
	@Inject(resource = "com.pyt.service.implement.ParametrosSvc")
	private IParametrosSvc parametroSvc;
	@Inject(resource = "com.pyt.service.implement.EmpresaSvc")
	private IEmpresasSvc empresaSvc;
	@FXML
	private Label codigo;
	@FXML
	private TextField nombre;
	@FXML
	private TextField nit;
	@FXML
	private TextField digito;
	@FXML
	private TextField direccion;
	@FXML
	private TextField email;
	@FXML
	private TextField telefono;
	@FXML
	private TextField pais;
	@FXML
	private ChoiceBox<String> moneda;
	@FXML
	private TextField representante;
	@FXML
	private TextField contador;
	@FXML
	private TextField nContador;
	@FXML
	private Label titulo;
	@FXML
	private BorderPane pane;
	private List<ParametroDTO> lMoneda;

	@FXML
	public void initialize() {
		NombreVentana = "Agregando Nueva Empresa";
		titulo.setText(NombreVentana);
		registro = new EmpresaDTO();
		
		try {
			ParametroDTO moneda = new ParametroDTO();
			moneda.setGrupo(ParametroConstants.GRUPO_MONEDA);
			lMoneda = parametroSvc.getAllParametros(moneda);
			SelectList.put(this.moneda, lMoneda, "descripcion");
			pais.setText(AppConstants.DEFAULT_PAIS_COL);
		} catch (ParametroException e) {
			error(e);
		}
	}

	/**
	 * Pasa los campos de la pantalla el objeto dto
	 */
	private void load() {
		if (registro == null) {
			registro = new EmpresaDTO();
		}
		registro.setCodigo(codigo.getText());
		registro.setNombre(nombre.getText());
		registro.setNit(nit.getText());
		registro.setDigitoVerificacion(digito.getText());
		registro.setDireccion(direccion.getText());
		registro.setCorreoElectronico(email.getText());
		registro.setTelefono(telefono.getText());
		registro.setPais(pais.getText());
		registro.setMonedaDefecto(SelectList.get(moneda, lMoneda, "descripcion"));
		registro.setNombreRepresentante(representante.getText());
		registro.setNombreContador(contador.getText());
		registro.setTarjetaProfeccionalContador(nContador.getText());
	}

	private void loadFxml() {
		if (registro == null)
			return;
		codigo.setText(registro.getCodigo());
		nombre.setText(registro.getNombre());
		nit.setText(registro.getNit());
		digito.setText(registro.getDigitoVerificacion());
		direccion.setText(registro.getDireccion());
		email.setText(registro.getCorreoElectronico());
		telefono.setText(registro.getTelefono());
		pais.setText(registro.getPais());
		SelectList.selectItem(moneda, registro.getMonedaDefecto(), "descripcion");
		representante.setText(registro.getNombreRepresentante());
		contador.setText(registro.getNombreContador());
		nContador.setText(registro.getTarjetaProfeccionalContador());
	}

	public void load(EmpresaDTO dto) {
		if (dto != null && dto.getCodigo() != null) {
			registro = dto;
			loadFxml();
			titulo.setText("Modificando Empresa");
		} else {
			error("La empresa es invalida para editar.");
			cancel();
		}
	}

	public void add() {
		load();
		try {
			if (registro.getCodigo() != null) {
				empresaSvc.update(registro, userLogin);
				notificar("Se guardo la empresa correctamente.");
			} else {
				empresaSvc.insert(registro, userLogin);
				notificar("Se agrego la emrpesa correctamente.");
			}
		} catch (EmpresasException e) {
			error(e);
		}
	}

	public void cancel() {
		getController(EmpresaBean.class);
	}

}
