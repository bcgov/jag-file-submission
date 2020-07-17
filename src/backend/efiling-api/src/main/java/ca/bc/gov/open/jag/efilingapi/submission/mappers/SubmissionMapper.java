package ca.bc.gov.open.jag.efilingapi.submission.mappers;

import ca.bc.gov.open.jag.efilingapi.api.model.FilingPackage;
import ca.bc.gov.open.jag.efilingapi.api.model.GenerateUrlRequest;
import ca.bc.gov.open.jag.efilingapi.submission.models.Submission;
import ca.bc.gov.open.jag.efilingcommons.model.AccountDetails;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.math.BigDecimal;
import java.util.UUID;

@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface SubmissionMapper {

    @Mapping(source = "submissionId", target = "id")
    @Mapping(source = "owner", target = "accountDetails.universalId")
    @Mapping(source = "generateUrlRequest.navigation", target = "navigation")
    @Mapping(source = "generateUrlRequest.clientApplication", target = "clientApplication")
    @Mapping(source = "filingPackage", target = "filingPackage")
    @Mapping(source = "accountDetails", target = "accountDetails")
    @Mapping(source = "statutoryFeeAmount", target = "statutoryFeeAmount")
    @Mapping(source = "expiryDate", target = "expiryDate")
    Submission toSubmission(
            UUID submissionId,
            GenerateUrlRequest generateUrlRequest,
            FilingPackage filingPackage,
            AccountDetails accountDetails,
            BigDecimal statutoryFeeAmount,
            long expiryDate);

}
