package ca.bc.gov.open.jag.efilingapi.file;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FileUploadConfig {

    private final FileUploadProperties fileUploadProperties;

    public FileUploadConfig(FileUploadProperties fileUploadProperties) {
        this.fileUploadProperties = fileUploadProperties;
    }

    @Bean
    public FileUploadService fileUploadService() { return new FileUploadServiceImpl(this.fileUploadProperties);  }
}
