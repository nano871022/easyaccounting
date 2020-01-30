package co.com.japl.ea.beans.abstracts;

import java.util.List;

import org.apache.commons.collections4.MultiValuedMap;
import org.apache.commons.collections4.multimap.ArrayListValuedHashMap;
import org.pyt.common.abstracts.ADto;
import org.pyt.common.annotations.Inject;

import com.pyt.service.interfaces.IGenericServiceSvc;
import com.pyt.service.interfaces.IParametrosSvc;

import co.com.japl.ea.dto.system.ConfigGenericFieldDTO;
import co.com.japl.ea.interfaces.IGenericFields;
import javafx.scene.Node;

public abstract class AGenericInterfacesFieldBean<F extends ADto> extends ABean<F>
		implements IGenericFields<ConfigGenericFieldDTO, F> {

	@Inject(resource = "com.pyt.service.implement.GenericServiceSvc")
	private IGenericServiceSvc<ConfigGenericFieldDTO> configGenericSvc;
	@Inject(resource = "com.pyt.service.implement.ParametrosSvc")
	private IParametrosSvc parametrosSvc;
	protected List<ConfigGenericFieldDTO> fields;
	private Class<F> classTypeDto;
	private MultiValuedMap<String, Node> mapFieldUseds;

	@Override
	public MultiValuedMap<String, Node> getMapFields(TypeGeneric typeGeneric) {
		if (mapFieldUseds == null) {
			mapFieldUseds = new ArrayListValuedHashMap<>();
		}
		return mapFieldUseds;
	}

	@Override
	public Class<F> getClazz() {
		return classTypeDto;
	}

	public void setClazz(Class<F> clazz) {
		classTypeDto = clazz;
	}

	@Override
	public List<ConfigGenericFieldDTO> getListGenericsFields(TypeGeneric typeGeneric) {
		return fields;
	}

	public F getRegister() {
		return registro;
	}

	@Override
	public F getInstanceDto(TypeGeneric typeGeneric) {
		return registro;
	}

	public void setRegister(F filter) {
		registro = filter;
	}

	@Override
	public IParametrosSvc getParametersSvc() {
		return parametrosSvc;
	}

}
