package co.com.japl.ea.beans.country;

import java.util.List;

import com.pyt.service.dto.PaisDTO;

import co.com.arquitectura.annotation.proccessor.FXMLFile;
import co.com.japl.ea.beans.abstracts.AGenericInterfacesBean;
import co.com.japl.ea.dto.system.ConfigGenericFieldDTO;
import javafx.fxml.FXML;
import javafx.scene.control.TableView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;

@FXMLFile(file = "list_country.fxml", path = "view/country")
public class ListCountry extends AGenericInterfacesBean<PaisDTO> {
	@FXML
	private TableView<PaisDTO> tblCountry;

	@FXML
	public void initialize() {

	}

	@Override
	public void selectedRow(MouseEvent eventHandler) {
		// TODO Auto-generated method stub

	}

	@Override
	public TableView<PaisDTO> getTableView() {
		return tblCountry;
	}

	@Override
	public Integer getMaxColumns(TypeGeneric typeGeneric) {
		return 4;
	}

	@Override
	public List<ConfigGenericFieldDTO> getListGenericsFields(TypeGeneric typeGeneric) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Class<PaisDTO> getClazz() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public GridPane getGridPane(TypeGeneric typeGeneric) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public PaisDTO getFilterToTable(PaisDTO filter) {
		// TODO Auto-generated method stub
		return null;
	}

}
