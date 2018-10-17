package org.pyt.app.beans.config;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.pyt.common.annotations.FXMLFile;
import org.pyt.common.annotations.Inject;
import org.pyt.common.common.ABean;
import org.pyt.common.constants.ConfigServiceConstant;
import org.pyt.common.poi.docs.Bookmark;
import org.pyt.common.poi.docs.DocX;
import org.pyt.common.poi.docs.TableBookmark;

import com.pyt.service.interfaces.IConfigMarcadorServicio;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;

/**
 * Se encarga de realizar la generacion de la configuracion de los servicios
 * 
 * @author Alejandro Parra
 * @since 16/10/2018
 */
@FXMLFile(path = "view/config/servicios", file = "genConfig.fxml",nombreVentana="Generar Archivo de Servicios")
public class GenConfigBean extends ABean {
	@FXML
	private Button btnGenerar;
	@FXML
	private Button btnCancelar;
	@FXML
	private Label nameGen;
	@FXML
	private AnchorPane Content;
	@Inject(resource = "com.pyt.service.implement.ConfigMarcadorServicioSvc")
	private IConfigMarcadorServicio configMarcadorServicio;
	private GridPane gridPane;
	private List<String> nombreCampos;
	private Map<String, Object> campos;
	private GenConfigServices gcs;
	private String nombreConfig;
	private DocX doc;
	public final static String PATH_DOCS = "./docs/";
	public Map<String,TextField> camposAgregados;

	@FXML
	public void initialize() {
		nombreCampos = new ArrayList<String>();
		gcs = new GenConfigServices(configMarcadorServicio);
		doc = new DocX();
		gridPane = new GridPane();
		camposAgregados = new HashMap<String,TextField>();
	}

	/**
	 * Se encarga de inicializar
	 */
	public void load(String nameGen) {
		if (StringUtils.isNotBlank(nameGen)) {
			this.nameGen.setText(nameGen);
			nombreConfig = nameGen;
		}
		this.nombreCampos.clear();
		gcs.setNombreConfiguracion(nameGen);
		String[] nombreCampos;
		try {
			nombreCampos = gcs.getConfigAsociar();
			if (nombreCampos == null || nombreCampos.length > 0) {
				for (String nombreCampo : nombreCampos) {
					this.nombreCampos.add(nombreCampo);
				}
			}
			loadGrid();
		} catch (Exception e) {
			error(e);
		}
	}
	/**
	 * Se encarga de crear la grilla con los objetos puestos en lass posiciones correctas
	 */
	private void loadGrid() {
		Integer columnIndex = 1;
		Integer rowIndex = 1;
		Integer maXRow = nombreCampos.size()<=5?nombreCampos.size():nombreCampos.size()<=16?nombreCampos.size()/2:nombreCampos.size()/3;
		Integer maxCol = nombreCampos.size()<=5?2:nombreCampos.size()<=16?4:6;
		for (String nombreCampo : nombreCampos) {
			camposAgregados.put(nombreCampo,new TextField());
			gridPane.add(new Label(nombreCampo), columnIndex, rowIndex);
			camposAgregados.get(nombreCampo).setVisible(true);
			camposAgregados.get(nombreCampo).setEditable(true);
			camposAgregados.get(nombreCampo).setDisable(false);
			gridPane.add(camposAgregados.get(nombreCampo), columnIndex+1, rowIndex);
			columnIndex += 2;
			if(columnIndex>maxCol) {
				columnIndex=0;
			}
			if(columnIndex==0) {
				rowIndex += 1;
			}
		}
	}

	/**
	 * Se encarga de generar
	 */
	public void generar() {
		List<Object> list = new ArrayList<Object>();
		try {
			list = gcs.gen(nombreConfig, campos);
			if (list.size() > 0) {
				for (Object mark : list) {
					if (mark instanceof TableBookmark) {
						doc.addTableBookmark((TableBookmark) mark);
					} else if (mark instanceof Bookmark) {
						doc.setBookmarks((Bookmark) mark);
					}
				}
				System.out.println("Nombre de archivo " + gcs.getNameFile());
				doc.setFile(PATH_DOCS + gcs.getNameFile());
				doc.setFileOut(PATH_DOCS + fileOut(gcs.getNameFile()));
				doc.generar();
			}
		} catch (Exception e) {
			error(e);
		}
	}

	/**
	 * Se encarga de retornar el nombre de salida
	 * 
	 * @param nameFileOrig
	 *            {@link String} nombre de entrada del archivo
	 * @return {@link String} nombre de salida
	 */
	private String fileOut(String nameFileOrig) {
		String[] name_split = nameFileOrig.split(ConfigServiceConstant.SEP_DOT);
		name_split[0] = name_split[0] + "_out";
		return String.join(ConfigServiceConstant.SEP_DOT, name_split);
	}

	/**
	 * Se encarga de cancelar la generacion
	 */
	public void cancelar() {

	}
}
