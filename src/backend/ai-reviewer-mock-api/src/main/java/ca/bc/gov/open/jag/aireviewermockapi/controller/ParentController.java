package ca.bc.gov.open.jag.aireviewermockapi.controller;

import ca.bc.gov.open.jag.aireviewermockapi.model.DocumentReady;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.client.RestTemplate;

import java.text.MessageFormat;
import java.util.UUID;

@Controller
public class ParentController {

    Logger logger = LoggerFactory.getLogger(ParentController.class);

    private final RestTemplate restTemplate;

    public ParentController(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @PostMapping("/parent/documentReady")
    public ResponseEntity documentReady(@RequestBody DocumentReady documentReady) {

        logger.info("Document {} of type {} is ready", documentReady.getDocumentId(), documentReady.getDocumentType());

        HttpHeaders headers = new HttpHeaders();
        headers.add("X-Transaction-Id", UUID.randomUUID().toString());

        HttpEntity<String> entity = new HttpEntity<>(null, headers);

        ResponseEntity<String> result = restTemplate.exchange(documentReady.getReturnUri(), HttpMethod.GET, entity, String.class);

        if (result.getStatusCode().is2xxSuccessful()) {
            logger.info("Processing results: {}", result.getBody());
            return ResponseEntity.ok("Thanks");
        }
        return ResponseEntity.badRequest().body("Something Happened");

    }
}
