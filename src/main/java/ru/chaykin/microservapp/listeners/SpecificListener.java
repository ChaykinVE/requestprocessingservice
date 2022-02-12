package ru.chaykin.microservapp.listeners;

import common.KafkaHeaderAccessor;
import common.Message;
import common.simple.SimpleKafkaListener;
import dto.requestservice.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.stereotype.Service;
import ru.chaykin.microservapp.config.KafkaProperties;

import java.util.Map;

@Service
@RequiredArgsConstructor
@KafkaListener(containerFactory = "specificKafkaListenerContainerFactory", topics = {"requestprocessingservice_specific"})
@Slf4j
public class SpecificListener extends SimpleKafkaListener {

    @Autowired
    private final KafkaProperties kafkaProperties;

    @KafkaHandler
    public void processCreateRequestResponse(CreateRequestResponseDto responseDto, @Headers Map<String, Object> headers,
                                             Acknowledgment acknowledgment) {
        processResponseMessage(headers, responseDto, acknowledgment);
    }

    @KafkaHandler
    public void processGetRequestResponse(GetRequestResponseDto responseDto, @Headers Map<String, Object> headers,
                                          Acknowledgment acknowledgment) {
        processResponseMessage(headers, responseDto, acknowledgment);
    }

    @KafkaHandler
    public void processUpdateRequestResponse(UpdateRequestResponseDto responseDto, @Headers Map<String, Object> headers,
                                             Acknowledgment acknowledgment) {
        processResponseMessage(headers, responseDto, acknowledgment);
    }

    @KafkaHandler
    public void processDeleteRequestResponse(DeleteRequestResponseDto responseDto, @Headers Map<String, Object> headers,
                                             Acknowledgment acknowledgment) {
        processResponseMessage(headers, responseDto, acknowledgment);
    }

    private void processResponseMessage(Map<String, Object> headers, Message message, Acknowledgment acknowledgment) {
        KafkaHeaderAccessor kafkaHeaderAccessor = KafkaHeaderAccessor.ofMap(headers);
        log.info("Received {}, kafkaHeaderAccessor: {} message: {}", message.getClass().getSimpleName(), kafkaHeaderAccessor, message);
        if (kafkaHeaderAccessor.requestId() == null) throw new IllegalStateException("requestId is null");
        try {
            if (kafkaProperties.getSpecificConsumer().getGroupId().equalsIgnoreCase(kafkaHeaderAccessor.destinationInstance())) {
                this.simpleKafkaTemplate().updateRequestsResponses(kafkaHeaderAccessor.requestId(), message, headers);
                acknowledgment.acknowledge();
            }
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }
}
