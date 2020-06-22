package ca.bc.gov.open.jag.efilingapi.submission.mappers;

import ca.bc.gov.open.jag.efilingaccountclient.CsoAccountDetails;
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
    @Mapping(source = "fee", target = "fee")
    @Mapping(source = "csoAccountDetails", target = "csoAccountDetails")
    Submission toSubmission(GenerateUrlRequest generateUrlRequest, Fee fee, CsoAccountDetails csoAccountDetails);


}
