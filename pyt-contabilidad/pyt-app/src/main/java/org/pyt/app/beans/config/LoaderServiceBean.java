package org.pyt.app.beans.config;

import java.io.File;
import java.io.FileInputStream;
import java.nio.file.Files;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.pyt.common.annotations.Inject;
import org.pyt.common.common.ABean;

import com.pyt.service.dto.ConfiguracionDTO;
import com.pyt.service.interfaces.ICargue;
import com.pyt.service.interfaces.IConfigMarcadorServicio;

import co.com.arquitectura.annotation.proccessor.FXMLFile;
import co.com.japl.ea.loader.pojo.FilePOJO;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

/**
 * se encarga de cargar un archivo seleccionado y enviarlo al servicio para
 * cargar el archivo
 * 
 * @author Alejandro Parra
 * @since 18/11/2018
 */
@SuppressWarnings("rawtypes")
@FXMLFile(path = "view/config/servicios", file = "loaderConfig.fxml", nombreVentana = "cargue de Archivo sobre Servicios")
public class LoaderServiceBean extends ABean {
	private FileChooser loadFile;
	@FXML
	private BorderPane window;
	@FXML
	private Label nameFile;
	@FXML
	private Label typeFile;
	@FXML
	private Label sizeFile;
	private File fileLoad;
	@Inject(resource = "com.pyt.service.implement.ConfigMarcadorServicioSvc")
	private IConfigMarcadorServicio configServicio;
	@Inject(resource="com.pyt.service.implement.CargueSvc")
	private ICargue loader;
	private ConfiguracionDTO configuracion;
	@FXML
	public void initialize() {
		configuracion = new ConfiguracionDTO();
		loadFile = new FileChooser();
	}

	@SuppressWarnings("unchecked")
	public void load(String nameConfig) {
		try {
			if (StringUtils.isBlank(nameConfig))
				throw new Exception("No se suministro el nombre de la configuracion.");
			configuracion.setConfiguracion(nameConfig);
			List<ConfiguracionDTO> list = configServicio.getConfiguraciones(configuracion);
			if (list.size() > 1) {
				throw new Exception("Se obtuvieron varios registros de la configuracion.");
			}
			configuracion = list.get(0);
			if (configuracion.getReport())
				throw new Exception("La configuracion es un reporte, esta opcion es invalida.");

		} catch (Exception e) {
			configuracion = null;
			error(e);
			cancel();
		}
	}

	@SuppressWarnings("unchecked")
	public final void cargar() {
		try {
			fileLoad = loadFile.showOpenDialog(null);
			if (fileLoad != null && fileLoad.exists() && fileLoad.isFile()) {
				nameFile.setText(fileLoad.getName());
				sizeFile.setText(String.valueOf(fileLoad.getTotalSpace()));
				typeFile.setText(Files.probeContentType(fileLoad.toPath()));
			}else {
				error("El archivo seleccionado no es valido.");
			}
		} catch (Exception e) {
			error(e);
		}
	}

	@SuppressWarnings({ "resource", "unchecked" })
	public final void procesar() {
		try {
			if (configuracion == null)
				throw new Exception("No se encontro la configuracion, opcion invalida.");
			if(fileLoad == null || !fileLoad.exists() || !fileLoad.isFile())throw new Exception("El archivo seleccionado no es valido");
			FileInputStream fis = new FileInputStream(fileLoad);
			FilePOJO file = new FilePOJO();
			byte[] b = fis.readAllBytes();
			file.setBytes(b);
			file.setNameFile(fileLoad.getName());
			file.setTypeFile(Files.probeContentType(fileLoad.toPath()));
			file.setSize(fileLoad.length());
			file.setSeparate(";");
			// llamando al servicio de cargue de datos.
			FilePOJO out = loader.cargue(configuracion.getConfiguracion(), file, userLogin);
			File fileo = new File(out.getNameFile());
			FileUtils.writeByteArrayToFile(fileo, out.getByte());
			this.notificar("Se genero el archivo de resultados en "+fileo.getAbsolutePath());
			cancel();
		} catch (Exception e) {
			error(e);
		}
	}

	public final void cancel() {
		fileLoad = null;
		nameFile.setText("");
		sizeFile.setText("");
		typeFile.setText("");
		Stage stage = (Stage) window.getScene().getWindow();
		stage.close();
	}
}
