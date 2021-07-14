package co.com.japl.ea.app.components;

import static co.com.japl.ea.utls.PermissionUtil.INSTANCE;
import static org.pyt.common.constants.InsertResourceConstants.CONST_RESOURCE_IMPL_SVC_CONFIG_GENERIC_FIELD;
import static org.pyt.common.constants.PermissionConstants.CONST_PERM_COPY;
import static org.pyt.common.constants.PermissionConstants.CONST_PERM_CREATE;
import static org.pyt.common.constants.PermissionConstants.CONST_PERM_DELETE;
import static org.pyt.common.constants.PermissionConstants.CONST_PERM_LOAD;
import static org.pyt.common.constants.PermissionConstants.CONST_PERM_PAY;
import static org.pyt.common.constants.PermissionConstants.CONST_PERM_PRINT;
import static org.pyt.common.constants.PermissionConstants.CONST_PERM_READ;
import static org.pyt.common.constants.PermissionConstants.CONST_PERM_UPDATE;

import org.controlsfx.glyphfont.FontAwesome.Glyph;
import org.pyt.common.annotations.Inject;

import co.com.japl.ea.app.beans.languages.LanguageBean;
import co.com.japl.ea.beans.abstracts.AGenericInterfacesBean;
import co.com.japl.ea.common.abstracts.ADto;
import co.com.japl.ea.common.button.apifluid.ButtonsImpl;
import co.com.japl.ea.dto.interfaces.IConfigGenericFieldSvc;
import co.com.japl.ea.exceptions.LoadAppFxmlException;
import co.com.japl.ea.utls.LoadAppFxml;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public abstract class AGenericsBeans<T extends ADto, B extends AGenericsBeans<T, B>> extends AGenericInterfacesBean<T>
		implements IGenericsBeans<T> {
	protected Label title;
	protected GridPane gridPane;
	protected VBox principal;
	protected Class<T> clazz;
	protected GridPane buttons;
	protected Class<B> beans;
	@Inject(resource = CONST_RESOURCE_IMPL_SVC_CONFIG_GENERIC_FIELD)
	protected IConfigGenericFieldSvc configGenericSvc;
	protected BooleanProperty copy;
	protected BooleanProperty pay;
	protected BooleanProperty print;
	protected BooleanProperty load;
	protected BooleanProperty cancel;

	public AGenericsBeans(Class<B> beans) {
		copy = new SimpleBooleanProperty(false);
		pay = new SimpleBooleanProperty(false);
		print = new SimpleBooleanProperty(false);
		load = new SimpleBooleanProperty(false);
		cancel = new SimpleBooleanProperty(false);
		this.beans = beans;
		title = new Label();
//		title.setScaleX(1.5);
//		title.setScaleY(1.5);
//		title.setMinWidth(500);
		principal = new VBox();
		gridPane = new GridPane();
		buttons = new GridPane();
		buttons.setAlignment(Pos.CENTER);
	}

	@Override
	public final Class<T> getClazz() {
		return clazz;
	}

	public final void initialize() throws Exception {
		this.registro = clazz.getConstructor().newInstance();
		gridPane.setHgap(10);
		gridPane.setVgap(10);
		initializeMethod();
		visibleButtons();
		ButtonsImpl.Stream(HBox.class).setLayout(buttons).setName("fxml.btn.create").action(this::create)
				.icon(Glyph.SAVE).isVisible(save).setName("fxml.btn.edit").action(this::update).icon(Glyph.EDIT)
				.isVisible(edit).setName("fxml.btn.delete").action(this::delete).icon(Glyph.REMOVE).isVisible(delete)
				.setName("fxml.btn.cancelar").action(this::cancel).isVisible(cancel).setName("fxml.btn.copy")
				.action(this::copy).icon(Glyph.COPY).isVisible(copy).setName("fxml.btn.load").action(this::upload)
				.icon(Glyph.UPLOAD).isVisible(load).setName("fxml.btn.pay").action(this::pay).icon(Glyph.MONEY)
				.isVisible(pay).setName("fxml.btn.print").action(this::print).icon(Glyph.PRINT).isVisible(print)
				.build();
	}

	protected void loadNameTitle(String title) {
		genericFormsUtils.assingInLabel(this.title, i18n().valueBundle(title), event -> loadPopupLanguages(title));
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void loadPopupLanguages(String title) {
		var popup = new PopupFromBean(LanguageBean.class);
		try {
			LoadAppFxml.loadBeanFX(popup);
			LanguageBean bean = (LanguageBean) popup.getBean();
			bean.openPopup();
			bean.addCode(title);
		} catch (LoadAppFxmlException e) {
			logger().logger(e);
		}
	}

	abstract void initializeMethod() throws Exception;

	abstract void create();

	abstract void update();

	abstract void delete();

	abstract void copy();

	abstract void upload();

	abstract void pay();

	abstract void print();

	abstract void cancel();

	public final Parent load(Class<T> classDto) throws Exception {
		clazz = classDto;
		principal.getChildren().add(title);
		principal.getChildren().add(gridPane);
		loadMethod();
		principal.getChildren().add(buttons);
		return principal;
	}

	abstract void loadMethod() throws Exception;

	@Override
	protected final void visibleButtons() {
		var create = INSTANCE().havePerm(CONST_PERM_CREATE, beans, getUsuario().getGrupoUser());
		var edit = INSTANCE().havePerm(CONST_PERM_UPDATE, beans, getUsuario().getGrupoUser());
		var delete = INSTANCE().havePerm(CONST_PERM_DELETE, beans, getUsuario().getGrupoUser());
		var view = !create && !edit && INSTANCE().havePerm(CONST_PERM_READ, beans, getUsuario().getGrupoUser());
		var copy = INSTANCE().havePerm(CONST_PERM_COPY, beans, getUsuario().getGrupoUser());
		var load = INSTANCE().havePerm(CONST_PERM_LOAD, beans, getUsuario().getGrupoUser());
		var pay = INSTANCE().havePerm(CONST_PERM_PAY, beans, getUsuario().getGrupoUser());
		var print = INSTANCE().havePerm(CONST_PERM_PRINT, beans, getUsuario().getGrupoUser());
		this.save.setValue(create);
		this.edit.setValue(edit);
		this.delete.setValue(delete);
		this.view.setValue(view);
		this.copy.setValue(copy);
		this.load.setValue(load);
		this.pay.setValue(pay);
		this.print.setValue(print);
		visibleButtonsMethod();
	}

	abstract void visibleButtonsMethod();

	@Override
	public final GridPane getGridPane(TypeGeneric typeGeneric) {
		return gridPane;
	}

}
