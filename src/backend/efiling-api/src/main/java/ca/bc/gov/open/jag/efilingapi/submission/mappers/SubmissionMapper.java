package ca.bc.gov.open.jag.efilingapi.submission.mappers;

import ca.bc.gov.open.jag.efilingapi.api.model.GenerateUrlRequest;
import ca.bc.gov.open.jag.efilingcommons.model.FilingPackage;
import ca.bc.gov.open.jag.efilingapi.submission.models.Submission;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.UUID;

@Mapper(componentModel = "spring",
        injectionStrategy = InjectionStrategy.CONSTRUCTOR,
uses = { FilingPackageMapper.class })
public interface SubmissionMapper {

    @Mapping(source = "submissionId", target = "id")
    @Mapping(source = "universalId", target = "universalId")
    @Mapping(source = "transactionId", target = "transactionId")
    @Mapping(source = "generateUrlRequest.navigation", target = "navigation")
    @Mapping(source = "generateUrlRequest.clientApplication", target = "clientApplication")
    @Mapping(source = "filingPackage", target = "filingPackage")
    @Mapping(source = "expiryDate", target = "expiryDate")
    @Mapping(source = "rushedSubmission", target = "rushedSubmission")
    Submission toSubmission(
            UUID universalId,
            UUID submissionId,
            UUID transactionId,
            GenerateUrlRequest generateUrlRequest,
            FilingPackage filingPackage,
            long expiryDate,
            boolean rushedSubmission);

}
