package ca.bc.gov.open.jag.efilingapi.file;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FileUploadConfig {

    private final FileUploadProperites fileUploadProperites;

    public FileUploadConfig(FileUploadProperites fileUploadProperites) {
        this.fileUploadProperites = fileUploadProperites;
    }

    @Bean
    public FileUploadService fileUploadService() { return new FileUploadServiceImpl(this.fileUploadProperites);  }
}
