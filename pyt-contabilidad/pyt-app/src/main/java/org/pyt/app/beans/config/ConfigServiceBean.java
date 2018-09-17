package org.pyt.app.beans.config;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.pyt.common.annotations.FXMLFile;
import org.pyt.common.annotations.Inject;
import org.pyt.common.common.ABean;
import org.pyt.common.common.ADto;
import org.pyt.common.common.SelectList;
import org.pyt.common.common.Table;
import org.pyt.common.constants.AppConstants;

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
import javafx.scene.control.Tab;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
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
	private List<MarcadorDTO> marcadores;
	private List<String> servicios;
	private List<String> campos;
	private List<ServicioCampoBusquedaDTO> serviciosCampoBusqueda;
	private List<MarcadorServicioDTO> marcadoresServicios;
	private List<ServicePOJO> serviciosPOJO;
	private Integer posicion;
	private Integer max;
	public final static Integer TAB_MARCADOR = 1;
	public final static Integer TAB_SERVICIO_CAMPO = 2;
	public final static Integer TAB_ASOCIAR_MARCADOR = 3;
	public final static Integer TAB_CONFIGURACION= 4;
	private AbstractListFromProccess<ServicePOJO> listServices;
	@Inject(resource = "com.pyt.service.implement.ConfigMarcadorServicioSvc")
	private IConfigMarcadorServicio configMarcadorServicio;
	private ConfiguracionDTO config;

	@FXML
	public void initialize() {
		marcadores = new ArrayList<MarcadorDTO>();
		servicios = new ArrayList<String>();
		campos = new ArrayList<String>();
		serviciosCampoBusqueda = new ArrayList<ServicioCampoBusquedaDTO>();
		marcadoresServicios = new ArrayList<MarcadorServicioDTO>();
		config = new ConfiguracionDTO();
		posicion = TAB_MARCADOR;
		max = TAB_CONFIGURACION;
		hiddenTabs();
		hiddenBtns();
		configColmn();
		loadServices();
		Table.put(lstServicioCampo, serviciosCampoBusqueda);
		columna.setText(String.valueOf(serviciosCampoBusqueda.size() + 1));
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
						return object.getAlias() + ":" + object.getDescription();
					} catch (Exception e) {
						error(e);
					}
					return null;
				}

				@Override
				public ServicePOJO fromString(String string) {
					try {
						for (ServicePOJO service : listServices.getList()) {
							if ((service.getAlias() + ":" + service.getDescription()).equalsIgnoreCase(string)) {
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
	}

	private void hiddenTabs() {
	    tabConfigurar.setDisable(true);
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
		tabMarcadores.setDisable(posicion.compareTo(TAB_MARCADOR) != 0);
		tabServicios.setDisable(posicion.compareTo(TAB_SERVICIO_CAMPO) != 0);
		tabAsociaciones.setDisable(posicion.compareTo(TAB_ASOCIAR_MARCADOR) != 0);
		tabConfigurar.setDisable(posicion.compareTo(TAB_CONFIGURACION) != 0);
		if (posicion.compareTo(TAB_MARCADOR) == 0) {
			tabMarcadores.getTabPane().getSelectionModel().select(tabMarcadores);
		}
		if (posicion.compareTo(TAB_SERVICIO_CAMPO) == 0) {
			tabServicios.getTabPane().getSelectionModel().select(tabServicios);
		}
		if (posicion.compareTo(TAB_ASOCIAR_MARCADOR) == 0) {
			tabAsociaciones.getTabPane().getSelectionModel().select(tabAsociaciones);
			loadTabAsociaciones();
		}
		if (posicion.compareTo(TAB_CONFIGURACION) == 0) {
			tabConfigurar.getTabPane().getSelectionModel().select(tabConfigurar);
		}
	}

	private void loadTabAsociaciones() {
		servicios.clear();
		Set<String> service = new HashSet<String>();
		for (ServicioCampoBusquedaDTO scb : serviciosCampoBusqueda) {
			service.add(scb.getServicio());
		}
		servicios.addAll(service);
		SelectList.put(servicio, servicios);
		SelectList.put(marcador, marcadores, "marcador");
		servicio.getSelectionModel().selectedItemProperty().addListener((ObservableValue<? extends String> observable,
				String oldval, String newVal) -> putCamposServicio(newVal));
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void putCamposServicio(String service) {
		try {
			if (service.contentEquals(AppConstants.SELECCIONE))
				return;
			Class[] parametros = null;
			for (ServicePOJO servicio : listServices.getList()) {
				if ((servicio.getClasss().getSimpleName() + ":" + servicio.getAlias()).contentEquals(service)) {
					try {
						parametros = new Class[servicio.getParameter().length];
						int i = 0;
						for (String parametro : servicio.getParameter()) {
							parametros[i] = Class.forName(parametro);
							i++;
						}
						Method metodo = servicio.getClasss().getDeclaredMethod(servicio.getName(), parametros);
						Class retorno = metodo.getReturnType();
						if (retorno.getConstructor().newInstance() instanceof ADto) {
							List<String> campos = (List<String>) retorno.getMethod(AppConstants.GET_NAME_FIELDS)
									.invoke(retorno.getConstructor().newInstance());
							for (String campo : campos) {
								this.campos.add(retorno.getSimpleName() + "::" + campo);
							}
						} else {
							campos.add(retorno.getSimpleName());
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
			error(e);
		}
	}

	public void addMarcador() {
		if (posicion.compareTo(TAB_MARCADOR) != 0)
			return;
		String marcador = nameMarcador.getText();
		if (StringUtils.isNotBlank(marcador)) {
			MarcadorDTO marca = new MarcadorDTO();
			marca.setMarcador(marcador);
			marcadores.add(marca);
			Table.put(lstMarcadores, marcadores);
			nameMarcador.clear();
		}
	}

	public void delMarcador() {
		if (posicion.compareTo(TAB_MARCADOR) != 0)
			return;
		String marcador = lstMarcadores.getSelectionModel().getSelectedItem().getMarcador();
		if (StringUtils.isNotBlank(marcador)) {
			marcadores.remove(marcador);
		}
	}

	public void addServicioCampo() {
		if (posicion.compareTo(TAB_SERVICIO_CAMPO) != 0)
			return;

		ServicioCampoBusquedaDTO scb = new ServicioCampoBusquedaDTO();
		ServicePOJO s = SelectList.get(lstServicios);
		if (s != null) {
			if (SelectList.get(lstCampos).equalsIgnoreCase(AppConstants.SELECCIONE)) {
				error("No se selecciono ningun campo.");
				return;
			}
			scb.setCampo(SelectList.get(lstCampos));
			scb.setColumna(Integer.valueOf(columna.getText()));
			scb.setServicio(s.getClasss().getSimpleName() + ":" + s.getAlias());
			serviciosCampoBusqueda.add(scb);
			Table.put(lstServicioCampo, serviciosCampoBusqueda);
			lstCampos.getSelectionModel().selectFirst();
			columna.setText(String.valueOf(serviciosCampoBusqueda.size() + 1));
		}
	}

	/**
	 * Se encarga de eliminar un registro seleccionado en la tabla de asociacion
	 * servicio campo
	 */
	public void delServicioCampo() {
		if (posicion.compareTo(TAB_SERVICIO_CAMPO) != 0)
			return;
		if (Table.isSelected(lstServicioCampo)) {
			List<ServicioCampoBusquedaDTO> lista = Table.getSelectedRows(lstServicioCampo);
			serviciosCampoBusqueda.removeAll(lista);
			Table.put(lstServicioCampo, serviciosCampoBusqueda);
			columna.setText(String.valueOf(serviciosCampoBusqueda.size() + 1));
		}
	}

	@SuppressWarnings({ "unchecked", })
	public <T extends Object> void putCamposServicio(ServicePOJO service) {
		try {
			List<String> listaCampos = new ArrayList<String>();
			Class<T> servicio = (Class<T>) service.getClasss();
			Method[] metodos = servicio.getDeclaredMethods();
			Method metodoUso = null;
			Boolean valid = true;
			for (Method metodo : metodos) {
				if (metodo.getName().equalsIgnoreCase(service.getName())) {
					Class<?>[] clases = metodo.getParameterTypes();
					for (Class<?> clase : clases) {
						for (String parametro : service.getParameter()) {
							if (clase.getCanonicalName().equalsIgnoreCase(parametro)) {
								valid &= true;
							}
						}
						if (valid) {
							metodoUso = metodo;
							break;
						}
					}
				}
			}
			if (metodoUso != null) {
				Class<?>[] parametros = metodoUso.getParameterTypes();
				for (Class<?> parametro : parametros) {
					if (parametro.getConstructor().newInstance() instanceof ADto) {
						List<String> campos = (List<String>) parametro.getMethod(AppConstants.GET_NAME_FIELDS)
								.invoke(parametro.getConstructor().newInstance());
						for (String campo : campos) {
							listaCampos.add(parametro.getSimpleName() + "::" + campo);
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
			config.setConfiguracion(configuracion.getText());
			for (ServicioCampoBusquedaDTO servicio : serviciosCampoBusqueda) {
				servicio.setConfiguracion(config.getConfiguracion());
				if (StringUtils.isNotBlank(servicio.getCodigo())) {
					configMarcadorServicio.updateServicioCampo(servicio, userLogin);
				} else if (StringUtils.isBlank(servicio.getCodigo())) {
					configMarcadorServicio.insertServicioCampoBusqueda(servicio, userLogin);
				}
			}
			for (MarcadorServicioDTO marcador : marcadoresServicios) {
				marcador.setConfiguracion(config.getConfiguracion());
				if (StringUtils.isNotBlank(marcador.getCodigo())) {
					configMarcadorServicio.updateServicioMarcador(marcador, userLogin);
				} else if (StringUtils.isBlank(marcador.getCodigo())) {
					configMarcadorServicio.insertMarcadorServicio(marcador, userLogin);
				}
			}
			for (MarcadorDTO marcador : marcadores) {
				marcador.setConfiguracion(config.getConfiguracion());
				if (StringUtils.isNotBlank(marcador.getCodigo())) {
					configMarcadorServicio.updateMarcador(marcador, userLogin);
				} else if (StringUtils.isBlank(marcador.getCodigo())) {
					configMarcadorServicio.insertMarcador(marcador, userLogin);
				}
			}
			if(StringUtils.isNotBlank(config.getCodigo())) {
				configMarcadorServicio.updateConfiguracion(config, userLogin);
			}else if(StringUtils.isBlank(config.getCodigo())){
				configMarcadorServicio.insertConfiguracion(config, userLogin);
			}
			this.notificar("Se guardaron los registros correctamente.");
		} catch (Exception e) {
			error(e);
		}
	}

	public final void load(ConfiguracionDTO configuracion) {
		try {
			config = configuracion;
			serviciosCampoBusqueda = configMarcadorServicio.getServiciosCampoBusqueda(configuracion.getConfiguracion());
			marcadoresServicios = configMarcadorServicio.getMarcadorServicio(configuracion.getConfiguracion());
			marcadores = configMarcadorServicio.getMarcador(configuracion.getConfiguracion());
		} catch (Exception e) {
			error(e);
		}
	}

	public final void generar() {

	}

	public void agregar() {
		MarcadorServicioDTO ms = new MarcadorServicioDTO();
		ms.setServicio(SelectList.get(servicio));
		ms.setMarcador(SelectList.get(marcador));
		ms.setNombreCampo(SelectList.get(campo));
		marcadoresServicios.add(ms);
		Table.add(lstMarcadoresCampos, ms);
		campo.getSelectionModel().selectFirst();
		marcador.getSelectionModel().selectFirst();
	}

	public void guardar() {

	}

	public void eliminar() {
		if (posicion.compareTo(TAB_ASOCIAR_MARCADOR) != 0)
			return;
		if (Table.isSelected(lstMarcadoresCampos)) {
			List<MarcadorServicioDTO> lista = Table.getSelectedRows(lstMarcadoresCampos);
			marcadoresServicios.removeAll(lista);
			Table.put(lstMarcadoresCampos, marcadoresServicios);
		}
	}

	private void mostratBotonesMov() {
		btnSiguiente.setVisible(posicion < max);
		btnAnterior.setVisible(posicion > TAB_MARCADOR);
	}

	public void anterior() {
		if (posicion > TAB_MARCADOR) {
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
		posicion = TAB_MARCADOR;
		marcadores.clear();
		servicios.clear();
		campos.clear();
		serviciosCampoBusqueda.clear();
		marcadoresServicios.clear();
		tabView();
	}
}
