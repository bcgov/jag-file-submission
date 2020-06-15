package ca.bc.gov.open.jagefilingapi.submission.mappers;

import ca.bc.gov.open.jagefilingapi.api.model.GenerateUrlRequest;
import ca.bc.gov.open.jagefilingapi.fee.models.Fee;
import ca.bc.gov.open.jagefilingapi.submission.models.Submission;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface SubmissionMapper {


    @Mapping(source = "generateUrlRequest.documentProperties", target = "documentProperties")
    @Mapping(source = "generateUrlRequest.navigation", target = "navigation")
    @Mapping(source = "fee", target = "fee")
    Submission toSubmission(GenerateUrlRequest generateUrlRequest, Fee fee);


}
