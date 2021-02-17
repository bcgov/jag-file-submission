package ca.bc.gov.open.jag.efilingreviewerapi.diligen;

import ca.bc.gov.open.jag.efilingdiligenclient.api.AuthenticationApi;
import ca.bc.gov.open.jag.efilingdiligenclient.api.ProjectsApi;
import ca.bc.gov.open.jag.efilingdiligenclient.api.handler.ApiClient;
import ca.bc.gov.open.jag.efilingdiligenclient.api.handler.ApiException;
import ca.bc.gov.open.jag.efilingdiligenclient.api.handler.auth.Authentication;
import ca.bc.gov.open.jag.efilingdiligenclient.api.handler.auth.HttpBearerAuth;
import ca.bc.gov.open.jag.efilingdiligenclient.api.model.InlineObject;
import ca.bc.gov.open.jag.efilingdiligenclient.api.model.InlineResponse2001;
import ca.bc.gov.open.jag.efilingdiligenclientstarter.DiligenProperties;
import ca.bc.gov.open.jag.efilingreviewerapi.util.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.*;

@Service
public class DiligenServiceImpl implements DiligenService {

    Logger logger = LoggerFactory.getLogger(DiligenServiceImpl .class);

    private final DiligenProperties diligenProperties;

    private final RestTemplate restTemplate;

    public DiligenServiceImpl(DiligenProperties diligenProperties, RestTemplate restTemplate) {
        this.diligenProperties = diligenProperties;
        this.restTemplate = restTemplate;
    }


    @Override
    public BigDecimal postDocument(String documentType, MultipartFile file) {
        try {
            File outBoundFile = FileUtils.convert(file);
            List<File> files = Collections.singletonList(outBoundFile);
            postDiligenDocument(files);
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        }
        return BigDecimal.ONE;
    }

    private void postDiligenDocument(List<File> files) {
       // ApiKeyAuth apiKeyAuth = new ApiKeyAuth("header", "bearer");
        //apiKeyAuth.setApiKey(getDiligenJWT());

        HttpBearerAuth httpBearerAuth = new HttpBearerAuth("bearer");
        httpBearerAuth.setBearerToken(getDiligenJWT());

        Map<String, Authentication> authMap = new HashMap<>();
        authMap.put("ApiAuthKey", httpBearerAuth);

        ApiClient apiClient = new ApiClient(authMap);
        apiClient.setBasePath(diligenProperties.getBasePath());

        ProjectsApi projectsApi = new ProjectsApi(apiClient);
        try {
            projectsApi.apiProjectsProjectIdDocumentsPost(diligenProperties.getProjectIdentifier(), files, null);
        } catch (ApiException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    private String getDiligenJWT() {

        logger.debug("attempt to get diligen jwt");

        ApiClient apiClient = new ApiClient();
        apiClient.setBasePath(diligenProperties.getBasePath());

        AuthenticationApi authenticationApi = new AuthenticationApi(apiClient);

        InlineObject loginParams = new InlineObject();
        loginParams.setEmail(diligenProperties.getUsername());
        loginParams.setPassword(diligenProperties.getPassword());
        try {

            InlineResponse2001 result = authenticationApi.apiLoginPost(loginParams);

            if (result.getData() == null) throw new RuntimeException("No login data");

            logger.info("diligen login complete");

            return result.getData().getJwt();

        } catch (ApiException e) {
            throw new RuntimeException(e.getMessage());
        }

    }

}
