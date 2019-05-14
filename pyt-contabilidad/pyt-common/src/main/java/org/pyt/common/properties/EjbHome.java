package org.pyt.common.properties;

import org.pyt.common.abstracts.AEjb;
import org.pyt.common.constants.PropertiesConstants;

/**
 * Se encarga de cargar las propiedades para los ejb remotos
 * 
 * @author Alejandro Parra
 * @since 13/05/2019
 */
public final class EjbHome extends AEjb{
	private static EjbHome ejb;

	private EjbHome() throws Exception {
		super(PropertiesConstants.PROP_EJB_HOME);
	}

	public static EjbHome getInstance() throws Exception {
		if (ejb == null) {
			ejb = new EjbHome();
		}
		return ejb;
	}
}
