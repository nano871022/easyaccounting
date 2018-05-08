package org.pyt.app.beans;

import org.pyt.common.exceptions.EmpresasException;

import com.pyt.service.dto.EmpresaDTO;
import com.pyt.service.interfaces.IEmpresasSvc;

/**
 * Bean encargado de crear las empresas
 * 
 * @author alejandro parra
 * @since 07/05/2018
 */
public class EmpresaBean extends ABean<EmpresaDTO> {
	private IEmpresasSvc empresaSvc;

	public void init() {
		search();
		filtro = new EmpresaDTO();
		registro = new EmpresaDTO();
		page = 1;
		rows = 10;
	}

	/**
	 * Se encarga de obtener todos los registros
	 */
	public void search() {
		try {
			lista = empresaSvc.getEmpresas(filtro, page, rows);
			totalRows = empresaSvc.getTotalRows(filtro);
		} catch (EmpresasException e) {
			error(e);
		}
	}

}
