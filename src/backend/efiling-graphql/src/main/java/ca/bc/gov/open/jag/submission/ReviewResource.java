package ca.bc.gov.open.jag.submission;

import ca.bc.gov.open.jag.efilingcommons.submission.EfilingStatusService;
import ca.bc.gov.open.jag.efilingcommons.submission.models.FilingPackage;
import ca.bc.gov.open.jag.submission.model.FilePackageOutput;
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

    public ReviewResource(EfilingStatusService efilingStatusService) {
        this.efilingStatusService = efilingStatusService;
    }

    @Query
    @Description("Query a package")
    public FilePackageOutput getSubmission(@Name("clientId") BigDecimal clientId, @Name("packageNo") BigDecimal packageNo) {
        logger.info("GraphQl Request received");

        Optional<FilingPackage> filePackage = efilingStatusService.findStatusByPackage(clientId, packageNo);

        FilePackageOutput filePackageOutput = new FilePackageOutput();

        if (filePackage.isPresent()) {
            filePackageOutput.setCourtClass(filePackage.get().getApplicationCode());
        }

        return filePackageOutput;
    }
}
