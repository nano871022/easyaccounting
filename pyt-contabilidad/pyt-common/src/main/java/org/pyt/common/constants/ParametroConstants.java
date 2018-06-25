package org.pyt.common.constants;

import java.util.HashMap;
import java.util.Map;

public class ParametroConstants {
	public final static String GRUPO_MONEDA = "Moneda";
	public final static String GRUPO_TIPO_PAGO = "TipoPago";
	public final static String GRUPO_TIPO_DOCUMENTO = "TipoDocumento";
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
}
