package ca.bc.gov.open.jag.efilingapi.file;

import ca.bc.gov.open.jag.efilingcommons.exceptions.EfilingFileException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.text.MessageFormat;

@Service
@EnableConfigurationProperties(FileUploadProperties.class)
public class FileUploadServiceImpl implements FileUploadService {

    Logger logger = LoggerFactory.getLogger(FileUploadServiceImpl.class);

    private final FileUploadProperties fileUploadProperties;

    public FileUploadServiceImpl(FileUploadProperties fileUploadProperties) {
        this.fileUploadProperties = fileUploadProperties;
    }

    @Override
    public void upload(byte[] file, String fileName) {
        try {
            Path copyLocation = Paths
                    .get(MessageFormat.format("{0}{1}{2}", fileUploadProperties.getLocation() , File.separator , StringUtils.cleanPath(fileName)));
            Files.copy(new ByteArrayInputStream(file), copyLocation, StandardCopyOption.REPLACE_EXISTING);
        } catch (Exception e) {
            logger.error("Error uploading file.", e);
            throw new EfilingFileException("Could not upload file");
        }

    }
}
