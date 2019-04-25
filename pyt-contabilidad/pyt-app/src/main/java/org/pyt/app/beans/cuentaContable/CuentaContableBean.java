package org.pyt.app.beans.cuentaContable;

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
import org.pyt.common.exceptions.CuentaContableException;

import com.pyt.service.dto.CuentaContableDTO;
import com.pyt.service.dto.EmpresaDTO;
import com.pyt.service.dto.ParametroDTO;
import com.pyt.service.interfaces.ICuentaContableSvc;

import co.com.arquitectura.annotation.proccessor.FXMLFile;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;

/**
 * Bean encargado de crear las empresas
 * 
 * @author alejandro parra
 * @since 07/05/2018
 */
@FXMLFile(path = "view/cuentaContable", file = "listCuentaContable.fxml")
public class CuentaContableBean extends ABean<CuentaContableDTO> {
	@Inject(resource = "com.pyt.service.implement.CuentaContableSvc")
	private ICuentaContableSvc cuentaContableSvc;
	@FXML
	private javafx.scene.control.TableView<CuentaContableDTO> tabla;
	@FXML
	private TextField nombre;
	@FXML
	private TextField asociado;
	@FXML
	private PopupParametrizedControl tipoCuentaContables;
	@FXML
	private Button btnMod;
	@FXML
	private Button btnDel;
	@FXML
	private HBox paginador;
	@FXML
	private TableColumn<CuentaContableDTO, String> tipo;
	@FXML
	private TableColumn<CuentaContableDTO, String> naturaleza;
	@FXML
	private TableColumn<CuentaContableDTO, String> tipoCuentaContable;
	private DataTableFXML<CuentaContableDTO, CuentaContableDTO> dt;
	private CuentaContableDTO filter;

	@FXML
	public void initialize() {
		NombreVentana = "Lista de Cuenta Contable";
		registro = new CuentaContableDTO();
		filter = new CuentaContableDTO();
		registro.setNaturaleza(new ParametroDTO());
		registro.setTipoPlanContable(new ParametroDTO());
		registro.setTipo(new ParametroDTO());
		registro.setEmpresa(new EmpresaDTO());
		tipoCuentaContables.setPopupOpenAction(()->popupTipoPlanContable());
		tipoCuentaContables.setCleanValue(()->filter.setTipoPlanContable(null));
		tipoCuentaContable
				.setCellValueFactory(e -> new SimpleStringProperty(e.getValue().getTipoPlanContable().getNombre()));
		tipo.setCellValueFactory(e -> new SimpleStringProperty(e.getValue().getTipo().getNombre()));
		naturaleza.setCellValueFactory(e -> new SimpleStringProperty(e.getValue().getNaturaleza().getNombre()));
		lazy();
	}

	/**
	 * encargada de crear el objeto que va controlar la tabla
	 */
	public void lazy() {
		dt = new DataTableFXML<CuentaContableDTO, CuentaContableDTO>(paginador, tabla) {
			@Override
			public List<CuentaContableDTO> getList(CuentaContableDTO filter, Integer page, Integer rows) {
				List<CuentaContableDTO> lista = new ArrayList<CuentaContableDTO>();
				try {
					lista = cuentaContableSvc.getCuentaContables(filter, page - 1, rows);
				} catch (CuentaContableException e) {
					error(e);
				}
				return lista;
			}

			@Override
			public Integer getTotalRows(CuentaContableDTO filter) {
				Integer count = 0;
				try {
					count = cuentaContableSvc.getTotalRows(filter);
				} catch (CuentaContableException e) {
					error(e);
				}
				return count;
			}

			@Override
			public CuentaContableDTO getFilter() {
				CuentaContableDTO filtro = new CuentaContableDTO();
				if (StringUtils.isNotBlank(nombre.getText())) {
					filtro.setNombre(nombre.getText());
				}
				if (StringUtils.isNotBlank(asociado.getText())) {
					filtro.setAsociado(asociado.getText());
				}
				if (filter.getTipoPlanContable() != null) {
					filtro.setTipoPlanContable(filter.getTipoPlanContable());
				}
				return filtro;
			}
		};
	}

	public void clickTable() {
		btnMod.setVisible(isSelected());
		btnDel.setVisible(isSelected());
	}
	
	public final void popupTipoPlanContable() {
		try {
			((PopupGenBean<ParametroDTO>) controllerPopup(new PopupGenBean<ParametroDTO>(ParametroDTO.class))
					.setWidth(350)
					.addDefaultValuesToGenericParametrized(ParametroConstants.FIELD_NAME_GROUP, ParametroConstants.GRUPO_TIPO_PLAN_CONTABLE)
					).load("#{CuentaContableBean.tipoPlanContable}");
		} catch (Exception e) {
			error(e);
		}
	}
	
	public final void setTipoPlanContable(ParametroDTO tipoPlanContable) {
		filter.setTipoPlanContable(tipoPlanContable);
		tipoCuentaContables.setText(tipoPlanContable.getNombre());
	}
	
	public void add() {
		getController(CuentaContableCRUBean.class);
	}

	public void search() {
		dt.search();
	}

	public void del() {
		try {
			registro = dt.getSelectedRow();
			if (registro != null) {
				cuentaContableSvc.delete(registro, userLogin);
				notificar("Se ha eliminado la cuenta contable.");
				dt.search();
			} else {
				notificar("No se ha seleccionado una cuenta contable.");
			}
		} catch (CuentaContableException e) {
			error(e);
		}
	}

	public void set() {
		try {
			controllerPopup(ConfirmPopupBean.class).load("#{CuentaContableBean.delete}",
				"¿Desea eliminar los registros seleccionados?");
		}catch(Exception e) {
			error(e);
		}
	}

	public void setDelete(Boolean valid) {
		if (!valid)
			return;
		registro = dt.getSelectedRow();
		if (registro != null) {
			getController(CuentaContableCRUBean.class).load(registro);
		} else {
			notificar("No se ha seleccionado un concepto.");
		}
	}

	public Boolean isSelected() {
		return dt.isSelected();
	}

	public DataTableFXML<CuentaContableDTO, CuentaContableDTO> getDt() {
		return dt;
	}
}
