package org.pyt.app.beans.config;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.pyt.common.abstracts.ADto;
import org.pyt.common.annotations.Inject;
import org.pyt.common.common.SearchService;
import org.pyt.common.common.SelectList;
import org.pyt.common.constants.AppConstants;
import org.pyt.common.constants.ConfigServiceConstant;
import org.pyt.common.constants.DataPropertiesConstants;
import org.pyt.common.constants.PermissionConstants;
import org.pyt.common.constants.PropertiesConstants;
import org.pyt.common.exceptions.LoadAppFxmlException;
import org.pyt.common.exceptions.MarcadorServicioException;
import org.pyt.common.exceptions.SearchServicesException;
import org.pyt.common.properties.PropertiesUtils;

import com.pyt.service.dto.AsociacionArchivoDTO;
import com.pyt.service.dto.ConfiguracionDTO;
import com.pyt.service.dto.MarcadorDTO;
import com.pyt.service.dto.MarcadorServicioDTO;
/**
 * Se encarga de controlar las asociacionesentra marcadores y servicios 
 * @author Alejandro Parra
 * @since 05-08-2018
 */
import com.pyt.service.dto.ServicioCampoBusquedaDTO;
import com.pyt.service.interfaces.IConfigMarcadorServicio;

import co.com.arquitectura.annotation.proccessor.FXMLFile;
import co.com.arquitectura.annotation.proccessor.Services;
import co.com.arquitectura.librerias.implement.Services.ServicePOJO;
import co.com.arquitectura.librerias.implement.listProccess.AbstractListFromProccess;
import co.com.japl.ea.beans.abstracts.ABean;
import co.com.japl.ea.utls.DataTableFXMLUtil;
import co.com.japl.ea.utls.PermissionUtil;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Tab;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.HBox;
import javafx.util.StringConverter;

@FXMLFile(path = "view/config/servicios", file = "ConfigService.fxml")
public class ConfigServiceBean extends ABean<AsociacionArchivoDTO> {
	@FXML
	private Button btnAnterior;
	@FXML
	private Button btnEliminar;
	@FXML
	private Button btnGuardar;
	@FXML
	private Button btnSiguiente;
	@FXML
	private Button btnUpdateMark;
	@FXML
	private Button btnCancelar;
	@FXML
	private Button btnAddMark;
	@FXML
	private Button btnDelServicio;
	@FXML
	private Button btnSaveMarcador;
	@FXML
	private Button btnDelMarcador;
	@FXML
	private Button btnLimpiar;
	@FXML
	private Tab tabServicios;
	@FXML
	private Tab tabMarcadores;
	@FXML
	private Tab tabAsociaciones;
	@FXML
	private Tab tabConfigurar;
	@FXML
	private TextField nameMarcador;
	@FXML
	private TextField nameFiler;
	@FXML
	private TextField nameFilerOut;
	@FXML
	private TextField columna;
	@FXML
	private TextField configuracion;
	@FXML
	private TextArea descripcion;
	@FXML
	private TextField orderMarcador;
	@FXML
	private TableView<MarcadorDTO> lstMarcadores;
	@FXML
	private TableColumn<MarcadorDTO, String> colMarcador;
	@FXML
	private TableColumn<MarcadorDTO, Integer> orden;
	@FXML
	private TableView<ServicioCampoBusquedaDTO> lstServicioCampo;
	@FXML
	private TableView<MarcadorServicioDTO> lstMarcadoresCampos;
	@FXML
	private TableColumn<String, String> colServicios;
	@FXML
	private ChoiceBox<ServicePOJO> lstServicios;
	@FXML
	private ChoiceBox<String> servicio;
	@FXML
	private ChoiceBox<String> campo;
	@FXML
	private ChoiceBox<String> marcador;
	@FXML
	private ChoiceBox<String> lstCampos;
	@FXML
	private ChoiceBox<String> lstMarcadorIn;
	@FXML
	private HBox paginatorMarcadores;
	@FXML
	private HBox paginatorServicios;
	@FXML
	private HBox paginatorAsociar;
	@FXML
	private TableColumn<MarcadorDTO, String> colInOut;
	@FXML
	private Label totalMarcador;
	@FXML
	private Label totalAsociar;
	@FXML
	private Label totalServicio;
	@FXML
	private Label lNameFiler;
	@FXML
	private Label lNameFilerOut;
	private ToggleGroup group;
	private ToggleGroup group2;
	private List<MarcadorDTO> marcadores;
	private List<String> servicios;
	private List<String> campos;
	private List<ServicioCampoBusquedaDTO> serviciosCampoBusqueda;
	private List<MarcadorServicioDTO> marcadoresServicios;
	private Integer posicion;
	private Integer max;
	private String servicePackageList;

