# ClarityVault API Documentation

This document provides detailed information about the API endpoints for the ClarityVault application.

## Table of Contents
1.  [User Registration API](#user-registration-api)
2.  [File Upload API](#file-upload-api)
3.  [File Processing API](#file-processing-api)

---

## User Registration API

Controller: `UserRegistrationController.java`

Handles user registration, login, and profile management.

### 1. Register a New User

-   **Endpoint:** `POST http://192.168.137.1:8080/register`
-   **Description:** Creates a new user account. It checks if the email is already in use before creating the new user. The password is encrypted before being saved.
-   **Request Body:**
    ```json
    {
      "firstName": "John",
      "lastName": "Doe",
      "email": "john.doe@example.com",
      "password": "yourpassword"
    }
    ```
-   **Responses:**
    -   `200 OK`: If the user is registered successfully.
    -   `400 Bad Request`: If the username (email) is already in use.
    -   `500 Internal Server Error`: If registration fails for other reasons.

### 2. User Login

-   **Endpoint:** `GET http://192.168.137.1:8080/login`
-   **Description:** Authenticates a user and returns a JWT token upon successful login.
-   **Request Parameters:**
    -   `email` (string): The user's email address.
    -   `password` (string): The user's password.
-   **Responses:**
    -   `200 OK`: Returns the JWT token as a string.
    -   `400 Bad Request`: If authentication fails due to wrong credentials.

### 3. Get User Data

-   **Endpoint:** `GET http://192.168.137.1:8080/data/{username}`
-   **Description:** Retrieves the profile information of a specific user.
-   **Path Variable:**
    -   `username` (string): The email of the user to retrieve.
-   **Responses:**
    -   `200 OK`: Returns the `UserRegistrationsModel` object for the user.
    -   `500 Internal Server Error`: If the user is not found.

### 4. Update User Registration

-   **Endpoint:** `PUT http://192.168.137.1:8080/updateRegistration`
-   **Description:** Updates the details of an existing user.
-   **Request Body:**
    ```json
    {
      "id": 1,
      "firstName": "Johnathan",
      "lastName": "Doe",
      "email": "john.doe@example.com",
      "password": "newpassword"
    }
    ```
-   **Responses:**
    -   `200 OK`: If the user is updated successfully.
    -   `500 Internal Server Error`: If the update fails.

### 5. Get All Users

-   **Endpoint:** `GET http://192.168.137.1:8080/getAllUsers`
-   **Description:** Retrieves a list of all registered users.
-   **Responses:**
    -   `200 OK`: Returns a list of `UserRegistrationsModel` objects.

### 6. Get User's Name

-   **Endpoint:** `GET http://192.168.137.1:8080/getName`
-   **Description:** Retrieves the full name of a user by their username (email).
-   **Request Parameter:**
    -   `username` (string): The user's email address.
-   **Responses:**
    -   `200 OK`: Returns the user's full name as a string.

---

## File Upload API

Controller: `FileUploadController.java`

Handles file storage, deletion, and retrieval.

### 1. Save a File

-   **Endpoint:** `POST http://192.168.137.1:8080/api/files/save`
-   **Description:** Uploads and saves a file for a specific user.
-   **Request Parameters (form-data):**
    -   `username` (string): The username (email) of the file owner.
    -   `file` (file): The file to be uploaded.
-   **Validations:**
    -   Max file size: 10MB.
    -   Allowed types: PDF, DOC, DOCX, XLS, XLSX, PPT, PPTX, TXT.
-   **Responses:**
    -   `200 OK`: On success, returns a JSON object with file details.
    -   `400 Bad Request`: If file validation fails.
    -   `500 Internal Server Error`: If saving the file fails.

### 2. Delete a File

-   **Endpoint:** `DELETE http://192.168.137.1:8080/api/files/delete/{id}`
-   **Description:** Deletes a file by its unique ID.
-   **Path Variable:**
    -   `id` (long): The ID of the file to delete.
-   **Responses:**
    -   `200 OK`: If the file is deleted successfully.
    -   `404 Not Found`: If no file with the given ID is found.
    -   `500 Internal Server Error`: If deletion fails.

### 3. Find File by ID

-   **Endpoint:** `GET http://192.168.137.1:8080/api/files/find/{id}`
-   **Description:** Retrieves metadata for a specific file.
-   **Path Variable:**
    -   `id` (long): The ID of the file to find.
-   **Responses:**
    -   `200 OK`: Returns a JSON object with file metadata.
    -   `404 Not Found`: If the file is not found.
    -   `500 Internal Server Error`: If the search fails.

### 4. Download a File

-   **Endpoint:** `GET http://192.168.137.1:8080/api/files/download/{id}`
-   **Description:** Downloads the content of a specific file.
-   **Path Variable:**
    -   `id` (long): The ID of the file to download.
-   **Responses:**
    -   `200 OK`: Returns the file as an attachment.
    -   `404 Not Found`: If the file is not found.
    -   `500 Internal Server Error`: If the download fails.

### 5. Find Files by Username

-   **Endpoint:** `GET http://192.168.137.1:8080/api/files/findByUsername/{username}`
-   **Description:** Retrieves metadata for all files belonging to a specific user.
-   **Path Variable:**
    -   `username` (string): The username (email) to search for.
-   **Responses:**
    -   `200 OK`: Returns a JSON object containing a list of files.
    -   `500 Internal Server Error`: If the search fails.

---

## File Processing API

Controller: `FileProcessingController.java`

Handles intelligent processing of text and PDF files.

### 1. Translate PDF Content

-   **Endpoint:** `POST http://192.168.137.1:8080/pdf_translation`
-   **Description:** Translates the entire content of a PDF file to a specified language.
-   **Request Parameters (form-data):**
    -   `file` (file): The PDF file to translate.
    -   `language` (string): The target language (e.g., "Spanish").
-   **Responses:**
    -   `200 OK`: Returns a JSON response from the processing service containing the translation.
    -   `400 Bad Request`: If the uploaded file is not a PDF.
    -   `500 Internal Server Error`: On processing error.

### 2. Translate Text

-   **Endpoint:** `POST http://192.168.137.1:8080/text_translation`
-   **Description:** Translates a string of text to a specified language.
-   **Request Parameters (form-data):**
    -   `text` (string): The text to translate.
    -   `language` (string): The target language.
-   **Responses:**
    -   `200 OK`: Returns the translated text as a plain string.
    -   `500 Internal Server Error`: On processing error.

### 3. Extract and Summarize from PDF

-   **Endpoint:** `POST http://192.168.137.1:8080/pdf_jargon_extraction`
-   **Description:** Analyzes a PDF to identify, extract, and summarize key sections and clauses in a specified language.
-   **Request Parameters (form-data):**
    -   `file` (file): The PDF file to analyze.
    -   `language` (string): The language for the summaries.
-   **Responses:**
    -   `200 OK`: Returns a JSON response with structured summaries of the document.
    -   `400 Bad Request`: If the uploaded file is not a PDF.
    -   `500 Internal Server Error`: On processing error.

### 4. Search YouTube Videos

-   **Endpoint:** `GET http://192.168.137.1:8080/search`
-   **Description:** Searches for YouTube videos by title and language.
-   **Request Parameters:**
    -   `title` (string): The video title to search for.
    -   `language` (string): The language of the videos.
-   **Responses:**
    -   `200 OK`: Returns a list of YouTube video links (strings).

### 5. Identify Document Type

-   **Endpoint:** `POST http://192.168.137.1:8080/find_Document_type`
-   **Description:** Identifies the type of a document (e.g., contract, invoice) from a PDF file.
-   **Request Parameters (form-data):**
    -   `file` (file): The PDF file to analyze.
-   **Responses:**
    -   `200 OK`: Returns a JSON response containing the document type.
    -   `400 Bad Request`: If the uploaded file is not a PDF.
    -   `500 Internal Server Error`: On processing error.

### 6. Analyze Text Content

-   **Endpoint:** `POST http://192.168.137.1:8080/analyze_text`
-   **Description:** Performs a detailed analysis of a given text, tailored to the document type and language.
-   **Request Parameters (form-data):**
    -   `text` (string): The text to analyze.
    -   `language` (string): The language for the analysis output.
    -   `documentType` (string): The type of document the text is from (e.g., "Legal Contract").
-   **Responses:**
    -   `200 OK`: Returns the analysis as a plain string.
    -   `500 Internal Server Error`: On processing error.
