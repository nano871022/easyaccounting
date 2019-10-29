package co.com.japl.ea.beans.abstracts;

import java.lang.reflect.Field;
import java.util.Arrays;

import org.pyt.common.abstracts.ADto;
import org.pyt.common.annotations.Inject;

import com.pyt.service.interfaces.IQuerysPopup;

/**
 * Clase encargada de tener codio generico para poder implementar las paginas de
 * creacion dinamica segun el tipo de la clase indicado
 * 
 * @author Alejandro Parra
 * @since 09/04/2019
 */
public abstract class AGenericInterfacesReflectionBean<T extends ADto> extends AGenericToBean<T> {

	@Inject(resource = "com.pyt.service.implement.QuerysPopupSvc")
	protected IQuerysPopup querys;

	/**
	 * Se debe suministrar la clase con la cual se indica el generico
	 * 
	 * @param clazz {@link Class}
	 * @throws {@link Exception}
	 */
	public AGenericInterfacesReflectionBean(Class<T> clazz) throws Exception {
		super();
		setClazz(clazz);
		instaceOfGenericDTOAll();
	}

	/**
	 * Se encarga de recorrer todos los campos de la aplicacion y aquellos que sean
	 * del generico de la clase les crea una instancia en la cosntruccion de la
	 * clase
	 * 
	 * @throws {@link Exception}
	 */
	private final void instaceOfGenericDTOAll() throws Exception {
		Field[] fields = this.getClass().getDeclaredFields();
		Arrays.asList(fields).stream().filter(field -> field.getType() == ADto.class)
				.forEach(field -> configFieldValue(field));
	}

	/**
	 * Se encargad e agregar el valor en el campo y que se obliga a poder ingresarle
	 * informacion directamente
	 * 
	 * @param field {@link Field}
	 */
	private final void configFieldValue(Field field) {
		try {
			if (!field.canAccess(this)) {
				field.trySetAccessible();
			}
			field.set(this, getInstanceDto(TypeGeneric.FIELD));
		} catch (Exception exception) {
			logger().logger(exception);
		}
	}

}
