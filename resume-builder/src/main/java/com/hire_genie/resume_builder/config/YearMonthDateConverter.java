package com.hire_genie.resume_builder.config;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import java.sql.Date;
import java.time.YearMonth;

@Converter(autoApply = true)
public class YearMonthDateConverter implements AttributeConverter<YearMonth, Date> {

    @Override
    public Date convertToDatabaseColumn(YearMonth attribute) {
        if (attribute == null) {
            return null;
        }
        // Converts YearMonth to the 1st day of that month for SQL storage
        return Date.valueOf(attribute.atDay(1));
    }

    @Override
    public YearMonth convertToEntityAttribute(Date dbData) {
        if (dbData == null) {
            return null;
        }
        // Converts SQL Date back to YearMonth
        return YearMonth.from(dbData.toLocalDate());
    }
}