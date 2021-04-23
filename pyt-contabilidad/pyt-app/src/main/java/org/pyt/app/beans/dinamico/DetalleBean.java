package org.pyt.app.beans.dinamico;

import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.apache.commons.collections4.MultiValuedMap;
import org.apache.commons.lang3.StringUtils;
import org.controlsfx.glyphfont.FontAwesome.Glyph;
import org.pyt.common.abstracts.ADto;
import org.pyt.common.annotations.Inject;
import org.pyt.common.common.DtoUtils;
import org.pyt.common.common.ListUtils;
import org.pyt.common.common.UtilControlFieldFX;
import org.pyt.common.constants.ParametroConstants;
import org.pyt.common.constants.PermissionConstants;
import org.pyt.common.constants.StylesPrincipalConstant;
import org.pyt.common.exceptions.DocumentosException;
import org.pyt.common.exceptions.GenericServiceException;
import org.pyt.common.exceptions.ParametroException;
import org.pyt.common.exceptions.validates.ValidateValueException;

import com.pyt.service.dto.ActividadIcaDTO;
import com.pyt.service.dto.CentroCostoDTO;
import com.pyt.service.dto.ConceptoDTO;
import com.pyt.service.dto.DetalleDTO;
import com.pyt.service.dto.DocumentosDTO;
import com.pyt.service.dto.EmpresaDTO;
import com.pyt.service.dto.IngresoDTO;
import com.pyt.service.dto.ParametroDTO;
import com.pyt.service.dto.ServicioDTO;
import com.pyt.service.interfaces.IDocumentosSvc;
import com.pyt.service.interfaces.IGenericServiceSvc;
import com.pyt.service.interfaces.IParametrosSvc;

import co.com.arquitectura.annotation.proccessor.FXMLFile;
import co.com.japl.ea.common.button.apifluid.ButtonsImpl;
import co.com.japl.ea.utls.PermissionUtil;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
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
	@Inject
	private IGenericServiceSvc<ConceptoDTO> conceptoSvc;
	@FXML
	private VBox central;
	@FXML
	private Label titulo;
	private ParametroDTO tipoDocumento;
	private VBox panelCentral;
	private String codigoDocumento;
	private GridPane gridPane;
	@FXML
	private HBox buttons;

	@Override
	@FXML
	public void initialize() {
		super.initialize();
		registro = new DetalleDTO();
		tipoDocumento = new ParametroDTO();
		gridPane = new GridPane();
		gridPane = new UtilControlFieldFX().configGridPane(gridPane);
		ButtonsImpl.Stream(HBox.class).setLayout(buttons).setName("fxml.btn.save").action(this::guardar)
				.icon(Glyph.SAVE).isVisible(save).setName("fxml.btn.edit").action(this::guardar).icon(Glyph.EDIT)
				.isVisible(edit).setName("fxml.btn.cancel").action(this::regresar).build();
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
		this.registro.setCodigoDocumento(this.codigoDocumento);
		incrementarRenglon();
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
		visibleButtons();
		loadField();
	}

	private void incrementarRenglon() {
		var renglon = 0;
		try {
			logger.info("Start Incrementar renglon");
			if (registro != null && StringUtils.isBlank(registro.getCodigo())) {
				var detail = new DetalleDTO();
				detail.setCodigoDocumento(this.codigoDocumento);
				var result = documentosSvc.getAllDetalles(detail);
				logger.info("Registros " + result);
				if (ListUtils.isNotBlank(result)) {
					renglon = Integer.valueOf((int) result.stream().count()) + 1;
					logger.info("Cantidad registros encontrados" + renglon);
				}
			}
		} catch (Exception e) {
			error(e);
		}
		logger.info("End Incrementar renglon");
		registro.setRenglon(renglon);
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
			getField("valorUnidad");
			getField("cantidad");
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
			if (registro.getConcepto() != null && registro.getConcepto().getValorManoObra() != null) {
				registro.setValorUnidad(new BigDecimal(registro.getConcepto().getValorManoObra()));
			}
			if (registro.getValorUnidad() != null && registro.getCantidad() != null) {
				registro.setValorNeto(registro.getValorUnidad().multiply(new BigDecimal(registro.getCantidad())));
			}
			loadValueIntoForm(TypeGeneric.FIELD, "valorIva");
			loadValueIntoForm(TypeGeneric.FIELD, "valorConsumo");
			loadValueIntoForm(TypeGeneric.FIELD, "valorNeto");
			loadValueIntoForm(TypeGeneric.FIELD, "valorUnidad");
			loadValueIntoForm(TypeGeneric.FIELD, "cantidad");
		} catch (ValidateValueException e) {
			error(e);
		}
	}

	@Override
	public GridPane getGridPane(TypeGeneric typeGeneric) {
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
		var save = DtoUtils.haveNotCode(registro) && PermissionUtil.INSTANCE().havePerm(
				PermissionConstants.CONST_PERM_CREATE, ListaDocumentosBean.class, getUsuario().getGrupoUser());
		var edit = DtoUtils.haveCode(registro) && PermissionUtil.INSTANCE().havePerm(
				PermissionConstants.CONST_PERM_UPDATE, ListaDocumentosBean.class, getUsuario().getGrupoUser());
		this.save.setValue(save);
		this.edit.setValue(edit);
	}
}
