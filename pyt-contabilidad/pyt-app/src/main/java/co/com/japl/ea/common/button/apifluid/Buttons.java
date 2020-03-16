package co.com.japl.ea.common.button.apifluid;

import java.util.function.Supplier;

import org.controlsfx.glyphfont.FontAwesome;

import javafx.beans.property.BooleanProperty;
import javafx.scene.layout.Pane;

public interface Buttons<P extends Pane> {

	/**
	 * Ingresa en donde el(os) boton(es) se pondran, en que layout, solo puede haber
	 * un layout.
	 * 
	 * @param <P>    extends {@link Pane}
	 * @param layout extends {@link Pane}
	 * @return {@link Buttons}
	 */
	public <T extends Pane> Buttons<P> setLayout(T layout);

	/**
	 * Ingresa el nombre que se mostrara, cada vez que ingrese un nombre iniciara
	 * una nueva configuración
	 * 
	 * @param name {@link String}
	 * @return {@link Buttons}
	 */
	public Buttons<P> setName(String name);

	/**
	 * En este punto por medio de un {@link Caller} que no retorna nada, ejecuta los
	 * que se ha dejado configurado
	 * 
	 * @param action {@link Caller}
	 * @return {@link Buttons}
	 */
	public Buttons<P> action(Caller action);

	/**
	 * En este punto por medio de un {@link Supplier} que retorna un {@link Boolean}
	 * retonar si se debe mostrar o no el botton se pueden asignar varios isVisible
	 * para el mismo botton
	 * 
	 * @param isVisible {@link Supplier} return {@link Boolean}
	 * @return {@link BUttons}
	 */
	public Buttons<P> isVisible(Supplier<Boolean> isVisible);

	/**
	 * Usado para indicar si es visible pero solo instancia actual, {@link Boolean}
	 * 
	 * @param isVisible {@link Boolean}
	 * @return {@link Buttons}
	 */
	public Buttons<P> isVisible(Boolean isVisible);

	/**
	 * Solo se puedde cargar un boolean property por boton, usar cuando la
	 * visibilidad del boton dependa de cambios en la pantalla y no solo al inicio
	 * de la carga de los botones
	 * 
	 * @param isVisible {@link BooleanProperty}
	 * @return {@link Buttons}
	 */
	public Buttons<P> isVisible(BooleanProperty isVisible);

	public Buttons<P> icon(FontAwesome.Glyph fontIcon);

	/**
	 * Ingresa los posibles comandos de teclado que seran los que activen la accion
	 * de click del boton
	 * 
	 * @param comand {String} {@link Array}
	 * @return {@link Buttons}
	 */
	public Buttons<P> setCommand(String... comand);

	/**
	 * Se ingresan las configruaciones de estilos de clases creados en las hojas de
	 * estilos
	 * 
	 * @param classCss {@link String} {@link Array}
	 * @return {@link Buttons}
	 */
	public Buttons<P> styleCss(String... classCss);

	/**
	 * Cuando se utiliza este metodo se finaliza la configuración y realiza la
	 * construccion de los botones con la configuracion indicada
	 */
	public void build();

}
