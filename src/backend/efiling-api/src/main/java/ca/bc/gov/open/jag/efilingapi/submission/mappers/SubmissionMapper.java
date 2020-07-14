package ca.bc.gov.open.jag.efilingapi.submission.mappers;

import ca.bc.gov.open.jag.efilingcommons.model.AccountDetails;
import ca.bc.gov.open.jag.efilingapi.fee.models.Fee;
import ca.bc.gov.open.jag.efilingapi.submission.models.Submission;
import ca.bc.gov.open.jag.efilingapi.api.model.GenerateUrlRequest;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface SubmissionMapper {

    @Mapping(source = "generateUrlRequest.documentProperties", target = "documentProperties")
    @Mapping(source = "generateUrlRequest.navigation", target = "navigation")
    @Mapping(source = "generateUrlRequest.parentApplication", target = "parentApplication")
    @Mapping(source = "fee", target = "fee")
    @Mapping(source = "accountDetails", target = "accountDetails")
    @Mapping(source = "expiryDate", target = "expiryDate")
    Submission toSubmission(GenerateUrlRequest generateUrlRequest, Fee fee, AccountDetails accountDetails, long expiryDate);

}
