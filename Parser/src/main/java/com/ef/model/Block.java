package com.ef.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(callSuper=false, of = {"id"})
public class Block {

	private Long id;
	
	private String ip;
	
	private String comment;

	public Block(String ip, String comment) {
		super();
		this.ip = ip;
		this.comment = comment;
	}
	
}
