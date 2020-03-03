package co.com.japl.ea.beans.abstracts;

import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.collections4.MultiValuedMap;
import org.apache.commons.collections4.multimap.ArrayListValuedHashMap;
import org.pyt.common.abstracts.ADto;
import org.pyt.common.annotations.Inject;
import org.pyt.common.exceptions.GenericServiceException;

import com.pyt.service.interfaces.IGenericServiceSvc;
import com.pyt.service.interfaces.IParametrosSvc;

import co.com.japl.ea.dto.system.ConfigGenericFieldDTO;
import co.com.japl.ea.interfaces.IGenericColumns;
import co.com.japl.ea.interfaces.IGenericFields;
import co.com.japl.ea.utls.DataTableFXMLUtil;
import javafx.beans.property.BooleanProperty;
import javafx.scene.Node;
import javafx.scene.control.TableView;
import javafx.scene.layout.HBox;

public abstract class AGenericInterfacesBean<T extends ADto> extends ABean<T>
		implements IGenericColumns<ConfigGenericFieldDTO, T>, IGenericFields<ConfigGenericFieldDTO, T> {

	protected DataTableFXMLUtil<T, T> dataTable;
	@Inject(resource = "com.pyt.service.implement.GenericServiceSvc")
	protected IGenericServiceSvc<ConfigGenericFieldDTO> configGenericSvc;
	@Inject(resource = "com.pyt.service.implement.GenericServiceSvc")
	private IGenericServiceSvc<T> serviceSvc;
	protected T filtro;
	private MultiValuedMap<String, Node> mapFieldUseds;
	@Inject(resource = "com.pyt.service.implement.ParametrosSvc")
	private IParametrosSvc parameterSvc;
	protected MultiValuedMap<String, Object> toChoiceBox;
	protected BooleanProperty save;
	protected BooleanProperty delete;
	protected BooleanProperty edit;
	protected BooleanProperty view;

	protected void loadDataModel(HBox paginator, TableView<T> tableView) {
		dataTable = new DataTableFXMLUtil<T, T>(paginator, tableView) {
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
}