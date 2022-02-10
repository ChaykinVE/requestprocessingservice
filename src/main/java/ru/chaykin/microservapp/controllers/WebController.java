package ru.chaykin.microservapp.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import common.KafkaHelper;
import dto.requestservice.CreateRequestDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;
import ru.chaykin.microservapp.services.RequestProcessingService;

@Slf4j
@RestController
@RequestMapping("/requestservice")
@RequiredArgsConstructor
public class WebController {
    private final RequestProcessingService requestProcessingService;
    private final ObjectMapper objectMapper;

    @PostMapping("/createrequest")
    public Mono<String> processCreateRequest(@RequestBody String request) {
        CreateRequestDto requestDto = readMessage(request, CreateRequestDto.class);
        return Mono.fromFuture(requestProcessingService.createRequest(requestDto))
                .map(KafkaHelper::toJsonString);
    }

    public <T> T readMessage(String message, Class<T> requestType) {
        try {
            log.info("Десериализация сообщения [{}] для WebController-а {}", requestType.getSimpleName(), message);
            return objectMapper.readValue(message, requestType);
        } catch (JsonProcessingException e) {
            log.error("Ошибка десериализации {} : {}", message, e);
            throw new RuntimeException(e);
        }
    }
}
