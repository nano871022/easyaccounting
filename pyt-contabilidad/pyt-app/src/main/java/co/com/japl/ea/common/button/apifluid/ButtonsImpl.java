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
import org.pyt.common.common.ListUtils;

import co.com.japl.ea.utls.LoadAppFxml;
import javafx.beans.property.BooleanProperty;
import javafx.scene.control.Button;
import javafx.scene.control.Tooltip;
import javafx.scene.input.KeyCharacterCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.Mnemonic;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;

public class ButtonsImpl<P extends Pane> implements Buttons<P> {

	private P layout;
	private List<String> names;
	private Map<String, Button> buttons;
	private MultiValuedMap<String, Supplier<Boolean>> visibilities;
	private Map<String, Boolean> booleanvisibilities;
	private Map<String, BooleanProperty> visibilitiesProperties;
	private Map<String, FontAwesome.Glyph> icons;
	private MultiValuedMap<String, Caller> actions;
	private MultiValuedMap<String, String> styles;
	private Map<String, String> commands;
	private Map<String, Boolean> br;
	private Integer[] minMaxAllBtn;

	private ButtonsImpl() {
		buttons = new HashMap<>();
		minMaxAllBtn = null;
		visibilities = new ArrayListValuedHashMap<String, Supplier<Boolean>>();
		actions = new ArrayListValuedHashMap<String, Caller>();
		styles = new ArrayListValuedHashMap<String, String>();
		booleanvisibilities = new HashMap<String, Boolean>();
		names = new ArrayList<>();
		icons = new HashMap<>();
		commands = new HashMap<String, String>();
		visibilitiesProperties = new HashMap<String, BooleanProperty>();
		br = new HashMap<String, Boolean>();
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
		var name2 = I18n.instance().get(name);
		configButtons(name, name2);
		return this;
	}

	public Buttons<P> widthMinMaxAllBtn(Integer min, Integer max) {
		Integer[] minMax = new Integer[2];
		minMax[0] = min;
		minMax[1] = max;
		minMaxAllBtn = minMax;
		return this;
	}

	@Override
	public Buttons<P> setReference(String reference) {
		configButtons(reference, "");
		return this;
	}

	private void configButtons(String reference, String name) {
		reference = I18n.instance().get(reference);
		names.add(reference);
		var button = newButton(name);
		var tooltip = new Tooltip(reference);
		button.setTooltip(tooltip);
		buttons.put(reference, button);
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
	public Buttons<P> setCommand(String command) {
		this.commands.put(getLastName(), command);
		return this;
	}

	@Override
	public Buttons<P> styleCss(String... classCss) {
		Arrays.asList(classCss).forEach(clazzCss -> styles.put(getLastName(), clazzCss));
		return this;
	}

	public Buttons<P> BR() {
		br.put(getLastName(), true);
		return this;
	}

	@Override
	public void build() {
		var gridPane = new GridPane();
		layout.getChildren().add(gridPane);
		names.stream().forEach(name -> {
			var button = buttons.get(name);
			button.getStylesheets().add("styles/principal.css");
			button.setStyle("-fx-margin: 10px");
			styles.get(name).forEach(style -> button.getStyleClass().add(style));

			if (ListUtils.isNotBlank(visibilities.get(name))) {
				button.setVisible(visibilities.get(name).stream().map(val -> val.get()).reduce(true,
						(val1, val2) -> val1 &= val2));
			}
			if (visibilitiesProperties.containsKey(name)) {
				button.visibleProperty().bind(visibilitiesProperties.get(name));
			}
			if (booleanvisibilities.containsKey(name)) {
				button.setVisible(booleanvisibilities.get(name));
			}
			if (icons.containsKey(name)) {
				button.setGraphic(new Glyph("FontAwesome", icons.get(name)));
			}
			if (minMaxAllBtn != null && minMaxAllBtn.length == 2) {
				button.setMinWidth(minMaxAllBtn[0]);
				button.setMaxWidth(minMaxAllBtn[1]);
			}
			if (commands.get(name) != null) {
				var kc = new KeyCharacterCombination(commands.get(name), KeyCombination.ALT_DOWN);
				var mnemonic = new Mnemonic(button, kc);
				button.setMnemonicParsing(true);
				if (LoadAppFxml.loadingApp().getStage() != null
						&& LoadAppFxml.loadingApp().getStage().getScene() != null) {
					LoadAppFxml.loadingApp().getStage().getScene().addMnemonic(mnemonic);
				}
			}
			actions.get(name).stream().forEach(action -> button.setOnAction(event -> action.call()));
			gridPane.getStyleClass().add("borderView");
			gridPane.setHgap(10);
			gridPane.addRow(numberRow(gridPane, br.containsKey(name)), button);
		});
	}

	private Integer numberRow(GridPane gridPane, boolean addRow) {
		var count = gridPane.getRowCount();
		if (addRow) {
			return count + 1;
		} else if (count == 0) {
			return 0;
		}
		return count - 1;

	}

	private String getLastName() {
		return names.get(names.size() - 1);
	}

	@Override
	public Buttons<P> icon(FontAwesome.Glyph fontIcon) {
		icons.put(getLastName(), fontIcon);
		return this;
	}

	@Override
	public Buttons<P> isVisible(Boolean isVisible) {
		booleanvisibilities.put(getLastName(), isVisible);
		return this;
	}
}
