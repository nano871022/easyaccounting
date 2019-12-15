package org.pyt.app.beans.dinamico;

import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.apache.commons.lang3.StringUtils;
import org.pyt.common.abstracts.ADto;
import org.pyt.common.annotations.Inject;
import org.pyt.common.common.SelectList;
import org.pyt.common.constants.AppConstants;
import org.pyt.common.constants.ParametroConstants;
import org.pyt.common.constants.StylesPrincipalConstant;
import org.pyt.common.constants.languages.Documento;
import org.pyt.common.exceptions.DocumentosException;
import org.pyt.common.exceptions.EmpresasException;
import org.pyt.common.exceptions.ParametroException;
import org.pyt.common.validates.ValidateValues;

import com.pyt.service.dto.DetalleDTO;
import com.pyt.service.dto.DocumentoDTO;
import com.pyt.service.dto.DocumentosDTO;
import com.pyt.service.dto.EmpresaDTO;
import com.pyt.service.dto.ParametroDTO;
import com.pyt.service.interfaces.IEmpresasSvc;

import co.com.arquitectura.annotation.proccessor.FXMLFile;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

/**
 * Se encarga de controlar el formulario dinamico para documentos
 * 
 * @author Alejandro Parra
 * @since 07-07-2018
 */
@FXMLFile(path = "view/dinamico", file = "formulario.fxml", name = "DocumentoDinamico")
public class DocumentoBean extends DinamicoBean<DocumentosDTO, DocumentoDTO> {
	@Inject(resource = "com.pyt.service.implement.EmpresaSvc")
	private IEmpresasSvc empresasSvc;
	@FXML
	private VBox central;
	@FXML
	private Label titulo;
	@FXML
	private ChoiceBox<ParametroDTO> tipoDocumentos;
	private ParametroDTO tipoDocumento;
	private List<ParametroDTO> listTipoDocumento;
	private ValidateValues valid;

	private GridPane gridPane;

	@FXML
	public void initialize() {
		super.initialize();
		gridPane = new GridPane();
		valid = new ValidateValues();
		registro = new DocumentoDTO();
		tipoDocumento = new ParametroDTO();
		try {
			listTipoDocumento = parametroSvc.getAllParametros(tipoDocumento, ParametroConstants.GRUPO_TIPO_DOCUMENTO);
		} catch (ParametroException e) {
			this.error(e);
		}
		tipoDocumento = new ParametroDTO();
		tipoDocumentos.onActionProperty().set(e -> loadField());
		tipoDocumentos.setConverter(new javafx.util.StringConverter<ParametroDTO>() {

			@Override
			public String toString(ParametroDTO object) {
				return object.getNombre();
			}

			@Override
			public ParametroDTO fromString(String string) {
				return listTipoDocumento.stream().filter(parametro -> parametro.getNombre().contains(string))
						.findFirst().get();
			}

		});
		SelectList.put(tipoDocumentos, listTipoDocumento);
		titulo.setText("");
	}

	/**
	 * Se encarga de realizar la busqueda de los campos configurados para el tipo de
	 * docuumento seleccionado
	 */
	@SuppressWarnings("unchecked")
	public final void loadField() {
		DocumentosDTO docs = new DocumentosDTO();
		ParametroDTO tipoDoc = SelectList.get(tipoDocumentos);
		if (tipoDoc != null) {
			docs.setDoctype(tipoDoc);
			docs.setClaseControlar(DocumentoDTO.class);
			try {
				campos = documentosSvc.getDocumentos(docs);
			} catch (DocumentosException e) {
				error(e);
			}
		}
		central.getChildren().clear();
		central.getChildren().add(gridPane);
		getListGenericsFields(TypeGeneric.FIELD).stream()
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
								getParametersSvc().getAllParametros(param)
										.forEach(reg -> mapListSelects.put(row.getFieldName(), reg));
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
		getListGenericsFields(TypeGeneric.FIELD).stream().filter(
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

		loadFields(TypeGeneric.FIELD, StylesPrincipalConstant.CONST_GRID_STANDARD);
	}

	/**
	 * Se encarga de cargar un nuevo registro
	 */
	public final void load(ParametroDTO tipoDocumento) {
		registro = new DocumentoDTO();
		registro.setTipoDocumento(tipoDocumento);
		this.tipoDocumentos.setDisable(true);
		SelectList.selectItem(tipoDocumentos, registro.getTipoDocumento());
		try {
			DocumentosDTO docs = new DocumentosDTO();
			docs.setDoctype(tipoDocumento);
			docs.setClaseControlar(DocumentoDTO.class);
			campos = documentosSvc.getDocumentos(docs);
		} catch (DocumentosException e) {
			error(e);
		}
	}

	public final void load(DocumentoDTO registro) {
		try {
			this.registro = registro;
			tipoDocumento = registro.getTipoDocumento();
			BigDecimal valores = sumaDetalles();
			SelectList.selectItem(tipoDocumentos, registro.getTipoDocumento());
			if (valores.compareTo(registro.getValor()) != 0) {
				this.registro.setValor(valores);
				documentosSvc.update(registro, getUsuario());
			}
		} catch (DocumentosException e) {
			error(e);
		}
	}

	private final BigDecimal sumaDetalles() {
		BigDecimal suma = new BigDecimal(0);
		try {
			DetalleDTO detalle = new DetalleDTO();
			detalle.setCodigoDocumento(registro.getCodigo());
			suma = documentosSvc.getAllDetalles(detalle).stream().map(detail -> detail.getValorNeto()).reduce(
					new BigDecimal(0), (v1, v2) -> ((BigDecimal) v1).add((BigDecimal) v2), (v1, v2) -> v1.add(v2));
		} catch (DocumentosException e) {
			error(e);
		}
		return suma;
	}

	/**
	 * Se encarga de guardar todo
	 */
	@SuppressWarnings("unchecked")
	public final void guardar() {
		if (valid()) {
			try {
				registro.setTipoDocumento(SelectList.get(tipoDocumentos));
				if (StringUtils.isNotBlank(registro.getCodigo())) {
					documentosSvc.update(registro, getUsuario());
					notificar(i18n().valueBundle(Documento.CONST_DOCUMENT_CREATED));
				} else {
					registro = documentosSvc.insert(registro, getUsuario());
					notificar(i18n().valueBundle(Documento.CONST_DOCUMENT_UPDATED));
				}
				comunicacion.setComando(AppConstants.COMMAND_PANEL_TIPO_DOC, registro);
			} catch (DocumentosException e) {
				error(e);
			}
		}
	}

	/**
	 * Se encarga de cancelar el almacenamiento de los datos
	 */
	public final void cancelar() {
		getController(ListaDocumentosBean.class).loadParameters(tipoDocumento.getValor2());
	}

	@Override
	public javafx.scene.layout.GridPane getGridPane(TypeGeneric typeGeneric) {
		return gridPane;
	}

	@Override
	public Integer getMaxColumns(TypeGeneric typeGeneric) {
		return 2;
	}

	@Override
	public Class<DocumentoDTO> getClazz() {
		return DocumentoDTO.class;
	}
}
