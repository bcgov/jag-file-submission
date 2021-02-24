package ca.bc.gov.open.efilingdiligenclient.diligen;

import ca.bc.gov.open.efilingdiligenclient.Keys;
import ca.bc.gov.open.efilingdiligenclient.diligen.model.DiligenResponse;
import ca.bc.gov.open.efilingdiligenclient.exception.DiligenDocumentException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
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

    private final ObjectMapper objectMapper;

    public DiligenServiceImpl(RestTemplate restTemplate, DiligenProperties diligenProperties, DiligenAuthService diligenAuthService, ObjectMapper objectMapper) {
        this.restTemplate = restTemplate;
        this.diligenProperties = diligenProperties;
        this.diligenAuthService = diligenAuthService;
        this.objectMapper = objectMapper;
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

        ResponseEntity<String> response = restTemplate.postForEntity(constructUrl(MessageFormat.format(Keys.POST_DOCUMENT_PATH,diligenProperties.getProjectIdentifier())), requestEntity, String.class);

        if (!response.getStatusCode().is2xxSuccessful()) throw new DiligenDocumentException(response.getStatusCode().toString());

        logger.info("document posted to diligen");

        return getFileId(headers, file.getOriginalFilename());

    }

    private BigDecimal getFileId(HttpHeaders headers, String fileName) {

        BigDecimal result = BigDecimal.ZERO;
        int attempt = 0;

        logger.debug("attempting to retrieve document Id");

        while(attempt < 5 && result.equals(BigDecimal.ZERO)) {

            try {
                HttpEntity<String> entity = new HttpEntity<>(null, headers);

                ResponseEntity<String> searchResponse = restTemplate.exchange(constructUrl(MessageFormat.format(Keys.GET_DOCUMENT_PATH, diligenProperties.getProjectIdentifier(), fileName)), HttpMethod.GET, entity, String.class);

                if (!searchResponse.getStatusCode().is2xxSuccessful())
                    throw new DiligenDocumentException(searchResponse.getStatusCode().toString());

                DiligenResponse diligenResponse = objectMapper.readValue(searchResponse.getBody(), DiligenResponse.class);

                if (!diligenResponse.getData().getDocuments().isEmpty())
                    result = diligenResponse.getData().getDocuments().get(0).getFileId();

            } catch (JsonProcessingException e) {
                //Using the exceptions message can contain PII data. It is safer to just say it failed for now.
                throw new DiligenDocumentException("Failed in object deserialization");
            }

            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            attempt++;

        }

        if(result.equals(BigDecimal.ZERO)) throw new DiligenDocumentException("Failed getting the document id after 5 attempts");

        return result;
        
    }

    private String constructUrl(String path) {

        return MessageFormat.format("{0}{1}", diligenProperties.getBasePath(), path);

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
