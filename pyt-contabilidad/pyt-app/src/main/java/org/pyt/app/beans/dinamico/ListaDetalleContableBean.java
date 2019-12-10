package org.pyt.app.beans.dinamico;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.pyt.app.components.ConfirmPopupBean;
import org.pyt.common.annotations.Inject;
import org.pyt.common.exceptions.DocumentosException;

import com.pyt.service.dto.DetalleContableDTO;
import com.pyt.service.dto.ParametroDTO;
import com.pyt.service.interfaces.IDocumentosSvc;

import co.com.arquitectura.annotation.proccessor.FXMLFile;
import co.com.japl.ea.beans.abstracts.ABean;
import co.com.japl.ea.utls.DataTableFXMLUtil;
import javafx.beans.property.SimpleObjectProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

/**
 * Se encargade crear la pantalla de lista de detalles
 * 
 * @author Alejandro Parra
 * @since 10-07-2018
 */
@FXMLFile(path = "view/dinamico", file = "listDetalleContable.fxml", nombreVentana = "Lista de Detalles")
public class ListaDetalleContableBean extends ABean<DetalleContableDTO> {
	@Inject(resource = "com.pyt.service.implement.DocumentosSvc")
	private IDocumentosSvc documentosSvc;
	@FXML
	private HBox paginador;
	@FXML
	private TableView<DetalleContableDTO> tabla;
	@FXML
	private Button editar;
	@FXML
	private Button eliminar;
	@FXML
	private TableColumn<DetalleContableDTO, String> cuentaContable;
	@FXML
	private TableColumn<DetalleContableDTO, String> concepto;
	@FXML
	private Label sumatoria;
	private VBox panelCentral;
	private DetalleContableDTO filtro;
	private DetalleContableDTO registro;
	private DataTableFXMLUtil<DetalleContableDTO, DetalleContableDTO> table;
	private ParametroDTO tipoDocumento;
	private String codigoDocumento;

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@FXML
	private final void initialize() {
		registro = new DetalleContableDTO();
		filtro = new DetalleContableDTO();
		eliminar.setVisible(false);
		editar.setVisible(false);
		cuentaContable.setCellValueFactory(e -> {
			SimpleObjectProperty<String> o = new SimpleObjectProperty();
			if (e.getValue() != null && e.getValue().getCuentaContable() != null)
				o.setValue(e.getValue().getCuentaContable().getNombre());
			return o;
		});
		concepto.setCellValueFactory(e -> {
			SimpleObjectProperty<String> o = new SimpleObjectProperty();
			if (e.getValue() != null && e.getValue().getConcepto() != null)
				o.setValue(e.getValue().getConcepto().getNombre());
			return o;
		});
		lazy();
	}

	/**
	 * Se encarga de cargar la pagina del listado de detalles agregados
	 */
	private final void lazy() {
		table = new DataTableFXMLUtil<DetalleContableDTO, DetalleContableDTO>(paginador, tabla) {

			@Override
			public Integer getTotalRows(DetalleContableDTO filter) {
				try {
					return documentosSvc.getTotalCount(filter);
				} catch (DocumentosException e) {
					error(e);
				}
				return 0;
			}

			@Override
			public List<DetalleContableDTO> getList(DetalleContableDTO filter, Integer page, Integer rows) {

				List<DetalleContableDTO> lista = new ArrayList<DetalleContableDTO>();
				try {
					lista = documentosSvc.getDetalles(filter, page - 1, rows);
					sumatoria.setText(sumatoria(lista,"valor").toString());
				} catch (DocumentosException e) {
					error(e);
				}
				return lista;
			}

			@Override
			public DetalleContableDTO getFilter() {
				DetalleContableDTO dto = new DetalleContableDTO();
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
		filtro = new DetalleContableDTO();
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
			getController(panelCentral, DetalleContableBean.class).load(panelCentral, tipoDocumento, codigoDocumento);
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
			List<DetalleContableDTO> list = table.getSelectedRows();
			if (list.size() == 1) {
				registro = table.getSelectedRow();
				try {
					getController(panelCentral, DetalleContableBean.class).load(panelCentral, registro, tipoDocumento,
							codigoDocumento);
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
			controllerPopup(ConfirmPopupBean.class).load("#{ListaDetalleContableBean.delete}", "¿Desea eliminar los registros seleccionados?");
		}catch(Exception e) {
			error(e);
		}
	}
	public void setDelete(Boolean valid) {
			if(!valid)return;
		if (table.isSelected()) {
			List<DetalleContableDTO> lista = table.getSelectedRows();
			Integer i = 0;
			for (DetalleContableDTO detalle : lista) {
				try {
					documentosSvc.delete(detalle, getUsuario());
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
