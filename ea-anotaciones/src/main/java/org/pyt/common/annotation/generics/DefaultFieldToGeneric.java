package org.pyt.common.annotation.generics;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * Esta anotacion es usada para indicar que un campo de un dto puede ser usado
 * por defecto(SIn configurar en pagina de parametrizacion de genericos) para
 * ser usado en una pantalla de generico, esto se realiza para no mostrar todos
 * los campos si no unos por defecto si se desea ver mas de estos campos se debe
 * configurar en parametrizacion de campos de genericoas
 * 
 * @author Alejandro Parra
 * @since 16/04/2019
 */
@Retention(RUNTIME)
@Target(FIELD)
@Repeatable(value=DefaultFieldToGenerics.class)
public @interface DefaultFieldToGeneric {
	/**
	 * Indica los usos que pede usar esta anotacion
	 *
	 */
	enum Uses {FILTER,COLUMN}
	/**
	 * Clase que indica que controller puede usar este campo como generico este debe
	 * extender de {@link IBean}
	 * 
	 * @return {@link Class} < {@link IBean} >
	 */
	public String simpleNameClazzBean();
	/**
	 * Indica el uso que se le puede dar a la anotacion {@link DefaultFieldToGeneric#Uses}
	 */
	public Uses use(); 
}