	private AbstractListFromProccess<ServicePOJO> listServices;
	@Inject(resource = "com.pyt.service.implement.ConfigMarcadorServicioSvc")
	private IConfigMarcadorServicio configMarcadorServicio;
	private ConfiguracionDTO config;
	private DataTableFXMLUtil<ServicioCampoBusquedaDTO, ServicioCampoBusquedaDTO> tbServicioCampoBusqueda;
	private DataTableFXMLUtil<MarcadorServicioDTO, MarcadorServicioDTO> tbMarcadorSerivicio;
	private DataTableFXMLUtil<MarcadorDTO, MarcadorDTO> tbMarcador;
	@FXML
	private RadioButton rbIn;
	@FXML
	private RadioButton rbOut;
	@FXML
	private RadioButton rbCargues;
	@FXML
	private RadioButton rbReportes;

	@FXML
	public void initialize() {
		marcadores = new ArrayList<MarcadorDTO>();
		servicios = new ArrayList<String>();
		campos = new ArrayList<String>();
		serviciosCampoBusqueda = new ArrayList<ServicioCampoBusquedaDTO>();
		marcadoresServicios = new ArrayList<MarcadorServicioDTO>();
		visibleButtons();
		config = new ConfiguracionDTO();
		posicion = ConfigServiceConstant.TAB_MARCADOR;
		max = ConfigServiceConstant.TAB_CONFIGURACION;
		group = new ToggleGroup();
		group2 = new ToggleGroup();
		btnUpdateMark.setVisible(false);
		btnLimpiar.setVisible(false);
		rbIn.setToggleGroup(group);
		rbOut.setToggleGroup(group);
		rbCargues.setToggleGroup(group2);
		rbReportes.setToggleGroup(group2);
		rbOut.setSelected(true);
		rbReportes.setSelected(true);
		rbCargues.onActionProperty().set(e -> {
			rbOut.setVisible(false);
			rbIn.setSelected(true);
			nameFiler.setVisible(false);
			nameFilerOut.setVisible(false);
			lNameFiler.setVisible(false);
			lNameFilerOut.setVisible(false);
		});
		rbReportes.onActionProperty().set(e -> {
			rbOut.setVisible(true);
			rbOut.setSelected(true);
			nameFiler.setVisible(true);
			nameFilerOut.setVisible(true);
			lNameFiler.setVisible(true);
			lNameFilerOut.setVisible(true);
		});
		try {
			servicePackageList = PropertiesUtils.getInstance().setNameProperties(PropertiesConstants.PROP_DATA).load()
					.getProperties().getProperty(DataPropertiesConstants.CONST_SERVICES_LIST);
		} catch (Exception e) {
			logger.logger(e);
		}
		hiddenTabs();
		hiddenBtns();
		configColmn();
		loadServices();
	}

	public final void load() {
		logger.info("Nuevo config service.");
		config = new ConfiguracionDTO();
		dataTable();
		orderMarcador.setText("1");
	}

	/**
	 * Se carga de realizar busqueda de los registros y carga las data tablas
	 * 
	 * @param configuracion {@link ConfiguracionDTO}
	 */
	public final void load(ConfiguracionDTO configuracion) {
		logger.info("Load cargado");
		try {
			if (configuracion == null) {
				configuracion = new ConfiguracionDTO();
			}
			config = configuracion;
			if (StringUtils.isNotBlank(config.getConfiguracion())) {
				this.configuracion.setText(config.getConfiguracion());
			}
			if (StringUtils.isNotBlank(config.getArchivo())) {
				this.nameFiler.setText(config.getArchivo());
			}
			if (StringUtils.isNotBlank(config.getArchivoSalida())) {
				this.nameFilerOut.setText(config.getArchivoSalida());
			}
			if (config.getReport() == null) {
				this.rbReportes.setSelected(true);
			}
			if (config.getReport() != null && config.getReport()) {
				this.rbReportes.setSelected(true);
			}
			if (config.getReport() != null && !config.getReport()) {
				this.rbCargues.setSelected(true);
				rbOut.setVisible(false);
				rbIn.setSelected(true);
				nameFiler.setVisible(false);
				nameFilerOut.setVisible(false);
				lNameFiler.setVisible(false);
				lNameFilerOut.setVisible(false);
			}
			if (StringUtils.isNotBlank(config.getDescripcion())) {
				this.descripcion.setText(config.getDescripcion());
			}
			dataTable();
		} catch (Exception e) {
			error(e);
		}
	}

