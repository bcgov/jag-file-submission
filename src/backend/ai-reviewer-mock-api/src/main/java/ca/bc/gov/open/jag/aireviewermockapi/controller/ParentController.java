package ca.bc.gov.open.jag.aireviewermockapi.controller;

import ca.bc.gov.open.jag.aireviewermockapi.model.DocumentReady;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.client.RestTemplate;

@Controller
public class ParentController {

    Logger logger = LoggerFactory.getLogger(ParentController.class);

    private final RestTemplate restTemplate;

    public ParentController(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @PostMapping("/parent/documentReady")
    public ResponseEntity documentReady(DocumentReady documentReady) {

        logger.info("Document {} of type {} is ready", documentReady.getDocumentId(), documentReady.getDocumentType());

        ResponseEntity<String> result = restTemplate.getForEntity(documentReady.getReturnUri(), String.class);

        if (result.getStatusCode().is2xxSuccessful()) {
            logger.info("Processing results: /n {}", result.getBody());
            return ResponseEntity.ok("Thanks");
        }
        return ResponseEntity.badRequest().body("Something Happened");

    }
}
