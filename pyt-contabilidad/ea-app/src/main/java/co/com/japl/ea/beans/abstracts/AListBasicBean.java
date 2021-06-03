package co.com.japl.ea.beans.abstracts;

import co.com.japl.ea.app.beans.interfaces.ListBasicBean;
import co.com.japl.ea.common.abstracts.ADto;
import co.com.japl.ea.utls.DataTableFXMLUtil;
/**
 * Se encarga de generalizar la pantalla de listar un objeto
 * @author Alejandro Parra
 * @since 22-06-2018
 */
public abstract class AListBasicBean<S extends Object,T extends ADto> extends ABean<T> implements ListBasicBean {
	protected DataTableFXMLUtil<S, T> dataTable;
	@Override
	public final Boolean isSelected() {
		return dataTable.isSelected();
	}

}
