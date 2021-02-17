package ca.bc.gov.open.efilingdiligenclient.diligen;

import ca.bc.gov.open.efilingdiligenclient.Keys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.text.MessageFormat;
import java.util.Arrays;
import java.util.Collections;

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
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);
        headers.setBearerAuth(diligenAuthService.getDiligenJWT(diligenProperties.getUsername(), diligenProperties.getPassword()));

        MultiValueMap<String, Object> body
                = new LinkedMultiValueMap<>();
        body.add("file_data", Collections.singletonList(file));

        HttpEntity<MultiValueMap<String, Object>> requestEntity
                = new HttpEntity<>(body, headers);

        ResponseEntity<String> response = restTemplate.postForEntity(constructUrl(diligenProperties.getBasePath(), MessageFormat.format(Keys.POST_DOCUMENT_PATH,diligenProperties.getProjectIdentifier())), requestEntity, String.class);

        if (!response.getStatusCode().is2xxSuccessful()) throw new RuntimeException(response.getStatusCode().toString());
        return BigDecimal.ONE;

    }

    private String constructUrl(String basePath, String path) {
        return MessageFormat.format("{0}{1}", basePath, path);
    }

}
