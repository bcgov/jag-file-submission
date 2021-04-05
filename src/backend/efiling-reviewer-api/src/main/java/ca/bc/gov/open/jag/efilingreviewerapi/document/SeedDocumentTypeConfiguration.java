package ca.bc.gov.open.jag.efilingreviewerapi.document;

import ca.bc.gov.open.jag.efilingreviewerapi.document.models.DocumentTypeConfiguration;
import ca.bc.gov.open.jag.efilingreviewerapi.document.store.DocumentTypeConfigurationRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

@Component
public class SeedDocumentTypeConfiguration {

    private final Logger logger = LoggerFactory.getLogger(SeedDocumentTypeConfiguration.class);

    private final DocumentTypeConfigurationRepository documentTypeConfigurationRepository;

    public SeedDocumentTypeConfiguration(DocumentTypeConfigurationRepository documentTypeConfigurationRepository) {
        this.documentTypeConfigurationRepository = documentTypeConfigurationRepository;
    }

    @EventListener
    public void seed(ContextRefreshedEvent event) {
        try {
            loadConfig();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadConfig() throws IOException {

        logger.info("attempting to load document type configuration into store.");

        File resource = ResourceUtils.getFile("classpath:courtDetails.schema.json");

        if(documentTypeConfigurationRepository.findByDocumentType("RCC") == null) {

            DocumentTypeConfiguration documentTypeConfiguration = DocumentTypeConfiguration
                    .builder()
                    .documentType("RCC")
                    .jsonSchema(new String(Files.readAllBytes(Paths.get(resource.getPath()))))
                    .create();

            documentTypeConfigurationRepository.save(documentTypeConfiguration);

            logger.info("successfully loaded document type configuration into store.");

        } else {
            logger.info("configuration is up to date.");
        }
    }

}
