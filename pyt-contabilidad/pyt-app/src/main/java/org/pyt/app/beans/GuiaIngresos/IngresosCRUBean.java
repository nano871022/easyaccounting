package org.pyt.app.beans.GuiaIngresos;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.ea.app.custom.PopupParametrizedControl;
import org.pyt.app.components.ConfirmPopupBean;
import org.pyt.common.abstracts.ADto;
import org.pyt.common.annotations.Inject;
import org.pyt.common.common.Table;
import org.pyt.common.constants.ParametroConstants;
import org.pyt.common.exceptions.GenericServiceException;
import org.pyt.common.exceptions.IngresoException;
import org.pyt.common.exceptions.LoadAppFxmlException;
import org.pyt.common.exceptions.validates.ValidateValueException;
import org.pyt.common.validates.ValidateValues;

import com.pyt.service.dto.EmpresaDTO;
import com.pyt.service.dto.IngresoDTO;
import com.pyt.service.dto.IngresoRepuestoDTO;
import com.pyt.service.dto.IngresoServicioDTO;
import com.pyt.service.dto.ParametroDTO;
import com.pyt.service.dto.ParametroGrupoDTO;
import com.pyt.service.dto.PersonaDTO;
import com.pyt.service.dto.ServicioDTO;
import com.pyt.service.dto.TrabajadorDTO;
import com.pyt.service.dto.inventario.ProductoDTO;
import com.pyt.service.dto.inventario.ResumenProductoDTO;
import com.pyt.service.interfaces.IEmpleadosSvc;
import com.pyt.service.interfaces.IEmpresasSvc;
import com.pyt.service.interfaces.IGenericServiceSvc;
import com.pyt.service.interfaces.IIngresosSvc;
import com.pyt.service.interfaces.IServiciosSvc;
import com.pyt.service.interfaces.inventarios.IProductosSvc;

import co.com.arquitectura.annotation.proccessor.FXMLFile;
import co.com.japl.ea.beans.abstracts.ABean;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

/**
 * Se encarga de procesar la pantalla de creacion y actualizacion de una empresa
 * 
 * @author Alejandro Parra
 * @since 2018-05-22
 */
@FXMLFile(path = "/view/guiaIngresos", file = "ingresos.fxml")
public class IngresosCRUBean extends ABean<IngresoDTO> {
	@Inject(resource = "com.pyt.service.implement.ServiciosSvc")
	private IServiciosSvc serviciosSvc;
	@Inject(resource = "com.pyt.service.implement.inventario.ProductosSvc")
	private IProductosSvc repuestosSvc;
	@Inject(resource = "com.pyt.service.implement.GenericServiceSvc")
	private IGenericServiceSvc<IngresoServicioDTO> ingresoServicioSvc;
	@Inject(resource = "com.pyt.service.implement.GenericServiceSvc")
	private IGenericServiceSvc<ResumenProductoDTO> resumenProductoSvc;
	@Inject(resource = "com.pyt.service.implement.GenericServiceSvc")
	private IGenericServiceSvc<IngresoRepuestoDTO> ingresoRepuestoSvc;
	@Inject(resource = "com.pyt.service.implement.GenericServiceSvc")
	private IGenericServiceSvc<ParametroGrupoDTO> parametroGrupoSvc;
	@Inject(resource = "com.pyt.service.implement.GenericServiceSvc")
	private IGenericServiceSvc<ParametroDTO> parametroSvc;
	@Inject(resource = "com.pyt.service.implement.IngresosSvc")
	private IIngresosSvc ingresosSvc;
	@Inject(resource = "com.pyt.service.implement.EmpresaSvc")
	private IEmpresasSvc empresaSvc;
	@Inject(resource = "com.pyt.service.implement.EmpleadosSvc")
	private IEmpleadosSvc empleadosSvc;
	@FXML
	private Label codigo;
	@FXML
	private TextField placa;
	@FXML
	private PopupParametrizedControl conductorEntrada;
	@FXML
	private PopupParametrizedControl conductorSalida;
	@FXML
	private TextField docEntrada;
	@FXML
	private TextField docSalida;
	@FXML
	private PopupParametrizedControl propietario;
	@FXML
	private TextField telContacto;
	@FXML
	private PopupParametrizedControl empresa;
	@FXML
	private PopupParametrizedControl servicio;
	@FXML
	private PopupParametrizedControl repuesto;
	@FXML
	private PopupParametrizedControl trabajador;
	@FXML
	private TextField tiempoEstimado;
	@FXML
	private TextField tiempoTrabajo;
	@FXML
	private DatePicker fechaEntrada;
	@FXML
	private DatePicker fechaSalida;
	@FXML
	private TextArea descripcion;
	@FXML
	private Label titulo;
	@FXML
	private Label totalServicio;
	@FXML
	private Label totalRepuesto;
	@FXML
	private TableView<IngresoServicioDTO> tablaServicio;
	@FXML
	private TableView<IngresoRepuestoDTO> tablaRepuesto;
	@FXML
	private TableColumn<IngresoRepuestoDTO, String> nombre;
	@FXML
	private TableColumn<IngresoRepuestoDTO, String> valorRepuesto;
	@FXML
	private TableColumn<IngresoServicioDTO, String> nombreServicio;
	@FXML
	private TableColumn<IngresoServicioDTO, String> valorServicio;
	private ValidateValues valid;
	private final static String field_name = "nombre";
	private final static String field_valor_venta = "valorVenta";
	private final static String field_valor_mano_obra = "valorManoObra";
	private List<IngresoServicioDTO> listServicios;
	private List<IngresoRepuestoDTO> listRepuestos;
	private ParametroDTO parametroEstado;
	private IngresoServicioDTO servicioSelect;
	private IngresoRepuestoDTO repuestoSelect;

