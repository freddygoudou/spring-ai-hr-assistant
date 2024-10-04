package com.mtn.hrassist.events;


import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.context.ApplicationEvent;


@ToString
@Getter
@Setter
public class ResourceUploadedEvent extends ApplicationEvent {

    private String filePath;

    public ResourceUploadedEvent(Object source, String filePath) {
        super(source);
        this.filePath = filePath;
    }
}
