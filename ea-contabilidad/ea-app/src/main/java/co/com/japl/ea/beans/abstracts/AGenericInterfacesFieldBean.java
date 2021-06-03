package co.com.japl.ea.beans.abstracts;

import java.util.List;

import org.apache.commons.collections4.MultiValuedMap;
import org.apache.commons.collections4.multimap.ArrayListValuedHashMap;
import org.pyt.common.annotations.Inject;

import co.com.japl.ea.common.abstracts.ADto;
import co.com.japl.ea.dto.interfaces.IGenericServiceSvc;
import co.com.japl.ea.dto.interfaces.IParametrosSvc;
import co.com.japl.ea.dto.system.ConfigGenericFieldDTO;
import co.com.japl.ea.exceptions.GenericServiceException;
import co.com.japl.ea.interfaces.IGenericFields;
import javafx.scene.Node;

public abstract class AGenericInterfacesFieldBean<F extends ADto> extends ABean<F>
		implements IGenericFields<ConfigGenericFieldDTO, F> {

	@Inject(resource = "com.pyt.service.implement.GenericServiceSvc")
	protected IGenericServiceSvc<ConfigGenericFieldDTO> configGenericSvc;
	@Inject(resource = "com.pyt.service.implement.ParametrosSvc")
	private IParametrosSvc parametrosSvc;
	protected List<ConfigGenericFieldDTO> fields;
	protected Class<F> classTypeDto;
	protected MultiValuedMap<String, Node> mapFieldUseds;
	protected MultiValuedMap<String, Object> toChoiceBox;

	@Override
	public MultiValuedMap<String, Node> getMapFields(TypeGeneric typeGeneric) {
		if (mapFieldUseds == null) {
			mapFieldUseds = new ArrayListValuedHashMap<>();
		}
		return mapFieldUseds;
	}

	/**
	 * se encarga de cargar los campos de tipo campos para ser mostrado en la
	 * pantalla se debe llamar despues de configurar
	 * {@link AGenericInterfacesFieldBean#classTypeDto}
	 */
	protected void loadFields() {
		try {
			var dto = new ConfigGenericFieldDTO();
			dto.setClassPath(classTypeDto.getName().replace("Class ", ""));
			dto.setClassPathBean(this.getClass().getName().replace("Class ", ""));
			fields = configGenericSvc.getAll(dto);
		} catch (GenericServiceException e) {
			error(e);
		}
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

	@Override
	public MultiValuedMap<String, Object> getMapListToChoiceBox() {
		if (toChoiceBox == null) {
			toChoiceBox = new ArrayListValuedHashMap<>();
		}
		return toChoiceBox;
	}

	protected abstract void visibleButtons();
}
