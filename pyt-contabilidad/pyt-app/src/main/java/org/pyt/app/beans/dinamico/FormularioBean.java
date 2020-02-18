package org.pyt.app.beans.dinamico;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.apache.commons.lang3.StringUtils;
import org.pyt.common.abstracts.ADto;
import org.pyt.common.annotations.Inject;
import org.pyt.common.common.Compare;
import org.pyt.common.common.SelectList;
import org.pyt.common.constants.ParametroConstants;
import org.pyt.common.exceptions.DocumentosException;
import org.pyt.common.exceptions.ParametroException;

import com.pyt.service.dto.ActividadIcaDTO;
import com.pyt.service.dto.BancoDTO;
import com.pyt.service.dto.CentroCostoDTO;
import com.pyt.service.dto.DetalleContableDTO;
import com.pyt.service.dto.DetalleDTO;
import com.pyt.service.dto.DocumentoDTO;
import com.pyt.service.dto.DocumentosDTO;
import com.pyt.service.dto.EmpresaDTO;
import com.pyt.service.dto.ParametroDTO;
import com.pyt.service.dto.ServicioDTO;
import com.pyt.service.dto.TrabajadorDTO;
import com.pyt.service.dto.inventario.ProductoDTO;
import com.pyt.service.interfaces.IDocumentosSvc;
import com.pyt.service.interfaces.IParametrosSvc;

