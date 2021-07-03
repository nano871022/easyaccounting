package co.com.japl.ea.app.beans.dinamico;

import static org.pyt.common.common.InstanceObject.instance;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

import org.apache.commons.collections4.MultiValuedMap;
import org.apache.commons.lang3.StringUtils;
import org.controlsfx.glyphfont.FontAwesome.Glyph;
import org.pyt.common.annotations.Inject;
import org.pyt.common.common.DtoUtils;
import org.pyt.common.constants.PermissionConstants;
import org.pyt.common.constants.StylesPrincipalConstant;

import co.com.arquitectura.annotation.proccessor.FXMLFile;
import co.com.japl.ea.app.custom.ResponsiveGridPane;
import co.com.japl.ea.common.abstracts.ADto;
import co.com.japl.ea.common.button.apifluid.ButtonsImpl;
import co.com.japl.ea.dto.dto.ConceptoDTO;
import co.com.japl.ea.dto.dto.CuentaContableDTO;
import co.com.japl.ea.dto.dto.DetalleContableDTO;
import co.com.japl.ea.dto.dto.DocumentosDTO;
import co.com.japl.ea.dto.dto.ParametroDTO;
import co.com.japl.ea.dto.interfaces.IDocumentosSvc;
import co.com.japl.ea.dto.interfaces.IGenericServiceSvc;
import co.com.japl.ea.dto.interfaces.IParametrosSvc;
import co.com.japl.ea.exceptions.DocumentosException;
import co.com.japl.ea.exceptions.GenericServiceException;
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
@FXMLFile(path = "view/dinamico", file = "detalleContable.fxml")
public class DetalleContableBean extends DinamicoBean<DocumentosDTO, DetalleContableDTO> {
	@Inject(resource = "com.pyt.service.implement.ParametrosSvc")
	private IParametrosSvc parametrosSvc;
	@Inject(resource = "com.pyt.service.implement.DocumentosSvc")
	private IDocumentosSvc documentoSvc;
	@Inject
	private IGenericServiceSvc<ConceptoDTO> conceptoSvc;
	@Inject
	private IGenericServiceSvc<CuentaContableDTO> cuentaContableSvc;
	@FXML
	private VBox central;
	private VBox centro;
	@FXML
	private Label titulo;
	private ParametroDTO tipoDocumento;
	private String codigoDocumento;
	private ResponsiveGridPane gridPane;
	@FXML
	private HBox buttons;

	@FXML
	public void initialize() {
		super.initialize();
		registro = new DetalleContableDTO();
		tipoDocumento = new ParametroDTO();
		gridPane = new ResponsiveGridPane();
//		gridPane = new UtilControlFieldFX().configGridPane(gridPane);
		visibleButtons();
		ButtonsImpl.Stream(HBox.class).setLayout(buttons).setName("fxml.btn.save").action(this::guardar)
				.icon(Glyph.SAVE).isVisible(save).setName("fxml.btn.edit").action(this::guardar).icon(Glyph.SAVE)
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
			docs.setClaseControlar(DetalleContableDTO.class);
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
		getListGenericsFields(TypeGeneric.FIELD).stream().filter(row -> {
			Object instance;
			try {
				instance = instance(row.getClaseControlar());
				var clazz = ((ADto) instance).getType(row.getFieldName());
				clazz.asSubclass(ADto.class);
				return true;
			} catch (InstantiationException | IllegalAccessException | IllegalArgumentException
					| InvocationTargetException | NoSuchMethodException | SecurityException | ClassCastException e) {
				logger().logger(e);
				return false;
			}
		}).forEach(row -> {
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

			} catch (ClassCastException | InstantiationException | IllegalAccessException | IllegalArgumentException
					| InvocationTargetException | NoSuchMethodException | SecurityException e) {
				logger().logger(e);
			} catch (GenericServiceException e) {
				logger().logger(e);
			}
		});
	}

	/**
	 * Se encarga de cargar un nuevo registro
	 */
	public final void load(VBox centro, ParametroDTO tipoDoc, String codigoDocumento) {
		registro = new DetalleContableDTO();
		tipoDocumento = tipoDoc;
		this.centro = centro;
		this.codigoDocumento = codigoDocumento;
		titulo.setText(concat(titulo.getText(), ": ", tipoDoc.getNombre()));
		visibleButtons();
		loadField();
	}

	public final void load(VBox centro, DetalleContableDTO registro, ParametroDTO tipoDoc, String codigoDocumento) {
		this.registro = registro;
		tipoDocumento = tipoDoc;
		this.centro = centro;
		this.codigoDocumento = codigoDocumento;
		titulo.setText(concat(titulo.getText(), ": ", tipoDoc.getNombre()));
		visibleButtons();
		loadField();
	}

	/**
	 * Se encarga de guardar todo
	 */
	public final void guardar() {
		if (validFields()) {
			try {
				if (StringUtils.isNotBlank(registro.getCodigo())) {
					documentosSvc.update(registro, getUsuario());
					notificarI18n("mensaje.accountingdetail.have.been.updated.succesfull");
				} else {
					registro.setCodigoDocumento(codigoDocumento);
					registro = documentosSvc.insert(registro, getUsuario());
					notificarI18n("mensaje.accountingdetail.was.inserted");
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
			getController(centro, ListaDetalleContableBean.class).load(centro, tipoDocumento, codigoDocumento);
		} catch (Exception e) {
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
	public Class<DetalleContableDTO> getClazz() {
		return DetalleContableDTO.class;
	}

	@Override
	protected void visibleButtons() {
		var save = !DtoUtils.haveCode(registro) && PermissionUtil.INSTANCE().havePerm(
				PermissionConstants.CONST_PERM_CREATE, ListaDocumentosBean.class, getUsuario().getGrupoUser());
		var edit = DtoUtils.haveCode(registro) && PermissionUtil.INSTANCE().havePerm(
				PermissionConstants.CONST_PERM_UPDATE, ListaDocumentosBean.class, getUsuario().getGrupoUser());
		this.save.setValue(save);
		this.edit.setValue(edit);
	}

}
