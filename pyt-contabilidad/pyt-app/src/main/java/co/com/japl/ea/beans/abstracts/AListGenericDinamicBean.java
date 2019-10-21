package co.com.japl.ea.beans.abstracts;

import java.util.List;

import org.apache.commons.collections4.MultiValuedMap;
import org.apache.commons.collections4.multimap.ArrayListValuedHashMap;
import org.pyt.common.abstracts.ADto;
import org.pyt.common.annotations.Inject;
import org.pyt.common.common.I18n;

import com.pyt.service.dto.DocumentosDTO;
import com.pyt.service.dto.ParametroDTO;
import com.pyt.service.interfaces.IGenericServiceSvc;

import co.com.japl.ea.interfaces.IGenericColumns;
import co.com.japl.ea.interfaces.IGenericFields;
import co.com.japl.ea.interfaces.IUrlLoadBean;
import javafx.scene.Node;

public abstract class AListGenericDinamicBean<T extends ADto, S extends ADto, F extends ADto> extends ABean<T>
		implements IUrlLoadBean, IGenericFields<T, F>, IGenericColumns<T, F> {
	protected List<DocumentosDTO> genericFields;
	protected List<DocumentosDTO> genericColumns;
	private MultiValuedMap<String, Node> configFields;
	@Inject(resource = "com.pyt.query.implement.GenericServiceSvc")
	private IGenericServiceSvc<S> querySvc;
	@Inject(resource = "com.pyt.query.implement.GenericServiceSvc")
	private IGenericServiceSvc<ParametroDTO> parametrosSvc;

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
	public MultiValuedMap<String, Node> getMapFields() {
		return configFields;
	}

	@Override
	public IGenericServiceSvc<ParametroDTO> getParametersSvc() {
		return parametrosSvc;
	}

	@SuppressWarnings("unchecked")
	public List<T> getListGenericsFields() {
		return (List<T>) genericFields;
	}

}
