package com.maybank.platform.services.util;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class JsonUtil {
    private static final Logger LOGGER = LoggerFactory.getLogger(JsonUtil.class);
    private static final ObjectMapper MAPPER;

    public JsonUtil() {
    }

    public static <T> String toJson(T t) {
        try {
            String json = MAPPER.writeValueAsString(t);
            return json;
        } catch (Exception var2) {
            LOGGER.error("", var2);
            return null;
        }
    }

    public static <T> byte[] toJsonAsBytes(T t) {
        try {
            byte[] bytes = MAPPER.writeValueAsBytes(t);
            return bytes;
        } catch (Exception var2) {
            LOGGER.error("", var2);
            return null;
        }
    }

    public static <T> T parseJson(String json, Class<T> clz) {
        try {
            return MAPPER.readValue(json, clz);
        } catch (Exception var3) {
            LOGGER.error("无法解析json: " + json, var3);
            return null;
        }
    }

    public static <T> T parseJsonByType(String json, TypeReference<T> type) {
        try {
            return MAPPER.readValue(json, type);
        } catch (Exception var3) {
            LOGGER.error("json : " + json, var3);
            return null;
        }
    }

    public static <T> T parseJsonAsBytes(byte[] bytes, Class<T> clz) {
        try {
            return MAPPER.readValue(bytes, clz);
        } catch (Exception var3) {
            LOGGER.error("json : " + bytes, var3);
            return null;
        }
    }

    public static <T> List<T> parseListJson(String json, Class<T> clz) {
        try {
            JavaType type = MAPPER.getTypeFactory().constructCollectionType(ArrayList.class, clz);
            return (List)MAPPER.readValue(json, type);
        } catch (Exception var3) {
            LOGGER.error("json : " + json, var3);
            return null;
        }
    }

    public static <T> List<T> parseListJsonByType(String json, TypeReference<T> type) {
        try {
            return (List)MAPPER.readValue(json, type);
        } catch (Exception var3) {
            LOGGER.error("json : " + json, var3);
            return null;
        }
    }

    public static <T> List<T> parseListJsonAsBytes(byte[] bytes, Class<T> clz) {
        try {
            JavaType type = MAPPER.getTypeFactory().constructCollectionType(ArrayList.class, clz);
            return (List)MAPPER.readValue(bytes, type);
        } catch (Exception var3) {
            LOGGER.error("json : " + bytes, var3);
            return null;
        }
    }

    public static <T> List<T> parseListJsonAsBytesByType(byte[] bytes, TypeReference<T> type) {
        try {
            return (List)MAPPER.readValue(bytes, type);
        } catch (Exception var3) {
            LOGGER.error("json : " + bytes, var3);
            return null;
        }
    }

    static {
        MAPPER = (new ObjectMapper()).configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false).registerModule(new JavaTimeModule()).setSerializationInclusion(Include.NON_NULL).configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false).configure(DeserializationFeature.READ_UNKNOWN_ENUM_VALUES_AS_NULL, true);
    }
}
