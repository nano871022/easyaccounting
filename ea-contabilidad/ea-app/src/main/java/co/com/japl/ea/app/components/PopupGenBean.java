package co.com.japl.ea.app.components;

import static org.apache.commons.lang3.StringUtils.isNotBlank;

import java.util.function.Consumer;

import org.pyt.common.common.ListUtils;
import org.pyt.common.constants.LanguageConstant;
import org.pyt.common.constants.StylesPrincipalConstant;

import co.com.arquitectura.annotation.proccessor.FXMLFile;
import co.com.japl.ea.app.enums.ResponsiveSizeEnum;
import co.com.japl.ea.beans.abstracts.AGenericInterfacesReflectionBean;
import co.com.japl.ea.common.abstracts.ADto;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.input.MouseEvent;

/**
 * Esta clase se encarga de configurar un popup de consultas con el dto
 * suministrado con el cual se realizan todas las consultas para obtener las
 * listas y filtrar datos y retornar un valor al componente quien lo solicito
 * 
 * @author Alejandro Parra
 *
 * @param <T> extends {@link ADto}
 */
@FXMLFile(path = "/", file = ".", nombreVentana = "PopupGenericBean")
public class PopupGenBean<T extends ADto> extends AGenericInterfacesReflectionBean<T> {

	public PopupGenBean(Class<T> clazz) throws Exception {
		super(clazz);
		tittleWindowI18n = LanguageConstant.GENERIC_WINDOW_POPUP_TITTLE + clazz.getSimpleName();
		sizeWindow(ResponsiveSizeEnum.SMALL.getMinWidth(), 500);
	}

	/**
	 * Realiza la carga del controlador con el fxml y la ratorna , para no usar el
	 * controllerPopup para este Bean generico
	 * 
	 * @param <T>
	 * @param clazz
	 * @return
	 * @throws Exception
	 */
	public static final <T extends ADto> PopupGenBean<T> instance(Class<T> clazz) throws Exception {
		var popup = new PopupGenBean<T>(clazz);
		return popup.controllerPopup(popup);
	}

	@SuppressWarnings("static-access")
	@FXML
	@Override
	public void initialize() {
		loadTable();
		try {
			panel.setTop(gridFilter);
			panel.setCenter(tabla);
			panel.setAlignment(tabla, Pos.CENTER);
			panel.setBottom(paginador);
			registro = clazz.getConstructor().newInstance();
			listColumns = configGenericFieldsSvc.getFieldToColumns(this.getClass(), clazz);
			listFilters = configGenericFieldsSvc.getFieldToFilters(this.getClass(), clazz);
			loadColumns(StylesPrincipalConstant.CONST_TABLE_CUSTOM);
			loadFields(TypeGeneric.FILTER, StylesPrincipalConstant.CONST_GRID_STANDARD);
		} catch (Exception e) {
			error(e);
		}
	}

	/**
	 * Se le indica en el momento de la carga a quien le debe retornar al
	 * seleccionar un registro
	 * 
	 * @param caller {@link String}
	 */
	@SuppressWarnings("unchecked")
	public final void load(String caller) throws Exception {
		if (ListUtils.isBlank(listFilters, listColumns)) {
			this.closeWindow();
			throw new Exception(i18n().valueBundle("err.popupgen.dto.isnt.configured",
					i18n().get(LanguageConstant.GENERIC_DOT.concat(getClazz().getSimpleName()))).get());
		}
		this.caller = caller;
		var cantidad = table.getTotalRows(applyFilterToDataTableSearch());
		if (cantidad == 1) {
			this.caller(caller, table.getList(applyFilterToDataTableSearch(), 0, 1).get(0));
			this.closeWindow();
		} else if (cantidad == 0) {
			this.closeWindow();
			throw new Exception(i18n().valueBundle(LanguageConstant.GENERIC_NO_ROWS_inputText,
					i18n().get(LanguageConstant.GENERIC_DOT.concat(getClazz().getSimpleName()))).get());
		} else {
			showWindow();
		}
	}

	@SuppressWarnings("unchecked")
	public final void load(Consumer<T> caller) throws Exception {
		if (ListUtils.isBlank(listFilters, listColumns)) {
			this.closeWindow();
			throw new Exception(i18n().valueBundle("err.popupgen.dto.isnt.configured",
					i18n().get(LanguageConstant.GENERIC_DOT.concat(getClazz().getSimpleName()))).get());
		}
		this.sCaller = caller;
		var cantidad = table.getTotalRows(applyFilterToDataTableSearch());
		if (cantidad == 1) {
			this.caller(caller, table.getList(applyFilterToDataTableSearch(), 0, 1).get(0));
			this.closeWindow();
		} else if (cantidad == 0) {
			this.closeWindow();
			throw new Exception(i18n().valueBundle(LanguageConstant.GENERIC_NO_ROWS_inputText,
					i18n().get(LanguageConstant.GENERIC_DOT.concat(getClazz().getSimpleName()))).get());
		} else {
			showWindow();
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public void selectedRow(MouseEvent event) {
		try {
			if (event.getClickCount() > 0 && table.isSelected()) {
				if (isNotBlank(caller)) {
					this.caller(caller, table.getSelectedRow());
				} else {
					this.caller(sCaller, table.getSelectedRow());
				}
				this.closeWindow();
			}
		} catch (Exception e) {
			error(e);
		}
	}

	@SuppressWarnings("unchecked")
	public PopupGenBean<T> setWidth(double width) {
		super.setWidth(width);
		return this;
	}

	@SuppressWarnings("unchecked")
	public PopupGenBean<T> addDefaultValuesToGenericParametrized(String nameGroup, String valueKeyPrameter) {
		super.addDefaultValuesToGenericParametrized(nameGroup, valueKeyPrameter);
		return this;
	}

	@SuppressWarnings("unchecked")
	public PopupGenBean<T> addDefaultValuesToGenericParametrized(String nameEstado, Integer estado) {
		super.addDefaultValuesToGenericParametrized(nameEstado, estado);
		return this;
	}

	@Override
	public Integer getMaxColumns(TypeGeneric typeGeneric) {
		return 2;
	}

	@Override
	public T applyFilterToDataTableSearch() {
		return registro;
	}

}