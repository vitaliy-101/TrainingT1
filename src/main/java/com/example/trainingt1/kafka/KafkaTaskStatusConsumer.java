package com.example.trainingt1.kafka;

import com.example.trainingt1.aspect.LogBefore;
import com.example.trainingt1.exceptions.RetrievingDataKafkaException;
import com.example.trainingt1.kafka.dto.TaskStatusDto;
import com.example.trainingt1.notification.NotificationService;
import com.example.trainingt1.service.TaskService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
public class KafkaTaskStatusConsumer {
    private final TaskService taskService;
    private final NotificationService notificationService;

    @LogBefore
    @KafkaListener(
            id = "${spring.kafka.consumer.group-id}",
            topics = "${spring.kafka.topic.updating_tasks}",
            containerFactory = "taskStatusContainerFactory")
    public void taskStatusUpdateListener(@Payload TaskStatusDto taskStatusDto,
                                         @Header(KafkaHeaders.RECEIVED_KEY) String key,
                                         @Header(KafkaHeaders.RECEIVED_TOPIC) String topic,
                                         Acknowledgment acknowledgment) {
        try {
            //Для проверки можно закомментировать метод notificationService.sendSimpleEmail и использовать просто метод обновления в сервисе
            //taskService.updateTaskStatus(taskStatusDto);
            notificationService.sendSimpleEmail("Эти данные обновлены: " + taskService.updateTaskStatus(taskStatusDto));
            acknowledgment.acknowledge();
            log.info(String.format("Successful sending of data to a topic = '%s' by key = '%s'", topic, key));
        } catch (Exception e) {
            log.error(String.format("Error receiving data = '%s' in the kafka", taskStatusDto.toString()));
            throw new RetrievingDataKafkaException(topic, e.getMessage());
        }

    }
}
