package org.pyt.app.beans.dinamico;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.apache.commons.collections4.MultiValuedMap;
import org.apache.commons.collections4.multimap.ArrayListValuedHashMap;
import org.apache.commons.lang3.StringUtils;
import org.controlsfx.glyphfont.FontAwesome.Glyph;
import org.pyt.app.components.ConfirmPopupBean;
import org.pyt.common.abstracts.ADto;
import org.pyt.common.annotations.Inject;
import org.pyt.common.constants.ParametroConstants;
import org.pyt.common.constants.PermissionConstants;
import org.pyt.common.constants.StylesPrincipalConstant;
import org.pyt.common.exceptions.DocumentosException;
import org.pyt.common.exceptions.GenericServiceException;
import org.pyt.common.exceptions.ParametroException;

import com.pyt.service.dto.ActividadIcaDTO;
import com.pyt.service.dto.CentroCostoDTO;
import com.pyt.service.dto.DetalleDTO;
import com.pyt.service.dto.DocumentosDTO;
import com.pyt.service.dto.EmpresaDTO;
import com.pyt.service.dto.IngresoDTO;
import com.pyt.service.dto.ParametroDTO;
import com.pyt.service.dto.ServicioDTO;
import com.pyt.service.interfaces.IDocumentosSvc;
import com.pyt.service.interfaces.IGenericServiceSvc;

import co.com.arquitectura.annotation.proccessor.FXMLFile;
import co.com.japl.ea.beans.abstracts.AListGenericDinamicBean;
import co.com.japl.ea.common.button.apifluid.ButtonsImpl;
import co.com.japl.ea.utls.DataTableFXMLUtil;
import co.com.japl.ea.utls.LoadAppFxml;
import co.com.japl.ea.utls.PermissionUtil;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * Se encargade crear la pantalla de lista de detalles
 * 
 * @author Alejandro Parra
 * @since 10-07-2018
 */
@FXMLFile(path = "view/dinamico", file = "listDetalle.fxml", nombreVentana = "Lista de Detalles")
public class ListaDetalleBean extends AListGenericDinamicBean<DetalleDTO, DocumentosDTO, DetalleDTO> {
	@Inject(resource = "com.pyt.service.implement.DocumentosSvc")
	private IDocumentosSvc documentosSvc;
	@Inject
	private IGenericServiceSvc<EmpresaDTO> empresaSvc;
	@Inject
	private IGenericServiceSvc<ActividadIcaDTO> actividadIcaSvc;
	@Inject
	private IGenericServiceSvc<IngresoDTO> ingresoSvc;
	@Inject
	private IGenericServiceSvc<ServicioDTO> servicioSvc;
	@Inject
	private IGenericServiceSvc<CentroCostoDTO> centroCostoSvc;

	@FXML
	private HBox paginador;
	@FXML
	private TableView<DetalleDTO> tabla;
	@FXML
	private Label sumatoria;
	@FXML
	private GridPane filterTable;
	private VBox panelCentral;
	private DetalleDTO filtro;
	private DetalleDTO registro;
	private DataTableFXMLUtil<DetalleDTO, DetalleDTO> table;
	private ParametroDTO tipoDocumento;
	private String codigoDocumento;
	private MultiValuedMap<String, Object> mapListSelects = new ArrayListValuedHashMap<>();
	@FXML
	private HBox buttons;

	@FXML
	private final void initialize() {
		registro = new DetalleDTO();
		filtro = new DetalleDTO();
		lazy();
		visibleButtons();
		ButtonsImpl.Stream(HBox.class).setLayout(buttons).setName("fxml.btn.save").action(this::agregar)
				.icon(Glyph.SAVE).isVisible(save).setName("fxml.btn.edit").action(this::editar).icon(Glyph.SAVE)
				.isVisible(edit).setName("fxml.btn.delete").action(this::eliminar).icon(Glyph.REMOVE).isVisible(delete)
				.setName("fxml.btn.view").action(this::editar).icon(Glyph.SAVE).isVisible(view).build();
	}

