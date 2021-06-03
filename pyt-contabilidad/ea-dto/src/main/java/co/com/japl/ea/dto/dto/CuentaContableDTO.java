package co.com.japl.ea.dto.dto;

import org.pyt.common.annotation.generics.DefaultFieldToGeneric;
import org.pyt.common.annotation.proccess.ValueInObject;

import co.com.arquitectura.annotation.proccessor.DelClass;
import co.com.arquitectura.annotation.proccessor.UpdClass;
import co.com.japl.ea.common.abstracts.ADto;

@DelClass(nombre = "co.com.japl.ea.dto.dto.dels.CuentaContableDelDTO")
@UpdClass(nombre = "co.com.japl.ea.dto.dto.upds.CuentaContableUpdDTO")

public class CuentaContableDTO extends ADto {
	private static final long serialVersionUID = -8350913912942141963L;
	@DefaultFieldToGeneric(simpleNameClazzBean = "PopupGenBean", use = DefaultFieldToGeneric.Uses.COLUMN)
	@DefaultFieldToGeneric(simpleNameClazzBean = "PopupGenBean", use = DefaultFieldToGeneric.Uses.FILTER)
	private String codigoCuenta;
	@ValueInObject(field="nombre")
	private ParametroDTO tipoPlanContable;
	@ValueInObject(field="nombre")
	private ParametroDTO tipo;
	@DefaultFieldToGeneric(simpleNameClazzBean = "PopupGenBean", use = DefaultFieldToGeneric.Uses.FILTER)
	@DefaultFieldToGeneric(simpleNameClazzBean = "PopupGenBean", use = DefaultFieldToGeneric.Uses.COLUMN)
	private String nombre;
	@ValueInObject(field="nombre")
	private ParametroDTO naturaleza;
	@ValueInObject(field="nombre")
	private EmpresaDTO empresa;
	@ValueInObject(field="codigoCuenta")
	private CuentaContableDTO asociado;

	public ParametroDTO getTipoPlanContable() {
		return tipoPlanContable;
	}

	public void setTipoPlanContable(ParametroDTO tipoPlanContable) {
		this.tipoPlanContable = tipoPlanContable;
	}

	public ParametroDTO getTipo() {
		return tipo;
	}

	public void setTipo(ParametroDTO tipo) {
		this.tipo = tipo;
	}

	public ParametroDTO getNaturaleza() {
		return naturaleza;
	}

	public void setNaturaleza(ParametroDTO naturaleza) {
		this.naturaleza = naturaleza;
	}

	public EmpresaDTO getEmpresa() {
		return empresa;
	}

	public void setEmpresa(EmpresaDTO empresa) {
		this.empresa = empresa;
	}

	public String getCodigoCuenta() {
		return codigoCuenta;
	}

	public void setCodigoCuenta(String codigoCuenta) {
		this.codigoCuenta = codigoCuenta;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public CuentaContableDTO getAsociado() {
		return this.asociado;
	}

	public void setAsociado(CuentaContableDTO asociado) {
		this.asociado = asociado;
	}

}
