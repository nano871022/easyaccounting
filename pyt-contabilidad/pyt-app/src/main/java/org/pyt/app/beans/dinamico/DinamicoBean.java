package org.pyt.app.beans.dinamico;

import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDate;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.pyt.common.annotations.FXMLFile;
import org.pyt.common.annotations.Inject;
import org.pyt.common.common.ABean;
import org.pyt.common.common.ADto;
import org.pyt.common.common.SelectList;
import org.pyt.common.common.ValidateValues;
import org.pyt.common.constants.AppConstants;
import org.pyt.common.exceptions.DocumentosException;
import org.pyt.common.exceptions.ParametroException;
import org.pyt.common.exceptions.QueryException;
import org.pyt.common.exceptions.ReflectionException;
import org.pyt.common.exceptions.ValidateValueException;

import com.pyt.query.interfaces.IQuerySvc;
import com.pyt.service.dto.DocumentoDTO;
import com.pyt.service.dto.DocumentosDTO;
import com.pyt.service.dto.ParametroDTO;
import com.pyt.service.interfaces.IDocumentosSvc;
import com.pyt.service.interfaces.IParametrosSvc;

import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

/**
 * Se encarga de procesar la configuracion de l formulario dinamico y mostrarlo
 * para documentoDTO
 * 
 * @author Alejandro Parra
 * @since 01-07-2018
 */
@FXMLFile(path = "view/dinamico", file = "formulario.fxml")
public final class DinamicoBean extends ABean<DocumentoDTO> {
	@Inject(resource = "com.pyt.service.implement.ParametrosSvc")
	private IParametrosSvc parametrosSvc;
	@Inject(resource = "com.pyt.service.implement.DocumentosSvc")
	private IDocumentosSvc documentosSvc;
	@Inject(resource = "com.pyt.query.implement.QuerySvc")
	private IQuerySvc querySvc;
	@FXML
	private VBox central;
	@FXML
	private Label titulo;
	@FXML
	private ChoiceBox<String> tipoDocumentos;
	private Map<String, Object> listas;
	private Map<String, Object> fields;
	private ParametroDTO tipoDocumento;
	private List<DocumentosDTO> campos;
	private List<ParametroDTO> listTipoDocumento;
	public final static String FIELD_NAME = "nombre";
	private ValidateValues validateValue;

	@FXML
	public void initialize() {
		registro = new DocumentoDTO();
		listas = new HashMap<String, Object>();
		fields = new HashMap<String, Object>();
		tipoDocumento = new ParametroDTO();
		validateValue = new ValidateValues();
		try {
			listTipoDocumento = parametrosSvc.getAllParametros(tipoDocumento);
		} catch (ParametroException e) {
			error(e);
		}
		tipoDocumento = new ParametroDTO();
		tipoDocumentos.onActionProperty().set(e -> loadField());
		SelectList.put(tipoDocumentos, listTipoDocumento, FIELD_NAME);
	}
	/**
	 * Se encarga de cargar un nuevo registro
	 */
	public final void load() {
		
	}
	
	/**
	 * Se encarga de realizar la busqueda de los campos configurados para el tipo de
	 * docuumento seleccionado
	 */
	public final void loadField() {
		DocumentosDTO docs = new DocumentosDTO();
		ParametroDTO tipoDoc = SelectList.get(tipoDocumentos, listTipoDocumento, FIELD_NAME);
		if (tipoDoc != null) {
			docs.setDoctype(tipoDoc);
			docs.setClaseControlar(DocumentoDTO.class);
			try {
				campos = documentosSvc.getDocumentos(docs);
			} catch (DocumentosException e) {
				error(e);
			}
		}
		loadGrid();
	}

