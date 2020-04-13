package org.pyt.common.common;

import java.time.LocalDate;
import java.time.Period;
import java.time.ZoneId;
import java.util.Date;

public class DateUtils {
		
	private DateUtils() {}
	
	public static LocalDate add(Date date,String period) {
		var fecha = to(date);
		if(!period.contains("P")) {
			period = "P"+period;
		}
		var periodo = Period.parse(period);
		return fecha.plus(periodo);
	}
	
	public static LocalDate add(LocalDate date,String period) {
		if(!period.contains("P")) {
			period = "P"+period;
		}
		var periodo = Period.parse(period);
		return date.plus(periodo);
	}
	
	public static LocalDate to(Date date) {
		return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
	}
	
	public static Boolean less(Date date1, Date date2) {
		return date1.compareTo(date2) < 0;
	}
	
	public static Boolean greather(Date date1, Date date2) {
		return date1.compareTo(date2) > 0;
	}
	
	public static Boolean lessThat(Date date1, Date date2) {
		return date1.compareTo(date2) <= 0;
	}
	
	public static Boolean lessThat(Date date1, LocalDate date2) {
		return to(date1).compareTo(date2) <= 0;
	}
	
	public static Boolean lessThat(LocalDate date1, LocalDate date2) {
		return date1.compareTo(date2) <= 0;
	}
	
	public static Boolean greatherThat(Date date1, Date date2) {
		return date1.compareTo(date2) >= 0;
	}
	
	public static Boolean equals(Date date1, Date date2) {
		return date1.compareTo(date2) == 0;
	}
}
