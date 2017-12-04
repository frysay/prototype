package com.prototype.body.response;

import lombok.Data;

@Data
public class ExceptionResponse {

	private final int errorCode;

	private final String errorMessage;

	public ExceptionResponse(ExceptionCode exceptionCode) {
		this.errorCode = exceptionCode.code;
		this.errorMessage = exceptionCode.message;
	}

	public enum ExceptionCode {
		BASIC_EXTERNAL_EXCEPTION(1,"external_exception"),
		BASIC_INTERNAL_EXCEPTION(2,"internal_exception"),
		VALIDATED_EXTERNAL_EXCEPTION(3,"external_exception"),
		VALIDATED_INTERNAL_EXCEPTION(4,"internal_exception");

		private int code;
		private String message;

		ExceptionCode(int code, String message) {
			this.code = code;
			this.message = message;
		}

		public int code() {
			return code;
		}
		
		public String message() {
			return message;
		}
	}
}