	/**
	 * Se encargaa de verificar que se selecciono el registro de la tabla y acivar
	 * el campo de eliminar
	 */
	public final void marcadorSelect() {
		btnEliminar.setVisible(tbMarcador.isSelected());
		btnUpdateMark.setVisible(tbMarcador.isSelected());
		var mark = tbMarcador.getSelectedRow();
		orderMarcador.setText(String.valueOf(mark.getOrden()));
		nameMarcador.setText(mark.getMarcador());
		if (mark.getTipoInOut().contains(rbCargues.getText())) {
			rbCargues.setSelected(true);
			rbReportes.setSelected(false);
		} else if (mark.getTipoInOut().contains(rbReportes.getText())) {
			rbCargues.setSelected(false);
			rbReportes.setSelected(true);
		}
		btnAddMark.setVisible(false);
		btnUpdateMark.setVisible(true);
		btnLimpiar.setVisible(true);
	}

	/**
	 * Se encarga de verificar que se selecciono el registro de la tabla y actuvar
	 * el campo de eliminar
	 */
	public final void servicioCampo() {
		btnDelServicio.setVisible(tbServicioCampoBusqueda.isSelected());
	}

	/**
	 * Se encarga de verificar que se selecciono el registro de la tabla y activa el
	 * campo
	 */
	public final void servicioMarcador() {
		btnDelMarcador.setVisible(tbMarcadorSerivicio.isSelected());
	}

	/**
	 * Se encarga de cargar la configuracion de consultass de data tablas para
	 * marcadores, servicios Campo busqueda y marcadores servicios
	 */
	private void dataTable() {
		tbServicioCampoBusqueda = new DataTableFXMLUtil<ServicioCampoBusquedaDTO, ServicioCampoBusquedaDTO>(
				paginatorMarcadores, lstServicioCampo) {

			@Override
			public Integer getTotalRows(ServicioCampoBusquedaDTO filter) {
				Integer cantidad = 0;
				try {
					cantidad = configMarcadorServicio.cantidadServiciosCampoBusqueda(filter.getConfiguracion());
					cantidad += serviciosCampoBusqueda.size();
					totalServicio.setText("Total: " + cantidad);
					columna.setText(String.valueOf(cantidad + 1));
				} catch (MarcadorServicioException e) {
					error(e);
				}
				return 0;
			}

			@Override
			public List<ServicioCampoBusquedaDTO> getList(ServicioCampoBusquedaDTO filter, Integer page, Integer rows) {
				List<ServicioCampoBusquedaDTO> lista = new ArrayList<ServicioCampoBusquedaDTO>();
				try {
					lista = configMarcadorServicio.getServiciosCampoBusqueda(filter.getConfiguracion(), page - 1, rows);
					lista.addAll(serviciosCampoBusqueda);
				} catch (MarcadorServicioException e) {
					error(e);
				}
				return lista;
			}

			@Override
			public ServicioCampoBusquedaDTO getFilter() {
				ServicioCampoBusquedaDTO dto = new ServicioCampoBusquedaDTO();
				var config = new ConfiguracionDTO();
				if (StringUtils.isNotBlank(configuracion.getText())) {
					config.setConfiguracion(configuracion.getText());
					dto.setConfiguracion(config);
				} else {
					config.setConfiguracion(".");
					dto.setConfiguracion(config);
				}
				return dto;
			}
		};
		tbMarcadorSerivicio = new DataTableFXMLUtil<MarcadorServicioDTO, MarcadorServicioDTO>(paginatorAsociar,
				lstMarcadoresCampos) {

			@Override
			public Integer getTotalRows(MarcadorServicioDTO filter) {
				Integer cantidad = 0;
				try {
					cantidad = configMarcadorServicio.cantidadMarcadorServicio(filter.getConfiguracion());
					cantidad += marcadoresServicios.size();
					totalAsociar.setText("Total: " + cantidad);
				} catch (MarcadorServicioException e) {
					error(e);
				}
				return cantidad;
			}

			@Override
			public List<MarcadorServicioDTO> getList(MarcadorServicioDTO filter, Integer page, Integer rows) {
				List<MarcadorServicioDTO> lista = new ArrayList<MarcadorServicioDTO>();
				try {
					lista = configMarcadorServicio.getMarcadorServicio(filter.getConfiguracion());
					lista.addAll(marcadoresServicios);
				} catch (MarcadorServicioException e) {
					error(e);
				}
				return lista;
			}

			@Override
			public MarcadorServicioDTO getFilter() {
				MarcadorServicioDTO dto = new MarcadorServicioDTO();
				if (StringUtils.isNotBlank(configuracion.getText())) {
					dto.setConfiguracion(getConfigurationForm());
				} else {
					var config = new ConfiguracionDTO();
					config.setConfiguracion(".");
					dto.setConfiguracion(config);
				}
				return dto;
			}
		};
		tbMarcador = new DataTableFXMLUtil<MarcadorDTO, MarcadorDTO>(paginatorMarcadores, lstMarcadores) {

			@Override
			public Integer getTotalRows(MarcadorDTO filter) {
				Integer cantidad = 0;
				try {
					cantidad = configMarcadorServicio.cantidadMarcador(filter.getConfiguracion());
					cantidad += marcadores.size();
					totalMarcador.setText("Total: " + cantidad);
				} catch (MarcadorServicioException e) {
					error(e);
				}
				return cantidad;
			}

			@Override
			public List<MarcadorDTO> getList(MarcadorDTO filter, Integer page, Integer rows) {
				List<MarcadorDTO> lista = new ArrayList<MarcadorDTO>();
				try {
					lista = configMarcadorServicio.getMarcador(filter.getConfiguracion());
					lista.addAll(marcadores);
				} catch (MarcadorServicioException e) {
					error(e);
				}
				return lista;
			}

			@Override
			public MarcadorDTO getFilter() {
				MarcadorDTO dto = new MarcadorDTO();
				if (StringUtils.isNotBlank(configuracion.getText())) {
					dto.setConfiguracion(getConfigurationForm());
				} else {
					var config = new ConfiguracionDTO();
					config.setConfiguracion(".");
					dto.setConfiguracion(config);
				}
				return dto;
			}
		};

	}

