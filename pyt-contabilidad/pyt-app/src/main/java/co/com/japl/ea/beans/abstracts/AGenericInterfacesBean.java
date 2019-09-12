package co.com.japl.ea.beans.abstracts;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.pyt.common.abstracts.ADto;
import org.pyt.common.annotations.Inject;
import org.pyt.common.common.I18n;
import org.pyt.common.common.Log;

import com.pyt.service.interfaces.IGenericServiceSvc;
import com.pyt.service.pojo.GenericPOJO;

import co.com.japl.ea.dto.system.ConfigGenericFieldDTO;
import co.com.japl.ea.interfaces.IGenericColumn;
import co.com.japl.ea.interfaces.IGenericFilter;
import co.com.japl.ea.utls.DataTableFXMLUtil;
import javafx.scene.control.TableView;
import javafx.scene.layout.HBox;

public abstract class AGenericInterfacesBean<T extends ADto> extends ABean<T>
		implements IGenericColumn<T>, IGenericFilter<T> {

	protected DataTableFXMLUtil<T, T> dataTable;
	@Inject(resource = "com.pyt.service.implement.GenericServiceSvc")
	private IGenericServiceSvc<ConfigGenericFieldDTO> configGenericSvc;
	@Inject(resource = "com.pyt.service.implement.GenericServiceSvc")
	private IGenericServiceSvc<T> serviceSvc;
	private Map<String, GenericPOJO<T>> filtersMap;
	private Map<String, GenericPOJO<T>> columnsMap;
	protected T filtro;
	private Class<T> classTypeDto;
	private Map<String, Object> mapFieldUseds;

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
				try {
					return serviceSvc.gets(filter, page, rows);
				} catch (Exception e) {
					error(e);
				}
				return null;
			}

			@Override
			public T getFilter() {
				return getFilterToTable(filtro);
			}
		};
	}

	public abstract T getFilterToTable(T filter);

	@Override
	public Map<String, Object> getMapFieldUseds() {
		if (mapFieldUseds == null) {
			mapFieldUseds = new HashMap<String, Object>();
		}
		return mapFieldUseds;
	}

	@Override
	public Map<String, GenericPOJO<T>> getFilters() {
		return filtersMap;
	}

	@Override
	public void setFilters(Map<String, GenericPOJO<T>> filters) {
		filtersMap = filters;
	}

	@Override
	public Map<String, GenericPOJO<T>> getColumns() {
		return columnsMap;
	}

	@Override
	public void setColumns(Map<String, GenericPOJO<T>> columns) {
		columnsMap = columns;
	}

	@Override
	public Log getLogger() {
		return logger;
	}

	@Override
	public I18n getI18n() {
		return i18n();
	}

	@Override
	public void setClazz(Class<T> clazz) {
		classTypeDto = clazz;
	}

	@Override
	public T getFilter() {
		return filtro;
	}

	@Override
	public void setFilter(T filter) {
		filtro = filter;
	}

	@Override
	public IGenericServiceSvc<ConfigGenericFieldDTO> getServiceSvc() {
		return configGenericSvc;
	}

	@Override
	public Class<T> getClazz() {
		return classTypeDto;
	}

	@Override
	public DataTableFXMLUtil<T, T> getTable() {
		return dataTable;
	}

	@Override
	public void warning(String msn) {
		alerta(msn);
	}

	@Override
	public void error(Throwable error) {
		error(error);
	}
}
