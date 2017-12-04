package com.prototype.integration;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;

import com.prototype.data.BasicObject;
import com.prototype.data.dao.BasicObjectDao;
import com.prototype.integration.utils.IntegrationTestConfig;

@ActiveProfiles(profiles = {"local"})
public class DbTest extends IntegrationTestConfig {
	
	@Autowired
	BasicObjectDao dao;
	
	@Test
	public void daoTest() {
		BasicObject basicObject1 = mockedBasicObject("user1");
		basicObject1 = dao.save(basicObject1);
		
		boolean result = dao.wasAlreadySaved(basicObject1);
		assertTrue(result);
		
		BasicObject basicObject2 = mockedBasicObject("user2");
		result = dao.wasAlreadySaved(basicObject2);
		assertFalse(result);
		
	}
	
	private BasicObject mockedBasicObject(String userId) {
		BasicObject basicObject = new BasicObject();
		basicObject.setPersonalInfo("personalInfo");
		basicObject.setUserId(userId);
		basicObject.setValid(false);
		return basicObject;
	}
}