	@SuppressWarnings({ "rawtypes", "unchecked", "static-access" })
	private void loadServices() {
		try {
			var pathServices = StringUtils.isNotBlank(servicePackageList) ? servicePackageList
					: AppConstants.PATH_LIST_SERVICE;
			listServices = (AbstractListFromProccess) this.getClass().forName(pathServices).getConstructor()
					.newInstance();
			lstServicios.converterProperty().set(new StringConverter<ServicePOJO>() {
				@Override
				public String toString(ServicePOJO object) {
					try {
						return object.getAlias() + ConfigServiceConstant.SEP_2_DOT + object.getDescription();
					} catch (Exception e) {
						error(e);
					}
					return null;
				}

				@Override
				public ServicePOJO fromString(String string) {
					try {
						for (ServicePOJO service : listServices.getList()) {
							if ((service.getAlias() + ConfigServiceConstant.SEP_2_DOT + service.getDescription())
									.equalsIgnoreCase(string)) {
								return service;
							}
						}
					} catch (Exception e) {
						error(e);
					}
					return null;
				}
			});
			lstServicios.getSelectionModel().selectedItemProperty()
					.addListener((ObservableValue<? extends ServicePOJO> observable, ServicePOJO oldVal,
							ServicePOJO newVal) -> putCamposServicio(newVal));
		} catch (Exception e) {
			error(e);
		}
	}

	/**
	 * Se encarga de filtrar la lista y obtener los registros que se necesitan segun
	 * el typo del servicio {@link Services#Type}
	 * 
	 * @return
	 * @throws Exception
	 */
	private final List<ServicePOJO> listFilter() throws Exception {
		Services.Type type = null;
		if (rbCargues.isSelected()) {
			type = Services.Type.CREATE;
		} else if (rbReportes.isSelected()) {
			type = Services.Type.DOWNLOAD;
		} else {
			type = Services.Type.UPLOAD;
		}
		List<ServicePOJO> listOut = new ArrayList<ServicePOJO>();
		List<ServicePOJO> list = listServices.getList();
		for (ServicePOJO sp : list) {
			// logger.info(type + " " + sp.getType());
			if (sp.getType() == type) {
				listOut.add(sp);
			}
		}
		return listOut;
	}

