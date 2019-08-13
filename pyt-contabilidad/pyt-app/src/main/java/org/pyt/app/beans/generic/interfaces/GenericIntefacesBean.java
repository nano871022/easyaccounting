package org.pyt.app.beans.generic.interfaces;

import co.com.arquitectura.annotation.proccessor.FXMLFile;
import co.com.japl.ea.beans.ABean;
import co.com.japl.ea.dto.system.ConfigGenericFieldDTO;

@FXMLFile(path = "/view/genericInterfaces", file = "generic_interfaces.fxml")
public class GenericIntefacesBean extends ABean<ConfigGenericFieldDTO> {

	public final void load() {
		registro = new ConfigGenericFieldDTO();
	}

	public final void load(ConfigGenericFieldDTO dto) {
		registro = dto;
	}
}
