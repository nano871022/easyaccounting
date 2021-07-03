package co.com.japl.ea.app.beans.dinamico;

import static org.pyt.common.common.InstanceObject.instance;

import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.apache.commons.collections4.MultiValuedMap;
import org.apache.commons.lang3.StringUtils;
import org.controlsfx.glyphfont.FontAwesome.Glyph;
import org.pyt.common.annotations.Inject;
import org.pyt.common.common.DtoUtils;
import org.pyt.common.constants.ParametroConstants;
import org.pyt.common.constants.PermissionConstants;
import org.pyt.common.constants.StylesPrincipalConstant;

import co.com.arquitectura.annotation.proccessor.FXMLFile;
import co.com.japl.ea.app.custom.ResponsiveGridPane;
import co.com.japl.ea.common.abstracts.ADto;
import co.com.japl.ea.common.button.apifluid.ButtonsImpl;
import co.com.japl.ea.dto.dto.ActividadIcaDTO;
import co.com.japl.ea.dto.dto.CentroCostoDTO;
import co.com.japl.ea.dto.dto.DetalleDTO;
import co.com.japl.ea.dto.dto.DocumentosDTO;
import co.com.japl.ea.dto.dto.EmpresaDTO;
import co.com.japl.ea.dto.dto.IngresoDTO;
import co.com.japl.ea.dto.dto.ParametroDTO;
import co.com.japl.ea.dto.dto.ServicioDTO;
import co.com.japl.ea.dto.interfaces.IDocumentosSvc;
import co.com.japl.ea.dto.interfaces.IGenericServiceSvc;
import co.com.japl.ea.dto.interfaces.IParametrosSvc;
import co.com.japl.ea.exceptions.DocumentosException;
import co.com.japl.ea.exceptions.GenericServiceException;
import co.com.japl.ea.exceptions.ParametroException;
import co.com.japl.ea.exceptions.validates.ValidateValueException;
import co.com.japl.ea.utls.PermissionUtil;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

/**
 * Se encarga de controlar el formulario dinamico para documentos
 * 
 * @author Alejandro Parra
 * @since 07-07-2018
 */
@FXMLFile(path = "view/dinamico", file = "detalle.fxml")
public class DetalleBean extends DinamicoBean<DocumentosDTO, DetalleDTO> {
	@Inject(resource = "com.pyt.service.implement.ParametrosSvc")
	private IParametrosSvc parametrosSvc;
	@Inject(resource = "com.pyt.service.implement.DocumentosSvc")
	private IDocumentosSvc documentoSvc;
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
	private VBox central;
	@FXML
	private Label titulo;
	private ParametroDTO tipoDocumento;
	private VBox panelCentral;
	private String codigoDocumento;
	private ResponsiveGridPane gridPane;
	@FXML
	private HBox buttons;

	@Override
	@FXML
	public void initialize() {
		super.initialize();
		registro = new DetalleDTO();
		tipoDocumento = new ParametroDTO();
		gridPane = new ResponsiveGridPane();
//		gridPane = new UtilControlFieldFX().configGridPane(gridPane);
		ButtonsImpl.Stream(HBox.class).setLayout(buttons).setName("fxml.btn.save").action(this::guardar)
				.icon(Glyph.SAVE).isVisible(save).setName("fxml.btn.edit").action(this::guardar).icon(Glyph.EDIT)
				.isVisible(edit).setName("fxml.btn.back").action(this::regresar).icon(Glyph.BACKWARD).build();
	}