	private void searchFilters() {
		try {
			if (tipoDocumento == null || StringUtils.isBlank(tipoDocumento.getCodigo())) {
				throw new Exception(i18n().valueBundle("document_Type_didnt_found.").get());
			}
			var documentos = new DocumentosDTO();
			documentos.setClaseControlar(DetalleDTO.class);
			documentos.setDoctype(tipoDocumento);
			documentos.setFieldFilter(true);
			genericFields = documentosSvc.getDocumentos(documentos);
			genericsLoads();
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
			documentos.setClaseControlar(DetalleDTO.class);
			documentos.setDoctype(tipoDocumento);
			documentos.setFieldColumn(true);
			genericColumns = documentosSvc.getDocumentos(documentos);
			loadColumns(StylesPrincipalConstant.CONST_TABLE_CUSTOM);
		} catch (Exception e) {
			logger.logger(e);
		}
	}

	private <D extends ADto> void loadInMapList(String name, List<D> rows) {
		rows.stream().forEach(row -> mapListSelects.put(name, row));
	}

	@SuppressWarnings("unchecked")
	private void genericsLoads() {
		getListGenericsFields(TypeGeneric.FILTER).stream()
				.filter(row -> Optional.ofNullable(row.getSelectNameGroup()).isPresent()).forEach(row -> {
					try {
						var instance = row.getClaseControlar().getDeclaredConstructor().newInstance();
						if (instance instanceof ADto) {
							var clazz = ((ADto) instance).getType(row.getFieldName());
							var instanceClass = clazz.getDeclaredConstructor().newInstance();
							if (instanceClass instanceof ParametroDTO) {
								var param = new ParametroDTO();
								param.setGrupo(row.getSelectNameGroup());
								param.setEstado(ParametroConstants.COD_ESTADO_PARAMETRO_ACTIVO_STR);
								loadInMapList(row.getFieldName(), getParametersSvc().getAllParametros(param));
							} else if (instanceClass instanceof ServicioDTO) {
								loadInMapList(row.getFieldName(), servicioSvc.getAll(new ServicioDTO()));
							} else if (instanceClass instanceof CentroCostoDTO) {
								loadInMapList(row.getFieldName(), centroCostoSvc.getAll(new CentroCostoDTO()));
							} else if (instanceClass instanceof ActividadIcaDTO) {
								loadInMapList(row.getFieldName(), actividadIcaSvc.getAll(new ActividadIcaDTO()));
							} else if (instanceClass instanceof IngresoDTO) {
								loadInMapList(row.getFieldName(), ingresoSvc.getAll(new IngresoDTO()));
							} else if (instanceClass instanceof EmpresaDTO) {
								loadInMapList(row.getFieldName(), empresaSvc.getAll(new EmpresaDTO()));
							}
						}

					} catch (ClassCastException | InstantiationException | IllegalAccessException
							| IllegalArgumentException | InvocationTargetException | NoSuchMethodException
							| SecurityException e) {
						logger().logger(e);
					} catch (ParametroException e) {
						logger().logger(e);
					} catch (GenericServiceException e) {
						logger().logger(e);
					}
				});
	}

	/**
	 * Se encarga de cargar la pagina del listado de detalles agregados
	 */
	private final void lazy() {
		table = new DataTableFXMLUtil<DetalleDTO, DetalleDTO>(paginador, tabla) {

			@Override
			public Integer getTotalRows(DetalleDTO filter) {
				try {
					return documentosSvc.getTotalRows(filter);
				} catch (DocumentosException e) {
					error(e);
				}
				return 0;
			}

			@Override
			public List<DetalleDTO> getList(DetalleDTO filter, Integer page, Integer rows) {

				List<DetalleDTO> lista = new ArrayList<DetalleDTO>();
				try {
					lista = documentosSvc.getDetalles(filter, page - 1, rows);
					sumatoria.setText(sumatoria(lista, "valorNeto").toString());
				} catch (DocumentosException e) {
					error(e);
				}
				return lista;
			}

			@Override
			public DetalleDTO getFilter() {
				DetalleDTO dto = new DetalleDTO();
				if (StringUtils.isNotBlank(codigoDocumento)) {
					dto.setCodigoDocumento(codigoDocumento);
				}
				return dto;
			}
		};
		table.selectRow(list -> {
			if (list.size() > 0) {
				delete.setValue(true);
				edit.setValue(true);
			}
		});
	}

	/**
	 * Se encarga de cargar los campos del filtro
	 */
	private final void loadFiltro() {
		filtro = new DetalleDTO();
	}

