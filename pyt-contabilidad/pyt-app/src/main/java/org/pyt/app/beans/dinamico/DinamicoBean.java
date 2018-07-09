package org.pyt.app.beans.dinamico;

import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.pyt.common.annotations.Inject;
import org.pyt.common.common.ABean;
import org.pyt.common.common.ADto;
import org.pyt.common.common.SelectList;
import org.pyt.common.common.ValidateValues;
import org.pyt.common.exceptions.QueryException;
import org.pyt.common.exceptions.ReflectionException;
import org.pyt.common.exceptions.ValidateValueException;

import com.pyt.query.interfaces.IQuerySvc;
import com.pyt.service.dto.DocumentosDTO;
import com.pyt.service.dto.ParametroDTO;
import com.pyt.service.interfaces.IDocumentosSvc;

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

/**
 * Se encarga de procesar la configuracion de l formulario dinamico y mostrarlo
 * para documentoDTO
 * 
 * @author Alejandro Parra
 * @since 01-07-2018
 */

public abstract class DinamicoBean<T extends ADto> extends ABean<T> {
	@Inject(resource = "com.pyt.service.implement.DocumentosSvc")
	protected IDocumentosSvc documentosSvc;
	@Inject(resource = "com.pyt.query.implement.QuerySvc")
	private IQuerySvc querySvc;
	private Map<String, Object> listas;
	private Map<String, Object> fields;
	protected List<DocumentosDTO> campos;
	public final static String FIELD_NAME = "nombre";
	private ValidateValues validateValue;

	public void initialize() {
		listas = new HashMap<String, Object>();
		fields = new HashMap<String, Object>();
		validateValue = new ValidateValues();
	}

