package com.mtn.hrassist.service;

import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface RagService {

    ResponseEntity<String> uploadDocumentResource(List<MultipartFile> files);

    String chat(VectorStore vectorStore, String query);

    void textEmbedding(Resource pdfResources);
}