	@FXML
	public void initialize() {
		NombreVentana = i18n().get("fxml.title.add.new.entry");
		titulo.setText(NombreVentana);
		registro = new IngresoDTO();
		valid = new ValidateValues();
		docEntrada.setDisable(false);
		docSalida.setDisable(false);
		docEntrada.setDisable(true);
		docSalida.setDisable(true);
		conductorEntrada.setPopupOpenAction(() -> popupConductorEntrada());
		conductorEntrada.setCleanValue(() -> cleanConductorEntrada());
		conductorSalida.setPopupOpenAction(() -> popupConductorSalida());
		conductorSalida.setCleanValue(() -> cleanConductorSalida());
		trabajador.setPopupOpenAction(() -> popupTrabajador());
		trabajador.setCleanValue(() -> cleanTrabajador());
		empresa.setPopupOpenAction(() -> popupEmpresa());
		empresa.setCleanValue(() -> cleanEmpresa());
		servicio.setPopupOpenAction(() -> popupServicio());
		servicio.setCleanValue(() -> cleanServicio());
		repuesto.setPopupOpenAction(() -> popupRepuesto());
		repuesto.setCleanValue(() -> cleanRepuesto());
		propietario.setPopupOpenAction(() -> popupPropietario());
		propietario.setCleanValue(() -> cleanPropietario());
		nombre.setCellValueFactory(e -> new SimpleStringProperty(e.getValue().getRepuesto().getProducto().getNombre()));
		valorServicio.setCellValueFactory(
				e -> new SimpleStringProperty(String.valueOf(e.getValue().getServicio().getValorManoObra())));
		nombreServicio.setCellValueFactory(e -> new SimpleStringProperty(e.getValue().getServicio().getNombre()));
		valorRepuesto.setCellValueFactory(
				e -> new SimpleStringProperty(String.valueOf(e.getValue().getRepuesto().getValorVenta())));
		tablaRepuesto.getSelectionModel().selectedItemProperty()
				.addListener((listener, oldValue, newValue) -> repuestoSelect());
		tablaServicio.getSelectionModel().selectedItemProperty()
				.addListener((listener, oldValue, newValue) -> servicioSelect());
		try {
			searchParametroEstadoTrabajadorActivo();
		} catch (Exception e) {
			logger.logger(e);
		}
	}

	public final void repuestoSelect() {
		var selected = Table.getSelectedRows(tablaRepuesto);
		repuestoSelect = selected.get(0);
		repuesto.setText(repuestoSelect.getRepuesto().getProducto().getNombre());
	}

	public final void servicioSelect() {
		var selected = Table.getSelectedRows(tablaServicio);
		servicioSelect = selected.get(0);
		servicio.setText(servicioSelect.getServicio().getNombre());
	}

