package ru.chaykin.microservapp.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class MessageConverter {

    private final ObjectMapper objectMapper;

    public <T> T readMessage(String message, Class<T> requestType) {
        try {
            log.info("Десериализация сообщения [{}] {}", requestType.getSimpleName(), message);
            return objectMapper.readValue(message, requestType);
        } catch (JsonProcessingException e) {
            log.error("Ошибка десериализации {} : {}", message, e);
            throw new RuntimeException(e);
        }
    }

    public <T> String convertResponse(T response) {
        try {
            return objectMapper.writeValueAsString(response);
        } catch (JsonProcessingException e) {
            log.error("Ошибка конвертации ответа {} : {}", response, e);
            throw new RuntimeException(e);
        }
    }
}
