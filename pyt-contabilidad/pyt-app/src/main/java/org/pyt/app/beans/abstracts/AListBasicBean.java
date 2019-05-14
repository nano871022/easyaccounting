package org.pyt.app.beans.abstracts;

import org.pyt.app.beans.interfaces.ListBasicBean;
import org.pyt.app.components.DataTableFXML;
import org.pyt.common.abstracts.ABean;
import org.pyt.common.abstracts.ADto;
/**
 * Se encarga de generalizar la pantalla de listar un objeto
 * @author Alejandro Parra
 * @since 22-06-2018
 */
public abstract class AListBasicBean<S extends Object,T extends ADto> extends ABean<T> implements ListBasicBean {
	protected DataTableFXML<S, T> dataTable;
	@Override
	public final Boolean isSelected() {
		return dataTable.isSelected();
	}

}
