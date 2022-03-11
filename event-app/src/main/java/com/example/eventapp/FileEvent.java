package com.example.eventapp;

import lombok.Builder;
import lombok.Getter;

import java.util.Map;
import java.util.UUID;

@Getter
@Builder
public class FileEvent {

    private String id;
    private String type;
    private Map<String, Object> data;private String id;

    public static FileEvent toCompleteEvent(Map data) {
        return FileEvent.builder()
                .id(UUID.randomUUID().toString())
                .type(EventType.COMPLETE.name())
                .data(data)
                .build()
                ;
    }
}
