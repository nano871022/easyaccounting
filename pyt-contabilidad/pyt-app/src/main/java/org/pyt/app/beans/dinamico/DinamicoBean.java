package org.pyt.app.beans.dinamico;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.pyt.common.abstracts.ADto;
import org.pyt.common.annotations.Inject;
import org.pyt.common.annotations.Operacion;
import org.pyt.common.annotations.Operaciones;
import org.pyt.common.annotations.Operar;
import org.pyt.common.common.I18n;
import org.pyt.common.common.Log;
import org.pyt.common.exceptions.ReflectionException;
import org.pyt.common.exceptions.validates.ValidateValueException;
import org.pyt.common.validates.ValidateValues;

import com.pyt.query.interfaces.IQuerySvc;
import com.pyt.service.dto.DocumentosDTO;
import com.pyt.service.interfaces.IDocumentosSvc;

import co.com.japl.ea.beans.abstracts.ABean;
import co.com.japl.ea.interfaces.IGenericFieldLoad;
import co.com.japl.ea.interfaces.IGenericFieldValueToField;
import co.com.japl.ea.interfaces.IGenericLoadValueFromField;
import javafx.scene.control.TextField;

/**
 * Se encarga de procesar la configuracion de l formulario dinamico y mostrarlo
 * para documentoDTO
 * 
 * @author Alejandro Parra
 * @since 01-07-2018
 */

public abstract class DinamicoBean<T extends ADto> extends ABean<T>
		implements IGenericFieldLoad, IGenericLoadValueFromField, IGenericFieldValueToField {
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

	@Override
	public Map<String, Object> getConfigFields() {
		return this.fields;
	}

	protected final ValidateValues getValid() {
		return validateValue;
	}

	@Override
	public void warning(String msn) {
		super.alerta(msn);
	}

	@Override
	public void error(Throwable error) {
		super.error(error);
	}

	@Override
	public Log getLogger() {
		return logger();
	}

	@Override
	public I18n getI18n() {
		return i18n();
	}

	@Override
	public List<DocumentosDTO> getFields() {
		return campos;
	}

	@Override
	public Map<String, Object> getConfigFieldTypeList() {
		return listas;
	}

	@Override
	public IQuerySvc getServiceSvc() {
		return querySvc;
	}

	@SuppressWarnings({ "unchecked", "hiding" })
	@Override
	public <T extends ADto> T getInstanceDTOUse() {
		return (T) registro;
	}
}
