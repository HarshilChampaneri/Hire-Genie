package com.hire_genie.resume_builder.config;

import com.fasterxml.jackson.core.json.JsonReadFeature;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.json.JsonMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableCaching
@RequiredArgsConstructor
public class RedisConfig {

    private final ObjectMapper objectMapper;

    private static final String REDIS_CERTIFICATE_KEY_1 = "allCertificates";
    private static final String REDIS_CERTIFICATE_KEY_2 = "certificates";
    private static final String REDIS_EDUCATION_KEY_1 = "allEducations";
    private static final String REDIS_EDUCATION_KEY_2 = "educations";
    private static final String REDIS_EXPERIENCE_KEY_1 = "allExperiences";
    private static final String REDIS_EXPERIENCE_KEY_2 = "experiences";
    private static final String REDIS_OTHER_KEY = "other";
    private static final String REDIS_PROFILE_KEY = "profile";
    private static final String REDIS_PROFILE_SUMMARY_KEY = "profileSummary";
    private static final String REDIS_PROJECT_KEY_1 = "allProjects";
    private static final String REDIS_PROJECT_KEY_2 = "projects";

    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory connectionFactory) {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(connectionFactory);

        template.setKeySerializer(new StringRedisSerializer());
        template.setHashKeySerializer(new StringRedisSerializer());

        Jackson2RedisSerializer jsonSerializer = new Jackson2RedisSerializer(objectMapper);

        template.setValueSerializer(jsonSerializer);
        template.setHashValueSerializer(jsonSerializer);

        template.afterPropertiesSet();
        return template;
    }

    @Bean
    public CacheManager cacheManager(RedisConnectionFactory connectionFactory) {

        Jackson2RedisSerializer jsonSerializer = new Jackson2RedisSerializer(objectMapper);

        RedisCacheConfiguration defaultConfig = RedisCacheConfiguration.defaultCacheConfig()
                .entryTtl(Duration.ofMinutes(10))
                .serializeKeysWith(
                        RedisSerializationContext.SerializationPair.fromSerializer(new StringRedisSerializer())
                )
                .serializeValuesWith(
                        RedisSerializationContext.SerializationPair.fromSerializer(jsonSerializer)
                )
                .disableCachingNullValues();

        Map<String, RedisCacheConfiguration> cacheConfigurations = new HashMap<>();
        cacheConfigurations.put(REDIS_CERTIFICATE_KEY_2, defaultConfig.entryTtl(Duration.ofMinutes(10)));
        cacheConfigurations.put(REDIS_CERTIFICATE_KEY_1, defaultConfig.entryTtl(Duration.ofMinutes(5)));
        cacheConfigurations.put(REDIS_EDUCATION_KEY_2, defaultConfig.entryTtl(Duration.ofMinutes(10)));
        cacheConfigurations.put(REDIS_EDUCATION_KEY_1, defaultConfig.entryTtl(Duration.ofMinutes(5)));
        cacheConfigurations.put(REDIS_EXPERIENCE_KEY_2, defaultConfig.entryTtl(Duration.ofMinutes(10)));
        cacheConfigurations.put(REDIS_EXPERIENCE_KEY_1, defaultConfig.entryTtl(Duration.ofMinutes(5)));
        cacheConfigurations.put(REDIS_OTHER_KEY, defaultConfig.entryTtl(Duration.ofMinutes(5)));
        cacheConfigurations.put(REDIS_PROFILE_KEY, defaultConfig.entryTtl(Duration.ofMinutes(10)));
        cacheConfigurations.put(REDIS_PROFILE_SUMMARY_KEY, defaultConfig.entryTtl(Duration.ofMinutes(5)));
        cacheConfigurations.put(REDIS_PROJECT_KEY_2, defaultConfig.entryTtl(Duration.ofMinutes(10)));
        cacheConfigurations.put(REDIS_PROJECT_KEY_1, defaultConfig.entryTtl(Duration.ofMinutes(5)));

        return RedisCacheManager.builder(connectionFactory)
                .cacheDefaults(defaultConfig)
                .withInitialCacheConfigurations(cacheConfigurations)
                .build();
    }

//    private ObjectMapper objectMapper() {
//
//        return JsonMapper.builder()
//                .enable(SerializationFeature.INDENT_OUTPUT)
//                .disable(SerializationFeature.FAIL_ON_EMPTY_BEANS)
//                .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
//                .disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
//                .enable(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT)
//                .configure(JsonReadFeature.ALLOW_JAVA_COMMENTS, true)
//                .configure(JsonReadFeature.ALLOW_UNQUOTED_FIELD_NAMES, true)
//                .configure(JsonReadFeature.ALLOW_SINGLE_QUOTES, true)
//                .propertyNamingStrategy(PropertyNamingStrategies.LOWER_CAMEL_CASE)
//                .build();
//    }
}