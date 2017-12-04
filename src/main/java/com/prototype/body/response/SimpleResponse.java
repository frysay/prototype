package com.prototype.body.response;

import lombok.Data;

@Data
public class SimpleResponse {
	
	private String successfulMessage;
	
	public SimpleResponse() {
		this.successfulMessage = "success";
	}
}
