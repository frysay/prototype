package com.prototype.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.prototype.body.request.BasicRequest;
import com.prototype.data.BasicObject;
import com.prototype.data.dao.BasicObjectDao;

@Service
public class RequestService {

	private BasicObjectDao basicObjectDao;

	@Autowired
	public RequestService(BasicObjectDao basicObjectDao) {
		this.basicObjectDao = basicObjectDao;
	}

	public void processRequest(BasicRequest request, String userId) {
		validateRequest(request);
		BasicObject basicObject = mapRequestToObject(request, userId);

		if(!basicObjectDao.wasAlreadySaved(basicObject)) {
			basicObjectDao.save(basicObject);
		}
	}

	private BasicObject mapRequestToObject(BasicRequest request, String userId) {
		BasicObject basicObject = new BasicObject();
		basicObject.setUserId(userId);
		basicObject.setPersonalInfo(request.getPersonalInfo());
		basicObject.setValid(request.isAlreadyValidatedByFE());

		return basicObject;
	}

	private void validateRequest(BasicRequest request) {
		if(request.isAlreadyValidatedByFE()) {
			return;
		}

		if(request.getPersonalInfo().contains("invalidInfo")) {
			throw new RuntimeException("The info provided are not valid: " + request.getPersonalInfo());
		}
	}
}
