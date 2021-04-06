package ca.bc.gov.open.jag.efilingreviewerapi.document;

import ca.bc.gov.open.efilingdiligenclient.diligen.model.FormData;
import ca.bc.gov.open.jag.efilingreviewerapi.document.models.DocumentTypeConfiguration;
import ca.bc.gov.open.jag.efilingreviewerapi.document.store.DocumentTypeConfigurationRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.stream.Collectors;

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

        InputStream resource = new ClassPathResource(
                "courtDetails.schema.json").getInputStream();

        try ( BufferedReader reader = new BufferedReader(
                new InputStreamReader(resource)) ) {

            String jsonSchemaStr = reader.lines()
                    .collect(Collectors.joining("\n"));

            ObjectMapper mapper = new ObjectMapper();

            FormData formData = mapper.readValue(jsonSchemaStr, FormData.class);

            if(documentTypeConfigurationRepository.findByDocumentType("RCC") == null) {

                DocumentTypeConfiguration documentTypeConfiguration = DocumentTypeConfiguration
                        .builder()
                        .documentType("RCC")
                        .formData(formData)
                        .create();

                documentTypeConfigurationRepository.save(documentTypeConfiguration);

                logger.info("successfully loaded document type configuration into store.");

            } else {
                logger.info("configuration is up to date.");
            }
        }
    }

}
