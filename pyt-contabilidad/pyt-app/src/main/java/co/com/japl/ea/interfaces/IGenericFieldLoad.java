package co.com.japl.ea.interfaces;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.pyt.common.abstracts.ADto;
import org.pyt.common.annotations.NoEdit;
import org.pyt.common.common.SelectList;
import org.pyt.common.common.UtilControlFieldFX;
import org.pyt.common.exceptions.QueryException;
import org.pyt.common.exceptions.ReflectionException;

import com.pyt.service.dto.DocumentosDTO;
import com.pyt.service.dto.ParametroDTO;

import javafx.scene.Node;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;

public interface IGenericFieldLoad extends IGenericMethodsCommon {
	final UtilControlFieldFX genericFormsUtils = new UtilControlFieldFX();

	public Integer maxColumns();

	public GridPane GridPane();

	/**
	 * Se encarga de crear la grilla con todos los campos que se encontraton y
	 * ponerlos en el fxml
	 * 
	 * @throws org.pyt.common.exceptions.ReflectionException
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	default <N, M extends ADto> GridPane loadGrid() {
		Integer maxColumn = maxColumns();
		var campos = getFields();
		if (campos == null) {
			warning(getI18n().valueBundle("field_doesnt_found_to_process"));
			return new GridPane();
		}
		Integer countFields = campos.size();
		var formulario = genericFormsUtils.configGridPane(GridPane());
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
						classe = getInstanceDTOUse().getType(docs.getFieldName());
					}
					putList(docs.getFieldName(), select, classe, docs.getPutNameShow(), docs.getSelectNameGroup(),
							getInstanceDTOUse().get(docs.getFieldName()), docs.getPutNameAssign());
				} catch (org.pyt.common.exceptions.ReflectionException e) {
					error(e);
				}
				formulario.add(select, columnIndex + 1, rowIndex);
				getConfigFields().put(docs.getFieldName(), select);
			} else {
				configNodeField(docs, formulario, columnIndex, rowIndex);
			}
			if (countFields < 6 && columnIndex == maxColumn) {
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
			Node field = genericFormsUtils.getFieldNodeFormFromTypeAndValue(
					getInstanceDTOUse().getType(docs.getFieldName()), getInstanceDTOUse().get(docs.getFieldName()));
			if (field instanceof TextField)
				((TextField) field).focusedProperty().addListener(e -> methodChanges());
			if (field != null) {
				field.setDisable(!docs.getEdit());
				field.setDisable(noEdit(docs.getFieldName()));
				formulario.add(field, columnIndex + 1, rowIndex);
				getConfigFields().put(docs.getFieldName(), field);
			}
		} catch (ReflectionException e) {
			error(e);
		}
	}

	/**
	 * Detecta si el campo esta anotado con noEdit para que el campo se desabilite
	 * 
	 * @param nombreCampo {@link String}
	 * @return {@link Boolean}
	 */
	private Boolean noEdit(String nombreCampo) {
		Field field;
		try {
			field = getInstanceDTOUse().getClass().getDeclaredField(nombreCampo);
			NoEdit noEdit = field.getDeclaredAnnotation(NoEdit.class);
			return noEdit != null;
		} catch (NoSuchFieldException | SecurityException e) {
			getLogger().logger(e);
		}
		return false;
	}

	/**
	 * Se encarga de crear un choice box de tipo string apartir de la inforamcion
	 * del DocumentDTo suministrado
	 * 
	 * @param docs {@link DocumentDTO}
	 * @return {@link ChoiceBox} < {@link String} >
	 */
	private ChoiceBox<String> configChoiceBox(DocumentosDTO docs) {
		ChoiceBox<String> select = new ChoiceBox<String>();
		select.setDisable(!docs.getEdit());
		select.setDisable(noEdit(docs.getFieldName()));
		select.onActionProperty().set(e -> selectionAction(docs, select));
		select.setMaxWidth(1.7976931348623157E308);
		return select;
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
			List list = (List) getConfigFieldTypeList().get(docs.getFieldName());
			if (list != null && list.size() > 0) {
				M obj = (M) SelectList.get(select, list, docs.getPutNameShow());
				if (StringUtils.isNotBlank(docs.getPutNameAssign()) && docs != null && obj != null) {
					N value = obj.get(docs.getPutNameAssign());
					if (value != null) {
						getInstanceDTOUse().set(docs.getFieldName(), value);
					}
				} else {
					if (obj != null) {
						getInstanceDTOUse().set(docs.getFieldName(), obj);
					}
				}
			} else {
				if (docs != null) {
					warning(getI18n().valueBundle("List_doesnt_found_to_" + docs.getFieldName()));
				} else {
					warning(getI18n().valueBundle("List_doesnt_found"));
				}
			}
		} catch (org.pyt.common.exceptions.ReflectionException e1) {
			error(e1);
		}
	}

	default void methodChanges() {
	}

	/**
	 * Se encarga de cargar la lista sobre los parametros
	 * 
	 * @param choiceBox {@link ChoiceBox}
	 * @param busqueda  {@link Class}
	 * @param show      {@link String} nombre de campo a mostrar del objeto busqueda
	 * @param grupo     {@link String} codigo del grupos a buscar
	 */
	@SuppressWarnings({ "unchecked" })
	private <L, T extends Object, S extends ADto> void putList(String nameField, ChoiceBox<String> choiceBox,
			Class<T> busqueda, String show, String grupo, L value, String assign) {
		S dto = null;
		if (busqueda == ParametroDTO.class) {
			dto = (S) new ParametroDTO();
			((ParametroDTO) dto).setGrupo(grupo);
		} else {
			try {
				dto = (S) busqueda.getConstructor().newInstance();
			} catch (InstantiationException | IllegalAccessException | IllegalArgumentException
					| InvocationTargetException | NoSuchMethodException | SecurityException e) {
				error(e);
			}
		}
		if (dto != null) {
			try {
				List<S> lista = getServiceSvc().gets(dto);
				SelectList.put(choiceBox, lista, show);
				getConfigFieldTypeList().put(nameField, lista);
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

}
