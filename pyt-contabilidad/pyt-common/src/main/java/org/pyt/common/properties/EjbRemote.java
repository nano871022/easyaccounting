package org.pyt.common.properties;

import org.pyt.common.abstracts.AEjb;
import org.pyt.common.constants.PropertiesConstants;

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