	/**
	 * Pasa los campos de la pantalla el objeto dto
	 */
	private void load() {
		if (registro == null) {
			registro = new IngresoDTO();
		}
		try {
			registro.setCodigo(codigo.getText());
			registro.setPlacaVehiculo(placa.getText());
			registro.setDescripcion(descripcion.getText());
			registro.setFechaEntrada(valid.cast(fechaEntrada.getValue(), Date.class));
			registro.setFechaSalida(valid.cast(fechaSalida.getValue(), Date.class));
			registro.setTelefonoContacto(telContacto.getText());
			registro.setTiempoEstimado(valid.cast(tiempoEstimado.getText(), Long.class));
			registro.setTiempoTrabajado(valid.cast(tiempoTrabajo.getText(), Long.class));
		} catch (ValidateValueException e) {
			error(e);
		}
	}

	private void loadFxml() {
		if (registro == null)
			return;
		try {
			codigo.setText(registro.getCodigo());
			placa.setText(registro.getPlacaVehiculo());
			conductorEntrada.setText(registro.getConductorEntrada().getNombres());
			conductorSalida.setText(registro.getConductorSalida().getNombres());
			descripcion.setText(registro.getDescripcion());
			docEntrada.setText(registro.getConductorEntrada().getDocumento());
			docSalida.setText(registro.getConductorSalida().getDocumento());
			fechaEntrada.setValue(valid.cast(registro.getFechaEntrada(), LocalDate.class));
			fechaSalida.setValue(valid.cast(registro.getFechaSalida(), LocalDate.class));
			propietario.setText(registro.getPropietario().getNombres());
			telContacto.setText(registro.getTelefonoContacto());
			tiempoEstimado.setText(valid.cast(registro.getTiempoEstimado(), String.class));
			tiempoTrabajo.setText(valid.cast(registro.getTiempoTrabajado(), String.class));
			trabajador.setText(registro.getTrabajador().getPersona().getNombres());
			empresa.setText(registro.getEmpresa().getNombre());

			Table.put(tablaRepuesto, listRepuestos);
			Table.put(tablaServicio, listServicios);
			totalRepuesto.setText(sumarRepuestos(listRepuestos).toString());
			totalServicio.setText(sumarServicios(listServicios).toString());
		} catch (ValidateValueException e) {
			error(e);
		}
	}

	public void load(IngresoDTO dto) {
		if (dto != null && dto.getCodigo() != null) {
			registro = dto;
			getListas();
			loadFxml();
			titulo.setText(i18n().get("mensaje.modifing.entry"));
		} else {
			error(i18n().get("err.entry.cant.edit"));
			cancel();
		}
	}

	private final void getListas() {
		try {
			var ingresoServicio = new IngresoServicioDTO();
			ingresoServicio.setIngreso(registro);
			var ingresoRepuesto = new IngresoRepuestoDTO();
			ingresoRepuesto.setIngreso(registro);
			listServicios = ingresoServicioSvc.getAll(ingresoServicio);
			listRepuestos = ingresoRepuestoSvc.getAll(ingresoRepuesto);
		} catch (Exception e) {
			error(e);
		}
	}

	private final <T extends ADto> BigDecimal sumarServicios(List<IngresoServicioDTO> lista) {
		BigDecimal suma = new BigDecimal(0);
		try {
			if (lista != null) {
				for (var dto : lista) {
					var value = dto.getServicio().getValorManoObra();
					if (value != null) {
						suma = suma.add(valid.cast(value, BigDecimal.class));
					}
				}
			}
		} catch (ValidateValueException e) {
			error(e);
		}
		return suma;
	}

	private final <T extends ADto> BigDecimal sumarRepuestos(List<IngresoRepuestoDTO> lista) {
		BigDecimal suma = new BigDecimal(0);
		try {
			if (lista != null) {
				for (var dto : lista) {
					var value = dto.getRepuesto().getValorVenta();
					if (value != null) {
						suma = suma.add(valid.cast(value, BigDecimal.class));
					}
				}
			}
		} catch (ValidateValueException e) {
			error(e);
		}
		return suma;
	}

	private void searchParametroEstadoTrabajadorActivo() throws GenericServiceException {
		var parametroGrupo = new ParametroGrupoDTO();
		parametroGrupo.setGrupo(ParametroConstants.GRUPO_ESTADO_EMPLEADO);
		var grupo = parametroGrupoSvc.get(parametroGrupo);
		var parametro = new ParametroDTO();
		parametro.setGrupo(grupo.getParametro());
		parametro.setValor("1");
		parametro = parametroSvc.get(parametro);
		parametroEstado = parametro;
	}

