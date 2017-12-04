package com.prototype.body.validator;

import java.lang.reflect.Parameter;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import com.prototype.body.request.ValidatedRequest;
import com.prototype.utils.HMACUtil;

import lombok.extern.slf4j.Slf4j;

@Aspect
@Component
@Slf4j
public class HMACValidatorAspect {

	@Before(value = "execution(* com.prototype..*(..)) && @annotation(validator)")
	public void validateHMAC(JoinPoint joinPoint, HMACValidator validator) throws Exception {
		MethodSignature signature = (MethodSignature) joinPoint.getSignature();
		Parameter[] params = signature.getMethod().getParameters();
		ValidatedRequest request = null;
		String userId = null;
		for(int i = 0; i < params.length; i++) {
			if(params[i].isAnnotationPresent(RequestBody.class)) {
				if(joinPoint.getArgs()[i] instanceof ValidatedRequest) {
					request = (ValidatedRequest) joinPoint.getArgs()[i];
				}
			}

			if(params[i].isAnnotationPresent(PathVariable.class)) {
				userId = (String) joinPoint.getArgs()[i];
			}
		}
		validate(request.getEncodedMessage(), userId + request.getPersonalInfo() + request.isAlreadyValidatedByFE());
	}

	private void validate(String messageEncoded, String messageToEncode) throws Exception {
		boolean isValid = HMACUtil.validate(messageEncoded, messageToEncode);
		if(!isValid) {
			log.error("The request is not valid: {} - {}", messageEncoded, messageToEncode);
			throw new RuntimeException("The request is not valid");
		}
	}
}
