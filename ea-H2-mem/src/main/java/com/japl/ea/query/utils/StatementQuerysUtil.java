package com.japl.ea.query.utils;

import java.util.Arrays;
import java.util.List;

import org.pyt.common.common.ADto;
import org.pyt.common.exceptions.ReflectionException;

public class StatementQuerysUtil {
	public final <T extends ADto> String fieldToWhere(T obj, boolean valuesInsert) throws ReflectionException {
		List<String> names = obj.getNameFields();
		String where = "";
		for (String name : names) {
			var value = obj.get(name);
			if (value != null && !valuesInsert) {
				if (where.length() > 0)
					where += ", ";
				where += name + "=" + (value.getClass() == String.class ? "'" + value.toString() + "'" : value);
			} else if (valuesInsert) {
				if (where.length() > 0)
					where += ", ";
				where += value != null
						? value.getClass() == String.class ? "'" + value.toString() + "'" : value.toString()
						: "NULL";
			}
		}
		return where;
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

}
