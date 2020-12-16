package ca.bc.gov.open.jag.submission;

import ca.bc.gov.open.jag.efilingcommons.service.EfilingSubmissionService;
import org.eclipse.microprofile.graphql.Description;
import org.eclipse.microprofile.graphql.GraphQLApi;
import org.eclipse.microprofile.graphql.Query;

import javax.inject.Inject;

@GraphQLApi
public class ReviewResource {

    @Inject
    EfilingSubmissionService efilingSubmissionService;

    @Query("submissionReview")
    @Description("Query a submission")
    public String getSubmission() {
        return "Hello";
    }
}
