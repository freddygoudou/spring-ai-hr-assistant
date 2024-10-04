package com.mtn.hrassist.events;

import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;


@Component
@RequiredArgsConstructor
public class EventsPublisher {

    private final ApplicationEventPublisher eventPublisher;

    public void publishResourceUploadedEvent(final String filePath) {
        ResourceUploadedEvent resourceUploadedEvent = new ResourceUploadedEvent(this, filePath);
        eventPublisher.publishEvent(resourceUploadedEvent);
    }
}
