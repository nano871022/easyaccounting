package co.com.japl.ea.app.components;

import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.apache.commons.lang3.StringUtils.isNotBlank;
import static org.pyt.common.common.DtoUtils.haveCode;

import java.util.List;

import org.pyt.common.constants.LanguageConstant;

import co.com.japl.ea.common.abstracts.ADto;
import co.com.japl.ea.dto.system.ConfigGenericFieldDTO;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.scene.control.TableView;
import javafx.scene.input.MouseEvent;

public class GenericBeans<T extends ADto> extends AGenericsBeans<T, GenericBeans<T>> {

	private List<ConfigGenericFieldDTO> listFields;

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public GenericBeans() {
		super((Class) GenericBeans.class);
	}

	public void initializeMethod() throws Exception {
		listFields = configGenericSvc.getFieldToFields(this.getClass(), clazz);
		loadNameTitle(clazz.getSimpleName() + ".bean.crud.title");
	}

	@Override
	public void loadMethod() throws Exception {
	}

	public void load(T dto) {
		this.registro = dto;
		visibleButtons();
		loadFields(TypeGeneric.FIELD);
	}

	public void load() throws Exception {
		this.registro = clazz.getConstructor().newInstance();
		loadFields(TypeGeneric.FIELD);
	}

	@Override
	public void selectedRow(MouseEvent eventHandler) {
	}

	@Override
	public TableView<T> getTableView() {
		return null;
	}

	@Override
	public Integer getMaxColumns(TypeGeneric typeGeneric) {
		return 2;
	}

	@Override
	public List<ConfigGenericFieldDTO> getListGenericsFields(TypeGeneric typeGeneric) {
		return listFields;
	}

	@Override
	void create() {
		try {
			if (validFields()) {
				T response = serviceSvc.insert(registro, getUsuario());
				if (haveCode(response)) {
					notificarI18n(registro.getClass().getSimpleName() + ".insert.success");
					visibleButtons();
				} else {
					alertaI18n(registro.getClass().getSimpleName() + "insert.failed");
				}
			}
		} catch (Exception e) {
			error(e);
		}
	}

	@Override
	void update() {
		try {
			if (validFields()) {
				serviceSvc.update(registro, getUsuario());
				notificarI18n(registro.getClass().getSimpleName() + ".update.success");
				visibleButtons();
			}
		} catch (Exception e) {
			error(e);
		}
	}

	@Override
	void delete() {
		try {
			controllerPopup(ConfirmPopupBean.class).load("#{GenericBeans.remove}",
					i18n().valueBundle(LanguageConstant.LANGUAGE_WARNING_DELETE_ROW));
		} catch (Exception e) {
			error(e);
		}
	}

	void setRemove(Boolean confirmar) {
		try {
			if (confirmar) {
				serviceSvc.delete(registro, getUsuario());
				notificarI18n(registro.getClass().getSimpleName() + ".delete.success");
				visibleButtons();
			}
		} catch (Exception e) {
			error(e);
		}
	}

	@Override
	void copy() {
		registro.setCodigo(null);
		notificarI18n(registro.getClass().getSimpleName() + ".copy.success");
		visibleButtons();
	}

	@Override
	void upload() {
		// TODO Auto-generated method stub

	}

	@Override
	void pay() {
		// TODO Auto-generated method stub

	}

	@Override
	void print() {
		// TODO Auto-generated method stub

	}

	@SuppressWarnings("unchecked")
	@Override
	void cancel() {
		try {
			if (openPopup) {
				this.cancelPopup();
			} else {
				getController(ListGenericBeans.class);
			}
		} catch (Exception e) {
			error(e);
		}
	}

	@Override
	public T getFilterToTable(T filter) {
		// TODO Auto-generated method stub
		return null;
	}

	void visibleButtonsMethod() {
		save.setValue(save.and(new SimpleBooleanProperty(isBlank(registro.getCodigo()))).getValue());
		edit.setValue(edit.and(new SimpleBooleanProperty(isNotBlank(registro.getCodigo()))).getValue());
		delete.setValue(delete.and(new SimpleBooleanProperty(isNotBlank(registro.getCodigo()))).getValue());
		cancel.setValue(true);
	}
}
