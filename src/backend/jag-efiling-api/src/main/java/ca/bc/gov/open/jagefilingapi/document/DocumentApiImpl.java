package ca.bc.gov.open.jagefilingapi.document;

import ca.bc.gov.open.api.DocumentApi;
import ca.bc.gov.open.api.model.GenerateUrlRequest;
import ca.bc.gov.open.api.model.GenerateUrlResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
public class DocumentApiImpl implements DocumentApi {

    @Override
    public ResponseEntity<GenerateUrlResponse> generateUrl(@Valid GenerateUrlRequest generateUrlRequest) {

        GenerateUrlResponse response = new GenerateUrlResponse();

        response.setEFilingUrl("https://httpbin.org/");

        return ResponseEntity.ok(response);

    }
}
