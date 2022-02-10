package ru.chaykin.microservapp.services;

import common.Message;
import common.simple.CallbackContext;
import common.simple.SimpleKafkaTemplate;
import config.Producer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerRecord;
import ru.chaykin.microservapp.config.KafkaConfig;
import utils.KafkaUtils;

import java.util.concurrent.CompletableFuture;

@Slf4j
@RequiredArgsConstructor
public abstract class TemplateKafkaService {

    protected final KafkaConfig kafkaConfig;
    protected final SimpleKafkaTemplate<String, Message> kafkaTemplate;

    public <T extends Message> CompletableFuture<T> sendMessage(Message message, Producer producer) {
        return this.send(message)
                .thenApply(KafkaUtils::<T>getResponseFromCallbackContext)
                .exceptionally(ex -> {
                    log.error("[{}]. Не удалось получить ответ от {}: ", message.getClass().getSimpleName(),
                            producer.getGroupTopic(), ex);
                    throw new RuntimeException(ex);
                });
    }

    private CompletableFuture<CallbackContext> send (Message message) {
        ProducerRecord<String, Message> record = KafkaUtils.generateProducerRecord(kafkaConfig.getProducers().getRequestservice().getGroupTopic(),
                kafkaConfig.getProducers().getRequestservice().toString(), kafkaConfig, message);
        log.info("ProducerRecord : {}", record);
        return kafkaTemplate.sendMessage(record);
    }
}