	private void configColmn() {
		colMarcador.setCellValueFactory(e -> new SimpleStringProperty(e.getValue().getMarcador()));
		colInOut.setCellValueFactory(e -> new SimpleStringProperty(e.getValue().getTipoInOut()));
		orden.setCellValueFactory(e -> new SimpleIntegerProperty(e.getValue().getOrden()).asObject());

	}

	private void hiddenTabs() {
		tabConfigurar.setDisable(false);
		tabServicios.setDisable(true);
		tabAsociaciones.setDisable(true);
	}

	private void hiddenBtns() {
		btnAnterior.setVisible(false);
		btnEliminar.setVisible(false);
		btnDelServicio.setVisible(false);
		btnDelMarcador.setVisible(false);
		btnSaveMarcador.setVisible(false);
	}

	private void tabView() {
		tabMarcadores.setDisable(posicion.compareTo(ConfigServiceConstant.TAB_MARCADOR) != 0);
		tabServicios.setDisable(posicion.compareTo(ConfigServiceConstant.TAB_SERVICIO_CAMPO) != 0);
		if (rbReportes.isSelected())
			tabAsociaciones.setDisable(posicion.compareTo(ConfigServiceConstant.TAB_ASOCIAR_MARCADOR) != 0);
		tabConfigurar.setDisable(posicion.compareTo(ConfigServiceConstant.TAB_CONFIGURACION) != 0);
		if (posicion.compareTo(ConfigServiceConstant.TAB_MARCADOR) == 0) {
			tabMarcadores.getTabPane().getSelectionModel().select(tabMarcadores);
		}
		if (posicion.compareTo(ConfigServiceConstant.TAB_SERVICIO_CAMPO) == 0) {
			tabServicios.getTabPane().getSelectionModel().select(tabServicios);
			loadTabServicioBusqueda();
		}
		if (posicion.compareTo(ConfigServiceConstant.TAB_ASOCIAR_MARCADOR) == 0) {
			tabAsociaciones.getTabPane().getSelectionModel().select(tabAsociaciones);
			loadTabAsociaciones();
		}
		if (posicion.compareTo(ConfigServiceConstant.TAB_CONFIGURACION) == 0) {
			tabConfigurar.getTabPane().getSelectionModel().select(tabConfigurar);
		}
	}

	/** Se encarga de configurar la carga del tab del servicio de busqueda **/
	private void loadTabServicioBusqueda() {
		try {
			List<MarcadorDTO> markout = new ArrayList<MarcadorDTO>();
			for (MarcadorDTO dto : tbMarcador.getList()) {
				if (dto.getTipoInOut().equalsIgnoreCase(ConfigServiceConstant.TYPE_IN))
					markout.add(dto);
			}
			SelectList.put(lstMarcadorIn, markout, ConfigServiceConstant.MARK);
			SelectList.put(lstServicios, listFilter());
		} catch (Exception e) {
			error(e);
		}
	}

	/** Se encarga de configurar la carga del tab de asociacion **/
	private void loadTabAsociaciones() {
		servicios.clear();
		Set<String> service = new HashSet<String>();
		for (ServicioCampoBusquedaDTO scb : tbServicioCampoBusqueda.getList()) {
			service.add(scb.getServicio());
		}
		List<MarcadorDTO> markout = new ArrayList<MarcadorDTO>();
		for (MarcadorDTO dto : tbMarcador.getList()) {
			if (dto.getTipoInOut().equalsIgnoreCase(ConfigServiceConstant.TYPE_OUT))
				markout.add(dto);
		}
		servicios.addAll(service);
		SelectList.put(servicio, servicios);
		SelectList.put(marcador, markout, ConfigServiceConstant.MARK);
		servicio.getSelectionModel().selectedItemProperty().addListener((ObservableValue<? extends String> observable,
				String oldval, String newVal) -> putCamposServicio(newVal));
	}

	public <T extends Object, S extends ADto> void putCamposServicio(String service) {
		try {
			if (service.contentEquals(AppConstants.SELECCIONE))
				return;
			campos.clear();
			campos.addAll(SearchService.getInstance().getCampos(service));
			SelectList.put(campo, campos);
		} catch (Exception e) {
			mensajeIzquierdo(e);
		}
	}

