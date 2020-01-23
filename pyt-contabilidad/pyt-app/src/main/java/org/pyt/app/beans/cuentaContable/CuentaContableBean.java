package org.pyt.app.beans.cuentaContable;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.ea.app.custom.PopupParametrizedControl;
import org.pyt.app.components.ConfirmPopupBean;
import org.pyt.app.components.PopupGenBean;
import org.pyt.common.annotations.Inject;
import org.pyt.common.constants.ParametroConstants;
import org.pyt.common.exceptions.CuentaContableException;

import com.pyt.service.dto.CuentaContableDTO;
import com.pyt.service.dto.EmpresaDTO;
import com.pyt.service.dto.ParametroDTO;
import com.pyt.service.dto.ParametroGrupoDTO;
import com.pyt.service.interfaces.ICuentaContableSvc;
import com.pyt.service.interfaces.IGenericServiceSvc;

import co.com.arquitectura.annotation.proccessor.FXMLFile;
import co.com.japl.ea.beans.abstracts.ABean;
import co.com.japl.ea.utls.DataTableFXMLUtil;
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
	@Inject(resource = "com.pyt.service.implement.GenericServiceSvc")
	private IGenericServiceSvc<ParametroGrupoDTO> parametroGrupoSvc;
	@FXML
	private javafx.scene.control.TableView<CuentaContableDTO> tabla;
	@FXML
	private TextField nombre;
	@FXML
	private PopupParametrizedControl asociado;
	@FXML
	private PopupParametrizedControl tipoPlanContable;
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
	private DataTableFXMLUtil<CuentaContableDTO, CuentaContableDTO> dt;
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
		asociado.setPopupOpenAction(() -> popupAsociado());
		asociado.setCleanValue(() -> filter.setAsociado(null));
		tipoCuentaContables.setPopupOpenAction(() -> popupTipoCuentaContable());
		tipoCuentaContables.setCleanValue(() -> filter.setTipo(null));
		tipoPlanContable.setPopupOpenAction(() -> popupTipoPlanContable());
		tipoPlanContable.setCleanValue(() -> filter.setTipoPlanContable(null));
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
		dt = new DataTableFXMLUtil<CuentaContableDTO, CuentaContableDTO>(paginador, tabla) {
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
				if (filter.getAsociado() != null) {
					filtro.setAsociado(filter.getAsociado());
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
			var popup = controllerPopup(new PopupGenBean<ParametroDTO>(ParametroDTO.class));
			var parametroGrupo = new ParametroGrupoDTO();
			parametroGrupo.setGrupo(ParametroConstants.GRUPO_TIPO_PLAN_CONTABLE);
			var codigo = parametroGrupoSvc.get(parametroGrupo).getParametro();
			popup.setWidth(250).addDefaultValuesToGenericParametrized(ParametroConstants.FIELD_NAME_GROUP, codigo);
			popup.load("#{CuentaContableBean.tipoPlanContable}");
		} catch (Exception e) {
			error(e);
		}
	}

	public final void popupTipoCuentaContable() {
		try {
			var popup = controllerPopup(new PopupGenBean<ParametroDTO>(ParametroDTO.class));
			var parametroGrupo = new ParametroGrupoDTO();
			parametroGrupo.setGrupo(ParametroConstants.GRUPO_TIPO);
			var codigo = parametroGrupoSvc.get(parametroGrupo).getParametro();
			popup.setWidth(250).addDefaultValuesToGenericParametrized(ParametroConstants.FIELD_NAME_GROUP, codigo);
			popup.load("#{CuentaContableBean.tipoCuentaContable}");
		} catch (Exception e) {
			error(e);
		}
	}

	public final void popupAsociado() {
		try {
			controllerPopup(new PopupGenBean<CuentaContableDTO>(CuentaContableDTO.class))
					.load("#{CuentaContableBean.asociado}");
		} catch (Exception e) {
			error(e);
		}
	}

	public final void setAsociado(CuentaContableDTO asociado) {
		if (asociado != null) {
			filter.setAsociado(asociado);
			this.asociado.setText(asociado.getCodigoCuenta() + ":" + asociado.getNombre());
		}
	}

	public final void setTipoPlanContable(ParametroDTO tipoPlanContable) {
		if (tipoPlanContable != null) {
			filter.setTipoPlanContable(tipoPlanContable);
			this.tipoPlanContable.setText(tipoPlanContable.getNombre());
		}
	}

	public final void setTipoCuentaContable(ParametroDTO tipoCuentaContable) {
		if (tipoCuentaContable != null) {
			filter.setTipo(tipoCuentaContable);
			tipoCuentaContables.setText(tipoCuentaContable.getNombre());
		}
	}

	public void add() {
		getController(CuentaContableCRUBean.class);
	}

	public void search() {
		dt.search();
	}

	public void set() {
		registro = dt.getSelectedRow();
		getController(CuentaContableCRUBean.class).load(registro);
	}

	public void del() {
		try {
			controllerPopup(ConfirmPopupBean.class).load("#{CuentaContableBean.delete}",
					"¿Desea eliminar los registros seleccionados?");
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
				cuentaContableSvc.delete(registro, getUsuario());
				notificar("Se ha eliminado la cuenta contable.");
				dt.search();
			} else {
				notificar("No se ha seleccionado una cuenta contable.");
			}
		} catch (Exception e) {
			error(e);
		}
	}

	public Boolean isSelected() {
		return dt.isSelected();
	}

	public DataTableFXMLUtil<CuentaContableDTO, CuentaContableDTO> getDt() {
		return dt;
	}
}
