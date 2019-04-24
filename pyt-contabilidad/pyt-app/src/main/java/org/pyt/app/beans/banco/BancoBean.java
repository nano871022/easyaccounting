package org.pyt.app.beans.banco;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.ea.app.custom.PopupParametrizedControl;
import org.pyt.app.components.ConfirmPopupBean;
import org.pyt.app.components.DataTableFXML;
import org.pyt.app.components.PopupGenBean;
import org.pyt.common.annotations.Inject;
import org.pyt.common.common.ABean;
import org.pyt.common.constants.ParametroConstants;
import org.pyt.common.exceptions.BancoException;

import com.pyt.service.dto.BancoDTO;
import com.pyt.service.dto.ParametroDTO;
import com.pyt.service.interfaces.IBancosSvc;
import com.pyt.service.interfaces.IParametrosSvc;

import co.com.arquitectura.annotation.proccessor.FXMLFile;
import javafx.beans.property.SimpleObjectProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;

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
	private PopupParametrizedControl tipoCuenta;
	@FXML
	private PopupParametrizedControl tipoBanco;
	@FXML
	private PopupParametrizedControl estado;
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
	private final static String FIELD_NOMBRE = "nombre";

	@FXML
	public void initialize() {
		NombreVentana = "Lista de Bancos";
		registro = new BancoDTO();
		tipoBanco.setPopupOpenAction(()->popupOpenTipoBanco());
		tipoBanco.setCleanValue(()->{registro.setTipoBanco(null);tipoBanco.setText(null);});
		tipoCuenta.setPopupOpenAction(()->popupOpenTipoCuentas());
		tipoCuenta.setCleanValue(()->{registro.setTipoCuenta(null);tipoCuenta.setText(null);});
		estado.setPopupOpenAction(()->popupOpenEstado());
		estado.setCleanValue(()->{registro.setEstado(null);estado.setText(null);});
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
			controllerPopup( ConfirmPopupBean.class).load("#{BancoBean.delete}", "¿Desea eliminar los registros seleccionados?");
		}catch(Exception e) {
			error(e);
		}
	}
	
	public final void popupOpenTipoBanco() {
		try {
			((PopupGenBean<ParametroDTO>) controllerPopup(new PopupGenBean<ParametroDTO>(ParametroDTO.class)
			.addDefaultValuesToGenericParametrized(ParametroConstants.FIELD_NAME_GROUP,parametroSvc.getIdByParametroGroup(ParametroConstants.GRUPO_TIPO_BANCO)))
			.addDefaultValuesToGenericParametrized(ParametroConstants.FIELD_NAME_STATE, ParametroConstants.COD_ESTADO_PARAMETRO_ACTIVO)
			).load("#{BancoBean.tipoBanco}");
		} catch (Exception e) {
			error(e);
		}
	}
	
	public final void setTipoBanco(ParametroDTO parametro) {
		registro.setTipoBanco(parametro);
		tipoBanco.setText(parametro.getDescripcion());
	}

	public final void popupOpenTipoCuentas() {
		try {
			((PopupGenBean<ParametroDTO>) controllerPopup(new PopupGenBean<ParametroDTO>(ParametroDTO.class)
					.addDefaultValuesToGenericParametrized(ParametroConstants.FIELD_NAME_GROUP,parametroSvc.getIdByParametroGroup(ParametroConstants.GRUPO_TIPO_CUENTA)))
					.addDefaultValuesToGenericParametrized(ParametroConstants.FIELD_NAME_STATE, ParametroConstants.COD_ESTADO_PARAMETRO_ACTIVO)
					).load("#{BancoBean.tipoCuentas}");
		} catch (Exception e) {
			error(e);
		}
	}
	
	public final void setTipoCuentas(ParametroDTO parametro) {
		registro.setTipoCuenta(parametro);
		tipoCuenta.setText(parametro.getDescripcion());
	}
	
	public final void popupOpenEstado() {
		try {
			((PopupGenBean<ParametroDTO>) controllerPopup(new PopupGenBean<ParametroDTO>(ParametroDTO.class)
					.addDefaultValuesToGenericParametrized(ParametroConstants.FIELD_NAME_GROUP,parametroSvc.getIdByParametroGroup(ParametroConstants.GRUPO_ESTADO_BANCO)))
					.addDefaultValuesToGenericParametrized(ParametroConstants.FIELD_NAME_STATE, ParametroConstants.COD_ESTADO_PARAMETRO_ACTIVO)
					).load("#{BancoBean.estado}");
		} catch (Exception e) {
			error(e);
		}
	}
	
	public final void setEstado(ParametroDTO parametro) {
		registro.setEstado(parametro);
		estado.setText(parametro.getDescripcion());
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
