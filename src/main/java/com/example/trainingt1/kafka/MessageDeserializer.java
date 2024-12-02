package com.example.trainingt1.kafka;

import lombok.RequiredArgsConstructor;
import org.apache.kafka.common.header.Headers;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.stereotype.Component;

import java.awt.*;

@Component
@RequiredArgsConstructor
public class MessageDeserializer<T> extends JsonDeserializer<T> {

    @Override
    public T deserialize(String topic, Headers headers, byte[] data) {
        try {
            return super.deserialize(topic, headers, data);
        }
        catch (Exception e) {
            //Custom exception
            return null;
        }
    }

    @Override
    public T deserialize(String topic, byte[] data) {
        try {
            return super.deserialize(topic, data);
        }
        catch (Exception e) {
            //Custom exception
            return null;
        }
    }


}
