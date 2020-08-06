package ca.bc.gov.open.jag.efiling.demo;

import ca.bc.gov.open.jag.efilingcommons.model.EfilingFilingPackage;
import ca.bc.gov.open.jag.efilingcommons.model.EfilingService;
import ca.bc.gov.open.jag.efilingcommons.service.EfilingSubmissionService;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.Date;
import java.util.GregorianCalendar;

public class EfilingSubmissionServiceDemoImpl implements EfilingSubmissionService {
    @Override
    public BigDecimal submitFilingPackage(EfilingService service, EfilingFilingPackage filingPackage) {
        return BigDecimal.ONE;
    }

    @Override
    public EfilingService addService(EfilingService efilingService) {
        GregorianCalendar entryDate = new GregorianCalendar();
        entryDate.setTime(Date.from(Instant.now()));
        efilingService.setServiceId(BigDecimal.TEN);
        try {
            efilingService.setServiceReceivedDateTime((DatatypeFactory.newInstance().newXMLGregorianCalendar(entryDate)));
        } catch (DatatypeConfigurationException e) {
            efilingService.setServiceReceivedDateTime(null);
        }
        efilingService.setServiceReceivedDtmText("Some recieved date message?");
        efilingService.setServiceSessionId(BigDecimal.TEN);
        efilingService.setServiceSubtypeCd("subTypeCd");
        efilingService.setServiceTypeCd("DCFL");
        efilingService.setServiceTypeDesc("Service type description");
        return efilingService;
    }

}
