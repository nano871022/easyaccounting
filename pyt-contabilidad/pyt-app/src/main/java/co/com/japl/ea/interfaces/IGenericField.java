package co.com.japl.ea.interfaces;

import java.util.HashMap;
import java.util.Map;

import org.pyt.common.abstracts.ADto;
import org.pyt.common.common.UtilControlFieldFX;
import org.pyt.common.constants.LanguageConstant;
import org.pyt.common.constants.StylesPrincipalConstant;

import com.pyt.service.pojo.GenericPOJO;

import javafx.scene.layout.GridPane;

public interface IGenericField<T extends ADto> extends IGenericFieldsCommon<T> {
	public Map<String, Object> defaultValuesGenericParametrized = new HashMap<String, Object>();

	public Map<String, GenericPOJO<T>> getFields();

	void setFields(Map<String, GenericPOJO<T>> filters);

	public T getRegister();

	public void setRegister(T filter);

	public GridPane getGridPaneField();

	default T getDto() {
		return getRegister();
	}

	default void configFields() throws Exception {
		var fields = getConfigFields(getInstaceOfGenericADto(), false, false);
		if (fields == null)
			throw new Exception(getI18n().valueBundle(LanguageConstant.GENERIC_FIELD_NOT_FOUND_FIELD_TO_USE));
		setFields(fields);
		final var indices = new Index();
		var util = new UtilControlFieldFX();

		getFields().entrySet().stream()
				.sorted((compare1, compare2) -> sortByOrderField(compare1.getValue().getOrder(),
						compare2.getValue().getOrder()))
				.forEachOrdered(value -> configFields(value.getValue(), util, getGridPaneField(), indices));
		getGridPaneField().getStyleClass().add(StylesPrincipalConstant.CONST_GRID_STANDARD);
	}

	default void cleanFields() {
		try {
			var util = new UtilControlFieldFX();
			getGridPaneField().getChildren().forEach(child -> util.cleanValueByFieldFX(child));
		} catch (Exception e) {
			getLogger().logger(e);
		}

	}
}
