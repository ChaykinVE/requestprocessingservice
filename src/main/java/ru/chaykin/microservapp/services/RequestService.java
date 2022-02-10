package ru.chaykin.microservapp.services;

import common.Message;
import common.simple.SimpleKafkaTemplate;
import config.Producer;
import dto.requestservice.CreateRequestDto;
import dto.requestservice.CreateRequestResponseDto;
import org.springframework.stereotype.Service;
import ru.chaykin.microservapp.config.KafkaConfig;

import java.util.concurrent.CompletableFuture;

@Service
public class RequestService extends TemplateKafkaService {

    public RequestService(
            KafkaConfig kafkaConfig,
            SimpleKafkaTemplate<String, Message> kafkaTemplate) {
        super(kafkaConfig, kafkaTemplate);
    }

    public CompletableFuture<CreateRequestResponseDto> createRequest(CreateRequestDto createRequestDto) {
        return super.sendMessage(createRequestDto, kafkaConfig.getProducers().getRequestservice());
    }
}
