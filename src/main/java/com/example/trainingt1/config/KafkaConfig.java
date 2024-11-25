package com.example.trainingt1.config;

import com.example.trainingt1.kafka.MessageDeserializer;
import com.example.trainingt1.kafka.dto.TaskStatusDto;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.*;
import org.springframework.kafka.listener.CommonErrorHandler;
import org.springframework.kafka.listener.ContainerProperties;
import org.springframework.kafka.listener.DefaultErrorHandler;
import org.springframework.kafka.support.serializer.ErrorHandlingDeserializer;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.support.serializer.JsonSerializer;
import org.springframework.util.backoff.FixedBackOff;

import java.util.HashMap;
import java.util.Map;
@Slf4j
@Configuration
public class KafkaConfig {

    @Value("${spring.kafka.consumer.group-id}")
    private String groupId;
    @Value("${spring.kafka.bootstrap-servers}")
    private String servers;
    @Value("${spring.kafka.session.timeout.ms:20000}")
    private String sessionTimeout;
    @Value("${spring.kafka.max.partition.fetch.bytes:500000}")
    private String maxPartitionFetchBytes;
    @Value("${spring.kafka.max.poll.records:1}")
    private String maxPollRecords;
    @Value("${spring.kafka.max.poll.interval.ms:3000}")
    private String maxPollIntervalMs;

    public Map<String, Object> consumerListenerConfig() {
        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, servers);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, groupId);
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, MessageDeserializer.class);
        props.put(JsonDeserializer.VALUE_DEFAULT_TYPE, "com.example.trainingt1.kafka.dto.TaskStatusDto");
        props.put(JsonDeserializer.TRUSTED_PACKAGES, "*");
        props.put(JsonDeserializer.USE_TYPE_INFO_HEADERS, true);
        props.put(ConsumerConfig.SESSION_TIMEOUT_MS_CONFIG, sessionTimeout);
        props.put(ConsumerConfig.MAX_PARTITION_FETCH_BYTES_CONFIG, maxPartitionFetchBytes);
        props.put(ConsumerConfig.MAX_POLL_RECORDS_CONFIG, maxPollRecords);
        props.put(ConsumerConfig.MAX_POLL_INTERVAL_MS_CONFIG, maxPollIntervalMs);
        props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, Boolean.FALSE);
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        props.put(ErrorHandlingDeserializer.VALUE_DESERIALIZER_CLASS, MessageDeserializer.class.getName());
        props.put(ErrorHandlingDeserializer.KEY_DESERIALIZER_CLASS, MessageDeserializer.class);
        return props;
    }

    @Bean
    public ConsumerFactory<String, TaskStatusDto> taskStatusConsumerFactory() {
        DefaultKafkaConsumerFactory<String, TaskStatusDto> factory = new DefaultKafkaConsumerFactory<>(consumerListenerConfig());
        factory.setKeyDeserializer(new StringDeserializer());
        return factory;
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, TaskStatusDto> taskStatusContainerFactory(@Qualifier("taskStatusConsumerFactory") ConsumerFactory<String, TaskStatusDto> consumerFactory) {
        ConcurrentKafkaListenerContainerFactory<String, TaskStatusDto> factory =
                new ConcurrentKafkaListenerContainerFactory<>();
        factoryBuilder(consumerFactory, factory);
        return factory;
    }

    private <T> void factoryBuilder(ConsumerFactory<String, T> consumerFactory, ConcurrentKafkaListenerContainerFactory<String, T> factory) {
        factory.setConsumerFactory(consumerFactory);
        factory.setBatchListener(true);
        factory.setConcurrency(1);
        factory.getContainerProperties().setAckMode(ContainerProperties.AckMode.MANUAL_IMMEDIATE);
        factory.setCommonErrorHandler(errorHandler());

    }


    private CommonErrorHandler errorHandler() {
        DefaultErrorHandler errorHandler = new DefaultErrorHandler(new FixedBackOff(1000, 3));
        errorHandler.addNotRetryableExceptions(IllegalStateException.class);
        errorHandler.setRetryListeners(((record, ex, deliveryAttempt) -> {
            log.error("ErrorHandler: RetryListeners message = {}, offset = {}, deliveryAttempt = {}", ex.getMessage(), record.offset(), deliveryAttempt);
        }));
        return errorHandler;
    }

    @Bean("taskStatus")
    public KafkaTemplate<String, TaskStatusDto> taskStatusKafkaTemplate(ProducerFactory<String, TaskStatusDto> producerFactory) {
        return new KafkaTemplate<>(producerFactory);
    }

    @Bean
    public ProducerFactory<String, TaskStatusDto> taskStatusProducerFactory() {
        Map<String, Object> props = new HashMap<>();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, servers);
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        props.put(ProducerConfig.ENABLE_IDEMPOTENCE_CONFIG, true);
        return new DefaultKafkaProducerFactory<>(props);
    }







}
