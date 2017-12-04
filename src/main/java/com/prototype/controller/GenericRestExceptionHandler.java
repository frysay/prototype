package com.prototype.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.prototype.body.response.ExceptionResponse;
import com.prototype.body.response.ExceptionResponse.ExceptionCode;
import com.prototype.log.LogContext;
import com.prototype.utils.ExceptionUtil;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@ControllerAdvice
public class GenericRestExceptionHandler {

	@ResponseStatus(value=HttpStatus.BAD_REQUEST)
	@ExceptionHandler({MethodArgumentNotValidException.class, HttpMessageNotReadableException.class, RuntimeException.class})
	public @ResponseBody ExceptionResponse handleValidationException(Exception e) {
		log.error("{}Invalid request error: {}", ExceptionUtil.getOriginalClassAndLogLine(e), e.getMessage(), e);
		LogContext.clearAll();
		return new ExceptionResponse(ExceptionCode.BASIC_EXTERNAL_EXCEPTION);
	}

	@ResponseStatus(value=HttpStatus.INTERNAL_SERVER_ERROR)
	@ExceptionHandler(Exception.class)
	public @ResponseBody ExceptionResponse handleException(Exception e) {
		log.error("{}Generic error: {}", ExceptionUtil.getOriginalClassAndLogLine(e), e.getMessage(), e);
		LogContext.clearAll();
		return new ExceptionResponse(ExceptionCode.BASIC_INTERNAL_EXCEPTION);
	}
}