	public void popupTrabajador() {
		try {
			var bean = controllerGenPopup((TrabajadorDTO.class));
			bean.addDefaultValuesToGenericParametrized("estado", parametroEstado);
			bean.load("#{IngresosCRUBean.trabajador}");
		} catch (Exception e) {
			error(e);
		}
	}

	public void setTrabajador(TrabajadorDTO trabajador) {
		if (trabajador != null) {
			registro.setTrabajador(trabajador);
			this.trabajador.setText(trabajador.getPersona().getNombres());
		}
	}

	public void cleanTrabajador() {
		registro.setTrabajador(null);
		this.trabajador.setText(null);
	}

	public void popupPropietario() {
		try {
			controllerGenPopup((PersonaDTO.class)).load("#{IngresosCRUBean.propietario}");
		} catch (Exception e) {
			error(e);
		}
	}

	public void setPropietario(PersonaDTO persona) {
		if (persona != null) {
			propietario.setText(persona.getNombres());
			registro.setPropietario(persona);
		}
	}

	public void cleanPropietario() {
		propietario.setText(null);
		registro.setPropietario(null);
	}

	public void popupEmpresa() {
		try {
			controllerGenPopup((EmpresaDTO.class)).load("#{IngresosCRUBean.empresa}");
		} catch (Exception e) {
			error(e);
		}
	}

	public void setEmpresa(EmpresaDTO empresa) {
		if (empresa != null) {
			this.empresa.setText(empresa.getNombre());
			registro.setEmpresa(empresa);
		}
	}

	public void cleanEmpresa() {
		empresa.setText(null);
		registro.setEmpresa(null);
	}

	public void popupRepuesto() {
		try {
			controllerGenPopup((ProductoDTO.class)).load("#{IngresosCRUBean.repuesto}");
		} catch (Exception e) {
			error(e);
		}
	}

	public void cleanRepuesto() {
		repuestoSelect = null;
		repuesto.setText(null);

	}

	public void popupServicio() {
		try {
			controllerGenPopup((ServicioDTO.class)).load("#{IngresosCRUBean.servicio}");
		} catch (Exception e) {
			error(e);
		}
	}

	public void cleanServicio() {
		servicio.setText(null);
		servicioSelect = null;
	}

	public final void setServicio(ServicioDTO servicio) {
		if (servicio != null) {
			servicioSelect = new IngresoServicioDTO();
			servicioSelect.setServicio(servicio);
			servicioSelect.setIngreso(registro);
			this.servicio.setText(servicio.getNombre());
		}
	}

	public void popupConductorEntrada() {
		try {
			controllerGenPopup(PersonaDTO.class).load("#{IngresosCRUBean.conductorEntrada}");
		} catch (Exception e) {
			error(e);
		}
	}

	public void cleanConductorEntrada() {
		registro.setConductorEntrada(null);
		conductorEntrada.setText(null);

	}

	public final void setConductorEntrada(PersonaDTO persona) {
		if (persona != null) {
			registro.setConductorEntrada(persona);
			conductorEntrada.setText(persona.getNombres());
			docEntrada.setText(persona.getDocumento());
		}
	}

	public void popupConductorSalida() {
		try {
			controllerGenPopup(PersonaDTO.class).load("#{IngresosCRUBean.conductorSalida}");
		} catch (Exception e) {
			error(e);
		}
	}

	public void cleanConductorSalida() {
		registro.setConductorSalida(null);
		conductorSalida.setText(null);

	}

	public final void setConductorSalida(PersonaDTO persona) {
		if (persona != null) {
			registro.setConductorSalida(persona);
			conductorSalida.setText(persona.getNombres());
			docSalida.setText(persona.getDocumento());
		}
	}

	public final void removeServicio() {
		try {
			if (listServicios != null && listServicios.size() > 0 && servicioSelect != null) {
				if (StringUtils.isNotBlank(servicioSelect.getCodigo())) {
					controllerPopup(ConfirmPopupBean.class).load("#{IngresosCRUBean.deleteServicio}",
							i18n().get("mensaje.wish.do.delete.selected.rows"));
				} else {
					listServicios.remove(servicioSelect);
					servicio.setText(null);
					Table.put(tablaServicio, listServicios);
				}
			}
		} catch (LoadAppFxmlException e) {
			error(e);
		}
	}

