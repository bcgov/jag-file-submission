package ca.bc.gov.open.jag.efilingcsoclient;

import ca.bc.gov.open.jag.efilingcommons.model.EfilingSoapClientProperties;
import org.apache.commons.lang3.StringUtils;
import org.apache.cxf.ext.logging.LoggingInInterceptor;
import org.apache.cxf.ext.logging.LoggingOutInterceptor;
import org.apache.cxf.jaxws.JaxWsProxyFactoryBean;

public class SoapUtils {

    private SoapUtils() {}

    public static <T> T getPort(Class<T> type, EfilingSoapClientProperties efilingSoapClientProperties, boolean debugEnabled) {
        JaxWsProxyFactoryBean jaxWsProxyFactoryBean = new JaxWsProxyFactoryBean();
        jaxWsProxyFactoryBean.setServiceClass(type);
        jaxWsProxyFactoryBean.setAddress(efilingSoapClientProperties.getUri());
        if(StringUtils.isNotBlank(efilingSoapClientProperties.getUserName()))
            jaxWsProxyFactoryBean.setUsername(efilingSoapClientProperties.getUserName());
        if(StringUtils.isNotBlank(efilingSoapClientProperties.getPassword()))
            jaxWsProxyFactoryBean.setPassword(efilingSoapClientProperties.getPassword());

        if(debugEnabled) {
            LoggingInInterceptor loggingInInterceptor = new LoggingInInterceptor();
            loggingInInterceptor.setPrettyLogging(true);
            LoggingOutInterceptor loggingOutInterceptor = new LoggingOutInterceptor();
            loggingOutInterceptor.setPrettyLogging(true);
            jaxWsProxyFactoryBean.getOutInterceptors().add(0, loggingOutInterceptor);
            jaxWsProxyFactoryBean.getInInterceptors().add(0, loggingInInterceptor);
        }

        return type.cast(jaxWsProxyFactoryBean.create());
    }
}
