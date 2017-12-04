package com.prototype.junit.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import com.prototype.body.request.BasicRequest;
import com.prototype.data.BasicObject;
import com.prototype.data.dao.BasicObjectDao;
import com.prototype.service.RequestService;

@RunWith(SpringRunner.class)
public class RequestServiceTest {

	private RequestService service;

	@MockBean
	BasicObjectDao dao;

	@Before
	public void init() {
		service = new RequestService(dao);
	}

	@Test
	public void processRequest_notValidate_save() {
		String userId = "user1";
		BasicRequest request = mockedBasicRequest("personalInfo", false);

		when(dao.wasAlreadySaved(any(BasicObject.class))).thenReturn(false);
		when(dao.save(any(BasicObject.class))).thenReturn(mockedBasicObject(userId));

		service.processRequest(request, userId);

		ArgumentCaptor<BasicObject> basicObjectCaptor = ArgumentCaptor.forClass(BasicObject.class);

		verify(dao).wasAlreadySaved(any(BasicObject.class));
		verify(dao).save(basicObjectCaptor.capture());

		BasicObject basicObject = (BasicObject) basicObjectCaptor.getValue();

		assertEquals("personalInfo", basicObject.getPersonalInfo());
		assertEquals(false, basicObject.isValid());
	}

	@Test
	public void processRequest_validated_notSaved() {
		String userId = "user1";
		BasicRequest request = mockedBasicRequest("personalInfo", true);

		when(dao.wasAlreadySaved(any(BasicObject.class))).thenReturn(true);
		when(dao.save(any(BasicObject.class))).thenReturn(mockedBasicObject(userId));

		service.processRequest(request, userId);

		verify(dao).wasAlreadySaved(any(BasicObject.class));
		verifyNoMoreInteractions(dao);
	}

	@Test(expected = RuntimeException.class)
	public void processRequest_invalidInfo_runtimeException() {
		String userId = "user1";
		BasicRequest request = mockedBasicRequest("invalidInfo", false);

		service.processRequest(request, userId);

		fail("It should not get here");
	}

	private BasicRequest mockedBasicRequest(String personalInfo, boolean alreadyValidatedByFE) {
		BasicRequest request = new BasicRequest();
		request.setPersonalInfo(personalInfo);
		request.setAlreadyValidatedByFE(alreadyValidatedByFE);
		return request;
	}

	private BasicObject mockedBasicObject(String userId) {
		BasicObject basicObject = new BasicObject();
		basicObject.setPersonalInfo("personalInfo");
		basicObject.setUserId(userId);
		basicObject.setValid(false);
		return basicObject;
	}

}
