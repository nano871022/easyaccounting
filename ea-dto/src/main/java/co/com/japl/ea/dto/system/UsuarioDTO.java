package co.com.japl.ea.dto.system;

import java.util.Date;

import org.pyt.common.abstracts.ADto;

import com.pyt.service.dto.PersonaDTO;
/**
 * Se encarga de almacena rel suaurio que se encuentra en sesion en la aplicacion
 * @author alejandro parra
 * @since 06/05/2018
 */
public class UsuarioDTO extends ADto{
	private static final long serialVersionUID = -2598260782899678821L;
	private String nombre;
	private String password;
	private GroupUsersDTO grupoUser;
	private PersonaDTO person;
	private Date fechaInicio;
	private Date fechaFin;
	private Integer state;

	public Integer getState() {
		return this.state;
	}

	public void setState(Integer state) {
		this.state = state;
	}

	public PersonaDTO getPerson() {
		return this.person;
	}

	public void setPerson(PersonaDTO person) {
		this.person = person;
	}

	public GroupUsersDTO getGrupoUser() {
		return this.grupoUser;
	}

	public void setGrupoUser(GroupUsersDTO grupoUser) {
		this.grupoUser = grupoUser;
	}
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public Date getFechaIncio() {
		return fechaInicio;
	}
	public void setFechaIncio(Date fechaIncio) {
		this.fechaInicio = fechaIncio;
	}
	public Date getFechaFin() {
		return fechaFin;
	}
	public void setFechaFin(Date fechaFin) {
		this.fechaFin = fechaFin;
	}
}
