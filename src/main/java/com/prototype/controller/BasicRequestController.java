package com.prototype.controller;

import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.prototype.body.request.BasicRequest;
import com.prototype.body.response.SimpleResponse;
import com.prototype.log.LogContext;
import com.prototype.service.RequestService;

@RestController
@RequestMapping("/basic")
public class BasicRequestController {

	private RequestService service;

	public BasicRequestController(RequestService service) {
		this.service = service;
	}

	@RequestMapping(value = "/save/{user_id}", method = RequestMethod.POST)
	@ResponseStatus(HttpStatus.CREATED)
	public SimpleResponse basicRequest(@PathVariable("user_id") String userId, 
			@RequestBody @Valid BasicRequest request) {

		LogContext.setupLogContext("basic", userId);

		service.processRequest(request, userId);

		LogContext.clearAll();
		
		return new SimpleResponse();
	}
}
