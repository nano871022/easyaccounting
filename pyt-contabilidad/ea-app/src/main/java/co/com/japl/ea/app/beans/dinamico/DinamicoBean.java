package co.com.japl.ea.app.beans.dinamico;

import static co.com.japl.ea.interfaces.IGenericCommons.TypeGeneric.FIELD;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections4.MultiValuedMap;
import org.apache.commons.collections4.multimap.ArrayListValuedHashMap;
import org.apache.commons.lang3.StringUtils;
import org.pyt.common.annotations.Inject;
import org.pyt.common.annotations.Operacion;
import org.pyt.common.annotations.Operaciones;
import org.pyt.common.annotations.Operar;
import org.pyt.common.constants.StylesPrincipalConstant;

import co.com.japl.ea.beans.abstracts.ABean;
import co.com.japl.ea.common.abstracts.ADto;
import co.com.japl.ea.common.validates.ValidateValues;
import co.com.japl.ea.dto.dto.DocumentosDTO;
import co.com.japl.ea.dto.interfaces.IDocumentosSvc;
import co.com.japl.ea.dto.interfaces.IGenericServiceSvc;
import co.com.japl.ea.dto.interfaces.IParametrosSvc;
import co.com.japl.ea.exceptions.ReflectionException;
import co.com.japl.ea.exceptions.validates.ValidateValueException;
import co.com.japl.ea.interfaces.IGenericFields;
import javafx.scene.Node;
import javafx.scene.control.TextField;

/**
 * Se encarga de procesar la configuracion de l formulario dinamico y mostrarlo
 * para documentoDTO
 * 
 * @author Alejandro Parra
 * @since 01-07-2018
 */

public abstract class DinamicoBean<S extends ADto, F extends ADto> extends ABean<F>
		implements IGenericFields<DocumentosDTO, F> {
	@Inject(resource = "com.pyt.service.implement.DocumentosSvc")
	protected IDocumentosSvc documentosSvc;
	@Inject(resource = "co.com.japl.ea.query.implement.GenericServiceSvc")
	private IGenericServiceSvc<S> querySvc;
	private Map<String, Object> listas;
	private MultiValuedMap<String, Node> fields;
	protected List<DocumentosDTO> campos;
	public final static String FIELD_NAME = "nombre";
	private ValidateValues validateValue;
	@Inject(resource = "co.com.japl.ea.query.implement.ParametrosSvc")
	protected IParametrosSvc parametroSvc;
	protected MultiValuedMap<String, Object> mapListSelects;

	public void initialize() {
		listas = new HashMap<String, Object>();
		fields = new ArrayListValuedHashMap<>();
		validateValue = new ValidateValues();
		mapListSelects = new ArrayListValuedHashMap<>();
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
					loadValueIntoForm(FIELD, nombreCampo);
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
		this.loadFields(TypeGeneric.FIELD, StylesPrincipalConstant.CONST_GRID_STANDARD);
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

	@Override
	public MultiValuedMap<String, Node> getMapFields(TypeGeneric typGeneric) {
		return this.fields;
	}

	protected final ValidateValues getValid() {
		return validateValue;
	}

	@Override
	public List<DocumentosDTO> getListGenericsFields(TypeGeneric typeGeneric) {
		return campos;
	}

	@Override
	public F getInstanceDto(TypeGeneric typeGeneric) {
		return registro;
	}

	@Override
	public IParametrosSvc getParametersSvc() {
		return parametroSvc;
	}

	@Override
	public MultiValuedMap<String, Object> getMapListToChoiceBox() {
		return mapListSelects;
	}

	protected abstract void visibleButtons();
}
