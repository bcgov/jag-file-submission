package ca.bc.gov.open.jag.efilingcsoclient;


import ca.bc.gov.ag.csows.lookups.CodeValue;
import ca.bc.gov.ag.csows.lookups.LookupFacadeBean;
import ca.bc.gov.ag.csows.lookups.NestedEjbException_Exception;
import ca.bc.gov.ag.csows.lookups.ServiceFee;
import ca.bc.gov.open.jag.efilingcommons.exceptions.EfilingLookupServiceException;
import ca.bc.gov.open.jag.efilingcommons.model.ServiceFees;
import ca.bc.gov.open.jag.efilingcommons.model.SubmissionFeeRequest;
import ca.bc.gov.open.jag.efilingcommons.service.EfilingLookupService;
import ca.bc.gov.open.jag.efilingcommons.submission.models.LookupItem;
import org.apache.commons.lang3.StringUtils;

import javax.xml.datatype.DatatypeConfigurationException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class CsoLookupServiceImpl implements EfilingLookupService {

    private LookupFacadeBean lookupFacade;

    public CsoLookupServiceImpl(LookupFacadeBean lookupFacade) {
        this.lookupFacade = lookupFacade;
    }

    @Override
    public ServiceFees getServiceFee(SubmissionFeeRequest submissionFeeRequest)  {

        // NOTE- "DCFL" is the only string that will work here until we get our service types setup
        if (StringUtils.isEmpty(submissionFeeRequest.getServiceType())) throw new IllegalArgumentException("service type is required");
        if (StringUtils.isEmpty(submissionFeeRequest.getApplication())) throw new IllegalArgumentException("application code is required");
        if (StringUtils.isEmpty(submissionFeeRequest.getClassification())) throw new IllegalArgumentException("class code is required");
        if (StringUtils.isEmpty(submissionFeeRequest.getDivision())) throw new IllegalArgumentException("division code is required");
        if (StringUtils.isEmpty(submissionFeeRequest.getLevel())) throw new IllegalArgumentException("level code is required");

        try {
            ServiceFee fee = lookupFacade.getServiceFeeByClassification(submissionFeeRequest.getServiceType(),
                    CsoHelpers.date2XMLGregorian(new Date()),
                    submissionFeeRequest.getApplication(),
                    submissionFeeRequest.getDivision(),
                    submissionFeeRequest.getLevel(),
                    submissionFeeRequest.getClassification());
            if (fee == null)
                return new ServiceFees(
                        BigDecimal.ZERO,
                        null);

            return new ServiceFees(
                    fee.getFeeAmt(),
                    fee.getServiceTypeCd());

        }
        catch(DatatypeConfigurationException | NestedEjbException_Exception e) {
            throw new EfilingLookupServiceException("Exception while retrieving service fee", e.getCause());
        }

    }

    @Override
    public List<String> getValidPartyRoles(String courtLevel, String courtClass, String documentTypes) {
        try {
            List<CodeValue> partyRolesResponse = lookupFacade.getEfilingPartyRoles(courtLevel, courtClass, documentTypes);
            List<String> validRoles = new ArrayList<>();

            for (CodeValue partyRole : partyRolesResponse) {
                validRoles.add(partyRole.getCode());
            }

            return validRoles;

        } catch(NestedEjbException_Exception e) {
            throw new EfilingLookupServiceException("Exception while getting party roles", e.getCause());
        }
    }

    @Override
    public List<LookupItem> getCountries() {

        try {

            List<CodeValue> countryCodes = lookupFacade.getCountryCodes();
            List<CodeValue> countries = lookupFacade.getCountries();

            return countryCodes.stream()
                    .map(country -> LookupItem.builder().code(country.getCode()).description(
                           countries.stream()
                                .filter(codeValue -> codeValue.getCode().equals(country.getParentCode()))
                                .findFirst()
                                .orElseGet(this::setMissingCodeValue).getDescription()
                    ).create())
                    .collect(Collectors.toList());
        } catch(NestedEjbException_Exception e) {
            throw new EfilingLookupServiceException("Exception while getting countries", e.getCause());
        }

    }

    /**
     * If the parent is missing ensure drop down populates
     * @return Generic message
     */
    private CodeValue setMissingCodeValue() {

        CodeValue codeValue = new CodeValue();

        codeValue.setDescription("Missing Description");

        return codeValue;

    }

}
