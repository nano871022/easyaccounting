package co.com.japl.ea.beans.abstracts;

import java.util.List;

import org.apache.commons.collections4.MultiValuedMap;
import org.apache.commons.collections4.multimap.ArrayListValuedHashMap;
import org.pyt.common.abstracts.ADto;
import org.pyt.common.annotations.Inject;
import org.pyt.common.common.DtoUtils;
import org.pyt.common.constants.PermissionConstants;
import org.pyt.common.exceptions.GenericServiceException;

import com.pyt.service.interfaces.IGenericServiceSvc;
import com.pyt.service.interfaces.IParametrosSvc;

import co.com.japl.ea.dto.system.ConfigGenericFieldDTO;
import co.com.japl.ea.interfaces.IGenericFields;
import co.com.japl.ea.utls.PermissionUtil;
import javafx.beans.property.BooleanProperty;
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
	protected BooleanProperty save;
	protected BooleanProperty edit;
	protected BooleanProperty delete;

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

	protected <B extends ABean<F>> void visibleFields(Class<B> bean) {
		var save = DtoUtils.haveCode(registro) && PermissionUtil.INSTANCE()
				.havePerm(PermissionConstants.CONST_PERM_CREATE, bean, getUsuario().getGrupoUser());
		var edit = !DtoUtils.haveCode(registro) && PermissionUtil.INSTANCE()
				.havePerm(PermissionConstants.CONST_PERM_UPDATE, bean, getUsuario().getGrupoUser());
		var delete = !DtoUtils.haveCode(registro) && PermissionUtil.INSTANCE()
				.havePerm(PermissionConstants.CONST_PERM_DELETE, bean, getUsuario().getGrupoUser());
		this.save.setValue(save);
		this.edit.setValue(edit);
		this.delete.setValue(delete);
	}

}
