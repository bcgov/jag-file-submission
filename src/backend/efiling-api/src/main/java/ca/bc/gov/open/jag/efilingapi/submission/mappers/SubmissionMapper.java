package ca.bc.gov.open.jag.efilingapi.submission.mappers;

import ca.bc.gov.open.jag.efilingapi.api.model.GenerateUrlRequest;
import ca.bc.gov.open.jag.efilingapi.submission.SubmissionKey;
import ca.bc.gov.open.jag.efilingapi.submission.models.Submission;
import ca.bc.gov.open.jag.efilingcommons.model.FilingPackage;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring",
        injectionStrategy = InjectionStrategy.CONSTRUCTOR,
uses = { FilingPackageMapper.class })
public interface SubmissionMapper {

    @Mapping(source = "submissionKey.submissionId", target = "id")
    @Mapping(source = "submissionKey.universalId", target = "universalId")
    @Mapping(source = "submissionKey.transactionId", target = "transactionId")
    @Mapping(source = "generateUrlRequest.navigation", target = "navigation")
    @Mapping(source = "generateUrlRequest.clientAppName", target = "clientAppName")
    @Mapping(source = "filingPackage", target = "filingPackage")
    @Mapping(source = "expiryDate", target = "expiryDate")
    @Mapping(source = "rushedSubmission", target = "rushedSubmission")
    Submission toSubmission(
            SubmissionKey submissionKey,
            GenerateUrlRequest generateUrlRequest,
            FilingPackage filingPackage,
            long expiryDate,
            boolean rushedSubmission);

}