	/**
	 * Se encarga de crear la grilla con todos los campos que se encontraton y
	 * ponerlos en el fxml
	 */
	@SuppressWarnings({ "unchecked", "rawtypes", "unused" })
	protected final <N, M extends ADto> GridPane loadGrid() {
		Integer maxColumn = 2;
		if (campos == null) {
			error("No se encontraron los campos para procesar.");
			return new GridPane();
		}
		Integer countFields = campos.size();
		GridPane formulario = new GridPane();
		formulario.setHgap(5);
		formulario.setVgap(5);
		formulario.setMaxWidth(1.7976931348623157E308);
		formulario.setPadding(new Insets(10));
		formulario.setAlignment(Pos.CENTER);
		BorderPane.setAlignment(formulario, Pos.TOP_LEFT);
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
						if (list != null && list.size() > 0) {
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
						} else {
							if (docs != null) {
								error("No se encontro lista para " + docs.getFieldName());
							} else {
								error("No se encontro lista");
							}
						}
					} catch (ReflectionException e1) {
						error(e1);
					}
				});
				select.setMaxWidth(1.7976931348623157E308);
				try {
					Class classe = docs.getObjectSearchDto();
					if (classe == null) {
						classe = registro.getType(docs.getFieldName());
					}
					putList(docs.getFieldName(), select, classe, docs.getPutNameShow(), docs.getSelectNameGroup(),
							registro.get(docs.getFieldName()), docs.getPutNameAssign());
				} catch (ReflectionException e) {
					error(e);
				}
				formulario.add(select, columnIndex + 1, rowIndex);
				fields.put(docs.getFieldName(), select);
			} else {
				try {
					Node field = getType(registro.getType(docs.getFieldName()), registro.get(docs.getFieldName()));
					if (field != null) {
						field.setDisable(!docs.getEdit());
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
		return formulario;
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
	@SuppressWarnings({ "unchecked", "hiding" })
	private final <L, T extends Object, S extends ADto> void putList(String nameField, ChoiceBox<String> choiceBox,
			Class<T> busqueda, String show, String grupo, L value, String assign) {
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
				if (StringUtils.isNotBlank(assign)) {
					if (value != null) {
						SelectList.selectItem(choiceBox, lista, show, value, assign);
					} else {
						choiceBox.getSelectionModel().selectFirst();
					}
				} else {
					if (value instanceof ADto) {
						SelectList.selectItem(choiceBox, lista, show, (S) value);
					} else {
						choiceBox.getSelectionModel().selectFirst();
					}
				}
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
	@SuppressWarnings("hiding")
	private final <T extends Object> Node getType(Class<T> type, T value) {
		if (type == Date.class) {
			DatePicker dp = new DatePicker();
			if (validateValue.isCast(value, Date.class)) {
				LocalDate ld = ((Date) value).toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
				dp.setValue(ld);
			}
			return dp;
		}
		if (type == LocalDate.class) {
			DatePicker dp = new DatePicker();
			if (validateValue.isCast(value, LocalDate.class))
				dp.setValue((LocalDate) value);
			return dp;
		}
		if (type == Boolean.class) {
			CheckBox cb = new CheckBox();
			if (validateValue.isCast(value, Boolean.class))
				cb.setSelected((Boolean) value);
			return cb;
		}
		if (type == String.class) {
			TextField tf = new TextField();
			if (validateValue.isCast(value, String.class))
				tf.setText((String) value);
			return tf;
		}
		if (type == Double.class) {
			TextField tf = new TextField();
			if (validateValue.isCast(value, Double.class))
				tf.setText(String.valueOf((Double) value));
			return tf;
		}
		if (type == Integer.class) {
			TextField tf = new TextField();
			if (validateValue.isCast(value, Integer.class))
				tf.setText(String.valueOf((Integer) value));
			return tf;
		}
		if (type == BigDecimal.class) {
			TextField tf = new TextField();
			if (validateValue.isCast(value, BigDecimal.class))
				tf.setText(String.valueOf((BigDecimal) value));
			return tf;
		}
		if (type == BigInteger.class) {
			TextField tf = new TextField();
			if (validateValue.isCast(value, BigInteger.class))
				tf.setText(String.valueOf((BigInteger) value));
			return tf;
		}
		if (type == Long.class) {
			TextField tf = new TextField();
			if (validateValue.isCast(value, Long.class))
				tf.setText(String.valueOf((Long) value));
			return tf;
		}
		if (type == Float.class) {
			TextField tf = new TextField();
			if (validateValue.isCast(value, Float.class))
				tf.setText(String.valueOf((Float) value));
			return tf;
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
	@SuppressWarnings({ "unchecked", "rawtypes" })
	protected final <M extends ADto> void loadData() {
		Set<String> sets = fields.keySet();
		for (String set : sets) {
			Object obj = fields.get(set);
			if (obj instanceof ChoiceBox) {
				List list = (List) this.listas.get(set);
				String nameShow = getShow(set);
				if (StringUtils.isNotBlank(nameShow) && list != null && list.size() > 0) {
					M value = (M) SelectList.get((ChoiceBox) obj, list, nameShow);
					String nameAssign = getAssing(set);
					if (StringUtils.isNotBlank(nameAssign)) {
						try {
							Object valuer = value.get(nameAssign);
							registro.set(set, valuer);
						} catch (ReflectionException e) {
							error(e);
						}
					} else {
						try {
							if (value != null) {
								registro.set(set, value);
							}
						} catch (ReflectionException e) {
							error(e);
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
					error(e);
				} catch (ValidateValueException e) {
					error(e);
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
					error(e);
				} catch (ValidateValueException e) {
					error(e);
				}

			} else if (obj instanceof CheckBox) {
				try {
					registro.set(set, ((CheckBox) obj).isSelected());
				} catch (ReflectionException e) {
					error(e);
				}
			}
		}
	}

	/**
	 * Se encarga de validar los campos del formulario
	 * 
	 * @return
	 */
	protected final Boolean valid() {
		Boolean valid = true;
		for (DocumentosDTO dto : campos) {
			if (dto.getNullable()) {
				try {
					if (registro.get(dto.getFieldName()) == null) {
						valid &= false;
					}
				} catch (ReflectionException e) {
					error(e);
				}
			}
		}
		return valid;
	}
}
