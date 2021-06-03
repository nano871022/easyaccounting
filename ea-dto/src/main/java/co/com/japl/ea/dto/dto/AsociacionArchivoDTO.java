package co.com.japl.ea.dto.dto;

import java.util.List;

import co.com.japl.ea.common.abstracts.ADto;

public class AsociacionArchivoDTO extends ADto {
	private static final long serialVersionUID = 4379123174272367940L;
	private String archivo;
	private String nombre;
	private List<MarcadorServicioDTO> marcadorServicios;
	private List<ServicioCampoBusquedaDTO> servicioCampoBusquedas;
	public String getArchivo() {
		return archivo;
	}
	public void setArchivo(String archivo) {
		this.archivo = archivo;
	}
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public List<MarcadorServicioDTO> getMarcadorServicios() {
		return marcadorServicios;
	}
	public void setMarcadorServicios(List<MarcadorServicioDTO> marcadorServicios) {
		this.marcadorServicios = marcadorServicios;
	}
}
