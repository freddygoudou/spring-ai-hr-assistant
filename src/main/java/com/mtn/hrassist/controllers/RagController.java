package com.mtn.hrassist.controllers;


import com.mtn.hrassist.service.RagService;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
public class RagController {

    private VectorStore vectorStore;
    private final RagService ragService;


    private RagController(VectorStore vectorStore, RagService ragService) {
        this.vectorStore = vectorStore;
        this.ragService = ragService;
    }

    @PostMapping("/upload")
    public ResponseEntity<String> uploadFiles(
            @RequestParam("files") List<MultipartFile> files
    ) {
        return ragService.uploadDocumentResource(files);
    }

    @GetMapping("/chat")
    public String chat(@RequestParam(defaultValue = "Donnes moi au format json le nom, les prénoms, la date de naissance les diplomes, les certifications et les experiences professionnelles")
                           String query) {
        return ragService.chat(vectorStore, query);
    }
}
