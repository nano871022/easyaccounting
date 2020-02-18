package org.pyt.common.constants;

import java.util.HashMap;
import java.util.Map;

public class ParametroInventarioConstants {
	public final static String GRUPO_TIPO_MOVIMIENTO = "Tipo Movimiento";
	public final static String GRUPO_DESC_TIPO_MOVIMIENTO = "TipoMovimiento";
	public final static String GRUPO_TIPO_KARDEX = "TipoKardex";
	public final static String GRUPO_DESC_TIPO_KARDEX = "Tipo Kardex";
	public final static String DESC_TIPO_MOV_ENTRADA = "entrada";
	public final static Map<String,Object> mapa_grupo ;
	static {
		mapa_grupo = new HashMap<String,Object>();
		mapa_grupo.put(GRUPO_TIPO_MOVIMIENTO,GRUPO_DESC_TIPO_MOVIMIENTO);
		mapa_grupo.put(GRUPO_TIPO_KARDEX,GRUPO_DESC_TIPO_KARDEX);
	}
}
