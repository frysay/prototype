package com.prototype.controller;

import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.prototype.body.request.ValidatedRequest;
import com.prototype.body.response.ExceptionResponse;
import com.prototype.body.response.ExceptionResponse.ExceptionCode;
import com.prototype.body.response.SimpleResponse;
import com.prototype.body.validator.HMACValidator;
import com.prototype.log.LogContext;
import com.prototype.service.RequestService;
import com.prototype.utils.ExceptionUtil;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/validated")
@Slf4j
public class ValidatedRequestController {

	private RequestService service;

	public ValidatedRequestController(RequestService service) {
		this.service = service;
	}

	@RequestMapping(value = "/save/{user_id}", method = RequestMethod.POST)
	@ResponseStatus(HttpStatus.CREATED)
	@HMACValidator
	public SimpleResponse validatedRequest(@PathVariable("user_id") String userId, 
			@RequestBody @Valid ValidatedRequest request) {

		LogContext.setupLogContext("validated", userId);

		service.processRequest(request, userId);

		LogContext.clearAll();

		return new SimpleResponse();
	}

	@ResponseStatus(value=HttpStatus.BAD_REQUEST)
	@ExceptionHandler({MethodArgumentNotValidException.class, HttpMessageNotReadableException.class, RuntimeException.class})
	public @ResponseBody ExceptionResponse handleValidationException(Exception e) {
		log.error("{}Invalid request error: {}", ExceptionUtil.getOriginalClassAndLogLine(e), e.getMessage(), e);
		LogContext.clearAll();
		return new ExceptionResponse(ExceptionCode.VALIDATED_EXTERNAL_EXCEPTION);
	}

	@ResponseStatus(value=HttpStatus.INTERNAL_SERVER_ERROR)
	@ExceptionHandler(Exception.class)
	public @ResponseBody ExceptionResponse handleException(Exception e) {
		log.error("{}Generic error: {}", ExceptionUtil.getOriginalClassAndLogLine(e), e.getMessage(), e);
		LogContext.clearAll();
		return new ExceptionResponse(ExceptionCode.VALIDATED_INTERNAL_EXCEPTION);
	}
}
