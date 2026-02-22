package com.harshil_infotech.hire_genie.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.deser.YearMonthDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.YearMonthSerializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.YearMonth;
import java.time.format.DateTimeFormatter;

@Configuration
public class JacksonDateConfig {

    private static final DateTimeFormatter YEAR_MONTH_FORMATTER = DateTimeFormatter.ofPattern("MM-yyyy");

    @Bean
    public ObjectMapper objectMapper() {
        SimpleModule yearMonthModule = new SimpleModule();

        yearMonthModule.addSerializer(YearMonth.class, new YearMonthSerializer(YEAR_MONTH_FORMATTER));
        yearMonthModule.addDeserializer(YearMonth.class, new YearMonthDeserializer(YEAR_MONTH_FORMATTER));

        return new ObjectMapper()
                .registerModule(new JavaTimeModule())
                .registerModule(yearMonthModule)
                .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    }

}