	/**
	 * Se encarga de crear la grilla con todos los campos que se encontraton y
	 * ponerlos en el fxml
	 */
	private final <N, M extends ADto> void loadGrid() {
		Integer maxColumn = 2;
		Integer countFields = campos.size();
		GridPane formulario = new GridPane();
		formulario.setHgap(5);
		formulario.setVgap(5);
		formulario.setMaxWidth(1.7976931348623157E308);
		formulario.setPadding(new Insets(10));
		formulario.setAlignment(Pos.CENTER);
		BorderPane.setAlignment(formulario, Pos.TOP_LEFT);
		DocumentoDTO doc = new DocumentoDTO();
		Integer columnIndex = 0;
		Integer rowIndex = 0;
		for (DocumentosDTO docs : campos) {
			Label label = new Label(docs.getFieldLabel());
			formulario.add(label, columnIndex, rowIndex);
			if (StringUtils.isNotBlank(docs.getPutNameShow())) {
				ChoiceBox<String> select = new ChoiceBox<String>();
				select.setDisable(!docs.getEdit());
				select.onActionProperty().set(e -> {
					try {
						List list = (List) listas.get(docs.getFieldName());
						M obj = (M) SelectList.get(select, list, docs.getPutNameShow());
						if (StringUtils.isNotBlank(docs.getPutNameAssign())) {
							N value = obj.get(docs.getPutNameAssign());
							if (value != null) {
								registro.set(docs.getFieldName(), value);
							}
						} else {
							if (obj != null) {
								registro.set(docs.getFieldName(), obj);
							}
						}
					} catch (ReflectionException e1) {
						System.err.println(e);
					}
				});
				select.setMaxWidth(1.7976931348623157E308);
				Class classe = docs.getObjectSearchDto();
				if (classe == null) {
					try {
						classe = doc.getType(docs.getFieldName());
						putList(docs.getFieldName(), select, classe, docs.getPutNameShow(), docs.getSelectNameGroup());
					} catch (ReflectionException e) {
						error(e);
					}
				}
				formulario.add(select, columnIndex + 1, rowIndex);
				fields.put(docs.getFieldName(), select);
			} else {
				try {
					Node field = getType(doc.getType(docs.getFieldName()));
					field.setDisable(!docs.getEdit());
					if (field != null) {
						formulario.add(field, columnIndex + 1, rowIndex);
						fields.put(docs.getFieldName(), field);
					}
				} catch (ReflectionException e) {
					error(e);
				}
			}
			if (countFields < 6 || columnIndex == maxColumn) {
				rowIndex++;
				columnIndex = 0;
			} else {
				columnIndex += 2;
			}
		} // end for
		central.getChildren().clear();
		central.getChildren().add(formulario);
	}

	/**
	 * Se encarga de cargar la lista sobre los parametros
	 * 
	 * @param choiceBox
	 *            {@link ChoiceBox}
	 * @param busqueda
	 *            {@link Class}
	 * @param show
	 *            {@link String} nombre de campo a mostrar del objeto busqueda
	 * @param grupo
	 *            {@link String} codigo del grupos a buscar
	 */
	@SuppressWarnings("unchecked")
	private final <T extends Object, S extends ADto> void putList(String nameField, ChoiceBox<String> choiceBox,
			Class<T> busqueda, String show, String grupo) {
		T dto = null;
		if (busqueda == ParametroDTO.class) {
			dto = (T) new ParametroDTO();
			((ParametroDTO) dto).setGrupo(grupo);
		} else {
			try {
				dto = busqueda.getConstructor().newInstance();
			} catch (InstantiationException | IllegalAccessException | IllegalArgumentException
					| InvocationTargetException | NoSuchMethodException | SecurityException e) {
				error(e);
			}
		}
		if (dto != null) {
			try {
				List<S> lista = querySvc.gets((S) dto);
				SelectList.put(choiceBox, lista, show);
				this.listas.put(nameField, lista);
				choiceBox.getSelectionModel().selectFirst();
			} catch (QueryException e) {
				error(e);
			}
		}
	}

	/**
	 * Obtiene el campo en el cual sera usado para poner en el formulario generado
	 * apartir del tipo de dato que retorna el nombre del campo a usar
	 * 
	 * @param type
	 *            {@link Class}
	 * @return {@link Node} campos javafx
	 */
	private final <T extends Object> Node getType(Class<T> type) {
		if (type == Date.class) {
			return new DatePicker();
		}
		if (type == LocalDate.class) {
			return new DatePicker();
		}
		if (type == Boolean.class) {
			return new CheckBox();
		}
		if (type == String.class) {
			return new TextField();
		}
		if (type == Double.class) {
			return new TextField();
		}
		if (type == Integer.class) {
			return new TextField();
		}
		if (type == BigDecimal.class) {
			return new TextField();
		}
		if (type == BigInteger.class) {
			return new TextField();
		}
		if (type == Long.class) {
			return new TextField();
		}
		if (type == Float.class) {
			return new TextField();
		}
		return null;
	}