import co.com.arquitectura.annotation.proccessor.FXMLFile;
import co.com.japl.ea.beans.abstracts.ABean;
import co.com.japl.ea.utls.DataTableFXMLUtil;
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
	private DataTableFXMLUtil<DocumentosDTO, DocumentosDTO> dataTable;
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
	private TextField fieldDefaultValue;
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
	private CheckBox fieldFilter;
	@FXML
	private CheckBox fieldColumn;
	@FXML
	private CheckBox fieldHasDefaultValue;
	@FXML
	private CheckBox fieldIsVisible;
	@FXML
	private HBox paginator;
	@FXML
	private Label labelDefaultValue;
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
	private ParametroDTO tipoDeDocumento;

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
			listTipoDocumento = parametroSvc.getAllParametros(tipoDoc, ParametroConstants.GRUPO_TIPO_DOCUMENTO);
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
		fieldColumn.setVisible(false);
		fieldFilter.setVisible(false);
		fieldLabel.setVisible(false);
		guardar.setVisible(false);
		cancelar.setVisible(false);
		fieldDefaultValue.setVisible(false);
		fieldIsVisible.setVisible(false);
		lazy();
	}

	private final void claseControl() {
		mapa_controlar.put("Documentos", DocumentoDTO.class);
		mapa_controlar.put("Detalle Documento", DetalleDTO.class);
		mapa_controlar.put("Detalle Contable", DetalleContableDTO.class);
	}

	private final void claseBusquedas() {
		mapa_claseBusqueda.put("Parametros", ParametroDTO.class);
		mapa_claseBusqueda.put("Banco", BancoDTO.class);
		mapa_claseBusqueda.put("Actividad Ica", ActividadIcaDTO.class);
		mapa_claseBusqueda.put("Centro de Costo", CentroCostoDTO.class);
		mapa_claseBusqueda.put("Empresa", EmpresaDTO.class);
		mapa_claseBusqueda.put("Trabajador", TrabajadorDTO.class);
		mapa_claseBusqueda.put("Producto", ProductoDTO.class);
		mapa_claseBusqueda.put("Servicios", ServicioDTO.class);
	}

	public final void tablaClick() {
		DocumentosDTO docs = dataTable.getSelectedRow();
		if (docs != null) {
			SelectList.selectItem(nombreCampo, mapa_nombreCampo, docs.getFieldName());
			fieldLabel.setText(docs.getFieldLabel());
			editable.setSelected(docs.getEdit());
			obligatorio.setSelected(docs.getNullable());
			fieldFilter.setSelected(docs.getFieldFilter());
			fieldColumn.setSelected(docs.getFieldColumn());
			fieldDefaultValue.setText(docs.getFieldDefaultValue());
			fieldHasDefaultValue.setSelected(Optional.ofNullable(docs.getFieldHasDefaultValue()).orElse(false));
			fieldIsVisible.setSelected(Optional.ofNullable(docs.getFieldIsVisible()).orElse(true));
			SelectList.selectItem(busqueda, mapa_claseBusqueda, docs.getObjectSearchDto());
			SelectList.selectItem(grupo, listGrupo, FIELD_NAME, docs, "selectNameGroup");
			SelectList.selectItem(campoMostrar, mapa_campoMostrar, docs.getPutNameShow());
			SelectList.selectItem(campoAsignar, mapa_campoAsignar, docs.getPutNameAssign());
			manejaDefault();
		} else {
			alertaI18n("warn.rows.havent.been.seleceted");
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

	public final void manejaDefault() {
		showFieldWhenValorDefecto();
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
			showFieldEdited();
			showFieldWhenValorDefecto();
			if (!instans) {
				showFieldWhenManejaLista();
			} else {
				showFieldWhenNotManejaLista(campo, param);
			}
		} else {
			hiddenFieldEdited();
		}
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private final void showFieldWhenNotManejaLista(Class field, Boolean hasParams) {
		manejaLista.setVisible(false);
		lCampoMostrar.setVisible(true);
		campoMostrar.setVisible(true);
		mapa_campoMostrar.clear();
		getFieldShow(field);
		SelectList.put(campoMostrar, mapa_campoMostrar);
		campoMostrar.getSelectionModel().selectFirst();
		grupo.setVisible(hasParams);
		lGrupo.setVisible(hasParams);
	}

	private final void showFieldWhenManejaLista() {
		manejaLista.setVisible(true);
		lCampoMostrar.setVisible(false);
		campoMostrar.setVisible(false);
		grupo.setVisible(false);
		lGrupo.setVisible(false);
	}

	private final void showFieldEdited() {
		editable.setVisible(true);
		obligatorio.setVisible(true);
		fieldFilter.setVisible(true);
		fieldColumn.setVisible(true);
		addItem.setVisible(true);
		modifyItem.setVisible(true);
		delItem.setVisible(true);
		clearItem.setVisible(true);
		fieldHasDefaultValue.setVisible(true);
		labelDefaultValue.setVisible(true);
	}

	private final void showFieldWhenValorDefecto() {
		if (fieldHasDefaultValue.isSelected()) {
			fieldDefaultValue.setVisible(true);
			fieldIsVisible.setVisible(true);
			labelDefaultValue.setVisible(true);
		} else {
			fieldDefaultValue.setVisible(false);
			fieldIsVisible.setVisible(false);
			labelDefaultValue.setVisible(false);
		}
	}

	private final void hiddenFieldEdited() {
		editable.setVisible(false);
		obligatorio.setVisible(false);
		manejaLista.setVisible(false);
		addItem.setVisible(false);
		modifyItem.setVisible(false);
		delItem.setVisible(false);
		clearItem.setVisible(false);
		fieldFilter.setVisible(false);
		fieldColumn.setVisible(false);
		fieldHasDefaultValue.setVisible(false);
		fieldDefaultValue.setVisible(false);
		fieldIsVisible.setVisible(false);
		labelDefaultValue.setVisible(false);
	}

	public final void tipoDocumento() {
		dataTable.search();
	}

	/**
	 * Obtiene los nombres de los campos que se encuentran dentro del objeto
	 * principal
	 */
	private final <T extends ADto> void getNameFields(Class<T> clase) {
		if (clase != null) {
			Field[] fields = clase.getDeclaredFields();
			mapa_nombreCampo.clear();
			for (Field field : fields) {
				if (!Modifier.isStatic(field.getModifiers())) {
					mapa_nombreCampo.put(field.getName(), field.getName());
				}
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
		mapa_campoAsignar.clear();
		mapa_campoAsignar.clear();
		for (Field field : fields) {
			if (!Modifier.isStatic(field.getModifiers())) {
				mapa_campoMostrar.put(field.getName(), field.getName());
				mapa_campoAsignar.put(field.getName(), field.getName());
			}
		}
	}

	public void lazy() {
		dataTable = new DataTableFXMLUtil<DocumentosDTO, DocumentosDTO>(paginator, tabla) {

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
					lista = documentoSvc.getDocumentos(filter, page - 1, rows);
					lista.addAll(documentos);
					if (lista.size() > 0) {
						loadControl(lista.get(0));
					}
				} catch (DocumentosException e) {
					error(e);
				}
				return lista;
			}

			@SuppressWarnings("rawtypes")
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
		var selectedDoc = dataTable.getSelectedRow();
		var dto = selectedDoc == null ? new DocumentosDTO() : selectedDoc;
		if ((dataTable.getSelectedRow() != null && StringUtils.isNotBlank(dataTable.getSelectedRow().getCodigo()))) {
			dto.setCodigo(dataTable.getSelectedRow().getCodigo());
		}
		dto.setDoctype(SelectList.get(tipoDocumento, listTipoDocumento, FIELD_NAME));
		dto.setClaseControlar((Class) SelectList.get(controlar, mapa_controlar));
		dto.setFieldName((String) SelectList.get(nombreCampo, mapa_nombreCampo));
		dto.setFieldLabel(fieldLabel.getText());
		dto.setEdit(editable.isSelected());
		dto.setNullable(obligatorio.isSelected());
		dto.setFieldFilter(fieldFilter.isSelected());
		dto.setFieldColumn(fieldColumn.isSelected());
		dto.setFieldHasDefaultValue(fieldHasDefaultValue.isSelected());
		dto.setFieldIsVisible(fieldIsVisible.isSelected());
		dto.setFieldDefaultValue(fieldDefaultValue.getText());
		if (grupo.isVisible()) {
			dto.setSelectNameGroup(SelectList.get(grupo, listGrupo, FIELD_NAME).getCodigo());
		}
		if (busqueda.isVisible()) {
			dto.setObjectSearchDto((Class) SelectList.get(busqueda, mapa_claseBusqueda));
		}
		if (campoMostrar.isVisible()) {
			dto.setPutNameShow((String) SelectList.get(campoMostrar, mapa_campoMostrar));
		}
		if (campoAsignar.isVisible()) {
			dto.setPutNameAssign((String) SelectList.get(campoAsignar, mapa_campoAsignar));
		}
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
				errorI18n("err.documenttype.havent.been.selected");
			} else if (documentos.size() > 0) {
				for (DocumentosDTO dto : documentos) {
					dto.setDoctype(SelectList.get(tipoDocumento, listTipoDocumento, FIELD_NAME));
					dto = documentoSvc.insert(dto, getUsuario());
				}
				notificarI18n("mensaje.rows.have.been.inserted.successfull");
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
			notificarI18n("mensaje.document.have.been.inserted");
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
		this.fieldLabel.setText("");
		fieldFilter.setSelected(false);
		fieldColumn.setSelected(false);
		fieldHasDefaultValue.setSelected(false);
	}

	public void modificarItem() {
		DocumentosDTO dto = loadItem();
		try {
			if (validItem(dto)) {
				if (StringUtils.isNotBlank(dto.getCodigo())) {
					documentoSvc.update(dto, getUsuario());

				} else if (StringUtils.isBlank(dto.getCodigo())) {
					for (int i = 0; i < documentos.size(); i++) {
						DocumentosDTO dtos = documentos.get(i);
						if (new Compare<DocumentosDTO>(dtos).to(dto)) {
							documentos.set(i, dtos);
							break;
						}
					}
				}
				notificarI18n("mensaje.document.have.been.updated");
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
			if (dto != null) {
				if (StringUtils.isNotBlank(dto.getCodigo())) {
					documentoSvc.delete(dto, getUsuario());
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
				notificarI18n("mensaje.document.have.been.deleted");
				dataTable.search();
				cleanItem();
			}
		} catch (DocumentosException e) {
			error(e);
		}
	}
}