	public void addMarcador() {
		if (posicion.compareTo(ConfigServiceConstant.TAB_MARCADOR) != 0)
			return;
		String marcador = nameMarcador.getText();
		if (StringUtils.isNotBlank(marcador)) {
			MarcadorDTO marca = new MarcadorDTO();
			marca.setMarcador(marcador);
			marca.setOrden(Integer.valueOf(orderMarcador.getText()));
			RadioButton toggle = (RadioButton) group.getSelectedToggle();
			marca.setTipoInOut(toggle.getText());
			marcadores.add(marca);
			nameMarcador.clear();
			tbMarcador.search();
			btnLimpiar.setVisible(false);
			orderMarcador.setText(String.valueOf(tbMarcador.getTotal() + 1));
		}
	}

	public final void updMarcador() {
		try {
			if (posicion.compareTo(ConfigServiceConstant.TAB_MARCADOR) != 0)
				return;
			var mark = tbMarcador.getSelectedRow();
			mark.setMarcador(nameMarcador.getText());
			mark.setOrden(Integer.valueOf(orderMarcador.getText()));
			RadioButton toggle = (RadioButton) group.getSelectedToggle();
			mark.setTipoInOut(toggle.getText());
			if (StringUtils.isNotBlank(mark.getCodigo())) {
				configMarcadorServicio.updateMarcador(mark, getUsuario());
			} else {
				for (var i = 0; i < marcadores.size(); i++) {
					if (marcadores.get(i).equals(mark)) {
						marcadores.set(i, mark);
						break;
					}
				}
			}
			nameMarcador.clear();
			orderMarcador.setText(String.valueOf(tbMarcador.getTotal() + 1));
			btnAddMark.setVisible(true);
			btnUpdateMark.setVisible(false);
			btnLimpiar.setVisible(false);
			tbMarcador.search();
		} catch (MarcadorServicioException e) {
			error(e);
		}

	}

	public final void cleanMarcador() {
		nameMarcador.clear();
		orderMarcador.setText(String.valueOf(tbMarcador.getTotal() + 1));
		btnAddMark.setVisible(true);
		btnUpdateMark.setVisible(false);
		btnLimpiar.setVisible(false);
	}

	public void delMarcador() {
		if (posicion.compareTo(ConfigServiceConstant.TAB_MARCADOR) != 0)
			return;
		MarcadorDTO marcador = tbMarcador.getSelectedRow();
		if (StringUtils.isNotBlank(marcador.getMarcador()) && StringUtils.isNotBlank(marcador.getCodigo())) {
			try {
				configMarcadorServicio.deleteMarcador(marcador, getUsuario());
			} catch (MarcadorServicioException e) {
				error(e);
			}
		} else if (StringUtils.isNotBlank(marcador.getMarcador()) && StringUtils.isBlank(marcador.getCodigo())) {
			marcadores.remove(marcador);
		}
		tbMarcador.search();
	}

	/** Se encarga de guardar la asociacion entre servicio y campo de busqueda **/
	public void addServicioCampo() {
		if (posicion.compareTo(ConfigServiceConstant.TAB_SERVICIO_CAMPO) != 0)
			return;

		ServicioCampoBusquedaDTO scb = new ServicioCampoBusquedaDTO();
		ServicePOJO s = SelectList.get(lstServicios);
		if (s != null) {
			if (SelectList.get(lstCampos).equalsIgnoreCase(AppConstants.SELECCIONE)) {
				error("No se selecciono ningun campo.");
				return;
			}
			scb.setCampo(SelectList.get(lstCampos));
			if (StringUtils.isBlank(columna.getText())) {
				error("No contiene la columna.");
				return;
			}
			scb.setColumna(Integer.valueOf(columna.getText()));
			scb.setServicio(s.getClasss().getSimpleName() + ConfigServiceConstant.SEP_2_DOT + s.getAlias());
			scb.setMarcador(SelectList.get(lstMarcadorIn));
			serviciosCampoBusqueda.add(scb);
			// Table.put(lstServicioCampo, serviciosCampoBusqueda);
			lstCampos.getSelectionModel().selectFirst();
			columna.setText(String.valueOf(lstMarcadores.getProperties().size()));
			tbServicioCampoBusqueda.search();
		}
	}

