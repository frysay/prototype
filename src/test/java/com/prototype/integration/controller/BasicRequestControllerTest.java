package com.prototype.integration.controller;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.times;
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

import com.prototype.body.request.BasicRequest;
import com.prototype.controller.BasicRequestController;
import com.prototype.data.BasicObject;
import com.prototype.data.dao.BasicObjectDao;
import com.prototype.integration.utils.IntegrationTestConfig;
import com.prototype.utils.MapperUtil;

@ActiveProfiles(profiles = {"mock"})
public class BasicRequestControllerTest extends IntegrationTestConfig {

	@Autowired
	BasicRequestController controller;

	@SpyBean
	BasicObjectDao dao;	

	@Test
	public void basicRequestTest_properRequestAlreadyValidated_success() throws Exception {
		String userId = "user1";
		BasicRequest request = mockedBasicRequest("personalInfo", true);
		String json = MapperUtil.toJson(request);

		ArgumentCaptor<BasicObject> basicObjectCaptor = ArgumentCaptor.forClass(BasicObject.class);

		mockMvc.perform(post("/basic/save/" + userId).contentType(MediaType.APPLICATION_JSON).content(json))
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
		BasicRequest request = mockedBasicRequest("personalInfo", false);
		String json = MapperUtil.toJson(request);

		ArgumentCaptor<BasicObject> basicObjectCaptor = ArgumentCaptor.forClass(BasicObject.class);

		mockMvc.perform(post("/basic/save/" + userId).contentType(MediaType.APPLICATION_JSON).content(json))
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
	public void basicRequestTest_multipleRequest_successSingleStore() throws Exception {
		String userId = "user1";
		BasicRequest request = mockedBasicRequest("personalInfo", false);
		String json = MapperUtil.toJson(request);

		ArgumentCaptor<BasicObject> basicObjectCaptor = ArgumentCaptor.forClass(BasicObject.class);

		mockMvc.perform(post("/basic/save/" + userId).contentType(MediaType.APPLICATION_JSON).content(json))
		.andExpect(status().isCreated())
		.andExpect(jsonPath("successfulMessage").value("success"));
		
		mockMvc.perform(post("/basic/save/" + userId).contentType(MediaType.APPLICATION_JSON).content(json))
		.andExpect(status().isCreated())
		.andExpect(jsonPath("successfulMessage").value("success"));

		verify(dao, times(2)).wasAlreadySaved(any(BasicObject.class));
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

		mockMvc.perform(post("/basic/save/" + userId).contentType(MediaType.APPLICATION_JSON).content(json))
		.andExpect(status().isBadRequest())
		.andExpect(jsonPath("errorCode").value(1))
		.andExpect(jsonPath("errorMessage").value("external_exception"));

		verifyZeroInteractions(dao);
	}

	@Test
	public void basicRequestTest_invalidInfo_400() throws Exception {
		String userId = "user1";
		BasicRequest request = mockedBasicRequest("invalidInfo", false);
		String json = MapperUtil.toJson(request);

		mockMvc.perform(post("/basic/save/" + userId).contentType(MediaType.APPLICATION_JSON).content(json))
		.andExpect(status().isBadRequest())
		.andExpect(jsonPath("errorCode").value(1))
		.andExpect(jsonPath("errorMessage").value("external_exception"));

		verifyZeroInteractions(dao);
	}

	@Test
	public void basicRequestTest_forgedRequest_201InsteadOf400() throws Exception {
		String userId = "user1";
		BasicRequest forgedRequest = mockedBasicRequest("invalidInfo", true);
		String json = MapperUtil.toJson(forgedRequest);

		ArgumentCaptor<BasicObject> basicObjectCaptor = ArgumentCaptor.forClass(BasicObject.class);
		
		mockMvc.perform(post("/basic/save/" + userId).contentType(MediaType.APPLICATION_JSON).content(json))
		.andExpect(status().isCreated())
		.andExpect(jsonPath("successfulMessage").value("success"));

		verify(dao).wasAlreadySaved(any(BasicObject.class));
		verify(dao).save(basicObjectCaptor.capture());

		BasicObject basicObject = (BasicObject) basicObjectCaptor.getValue();

		assertEquals("invalidInfo", basicObject.getPersonalInfo());
		assertEquals(true, basicObject.isValid());
		assertNotNull(basicObject.getId());
	}

	private BasicRequest mockedBasicRequest(String personalInfo, boolean alreadyValidatedByFE) {
		BasicRequest request = new BasicRequest();
		request.setPersonalInfo(personalInfo);
		request.setAlreadyValidatedByFE(alreadyValidatedByFE);
		return request;
	}
}
