package ca.bc.gov.open.jag.aireviewermockapi.controller;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HealthController {

    @GetMapping("/api/isServerUp")
    public ResponseEntity getHealth() {
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body("{\n" +
                        "  \"answer\": \"yes\"\n" +
                        "}");
    }

}