	/**
	 * Se encarga de eliminar un registro seleccionado en la tabla de asociacion
	 * servicio campo
	 */
	public void delServicioCampo() {
		if (posicion.compareTo(ConfigServiceConstant.TAB_SERVICIO_CAMPO) != 0)
			return;
		if (tbServicioCampoBusqueda.isSelected()) {
			List<ServicioCampoBusquedaDTO> lista = tbServicioCampoBusqueda.getSelectedRows();// Table.getSelectedRows(lstServicioCampo);
			if (lista.size() > 0) {
				for (ServicioCampoBusquedaDTO campo : lista) {
					if (StringUtils.isNotBlank(campo.getCodigo())) {
						try {
							configMarcadorServicio.deleteServicioCampo(campo, getUsuario());
						} catch (MarcadorServicioException e) {
							error(e);
						}
					} else {
						serviciosCampoBusqueda.remove(campo);
					}
				}
				// Table.put(lstServicioCampo, serviciosCampoBusqueda);
				columna.setText(String.valueOf(serviciosCampoBusqueda.size() + 1));
			}
			tbServicioCampoBusqueda.search();
		}
	}

	public void agregar() {
		MarcadorServicioDTO ms = new MarcadorServicioDTO();
		ms.setServicio(SelectList.get(servicio));
		ms.setMarcador(SelectList.get(marcador));
		ms.setNombreCampo(SelectList.get(campo));
		marcadoresServicios.add(ms);
		// Table.add(lstMarcadoresCampos, ms);
		campo.getSelectionModel().selectFirst();
		marcador.getSelectionModel().selectFirst();
		tbMarcadorSerivicio.search();
	}

	/**
	 * Se encarga de poner los campos del servicio usado.
	 * 
	 * @param service {@link ServicePOJO}
	 */
	public <T extends Object> void putCamposServicio(ServicePOJO service) {
		try {
			List<String> listaCampos = new ArrayList<String>();
			listaCampos = SearchService.getInstance().putCamposServicios(service);
			SelectList.put(lstCampos, listaCampos);
		} catch (SecurityException | SearchServicesException | IllegalArgumentException e) {
			error(e);
		}
	}

	private final void loadFields() throws Exception {
		if (StringUtils.isNotBlank(configuracion.getText())) {
			config.setConfiguracion(configuracion.getText());
		} else {
			throw new Exception("No se encontró el nombre de la configuración.");
		}
		if (StringUtils.isNotBlank(nameFiler.getText())) {
			config.setArchivo(nameFiler.getText());
		} else if (rbReportes.isSelected()) {
			throw new Exception("No se encontró el nombre del archivo.");
		}
		if (StringUtils.isNotBlank(nameFilerOut.getText())) {
			config.setArchivoSalida(nameFilerOut.getText());
		}
		if (StringUtils.isNotBlank(this.descripcion.getText())) {
			config.setDescripcion(descripcion.getText());
		}
		if (rbReportes.isSelected()) {
			config.setReport(true);
		}
		if (rbCargues.isSelected()) {
			config.setReport(false);
		}
	}

	private final void servicioCampoUPDINST() throws MarcadorServicioException {
		for (ServicioCampoBusquedaDTO servicio : serviciosCampoBusqueda) {
			servicio.setConfiguracion(config);
			if (StringUtils.isNotBlank(servicio.getCodigo())) {
				configMarcadorServicio.updateServicioCampo(servicio, getUsuario());
			} else if (StringUtils.isBlank(servicio.getCodigo())) {
				servicio = configMarcadorServicio.insertServicioCampoBusqueda(servicio, getUsuario());
			}
		}

	}

	private final void servicioMarcadorUPDINST() throws MarcadorServicioException {
		for (MarcadorServicioDTO marcador : marcadoresServicios) {
			marcador.setConfiguracion(config);
			if (StringUtils.isNotBlank(marcador.getCodigo())) {
				configMarcadorServicio.updateServicioMarcador(marcador, getUsuario());
			} else if (StringUtils.isBlank(marcador.getCodigo())) {
				marcador = configMarcadorServicio.insertMarcadorServicio(marcador, getUsuario());
			}
		}
	}

	private final ConfiguracionDTO getConfigurationForm() {
		var configuration = new ConfiguracionDTO();
		configuration.setConfiguracion(configuracion.getText());
		return configuration;
	}

	private final void marcadorUPDINST() throws MarcadorServicioException {
		for (MarcadorDTO marcador : marcadores) {
			marcador.setConfiguracion(config);
			if (StringUtils.isNotBlank(marcador.getCodigo())) {
				configMarcadorServicio.updateMarcador(marcador, getUsuario());
			} else if (StringUtils.isBlank(marcador.getCodigo())) {
				marcador = configMarcadorServicio.insertMarcador(marcador, getUsuario());
			}
		}
	}