	/**
	 * Se encarga de realizar la busqueda de los campos configurados para el tipo de
	 * docuumento seleccionado
	 */
	public final void loadField() {
		DocumentosDTO docs = new DocumentosDTO();
		if (tipoDocumento != null) {
			docs.setDoctype(tipoDocumento);
			docs.setClaseControlar(DetalleDTO.class);
			try {
				campos = documentosSvc.getDocumentos(docs);
			} catch (DocumentosException e) {
				error(e);
			}
		}
		central.getChildren().clear();
		central.getStyleClass().add("borderView");
		central.getChildren().add(gridPane);

		genericsLoads();
		loadFields(TypeGeneric.FIELD, StylesPrincipalConstant.CONST_GRID_STANDARD);
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
							var instanceClass = instance(clazz);
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
	 * Se encarga de cargar un nuevo registro
	 */
	public final void load(VBox panel, ParametroDTO tipoDoc, String codigoDocumento) throws Exception {
		if (tipoDoc == null || StringUtils.isBlank(tipoDoc.getCodigo()))
			throw new Exception(i18n("err.documenttype.wasnt.entered"));
		if (panel == null)
			throw new Exception(i18n("err.panel.wasnt.entered"));
		registro = new DetalleDTO();
		tipoDocumento = tipoDoc;
		panelCentral = panel;
		this.codigoDocumento = codigoDocumento;
		titulo.setText(titulo.getText() + ": " + tipoDoc.getNombre());
		visibleButtons();
		loadField();
	}

	public final void load(VBox panel, DetalleDTO registro, ParametroDTO tipoDoc) throws Exception {
		if (tipoDoc == null || StringUtils.isBlank(tipoDoc.getCodigo()))
			throw new Exception(i18n().get("err.documenttype.wasnt.entered"));
		if (registro == null || StringUtils.isBlank(registro.getCodigo()))
			throw new Exception(i18n("err.detail.wasnt.entered"));
		if (panel == null)
			throw new Exception(i18n("err.panel.wasnt.entered"));
		this.registro = registro;
		tipoDocumento = tipoDoc;
		codigoDocumento = registro.getCodigoDocumento();
		panelCentral = panel;
		titulo.setText(concat(titulo.getText(), ": ", tipoDoc.getNombre()));
		loadField();
	}

	/**
	 * Se encarga de guardar todo
	 */
	public final void guardar() {
		if (validFields()) {
			try {
				if (StringUtils.isNotBlank(registro.getCodigo())) {
					registro.setCodigoDocumento(codigoDocumento);
					documentosSvc.update(registro, getUsuario());
					notificarI18n("mensaje.detail.have.been.updated");
					loadField();
				} else {
					registro.setCodigoDocumento(codigoDocumento);
					registro = documentosSvc.insert(registro, getUsuario());
					notificarI18n("mensaje.detail.have.been.inserted");
					loadField();
				}
			} catch (DocumentosException e) {
				error(e);
			}
		}
	}

	/**
	 * Se encarga de cancelar el almacenamiento de los datos
	 */
	public final void regresar() {
		try {
			getController(panelCentral, ListaDetalleBean.class).load(panelCentral, tipoDocumento, codigoDocumento);
		} catch (Exception e) {
			errorI18n("err.panel.cant.load.list.detail");
		}
	}

	public void methodChanges() {
		try {
			getField("valorIva");
			getField("impuestoConsumo");
			getField("valorConsumo");
			getField("valorBruto");
			getField("porcentajeIva");
			Double iVa = null;
			Double cons = null;
			if (registro.getValorIva() == null) {
				registro.setValorIva(new BigDecimal(0));
			}
			if (registro.getValorConsumo() == null) {
				registro.setValorConsumo(new BigDecimal(0));
			}
			if (registro.getPorcentajeIva() == null) {
				registro.setPorcentajeIva((long) 0);
			} else {
				iVa = registro.getPorcentajeIva().doubleValue();
				if (iVa >= 1 && iVa <= 100) {
					iVa = iVa / 100;
				}
				if (registro.getValorBruto() != null) {
					registro.setValorIva(registro.getValorBruto().multiply(getValid().cast(iVa, BigDecimal.class)));
				}
			}
			if (registro.getImpuestoConsumo() == null) {
				registro.setImpuestoConsumo((long) 0);
			} else {
				cons = registro.getImpuestoConsumo().doubleValue();
				if (cons >= 1 && cons <= 100) {
					cons = cons / 100;
				}
				if (registro.getValorBruto() != null) {
					registro.setValorConsumo(
							registro.getValorBruto().multiply(getValid().cast(cons, BigDecimal.class)));
				}
			}
			if (registro.getValorConsumo() != null && registro.getValorIva() != null
					&& registro.getValorBruto() != null) {
				registro.setValorNeto(
						registro.getValorConsumo().add(registro.getValorIva().add(registro.getValorBruto())));
			}
			loadValueIntoForm(TypeGeneric.FIELD, "valorIva");
			loadValueIntoForm(TypeGeneric.FIELD, "valorConsumo");
			loadValueIntoForm(TypeGeneric.FIELD, "valorNeto");
		} catch (ValidateValueException e) {
			error(e);
		}
	}

	@Override
	public ResponsiveGridPane getGridPane(TypeGeneric typeGeneric) {
		return gridPane;
	}

	@Override
	public Integer getMaxColumns(TypeGeneric typeGeneric) {
		return 2;
	}

	@Override
	public MultiValuedMap<String, Object> getMapListToChoiceBox() {
		return mapListSelects;
	}

	@Override
	public Class<DetalleDTO> getClazz() {
		return DetalleDTO.class;
	}

	@Override
	public void visibleButtons() {
		var save = !DtoUtils.haveCode(registro) && PermissionUtil.INSTANCE().havePerm(
				PermissionConstants.CONST_PERM_CREATE, ListaDocumentosBean.class, getUsuario().getGrupoUser());
		var edit = DtoUtils.haveCode(registro) && PermissionUtil.INSTANCE().havePerm(
				PermissionConstants.CONST_PERM_UPDATE, ListaDocumentosBean.class, getUsuario().getGrupoUser());
		this.save.setValue(save);
		this.edit.setValue(edit);
	}
}
