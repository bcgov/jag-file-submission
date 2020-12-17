package ca.bc.gov.open.jag.submission;

import org.eclipse.microprofile.graphql.Description;
import org.eclipse.microprofile.graphql.GraphQLApi;
import org.eclipse.microprofile.graphql.Query;


@GraphQLApi
public class ReviewResource {

    @Query("submissionReview")
    @Description("Query a submission")
    public String getSubmission() {
        return "Hello";
    }
}
