package com.japl.ea.query.privates.utils;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Random;

import org.pyt.common.common.ADto;
import org.pyt.common.exceptions.ReflectionException;

import com.pyt.query.interfaces.IAdvanceQuerySvc.triggerAction;
import com.pyt.query.interfaces.IAdvanceQuerySvc.triggerOption;

public class StatementQuerysUtil {
	public final <T extends ADto> String fieldToWhere(T obj, boolean valuesInsert) throws ReflectionException {
		List<String> names = obj.getNameFields();
		String where = "";
		for (String name : names) {
			var value = obj.get(name);
			if (value != null && !valuesInsert) {
				if (where.length() > 0)
					where += " AND ";
				where += name + "=" + valueFormat(value);
			} else if (valuesInsert) {
				if (where.length() > 0)
					where += ", ";
				where += value != null ? valueFormat(value) : "NULL";
			}
		}
		return where;
	}
	
	public final <T extends Object> String valueFormat(T value) {
		if(value == null)return null;
		if(value.getClass() == String.class) {
			return "'" + value.toString() + "'";
		}
		if(value.getClass() == Date.class) {
			var sdf = new SimpleDateFormat("yyyy-MM-dd");
			return "'" + sdf.format(value) + "'";
		}
		if(value.getClass() == Timestamp.class) {
			var dt = new Date(((Timestamp)value).getTime());
			var sdf = new SimpleDateFormat("yyyy-MM-dd");
			return "'" + sdf.format(dt) + "'";
		}
		return value.toString();
	}
	
	public final <T extends ADto> String fieldToSelect(T obj) throws ReflectionException {
		List<String> names = obj.getNameFields();
		String fields = Arrays.toString(names.toArray(new String[names.size()]));
		fields = fields.replace("[", "");
		fields = fields.replace("]", "");
		return fields;
	}
	
	public String typeDataDB(String typeJava) {
		if (typeJava.toLowerCase().contains("long") || typeJava.toLowerCase().contains("int")
				|| typeJava.toLowerCase().contains("boolean") || typeJava.toLowerCase().contains("big")) {
			return "NUMBER";
		}
		if (typeJava.toLowerCase().contains("string") || typeJava.toLowerCase().contains("char")) {
			return "VARCHAR2(100)";
		}
		if (typeJava.toLowerCase().contains("date")) {
			return "DATETIME";
		}
		return "VARCHAR2(30)";
	}
	
	public <T extends ADto> String getTableName(T obj) {
		return "MEM_" + obj.getClass().getSimpleName().replace("DTO", "");
	}
	/**
	 * Se encarga de generar el consecutivo
	 * 
	 * @param dto
	 * @return
	 */
	public final <T extends ADto> String genConsecutivo(Class<T> dto, Integer size) {
		String cadena = "AaBbCcDdEeFfGgHhIiJjKkLlMmNnOoPpQqRrSsTtUuVvWwXxYyZz1234567890";
		Integer max = 15;
		String name = dto.getSimpleName();
		Integer lenSize = size.toString().length();
		name = name.replace("DTO", "");
		Integer length = name.length();
		if (length > max) {
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
		for (int i = 0; i < max - length - lenSize; i++) {
			Double valor = aleatorio.nextDouble() * (cadena.length() - 1 + 0);
			sb.append(cadena.charAt(valor.intValue()));
		}
		sb.append(size);
		return sb.toString();
	}
	
	public <T extends ADto> String getNameTriggerPOJO(Class<T> obj,triggerOption to,triggerAction... tas) {
		var name = "";
		name += to.toString().subSequence(0, 1);
		for(triggerAction ta : tas) {
			name += ta.toString().substring(0,1);
		}
		name += obj.getSimpleName();
		return name;
	}
}
