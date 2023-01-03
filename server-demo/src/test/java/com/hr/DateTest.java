package com.hr;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DateTest {

	public static void main(String[] args) {

		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		LocalDateTime localDateTime = LocalDateTime.parse("2018-07-01 20:04:46", formatter);

		LocalDate localDate = localDateTime.toLocalDate();
		localDate = localDate.plusDays(-1);

		System.out.println(localDate.getMonthValue());

	}

}
