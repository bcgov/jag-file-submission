package ca.bc.gov.open.jag.aireviewermockapi.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.stream.Collectors;

@Controller
public class DocumentController {

    @Value("classpath:data/valid.json")
    Resource valid;

    @Value("classpath:data/invalidDocumentType.json")
    Resource invalid;

    @GetMapping("/api/documents/{document_id}/details")
    public ResponseEntity getDetails(@PathVariable Integer document_id) {
        //Currently we do nothing with this data
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body("{\n" +
                        "  \"data\": {\n" +
                        "    \"file_details\": {\n" +
                        "      \"file_status\": \"PROCESSED\"" +
                        "}}}");
    }

    @GetMapping("/api/documents/{document_id}/projectFields")
    public ResponseEntity getFields(@PathVariable Integer document_id) throws IOException {
        if (document_id.equals(1234)) {
            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(getResourceFileAsString(valid));
        }

        if (document_id.equals(9999)) {
            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(getResourceFileAsString(invalid));
        }

        return new ResponseEntity(HttpStatus.NOT_FOUND);

    }

    private String getResourceFileAsString(Resource file) throws IOException {
        try (InputStream is = file.getInputStream()) {
            if (is == null) return null;
            try (InputStreamReader isr = new InputStreamReader(is);
                 BufferedReader reader = new BufferedReader(isr)) {
                return reader.lines().collect(Collectors.joining(System.lineSeparator()));
            }
        }
    }
}