	/**
	 * Se encarga de obtener el campo de mostrar apartir del nombre del campo a usar
	 * 
	 * @param fieldName
	 *            {@link String}
	 * @return {@link String}
	 */
	private final String getShow(String fieldName) {
		for (DocumentosDTO doc : campos) {
			if (doc.getFieldName().contains(fieldName)) {
				return doc.getPutNameShow();
			}
		}
		return null;
	}

	/**
	 * Se encagra de obtener el nombre de asignacion del nombre del campo
	 * 
	 * @param fieldName
	 *            {@link String}
	 * @return {@link String}
	 */
	private final String getAssing(String fieldName) {
		for (DocumentosDTO doc : campos) {
			if (doc.getFieldName().contains(fieldName)) {
				return doc.getPutNameAssign();
			}
		}
		return null;
	}

	/**
	 * Se encarga de cargar los datos de los campos
	 */
	private final <M extends ADto> void loadData() {
		Set<String> sets = fields.keySet();
		for (String set : sets) {
			Object obj = fields.get(set);
			if (obj instanceof ChoiceBox) {
				List list = (List) this.listas.get(set);
				String nameShow = getShow(set);
				if (StringUtils.isNotBlank(nameShow)) {
					M value = (M) SelectList.get((ChoiceBox) obj, list, nameShow);
					String nameAssign = getAssing(set);
					if (StringUtils.isNotBlank(nameAssign)) {
						try {
							Object valuer = value.get(nameAssign);
							registro.set(set, valuer);
						} catch (ReflectionException e) {
							System.err.println(e);
						}
					} else {
						try {
							if (value != null) {
								registro.set(set, value);
							}
						} catch (ReflectionException e) {
							System.err.println(e);
						}
					}
				}
			} else if (obj instanceof TextField) {
				String value = ((TextField) obj).getText();
				try {
					Class clase = registro.getType(set);
					Object valueEnd = validateValue.cast(value, clase);
					if (valueEnd != null) {
						registro.set(set, valueEnd);
					}
				} catch (ReflectionException e) {
					System.err.println(e);
				} catch (ValidateValueException e) {
					System.err.println(e);
				}
			} else if (obj instanceof DatePicker) {
				LocalDate ld = ((DatePicker) obj).getValue();
				Class clase;
				try {
					clase = registro.getType(set);
					Object valueEnd = validateValue.cast(ld, clase);
					if (valueEnd != null) {
						registro.set(set, valueEnd);
					}
				} catch (ReflectionException e) {
					System.err.println(e);
				} catch (ValidateValueException e) {
					System.err.println(e);
				}

			} else if (obj instanceof CheckBox) {
				try {
					registro.set(set, ((CheckBox) obj).isSelected());
				} catch (ReflectionException e) {
					System.err.println(e);
				}
			}
		}
	}

	/**
	 * Se encarga de validar los campos del formulario
	 * 
	 * @return
	 */
	private final Boolean valid() {
		Boolean valid = true;
		for (DocumentosDTO dto : campos) {
			if (dto.getNullable()) {
				try {
					if (registro.get(dto.getFieldName()) != null) {
						valid &= false;
					}
				} catch (ReflectionException e) {
					System.err.println(e);
				}
			}
		}
		return valid;
	}

	/**
	 * Se encarga de guardar todo
	 */
	@SuppressWarnings("unchecked")
	public final void guardar() {
		loadData();
		if (valid()) {
			try {
				registro = documentosSvc.insert(registro, userLogin);
				notificar("Se agrego el nuevo documento.");
				comunicacion.setComando(AppConstants.COMMAND_PANEL_TIPO_DOC, registro);
			} catch (DocumentosException e) {
				System.err.println(e);
			}
		}
	}

	/**
	 * Se encarga de cancelar el almacenamiento de los datos
	 */
	public final void cancelar() {

	}
}
