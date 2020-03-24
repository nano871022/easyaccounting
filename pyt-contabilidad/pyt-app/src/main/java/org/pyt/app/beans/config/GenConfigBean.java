package org.pyt.app.beans.config;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.pyt.common.annotations.Inject;
import org.pyt.common.constants.ConfigServiceConstant;
import org.pyt.common.poi.docs.Bookmark;
import org.pyt.common.poi.docs.TableBookmark;
import org.pyt.common.poi.gen.DocxToPDFGen;

import com.pyt.service.interfaces.IConfigMarcadorServicio;

import co.com.arquitectura.annotation.proccessor.FXMLFile;
import co.com.japl.ea.beans.abstracts.ABean;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

/**
 * Se encarga de realizar la generacion de la configuracion de los servicios
 * 
 * @author Alejandro Parra
 * @since 16/10/2018
 */
@SuppressWarnings("rawtypes")
@FXMLFile(path = "view/config/servicios", file = "genConfig.fxml", nombreVentana = "Generar Archivo de Servicios")
public class GenConfigBean extends ABean {
	@FXML
	private BorderPane window;
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
	private DocxToPDFGen doc;
	public final static String PATH_DOCS = "./docs/";
	public Map<String, TextField> camposAgregados;

	@FXML
	public void initialize() {
		nombreCampos = new ArrayList<String>();
		gcs = new GenConfigServices(configMarcadorServicio);
		doc = new DocxToPDFGen();
		gridPane = new GridPane();
		campos = new HashMap<String, Object>();
		camposAgregados = new HashMap<String, TextField>();
		Content.getChildren().add(gridPane);
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
				Arrays.asList(nombreCampos).forEach(nombreCampo -> this.nombreCampos.add(nombreCampo));
			}
			loadGrid();
		} catch (Exception e) {
			error(e);
		}
	}

	/**
	 * Se encarga de crear la grilla con los objetos puestos en lass posiciones
	 * correctas
	 */
	@SuppressWarnings({ "static-access", "unused" })
	private void loadGrid() {
		Integer columnIndex = 0;
		Integer rowIndex = 0;
		Integer maXRow = nombreCampos.size() <= 5 ? nombreCampos.size()
				: nombreCampos.size() <= 16 ? nombreCampos.size() / 2 : nombreCampos.size() / 3;
		Integer maxCol = nombreCampos.size() <= 5 ? 2 : nombreCampos.size() <= 16 ? 4 : 6;
		for (String nombreCampo : nombreCampos) {
			camposAgregados.put(nombreCampo, new TextField());
			camposAgregados.get(nombreCampo).setVisible(true);
			camposAgregados.get(nombreCampo).setEditable(true);
			camposAgregados.get(nombreCampo).setDisable(false);
			gridPane.add(new Label(nombreCampo), columnIndex, rowIndex, 1, 1);
			gridPane.add(camposAgregados.get(nombreCampo), columnIndex + 1, rowIndex, 1, 1);
			gridPane.setMargin(camposAgregados.get(nombreCampo), new Insets(5, 5, 5, 5));
			gridPane.setPadding(new Insets(5, 5, 5, 5));
			columnIndex += 2;
			if (columnIndex > maxCol) {
				columnIndex = 0;
			}
			if (columnIndex == 0) {
				rowIndex += 1;
			}
		}
		Content.setMinHeight(gridPane.getHeight() + 50);
		gridPane.setMaxWidth(Content.getMaxWidth());
	}

	/**
	 * Se encarga de generar
	 */
	public void generar() {
		List<Object> list = new ArrayList<Object>();
		loadValueFields();
		try {// DocumentotvgÑO0
			list = gcs.gen(nombreConfig, campos);
			if (list.size() > 0) {
				for (Object mark : list) {
					if (mark instanceof TableBookmark) {
						doc.addTableBookmark((TableBookmark) mark);
					} else if (mark instanceof Bookmark) {
						doc.setBookmarks((Bookmark) mark);
					}
				}
				String nameFile = gcs.getNameFile();
				System.out.println("Nombre de archivo " + nameFile);
				doc.setFileInput(PATH_DOCS + nameFile);
				doc.setFileOutput(PATH_DOCS + fileOut(nameFile));
				String output = doc.generate();
				cancelar();
				notificar("Se genero " + nombreConfig + " con el nombre " + output);
			}
		} catch (Exception e) {
			error(e);
		}
	}

	private void loadValueFields() {
		campos.clear();
		Set<String> lcampos = camposAgregados.keySet();
		for (String campo : lcampos) {
			campos.put(campo, getValue(campo));
		}
	}

	private final <S extends Node> String getValue(String nombreCampo) {
		TextField campo = camposAgregados.get(nombreCampo);
		return campo.getText();
	}

	/**
	 * Se encarga de retornar el nombre de salida
	 * 
	 * @param nameFileOrig {@link String} nombre de entrada del archivo
	 * @return {@link String} nombre de salida
	 */
	private String fileOut(String nameFileOrig) {
		String[] name_split = nameFileOrig.split(ConfigServiceConstant.SEP_BACK_DOT);
		name_split[0] = name_split[0] + "_out";
		return String.join(ConfigServiceConstant.SEP_DOT, name_split);
	}

	/**
	 * Se encarga de cancelar la generacion
	 */
	public void cancelar() {
		Stage stage = (Stage) window.getScene().getWindow();
		stage.close();
	}

	@Override
	protected void visibleButtons() {
		// TODO Auto-generated method stub

	}
}
