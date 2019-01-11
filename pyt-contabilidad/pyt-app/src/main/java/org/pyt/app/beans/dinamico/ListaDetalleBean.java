package org.pyt.app.beans.dinamico;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.pyt.app.components.ConfirmPopupBean;
import org.pyt.app.components.DataTableFXML;
import org.pyt.common.annotations.Inject;
import org.pyt.common.common.ABean;
import org.pyt.common.common.LoadAppFxml;
import org.pyt.common.exceptions.DocumentosException;

import com.pyt.service.dto.DetalleDTO;
import com.pyt.service.dto.ParametroDTO;
import com.pyt.service.interfaces.IDocumentosSvc;

import co.com.arquitectura.annotation.proccessor.FXMLFile;
import javafx.beans.property.SimpleObjectProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * Se encargade crear la pantalla de lista de detalles
 * 
 * @author Alejandro Parra
 * @since 10-07-2018
 */
@FXMLFile(path = "view/dinamico", file = "listDetalle.fxml", nombreVentana = "Lista de Detalles")
public class ListaDetalleBean extends ABean<DetalleDTO> {
	@Inject(resource = "com.pyt.service.implement.DocumentosSvc")
	private IDocumentosSvc documentosSvc;
	@FXML
	private HBox paginador;
	@FXML
	private TableView<DetalleDTO> tabla;
	@FXML
	private Button editar;
	@FXML
	private Button eliminar;
	@FXML
	private TableColumn<DetalleDTO, String> centroCosto;
	@FXML
	private TableColumn<DetalleDTO, String> categoria;
	@FXML
	private TableColumn<DetalleDTO, String> concepto;
	@FXML
	private Label sumatoria;
	private VBox panelCentral;
	@SuppressWarnings("unused")
	private DetalleDTO filtro;
	private DetalleDTO registro;
	private DataTableFXML<DetalleDTO, DetalleDTO> table;
	private ParametroDTO tipoDocumento;
	private String codigoDocumento;

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@FXML
	private final void initialize() {
		registro = new DetalleDTO();
		filtro = new DetalleDTO();
		eliminar.setVisible(false);
		editar.setVisible(false);
		centroCosto.setCellValueFactory(e -> {
			SimpleObjectProperty<String> o = new SimpleObjectProperty();
			o.setValue(e.getValue().getCentroCosto().getNombre());
			return o;
		});
		categoria.setCellValueFactory(e -> {
			SimpleObjectProperty<String> o = new SimpleObjectProperty();
			if(e.getValue() != null && e.getValue().getCategoriaGasto() != null && e.getValue().getCategoriaGasto().getNombre() != null){ 
			o.setValue(
					e.getValue().
						getCategoriaGasto().
							getNombre());
			}
			return o;
		});
		concepto.setCellValueFactory(e -> {
			SimpleObjectProperty<String> o = new SimpleObjectProperty();
			o.setValue(e.getValue().getConcepto().getNombre());
			return o;
		});
		lazy();
	}

	/**
	 * Se encarga de cargar la pagina del listado de detalles agregados
	 */
	private final void lazy() {
		table = new DataTableFXML<DetalleDTO, DetalleDTO>(paginador, tabla) {

			@Override
			public Integer getTotalRows(DetalleDTO filter) {
				try {
					return documentosSvc.getTotalRows(filter);
				} catch (DocumentosException e) {
					error(e);
				}
				return 0;
			}

			@Override
			public List<DetalleDTO> getList(DetalleDTO filter, Integer page, Integer rows) {

				List<DetalleDTO> lista = new ArrayList<DetalleDTO>();
				try {
					lista = documentosSvc.getDetalles(filter, page - 1, rows);
					sumatoria.setText(sumatoria(lista, "valorNeto").toString());
				} catch (DocumentosException e) {
					error(e);
				}
				return lista;
			}

			@Override
			public DetalleDTO getFilter() {
				DetalleDTO dto = new DetalleDTO();
				if (StringUtils.isNotBlank(codigoDocumento)) {
					dto.setCodigoDocumento(codigoDocumento);
				}
				return dto;
			}
		};
	}

	/**
	 * Se encarga de cargar los campos del filtro
	 */
	private final void loadFiltro() {
		filtro = new DetalleDTO();
	}

	/**
	 * Se encarga de cargar la interfaz con los registros
	 * 
	 * @param tipoDocumento
	 *            {@link ParametroDTO}
	 */
	public final void load(VBox panel, ParametroDTO tipoDocumento, String codigoDocumento) throws Exception {
		if (tipoDocumento == null || StringUtils.isBlank(tipoDocumento.getCodigo()))
			throw new Exception("No se suministro el tipo de documento.");
		if (panel == null)
			throw new Exception("El panel de creacion no se suministro.");
		if (StringUtils.isBlank(codigoDocumento))
			throw new Exception("No se suministro el codigo del documento..");
		this.tipoDocumento = tipoDocumento;
		this.codigoDocumento = codigoDocumento;
		panelCentral = panel;
		table.search();
	}

	/**
	 * Se encarga de llamar el bean para cargar un nuevo registro
	 */
	public final void agregar() {
		try {
			getController(panelCentral, DetalleBean.class).load(panelCentral, tipoDocumento, codigoDocumento);
		} catch (Exception e) {
			error("No se logro cargar la pantalla para agregar el nuevo detalle.");
		}
	}

	/**
	 * Se encarga de buscar en la tabla
	 */
	public final void buscar() {
		loadFiltro();
		table.search();
	}

	/**
	 * Se encarga de llamar el bean para editar un nuevo registro
	 */
	public final void editar() {
		if (table.isSelected()) {
			List<DetalleDTO> list = table.getSelectedRows();
			if (list.size() == 1) {
				registro = table.getSelectedRow();
				try {
					getController(panelCentral, DetalleBean.class).load(panelCentral, registro, tipoDocumento);
				} catch (Exception e) {
					error("No se logro la pantalla para editar el detalle.");
				}
			} else if (list.size() > 1) {
				error("Se seleccionaron varios detalles.");
			} else {
				error("No se selecciono ningun detalle.");
			}
		}
	}

	/**
	 * Se encarga de llamar el bean para eliminar un registro
	 */
	public final void eliminar() {
		try {
			LoadAppFxml.loadBeanFxml(new Stage(), ConfirmPopupBean.class).load("#{ListaDetalleBean.delete}", "¿Desea eliminar los registros seleccionados?");
		}catch(Exception e) {
			error(e);
		}
	}
	public void setDelete(Boolean valid) {
			if(!valid)return;
		if (table.isSelected()) {
			List<DetalleDTO> lista = table.getSelectedRows();
			Integer i = 0;
			for (DetalleDTO detalle : lista) {
				try {
					documentosSvc.delete(detalle, userLogin);
					i++;
				} catch (DocumentosException e) {
					error(e);
				}
			} // end for
			notificar("Se eliminaron " + i + "/" + lista.size() + " detalles.");
		} else {
			notificar("No se selecciono registros a eliminar.");
		}
	}

	/**
	 * Se encarga de seleccionar un registro de la tabla
	 */
	public final void seleccionar() {
		if (table.isSelected()) {
			eliminar.setVisible(true);
			editar.setVisible(true);
		}
	}
}
