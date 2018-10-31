package com.ef.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum DurationEnum {

	DAILY("daily"),
	HOURLY("hourly");
	
	private String description;
	
}
