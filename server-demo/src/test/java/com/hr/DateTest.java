package com.hr;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DateTest {
	
	public static void main(String[] args) {
		

		DateTimeFormatter formatter =DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

		LocalDateTime date= LocalDateTime.parse("2018-07-12 20:04:46",formatter);
		
		System.out.println(date.getHour());
		
	}

}
