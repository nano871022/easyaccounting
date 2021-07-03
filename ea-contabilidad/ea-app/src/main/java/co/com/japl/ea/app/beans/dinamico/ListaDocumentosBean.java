package co.com.japl.ea.app.beans.dinamico;

import static org.pyt.common.common.InstanceObject.instance;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.apache.commons.collections4.MultiValuedMap;
import org.apache.commons.collections4.multimap.ArrayListValuedHashMap;
import org.apache.commons.lang3.StringUtils;
import org.controlsfx.glyphfont.FontAwesome.Glyph;
import org.pyt.common.annotations.Inject;
import org.pyt.common.constants.ParametroConstants;
import org.pyt.common.constants.PermissionConstants;
import org.pyt.common.constants.StylesPrincipalConstant;

import co.com.arquitectura.annotation.proccessor.FXMLFile;
import co.com.japl.ea.app.components.ConfirmPopupBean;
import co.com.japl.ea.app.components.PopupBean;
import co.com.japl.ea.app.custom.ResponsiveGridPane;
import co.com.japl.ea.beans.abstracts.AListGenericDinamicBean;
import co.com.japl.ea.common.abstracts.ADto;
import co.com.japl.ea.common.button.apifluid.ButtonsImpl;
import co.com.japl.ea.dto.dto.DocumentoDTO;
import co.com.japl.ea.dto.dto.DocumentosDTO;
import co.com.japl.ea.dto.dto.EmpresaDTO;
import co.com.japl.ea.dto.dto.ParametroDTO;
import co.com.japl.ea.dto.interfaces.IDocumentosSvc;
import co.com.japl.ea.dto.interfaces.IEmpresasSvc;
import co.com.japl.ea.dto.interfaces.IParametrosSvc;
import co.com.japl.ea.exceptions.DocumentosException;
import co.com.japl.ea.exceptions.EmpresasException;
import co.com.japl.ea.exceptions.LoadAppFxmlException;
import co.com.japl.ea.exceptions.ParametroException;
import co.com.japl.ea.utls.DataTableFXMLUtil;
import co.com.japl.ea.utls.PermissionUtil;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;

/**
 * Se encarga de controlar la intefase que enlista todos los documentos creados
 * 
 * @author Alejandro Parra
 * @since 02-07-2018
 */
@FXMLFile(path = "view/dinamico", file = "listaDocumentos.fxml", nombreVentana = "Lista de Documentos")
public class ListaDocumentosBean extends AListGenericDinamicBean<DocumentoDTO, DocumentosDTO, DocumentoDTO> {
	@Inject(resource = "com.pyt.service.implement.DocumentosSvc")
	private IDocumentosSvc documentosSvc;
	@Inject(resource = "com.pyt.service.implement.ParametrosSvc")
	private IParametrosSvc parametroSvc;
	@Inject(resource = "com.pyt.service.implement.EmpresaSvc")
	private IEmpresasSvc empresasSvc;
	@FXML
	private TableView<DocumentoDTO> tabla;
	@FXML
	private HBox paginador;
	@FXML
	private HBox titulo;
	@FXML
	private Label lblTitle;
	private DataTableFXMLUtil<DocumentoDTO, DocumentoDTO> dataTable;
	@FXML
	private ResponsiveGridPane filterTable;
	protected DocumentoDTO filter;
	private ParametroDTO tipoDocumento;
	private MultiValuedMap<String, Object> mapListSelects = new ArrayListValuedHashMap<>();
	@FXML
	private HBox buttons;

	@FXML
	public void initialize() {
		try {
			registro = new DocumentoDTO();
			filter = new DocumentoDTO();
			lazy();
			visibleButtons();
			ButtonsImpl.Stream(HBox.class).setLayout(buttons).setName("fxml.btn.add").action(this::agregar)
					.icon(Glyph.SAVE).isVisible(save).setName("fxml.btn.edit").action(this::modificar).icon(Glyph.EDIT)
					.isVisible(edit).setName("fxml.btn.delete").action(this::eliminar).icon(Glyph.REMOVE)
					.isVisible(delete).setName("fxml.btn.view").action(this::modificar).icon(Glyph.FILE_TEXT)
					.isVisible(view).build();
		} catch (Exception e) {
			error(e);
		}
	}

