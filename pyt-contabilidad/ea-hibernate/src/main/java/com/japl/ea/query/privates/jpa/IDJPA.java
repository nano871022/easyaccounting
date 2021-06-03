package com.japl.ea.query.privates.jpa;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.MappedSuperclass;

import co.com.japl.ea.common.reflection.ReflectionDto;

@MappedSuperclass
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
@Embeddable
public class IDJPA extends ReflectionDto implements Serializable {
	private static final long serialVersionUID = 1L;
	@Column(name = "scode")
	public String codigo;

	public String getCodigo() {
		return codigo;
	}

	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}
}
