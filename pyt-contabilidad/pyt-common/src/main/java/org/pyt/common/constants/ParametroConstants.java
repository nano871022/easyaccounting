package org.pyt.common.constants;

import java.util.HashMap;
import java.util.Map;

public class ParametroConstants {
	public final static String GRUPO_MONEDA = "Moneda";
	public final static String GRUPO_TIPO_PAGO = "TipoPago";
	public final static String GRUPO_TIPO_DOCUMENTO = "TipoDocumento";
	public final static String GRUPO_TIPOS_DOCUMENTO_PERSONA = "TiposDocumentoPersona";
	public final static String GRUPO_ESTADO_EMPLEADO= "EstadoEmpleado";
	public final static Map<String,Object> mapa_grupo ;
	static {
		mapa_grupo = new HashMap<String,Object>();
		mapa_grupo.put("Moneda",GRUPO_MONEDA);
		mapa_grupo.put("Tipo Pago",GRUPO_TIPO_PAGO);
		mapa_grupo.put("TIpo Documento",GRUPO_TIPO_DOCUMENTO);
		mapa_grupo.put("Estado Empleado",GRUPO_ESTADO_EMPLEADO);
	}
	public final static String DESC_ESTADO_PARAMETRO_ACTIVO = "Activo";
	public final static String DESC_ESTADO_PARAMETRO_INACTIVO = "Inactivo";
	public final static String COD_ESTADO_PARAMETRO_ACTIVO = "A";
	public final static String COD_ESTADO_PARAMETRO_INACTIVO = "I";
	public final static Map<String,Object> mapa_estados_parametros;
	static {
		mapa_estados_parametros = new HashMap<String,Object>();
		mapa_estados_parametros.put(DESC_ESTADO_PARAMETRO_ACTIVO, COD_ESTADO_PARAMETRO_ACTIVO);
		mapa_estados_parametros.put(DESC_ESTADO_PARAMETRO_INACTIVO, COD_ESTADO_PARAMETRO_INACTIVO);
	}
	public final static String GRUPO_TIPO_BANCO          = "GRUPO_TIPO_BANCO";
	public final static String GRUPO_TIPO_CUENTA         = "GRUPO_TIPO_CUENTA";
	public final static String GRUPO_TIPO_CUENTA_CONTABLE= "GRUPO_TIPO_CUENTA_CONTABLE";
	public final static String GRUPO_TTIPO_DOCUMENTO     = "GRUPO_TIPO_DOCUMENTO";
	public final static String GRUPO_ESTADO_CONCEPTO     = "GRUPO_ESTADO_CONCEPTO";
	public final static String GRUPO_ESTADO_CENTRO_COSTO = "GRUPO_ESTADO_CENTRO_COSTO";
	public final static String GRUPO_ESTADO_BANCO        = "GRUPO_ESTADO_BANCO";
	public final static Map<String,Object> MAPA_GRUPOS = new HashMap<String,Object>();
	static {
		MAPA_GRUPOS.put(GRUPO_MONEDA, GRUPO_MONEDA);
		MAPA_GRUPOS.put(GRUPO_TIPO_BANCO, GRUPO_TIPO_BANCO);
		MAPA_GRUPOS.put(GRUPO_TIPO_PAGO, GRUPO_TIPO_PAGO);
		MAPA_GRUPOS.put(GRUPO_TIPO_CUENTA, GRUPO_TIPO_CUENTA);
		MAPA_GRUPOS.put(GRUPO_TTIPO_DOCUMENTO, GRUPO_TIPO_DOCUMENTO);
		MAPA_GRUPOS.put(GRUPO_ESTADO_CONCEPTO, GRUPO_ESTADO_CONCEPTO);
		MAPA_GRUPOS.put(GRUPO_ESTADO_CENTRO_COSTO, GRUPO_ESTADO_CENTRO_COSTO);
		MAPA_GRUPOS.put(GRUPO_ESTADO_BANCO, GRUPO_ESTADO_BANCO);
		MAPA_GRUPOS.put(GRUPO_ESTADO_EMPLEADO, GRUPO_ESTADO_EMPLEADO);
		MAPA_GRUPOS.put(GRUPO_TIPOS_DOCUMENTO_PERSONA, GRUPO_TIPOS_DOCUMENTO_PERSONA);
		MAPA_GRUPOS.put(GRUPO_TIPO_CUENTA_CONTABLE, GRUPO_TIPO_CUENTA_CONTABLE);
	}
}
