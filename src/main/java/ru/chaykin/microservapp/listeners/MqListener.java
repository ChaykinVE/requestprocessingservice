package ru.chaykin.microservapp.listeners;

import dto.requestservice.CreateRequestDto;
import dto.requestservice.DeleteRequestDto;
import dto.requestservice.GetRequestDto;
import dto.requestservice.UpdateRequestDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;
import ru.chaykin.microservapp.services.RequestProcessingService;
import ru.chaykin.microservapp.utils.MessageConverter;

import javax.jms.TextMessage;
import java.util.function.Consumer;

@Service
@Slf4j
@RequiredArgsConstructor
public class MqListener {

    private final JmsTemplate jmsTemplate;
    private final MessageConverter messageConverter;
    private final RequestProcessingService requestProcessingService;

    @JmsListener(destination = "${ibm.mq.commonConfig.createRequest.inQueue}", containerFactory = "mqContainerFactory")
    public void createRequest(TextMessage message) {
        jmsRequestHandle(message, CreateRequestDto.class,
                x -> requestProcessingService.createRequest(x)
                        .thenAccept(this::sendMessage));
    }

    @JmsListener(destination = "${ibm.mq.commonConfig.getRequest.inQueue}", containerFactory = "mqContainerFactory")
    public void getRequest(TextMessage message) {
        jmsRequestHandle(message, GetRequestDto.class,
                x -> requestProcessingService.getRequest(x)
                        .thenAccept(this::sendMessage));
    }

    @JmsListener(destination = "${ibm.mq.commonConfig.updateRequest.inQueue}", containerFactory = "mqContainerFactory")
    public void updateRequest(TextMessage message) {
        jmsRequestHandle(message, UpdateRequestDto.class,
                x -> requestProcessingService.updateRequest(x)
                        .thenAccept(this::sendMessage));
    }

    @JmsListener(destination = "${ibm.mq.commonConfig.deleteRequest.inQueue}", containerFactory = "mqContainerFactory")
    public void deleteRequest(TextMessage message) {
        jmsRequestHandle(message, DeleteRequestDto.class,
                x -> requestProcessingService.deleteRequest(x)
                        .thenAccept(this::sendMessage));
    }

    private <T> void jmsRequestHandle(TextMessage message, Class<T> requestType, Consumer<T> consumer) {
        log.debug("Received message from mq [{}], message: {}", requestType.getSimpleName(), message);
        try {
            T request = messageConverter.readMessage(message.getText(), requestType);
            consumer.accept(request);
        } catch (Exception e) {
            log.error("Failed getting message from mq [{}], message: {}", requestType.getSimpleName(), message);
        }
    }

    private <T> void sendMessage(T response) {
        jmsTemplate.send(session -> {
            try {
                String message = messageConverter.convertResponse(response);
                TextMessage textMessage = session.createTextMessage(message);
                log.debug("Sending {} response to mq, message: {}", response.getClass().getSimpleName(), response);
                return textMessage;
            } catch (Exception ex) {
                log.error("Error while creating response: {}", response, ex);
                throw new RuntimeException(ex);
            }
        });
    }
}
