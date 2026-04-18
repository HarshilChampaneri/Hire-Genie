package com.hire_genie.job_service.config;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.Nullable;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.SerializationException;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
public class Jackson2RedisSerializer implements RedisSerializer<Object> {

    private final ObjectMapper objectMapper;

    @Override
    public byte[] serialize(@Nullable Object value) throws SerializationException {
        if (value == null) return new byte[0];
        try {
            String typeStr = resolveCanonicalType(value);
            byte[] typeHeader = (typeStr + "\n").getBytes(StandardCharsets.UTF_8);
            byte[] valueBytes = objectMapper.writeValueAsBytes(value);

            byte[] result = new byte[typeHeader.length + valueBytes.length];
            System.arraycopy(typeHeader, 0, result, 0, typeHeader.length);
            System.arraycopy(valueBytes, 0, result, typeHeader.length, valueBytes.length);
            return result;
        } catch (Exception e) {
            throw new SerializationException("Could not serialize object: " + e.getMessage(), e);
        }
    }

    @Override
    @Nullable
    public Object deserialize(byte[] bytes) throws SerializationException {
        if (bytes == null || bytes.length == 0) return null;
        try {
            String content = new String(bytes, StandardCharsets.UTF_8);
            int newlineIdx = content.indexOf('\n');
            String typeStr = content.substring(0, newlineIdx);
            String json = content.substring(newlineIdx + 1);

            JavaType javaType = objectMapper.getTypeFactory().constructFromCanonical(typeStr);
            return objectMapper.readValue(json, javaType);
        } catch (Exception e) {
            throw new SerializationException("Could not deserialize bytes: " + e.getMessage(), e);
        }
    }

    private String resolveCanonicalType(Object value) {
        if (value instanceof List<?> list && !list.isEmpty()) {
            Object firstElement = list.getFirst();
            if (firstElement != null) {
                JavaType elementType = objectMapper.getTypeFactory()
                        .constructType(firstElement.getClass());
                JavaType listType = objectMapper.getTypeFactory()
                        .constructCollectionType(ArrayList.class, elementType.getRawClass());
                return listType.toCanonical();
            }
        }
        return value.getClass().getName();
    }
}