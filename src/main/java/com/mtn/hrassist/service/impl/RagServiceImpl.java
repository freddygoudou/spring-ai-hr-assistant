package com.mtn.hrassist.service.impl;

import com.mtn.hrassist.events.EventsPublisher;
import com.mtn.hrassist.service.RagService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.SystemPromptTemplate;
import org.springframework.ai.document.Document;
import org.springframework.ai.reader.pdf.PagePdfDocumentReader;
import org.springframework.ai.reader.pdf.config.PdfDocumentReaderConfig;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class RagServiceImpl implements RagService {

    private ChatClient chatClient;
    private final VectorStore vectorStore;
    private final EventsPublisher eventsPublisher;
    private final FileStorageService fileStorageService;

    public RagServiceImpl(VectorStore vectorStore, ChatClient.Builder chatClient, EventsPublisher eventsPublisher, FileStorageService fileStorageService) {
        this.vectorStore = vectorStore;
        this.chatClient = chatClient.build();
        this.eventsPublisher = eventsPublisher;
        this.fileStorageService = fileStorageService;
    }


    @Override
    public ResponseEntity<String> uploadDocumentResource(List<MultipartFile> files) {
        if (files.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("No files to upload");
        }

        for (MultipartFile file : files) {
            try {
                eventsPublisher.publishResourceUploadedEvent(fileStorageService.uploadFile(file));
            } catch (IOException e) {
                System.out.println("The error : "+e.getMessage());
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body("Failed to upload file: " + file.getOriginalFilename());
            }
        }
        return ResponseEntity.status(HttpStatus.OK).body("Files uploaded successfully!");
    }


    @Override
    public String chat(VectorStore vectorStore, String query) {
        // - This is supposed to be the user question as query param

        List<Document> documentList = vectorStore.similaritySearch(query);

        String systemMessageTemplate = """
					Answer the following question based only on the provided CONTEXT.
					If the answer is not found in the context, respond "I don't know".
					CONTEXT :
						{CONTEXT}
					""";

        Message systemMessage = new SystemPromptTemplate(systemMessageTemplate).createMessage(Map.of("CONTEXT", documentList));
        UserMessage userMessage = new UserMessage(query);
        Prompt prompt = new Prompt(List.of(systemMessage, userMessage));
        ChatResponse response = chatClient.prompt(prompt)
                .call()
                .chatResponse();
        String responseContent = response.getResult().getOutput().getContent();
        System.out.println(responseContent);
        return responseContent;
    }


    @Override
    public void textEmbedding(Resource pdfResource) {
        PdfDocumentReaderConfig config = PdfDocumentReaderConfig.defaultConfig();
        PagePdfDocumentReader pdfDocumentReader = new PagePdfDocumentReader(pdfResource, config);
        List<Document> documents = pdfDocumentReader.get();
        TokenTextSplitter tokenTextSplitter = new TokenTextSplitter();
        List<Document> chunkDocuments  = tokenTextSplitter.split(documents);
        vectorStore.accept(chunkDocuments);
    }
}
