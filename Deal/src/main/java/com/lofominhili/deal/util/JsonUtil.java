package com.lofominhili.deal.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.lofominhili.deal.util.exception.ParseException;
import lombok.experimental.UtilityClass;

@UtilityClass
public class JsonUtil {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper().registerModule(new JavaTimeModule());

    public static String toJson(Object object) {
        try {
            return OBJECT_MAPPER.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            throw ParseException.Code.PARSING_ERROR.get(e, "Ошибка сериализации объекта в json");
        }
    }

    public <T> T toObject(String json, Class<T> targetClass) {
        try {
            return OBJECT_MAPPER.readValue(json, targetClass);
        } catch (JsonProcessingException e) {
            throw ParseException.Code.PARSING_ERROR.get(e, "Ошибка десериализации JSON в объект: " + targetClass.getSimpleName());
        }
    }
}
