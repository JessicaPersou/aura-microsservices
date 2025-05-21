package com.postech.auramsorder.config.jackson;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;

class JacksonConfigTest {

    @Test
    void objectMapper_ShouldHaveExpectedConfiguration() {
        // Arrange
        JacksonConfig jacksonConfig = new JacksonConfig();

        // Act
        ObjectMapper objectMapper = jacksonConfig.objectMapper();

        // Assert
        assertFalse(objectMapper.isEnabled(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS),
                "SerializationFeature.WRITE_DATES_AS_TIMESTAMPS should be disabled");
    }
}