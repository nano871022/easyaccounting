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
import org.pyt.common.constants.PermissionConstants;
import org.pyt.common.constants.StylesPrincipalConstant;
import org.pyt.common.exceptions.DocumentosException;
import org.pyt.common.exceptions.GenericServiceException;

import com.pyt.service.dto.ConceptoDTO;
import com.pyt.service.dto.CuentaContableDTO;
import com.pyt.service.dto.DetalleContableDTO;
import com.pyt.service.dto.DocumentosDTO;
import com.pyt.service.dto.ParametroDTO;
import com.pyt.service.interfaces.IDocumentosSvc;
import com.pyt.service.interfaces.IGenericServiceSvc;

import co.com.arquitectura.annotation.proccessor.FXMLFile;
import co.com.japl.ea.beans.abstracts.AListGenericDinamicBean;
import co.com.japl.ea.common.button.apifluid.ButtonsImpl;
import co.com.japl.ea.utls.DataTableFXMLUtil;
import co.com.japl.ea.utls.PermissionUtil;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

/**
 * Se encargade crear la pantalla de lista de detalles
 * 
 * @author Alejandro Parra
 * @since 10-07-2018
 */
@FXMLFile(path = "view/dinamico", file = "listDetalleContable.fxml", nombreVentana = "Lista de Detalles")
public class ListaDetalleContableBean
		extends AListGenericDinamicBean<DetalleContableDTO, DocumentosDTO, DetalleContableDTO> {
	@Inject(resource = "com.pyt.service.implement.DocumentosSvc")
	private IDocumentosSvc documentosSvc;
	@Inject
	private IGenericServiceSvc<ConceptoDTO> conceptoSvc;
	@Inject
	private IGenericServiceSvc<CuentaContableDTO> cuentaContableSvc;
	@FXML
	private HBox paginador;
	@FXML
	private TableView<DetalleContableDTO> tabla;
	@FXML
	private Label sumatoria;
	@FXML
	private GridPane filterTable;
	private VBox panelCentral;
	private DetalleContableDTO filtro;
	private DetalleContableDTO registro;
	private DataTableFXMLUtil<DetalleContableDTO, DetalleContableDTO> table;
	private ParametroDTO tipoDocumento;
	private String codigoDocumento;
	private MultiValuedMap<String, Object> mapListSelects = new ArrayListValuedHashMap<>();
	@FXML
	private HBox buttons;

	@FXML
	private final void initialize() {
		registro = new DetalleContableDTO();
		filtro = new DetalleContableDTO();
		lazy();
		visibleButtons();
		ButtonsImpl.Stream(HBox.class).setLayout(buttons).setName("fxml.btn.save").action(this::agregar)
				.icon(Glyph.SAVE).isVisible(save).setName("fxml.btn.edit").action(this::editar).icon(Glyph.SAVE)
				.isVisible(edit).setName("fxml.btn.delete").action(this::eliminar).icon(Glyph.REMOVE).isVisible(delete)
				.setName("fxml.btn.view").action(this::editar).icon(Glyph.FILE_TEXT).isVisible(view).build();
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
							if (instanceClass instanceof ConceptoDTO) {
								loadInMapList(row.getFieldName(), conceptoSvc.getAll(new ConceptoDTO()));
							} else if (instanceClass instanceof CuentaContableDTO) {
								loadInMapList(row.getFieldName(), cuentaContableSvc.getAll(new CuentaContableDTO()));
							}
						}

					} catch (ClassCastException | InstantiationException | IllegalAccessException
							| IllegalArgumentException | InvocationTargetException | NoSuchMethodException
							| SecurityException e) {
						logger().logger(e);
					} catch (GenericServiceException e) {
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
			documentos.setClaseControlar(DetalleContableDTO.class);
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
			documentos.setClaseControlar(DetalleContableDTO.class);
			documentos.setDoctype(tipoDocumento);
			documentos.setFieldColumn(true);
			genericColumns = documentosSvc.getDocumentos(documentos);
			loadColumns(StylesPrincipalConstant.CONST_TABLE_CUSTOM);
		} catch (Exception e) {
			logger.logger(e);
		}
	}

	/**
	 * Se encarga de cargar la pagina del listado de detalles agregados
	 */
	private final void lazy() {
		table = new DataTableFXMLUtil<DetalleContableDTO, DetalleContableDTO>(paginador, tabla) {

			@Override
			public Integer getTotalRows(DetalleContableDTO filter) {
				try {
					return documentosSvc.getTotalCount(filter);
				} catch (DocumentosException e) {
					error(e);
				}
				return 0;
			}

			@Override
			public List<DetalleContableDTO> getList(DetalleContableDTO filter, Integer page, Integer rows) {

				List<DetalleContableDTO> lista = new ArrayList<DetalleContableDTO>();
				try {
					lista = documentosSvc.getDetalles(filter, page - 1, rows);
					sumatoria.setText(sumatoria(lista, "valor").toString());
				} catch (DocumentosException e) {
					error(e);
				}
				return lista;
			}

			@Override
			public DetalleContableDTO getFilter() {
				DetalleContableDTO dto = new DetalleContableDTO();
				if (StringUtils.isNotBlank(codigoDocumento)) {
					dto.setCodigoDocumento(codigoDocumento);
				}
				return dto;
			}
		};
	}

	/**
	 * Se encarga de cargar los campos del filtro
	 */
	private final void loadFiltro() {
		filtro = new DetalleContableDTO();
	}

	/**
	 * Se encarga de cargar la interfaz con los registros
	 * 
	 * @param tipoDocumento {@link ParametroDTO}
	 */
	public final void load(VBox panel, ParametroDTO tipoDocumento, String codigoDocumento) throws Exception {
		if (tipoDocumento == null || StringUtils.isBlank(tipoDocumento.getCodigo()))
			throw new Exception(i18n("err.documenttype.wasnt.entered"));
		if (panel == null)
			throw new Exception(i18n("err.panel.wasnt.entered"));
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
			getController(panelCentral, DetalleContableBean.class).load(panelCentral, tipoDocumento, codigoDocumento);
		} catch (Exception e) {
			errorI18n("err.detail.cant.load.on.screen.to.add");
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
			List<DetalleContableDTO> list = table.getSelectedRows();
			if (list.size() == 1) {
				registro = table.getSelectedRow();
				try {
					getController(panelCentral, DetalleContableBean.class).load(panelCentral, registro, tipoDocumento,
							codigoDocumento);
				} catch (Exception e) {
					errorI18n("err.detail.cant.load.on.screen.to.edit");
				}
			} else if (list.size() > 1) {
				errorI18n("err.some.details.was.selected");
			} else {
				errorI18n("err.detail.wasnt.selected");
			}
		}
	}

	/**
	 * Se encarga de llamar el bean para eliminar un registro
	 */
	public final void eliminar() {
		try {
			controllerPopup(ConfirmPopupBean.class).load("#{ListaDetalleContableBean.delete}",
					i18n("mensaje.wish.do.delete.selected.rows"));
		} catch (Exception e) {
			error(e);
		}
	}

	public void setDelete(Boolean valid) {
		if (!valid)
			return;
		if (table.isSelected()) {
			List<DetalleContableDTO> lista = table.getSelectedRows();
			Integer i = 0;
			for (DetalleContableDTO detalle : lista) {
				try {
					documentosSvc.delete(detalle, getUsuario());
					i++;
				} catch (DocumentosException e) {
					error(e);
				}
			} // end for
			notificar("Se eliminaron " + i + "/" + lista.size() + " detalles.");
		} else {
			alertaI18n("warn.rows.havent.been.selected.to.delete");
		}
	}

	/**
	 * Se encarga de seleccionar un registro de la tabla
	 */
	public final void seleccionar() {
		visibleButtons();
	}

	@Override
	public void loadParameters(String... tipoDocumento) {
	}

	@Override
	public GridPane getGridPane(TypeGeneric typeGeneric) {
		return filterTable;
	}

	@Override
	public DetalleContableDTO getInstanceDto(TypeGeneric typeGeneric) {
		return filtro;
	}

	@Override
	public MultiValuedMap<String, Object> getMapListToChoiceBox() {
		return mapListSelects;
	}

	@Override
	public Integer getMaxColumns(TypeGeneric typeGeneric) {
		return 2;
	}

	@Override
	public Class<DetalleContableDTO> getClazz() {
		return DetalleContableDTO.class;
	}

	@Override
	public void selectedRow(MouseEvent eventHandler) {
	}

	@Override
	public TableView<DetalleContableDTO> getTableView() {
		return tabla;
	}

	@Override
	public DataTableFXMLUtil<DetalleContableDTO, DetalleContableDTO> getTable() {
		return table;
	}

	@Override
	@SuppressWarnings("rawtypes")
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
