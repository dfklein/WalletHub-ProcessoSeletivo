package com.ef;

import static com.ef.utils.ParserUtils.convertDurationStringToEnum;
import static com.ef.utils.ParserUtils.convertFileToLogList;
import static com.ef.utils.ParserUtils.convertStringToDate;
import static com.ef.utils.ParserUtils.convertToLocalDateTime;
import static com.ef.utils.ParserUtils.parsePropertyFromArgs;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.ef.dao.ParserDao;
import com.ef.enums.DurationEnum;
import com.ef.model.Block;
import com.ef.model.Request;
import com.ef.utils.ParserUtils;

public class Parser {

	public static void main(String[] args) {
		
		
		try {
			final String DATE_PATTERN = "yyyy-MM-dd.HH:mm:ss";
			
			String accesslogArg = parsePropertyFromArgs("accesslog", args);
			String startDateArg = parsePropertyFromArgs("startDate", args);
			String durationArg = parsePropertyFromArgs("duration", args);
			String thresholdArg = parsePropertyFromArgs("threshold", args);
			
			Path accesslog = accesslogArg != null ? Paths.get(accesslogArg, "access.log") : Paths.get(System.getProperty("user.dir"), "access.log");
			DurationEnum duration = convertDurationStringToEnum(durationArg);
			LocalDateTime startDate = convertStringToDate(startDateArg, DATE_PATTERN);
			Integer threshold = Integer.valueOf(thresholdArg);
			LocalDateTime endDate = DurationEnum.DAILY == duration ? startDate.plusDays(1).minusSeconds(1) : startDate.plusHours(1).minusSeconds(1);
			
			
			List<Request> listLog = convertFileToLogList(accesslog);
			
			ParserDao logDao = new ParserDao();
			logDao.persistLogs(listLog);
			
			// Filter logs generated in the desired interval.
			List<Request> logsToAnalyse = listLog.stream()
				.filter(log -> convertToLocalDateTime(log.getDate()).isAfter(startDate) && convertToLocalDateTime(log.getDate()).isBefore(endDate))
				.collect(Collectors.toList());
			
			
			List<Block> listBlockedIps = new ArrayList<Block>();
			
			Map<String, List<Request>> logsByIp = logsToAnalyse.stream().collect(Collectors.groupingBy(Request::getIp));
			
			logsByIp.forEach((ip, logs) -> {
				if(logs.size() > threshold) {
					String msg = ip + " has " + threshold + " or more requests between " + ParserUtils.convertDateToString(startDate, DATE_PATTERN) + " and " + ParserUtils.convertDateToString(endDate, DATE_PATTERN);
					listBlockedIps.add(new Block(ip, msg));
					System.out.println(msg);
				}
			});
			
			logDao.persistBlockedIps(listBlockedIps);
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
	}

}
