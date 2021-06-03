package co.com.japl.ea.dto.dto;

import org.pyt.common.annotation.generics.AssingValue;
import org.pyt.common.annotation.generics.DefaultFieldToGeneric;
import org.pyt.common.constants.ParametroConstants;

import co.com.japl.ea.common.abstracts.ADto;
import co.com.japl.ea.dto.dto.dels.ParametroDelDTO;
import co.com.japl.ea.dto.dto.upds.ParametroUpdDTO;

import co.com.arquitectura.annotation.proccessor.DelClass;
import co.com.arquitectura.annotation.proccessor.UpdClass;

/**
 * Se almacenan todos los parametros que se van a utilizar en la aplicacion y se
 * separan por grupos.
 * 
 * @author alejandro parra
 * @since 06/05/2018
 */
@DelClass(clase = ParametroDelDTO.class)
@UpdClass(clase = ParametroUpdDTO.class)
public class ParametroDTO extends ADto {
	private static final long serialVersionUID = -5396836082089633791L;

	private Long orden;
	@DefaultFieldToGeneric(simpleNameClazzBean = "PopupGenBean", use = DefaultFieldToGeneric.Uses.FILTER)
	@DefaultFieldToGeneric(simpleNameClazzBean = "PopupGenBean", use = DefaultFieldToGeneric.Uses.COLUMN)
	private String nombre;
	@DefaultFieldToGeneric(simpleNameClazzBean = "PopupGenBean", use = DefaultFieldToGeneric.Uses.COLUMN, width = 200)
	private String descripcion;
	private String valor;
	private String valor2;
	private String grupo;
	@AssingValue(nameField = ParametroConstants.FIELD_NAME_STATE, value = ParametroConstants.COD_ESTADO_PARAMETRO_ACTIVO_STR)
	private String estado;

	public Long getOrden() {
		return orden;
	}

	public void setOrden(Long orden) {
		this.orden = orden;
	}

	public String getCodigo() {
		return codigo;
	}

	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public String getValor() {
		return valor;
	}

	public void setValor(String valor) {
		this.valor = valor;
	}

	public String getValor2() {
		return valor2;
	}

	public void setValor2(String valor2) {
		this.valor2 = valor2;
	}

	public String getGrupo() {
		return grupo;
	}

	public void setGrupo(String grupo) {
		this.grupo = grupo;
	}

	public String getEstado() {
		return estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}
}
