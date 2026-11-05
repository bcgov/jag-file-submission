package ca.bc.gov.open.jag.efilingapi.document;

import ca.bc.gov.open.jag.efilingcommons.service.EfilingDocumentService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DocumentConfiguration {

    @Bean
    public DocumentStore documentStore(EfilingDocumentService efilingDocumentService) {
        return new DocumentStoreImpl(efilingDocumentService);
    }

    @Bean
    public DocumentService documentService(EfilingDocumentService efilingDocumentService) {
        return new DocumentServiceImpl(efilingDocumentService);
    }

}
