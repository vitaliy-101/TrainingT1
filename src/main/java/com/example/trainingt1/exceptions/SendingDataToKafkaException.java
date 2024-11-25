package com.example.trainingt1.exceptions;

    public class SendingDataToKafkaException extends RuntimeException {
    public SendingDataToKafkaException(String topic, String message) {
        super(String.format("Error sending data to the Kafka topic = '%s'. Exception message: '%s'", topic, message));
    }
}
