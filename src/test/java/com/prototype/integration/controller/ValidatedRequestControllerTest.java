package com.prototype.integration.controller;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;

import com.prototype.body.request.ValidatedRequest;
import com.prototype.controller.ValidatedRequestController;
import com.prototype.data.BasicObject;
import com.prototype.data.dao.BasicObjectDao;
import com.prototype.integration.utils.IntegrationTestConfig;
import com.prototype.utils.HMACUtil;
import com.prototype.utils.MapperUtil;

@ActiveProfiles(profiles = {"mock"})
public class ValidatedRequestControllerTest extends IntegrationTestConfig {

	@Autowired
	ValidatedRequestController controller;

	@SpyBean
	BasicObjectDao dao;

	@Test
	public void basicRequestTest_properRequestAlreadyValidated_success() throws Exception {
		String userId = "user1";
		ValidatedRequest request = mockedValidatedRequest(userId, "personalInfo", true);
		String json = MapperUtil.toJson(request);

		ArgumentCaptor<BasicObject> basicObjectCaptor = ArgumentCaptor.forClass(BasicObject.class);

		mockMvc.perform(post("/validated/save/" + userId).contentType(MediaType.APPLICATION_JSON).content(json))
		.andExpect(status().isCreated())
		.andExpect(jsonPath("successfulMessage").value("success"));

		verify(dao).wasAlreadySaved(any(BasicObject.class));
		verify(dao).save(basicObjectCaptor.capture());

		BasicObject basicObject = (BasicObject) basicObjectCaptor.getValue();

		assertEquals("personalInfo", basicObject.getPersonalInfo());
		assertEquals(true, basicObject.isValid());
		assertNotNull(basicObject.getId());
	}

	@Test
	public void basicRequestTest_properRequestNotValidatedYet_success() throws Exception {
		String userId = "user1";
		ValidatedRequest request = mockedValidatedRequest(userId, "personalInfo", false);
		String json = MapperUtil.toJson(request);

		ArgumentCaptor<BasicObject> basicObjectCaptor = ArgumentCaptor.forClass(BasicObject.class);

		mockMvc.perform(post("/validated/save/" + userId).contentType(MediaType.APPLICATION_JSON).content(json))
		.andExpect(status().isCreated())
		.andExpect(jsonPath("successfulMessage").value("success"));

		verify(dao).wasAlreadySaved(any(BasicObject.class));
		verify(dao).save(basicObjectCaptor.capture());

		BasicObject basicObject = (BasicObject) basicObjectCaptor.getValue();

		assertEquals("personalInfo", basicObject.getPersonalInfo());
		assertEquals(false, basicObject.isValid());
		assertNotNull(basicObject.getId());
	}

	@Test
	public void basicRequestTest_invalidJsonRequest_400() throws Exception {
		String userId = "user1";
		String json = "not a valid json format";

		mockMvc.perform(post("/validated/save/" + userId).contentType(MediaType.APPLICATION_JSON).content(json))
		.andExpect(status().isBadRequest())
		.andExpect(jsonPath("errorCode").value(3))
		.andExpect(jsonPath("errorMessage").value("external_exception"));

		verifyZeroInteractions(dao);
	}

	@Test
	public void basicRequestTest_invalidInfo_400() throws Exception {
		String userId = "user1";
		ValidatedRequest request = mockedValidatedRequest(userId, "invalidInfo", false);
		String json = MapperUtil.toJson(request);

		mockMvc.perform(post("/validated/save/" + userId).contentType(MediaType.APPLICATION_JSON).content(json))
		.andExpect(status().isBadRequest())
		.andExpect(jsonPath("errorCode").value(3))
		.andExpect(jsonPath("errorMessage").value("external_exception"));

		verifyZeroInteractions(dao);
	}

	@Test
	public void basicRequestTest_forgedRequest_400AsExpected() throws Exception {
		String userId = "user1";
		ValidatedRequest forgedRequest = mockedValidatedRequest(userId, "invalidInfo", true);
		String json = MapperUtil.toJson(forgedRequest);

		mockMvc.perform(post("/validated/save/" + userId).contentType(MediaType.APPLICATION_JSON).content(json))
		.andExpect(status().isBadRequest())
		.andExpect(jsonPath("errorCode").value(3))
		.andExpect(jsonPath("errorMessage").value("external_exception"));

		verifyZeroInteractions(dao);
	}

	private ValidatedRequest mockedValidatedRequest(String userId, String personalInfo, boolean alreadyValidatedByFE) throws Exception {
		ValidatedRequest request = new ValidatedRequest();
		request.setPersonalInfo(personalInfo);
		request.setAlreadyValidatedByFE(alreadyValidatedByFE);
		String encoded = personalInfo.contains("invalidInfo") ? HMACUtil.encode(userId + personalInfo + false) : HMACUtil.encode(userId + personalInfo + alreadyValidatedByFE);
		request.setEncodedMessage(encoded);
		return request;
	}
}
