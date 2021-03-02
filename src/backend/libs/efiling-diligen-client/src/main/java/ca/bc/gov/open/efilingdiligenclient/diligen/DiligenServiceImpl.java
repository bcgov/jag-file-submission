package ca.bc.gov.open.efilingdiligenclient.diligen;

import ca.bc.gov.open.efilingdiligenclient.Keys;
import ca.bc.gov.open.efilingdiligenclient.diligen.mapper.DiligenDocumentDetailsMapper;
import ca.bc.gov.open.efilingdiligenclient.diligen.model.DiligenDocumentDetails;
import ca.bc.gov.open.efilingdiligenclient.diligen.model.DiligenResponse;
import ca.bc.gov.open.efilingdiligenclient.exception.DiligenDocumentException;
import ca.bc.gov.open.jag.efilingdiligenclient.api.DocumentsApi;
import ca.bc.gov.open.jag.efilingdiligenclient.api.handler.ApiException;
import ca.bc.gov.open.jag.efilingdiligenclient.api.model.InlineResponse2003;
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
import java.util.Optional;

@Service
public class DiligenServiceImpl implements DiligenService {

    Logger logger = LoggerFactory.getLogger(DiligenServiceImpl.class);

    private final RestTemplate restTemplate;

    private final DiligenProperties diligenProperties;

    private final DiligenAuthService diligenAuthService;

    private final ObjectMapper objectMapper;

    private final DocumentsApi documentsApi;

    private final DiligenDocumentDetailsMapper diligenDocumentDetailsMapper;

    public DiligenServiceImpl(RestTemplate restTemplate, DiligenProperties diligenProperties, DiligenAuthService diligenAuthService, ObjectMapper objectMapper, DocumentsApi documentsApi, DiligenDocumentDetailsMapper diligenDocumentDetailsMapper) {
        this.restTemplate = restTemplate;
        this.diligenProperties = diligenProperties;
        this.diligenAuthService = diligenAuthService;
        this.objectMapper = objectMapper;
        this.documentsApi = documentsApi;
        this.diligenDocumentDetailsMapper = diligenDocumentDetailsMapper;
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

        Optional<BigDecimal> fileId = tryGetFileId(headers, file.getOriginalFilename(), 5);

        if(!fileId.isPresent()) {
            throw new DiligenDocumentException("Failed getting the document id after 5 attempts");
        }

        return fileId.get();

    }

    @Override
    public DiligenDocumentDetails getDocumentDetails(BigDecimal documentId) {

        logger.debug("getting document details from diligen");

        documentsApi.getApiClient().setBearerToken(diligenAuthService.getDiligenJWT(diligenProperties.getUsername(), diligenProperties.getPassword()));

        try {
            InlineResponse2003 result = documentsApi.apiDocumentsFileIdDetailsGet(documentId.intValue());

            logger.info("detail retrieved");

            if (!result.getData().getFileDetails().getFileStatus().equals("PROCESSED")) throw new DiligenDocumentException(MessageFormat.format("Document not in processed status. Document in status {0}", result.getData().getFileDetails().getFileStatus()));

            return diligenDocumentDetailsMapper.toDiligenDocumentDetails(result.getData().getFileDetails());

        } catch (ApiException e) {
            throw new DiligenDocumentException("Failed getting the document details");
        }

    }

    private Optional<BigDecimal> tryGetFileId(HttpHeaders headers, String fileName, int maxAttempt) {
        
        int attempt = 0;

        logger.debug("attempting to retrieve document Id");

        while(attempt < maxAttempt) {

            Optional<BigDecimal> result = getFileId(headers, fileName);
            if(result.isPresent()) return result;
            attempt++;

        }

        return Optional.empty();
        
    }

    private Optional<BigDecimal> getFileId(HttpHeaders headers, String fileName) {

        try {
            HttpEntity<String> entity = new HttpEntity<>(null, headers);

            ResponseEntity<String> searchResponse = restTemplate.exchange(constructUrl(MessageFormat.format(Keys.GET_DOCUMENT_PATH, diligenProperties.getProjectIdentifier(), fileName)), HttpMethod.GET, entity, String.class);

            if (!searchResponse.getStatusCode().is2xxSuccessful())
                throw new DiligenDocumentException(searchResponse.getStatusCode().toString());

            DiligenResponse diligenResponse = objectMapper.readValue(searchResponse.getBody(), DiligenResponse.class);

            if (!diligenResponse.getData().getDocuments().isEmpty()) {
                BigDecimal result = diligenResponse.getData().getDocuments().get(0).getFileId();
                logger.debug("successfully retrieved document id {}", result);
                return Optional.of(result);
            } else {
                logger.debug("attempt {} to retrieve document id failed, waiting 2s");
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        } catch (JsonProcessingException e) {
            //Using the exceptions message can contain PII data. It is safer to just say it failed for now.
            throw new DiligenDocumentException("Failed in object deserialization");
        }

        return Optional.empty();
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
