package co.com.japl.ea.common.button.apufluid;

import static org.junit.Assert.assertEquals;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import javax.swing.SwingUtilities;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import co.com.japl.ea.common.button.apifluid.ButtonsImpl;
import javafx.embed.swing.JFXPanel;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;

public class ButtonsImplTest {

	private boolean jfxIsSetup;

	@Before
	public void initToolkit() throws InterruptedException {
		final CountDownLatch latch = new CountDownLatch(1);
		SwingUtilities.invokeLater(() -> {
			new JFXPanel(); // initializes JavaFX environment
			latch.countDown();
		});

		// That's a pretty reasonable delay... Right?
		if (!latch.await(5L, TimeUnit.SECONDS))
			throw new ExceptionInInitializerError();
	}

	@Test
	public void testCreateButtonsWhenAddOne() {
		var hb = new HBox();
		var button = new Button();
		var buttonName = "clickeado";
		var buttons = Mockito.spy(ButtonsImpl.Stream(hb.getClass()));

		Mockito.doReturn(button).when(((ButtonsImpl) buttons)).newButton(buttonName);

		buttons.setLayout(hb).setName(buttonName).action(() -> System.out.println("clickeado")).isVisible(() -> true)
				.setCommand("ALT+A").styleCss("buttons-disk-save").build();

		assertEquals(1, hb.getChildren().size());
	}

	@Test
	public void testCreateButtonsWhenAddTwo() {
		var hb = new HBox();
		var button1 = new Button();
		var buttonName = "clickeado";
		var buttonName2 = "cancel";
		var buttons = Mockito.spy(ButtonsImpl.Stream(hb.getClass()));

		Mockito.doReturn(button1).when(((ButtonsImpl) buttons)).newButton(buttonName);

		buttons.setLayout(hb).setName(buttonName).action(() -> System.out.println("clickeado")).isVisible(() -> true)
				.setCommand("ALT+A").styleCss("buttons-disk-save").setName(buttonName2)
				.action(() -> System.out.println("cancel")).setCommand("ALT+C").styleCss("button-cancel")
				.isVisible(() -> false).build();

		assertEquals(1, hb.getChildren().size());
	}

}
