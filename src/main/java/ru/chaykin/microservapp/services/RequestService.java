package ru.chaykin.microservapp.services;

import common.Message;
import common.simple.SimpleKafkaTemplate;
import dto.requestservice.*;
import org.springframework.stereotype.Service;
import ru.chaykin.microservapp.config.KafkaProperties;

import java.util.concurrent.CompletableFuture;

@Service
public class RequestService extends TemplateKafkaService {

    public RequestService(
            KafkaProperties kafkaProperties,
            SimpleKafkaTemplate<String, Message> kafkaTemplate) {
        super(kafkaProperties, kafkaTemplate);
    }

    public CompletableFuture<CreateRequestResponseDto> createRequest(CreateRequestDto createRequestDto) {
        return super.sendMessage(createRequestDto, kafkaProperties.getProducers().getRequestservice());
    }

    public CompletableFuture<GetRequestResponseDto> getRequest(GetRequestDto getRequestDto) {
        return super.sendMessage(getRequestDto, kafkaProperties.getProducers().getRequestservice());
    }

    public CompletableFuture<UpdateRequestResponseDto> updateRequest(UpdateRequestDto updateRequestDto) {
        return super.sendMessage(updateRequestDto, kafkaProperties.getProducers().getRequestservice());
    }

    public CompletableFuture<DeleteRequestResponseDto> deleteRequest(DeleteRequestDto deleteRequestDto) {
        return super.sendMessage(deleteRequestDto, kafkaProperties.getProducers().getRequestservice());
    }
}
