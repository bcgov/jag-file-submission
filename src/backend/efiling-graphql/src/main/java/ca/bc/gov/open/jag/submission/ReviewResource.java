package ca.bc.gov.open.jag.submission;

import ca.bc.gov.open.jag.efilingcommons.model.FilePackage;
import ca.bc.gov.open.jag.efilingcommons.service.EfilingStatusService;
import org.eclipse.microprofile.graphql.Description;
import org.eclipse.microprofile.graphql.GraphQLApi;
import org.eclipse.microprofile.graphql.Name;
import org.eclipse.microprofile.graphql.Query;

import java.math.BigDecimal;


@GraphQLApi
public class ReviewResource {

    private final EfilingStatusService efilingStatusService;

    public ReviewResource(EfilingStatusService efilingStatusService) {
        this.efilingStatusService = efilingStatusService;
    }

    @Query("packageReview")
    @Description("Query a package")
    public FilePackage getSubmission(@Name("clientId") BigDecimal clientId, @Name("packageNo") BigDecimal packageNo) {

        return efilingStatusService.findStatusByPackage(clientId, packageNo).get();
    }
}
