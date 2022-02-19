package ru.chaykin.microservapp.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.jms.DefaultJmsListenerContainerFactoryConfigurer;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.config.DefaultJmsListenerContainerFactory;
import org.springframework.jms.config.JmsListenerContainerFactory;

import javax.jms.ConnectionFactory;

@Configuration
@ConfigurationProperties(prefix = "ibm.mq")
@Data
public class JmsConfig {
    @Value("${ibm.mq.enableMqContainer:true}")
    private boolean enableMqContainer;

    private CommonJmsConfig commonConfig;

    @Bean("mqContainerFactory")
    public JmsListenerContainerFactory<?> mqContainerFactory(ConnectionFactory connectionFactory,
                                                             DefaultJmsListenerContainerFactoryConfigurer configurer) {
        DefaultJmsListenerContainerFactory factory = new DefaultJmsListenerContainerFactory();
        configurer.configure(factory, connectionFactory);
        factory.setConcurrency(commonConfig.concurrency);
        factory.setAutoStartup(enableMqContainer);
        return factory;
    }

    /*@Bean("mqJmsTemplate")
    public JmsTemplate mqJmsTemplate(JmsPoolConnectionFactory connectionFactory) {
        return new JmsTemplate(connectionFactory);
    }*/

    /*@Bean
    public JmsPoolConnectionFactory jmsPoolConnectionFactory() {
        return new JmsPoolConnectionFactory();
    }*/

    @Data
    static class CommonJmsConfig {
        private Boolean enable = false;
        private String concurrency;
        private String inQueue;
        private String outQueue;
    }
}
