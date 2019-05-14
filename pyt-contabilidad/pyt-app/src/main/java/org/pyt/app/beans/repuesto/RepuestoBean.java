package org.pyt.app.beans.repuesto;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.pyt.app.components.ConfirmPopupBean;
import org.pyt.app.components.DataTableFXML;
import org.pyt.common.abstracts.ABean;
import org.pyt.common.annotations.Inject;
import org.pyt.common.common.LoadAppFxml;
import org.pyt.common.exceptions.inventario.ProductosException;

import com.pyt.service.dto.inventario.ProductoDto;
import com.pyt.service.interfaces.inventarios.IProductosSvc;

import co.com.arquitectura.annotation.proccessor.FXMLFile;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

/**
 * Bean encargado de crear las empresas
 * 
 * @author alejandro parra
 * @since 07/05/2018
 */
@FXMLFile(path = "view/repuesto", file = "listRepuesto.fxml")
public class RepuestoBean extends ABean<ProductoDto> {
	@Inject(resource = "com.pyt.service.implement.inventario.ProductosSvc")
	private IProductosSvc productosSvc;
	@FXML
	private javafx.scene.control.TableView<ProductoDto> tabla;
	@FXML
	private TextField nombre;
	@FXML
	private Button btnMod;
	@FXML
	private HBox paginador;
	private DataTableFXML<ProductoDto, ProductoDto> dt;

	@FXML
	public void initialize() {
		NombreVentana = "Lista de Repuestos";
		registro = new ProductoDto();
		lazy();
	}

	/**
	 * encargada de crear el objeto que va controlar la tabla
	 */
	public void lazy() {
		dt = new DataTableFXML<ProductoDto, ProductoDto>(paginador, tabla) {
			@Override
			public List<ProductoDto> getList(ProductoDto filter, Integer page, Integer rows) {
				List<ProductoDto> lista = new ArrayList<ProductoDto>();
				try {
					lista = productosSvc.productos(filter, page-1, rows);
				} catch (ProductosException e) {
					error(e);
				}
				return lista;
			}

			@Override
			public Integer getTotalRows(ProductoDto filter) {
				Integer count = 0;
				try {
					count = productosSvc.getTotalRows(filter);
				} catch (ProductosException e) {
					error(e);
				}
				return count;
			}

			@Override
			public ProductoDto getFilter() {
				ProductoDto filtro = new ProductoDto();
				if (StringUtils.isNotBlank(nombre.getText())) {
					filtro.setNombre("%"+nombre.getText()+"%");
				}
				return filtro;
			}
		};
	}

	public void clickTable() {
		btnMod.setVisible(isSelected());
	}

	public void add() {
		getController(RepuestoCRUBean.class).load();
	}

	public void search() {
		dt.search();
	}

	@SuppressWarnings("unchecked")
	public void del() {
		try {
			controllerPopup(ConfirmPopupBean.class).load("#{RepuestoBean.delete}", "¿Desea eliminar los registros seleccionados?");
		}catch(Exception e) {
			error(e);
		}
	}
	public void setDelete(Boolean valid) {
		try {
			if(!valid)return;
			registro = dt.getSelectedRow();
			if (registro != null) {
				productosSvc.del(registro, userLogin);
				notificar("Se ha eliminado el repuesto.");
				dt.search();
			} else {
				notificar("No se ha seleccionado un repuesto.");
			}
		} catch (ProductosException e) {
			error(e);
		}
	}

	public void set() {
		registro = dt.getSelectedRow();
		if (registro != null) {
			getController(RepuestoCRUBean.class).load(registro);
		} else {
			notificar("No se ha seleccionado un repuesto.");
		}
	}

	public Boolean isSelected() {
		return dt.isSelected();
	}

	public DataTableFXML<ProductoDto, ProductoDto> getDt() {
		return dt;
	}
}
