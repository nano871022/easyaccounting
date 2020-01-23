package co.com.japl.ea.beans.abstracts;

import java.util.List;

import org.apache.commons.collections4.MultiValuedMap;
import org.apache.commons.collections4.multimap.ArrayListValuedHashMap;
import org.pyt.common.abstracts.ADto;
import org.pyt.common.annotations.Inject;

import com.pyt.service.interfaces.IGenericServiceSvc;
import com.pyt.service.interfaces.IParametrosSvc;

import co.com.japl.ea.dto.system.ConfigGenericFieldDTO;
import co.com.japl.ea.interfaces.IGenericColumns;
import co.com.japl.ea.interfaces.IGenericFields;
import co.com.japl.ea.utls.DataTableFXMLUtil;
import javafx.scene.Node;
import javafx.scene.control.TableView;
import javafx.scene.layout.HBox;

public abstract class AGenericInterfacesBean<T extends ADto> extends ABean<T>
		implements IGenericColumns<ConfigGenericFieldDTO, T>, IGenericFields<ConfigGenericFieldDTO, T> {

	protected DataTableFXMLUtil<T, T> dataTable;
	@Inject(resource = "com.pyt.service.implement.GenericServiceSvc")
	private IGenericServiceSvc<ConfigGenericFieldDTO> configGenericSvc;
	@Inject(resource = "com.pyt.service.implement.GenericServiceSvc")
	private IGenericServiceSvc<T> serviceSvc;
	protected T filtro;
	private MultiValuedMap<String, Node> mapFieldUseds;
	@Inject(resource = "com.pyt.service.implement.ParametrosSvc")
	private IParametrosSvc parameterSvc;

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

}