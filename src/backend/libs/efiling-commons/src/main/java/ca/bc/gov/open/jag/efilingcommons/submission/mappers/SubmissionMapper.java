package ca.bc.gov.open.jag.efilingcommons.submission.mappers;


import ca.bc.gov.open.jag.efilingcommons.model.AccountDetails;
import ca.bc.gov.open.jag.efilingcommons.model.api.FilingPackage;
import ca.bc.gov.open.jag.efilingcommons.model.api.GenerateUrlRequest;
import ca.bc.gov.open.jag.efilingcommons.submission.models.Submission;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.UUID;

@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface SubmissionMapper {

    @Mapping(source = "submissionId", target = "id")
    @Mapping(source = "accountDetails.universalId", target = "owner")
    @Mapping(source = "generateUrlRequest.navigation", target = "navigation")
    @Mapping(source = "generateUrlRequest.clientApplication", target = "clientApplication")
    @Mapping(source = "filingPackage", target = "filingPackage")
    @Mapping(source = "accountDetails", target = "accountDetails")
    @Mapping(source = "expiryDate", target = "expiryDate")
    Submission toSubmission(
            UUID submissionId,
            GenerateUrlRequest generateUrlRequest,
            FilingPackage filingPackage,
            AccountDetails accountDetails,
            long expiryDate);

}
