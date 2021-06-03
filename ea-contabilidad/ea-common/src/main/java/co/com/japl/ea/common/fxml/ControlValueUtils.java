package co.com.japl.ea.common.fxml;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.pyt.common.common.Log;
import org.pyt.common.common.SelectList;
import org.pyt.common.constants.AppConstants;

import co.com.japl.ea.common.abstracts.ADto;
import co.com.japl.ea.common.validates.ValidateValues;
import javafx.scene.Node;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;

public final class ControlValueUtils {
	private static ControlValueUtils cvu;
	private ValidateValues vv;
	private final static String CONST_METHOD_NAME = "controlAssingValue";
	private final static String CONST_METHOD_CLEAN_NAME = "cleanNode";
	private final Log logger = Log.Log(this.getClass());

	private ControlValueUtils() {
		vv = new ValidateValues();
	}

	public final static ControlValueUtils INSTANCE() {
		return Optional.ofNullable(cvu).orElse(new ControlValueUtils());
	}

	public <D extends ADto, N extends Node, V> void setValue(N node, V value) {
		if (value != null || node != null) {
			getMethods(CONST_METHOD_NAME).stream()
					.filter(method -> vv.isCast(node, method.getParameterTypes()[0]))
					.filter(method -> vv.isCast(value, method.getParameterTypes()[1]))
					.forEach(method -> {
					try {
							method.invoke(this, vv.cast(node, method.getParameterTypes()[0]),
									vv.cast(value, method.getParameterTypes()[1]));
					} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
						logger.DEBUG(e);
					}
				});
		}
	}

	public <D extends ADto, N extends Node, V> void cleanValue(N node) {
		if (node != null) {
			getMethods(CONST_METHOD_CLEAN_NAME).stream()
					.filter(method -> method.getParameterTypes()[0] == node.getClass())
				.forEach(method -> {
					try {
							method.invoke(this, vv.cast(node, method.getParameterTypes()[0]));
					} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
						logger.DEBUG(e);
					}
				});
		}
	}
	
	private List<Method> getMethods(String nameMethod) {
		Method[] methods = this.getClass().getDeclaredMethods();
		return Arrays.asList(methods).stream().filter(method -> nameMethod.contains(method.getName()))
				.collect(Collectors.toList());
	}

	@SuppressWarnings("unused")
	private final void controlAssingValue(TextField control, String value) {
		control.setText(value);
	}

	@SuppressWarnings("unused")
	private final void controlAssingValue(CheckBox control, Boolean bool) {
		control.setSelected(bool);
	}

	@SuppressWarnings("unused")
	private final void controlAssingValue(DatePicker control, LocalDate date) {
		control.valueProperty().setValue(date);
	}

	@SuppressWarnings("unused")
	private final void controlAssingValue(DatePicker control, Date date) {
		var localDate = LocalDate.ofInstant(date.toInstant(), ZoneId.systemDefault());
		control.valueProperty().setValue(localDate);
	}

	@SuppressWarnings("unused")
	private final void controlAssingValue(RadioButton control, String value) {
	}

	@SuppressWarnings("unused")
	private final <T> void controlAssingValue(ChoiceBox<T> control, T value) {
	}

	@SuppressWarnings("unused")
	private final void cleanNode(TextField control) {
		control.clear();
	}

	@SuppressWarnings("unused")
	private final void cleanNode(CheckBox control) {
		control.setSelected(false);
	}

	@SuppressWarnings("unused")
	private final void cleanNode(DatePicker control) {
		control.valueProperty().set(null);
	}

	@SuppressWarnings("unused")
	private final void cleanNode(RadioButton control) {
	}

	@SuppressWarnings({ "unchecked", "rawtypes", "unused" })
	private final <T> void cleanNode(ChoiceBox control) {
		control.getSelectionModel().clearSelection();
		SelectList.selectItem(control, AppConstants.SELECCIONE);

	}

}
