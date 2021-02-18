package ca.bc.gov.open.efilingdiligenclient.diligen;

import ca.bc.gov.open.efilingdiligenclient.Keys;
import ca.bc.gov.open.efilingdiligenclient.diligen.DiligenProperties;
import ca.bc.gov.open.efilingdiligenclient.exception.DiligenDocumentException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.MessageFormat;

@Service
public class DiligenServiceImpl implements DiligenService {

    Logger logger = LoggerFactory.getLogger(DiligenServiceImpl.class);

    private final RestTemplate restTemplate;

    private final DiligenProperties diligenProperties;

    private final DiligenAuthService diligenAuthService;

    public DiligenServiceImpl(RestTemplate restTemplate, DiligenProperties diligenProperties, DiligenAuthService diligenAuthService) {
        this.restTemplate = restTemplate;
        this.diligenProperties = diligenProperties;
        this.diligenAuthService = diligenAuthService;
    }
    @Override
    public BigDecimal postDocument(String documentType, MultipartFile file) {

        logger.debug("posting document to diligen");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);
        headers.setBearerAuth(diligenAuthService.getDiligenJWT(diligenProperties.getUsername(), diligenProperties.getPassword()));

        MultiValueMap<String, Object> body;
        try {
            body = new LinkedMultiValueMap<>();
            body.add("file_data", new FileSystemResource(file.getBytes(), file.getOriginalFilename()));
        } catch (IOException e) {
            throw new DiligenDocumentException(e.getMessage());
        }
        HttpEntity<MultiValueMap<String, Object>> requestEntity
                = new HttpEntity<>(body, headers);

        ResponseEntity<String> response = restTemplate.postForEntity(constructUrl(), requestEntity, String.class);

        if (!response.getStatusCode().is2xxSuccessful()) throw new DiligenDocumentException(response.getStatusCode().toString());

        logger.info("document posted to diligen");

        return BigDecimal.ONE;

    }

    private String constructUrl() {

        return MessageFormat.format("{0}{1}", diligenProperties.getBasePath(), MessageFormat.format(Keys.POST_DOCUMENT_PATH,diligenProperties.getProjectIdentifier()));

    }

    private static class FileSystemResource extends ByteArrayResource {

        private String fileName;

        public FileSystemResource(byte[] byteArray , String filename) {
            super(byteArray);
            this.fileName = filename;
        }

        public String getFilename() { return fileName; }

    }

}
