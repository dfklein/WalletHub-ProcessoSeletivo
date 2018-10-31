package com.ef.utils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Stream;

import com.ef.enums.DurationEnum;
import com.ef.model.Request;

public class ParserUtils {

	private ParserUtils() {
		
	}
	
	public static String parsePropertyFromArgs(String property, String[] args) {
		String value = null;
		for(String arg : args) {
			if(arg.startsWith("--" + property)) {
				String[] splitArg = arg.split("=", 10);
				value = splitArg[1];
				break;
			}
		}
		return value;
	}
	
	public static DurationEnum convertDurationStringToEnum(String duration) {
		if (duration.equalsIgnoreCase("daily")) return DurationEnum.DAILY;
		if (duration.equalsIgnoreCase("hourly")) return DurationEnum.HOURLY;
		return null;
	}
	
	public static String convertDateToString(LocalDateTime date, String pattern) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
		return date.format(formatter);
	}

	public static LocalDateTime convertStringToDate(String startDateArg, String pattern) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
		return LocalDateTime.parse(startDateArg, formatter);
	}
	
	public static LocalDateTime convertToLocalDateTime(Date date) {
	    return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
	}
	
	public static Date convertToDate(LocalDateTime date) {
	    return Date.from(date.atZone(ZoneId.systemDefault()).toInstant());
	}

	public static List<Request> convertFileToLogList(Path accesslog) throws IOException {
		try (Stream<String> stream = Files.lines(accesslog)) {
			
			List<Request> listLogModel = new ArrayList<>();

			stream.forEach(line -> {
				String[] elems = line.split("\\|");
				LocalDateTime logDate = convertStringToDate(elems[0], "yyyy-MM-dd HH:mm:ss.SSS");
				
				String ip = elems[1];
				String request = elems[2];
				Integer status = Integer.valueOf(elems[3]);
				String userAgent = elems[4];
				
				Request logModel = new Request(logDate, ip, request, status, userAgent);
				listLogModel.add(logModel);
				
			});
			return listLogModel;
			
		}
	}

}