	/**
	 * Se encarga de cargar la interfaz con los registros
	 * 
	 * @param tipoDocumento {@link ParametroDTO}
	 */
	public final void load(VBox panel, ParametroDTO tipoDocumento, String codigoDocumento) throws Exception {
		if (tipoDocumento == null || StringUtils.isBlank(tipoDocumento.getCodigo()))
			throw new Exception(i18n("err.documenttype.havent.been.entered"));
		if (panel == null)
			throw new Exception(i18n("err.panel.havent.been.entered"));
		if (StringUtils.isBlank(codigoDocumento))
			throw new Exception(i18n("err.documentcode.hevent.been.entered"));
		this.tipoDocumento = tipoDocumento;
		this.codigoDocumento = codigoDocumento;
		panelCentral = panel;
		visibleButtons();
		searchFilters();
		searchColumns();
		table.search();
	}

	/**
	 * Se encarga de llamar el bean para cargar un nuevo registro
	 */
	public final void agregar() {
		try {
			getController(panelCentral, DetalleBean.class).load(panelCentral, tipoDocumento, codigoDocumento);
		} catch (Exception e) {
			errorI18n("err.detail.cant.load.screen.add");
		}
	}

	/**
	 * Se encarga de buscar en la tabla
	 */
	public final void buscar() {
		loadFiltro();
		table.search();
	}

	/**
	 * Se encarga de llamar el bean para editar un nuevo registro
	 */
	public final void editar() {
		if (table.isSelected()) {
			List<DetalleDTO> list = table.getSelectedRows();
			if (list.size() == 1) {
				registro = table.getSelectedRow();
				try {
					getController(panelCentral, DetalleBean.class).load(panelCentral, registro, tipoDocumento);
				} catch (Exception e) {
					errorI18n("err.detial.cant.load.screen.edit");
				}
			} else if (list.size() > 1) {
				errorI18n("err.selecting.alotof.details");
			} else {
				error("No se selecciono ningun detalle.");
			}
		}
	}

	/**
	 * Se encarga de llamar el bean para eliminar un registro
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public final void eliminar() {
		try {
			((ConfirmPopupBean) LoadAppFxml.loadBeanFxml(new Stage(), (Class) ConfirmPopupBean.class))
					.load("#{ListaDetalleBean.delete}", i18n("mensaje.wish.do.delete.selected.rows"));
		} catch (Exception e) {
			error(e);
		}
	}

	public void setDelete(Boolean valid) {
		if (!valid)
			return;
		if (table.isSelected()) {
			List<DetalleDTO> lista = table.getSelectedRows();
			Integer i = 0;
			for (DetalleDTO detalle : lista) {
				try {
					documentosSvc.delete(detalle, getUsuario());
					i++;
				} catch (DocumentosException e) {
					error(e);
				}
			} // end for
			notificar(i18n().valueBundle("mensaje.delete.it.detials", i, lista.size()));
		} else {
			alertaI18n("warn.rows.wasnt.selected.to.delete");
		}
	}

	@Override
	public void loadParameters(String... tipoDocumento) {
	}

	@Override
	public GridPane getGridPane(TypeGeneric typeGeneric) {
		return filterTable;
	}

	@Override
	public DetalleDTO getInstanceDto(TypeGeneric typeGeneric) {
		return filtro;
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
	public Class<DetalleDTO> getClazz() {
		return DetalleDTO.class;
	}

	@Override
	public void selectedRow(MouseEvent eventHandler) {
		visibleButtons();
	}

	@Override
	public TableView<DetalleDTO> getTableView() {
		return tabla;
	}

	@Override
	public DataTableFXMLUtil<DetalleDTO, DetalleDTO> getTable() {
		return table;
	}

	@Override
	protected void visibleButtons() {
		var save = PermissionUtil.INSTANCE().havePerm(PermissionConstants.CONST_PERM_CREATE, ListaDocumentosBean.class,
				getUsuario().getGrupoUser());
		var edit = table.isSelected() && PermissionUtil.INSTANCE().havePerm(PermissionConstants.CONST_PERM_UPDATE,
				ListaDocumentosBean.class, getUsuario().getGrupoUser());
		var delete = table.isSelected() && PermissionUtil.INSTANCE().havePerm(PermissionConstants.CONST_PERM_DELETE,
				ListaDocumentosBean.class, getUsuario().getGrupoUser());
		var view = !save && !edit && !delete && PermissionUtil.INSTANCE().havePerm(PermissionConstants.CONST_PERM_READ,
				ListaDocumentosBean.class, getUsuario().getGrupoUser());
		this.save.setValue(save);
		this.edit.setValue(edit);
		this.view.setValue(view);
		this.delete.setValue(delete);
	}

}
