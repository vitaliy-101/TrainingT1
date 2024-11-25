package com.example.trainingt1.exceptions;

public class RetrievingDataKafkaException extends RuntimeException {
    public RetrievingDataKafkaException(String topic, String message) {
        super(String.format("Error receiving data in kafka in the topic = '%s'. Exception message: '%s'", topic, message));
    }
}
