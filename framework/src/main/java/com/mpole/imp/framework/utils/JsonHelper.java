package com.mpole.imp.framework.utils;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonHelper {
    public static String toJson(Object object) {
        try {
            return getINSTANCE().writeValueAsString(object);
        } catch (Exception e) {
            throw new RuntimeException("Json serialization error: " + e.getMessage(), e);
        }
    }


    public static <T> T fromJson(String json, Class<T> clazz) {
        try {
            return getINSTANCE().readValue(json, clazz);
        } catch (Exception e) {
            throw new RuntimeException("Json parsing error: " + e.getMessage(), e);
        }
    }

    public static ObjectMapper getINSTANCE() {
        return lazyHolderObjectMapper.INSTANCE;
    }


    private static class lazyHolderObjectMapper {
        private static final ObjectMapper INSTANCE = createConfiguredObjectMapper();

        private static ObjectMapper createConfiguredObjectMapper() {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false); // 예시: 알 수 없는 속성 무시
            objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL); // 예시: null 값 제외
            //objectMapper.registerModule(new JavaTimeModule()); // 예시: Java 8 날짜 및 시간 처리 모듈 등록
            return objectMapper;
        }


    }
}
