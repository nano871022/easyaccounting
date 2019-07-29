package org.pyt.app.beans.dinamico;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.pyt.common.abstracts.ABean;
import org.pyt.common.abstracts.ADto;
import org.pyt.common.annotations.Inject;
import org.pyt.common.annotations.NoEdit;
import org.pyt.common.annotations.Operacion;
import org.pyt.common.annotations.Operaciones;
import org.pyt.common.annotations.Operar;
import org.pyt.common.common.SelectList;
import org.pyt.common.exceptions.QueryException;
import org.pyt.common.exceptions.ReflectionException;
import org.pyt.common.exceptions.validates.ValidateValueException;
import org.pyt.common.validates.ValidateValues;

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
	 * Detecta si el campo esta anotado con noEdit para que el campo se desabilite
	 * 
	 * @param nombreCampo {@link String}
	 * @return {@link Boolean}
	 */
	private final Boolean noEdit(String nombreCampo) {
		Field field;
		try {
			field = registro.getClass().getDeclaredField(nombreCampo);
			NoEdit noEdit = field.getDeclaredAnnotation(NoEdit.class);
			return noEdit != null;
		} catch (NoSuchFieldException | SecurityException e) {
			logger.logger(e);
		}
		return false;
	}

	/**
	 * Verifica si el campo esta anotado con {@link Operacion}, para realizar
	 * calculos
	 * 
	 * @param nombreCampo {@link String}
	 */
	@SuppressWarnings("unused")
	private final <S extends Object> void operacion(String nombreCampo) {
		Field field;
		try {
			field = registro.getClass().getDeclaredField(nombreCampo);
			if (field != null) {
				Operacion operacion = field.getDeclaredAnnotation(Operacion.class);
				operar(nombreCampo, operacion);
				Operaciones operaciones = field.getDeclaredAnnotation(Operaciones.class);
				if (operaciones == null)
					return;
				for (Operacion opera : operaciones.value()) {
					operar(nombreCampo, opera);
				}
			}
		} catch (NoSuchFieldException | SecurityException e) {
			logger.logger(e);
		}
	}

	/**
	 * Se encarga de operar los valores de los campos
	 * 
	 * @param nombreCampo {@link String}
	 * @param operacion   {@link Operacion}
	 */
	private final <M extends Object, N extends Object, S extends Object, R extends Object> void operar(
			String nombreCampo, Operacion operacion) {
		M value = null;
		N value1 = null;
		S value2 = null;
		R result = null;
		try {
			if (operacion == null)
				return;
			if (StringUtils.isNotBlank(operacion.valor1())) {
				value1 = registro.get(operacion.valor1());
			}
			if (StringUtils.isNotBlank(operacion.valor2())) {
				value2 = registro.get(operacion.valor2());
			}
			if (StringUtils.isNotBlank(nombreCampo)) {
				value = registro.get(nombreCampo);
			}
			if (Operar.SUMA == operacion.operacion()) {
				if (value2 == null)
					result = validateValue.sumar(value1, value);
				else
					result = validateValue.sumar(value1, value2);
			}
			if (Operar.MULTIPLICAR == operacion.operacion()) {
				if (value2 == null)
					result = validateValue.multiplicar(value1, value);
				else
					result = validateValue.multiplicar(value1, value2);
			}
			if (result != null) {
				if (validateValue.isCast(result, registro.getType(nombreCampo))) {
					registro.set(nombreCampo, validateValue.cast(result, registro.getType(nombreCampo)));
					putValueField(nombreCampo, result);
				}
			}
		} catch (ReflectionException | ValidateValueException e) {
			mensajeIzquierdo(e);
		}
	}

	/**
	 * Se encarga de poner en un campo el valor suministrado
	 * 
	 * @param nombreCampo {@link String}
	 * @param value       {@link Object} extends
	 */
	protected final <S extends Object> void putValueField(String nombreCampo, S value) {
		try {
			if (value == null)
				return;
			Object obj = this.fields.get(nombreCampo);
			if (obj instanceof TextField) {
				((TextField) obj).setText(validateValue.cast(value, String.class));
			}
		} catch (ValidateValueException e) {
			mensajeIzquierdo(e);
		}
	}

	/**
	 * Metodo cargado con cambio de texto en el campo
	 */
	public final void methodChange() {
		Field[] fields = registro.getClass().getDeclaredFields();
		for (Field field : fields) {
			Operacion operacion = field.getAnnotation(Operacion.class);
			Operaciones operaciones = field.getAnnotation(Operaciones.class);
			if (operacion != null) {
				getField(field.getName());
				getField(operacion.valor1());
				getField(operacion.valor2());
				operar(field.getName(), operacion);
			}
			if (operaciones != null) {
				getField(field.getName());
				for (Operacion opera : operaciones.value()) {
					getField(opera.valor1());
					getField(opera.valor2());
					operar(field.getName(), opera);
				}
			}
		} // end for
		loadGrid();
	}

	/**
	 * Se encarga de cargar los campos
	 * 
	 * @param nombre {@link String}
	 */
	protected final void getField(String nombre) {
		String valor = "";
		if (StringUtils.isBlank(nombre))
			return;
		Object campo = this.fields.get(nombre);
		if (campo instanceof TextField) {
			valor = ((TextField) campo).getText();
			try {
				registro.set(nombre, validateValue.cast(valor, registro.getType(nombre)));
			} catch (ReflectionException | ValidateValueException e) {
				logger.logger(e);
			}
		}
	}

	protected void methodChanges() {
	}

	/**
	 * Cuando se selecciona una opcion de un choicebox de tipo String agrega el
	 * valor dentro del campo registro en el campo configurado
	 * 
	 * @param docs
	 * @param select
	 */
	@SuppressWarnings({ "unchecked", "unused", "rawtypes" })
	private <N, M extends ADto> void selectionAction(DocumentosDTO docs, ChoiceBox<String> select) {
		try {
			List list = (List) listas.get(docs.getFieldName());
			if (list != null && list.size() > 0) {
				M obj = (M) SelectList.get(select, list, docs.getPutNameShow());
				if (StringUtils.isNotBlank(docs.getPutNameAssign()) && docs != null && obj != null) {
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
					mensajeIzquierdo("No se encontro lista para " + docs.getFieldName());
				} else {
					mensajeIzquierdo("No se encontro lista");
				}
			}
		} catch (ReflectionException e1) {
			mensajeIzquierdo(e1);
		}
	}

	/**
	 * Se encarga de crear un choice box de tipo string apartir de la inforamcion
	 * del DocumentDTo suministrado
	 * 
	 * @param docs {@link DocumentDTO}
	 * @return {@link ChoiceBox} < {@link String} >
	 */
	private final ChoiceBox<String> configChoiceBox(DocumentosDTO docs) {
		ChoiceBox<String> select = new ChoiceBox<String>();
		select.setDisable(!docs.getEdit());
		select.setDisable(noEdit(docs.getFieldName()));
		select.onActionProperty().set(e -> selectionAction(docs, select));
		select.setMaxWidth(1.7976931348623157E308);
		return select;
	}

	/**
	 * Se encarga de configurar el grid panel para el formulario
	 * 
	 * @return {@link GridPane}
	 */
	private final GridPane configGridPane() {
		var formulario = new GridPane();
		formulario.setHgap(5);
		formulario.setVgap(5);
		formulario.setMaxWidth(1.7976931348623157E308);
		formulario.setPadding(new Insets(10));
		formulario.setAlignment(Pos.CENTER);
		BorderPane.setAlignment(formulario, Pos.TOP_LEFT);
		return formulario;
	}

	/**
	 * Se encarga de crear la grilla con todos los campos que se encontraton y
	 * ponerlos en el fxml
	 */
	@SuppressWarnings({ "unchecked", "rawtypes", "unused" })
	protected final <N, M extends ADto> GridPane loadGrid() {
		Integer maxColumn = 2;
		if (campos == null) {
			mensajeIzquierdo("No se encontraron los campos para procesar.");
			return new GridPane();
		}
		Integer countFields = campos.size();
		var formulario = configGridPane();
		Integer columnIndex = 0;
		Integer rowIndex = 0;
		for (DocumentosDTO docs : campos) {
			Label label = new Label(docs.getFieldLabel());
			formulario.add(label, columnIndex, rowIndex);
			if (StringUtils.isNotBlank(docs.getPutNameShow())) {
				var select = configChoiceBox(docs);
				try {
					Class classe = docs.getObjectSearchDto();
					if (classe == null) {
						classe = registro.getType(docs.getFieldName());
					}
					putList(docs.getFieldName(), select, classe, docs.getPutNameShow(), docs.getSelectNameGroup(),
							registro.get(docs.getFieldName()), docs.getPutNameAssign());
				} catch (ReflectionException e) {
					mensajeIzquierdo(e);
				}
				formulario.add(select, columnIndex + 1, rowIndex);
				fields.put(docs.getFieldName(), select);
			} else {
				configNodeField(docs, formulario, columnIndex, rowIndex);
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
	 * Se encarga de configurar el {@link Node} Field cuando el campo
	 * {@link DocumentosDTO#PutNameShow Put Name show} se cneuntra vacio
	 * 
	 * @param docs        {@link DocumentosDTO}
	 * @param formulario  {@link GridPane}
	 * @param columnIndex {@link Integer}
	 * @param rowIndex    {@link Integer}
	 */
	private void configNodeField(DocumentosDTO docs, GridPane formulario, Integer columnIndex, Integer rowIndex) {
		try {
			Node field = getType(registro.getType(docs.getFieldName()), registro.get(docs.getFieldName()));
			if (field instanceof TextField)
				((TextField) field).focusedProperty().addListener(e -> methodChanges());
			if (field != null) {
				field.setDisable(!docs.getEdit());
				field.setDisable(noEdit(docs.getFieldName()));
				formulario.add(field, columnIndex + 1, rowIndex);
				fields.put(docs.getFieldName(), field);
			}
		} catch (ReflectionException e) {
			error(e);
		}
	}

	/**
	 * Se encarga de cargar la lista sobre los parametros
	 * 
	 * @param choiceBox {@link ChoiceBox}
	 * @param busqueda  {@link Class}
	 * @param show      {@link String} nombre de campo a mostrar del objeto busqueda
	 * @param grupo     {@link String} codigo del grupos a buscar
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
				mensajeIzquierdo(e);
			}
		}
	}

	/**
	 * Obtiene el campo en el cual sera usado para poner en el formulario generado
	 * apartir del tipo de dato que retorna el nombre del campo a usar
	 * 
	 * @param type {@link Class}
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
	 * @param fieldName {@link String}
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
	 * @param fieldName {@link String}
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
	@SuppressWarnings({ "rawtypes" })
	protected final void loadData() {
		fields.forEach((key, value) -> {
			if (value instanceof ChoiceBox) {
				loadValueToField((ChoiceBox) value, key);
			} else if (value instanceof TextField) {
				loadValueToField((TextField) value, key);
			} else if (value instanceof DatePicker) {
				loadValueToField((DatePicker) value, key);
			} else if (value instanceof CheckBox) {
				loadValueToField((CheckBox) value, key);
			}
		});
	}

	/**
	 * Cuando el valor que se desea ingresar es en un campo de tipo
	 * {@link ChoiceBox}
	 * 
	 * @param obj       {@link ChoiceBox}
	 * @param nameField {@link String}
	 */
	@SuppressWarnings({ "unchecked", "rawtypes", "hiding" })
	private final <T, M extends ADto> void loadValueToField(ChoiceBox obj, String nameField) {
		var list = (List) this.listas.get(nameField);
		var nameShow = getShow(nameField);
		if (StringUtils.isNotBlank(nameShow) && list != null && list.size() > 0) {
			M value = (M) SelectList.get((ChoiceBox) obj, list, nameShow);
			var nameAssign = getAssing(nameField);
			try {
				T value2 = null;
				if (value != null && StringUtils.isNotBlank(nameAssign)) {
					value2 = value.get(nameAssign);
				}
				registro.set(nameField, value2 != null ? value2 : value);
			} catch (ReflectionException e) {
				error(e);
			}
		}
	}

	/**
	 * Cuando el valor que se desea ingresar es en un campo de tipo
	 * {@link TextField}
	 * 
	 * @param obj       {@link TextField}
	 * @param nameField {@link String}
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private final void loadValueToField(TextField obj, String nameField) {
		var value = ((TextField) obj).getText();
		try {
			var clase = registro.getType(nameField);
			var valueEnd = validateValue.cast(value, clase);
			if (valueEnd != null) {
				registro.set(nameField, valueEnd);
			}
		} catch (ReflectionException | ValidateValueException e) {
			error(e);
		}
	}

	/**
	 * Cuando el valor que se desea ingresar es en un campo de tipo
	 * {@link DataPicker}
	 * 
	 * @param obj       {@link DataPicker}
	 * @param nameField {@link String}
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private final void loadValueToField(DatePicker obj, String nameField) {
		var ld = ((DatePicker) obj).getValue();
		Class clase;
		try {
			clase = registro.getType(nameField);
			var valueEnd = validateValue.cast(ld, clase);
			if (valueEnd != null) {
				registro.set(nameField, valueEnd);
			}
		} catch (ReflectionException | ValidateValueException e) {
			error(e);
		}
	}

	/**
	 * Cuando el valor que se desea ingresar es en un campo de tipo {@link CheckBox}
	 * 
	 * @param obj       {@link CheckBox}
	 * @param nameField {@link String}
	 */
	private final void loadValueToField(CheckBox obj, String nameField) {
		try {
			registro.set(nameField, ((CheckBox) obj).isSelected());
		} catch (ReflectionException e) {
			error(e);
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
					mensajeIzquierdo(e);
				}
			}
		}
		return valid;
	}

	protected final Map<String, Object> getFields() {
		return this.fields;
	}

	protected final ValidateValues getValid() {
		return validateValue;
	}
}
