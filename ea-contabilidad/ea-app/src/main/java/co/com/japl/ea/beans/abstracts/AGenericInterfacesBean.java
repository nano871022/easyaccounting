package co.com.japl.ea.beans.abstracts;

import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.collections4.MultiValuedMap;
import org.apache.commons.collections4.multimap.ArrayListValuedHashMap;
import org.pyt.common.annotations.Inject;

import co.com.japl.ea.app.components.GenericBeans;
import co.com.japl.ea.app.components.PopupFromBean;
import co.com.japl.ea.common.abstracts.ADto;
import co.com.japl.ea.dto.interfaces.IGenericServiceSvc;
import co.com.japl.ea.dto.interfaces.IParametrosSvc;
import co.com.japl.ea.dto.system.ConfigGenericFieldDTO;
import co.com.japl.ea.dto.system.LanguagesDTO;
import co.com.japl.ea.exceptions.GenericServiceException;
import co.com.japl.ea.exceptions.LoadAppFxmlException;
import co.com.japl.ea.interfaces.IGenericColumns;
import co.com.japl.ea.interfaces.IGenericFields;
import co.com.japl.ea.utls.DataTableFXMLUtil;
import co.com.japl.ea.utls.LoadAppFxml;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.layout.HBox;

public abstract class AGenericInterfacesBean<T extends ADto> extends ABean<T>
		implements IGenericColumns<ConfigGenericFieldDTO, T>, IGenericFields<ConfigGenericFieldDTO, T> {

	protected DataTableFXMLUtil<T, T> dataTable;
	@Inject
	protected IGenericServiceSvc<ConfigGenericFieldDTO> configGenericSvc;
	@Inject
	protected IGenericServiceSvc<T> serviceSvc;
	protected T filtro;
	private MultiValuedMap<String, Node> mapFieldUseds;
	@Inject
	private IParametrosSvc parameterSvc;
	protected MultiValuedMap<String, Object> toChoiceBox;

	protected void loadDataModel(HBox paginator, TableView<T> tableView) {
		dataTable = new DataTableFXMLUtil<T, T>(paginator, tableView, false) {
			@Override
			public Integer getTotalRows(T filter) {
				try {
					return serviceSvc.getTotalRows(filter);
				} catch (Exception e) {
					error(e);
				}
				return 0;
			}

			@Override
			public List<T> getList(T filter, Integer page, Integer rows) {
				List<T> list = null;
				try {
					list = serviceSvc.gets(filter, page, rows);
				} catch (Exception e) {
					error(e);
				}
				return list;
			}

			@Override
			public T getFilter() {
				return getFilterToTable(filtro);
			}
		};
	}

	public <D extends ADto, B extends ABean<D>> List<ConfigGenericFieldDTO> findFields(TypeGeneric typeGeneric,
			Class<D> dto, Class<B> bean) {
		try {
			var cgf = new ConfigGenericFieldDTO();
			cgf.setClassPathBean(bean.getName().replace("Class ", ""));
			cgf.setClassPath(dto.getName().replace("Class ", ""));
			if (TypeGeneric.COLUMN == typeGeneric) {
				cgf.setIsColumn(true);
			} else if (TypeGeneric.FILTER == typeGeneric) {
				cgf.setIsFilter(true);
			}
			return configGenericSvc.getAll(cgf).stream()
					.sorted((row1, row2) -> row1.getOrden().compareTo(row2.getOrden())).collect(Collectors.toList());
		} catch (GenericServiceException e) {
			error(e);
		}
		return null;
	}

	public abstract T getFilterToTable(T filter);

	@Override
	public MultiValuedMap<String, Node> getMapFields(TypeGeneric typeGeneric) {
		if (mapFieldUseds == null) {
			mapFieldUseds = new ArrayListValuedHashMap<>();
		}
		return mapFieldUseds;
	}

	@Override
	public T getInstanceDto(TypeGeneric typeGeneric) {
		if (TypeGeneric.FIELD == typeGeneric) {
			return registro;
		}
		return filtro;
	}

	public IGenericServiceSvc<ConfigGenericFieldDTO> getServiceSvc() {
		return configGenericSvc;
	}

	@Override
	public DataTableFXMLUtil<T, T> getTable() {
		return dataTable;
	}

	@Override
	public IParametrosSvc getParametersSvc() {
		return parameterSvc;
	}

	@Override
	public MultiValuedMap<String, Object> getMapListToChoiceBox() {
		if (toChoiceBox == null) {
			toChoiceBox = new ArrayListValuedHashMap<>();
		}
		return toChoiceBox;
	}

	protected void loadNameTitle(String title, Label label) {
		genericFormsUtils.assingInLabel(label, i18n().valueBundle(title), event -> {
			if (event.getClickCount() == 2) {
				loadPopupLanguages(title);
			}
		});
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void loadPopupLanguages(String title) {
		var popup = new PopupFromBean(GenericBeans.class, LanguagesDTO.class);
		try {
			LoadAppFxml.loadBeanFX(popup);
			GenericBeans bean = (GenericBeans) popup.getBean();
			bean.openPopup();
			var dto = bean.addValueToField(title, "code");
			bean.addValueToField("es_ES", "idiom");
			bean.load(dto);
		} catch (LoadAppFxmlException | SecurityException e) {
			logger().logger(e);
		} catch (Exception e) {
			logger().logger(e);
		}
	}

}