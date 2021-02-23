package ca.bc.gov.open.efilingdiligenclient.diligen;

import ca.bc.gov.open.efilingdiligenclient.Keys;
import ca.bc.gov.open.efilingdiligenclient.diligen.model.DiligenDocument;
import ca.bc.gov.open.efilingdiligenclient.diligen.model.DiligenResponse;
import ca.bc.gov.open.efilingdiligenclient.exception.DiligenDocumentException;
import ca.bc.gov.open.jag.efilingdiligenclient.api.ProjectsApi;
import ca.bc.gov.open.jag.efilingdiligenclient.api.handler.ApiException;
import ca.bc.gov.open.jag.efilingdiligenclient.api.model.Filter;
import ca.bc.gov.open.jag.efilingdiligenclient.api.model.InlineResponse2002;
import com.fasterxml.jackson.databind.DeserializationFeature;
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

    private final ProjectsApi projectsApi;

    private final ObjectMapper objectMapper;

    public DiligenServiceImpl(RestTemplate restTemplate, DiligenProperties diligenProperties, DiligenAuthService diligenAuthService, ProjectsApi projectsApi, ObjectMapper objectMapper) {
        this.restTemplate = restTemplate;
        this.diligenProperties = diligenProperties;
        this.diligenAuthService = diligenAuthService;
        this.projectsApi = projectsApi;
        this.objectMapper = objectMapper;
    }
    @Override
    public BigDecimal postDocument(String documentType, MultipartFile file) {

        logger.debug("posting document to diligen");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);
        String apiKey = diligenAuthService.getDiligenJWT(diligenProperties.getUsername(), diligenProperties.getPassword());
        headers.setBearerAuth(apiKey);

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

        try {
            ResponseEntity<String> searchResponse = restTemplate.exchange(constructUrl(MessageFormat.format(Keys.GET_DOCUMENT_PATH,diligenProperties.getProjectIdentifier(), file.getOriginalFilename())), HttpMethod.GET, requestEntity, String.class);
            objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
            DiligenResponse diligenResponse = objectMapper.readValue(searchResponse.getBody(), DiligenResponse.class);
            return diligenResponse.getData().getDocuments().get(0).getFileId();
        } catch (Exception e) {
            throw new DiligenDocumentException(e.getMessage());
        }

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