	@SuppressWarnings("unchecked")
	private final void loadField() {
		getListGenericsFields(TypeGeneric.FILTER).stream()
				.filter(row -> Optional.ofNullable(row.getSelectNameGroup()).isPresent()).forEach(row -> {
					try {
						var instance = row.getClaseControlar().getDeclaredConstructor().newInstance();
						if (instance instanceof ADto) {
							var clazz = ((ADto) instance).getType(row.getFieldName());
							var instanceClass = instance(clazz);
							if (instanceClass instanceof ParametroDTO) {
								var param = new ParametroDTO();
								param.setGrupo(row.getSelectNameGroup());
								param.setEstado(ParametroConstants.COD_ESTADO_PARAMETRO_ACTIVO_STR);
								getParametersSvc().getAllParametros(param).forEach(reg -> {
									mapListSelects.put(row.getFieldName(), reg);
								});
							}
						}
					} catch (ClassCastException | InstantiationException | IllegalAccessException
							| IllegalArgumentException | InvocationTargetException | NoSuchMethodException
							| SecurityException e) {
						logger().logger(e);
					} catch (ParametroException e) {
						logger().logger(e);
					}
				});
		getListGenericsFields(TypeGeneric.FILTER).stream().filter(
				row -> StringUtils.isBlank(row.getSelectNameGroup()) && StringUtils.isNotBlank(row.getPutNameShow()))
				.forEach(row -> {
					try {
						var instance = row.getClaseControlar().getDeclaredConstructor().newInstance();
						if (instance instanceof ADto) {
							var clazz = ((ADto) instance).getType(row.getFieldName());
							var instanceClass = clazz.getDeclaredConstructor().newInstance();
							if (instanceClass instanceof EmpresaDTO) {
								var empresa = new EmpresaDTO();
								var list = empresasSvc.getAllEmpresas(empresa);
								list.forEach(reg -> mapListSelects.put(row.getFieldName(), reg));
							}
						}
					} catch (ClassCastException | InstantiationException | IllegalAccessException
							| IllegalArgumentException | InvocationTargetException | NoSuchMethodException
							| SecurityException e) {
						logger().logger(e);
					} catch (EmpresasException e) {
						logger().logger(e);
					}
				});

	}

	private void searchFilters() {
		try {
			if (tipoDocumento == null || StringUtils.isBlank(tipoDocumento.getCodigo())) {
				throw new Exception(i18n().valueBundle("document_Type_didnt_found.").get());
			}
			var documentos = new DocumentosDTO();
			documentos.setClaseControlar(DocumentoDTO.class);
			documentos.setDoctype(tipoDocumento);
			documentos.setFieldFilter(true);
			genericFields = documentosSvc.getDocumentos(documentos);
			loadField();
			loadFields(TypeGeneric.FILTER, StylesPrincipalConstant.CONST_GRID_STANDARD);
		} catch (Exception e) {
			logger.logger(e);
		}
	}

	private void searchColumns() {
		try {
			if (tipoDocumento == null || StringUtils.isBlank(tipoDocumento.getCodigo())) {
				throw new Exception(i18n().valueBundle("document_Type_didnt_found.").get());
			}
			var documentos = new DocumentosDTO();
			documentos.setClaseControlar(DocumentoDTO.class);
			documentos.setDoctype(tipoDocumento);
			documentos.setFieldColumn(true);
			genericColumns = documentosSvc.getDocumentos(documentos);
			loadColumns(StylesPrincipalConstant.CONST_TABLE_CUSTOM);
		} catch (Exception e) {
			logger.logger(e);
		}
	}

	public void lazy() {
		dataTable = new DataTableFXMLUtil<DocumentoDTO, DocumentoDTO>(paginador, tabla) {

			@Override
			public Integer getTotalRows(DocumentoDTO filter) {
				try {
					return documentosSvc.getTotalCount(filter);
				} catch (DocumentosException e) {
					error(e);
				}
				return 0;
			}

			@Override
			public List<DocumentoDTO> getList(DocumentoDTO filter, Integer page, Integer rows) {
				List<DocumentoDTO> lista = new ArrayList<DocumentoDTO>();
				try {
					lista = documentosSvc.getDocumentos(filter, page - 1, rows);
				} catch (DocumentosException e) {
					error(e);
				}
				return lista;
			}

			@Override
			public DocumentoDTO getFilter() {
				if (tipoDocumento != null && StringUtils.isNotBlank(tipoDocumento.getCodigo())) {
					filter.setTipoDocumento(tipoDocumento);
				}
				return filter;
			}
		};
	}

	public final void agregar() {
		var documento = new DocumentoDTO();
		documento.setTipoDocumento(tipoDocumento);
		getController(PanelBean.class).load(documento);
	}

	public final void modificar() {
		registro = dataTable.getSelectedRow();
		if (registro != null && StringUtils.isNotBlank(registro.getCodigo())) {
			getController(PanelBean.class).load(registro);
		} else {
			errorI18n("err.document.havent.been.selected");
		}
	}

