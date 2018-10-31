package com.ef.model;

import java.time.LocalDateTime;
import java.util.Date;

import com.ef.utils.ParserUtils;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(callSuper=false, of = {"id"})
public class Request {

	private Long id;
	
	private Date date;
	
	private String ip;
	
	private String request;
	
	private Integer status;
	
	private String userAgent;

	public Request(LocalDateTime date, String ip, String request, Integer status, String userAgent) {
		super();
		this.date = ParserUtils.convertToDate(date);
		this.ip = ip;
		this.request = request;
		this.status = status;
		this.userAgent = userAgent;
	}
	
	
	
}
