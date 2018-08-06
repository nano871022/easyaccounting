package org.pyt.app.beans.config;

import org.pyt.common.annotations.FXMLFile;
import org.pyt.common.common.ABean;

import com.pyt.service.dto.AsociacionArchivoDTO;
/**
 * Se encarga de controlar las asociacionesentra marcadores y servicios 
 * @author Alejandro Parra
 * @since 05-08-2018
 */
@FXMLFile(path = "view/config/servicios", file = "ConfigService.fxml")
public class ConfigServiceBean extends ABean<AsociacionArchivoDTO> {

}
