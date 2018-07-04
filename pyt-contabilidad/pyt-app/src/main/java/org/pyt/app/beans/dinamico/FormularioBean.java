package org.pyt.app.beans.dinamico;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.pyt.app.components.DataTableFXML;
import org.pyt.common.annotations.FXMLFile;
import org.pyt.common.annotations.Inject;
import org.pyt.common.common.ABean;
import org.pyt.common.common.ADto;
import org.pyt.common.common.Compare;
import org.pyt.common.common.SelectList;
import org.pyt.common.exceptions.DocumentosException;
import org.pyt.common.exceptions.ParametroException;

import com.pyt.service.dto.ActividadIcaDTO;
import com.pyt.service.dto.BancoDTO;
import com.pyt.service.dto.CentroCostoDTO;
import com.pyt.service.dto.DocumentoDTO;
import com.pyt.service.dto.DocumentosDTO;
import com.pyt.service.dto.EmpresaDTO;
import com.pyt.service.dto.ParametroDTO;
import com.pyt.service.dto.RepuestoDTO;
import com.pyt.service.dto.ServicioDTO;
import com.pyt.service.dto.TrabajadorDTO;
import com.pyt.service.interfaces.IDocumentosSvc;
import com.pyt.service.interfaces.IParametrosSvc;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;

@FXMLFile(path = "view/dinamico", file = "documento.fxml", nombreVentana = "Formulario Dinamico")
public class FormularioBean extends ABean<DocumentosDTO> {
	@Inject(resource = "com.pyt.service.implement.DocumentosSvc")
	private IDocumentosSvc documentoSvc;
	@Inject(resource = "com.pyt.service.implement.ParametrosSvc")
	private IParametrosSvc parametroSvc;
	private DataTableFXML<DocumentosDTO, DocumentosDTO> dataTable;
	@FXML
	private Button guardar;
	@FXML
	private Button cancelar;
	@FXML
	private Button addItem;
	@FXML
	private Button modifyItem;
	@FXML
	private Button clearItem;
	@FXML
	private Button delItem;
	@FXML
	private TableView<DocumentosDTO> tabla;
	@FXML
	private ChoiceBox<String> tipoDocumento;
	@FXML
	private ChoiceBox<String> nombreCampo;
	@FXML
	private ChoiceBox<String> grupo;
	@FXML
	private ChoiceBox<String> busqueda;
	@FXML
	private ChoiceBox<String> campoMostrar;
	@FXML
	private ChoiceBox<String> campoAsignar;
	@FXML
	private ChoiceBox<String> controlar;
	@FXML
	private TextField fieldLabel;
	@FXML
	private CheckBox editable;
	@FXML
	private CheckBox obligatorio;
	@FXML
	private CheckBox valor;
	@FXML
	private CheckBox manejaLista;
	@FXML
	private CheckBox manejaGrupo;
	@FXML
	private HBox paginator;
	@FXML
	private Label etiqueta;
	@FXML
	private Label lCampoMostrar;
	@FXML
	private Label lCampoAsignar;
	@FXML
	private Label lGrupo;
	@FXML
	private Label lNombreCampo;
	@FXML
	private Label lBusqueda;
	private List<DocumentosDTO> documentos;
	private List<ParametroDTO> listTipoDocumento;
	private List<ParametroDTO> listGrupo;
	private Map<String, Object> mapa_controlar;
	private Map<String, Object> mapa_nombreCampo;
	private Map<String, Object> mapa_campoMostrar;
	private Map<String, Object> mapa_campoAsignar;
	private Map<String, Object> mapa_claseBusqueda;
	private Boolean instans;
	private final static String FIELD_NAME = "nombre";

