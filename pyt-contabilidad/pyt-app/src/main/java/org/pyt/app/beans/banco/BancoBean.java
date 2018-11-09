package org.pyt.app.beans.banco;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.pyt.app.components.ConfirmPopupBean;
import org.pyt.app.components.DataTableFXML;
import org.pyt.common.annotations.FXMLFile;
import org.pyt.common.annotations.Inject;
import org.pyt.common.common.ABean;
import org.pyt.common.common.LoadAppFxml;
import org.pyt.common.common.SelectList;
import org.pyt.common.constants.ParametroConstants;
import org.pyt.common.exceptions.BancoException;
import org.pyt.common.exceptions.ParametroException;

import com.pyt.service.dto.BancoDTO;
import com.pyt.service.dto.ParametroDTO;
import com.pyt.service.interfaces.IBancosSvc;
import com.pyt.service.interfaces.IParametrosSvc;

import javafx.beans.property.SimpleObjectProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

/**
 * Bean encargado de crear las actividades ica
 * 
 * @author alejandro parra
 * @since 07/05/2018
 */
@FXMLFile(path = "view/banco", file = "listBanco.fxml")
public class BancoBean extends ABean<BancoDTO> {
	@Inject(resource = "com.pyt.service.implement.BancoSvc")
	private IBancosSvc bancoSvc;
	@Inject(resource = "com.pyt.service.implement.ParametrosSvc")
	private IParametrosSvc parametroSvc;
	@FXML
	private javafx.scene.control.TableView<BancoDTO> tabla;
	@FXML
	private TextField nombre;
	@FXML
	private TextField descripcion;
	@FXML
	private TextField numeroCuenta;
	@FXML
	private ChoiceBox<String> tipoCuenta;
	@FXML
	private ChoiceBox<String> tipoBanco;
	@FXML
	private ChoiceBox<String> estado;
	@FXML
	private Button btnMod;
	@FXML
	private HBox paginador;
	@FXML
	private TableColumn<BancoDTO, String> tipoBancos;
	@FXML
	private TableColumn<BancoDTO, String> tipoCuentas;
	@FXML
	private TableColumn<BancoDTO, String> estados;
	private DataTableFXML<BancoDTO, BancoDTO> dt;
	private List<ParametroDTO> listEstados;
	private List<ParametroDTO> listTipoCuenta;
	private List<ParametroDTO> listTipoBanco;
	private final static String FIELD_NOMBRE = "nombre";

	@FXML
	public void initialize() {
		NombreVentana = "Lista de Bancos";
		registro = new BancoDTO();
		ParametroDTO pTipoCuenta = new ParametroDTO();
		ParametroDTO pTipoBanco = new ParametroDTO();
		ParametroDTO pEstados = new ParametroDTO();
		try {
			listTipoCuenta = parametroSvc.getAllParametros(pTipoCuenta, ParametroConstants.GRUPO_TIPO_CUENTA);
			listTipoBanco = parametroSvc.getAllParametros(pTipoBanco, ParametroConstants.GRUPO_TIPO_BANCO);
			listEstados = parametroSvc.getAllParametros(pEstados, ParametroConstants.GRUPO_ESTADO_BANCO);
		} catch (ParametroException e) {
			error(e);
		}
		SelectList.put(tipoCuenta, listTipoCuenta, FIELD_NOMBRE);
		SelectList.put(tipoBanco, listTipoBanco, FIELD_NOMBRE);
		SelectList.put(estado, listEstados, FIELD_NOMBRE);
		tipoCuenta.getSelectionModel().selectFirst();
		tipoBanco.getSelectionModel().selectFirst();
		estado.getSelectionModel().selectFirst();
		tipoBancos.setCellValueFactory(e -> {
			SimpleObjectProperty<String> property = new SimpleObjectProperty<String>();
			property.setValue(e.getValue().getTipoBanco().getNombre());
			return property;
		});
		tipoCuentas.setCellValueFactory(e -> {
			SimpleObjectProperty<String> property = new SimpleObjectProperty<String>();
			property.setValue(e.getValue().getTipoCuenta().getNombre());
			return property;
		});
		estados.setCellValueFactory(e -> {
			SimpleObjectProperty<String> property = new SimpleObjectProperty<String>();
			property.setValue(e.getValue().getEstado().getNombre());
			return property;
		});
		lazy();
	}

	/**
	 * encargada de crear el objeto que va controlar la tabla
	 */
	public void lazy() {
		dt = new DataTableFXML<BancoDTO, BancoDTO>(paginador, tabla) {
			@Override
			public List<BancoDTO> getList(BancoDTO filter, Integer page, Integer rows) {
				List<BancoDTO> lista = new ArrayList<BancoDTO>();
				try {
					lista = bancoSvc.getBancos(filter, page-1, rows);
				} catch (BancoException e) {
					error(e);
				}
				return lista;
			}

			@Override
			public Integer getTotalRows(BancoDTO filter) {
				Integer count = 0;
				try {
					count = bancoSvc.getTotalRows(filter);
				} catch (BancoException e) {
					error(e);
				}
				return count;
			}

			@Override
			public BancoDTO getFilter() {
				BancoDTO filtro = new BancoDTO();
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
		getController(BancoCRUBean.class);
	}

	public void search() {
		dt.search();
	}

	public void del() {
		try {
			LoadAppFxml.loadBeanFxml(new Stage(), ConfirmPopupBean.class).load("#{BancoBean.delete}", "¿Desea eliminar los registros seleccionados?");
		}catch(Exception e) {
			error(e);
		}
	}
	public void setDelete(Boolean valid) {
		try {
			if(!valid)return;
			registro = dt.getSelectedRow();
			if (registro != null) {
				bancoSvc.delete(registro, userLogin);
				notificar("Se ha eliminaro el banco.");
				dt.search();
			} else {
				notificar("No se ha seleccionado un banco.");
			}
		} catch (BancoException e) {
			error(e);
		}
	}

	public void set() {
		registro = dt.getSelectedRow();
		if (registro != null) {
			getController(BancoCRUBean.class).load(registro);
		} else {
			notificar("No se ha seleccionado un banco.");
		}
	}

	public Boolean isSelected() {
		return dt.isSelected();
	}

	public DataTableFXML<BancoDTO, BancoDTO> getDt() {
		return dt;
	}
}
