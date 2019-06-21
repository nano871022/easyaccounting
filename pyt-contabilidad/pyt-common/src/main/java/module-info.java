
module ea_common {
	exports org.pyt.common.binario;
	exports org.pyt.common.common;
	exports org.pyt.common.controller;
	exports org.pyt.common.interfaces;
	exports org.pyt.common.reflection;
	exports org.pyt.common.abstracts;
	exports org.pyt.common.validates;
	exports org.pyt.common.properties;

	requires ea_constantes;
	requires ea_exceptions;
	requires javafx.controls;
	requires org.kordamp.ikonli.javafx;
	requires java.naming;
	requires ea_anotaciones;
	requires commons.lang3;
	requires javafx.fxml;
	requires arquitectura.librerias;
	requires arquitectura.anotaciones;
}
