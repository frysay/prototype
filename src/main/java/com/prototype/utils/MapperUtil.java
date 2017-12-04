package com.prototype.utils;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class MapperUtil {

	private static ObjectMapper objectMapper;

	@Autowired
	public MapperUtil(ObjectMapper objectMapper) {
		MapperUtil.objectMapper = objectMapper;
	}

	public static String toJson(Object value) throws JsonProcessingException {
		return objectMapper.writeValueAsString(value);
	}

	public static <T> T fromJson(String value, Class<T> type) throws IOException {
		return objectMapper.readValue(value, type);
	}

	public static <T> List<T> convertListFromJson(String json, Class<T> type) throws IOException {
		return objectMapper.readValue(json, objectMapper.getTypeFactory().constructCollectionType(List.class, type));
	}
}
