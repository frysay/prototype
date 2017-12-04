package com.prototype.body.request;

import javax.validation.constraints.NotNull;

import lombok.Data;

@Data
public class BasicRequest {
	
	@NotNull
	private String personalInfo;
	
	private boolean alreadyValidatedByFE;
}
