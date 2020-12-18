package ca.bc.gov.open.jag.submission;

import ca.bc.gov.open.jag.efilingcommons.submission.EfilingStatusService;
import ca.bc.gov.open.jag.efilingcommons.submission.models.FilingPackage;
import ca.bc.gov.open.jag.efilingcsoclient.mappers.FilePackageMapper;
import org.eclipse.microprofile.graphql.Description;
import org.eclipse.microprofile.graphql.GraphQLApi;
import org.eclipse.microprofile.graphql.Name;
import org.eclipse.microprofile.graphql.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.util.Optional;


@GraphQLApi
public class ReviewResource {

    Logger logger = LoggerFactory.getLogger(this.getClass());


    private final EfilingStatusService efilingStatusService;

    private final FilePackageMapper filePackageMapper;

    public ReviewResource(EfilingStatusService efilingStatusService, FilePackageMapper filePackageMapper) {
        this.efilingStatusService = efilingStatusService;
        this.filePackageMapper = filePackageMapper;
    }

    @Query
    @Description("Query a package")
    public ca.bc.gov.open.jag.submission.model.FilingPackage getSubmission(@Name("clientId") BigDecimal clientId, @Name("packageNo") BigDecimal packageNo) {
        logger.info("GraphQl Request received");

        Optional<FilingPackage> filePackage = efilingStatusService.findStatusByPackage(clientId, packageNo);


        if (filePackage.isPresent()) {
            
        }

        return new ca.bc.gov.open.jag.submission.model.FilingPackage();
    }
}