	public final void setDeleteServicio(Boolean respuesta) {
		try {
			if (respuesta) {
				ingresoServicioSvc.delete(servicioSelect, getUsuario());
				listServicios.remove(servicioSelect);
				servicio.setText(null);
				Table.put(tablaServicio, listServicios);
			}
		} catch (Exception e) {
			error(e);
		}
	}

	public final void removeRepuesto() {
		try {
			if (listRepuestos != null && listRepuestos.size() > 0 && repuestoSelect != null) {
				if (StringUtils.isNotBlank(repuestoSelect.getCodigo())) {
					controllerPopup(ConfirmPopupBean.class).load("#{IngresosCRUBean.deleteRepuesto}",
							i18n().get("wish.do.delete.selected.rows"));
				} else {
					listRepuestos.remove(repuestoSelect);
					repuesto.setText(null);
					Table.put(tablaRepuesto, listRepuestos);
				}
			}
		} catch (LoadAppFxmlException e) {
			error(e);
		}
	}

	public final void setDeleteRepuesto(Boolean respuesta) {
		try {
			if (respuesta) {
				ingresoRepuestoSvc.delete(repuestoSelect, getUsuario());
				listRepuestos.remove(repuestoSelect);
				repuesto.setText(null);
				Table.put(tablaRepuesto, listRepuestos);
			}
		} catch (Exception e) {
			error(e);
		}
	}

	public final void addServicio() {
		if (listServicios == null) {
			listServicios = new ArrayList<IngresoServicioDTO>();
		}
		if (servicioSelect != null) {
			listServicios.add(servicioSelect);
			Table.put(tablaServicio, listServicios);
			totalServicio.setText(sumarServicios(listServicios).toString());
			servicioSelect = null;
			servicio.setText(null);
		}
	}

	public final void setRepuesto(ProductoDTO repuesto) {
		if (repuesto != null) {
			repuestoSelect = new IngresoRepuestoDTO();
			repuestoSelect.setRepuesto(getResumenProducto(repuesto));
			repuestoSelect.setIngreso(registro);
			this.repuesto.setText(repuesto.getNombre());
		}
	}

	private final ResumenProductoDTO getResumenProducto(ProductoDTO producto) {
		try {
			var resumenProducto = new ResumenProductoDTO();
			resumenProducto.setProducto(producto);
			var list = resumenProductoSvc.getAll(resumenProducto);
			if (list != null) {
				return list.get(0);
			}
		} catch (Exception e) {
			error(e);
		}
		return null;
	}

	public final void addRepuesto() {
		if (listRepuestos == null) {
			listRepuestos = new ArrayList<IngresoRepuestoDTO>();
		}
		if (repuestoSelect != null) {
			listRepuestos.add(repuestoSelect);
			Table.put(tablaRepuesto, listRepuestos);
			totalRepuesto.setText(sumarRepuestos(listRepuestos).toString());
			repuestoSelect = null;
			repuesto.setText(null);
		}
	}

	public void add() {
		load();
		try {
			if (StringUtils.isNotBlank(registro.getCodigo())) {
				ingresosSvc.update(registro, getUsuario());
				ingresoServiciosYRepuestos();
				notificarI18n("mensaje.entry.have.been.updated.succesfull");
				cancel();
			} else {
				registro = ingresosSvc.insert(registro, getUsuario());
				ingresoServiciosYRepuestos();
				codigo.setText(registro.getCodigo());
				notificarI18n("mensaje.entry.have.been.inserted.succesfull");
				cancel();
			}
		} catch (IngresoException e) {
			error(e);
		}
	}

	private final void ingresoServiciosYRepuestos() {
		listServicios.stream().filter(row -> StringUtils.isBlank(row.getCodigo())).forEach(row -> {
			try {
				ingresoServicioSvc.insert(row, getUsuario());
			} catch (GenericServiceException e) {
				logger.DEBUG(i18n().get("err.input.service"), e);
			}
		});
		listRepuestos.stream().filter(row -> StringUtils.isBlank(row.getCodigo())).forEach(row -> {
			try {
				ingresoRepuestoSvc.insert(row, getUsuario());
			} catch (GenericServiceException e) {
				logger.DEBUG("err.input.spareparts", e);
			}
		});

	}

	public void cancel() {
		getController(ListIngresosBean.class);
	}

}
