package ca.bc.gov.open.jag.efilingapi.submission.mappers;

import ca.bc.gov.open.jag.efilingapi.api.model.GenerateUrlRequest;
import ca.bc.gov.open.jag.efilingapi.submission.models.Submission;
import ca.bc.gov.open.jag.efilingcommons.submission.models.FilingPackage;
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
    @Mapping(source = "generateUrlRequest.navigationUrls", target = "navigationUrls")
    @Mapping(source = "filingPackage", target = "filingPackage")
    @Mapping(source = "expiryDate", target = "expiryDate")
    Submission toSubmission(
            String universalId,
            UUID submissionId,
            UUID transactionId,
            GenerateUrlRequest generateUrlRequest,
            FilingPackage filingPackage,
            long expiryDate);

}
