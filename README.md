# Getting Started

# HR Assistant

## Overview

The application uses a RAG (Retrieval-Augmented Generation) architecture to extract relevant information from PDF files and then generates questions about the content of those documents. Its goal is to help users intelligently interact with complex documents by automating the retrieval of information and generating clear responses based on the extracted text.

## Setup Instructions

1. **Visit the PGVector Documentation:**
   To get started, go to the following documentation page: [Spring AI PGVector Documentation](https://docs.spring.io/spring-ai/reference/api/vectordbs/pgvector.html). This page contains all the necessary setup instructions to work with Spring AI and PGVector.

2. This assistant is base on OpenAI. So go to https://platform.openai.com and generate an api-key after crediting you account to be able to query the OpenAI.
## Available Endpoints

Our application provides two primary endpoints:

### 1. Upload File Endpoint

- **Endpoint:** `/upload`
- **Method:** `POST`
- **Description:** Use this endpoint to upload files to the server. The uploaded files will be processed and stored in the PGVector store.

#### Request Example:
```http

POST /upload
Content-Type: multipart/form-data
<file>
````

### 2. Ask a question to the assistant

- **Endpoint:** `/chat`
- **Params:** `query`
- **Method:** `GET`
- **Description:** Use this endpoint to ask a question to the assistant.

#### Request Example:
```http
GET /chat?query=What is the main topic of the uploaded document?
````

