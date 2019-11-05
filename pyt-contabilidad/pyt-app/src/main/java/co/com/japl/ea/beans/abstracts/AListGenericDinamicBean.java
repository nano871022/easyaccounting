package co.com.japl.ea.beans.abstracts;

import java.util.List;

import org.apache.commons.collections4.MultiValuedMap;
import org.apache.commons.collections4.multimap.ArrayListValuedHashMap;
import org.pyt.common.abstracts.ADto;
import org.pyt.common.annotations.Inject;
import org.pyt.common.common.I18n;

import com.pyt.service.interfaces.IGenericServiceSvc;
import com.pyt.service.interfaces.IParametrosSvc;

import co.com.japl.ea.interfaces.IGenericColumns;
import co.com.japl.ea.interfaces.IGenericFields;
import co.com.japl.ea.interfaces.IUrlLoadBean;
import javafx.scene.Node;

public abstract class AListGenericDinamicBean<T extends ADto, S extends ADto, F extends ADto> extends ABean<T>
		implements IUrlLoadBean, IGenericFields<S, F>, IGenericColumns<S, F> {
	protected List<S> genericFields;
	protected List<S> genericColumns;
	private MultiValuedMap<String, Node> configFields;
	@Inject(resource = "com.pyt.query.implement.GenericServiceSvc")
	private IGenericServiceSvc<S> querySvc;
	@Inject(resource = "com.pyt.query.implement.ParametrosSvc")
	private IParametrosSvc parametrosSvc;

	public AListGenericDinamicBean() {
		configFields = new ArrayListValuedHashMap<>();
	}

	public IGenericServiceSvc<S> getServiceSvc() {
		return querySvc;
	}

	@Override
	public I18n i18n() {
		return i18n();
	}

	@Override
	public MultiValuedMap<String, Node> getMapFields(TypeGeneric typeGeneric) {
		return configFields;
	}

	@Override
	public IParametrosSvc getParametersSvc() {
		return parametrosSvc;
	}

	@Override
	public List<S> getListGenericsFields(TypeGeneric typeGeneric) {
		switch (typeGeneric) {
		case FILTER:
			return genericFields;
		case COLUMN:
			return genericColumns;
		}
		return null;
	}

}
