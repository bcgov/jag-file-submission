package ca.bc.gov.open.jag.aireviewercsoapi.service;

import ca.bc.gov.open.jag.aireviewercsoapi.model.ProcessedDocument;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class ProcessedDocumentServiceImpl implements ProcessedDocumentService {

    private final RestTemplate restTemplate;

    public ProcessedDocumentServiceImpl(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    public void processDocument(ProcessedDocument processedDocument) {

    }

}
