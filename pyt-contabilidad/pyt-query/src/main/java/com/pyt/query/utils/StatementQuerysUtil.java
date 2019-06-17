package com.pyt.query.utils;

import java.time.LocalDateTime;
import java.util.Random;

import org.pyt.common.abstracts.ADto;

public class StatementQuerysUtil {
	private final static String CONST_DTO = "DTO";
	private final static String CONST_ABC_CHAIN = "AaBbCcDdEeFfGgHhIiJjKkLlMmNnOoPpQqRrSsTtUuVvWwXxYyZz1234567890";
	private final static Integer CONST_MAX_LENGTH = 17;
	private final static String CONST_PREFIX_TABLE = "TBL_";
	private final static String CONST_EMPTY = "";

	
	public <T extends ADto> String getTableName(T obj) {
		return CONST_PREFIX_TABLE + obj.getClass().getSimpleName().replace(CONST_DTO, CONST_EMPTY);
	}

	/**
	 * Se encarga de generar el consecutivo
	 * 
	 * @param dto
	 * @return
	 */
	public final <T extends ADto> String genConsecutivo(Class<T> dto, Integer size) {
		String name = dto.getSimpleName();
		Integer lenSize = size.toString().length();
		name = name.replace(CONST_DTO, CONST_EMPTY);
		Integer length = name.length();
		if (length > CONST_MAX_LENGTH) {
			name = name.substring(0, 7);
			length = name.length();
		}
		StringBuilder sb = new StringBuilder();
		LocalDateTime fecha = LocalDateTime.now();
		sb.append(name);
		sb.append(fecha.getYear());
		sb.append(fecha.getMonthValue());
		sb.append(fecha.getDayOfMonth());
		sb.append(fecha.getHour());
		sb.append(fecha.getMinute());
		sb.append(fecha.getSecond());
		Random aleatorio = new Random();
		for (int i = 0; i < CONST_MAX_LENGTH - length - lenSize; i++) {
			Double valor = aleatorio.nextDouble() * (CONST_ABC_CHAIN.length() - 1 + 0);
			sb.append(CONST_ABC_CHAIN.charAt(valor.intValue()));
		}
		sb.append(size);
		return sb.toString();
	}

}
