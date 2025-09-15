package com.kucp1127.clarityvault.FileProcessing.Controller;


import com.kucp1127.clarityvault.FileProcessing.Service.FileProcessingService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kucp1127.clarityvault.FileProcessing.Service.YouTubeVideoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;
import java.util.Objects;

@RestController
@CrossOrigin("*")
public class FileProcessingController {

    @Autowired
    private FileProcessingService fileProcessingService;

    @Autowired
    private YouTubeVideoService youTubeVideoService;

    @PostMapping("/pdf_translation")
    public ResponseEntity<?> convertPdfToLanguage(
            @RequestParam("file") MultipartFile pdfFile,
            @RequestParam("language") String language) {

        try {
            // Validate that the uploaded file is a PDF
            if (!Objects.equals(pdfFile.getContentType(), "application/pdf")) {
                return ResponseEntity.badRequest().body("Only PDF files are supported");
            }

            // Create prompt for language conversion
            String prompt = "Please convert the content of this PDF document to " + language +
                          ". Maintain the original structure and formatting as much as possible. " +
                          "Provide a clear and accurate translation of all text content.";

            // Call the service to process the PDF
            JsonNode response = fileProcessingService.getResponse(pdfFile, prompt);

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body("Error processing PDF: " + e.getMessage());
        }
    }

    @PostMapping("/text_translation")
    public ResponseEntity<?> translateTextToLanguage(
            @RequestParam("text") String text,
            @RequestParam("language") String language) {
        try {
            // Get the Mono<String> from service and extract the actual string content
            String translation = fileProcessingService.generateTranslation(text, language).block();

            // Parse the JSON response to extract only the translated text
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonResponse = objectMapper.readTree(translation);

            // Extract the translated text from the API response structure
            if (jsonResponse.has("candidates") && jsonResponse.get("candidates").isArray()) {
                JsonNode candidates = jsonResponse.get("candidates");
                if (!candidates.isEmpty()) {
                    JsonNode content = candidates.get(0).get("content");
                    if (content != null && content.has("parts")) {
                        JsonNode parts = content.get("parts");
                        if (parts.isArray() && !parts.isEmpty()) {
                            String translatedText = parts.get(0).get("text").asText();
                            return ResponseEntity.ok(translatedText);
                        }
                    }
                }
            }

            // If we couldn't extract the text, return the raw response
            return ResponseEntity.ok(translation);

        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body("Error translating text: " + e.getMessage());
        }
    }

    @PostMapping("/pdf_jargon_extraction")
    public ResponseEntity<?> extractJargonFromPdf(
            @RequestParam("file") MultipartFile pdfFile,
            @RequestParam("language") String language) {

        try {
            // Validate that the uploaded file is a PDF
            if (pdfFile.getContentType() == null || !pdfFile.getContentType().equals("application/pdf")) {
                return ResponseEntity.badRequest().body("Only PDF files are supported");
            }


            String prompt = "Analyze this PDF document to identify and summarize its most important sections and key clauses. " +
                    "Your goal is to make a complex document easy to navigate and understand.\n\n" +
                    "For each critical section you identify, provide the output in the following exact format:\n\n" +
                    "SECTION: [The name or title of the key section, e.g., 'Limitation of Liability']\n" +
                    "PAGE: [The exact page number where the section begins]\n" +
                    "SUMMARY: [A brief, easy-to-understand summary of what this section means and its implications, written in " + language + "]\n" +
                    "---\n\n" +
                    "Please focus on locating and explaining sections related to:\n" +
                    "- Core obligations and responsibilities of the parties\n" +
                    "- Financial elements like payment terms, fees, and penalties\n" +
                    "- Legal and liability clauses (Indemnification, Limitation of Liability, Governing Law)\n" +
                    "- The duration of the agreement (Term and Termination)\n" +
                    "- Confidentiality and data protection\n" +
                    "- Dispute resolution processes\n\n" +
                    "Provide summaries in " + language + " and ensure they are clear and concise for a general audience.";

            // Call the service to process the PDF
            JsonNode response = fileProcessingService.getResponse(pdfFile, prompt);

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body("Error extracting jargon from PDF: " + e.getMessage());
        }
    }

    @GetMapping("/search")
    public ResponseEntity<List<String>> searchVideos(
            @RequestParam String title,
            @RequestParam String language) {

        List<String> videoLinks = youTubeVideoService.searchVideosByTitleAndLanguage(title, language);
        return ResponseEntity.ok(videoLinks);
    }

    @PostMapping("/find_Document_type")
    public ResponseEntity<?> findDocumentType(@RequestParam("file") MultipartFile file) {
        try {

            if (!Objects.equals(file.getContentType(), "application/pdf")) {
                return ResponseEntity.badRequest().body("Only PDF files are supported");
            }


            String prompt = "Identify the type of this document (e.g., contract, invoice, report, etc.) whether" +
                    " it is a legal, financial, or technical document. give it in a single word or short phrase.";

            JsonNode response = fileProcessingService.getResponse(file, prompt);

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body("Error processing PDF: " + e.getMessage());
        }
    }

    @PostMapping("/analyze_text")
    public ResponseEntity<?> analyzeText(
            @RequestParam("text") String text,
            @RequestParam("language") String language,
            @RequestParam("documentType") String documentType) {
        try {
            // Get the Mono<String> from service and extract the actual string content
            String analysis = fileProcessingService.analyzeText(text, language, documentType).block();

            // Parse the JSON response to extract only the analysis text
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonResponse = objectMapper.readTree(analysis);

            // Extract the analysis text from the API response structure
            if (jsonResponse.has("candidates") && jsonResponse.get("candidates").isArray()) {
                JsonNode candidates = jsonResponse.get("candidates");
                if (!candidates.isEmpty()) {
                    JsonNode content = candidates.get(0).get("content");
                    if (content != null && content.has("parts")) {
                        JsonNode parts = content.get("parts");
                        if (parts.isArray() && !parts.isEmpty()) {
                            String analysisText = parts.get(0).get("text").asText();
                            return ResponseEntity.ok(analysisText);
                        }
                    }
                }
            }

            // If we couldn't extract the text, return the raw response
            return ResponseEntity.ok(analysis);

        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body("Error analyzing text: " + e.getMessage());
        }
    }

}
