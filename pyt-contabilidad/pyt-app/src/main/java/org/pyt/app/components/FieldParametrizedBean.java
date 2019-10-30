package org.pyt.app.components;

import java.util.List;
import java.util.Map;

import org.apache.commons.collections4.MultiValuedMap;
import org.apache.commons.lang3.StringUtils;
import org.pyt.common.abstracts.ADto;
import org.pyt.common.annotations.Inject;
import org.pyt.common.exceptions.ReflectionException;

import com.pyt.service.interfaces.IGenericServiceSvc;
import com.pyt.service.pojo.GenericPOJO;

import co.com.arquitectura.annotation.proccessor.FXMLFile;
import co.com.japl.ea.beans.abstracts.AGenericInterfacesReflectionBean;
import co.com.japl.ea.dto.system.ConfigGenericFieldDTO;
import co.com.japl.ea.utls.DataTableFXMLUtil;
import javafx.fxml.FXML;
import javafx.scene.control.TableView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;

@FXMLFile(path = "/", file = ".", nombreVentana = "FieldParametrizedBean")
public class FieldParametrizedBean<T extends ADto> extends AGenericInterfacesReflectionBean<T> {
	@Inject(resource = "com.pyt.service.impls.GenericServiceSvc")
	private IGenericServiceSvc<ConfigGenericFieldDTO> configGenericFieldSvc;
	private PopupGenBean popupGenericBean;
	private T filter;
	private Map<String, GenericPOJO<T>> filtros;
	private Boolean isPopup;
	private Integer maxRowsChoiceBox;
	private List<T> list;

	public FieldParametrizedBean(Class<T> clazz) throws Exception {
		super(clazz);
	}

	@Override
	@FXML
	public void initialize() {
		filter = validFilter();
		isPopup = false;

	}

	public T validFilter() {
		filtros.forEach((key, value) -> {
			try {
				if (((Class<?>) filter.getType(value.getField().getName())) == String.class
						&& StringUtils.isNotBlank(filter.get(value.getField().getName()))) {
					filter.set(value.getField().getName(), "%" + filter.get(value.getField().getName()) + "%");
				}
			} catch (ReflectionException e) {
				logger().logger(e);
			}
		});
		return filter;
	}

	/**
	 * Se le indica en el momento de la carga a quien le debe retornar al
	 * seleccionar un registro
	 * 
	 * @param caller {@link String}
	 */
	public final void load(String caller) throws Exception {
		this.caller = caller;
		filter = assingValuesParameterized(filter);
		var cantidad = querys.records(validFilter(), userLogin);
		if (cantidad == 1 || (cantidad > 1 && cantidad <= maxRowsChoiceBox)) {
			isPopup = true;
		} else {
			isPopup = false;
			list = querys.list(filter, userLogin);
		}

	}

	public final List<T> getList() {
		return list;
	}

	public final Boolean isPopup() {
		return isPopup;
	}

	@Override
	public GridPane getGridPane(TypeGeneric typeGeneric) {
		return null;
	}

	@Override
	public Integer getMaxColumns(TypeGeneric typeGeneric) {
		return 2;
	}

	@Override
	public MultiValuedMap<String, Object> getMapListToChoiceBox() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<ConfigGenericFieldDTO> getListGenericsFields(TypeGeneric typeGeneric) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void selectedRow(MouseEvent eventHandler) {
		// TODO Auto-generated method stub

	}

	@Override
	public TableView<T> getTableView() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public DataTableFXMLUtil<T, T> getTable() {
		// TODO Auto-generated method stub
		return null;
	}

}
