package com.mtn.hrassist.events;

import com.mtn.hrassist.service.RagService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationListener;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Component;

import java.net.MalformedURLException;
import java.nio.file.Path;
import java.nio.file.Paths;


@Component
@RequiredArgsConstructor
public class ResourceUploadedEventListener implements ApplicationListener<ResourceUploadedEvent> {

    private final RagService ragService;

    @Override
    public void onApplicationEvent(ResourceUploadedEvent event) {
        try {
            System.out.println("The path is : "+event.getFilePath());
            Path path = Paths.get(event.getFilePath());
            Resource resource = new UrlResource(path.toUri());
            ragService.textEmbedding(resource);
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
    }
}