	@FXML
	public void initialize() {
		instans = false;
		documentos = new ArrayList<DocumentosDTO>();
		mapa_controlar = new HashMap<String, Object>();
		claseControl();
		mapa_nombreCampo = new HashMap<String, Object>();
		mapa_campoAsignar = new HashMap<String, Object>();
		mapa_claseBusqueda = new HashMap<String, Object>();
		mapa_campoMostrar = new HashMap<String, Object>();
		ParametroDTO grupo = new ParametroDTO();
		grupo.setGrupo("*");
		ParametroDTO tipoDoc = new ParametroDTO();
		try {
			listGrupo = parametroSvc.getAllParametros(grupo);
			listTipoDocumento = parametroSvc.getAllParametros(tipoDoc);
		} catch (ParametroException e) {
			error(e);
		}
		SelectList.put(this.grupo, listGrupo, FIELD_NAME);
		SelectList.put(this.tipoDocumento, listTipoDocumento, FIELD_NAME);
		SelectList.put(controlar, mapa_controlar);
		controlar.getSelectionModel().selectFirst();
		this.grupo.getSelectionModel().selectFirst();
		this.tipoDocumento.getSelectionModel().selectFirst();
		this.nombreCampo.getSelectionModel().selectFirst();
		this.editable.setSelected(true);
		tabla.onMouseClickedProperty().set(e -> tablaClick());
		tipoDocumento.onActionProperty().set(e -> tipoDocumento());
		controlar.onActionProperty().set(e -> controla());
		nombreCampo.onActionProperty().set(e -> campo());
		claseBusquedas();
		SelectList.put(busqueda, mapa_claseBusqueda);
		busqueda.getSelectionModel().selectFirst();
		busqueda.onActionProperty().set(e -> busqueda());
		manejaLista();
		manejaGrupo();
		campo();
		nombreCampo.setVisible(false);
		etiqueta.setVisible(false);
		lNombreCampo.setVisible(false);
		fieldLabel.setVisible(false);
		guardar.setVisible(false);
		cancelar.setVisible(false);
		lazy();
	}

	private final void claseControl() {
		mapa_controlar.put("Documentos", DocumentoDTO.class);
	}

	private final void claseBusquedas() {
		mapa_claseBusqueda.put("Parametros", ParametroDTO.class);
		mapa_claseBusqueda.put("Banco", BancoDTO.class);
		mapa_claseBusqueda.put("Actividad Ica", ActividadIcaDTO.class);
		mapa_claseBusqueda.put("Centro de Costo", CentroCostoDTO.class);
		mapa_claseBusqueda.put("Empresa", EmpresaDTO.class);
		mapa_claseBusqueda.put("Trabajador", TrabajadorDTO.class);
		mapa_claseBusqueda.put("Repuestos", RepuestoDTO.class);
		mapa_claseBusqueda.put("Servicios", ServicioDTO.class);
	}

	public final void tablaClick() {
		DocumentosDTO docs = dataTable.getSelectedRow();
		if (docs != null) {
			SelectList.selectItem(nombreCampo, mapa_nombreCampo, docs.getFieldName());
			fieldLabel.setText(docs.getFieldLabel());
			editable.setSelected(docs.getEdit());
			obligatorio.setSelected(docs.getNullable());
			SelectList.selectItem(busqueda, mapa_claseBusqueda, docs.getObjectSearchDto());
			SelectList.selectItem(grupo, listGrupo, FIELD_NAME, docs, "selectNameGroup");
			SelectList.selectItem(campoMostrar, mapa_campoMostrar, docs.getPutNameShow());
			SelectList.selectItem(campoAsignar, mapa_campoAsignar, docs.getPutNameAssign());
		}else {
			notificar("No se ha seleccionado ningun registro.");
		}
	}

	public final void manejaLista() {
		busqueda.setVisible(manejaLista.isSelected());
		manejaGrupo.setVisible(manejaLista.isSelected());
		lCampoAsignar.setVisible(manejaLista.isSelected());
		campoAsignar.setVisible(manejaLista.isSelected());
		lCampoMostrar.setVisible(manejaLista.isSelected());
		campoMostrar.setVisible(manejaLista.isSelected());
		lBusqueda.setVisible(manejaLista.isSelected());
	}

	public final void manejaGrupo() {
		grupo.setVisible(manejaGrupo.isSelected());
		lGrupo.setVisible(manejaGrupo.isSelected());
	}

	@SuppressWarnings("unchecked")
	public final <T extends ADto> void controla() {
		Class<T> clase = (Class<T>) SelectList.get(controlar, mapa_controlar);
		getNameFields(clase);
		SelectList.put(nombreCampo, mapa_nombreCampo);
		nombreCampo.getSelectionModel().selectFirst();
		nombreCampo.setVisible(clase != null);
		fieldLabel.setVisible(clase != null);
		etiqueta.setVisible(clase != null);
		lNombreCampo.setVisible(clase != null);
		campo();
		dataTable.search();
	}

