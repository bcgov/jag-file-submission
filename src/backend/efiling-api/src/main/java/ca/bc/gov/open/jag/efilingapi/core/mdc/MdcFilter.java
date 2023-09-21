package ca.bc.gov.open.jag.efilingapi.core.mdc;

import java.io.IOException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;

import ca.bc.gov.open.jag.efilingapi.Keys;
import ca.bc.gov.open.jag.efilingapi.core.security.SecurityUtils;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;

/**
 * The MdcFilter class sets the context for any http request handled by the efiling api
 * It adds all the header (except authorization) and value of interest from the jwt token
 */
@Component
public class MdcFilter implements Filter {

    private Logger logger = LoggerFactory.getLogger(MdcFilter.class);

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {

        List<String> mdcKeys = new ArrayList<>();

        mdcKeys.addAll(setMDCHeaders((HttpServletRequest) servletRequest));

        mdcKeys.addAll(setMDCToken());

        filterChain.doFilter(servletRequest, servletResponse);

        mdcKeys.stream().forEach(MDC::remove);

    }

    private List<String> setMDCHeaders(HttpServletRequest httpRequest) {

        List<String> result = new ArrayList<>();

        Enumeration<String> headerNames = httpRequest.getHeaderNames();

        if (headerNames == null) return result;

        while (headerNames.hasMoreElements()) {

            String header = headerNames.nextElement();

            if (StringUtils.equalsIgnoreCase("authorization", header)) continue;

            String mdcKey = MessageFormat.format("{0}.{1}", Keys.MDC_EFILING_REQUEST_HEADERS, header);
            result.add(mdcKey);

            logger.debug("{}:{}", mdcKey, httpRequest.getHeader(header));
            MDC.put(mdcKey, httpRequest.getHeader(header));

        }

        return result;
    }

    private List<String> setMDCToken() {

        List<String> result = new ArrayList<>();

        SecurityUtils.getClientId().ifPresent(value -> result.add(setJwtValue("clientId", value)));
        SecurityUtils.getUniversalIdFromContext().ifPresent(value -> result.add(setJwtValue("universalId", value)));
        SecurityUtils.getApplicationCode().ifPresent(value -> result.add(setJwtValue("applicationCode", value)));
        SecurityUtils.getIdentityProvider().ifPresent(value -> result.add(setJwtValue("identityProvider", value)));

        return result;

    }

    private String setJwtValue( String key, String value) {
        String mdcKey = MessageFormat.format("{0}.{1}", Keys.MDC_EFILING_JWT, key);
        logger.debug("{}:{}", key, value);
        MDC.put(mdcKey, value);
        return mdcKey;
    }


}
