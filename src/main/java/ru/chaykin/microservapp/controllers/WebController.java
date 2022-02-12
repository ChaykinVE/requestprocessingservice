package ru.chaykin.microservapp.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import common.KafkaHelper;
import dto.requestservice.CreateRequestDto;
import dto.requestservice.DeleteRequestDto;
import dto.requestservice.GetRequestDto;
import dto.requestservice.UpdateRequestDto;
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

    @PostMapping("/getrequest")
    public Mono<String> processGetRequest(@RequestBody String request) {
        GetRequestDto requestDto = readMessage(request, GetRequestDto.class);
        return Mono.fromFuture(requestProcessingService.getRequest(requestDto))
                .map(KafkaHelper::toJsonString);
    }

    @PostMapping("/updaterequest")
    public Mono<String> processUpdateRequest(@RequestBody String request) {
        UpdateRequestDto requestDto = readMessage(request, UpdateRequestDto.class);
        return Mono.fromFuture(requestProcessingService.updateRequest(requestDto))
                .map(KafkaHelper::toJsonString);
    }

    @PostMapping("/deleterequest")
    public Mono<String> processDeleteRequest(@RequestBody String request) {
        DeleteRequestDto requestDto = readMessage(request, DeleteRequestDto.class);
        return Mono.fromFuture(requestProcessingService.deleteRequest(requestDto))
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
