package co.com.japl.ea.interfaces;

import java.lang.reflect.InvocationTargetException;
import java.util.Optional;

import org.pyt.common.abstracts.ADto;
import org.pyt.common.constants.LanguageConstant;
import org.pyt.common.constants.StylesPrincipalConstant;
import org.pyt.common.exceptions.ReflectionException;

import com.pyt.service.dto.DocumentosDTO;

import co.com.japl.ea.interfaces.IGenericFieldsCommon.Index;
import javafx.scene.Node;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.GridPane;
/**
 * Esta interface se encarga de configurar los metodos de carga generica
 * @author Alejo Parra
 *
 * @param <C>
 * @param <L>
 * @param <F>
 */
public interface IGenericFields <C extends ADto,L extends ADto,F extends ADto>extends IGenericFieldCommon<C, L, F> {
	/**
	 * Contiene el procesamiento de los campos que se pondran sobre el {@link GridPane} configurado en la interface superiro
	 * @return {@link GridPane}
	 * @throws Exception 
	 */
	default GridPane loadFields() throws Exception {
		var gridPane = getGridPane();
		var listGenericFields = getListGenericsFields();
		if(!Optional.ofNullable(listGenericFields).isPresent()) {
			throw new Exception(i18n().valueBundle(LanguageConstant.GENERIC_FIELD_NOT_FOUND_FIELD_TO_USE));
		}
		var countFields = listGenericFields.size();
		var formulario = genericFormsUtils.configGridPane(gridPane);
		var dto = getInstanceDto();
		var indice = new Index();
		gridPane.getStyleClass().add(StylesPrincipalConstant.CONST_GRID_STANDARD);
		listGenericFields.forEach(genericField->{
			try{
				var nameField = (String)genericField.get("name");
				var label = genericFormsUtils.createLabel(genericField.get("alias"));
				var description = genericField.get("description");
				if(Optional.ofNullable(description).isPresent()) {
					label.setTooltip(new Tooltip(genericField.get("description")));
				}
				var clazType = dto.getType(nameField);
				var value = dto.get(nameField);
				var nodo = genericFormsUtils.getFieldNodeFormFromTypeAndValue(clazType, value);
				gridPane.add(nodo, indice.columnIndex, indice.rowIndex);
				var width = (Double)genericField.get("width");
				if(width > 0) {
					nodo.prefWidth(width);
				}
				genericFormsUtils.inputListenerToAssingValue(nodo, (obj)->assingsValueToField(nameField, obj));
				getMapFiles().put(nameField, nodo);
				calcPosFieldInGrid(indice);
			}catch(Exception e) {
				error(e);
			}
		});
		return gridPane;
	}
	/**
	 * Se encarga de calcular la sigueinte posicion de los campos
	 * @param indices {@link Index}
	 */
	private void calcPosFieldInGrid(Index indices) {
		indices.columnIndex = indices.columnIndex == getMaxColumns() ? 0 : indices.columnIndex + 2;
		indices.rowIndex = indices.columnIndex == 0 ? indices.rowIndex + 1 : indices.rowIndex;
	}
	
	public class Index {
		Integer columnIndex = 0;
		Integer rowIndex = 0;
	}
	
	default <O extends Object> void assingsValueToField(String nameField, O value) {
		try {
			if (value == null) {
				alerta(i18n().valueBundle(String.format(LanguageConstant.LANGUAGE_FIELD_ASSIGN_EMPTY,
						nameField, getInstanceDto().getClass().getSimpleName())));
			} else {
				getInstanceDto().set(nameField, value);
			}
		} catch (ReflectionException  e) {
			logger().logger(e);
		}
	}
}
