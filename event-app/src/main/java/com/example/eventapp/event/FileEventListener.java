package com.example.eventapp.event;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class FileEventListener {
    @EventListener
    public void onFileEventHandler(FileEvent fileEvent) {
        log.info("fileEvent recive type:{} data:{}",
                fileEvent.getType(), fileEvent.getData());
    }

}
