package org.pyt.app.beans.config;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.pyt.app.components.DataTableFXML;
import org.pyt.common.annotations.FXMLFile;
import org.pyt.common.annotations.Inject;
import org.pyt.common.common.ABean;
import org.pyt.common.common.ADto;
import org.pyt.common.common.LoadAppFxml;
import org.pyt.common.common.SelectList;
import org.pyt.common.constants.AppConstants;
import org.pyt.common.constants.ConfigServiceConstant;
import org.pyt.common.exceptions.LoadAppFxmlException;
import org.pyt.common.exceptions.MarcadorServicioException;

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

import co.com.arquitectura.librerias.implement.Services.ServicePOJO;
import co.com.arquitectura.librerias.implement.listProccess.AbstractListFromProccess;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Tab;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
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
	private Button btnCancelar;
	@FXML
	private Button btnDelServicio;
	@FXML
	private Button btnSaveMarcador;
	@FXML
	private Button btnDelMarcador;
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
	private TextField columna;
	@FXML
	private TextField configuracion;
	@FXML
	private TableView<MarcadorDTO> lstMarcadores;
	@FXML
	private TableColumn<MarcadorDTO, String> colMarcador;
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
	private ToggleGroup group;
	private List<MarcadorDTO> marcadores;
	private List<String> servicios;
	private List<String> campos;
	private List<ServicioCampoBusquedaDTO> serviciosCampoBusqueda;
	private List<MarcadorServicioDTO> marcadoresServicios;
	private List<ServicePOJO> serviciosPOJO;
	private Integer posicion;
	private Integer max;

	private AbstractListFromProccess<ServicePOJO> listServices;
	@Inject(resource = "com.pyt.service.implement.ConfigMarcadorServicioSvc")
	private IConfigMarcadorServicio configMarcadorServicio;
	private ConfiguracionDTO config;
	private DataTableFXML<ServicioCampoBusquedaDTO, ServicioCampoBusquedaDTO> tbServicioCampoBusqueda;
	private DataTableFXML<MarcadorServicioDTO, MarcadorServicioDTO> tbMarcadorSerivicio;
	private DataTableFXML<MarcadorDTO, MarcadorDTO> tbMarcador;
	@FXML
	private RadioButton rbIn;
	@FXML
	private RadioButton rbOut;

	@FXML
	public void initialize() {
		marcadores = new ArrayList<MarcadorDTO>();
		servicios = new ArrayList<String>();
		campos = new ArrayList<String>();
		serviciosCampoBusqueda = new ArrayList<ServicioCampoBusquedaDTO>();
		marcadoresServicios = new ArrayList<MarcadorServicioDTO>();
		config = new ConfiguracionDTO();
		posicion = ConfigServiceConstant.TAB_MARCADOR;
		max = ConfigServiceConstant.TAB_CONFIGURACION;
		group = new ToggleGroup();
		rbIn.setToggleGroup(group);
		rbOut.setToggleGroup(group);
		rbOut.setSelected(true);
		hiddenTabs();
		hiddenBtns();
		configColmn();
		loadServices();
		// Table.put(lstServicioCampo, serviciosCampoBusqueda);
		columna.setText(String.valueOf(serviciosCampoBusqueda.size() + 1));
	}

	/**
	 * Se carga de realizar busqueda de los registros y carga las data tablas
	 * 
	 * @param configuracion
	 *            {@link ConfiguracionDTO}
	 */
	public final void load(ConfiguracionDTO configuracion) {
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
		tbServicioCampoBusqueda = new DataTableFXML<ServicioCampoBusquedaDTO, ServicioCampoBusquedaDTO>(
				paginatorMarcadores, lstServicioCampo) {

			@Override
			public Integer getTotalRows(ServicioCampoBusquedaDTO filter) {
				Integer cantidad = 0;
				try {
					cantidad = configMarcadorServicio.cantidadServiciosCampoBusqueda(filter.getConfiguracion());
					cantidad += serviciosCampoBusqueda.size();
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
				if (StringUtils.isNotBlank(configuracion.getText())) {
					dto.setConfiguracion(configuracion.getText());
				}
				return dto;
			}
		};
		tbMarcadorSerivicio = new DataTableFXML<MarcadorServicioDTO, MarcadorServicioDTO>(paginatorAsociar,
				lstMarcadoresCampos) {

			@Override
			public Integer getTotalRows(MarcadorServicioDTO filter) {
				Integer cantidad = 0;
				try {
					cantidad = configMarcadorServicio.cantidadMarcadorServicio(filter.getConfiguracion());
					cantidad += marcadoresServicios.size();
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
					dto.setConfiguracion(configuracion.getText());
				}
				return dto;
			}
		};
		tbMarcador = new DataTableFXML<MarcadorDTO, MarcadorDTO>(paginatorMarcadores, lstMarcadores) {

			@Override
			public Integer getTotalRows(MarcadorDTO filter) {
				Integer cantidad = 0;
				try {
					cantidad = configMarcadorServicio.cantidadMarcador(filter.getConfiguracion());
					cantidad += marcadores.size();
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
					dto.setConfiguracion(configuracion.getText());
				}
				return dto;
			}
		};
	}

	@SuppressWarnings({ "rawtypes", "unchecked", "static-access" })
	private void loadServices() {
		try {
			listServices = (AbstractListFromProccess) this.getClass().forName(AppConstants.PATH_LIST_SERVICE)
					.getConstructor().newInstance();
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
			SelectList.put(lstServicios, listServices.getList());
		} catch (Exception e) {
			error(e);
		}
	}

	private void configColmn() {
		colMarcador.setCellValueFactory(e -> new SimpleStringProperty(e.getValue().getMarcador()));
		colInOut.setCellValueFactory(e -> new SimpleStringProperty(e.getValue().getTipoInOut()));
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
		List<MarcadorDTO> markout = new ArrayList<MarcadorDTO>();
		for (MarcadorDTO dto : tbMarcador.getList()) {
			if (dto.getTipoInOut().equalsIgnoreCase(ConfigServiceConstant.TYPE_IN))
				markout.add(dto);
		}
		SelectList.put(lstMarcadorIn, markout, ConfigServiceConstant.MARK);
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

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public <T extends Object, S extends ADto> void putCamposServicio(String service) {
		try {
			if (service.contentEquals(AppConstants.SELECCIONE))
				return;
			Class[] parametros = null;
			campos.clear();
			for (ServicePOJO servicio : listServices.getList()) {
				if ((servicio.getClasss().getSimpleName() + ConfigServiceConstant.SEP_2_DOT + servicio.getAlias())
						.contentEquals(service)) {
					try {
						parametros = new Class[servicio.getParameter().length];
						int i = 0;
						for (String parametro : servicio.getParameter()) {
							parametros[i] = Class.forName(parametro);
							i++;
						}
						Method metodo = servicio.getClasss().getDeclaredMethod(servicio.getName(), parametros);
						Class retorno = metodo.getReturnType();

						if (retorno == List.class) {

							Type tipoRetorno = metodo.getGenericReturnType();
							if (tipoRetorno instanceof ParameterizedType) {
								ParameterizedType type = (ParameterizedType) tipoRetorno;
								Type[] typeArguments = type.getActualTypeArguments();
								for (Type typeArgument : typeArguments) {
									Class clase = (Class) typeArgument;
									T obj = (T) clase.getDeclaredConstructor().newInstance();
									if (obj instanceof ADto) {
										List<String> campos = (List<String>) clase
												.getMethod(AppConstants.GET_NAME_FIELDS)
												.invoke(clase.getConstructor().newInstance());
										for (String campo : campos) {
											this.campos.add(clase.getSimpleName() + "::" + campo);
										}
									} else {
										campos.add(retorno.getSimpleName());
									}
								}
							}

						} else {
							T obj = (T) retorno.getDeclaredConstructor().newInstance();
							if (obj instanceof ADto) {
								List<String> campos = (List<String>) retorno.getMethod(AppConstants.GET_NAME_FIELDS)
										.invoke(retorno.getConstructor().newInstance());
								for (String campo : campos) {
									this.campos.add(retorno.getSimpleName() + ConfigServiceConstant.SEP_2_DOTS + campo);
								}
							} else {
								campos.add(retorno.getSimpleName());
							}
						}
					} catch (ClassNotFoundException e) {
						throw new Exception(e);
					} catch (NoSuchMethodException e) {
						throw new Exception(e);
					} catch (SecurityException e) {
						throw new Exception(e);
					} catch (InstantiationException e) {
						throw new Exception(e);
					} catch (IllegalAccessException e) {
						throw new Exception(e);
					} catch (IllegalArgumentException e) {
						throw new Exception(e);
					} catch (InvocationTargetException e) {
						throw new Exception(e);
					}
					break;
				}
			}
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
			RadioButton toggle = (RadioButton) group.getSelectedToggle();
			marca.setTipoInOut(toggle.getText());
			marcadores.add(marca);
			// Table.put(lstMarcadores, marcadores);
			nameMarcador.clear();
			tbMarcador.search();
		}
	}

	public void delMarcador() {
		if (posicion.compareTo(ConfigServiceConstant.TAB_MARCADOR) != 0)
			return;
		MarcadorDTO marcador = tbMarcador.getSelectedRow();
		if (StringUtils.isNotBlank(marcador.getMarcador()) && StringUtils.isNotBlank(marcador.getCodigo())) {
			try {
				configMarcadorServicio.deleteMarcador(marcador, userLogin);
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
							configMarcadorServicio.deleteServicioCampo(campo, userLogin);
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
	 * Se encarga de obtener el metodo del servicio segun los datos suministrados
	 * 
	 * @param cservice
	 *            {@link Class}
	 * @param nombre
	 *            {@link String}
	 * @param parametros
	 *            {@link String}[]
	 * @return {@link Method}
	 */
	@SuppressWarnings("unused")
	private <S extends Object> Method getMethod(Class<S> cservice, String nombre, String[] parametros) {
		Boolean valid = true;
		if (cservice != null) {
			List<String> listaCampos = new ArrayList<String>();
			Method[] metodos = cservice.getDeclaredMethods();
			for (Method metodo : metodos) {
				if (metodo.getName().equalsIgnoreCase(nombre)) {
					Class<?>[] clases = metodo.getParameterTypes();
					for (Class<?> clase : clases) {
						for (String parametro : parametros) {
							if (clase.getCanonicalName().equalsIgnoreCase(parametro)) {
								valid &= true;
							} else {
								valid &= false;
							}
						}
					}
					if (valid) {
						return metodo;
					} else {
						valid = true;
					}
				}
			}
		}
		return null;
	}

	@SuppressWarnings({ "unchecked", })
	public <T extends Object> void putCamposServicio(ServicePOJO service) {
		try {
			List<String> listaCampos = new ArrayList<String>();
			Method metodoUso = getMethod(service.getClasss(), service.getName(), service.getParameter());
			if (metodoUso != null) {
				Class<?>[] parametros = metodoUso.getParameterTypes();
				for (Class<?> parametro : parametros) {
					if (parametro.getConstructor().newInstance() instanceof ADto) {
						List<String> campos = (List<String>) parametro.getMethod(AppConstants.GET_NAME_FIELDS)
								.invoke(parametro.getConstructor().newInstance());
						for (String campo : campos) {
							listaCampos.add(parametro.getSimpleName() + ConfigServiceConstant.SEP_2_DOTS + campo);
						}
					} else {
						listaCampos.add(parametro.getSimpleName());
					}

				}
			}
			SelectList.put(lstCampos, listaCampos);
		} catch (SecurityException | ClassNotFoundException | IllegalAccessException | IllegalArgumentException
				| InvocationTargetException | NoSuchMethodException | InstantiationException e) {
			error(e);
		}
	}

	public final void saveAll() {
		try {
			if (StringUtils.isNotBlank(configuracion.getText())) {
				config.setConfiguracion(configuracion.getText());
			} else {
				this.notificar("No se encontró el nombre de la configuración.");
				return;
			}
			if (StringUtils.isNotBlank(nameFiler.getText())) {
				config.setArchivo(nameFiler.getText());
			} else {
				this.notificar("No se encontró el nombre del archivo.");
				return;
			}
			for (ServicioCampoBusquedaDTO servicio : serviciosCampoBusqueda) {
				servicio.setConfiguracion(config.getConfiguracion());
				if (StringUtils.isNotBlank(servicio.getCodigo())) {
					configMarcadorServicio.updateServicioCampo(servicio, userLogin);
				} else if (StringUtils.isBlank(servicio.getCodigo())) {
					servicio = configMarcadorServicio.insertServicioCampoBusqueda(servicio, userLogin);
				}
			}
			for (MarcadorServicioDTO marcador : marcadoresServicios) {
				marcador.setConfiguracion(config.getConfiguracion());
				if (StringUtils.isNotBlank(marcador.getCodigo())) {
					configMarcadorServicio.updateServicioMarcador(marcador, userLogin);
				} else if (StringUtils.isBlank(marcador.getCodigo())) {
					marcador = configMarcadorServicio.insertMarcadorServicio(marcador, userLogin);
				}
			}
			for (MarcadorDTO marcador : marcadores) {
				marcador.setConfiguracion(config.getConfiguracion());
				if (StringUtils.isNotBlank(marcador.getCodigo())) {
					configMarcadorServicio.updateMarcador(marcador, userLogin);
				} else if (StringUtils.isBlank(marcador.getCodigo())) {
					marcador = configMarcadorServicio.insertMarcador(marcador, userLogin);
				}
			}
			if (StringUtils.isNotBlank(config.getCodigo())) {
				configMarcadorServicio.updateConfiguracion(config, userLogin);
			} else if (StringUtils.isBlank(config.getCodigo())) {
				config = configMarcadorServicio.insertConfiguracion(config, userLogin);
			}
			this.notificar("Se guardaron los registros correctamente.");
		} catch (Exception e) {
			error(e);
		}
	}

	/**
	 * Se encarga de generar la consulta de la factura
	 */
	@SuppressWarnings("unchecked")
	public final <T extends Object, M extends Object> void generar() {
		GenConfigBean gcb;
		try {
			gcb = LoadAppFxml.loadBeanFxml(new Stage(), GenConfigBean.class);
			gcb.load(config.getConfiguracion());
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
							configMarcadorServicio.deleteServicioMarcador(marcador, userLogin);
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

	private void mostratBotonesMov() {
		btnSiguiente.setVisible(posicion < max);
		btnAnterior.setVisible(posicion > ConfigServiceConstant.TAB_MARCADOR);
	}

	public void anterior() {
		if (posicion > ConfigServiceConstant.TAB_MARCADOR) {
			posicion--;
		}
		tabView();
		mostratBotonesMov();
	}

	public void siguiente() {
		if (posicion < max) {
			posicion++;
		}
		tabView();
		mostratBotonesMov();
	}

	public void cancelar() {
		if (posicion == ConfigServiceConstant.TAB_MARCADOR) {
			getController(ListConfigBean.class);
		}
		posicion = ConfigServiceConstant.TAB_MARCADOR;
		marcadores.clear();
		servicios.clear();
		campos.clear();
		serviciosCampoBusqueda.clear();
		marcadoresServicios.clear();
		tabView();
	}
}