	public final void saveAll() {
		try {
			loadFields();
			if (StringUtils.isNotBlank(config.getCodigo())) {
				configMarcadorServicio.updateConfiguracion(config, getUsuario());
			} else {
				config = configMarcadorServicio.insertConfiguracion(config, getUsuario());
			}
			servicioCampoUPDINST();
			servicioMarcadorUPDINST();
			marcadorUPDINST();
			this.notificar("Se guardaron los registros correctamente.");
		} catch (Exception e) {
			error(e);
		}
	}

	/**
	 * Se encarga de generar la consulta de la factura
	 */
	public final <T extends Object, M extends Object> void generar() {
		if (rbCargues.isSelected()) {
			loaders();
		} else if (rbReportes.isSelected()) {
			reports();
		}
	}

	public final <T extends Object, M extends Object> void loaders() {
		try {
			controllerPopup(LoaderServiceBean.class).load(config.getConfiguracion());
		} catch (LoadAppFxmlException e) {
			error(e);
		}
	}

	/**
	 * Se encarga de generar los repores
	 */
	public final <T extends Object, M extends Object> void reports() {
		try {
			controllerPopup(GenConfigBean.class).load(config.getConfiguracion());
		} catch (LoadAppFxmlException e) {
			error(e);
		}
	}

	public void guardar() {

	}

	/**
	 * Se encarga de eliminar el marcador asociado al campo del servicio
	 */
	public void eliminar() {
		if (posicion.compareTo(ConfigServiceConstant.TAB_ASOCIAR_MARCADOR) != 0)
			return;
		if (tbMarcadorSerivicio.isSelected()) {// Table.isSelected(lstMarcadoresCampos)) {
			List<MarcadorServicioDTO> lista = tbMarcadorSerivicio.getSelectedRows();// Table.getSelectedRows(lstMarcadoresCampos);
			if (lista.size() > 0) {
				for (MarcadorServicioDTO marcador : lista) {
					if (StringUtils.isNotBlank(marcador.getCodigo())) {
						try {
							configMarcadorServicio.deleteServicioMarcador(marcador, getUsuario());
						} catch (MarcadorServicioException e) {
							error(e);
						}
					} else {
						marcadoresServicios.remove(marcador);
					}
				}
			}
			// Table.put(lstMarcadoresCampos, marcadoresServicios);
			tbMarcadorSerivicio.search();
		}
	}

	private void mostrarBotonesMov() {
		btnSiguiente.setVisible(posicion < max);
		btnAnterior.setVisible(posicion > ConfigServiceConstant.TAB_MARCADOR);
	}

	public void anterior() {
		if (posicion > ConfigServiceConstant.TAB_MARCADOR) {
			posicion--;
		}
		if (!rbReportes.isSelected()) {
			if (posicion.compareTo(ConfigServiceConstant.TAB_ASOCIAR_MARCADOR) == 0) {
				posicion--;
			}
		}
		tabView();
		mostrarBotonesMov();
	}

	public void siguiente() {
		if (posicion < max) {
			posicion++;
		}
		if (!rbReportes.isSelected()) {
			if (posicion.compareTo(ConfigServiceConstant.TAB_ASOCIAR_MARCADOR) == 0) {
				posicion++;
			}
		}
		tabView();
		mostrarBotonesMov();
	}

	public void cancelar() {
		if (posicion == ConfigServiceConstant.TAB_MARCADOR) {
			getController(ListConfigBean.class);
		}
		posicion = ConfigServiceConstant.TAB_MARCADOR;
		marcadores.clear();
		servicios.clear();
		campos.clear();
		this.config = null;
		serviciosCampoBusqueda.clear();
		marcadoresServicios.clear();
		tabView();
	}

	protected void visibleButtons() {
		var save = PermissionUtil.INSTANCE().havePerm(PermissionConstants.CONST_PERM_CREATE, ListConfigBean.class,
				getUsuario().getGrupoUser());
		var edit = PermissionUtil.INSTANCE().havePerm(PermissionConstants.CONST_PERM_UPDATE, ListConfigBean.class,
				getUsuario().getGrupoUser());
		var delete = PermissionUtil.INSTANCE().havePerm(PermissionConstants.CONST_PERM_DELETE, ListConfigBean.class,
				getUsuario().getGrupoUser());
		btnGuardar.setVisible(save);
		btnUpdateMark.setVisible(edit);
		btnAddMark.setVisible(save);
		btnDelMarcador.setVisible(delete);
		btnSaveMarcador.setVisible(save);
		btnDelServicio.setVisible(delete);
	}
}
