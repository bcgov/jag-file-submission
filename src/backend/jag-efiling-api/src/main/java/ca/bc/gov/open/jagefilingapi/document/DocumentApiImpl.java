package ca.bc.gov.open.jagefilingapi.document;

import ca.bc.gov.open.api.DocumentApi;
import ca.bc.gov.open.api.model.GenerateUrlRequest;
import ca.bc.gov.open.api.model.GenerateUrlResponse;
import ca.bc.gov.open.jagefilingapi.config.NavigationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@EnableConfigurationProperties(NavigationProperties.class)
public class DocumentApiImpl implements DocumentApi {

    private final NavigationProperties navigationProperties;

    public DocumentApiImpl(NavigationProperties navigationProperties) {
        this.navigationProperties = navigationProperties;
    }

    @Override
    public ResponseEntity<GenerateUrlResponse> generateUrl(@Valid GenerateUrlRequest generateUrlRequest) {

        GenerateUrlResponse response = new GenerateUrlResponse();

        response.setEFilingUrl(navigationProperties.getBaseUrl());

        return ResponseEntity.ok(response);

    }
}