	@SuppressWarnings("unchecked")
	public final <T extends ADto> void busqueda() {
		Class<T> clase = (Class<T>) SelectList.get(busqueda, mapa_claseBusqueda);
		getFieldShow(clase);
		SelectList.put(campoMostrar, mapa_campoMostrar);
		SelectList.put(campoAsignar, mapa_campoAsignar);
		campoMostrar.getSelectionModel().selectFirst();
		campoAsignar.getSelectionModel().selectFirst();

	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public final <T extends ADto> void campo() {
		instans = false;
		Object obj = SelectList.get(nombreCampo, mapa_nombreCampo);
		Class campo = null;
		Boolean param = false;
		if (obj instanceof String) {
			Class control = (Class) SelectList.get(controlar, mapa_controlar);
			if (control != null) {
				try {
					T instancia = ((Class<T>) control).getConstructor().newInstance();
					campo = instancia.getType((String) obj);
					Object inst = campo.getConstructor().newInstance();
					if (inst instanceof ADto) {
						if (inst instanceof ParametroDTO) {
							param = true;
						}
						instans = true;
					}
				} catch (NoSuchMethodException e) {
					instans = false;
					param = false;
				} catch (Exception e) {
					error(e);
				}
			}
		}
		if (obj != null) {
			editable.setVisible(true);
			obligatorio.setVisible(true);
			if (!instans) {
				manejaLista.setVisible(true);
				lCampoMostrar.setVisible(false);
				campoMostrar.setVisible(false);
				grupo.setVisible(false);
				lGrupo.setVisible(false);
			} else {
				manejaLista.setVisible(false);
				lCampoMostrar.setVisible(true);
				campoMostrar.setVisible(true);
				mapa_campoMostrar.clear();
				getFieldShow(campo);
				SelectList.put(campoMostrar, mapa_campoMostrar);
				campoMostrar.getSelectionModel().selectFirst();
				grupo.setVisible(param);
				lGrupo.setVisible(param);
			}
			addItem.setVisible(true);
			modifyItem.setVisible(true);
			delItem.setVisible(true);
			clearItem.setVisible(true);
		} else {
			editable.setVisible(false);
			obligatorio.setVisible(false);
			manejaLista.setVisible(false);
			addItem.setVisible(false);
			modifyItem.setVisible(false);
			delItem.setVisible(false);
			clearItem.setVisible(false);
		}
	}

	public final void tipoDocumento() {
		dataTable.search();
	}

	/**
	 * Obtiene los nombres de los campos que se encuentran dentro del objeto
	 * principal
	 */
	private final <T extends ADto> void getNameFields(Class<T> clase) {
		Field[] fields = clase.getDeclaredFields();
		for (Field field : fields) {
			if (!Modifier.isStatic(field.getModifiers())) {
				mapa_nombreCampo.put(field.getName(), field.getName());
			}
		}
	}

	/**
	 * Obtiene los nombres de los campos que se encuentran dentro del objeto del
	 * cual se trabajara como lista
	 */
	private final <T extends ADto> void getFieldShow(Class<T> clase) {
		mapa_campoMostrar.clear();
		mapa_campoAsignar.clear();
		if (clase == null)
			return;
		Field[] fields = clase.getDeclaredFields();
		for (Field field : fields) {
			if (!Modifier.isStatic(field.getModifiers())) {
				mapa_campoMostrar.put(field.getName(), field.getName());
				mapa_campoAsignar.put(field.getName(), field.getName());
			}
		}
	}

	public void lazy() {
		dataTable = new DataTableFXML<DocumentosDTO, DocumentosDTO>(paginator, tabla) {

			@Override
			public Integer getTotalRows(DocumentosDTO filter) {
				try {
					return documentoSvc.getTotalCount(filter) + documentos.size();
				} catch (DocumentosException e) {
					error(e);
				}
				return 0;
			}

			@Override
			public List<DocumentosDTO> getList(DocumentosDTO filter, Integer page, Integer rows) {
				List<DocumentosDTO> lista = new ArrayList<DocumentosDTO>();
				try {
					lista = documentoSvc.getDocumentos(filter, page, rows);
					lista.addAll(documentos);
					if (lista.size() > 0) {
						loadControl(lista.get(0));
					}
				} catch (DocumentosException e) {
					error(e);
				}
				return lista;
			}

			@Override
			public DocumentosDTO getFilter() {
				DocumentosDTO filtro = new DocumentosDTO();
				ParametroDTO param = SelectList.get(tipoDocumento, listTipoDocumento, FIELD_NAME);
				Class control = (Class) SelectList.get(controlar, mapa_controlar);
				if (param != null) {
					filtro.setDoctype(param);
				} else {
					filtro.setSelectNameGroup("NO MOSTRAR NADA");
				}
				if (control != null) {
					filtro.setClaseControlar(control);
				} else {
					filtro.setSelectNameGroup("NO MOSTRAR NADA");
				}
				return filtro;
			}
		};
	}

	private final <T extends DocumentosDTO> void loadControl(T dto) {
		if (dto != null) {
			SelectList.selectItem(busqueda, mapa_controlar, dto.getClaseControlar());
			busqueda();
		}
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private DocumentosDTO loadItem() {
		DocumentosDTO dto = new DocumentosDTO();
		dto.setDoctype(SelectList.get(tipoDocumento, listTipoDocumento, FIELD_NAME));
		dto.setClaseControlar((Class) SelectList.get(controlar, mapa_controlar));
		dto.setFieldName((String) SelectList.get(nombreCampo, mapa_nombreCampo));
		dto.setFieldLabel(fieldLabel.getText());
		dto.setEdit(editable.isSelected());
		dto.setNullable(obligatorio.isSelected());
		if (grupo.isVisible()) {
			dto.setSelectNameGroup(SelectList.get(grupo, listGrupo, FIELD_NAME).getGrupo());
		}
		dto.setObjectSearchDto((Class) SelectList.get(busqueda, mapa_claseBusqueda));
		dto.setPutNameShow((String) SelectList.get(campoMostrar, mapa_campoMostrar));
		dto.setPutNameAssign((String) SelectList.get(campoAsignar, mapa_campoAsignar));
		ParametroDTO param = SelectList.get(grupo, listGrupo, FIELD_NAME);
		if (param != null) {
			dto.setSelectNameGroup(param.getCodigo());
		}
		return dto;
	}

	private Boolean validItem(DocumentosDTO dto) {
		Boolean valid = true;
		valid &= dto.getDoctype() != null;
		valid &= StringUtils.isNotBlank(dto.getFieldName());
		return valid;
	}

	public void guardar() {
		try {
			ParametroDTO param = SelectList.get(tipoDocumento, listTipoDocumento, FIELD_NAME);
			if (StringUtils.isBlank(param.getCodigo())) {
				error("NO se ha seleccionado ningun tipo de documento.");
			} else if (documentos.size() > 0) {
				for (DocumentosDTO dto : documentos) {
					dto.setDoctype(SelectList.get(tipoDocumento, listTipoDocumento, FIELD_NAME));
					dto = documentoSvc.insert(dto, userLogin);
				}
				notificar("Se ha guardado todos los registros correctamente.");
			}
		} catch (DocumentosException e) {
			error(e);
		}
	}

	public void cancelar() {
		documentos.clear();
		tipoDocumento.getSelectionModel().selectFirst();
	}

	public void agregarItem() {
		DocumentosDTO dto = loadItem();
		if (validItem(dto)) {
			if (StringUtils.isBlank(dto.getCodigo())) {
				documentos.add(dto);
				guardar.setVisible(true);
				cancelar.setVisible(true);
			}
			notificar("Se ha ingresado el documento.");
			cleanItem();
			dataTable.search();
		}
	}

	public final void clearItem() {
		cleanItem();
	}

	private void cleanItem() {
		this.grupo.getSelectionModel().selectFirst();
		this.nombreCampo.getSelectionModel().selectFirst();
		this.editable.setSelected(true);
		this.obligatorio.setSelected(false);
		this.busqueda.getSelectionModel().selectFirst();
		this.campoAsignar.getSelectionModel().selectFirst();
		this.campoMostrar.getSelectionModel().selectFirst();
	}

	public void modificarItem() {
		DocumentosDTO dto = loadItem();
		try {
			if (validItem(dto)) {
				if (StringUtils.isNotBlank(dto.getCodigo())) {
					documentoSvc.update(dto, userLogin);
				} else if (StringUtils.isBlank(dto.getCodigo())) {
					for (int i = 0; i < documentos.size(); i++) {
						DocumentosDTO dtos = documentos.get(i);
						if (new Compare<DocumentosDTO>(dtos).to(dto)) {
							documentos.set(i, dtos);
							break;
						}
					}
				}
				notificar("Se ha actualizado el documento.");
				dataTable.search();
				cleanItem();
			}
		} catch (DocumentosException e) {
			error(e);
		}
	}

	public void eliminarItem() {
		DocumentosDTO dto = dataTable.getSelectedRow();
		try {
			if (StringUtils.isNotBlank(dto.getCodigo())) {
				documentoSvc.delete(dto, userLogin);
			} else if (StringUtils.isBlank(dto.getCodigo())) {
				for (int i = 0; i < documentos.size(); i++) {
					DocumentosDTO dtos = documentos.get(i);
					if (new Compare<DocumentosDTO>(dtos).to(dto)) {
						documentos.remove(i);
						break;
					}
				} // end for
				if (documentos.size() == 0) {
					guardar.setVisible(false);
					cancelar.setVisible(false);
				}
			}
			notificar("Se ha eliminado el documento.");
			dataTable.search();
			cleanItem();
		} catch (DocumentosException e) {
			error(e);
		}
	}
}
