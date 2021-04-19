package ca.bc.gov.open.jag.aireviewermockapi.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

@Controller
public class ProjectController {

    @PostMapping("/api/projects/{projectId}/documents")
    public ResponseEntity postFile(@PathVariable String projectId) {
        return ResponseEntity.ok("accepted");
    }

    @GetMapping("/api/projects/{projectId}/documents")
    public ResponseEntity getFileId(@PathVariable String projectId,
                                    @RequestParam Map<String, String> filter) {
        String fileName = filter.get("filter[filename]");

        if (fileName.equals("test-document.pdf")) {
            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_JSON)
                    .body("{\n" +
                            "  \"data\": {\n" +
                            "    \"documents\": [\n" +
                            "      {\n" +
                            "        \"file_id\": 1234\n" +
                            "      }\n" +
                            "    ]\n" +
                            "  }\n" +
                            "}");
        }

        return new ResponseEntity(HttpStatus.NOT_FOUND);
    }

}
