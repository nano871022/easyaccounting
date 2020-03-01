package co.com.japl.ea.common.button.apifluid;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

import org.apache.commons.collections4.MultiValuedMap;
import org.apache.commons.collections4.multimap.ArrayListValuedHashMap;
import org.controlsfx.glyphfont.FontAwesome;
import org.controlsfx.glyphfont.Glyph;
import org.pyt.common.common.I18n;

import javafx.beans.property.BooleanProperty;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;

public class ButtonsImpl<P extends Pane> implements Buttons<P> {

	private P layout;
	private List<String> names;
	private Map<String, Button> buttons;
	private MultiValuedMap<String, Supplier<Boolean>> visibilities;
	private Map<String, BooleanProperty> visibilitiesProperties;
	private Map<String, FontAwesome.Glyph> icons;
	private MultiValuedMap<String, Caller> actions;
	private MultiValuedMap<String, String> styles;
	private MultiValuedMap<String, String> commands;

	private ButtonsImpl() {
		buttons = new HashMap<>();
		visibilities = new ArrayListValuedHashMap<String, Supplier<Boolean>>();
		actions = new ArrayListValuedHashMap<String, Caller>();
		styles = new ArrayListValuedHashMap<String, String>();
		names = new ArrayList<>();
		icons = new HashMap<>();
		commands = new ArrayListValuedHashMap<String, String>();
		visibilitiesProperties = new HashMap<String, BooleanProperty>();
	}

	public static <P extends Pane> Buttons<?> Stream(Class<P> clazz) {
		return new ButtonsImpl<P>();
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T extends Pane> Buttons<P> setLayout(T layout) {
		this.layout = (P) layout;
		return this;
	}

	@Override
	public Buttons<P> setName(String name) {
		name = I18n.instance().get(name);
		names.add(name);
		buttons.put(name, newButton(name));
		return this;
	}

	public Button newButton(String name) {
		return new Button(name);
	}

	@Override
	public Buttons<P> action(Caller action) {
		actions.put(getLastName(), action);
		return this;
	}

	@Override
	public Buttons<P> isVisible(Supplier<Boolean> isVisible) {
		visibilities.put(getLastName(), isVisible);
		return this;
	}

	/**
	 * Solo se puedde cargar un boolean property por boton, usar cuando la
	 * visibilidad del boton dependa de cambios en la pantalla y no solo al inicio
	 * de la carga de los botones
	 * 
	 * @param isVisible {@link BooleanProperty}
	 * @return {@link Buttons}
	 */
	public Buttons<P> isVisible(BooleanProperty isVisible) {
		visibilitiesProperties.put(getLastName(), isVisible);
		return this;
	}

	@Override
	public Buttons<P> setCommand(String... commands) {
		Arrays.asList(commands).forEach(command -> this.commands.put(getLastName(), command));
		return this;
	}

	@Override
	public Buttons<P> styleCss(String... classCss) {
		Arrays.asList(classCss).forEach(clazzCss -> styles.put(getLastName(), clazzCss));
		return this;
	}

	@Override
	public void build() {
		var gridPane = new GridPane();
		layout.getChildren().add(gridPane);
		names.stream().forEach(name -> {
			var button = buttons.get(name);
			button.getStylesheets().add("./styles/principal.css");
			button.setStyle("-fx-margin: 10px");
			styles.get(name).forEach(style -> button.getStyleClass().add(style));
			if (visibilities.get(name).size() > 0) {
				button.setVisible(visibilities.get(name).stream().map(val -> val.get()).reduce(true,
						(val1, val2) -> val1 &= val2));
			}
			if (visibilitiesProperties.get(name) != null) {
				button.visibleProperty().bind(visibilitiesProperties.get(name));
			}
			if (icons.get(name) != null) {
				button.setGraphic(new Glyph("FontAwesome", icons.get(name)));
			}
			actions.get(name).stream().forEach(action -> button.setOnAction(event -> action.call()));
			gridPane.setHgap(10);
			gridPane.addRow(1, button);
		});
	}

	private String getLastName() {
		return names.get(names.size() - 1);
	}

	@Override
	public Buttons<P> icon(FontAwesome.Glyph fontIcon) {
		icons.put(getLastName(), fontIcon);
		return this;
	}
}
