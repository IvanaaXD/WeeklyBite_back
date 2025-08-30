package com.backend.weeklybite.converter;

import com.backend.weeklybite.domain.Step;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.util.List;

@Converter
public class StepListConverter implements AttributeConverter<List<Step>, String> {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public String convertToDatabaseColumn(List<Step> steps) {
        try {
            return objectMapper.writeValueAsString(steps);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Step> convertToEntityAttribute(String dbData) {
        try {
            return objectMapper.readValue(dbData, new TypeReference<List<Step>>() {});
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}

