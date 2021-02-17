package ca.bc.gov.open.jag.efilingreviewerapi.diligen;

import ca.bc.gov.open.jag.efilingdiligenclient.api.AuthenticationApi;
import ca.bc.gov.open.jag.efilingdiligenclient.api.handler.ApiClient;
import ca.bc.gov.open.jag.efilingdiligenclient.api.handler.ApiException;
import ca.bc.gov.open.jag.efilingdiligenclient.api.handler.ApiResponse;
import ca.bc.gov.open.jag.efilingdiligenclient.api.model.InlineObject;
import ca.bc.gov.open.jag.efilingdiligenclientstarter.DiligenProperties;
import ca.bc.gov.open.jag.efilingreviewerapi.extract.DocumentsApiDelegateImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;

@Service
public class DiligenServiceImpl implements DiligenService {

    Logger logger = LoggerFactory.getLogger(DiligenServiceImpl .class);

    private final DiligenProperties diligenProperties;

    public DiligenServiceImpl(DiligenProperties diligenProperties) {
        this.diligenProperties = diligenProperties;
    }


    @Override
    public BigDecimal postDocument(String documentType, MultipartFile file) {
        return null;
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
            ApiResponse<?> result = authenticationApi.apiLoginPostWithHttpInfo(loginParams);
            return "nothing it is void but why though";
        } catch (ApiException e) {
            throw new RuntimeException(e.getMessage());
        }

    }

}
