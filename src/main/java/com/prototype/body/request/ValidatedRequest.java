package com.prototype.body.request;

import javax.validation.constraints.NotNull;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
public class ValidatedRequest extends BasicRequest {
	
	@NotNull
	private String encodedMessage;
}
