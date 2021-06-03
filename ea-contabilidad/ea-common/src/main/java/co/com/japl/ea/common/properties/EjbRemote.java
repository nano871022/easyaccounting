package co.com.japl.ea.common.properties;

import org.pyt.common.constants.PropertiesConstants;

import co.com.japl.ea.common.abstracts.AEjb;

/**
 * Se encarga de cargar las propiedades para los ejb remotos
 * 
 * @author Alejandro Parra
 * @since 13/05/2019
 */
public final class EjbRemote extends AEjb {
	private static EjbRemote ejb;
	private EjbRemote() throws Exception {
		super(PropertiesConstants.PROP_EJB_REMOTE);
	}

	public static EjbRemote getInstance() throws Exception {
		if (ejb == null) {
			ejb = new EjbRemote();
		}
		return ejb;
	}
}