	public final void eliminar() {
		try {
			controllerPopup(ConfirmPopupBean.class).load("#{ListaDocumentosBean.delete}",
					i18n().valueBundle("want_delete_this_record"));
		} catch (LoadAppFxmlException e) {
			error(e);
		}
	}

	public final void setDelete(Boolean valid) {
		if (valid) {
			try {
				controllerPopup(PopupBean.class).load(i18n().valueBundle("this_functionality_isnt_active"),
						PopupBean.TIPOS.WARNING);
			} catch (LoadAppFxmlException e) {
				error(e);
			}
		}
	}

	public final void loadParameters(String... tipoDocumento) {
		try {
			if (tipoDocumento.length == 1 && tipoDocumento[0].trim().toLowerCase().contains("tipodocumento")) {
				var grupo = parametroSvc.getIdByParametroGroup(ParametroConstants.GRUPO_TIPO_DOCUMENTO);
				var parametro = new ParametroDTO();
				parametro.setValor2(tipoDocumento[0].substring(tipoDocumento[0].indexOf("$") + 1));
				parametro.setEstado(ParametroConstants.COD_ESTADO_PARAMETRO_ACTIVO_STR);
				parametro.setGrupo(grupo);
				logger.DEBUG("Opcion valor2 Parametro Tipo Documento::" + parametro.getValor2());
				var tdoc = parametroSvc.getParametro(parametro);
				this.tipoDocumento = tdoc;
				lblTitle.setText(this.tipoDocumento.getNombre());
				dataTable.search();
				visibleButtons();
				searchFilters();
				searchColumns();
			} else if (tipoDocumento.length == 1 && !tipoDocumento[0].trim().toLowerCase().contains("tipodocumento")) {
				var grupo = parametroSvc.getIdByParametroGroup(ParametroConstants.GRUPO_TIPO_DOCUMENTO);
				var parametro = new ParametroDTO();
				parametro.setValor2(tipoDocumento[0]);
				parametro.setEstado(ParametroConstants.COD_ESTADO_PARAMETRO_ACTIVO_STR);
				parametro.setGrupo(grupo);
				logger.DEBUG("Opcion valor2 Parametro Tipo Documento::" + parametro.getValor2());
				var tdoc = parametroSvc.getParametro(parametro);
				this.tipoDocumento = tdoc;
				lblTitle.setText(this.tipoDocumento.getNombre());
				dataTable.search();
				visibleButtons();
				searchFilters();
				searchColumns();
			}
		} catch (ParametroException e) {
			error(e);
		} catch (Exception e) {
			logger.logger(i18n().valueBundle("document.type.had.error.in.its.processing"), e);
		}
	}

	@Override
	public TableView<DocumentoDTO> getTableView() {
		return tabla;
	}

	@Override
	public javafx.scene.layout.GridPane getGridPane(TypeGeneric typeGeneric) {
		return filterTable;
	}

	@Override
	public DocumentoDTO getInstanceDto(TypeGeneric typeGeneric) {
		return filter;
	}

	@Override
	public MultiValuedMap<String, Object> getMapListToChoiceBox() {
		return mapListSelects;
	}

	@Override
	public Integer getMaxColumns(TypeGeneric typeGeneric) {
		return 4;
	}

	@Override
	public Class<DocumentoDTO> getClazz() {
		return DocumentoDTO.class;
	}

	@Override
	public void selectedRow(MouseEvent eventHandler) {
		visibleButtons();
	}

	@Override
	public DataTableFXMLUtil<DocumentoDTO, DocumentoDTO> getTable() {
		return dataTable;
	}

	@Override
	protected void visibleButtons() {
		var save = PermissionUtil.INSTANCE().havePerm(PermissionConstants.CONST_PERM_CREATE, ListaDocumentosBean.class,
				getUsuario().getGrupoUser());
		var edit = dataTable.isSelected() && PermissionUtil.INSTANCE().havePerm(PermissionConstants.CONST_PERM_UPDATE,
				ListaDocumentosBean.class, getUsuario().getGrupoUser());
		var delete = dataTable.isSelected() && PermissionUtil.INSTANCE().havePerm(PermissionConstants.CONST_PERM_DELETE,
				ListaDocumentosBean.class, getUsuario().getGrupoUser());
		var view = !save && PermissionUtil.INSTANCE().havePerm(PermissionConstants.CONST_PERM_READ,
				ListaDocumentosBean.class, getUsuario().getGrupoUser());
		this.save.setValue(save);
		this.edit.setValue(edit);
		this.delete.setValue(delete);
		this.view.setValue(view);
	}

}
