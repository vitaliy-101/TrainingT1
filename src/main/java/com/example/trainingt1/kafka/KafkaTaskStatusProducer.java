package com.example.trainingt1.kafka;


import com.example.trainingt1.aspect.LogException;
import com.example.trainingt1.exceptions.SendingDataToKafkaException;
import com.example.trainingt1.kafka.dto.TaskStatusDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.protocol.Message;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
public class KafkaTaskStatusProducer {

    private final KafkaTemplate<String, TaskStatusDto> kafkaTemplate;

    @LogException
    public void send(String topic, TaskStatusDto taskStatusDto) {
        try {
            kafkaTemplate.send(topic, "task_status", taskStatusDto);
            kafkaTemplate.flush();
        }
        catch (Exception e) {
            log.error(String.format("Error sending data = '%s' to the Kafka topic", taskStatusDto.toString()));
            throw new SendingDataToKafkaException(topic, e.getMessage());
        }
    }

}
