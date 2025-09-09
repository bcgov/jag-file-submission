package ca.bc.gov.open.jag.efilingapi.submission.mappers;

import ca.bc.gov.open.jag.efilingapi.api.model.GenerateUrlResponse;
import ca.bc.gov.open.jag.efilingapi.submission.models.Submission;
import org.mapstruct.*;

import java.text.MessageFormat;

@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface GenerateUrlResponseMapper {

    @Mapping(source = "expiryDate", target = "expiryDate")
    @Mapping(target = "efilingUrl", source = "submission", qualifiedByName = "submissionIdToUrl")
    GenerateUrlResponse toGenerateUrlResponse(Submission submission, @Context String baseUrl);


    @Named("submissionIdToUrl")
    static String submissionIdToUrl(Submission submission, @Context String baseUrl) {
        return MessageFormat.format(
                "{0}?submissionId={1}&transactionId={2}",
                baseUrl,
                submission.getId(),
                submission.getTransactionId());
    }

}
